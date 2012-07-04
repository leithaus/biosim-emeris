package com.biosimilarity.emeris.newmodel


import java.net.URL
import scala.collection.immutable.Stack

object Model {
	
	trait Uid {
	  def value: String
	  def ===(right: Uid) = {
	    val left = this
	    left.value == right.value 
	  }
	  def asTUid[T <: Node]: TUid[T]
	}
	
	trait TUid[T <: Node] extends Uid {
	  def node(implicit ad: AgentDatabase): T = ad.node(this)
	}
	
	
	trait Agent extends Node {
	  def url: URL
	}
	
	
	trait Node {
	  
	  type T <: Node
	
	  def ===(right: Node) = {
	    val left = this
	    left.uid === right.uid 
	  }
	
	  def uid: TUid[T]
	  
	  def children(implicit ad: AgentDatabase) = ad.children(this)
	
	  def isParentOf(child: Node)(implicit ad: AgentDatabase): Boolean =
	    outgoingLinks.exists(_.to === child.uid)
	
	  def incomingLinks(implicit ad: AgentDatabase) = 
	    ad.incomingLinks(uid)
	
	  def outgoingLinks(implicit ad: AgentDatabase) = 
	    ad.outgoingLinks(uid)
	
	  def visitDescendants(visitor: Node => Unit )(implicit ad: AgentDatabase): Unit = {
	    var stack = Set[Node]()
	    children.foreach { ch =>
	      visitor(ch)
	      ch.visitDescendants(visitor)
	    }
	  }
	
	  def visitDescendants(visitor: PartialFunction[Node,Unit])(implicit ad: AgentDatabase): Unit = {
	    visitDescendants { node: Node => 
	      if ( visitor.isDefinedAt(node) ) visitor.apply(node)
	    }
	  }
	  
	  def parentLabels(implicit ad: AgentDatabase) = ( 
	    incomingLinks
	      .flatMap { _.to.node match {
	          case l: Label => Some(l)
	          case _ => None
	        } 
	      }
	  )
	  
	  def canBeSeenBy(voyeur: Node)(implicit ad: AgentDatabase): Boolean = {
	    def canBeSeenBy0(viewee: Node, stack0: Stack[Uid]): Boolean = {
	      if ( voyeur === viewee ) true
	      else {
	        val il = viewee.incomingLinks
	        val stack = stack0.push(viewee.uid)
	        il.filterNot( l => stack.contains(l.from) )
	          .exists( l => canBeSeenBy0(l.from.node, stack) )
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
	
	
	trait Label extends Node {
	  
	  def name: String
	  
	  def childLabels(implicit ad: AgentDatabase): Iterable[Label] =
	    children
	      .collect { case l: Label => l }
	  
	}
	
	
	trait Alias extends Node {
	  def name: String
	  def iconUrl: String
	}
	
	
	trait Connection extends Node {
	  def name: String
	  def url: URL
	}
	
	
	trait Blob extends Node {
	  def extension: String
	  def contentType: String
	  def data: TUid[BlobData]
	}
	
	
	trait BlobData extends Node {
	  def asStream: java.io.InputStream
	}
	
	
	trait Message extends Node {
	  def from: Agent
	  def to: Agent
	  def subject: String
	  def message: String
	}
	
	
	trait Link extends Node {
	  def from: TUid[Node]
	  def to: TUid[Node]
	}
	
	
	trait AgentDAO {
	  
	  def query(labels: Iterable[Label], connections: Iterable[Connection]): Iterable[FilterAcceptCriteria]
	  def childLabels(parent: Label): Iterable[Label]
	  def connections: Iterable[Connection]
	  
	}
	
	trait LocalAgentDAO {
	  implicit def agentDatabase: AgentDatabase
	
	  def query(labels: Iterable[Label], connections: Iterable[Connection]): Iterable[FilterAcceptCriteria] = {
	    val filter = Filter(labels, connections, agentDatabase)
	    agentDatabase.nodes.flatMap(filter.accept)
	  }
	  
	  def labels: Iterable[Label] = agentDatabase.nodes.collect{ case l: Label => l }
	  def connections: Iterable[Connection] = agentDatabase.nodes.collect{ case c: Connection => c }
	  
	}
	
	
	trait AgentDatabase {
	  
	  def add[T <: Node](n: T): T
	  
	  def incomingLinks(uid: Uid): Iterable[Link] = links.filter(_.to === uid)
	  def outgoingLinks(uid: Uid): Iterable[Link] = links.filter(_.from === uid)
	  def node[T <: Node](uid: Uid): T = nodes.find(_.uid === uid).getOrElse(sys.error("cannot find node " + uid)).asInstanceOf[T]
	  
	  def children(parentNode: Node): Iterable[Node] = 
	    outgoingLinks(parentNode.uid)
	      .map(l=>l.to.node(this))
	      
	  def links: Iterable[Link] = nodes.collect { case l: Link => l }
	  
	  def nodes: Iterable[Node]
	  
	}

}
