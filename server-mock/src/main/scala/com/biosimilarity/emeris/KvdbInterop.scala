package com.biosimilarity.emeris


import PersistedMonadicKVDBNet.KVDBNodeFactory.{ loopBack, ptToPt }
import java.net.URI
import com.biosimilarity.emeris.AgentDataSet.Uid
import com.biosimilarity.emeris.AgentDataSet.Node
import com.biosimilarity.emeris.AgentDataSet.NodeWrapper
import PersistedMonadicKVDBNet._
import scala.util.continuations._
import com.biosimilarity.lift.model.store.CnxnCtxtLeaf
import com.biosimilarity.lift.model.store.CnxnCtxtBranch
import scala.util.continuations._
import com.biosimilarity.emeris.AgentDataSet.{Uid, NodeWrapper, Node}
import JsonHelper._
import net.liftweb.json.JsonDSL._
import net.liftweb.json.Serialization.{read, write}
import net.liftweb.json._
import m3.FileSystem.Directory
import m3.FileSystem.File
import m3.predef._
import m3.LockFreeMap


object KvdbFactory extends FakeKvdbFactory

trait KvdbFactory {
  
  def createDatabase(uid: Uid): KvdbInterop
  def database(uid: Uid): KvdbInterop
  
  def databases: Iterable[KvdbInterop]
  
}

trait LoggingKvdbFactory extends KvdbFactory with Logging {
  
  abstract override def createDatabase(uid: Uid): KvdbInterop = {
    logger.debug("createDatabase({})", uid)
    super.createDatabase(uid)
  }
  
  abstract override def database(uid: Uid): KvdbInterop = {
    logger.debug("database({})", uid)
    super.database(uid)
  }

  abstract override def databases: Iterable[KvdbInterop] = {
    logger.debug("databases")
    super.databases
  }

}

trait FakeKvdbFactory extends KvdbFactory with Logging {

  lazy val databasesRootDirectory = {
    val d = Directory("database")
    d.makeDirectories
    d
  }
  
  def createDatabase(uid: Uid) = {
    logger.debug("createDatabase({})", uid)
    (databasesRootDirectory \\ uid.value)
      .makeDirectories()
    database(uid)
  }
  
  def databaseGen(uid: Uid) = {
	(databasesRootDirectory \\ uid.value) match {
	  case d if d.exists => new FakeKvdbInterop with LoggingKvdbInterop {
        val directory = d
        val agentUid = uid
	  }
	  case d => sys.error("database " + d + " does not exist")
	}
  }
  
  def database(agentUid: Uid) = { 
    logger.debug("database({})", agentUid)
    databaseGen(agentUid)
  }
  
  def databases = 
    databasesRootDirectory
      .subDirectories
      .map(d=>databaseGen(Uid(d.name)))
 
}


trait KvdbInterop extends Serializable {
  def clearDatabase = allNodes.map(_.uid).foreach(delete)
  def delete(uid: Uid)
  def store(node: Node)
  def allNodes: List[Node]
  def fetchOne[T <: Node](uid: Uid): Option[T] 
  def agentUid: Uid
  def name: String = agentUid.value
  def dropDatabase()
}

trait LoggingKvdbInterop extends KvdbInterop with Logging {
  
  abstract override def clearDatabase = {
    log("clearDatabase()")
    super.clearDatabase
  }
  
  abstract override def delete(uid: Uid) = {
    log("delete(" + uid + ")")
    super.delete(uid)
  }
  
  abstract override def store(node: Node) = {
    log("store(" + node + ")")
    super.store(node)
  }
  
  abstract override def allNodes: List[Node] = {
    log("allNodes")
    super.allNodes
  }
  
  abstract override def fetchOne[T <: Node](uid: Uid): Option[T] = {
    log("fetchOne(" + uid + ")")
    super.fetchOne(uid)    
  }
  
  abstract override def dropDatabase() = {
    log("dropDatabase()")
    super.dropDatabase()
  }
  
  def log(msg: String) = logger.debug("Agent({}) - {}", agentUid, msg)
  
}

trait FakeKvdbInterop extends KvdbInterop {
	
  import JsonHelper._

  val directory: Directory

  override def clearDatabase = 
    directory
      .files
      .foreach(_.delete())
  
