package com.biosimilarity.emeris.newmodel


import java.net.URL
import scala.collection.immutable.Stack
import m3.predef._
import com.biosimilarity.emeris.newmodel.JsonHelper.JsonCapable
import m3.FileSystem.Directory
import m3.FileSystem.File
import net.liftweb.json._
import java.util.UUID

object Model {
  
  case class Uid(value: String = UUID.randomUUID.toString) {
    def ===(right: Uid) = {
      val left = this
      left.value == right.value
    }
    def node(implicit ad: AgentDatabase): Node = ad.node(this)
  }
  
  sealed trait Node extends JsonCapable {

    def ===(right: Node) = {
      val left = this
      left.uid === right.uid
    }

    def uid: Uid

    def children(implicit ad: AgentDatabase) = ad.children(this)

    def isParentOf(child: Node)(implicit ad: AgentDatabase): Boolean =
      outgoingLinks.
        exists(_.to === child.uid)

    def incomingLinks(implicit ad: AgentDatabase) =
      ad.incomingLinks(uid)

    def outgoingLinks(implicit ad: AgentDatabase) =
      ad.outgoingLinks(uid)

    def visitDescendants(visitor: Node => Unit)(implicit ad: AgentDatabase): Unit = {
      var stack = Set[Node]()
      children.foreach { ch =>
        visitor(ch)
        ch.visitDescendants(visitor)
      }
    }

    def visitDescendants(visitor: PartialFunction[Node, Unit])(implicit ad: AgentDatabase): Unit = {
      visitDescendants { node: Node =>
        if (visitor.isDefinedAt(node)) visitor.apply(node)
      }
    }

    def parentLabels(implicit ad: AgentDatabase) = (
      incomingLinks
      .flatMap {
        _.to.node match {
          case l: Label => Some(l)
          case _ => None
        }
      })

    def canBeSeenBy(voyeur: Node)(implicit ad: AgentDatabase): Boolean = {
      def canBeSeenBy0(viewee: Node, stack0: Stack[Uid]): Boolean = {
        if (voyeur === viewee) true
        else {
          val il = viewee.incomingLinks
          val stack = stack0.push(viewee.uid)
          il.filterNot(l => stack.contains(l.from))
            .exists(l => canBeSeenBy0(l.from.node, stack))
        }
      }
      canBeSeenBy0(this, Stack())
    }

    //	public String canBeSeenBy(Node n) {
    //		Stack<Node> s = canBeSeenByImpl(n, new Stack<Node>());
    //		if ( s != null ) {
    //			List<Node> list = ListX.create();
    //			s.remove(this);
    //			list.addAll(s);
    //			list.add(n);
    //			java.util.Collections.reverse(list);
    //			return ListX.join(list, ":");
    //		} else {
    //			return null;
    //		}
    //	}
    //
    //	private Stack<Node> canBeSeenByImpl(final Node n, Stack<Node> stackState) {
    //		if ( this.equals(n) ) {
    //			return stackState;
    //		}
    //		for ( Link l : getIncomingLinks() ) {
    //			if ( !stackState.contains(l.getTo()) ) {
    //				stackState.add(l.getTo());
    //				Stack<Node> s = l.getFrom().canBeSeenByImpl(n, stackState);
    //				if ( s != null ) {
    //					return s;
    //				} else {
    //					stackState.pop();
    //				}
    //			}
    //		}
    //		return null;
    //	}

  }

  val types = List(
    classOf[Address],
    classOf[Agent],
    classOf[AgentData],
    classOf[Blob],
    classOf[Connection],
    classOf[Image],
    classOf[Label],
    classOf[Link],
    classOf[Phone],
    classOf[TextMessage]
  )

  case class AgentData(uid: Uid, nodes: List[Node]) extends JsonCapable

  case class Agent(name: String, uid: Uid = Uid()) extends Node 
  
  case class Label(name: String, icon: BlobRef, uid: Uid = Uid()) extends Node {

    def childLabels(implicit ad: AgentDatabase): Iterable[Label] =
      children
        .collect { case l: Label => l }

  }

  case class Connection(name: String, icon: BlobRef, remoteAgent: Uid, uid: Uid = Uid()) extends Node
  
  case class Address(value: String, uid: Uid = Uid()) extends Node 
  case class Phone(value: String, uid: Uid = Uid()) extends Node 
  case class TextMessage(value: String, uid: Uid = Uid()) extends Node

  case class BlobRef(agentUid: Uid, blobUid: Uid, filename: String)
  
  case class Image(blobRef: BlobRef, uid: Uid = Uid()) extends Node

  case class Blob(agentUid: Uid, filename: String, dataInBase64: String, uid: Uid = Uid()) extends Node {
    def asBlobRef = BlobRef(agentUid, uid, filename)
  }

  case class Link(from: Uid, to: Uid, uid: Uid = Uid()) extends Node

  trait AgentDAO {
    def fetch(uid: Uid): Option[Node]
    def query(labels: Iterable[Label], connections: Iterable[Connection]): Iterable[FilterAcceptCriteria]
    def childLabels(parent: Node): Iterable[Label]
    def connections: Iterable[Connection]
  }

  class LocalAgentDAO(database0: AgentDatabase) extends AgentDAO {

    implicit def database: AgentDatabase = database0

    override def fetch(uid: Uid) =
      database.
        nodes.
        find(_.uid == uid)

    override def query(labels: Iterable[Label], connections: Iterable[Connection]): Iterable[FilterAcceptCriteria] = {
      val filter = Filter(labels, connections, database)
      database.nodes.flatMap(filter.accept)
    }

    override def childLabels(parent: Node): Iterable[Label] = {
      val children = parent.children
      children.
        collect { case l: Label => l }
    }

    override def connections: Iterable[Connection] =
      database.
        nodes.
        collect { case c: Connection => c }

  }

  trait AgentDatabase {

    val uid: Uid
    
    lazy val dao = new LocalAgentDAO(this)
    
    def insert[T <: Node](n: T): T
    def fetch[T <: Node](uid: Uid)(implicit mf: Manifest[T]): Option[T]
    def nodes: Iterable[Node]
    def delete(uid: Uid)
    def dropDatabase()

    def incomingLinks(uid: Uid): Iterable[Link] = links.filter(_.to === uid)
    def outgoingLinks(uid: Uid): Iterable[Link] = links.filter(_.from === uid)
    def node[T <: Node](uid: Uid): T =
      nodes.
        find(_.uid === uid).
        getOrError("cannot find node " + uid).
        asInstanceOf[T]

    def children(parentNode: Node): Iterable[Node] = {
      val n = nodes.toIndexedSeq
      val ol = outgoingLinks(parentNode.uid)
      ol.map(l => l.to.node(this))
    }

    def links: Iterable[Link] = nodes.collect { case l: Link => l }

    def asJValue = asAgentData.asJValue
    
    def asJsonStr = pretty(render(asJValue))

    def asAgentData = AgentData(uid, nodes.toList)
    
  }
  
  case class NodeWrapper(node: Node) {
    
    def asJValue = Extraction.decompose(this)(JsonHelper.formats)

    def asJsonStr = pretty(render(asJValue))

  }

  class FileSystemDatabase(val directory: Directory, val uid: Uid) extends AgentDatabase {

    implicit def uidFile(uid: Uid): File = directory.file(uid.value + ".json")

    import JsonHelper._
    
    override def insert[T <: Node](n: T) = {
      uidFile(n.uid).
        write(NodeWrapper(n).asJsonStr)
      n
    }
    
    override def fetch[T <: Node](uid: Uid)(implicit mf: Manifest[T]) = uidFile(uid) match {
      case f if f.exists => Some(parse(f.readText).extract[NodeWrapper].node.asInstanceOf[T])
      case _ => None
    }
    
    override def nodes = directory.files.map{ f =>
      parseJson[NodeWrapper](f.readText).node
    }

    override def delete(uid: Uid) = uidFile(uid).delete
    
    override def dropDatabase() = {
      directory.deleteTree
    }

  }
    
}