  implicit def uidFile(uid: Uid): File = directory.file(uid.value + ".json")

  def delete(uid: Uid) = uidFile(uid).delete

  def dropDatabase() = new net.model3.newfile.Directory(directory.asJioFile).deleteTree()

  def store(node: Node) =  uidFile(node.uid).write(NodeWrapper(node).asJsonStr)

  def allNodes: List[Node] = {
    directory
    	.files
    	.filter(_.name.endsWith(".json"))
    	.flatMap(f=>load(f))
  }

  def fetchOne[T <: Node](uid: Uid): Option[T] = load(uid).asInstanceOf[Option[T]]

  def load(file: File): Option[Node] = {
    if ( file.exists ) Some(
      parse(file.readText)
    	  .extract[NodeWrapper]
    	  .node
    ) else None
  }
  
  
}


trait RealKvdbInterop extends KvdbInterop with Logging {
  
  import com.biosimilarity.lift.model.store.CnxnConversionStringScope._
  import com.biosimilarity.lift.model.store.usage._
  import com.biosimilarity.lift.model.store._
  import com.biosimilarity.lift.model.store.MonadicTermTypes$RBound$

  val host: String = "localhost"
  val database: String
  
  @transient lazy val kvdbImpl = 
        ptToPt(
          new URI( "agent", null, "localhost", 5672, "/localex", null, null ),
          new URI( "agent", null, "localhost", 5672, "/remoteex", null, null )
        )

  def delete(uid: Uid) = reset {
    for(
        eo <- kvdbImpl.get(nodeToTermKey((uid))) ;
        e <- eo
    ) {
      // do nothing and it will disappear forever
    }
  }
  
  lazy val queryForAll = new CnxnCtxtBranch[String,String,String]( 
        "node", 
        List( 
            new CnxnCtxtBranch[String,String,String]( 
                "uid"
                , List( new CnxnCtxtLeaf[String,String,String]( 
                    Right[String,String]( "Var" ) 
                  ) 
                ) 
            ) 
    )   
  );


  def nodeToTermKey(uid: Uid): CnxnCtxtBranch[String,String,String] = {
    new CnxnCtxtBranch[String,String,String]( 
        "node", 
        List( 
            new CnxnCtxtBranch[String,String,String]( 
                "uid"
                , List( new CnxnCtxtLeaf[String,String,String]( 
                    Left[String,String]( uid.value ) 
                  ) 
                ) 
            ) 
        )   
    )
  }
  
  def nodeToTermKey(node: Node): CnxnCtxtBranch[String,String,String] = nodeToTermKey(node.uid)

  def store(node: Node) = reset { 
    val key = nodeToTermKey(node)
    val value = NodeWrapper(node).asJsonStr
    println("storing -- " + nodeToTermKey(node))
    kvdbImpl.put(key, value)
    println("stored -- " + nodeToTermKey(node))
  }

  def allNodes: List[Node] = query(queryForAll)

  def fetchOne[T <: Node](uid: Uid): Option[T] = query[T](nodeToTermKey(uid)).optMap(_.head)

  def queryX[T <: Node](queryTerms: String): List[T] = List[T]()
//  def query[T <: Node](queryTerms: String): List[T] = {
//    query(string2cnxnCtxLabel(queryTerms))
//  }
  
  def query[T <: Node](queryTermsAsLabel: CnxnCtxtBranch[String,String,String]): List[T] = {
    var list = List[T]()
    reset {
      for (
        i <- kvdbImpl.fetch(queryTermsAsLabel) ;
        j <- i
      ) {
        def append(json: String) = {
          val nw = parse(json).extract[NodeWrapper]
          list ::= nw.node.asInstanceOf[T]          
        }
        j match {
//          case PersistedMonadicKVDBNet.mTT.RBoundAList( Some( mTT.Ground( inc ) ), soln ) => {
//            append( inc )
//          }
//          case PersistedMonadicKVDBNet.mTT.RBoundHM( Some( mTT.Ground( inc ) ), soln ) => {
//            append( inc )
//          }
//          case PersistedMonadicKVDBNet.mTT.Ground( inc ) => {
//            append( inc )
//          }
          case a => logger.warn("don't know how to handle " + a)
        }
      }
    }
    list
  }
  
}

