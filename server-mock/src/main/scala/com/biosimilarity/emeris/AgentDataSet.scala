package com.biosimilarity.emeris


import java.util.UUID
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.HashSet
import scala.collection.mutable.SynchronizedBuffer
import scala.collection.mutable.SynchronizedSet
import java.io.Writer
import net.liftweb.json._
import net.liftweb.json.JsonDSL._
import net.model3.lang.ClassX
import net.liftweb.json._
import net.liftweb.json.JsonDSL._
import JsonHelper._
import SocketProtocol.CreateNodes
import net.model3.text.ThreadSafeSimpleDateFormat
import net.model3.chrono.DateTime
import net.model3.chrono.DateFormatter

object AgentDataSet {

    def jsonTypes: List[Class[_]] = List(
        classOf[Address]
        , classOf[Alias]
        , classOf[Label]
        , classOf[Link]
        , classOf[NodeWrapper]
        , classOf[Need]
        , classOf[Offer]
        , classOf[Person]
        , classOf[Phone]
        , classOf[Image]
        , classOf[TextMessage]
    )
    
    case class NodeWrapper(node: Node) {
            	
      	def asJValue = {
		  import JsonHelper._
      	  import net.liftweb.json.Serialization.write
      	  decompose(this)
      	}

      	def asJsonStr = pretty(render(asJValue))

    }
    
	trait Node {
	  
		val uid: Uid
		val created: DateTime
		
		def typeName: String = {
		  val classname = getClass.getName
		  classname.substring(classname.indexOf("$")+1)
		}
      	
      	override def hashCode = uid.hashCode
      	
      	override def equals(x: Any) = {
		  x match {
		    case n: Node => n.uid == uid
		    case _ => false
		  }
		}
      	
      	def asJValue = {
		  import JsonHelper._
      	  import net.liftweb.json.Serialization.write
      	  decompose(this)
      	}

      	def asJsonStr = pretty(render(asJValue))

	}

	case class Uid(value: String = UUID.randomUUID.toString) {
	  override def toString = value
	}
	
	case class Alias(name: String, agent: Uid, iconUrl: String, created: DateTime = new DateTime(), uid: Uid = Uid()) extends Node
	
	case class Address(value: String, created: DateTime = new DateTime(), uid: Uid = Uid()) extends Node 

	case class Label(name: String, created: DateTime = new DateTime(), uid: Uid = Uid()) extends Node

	case class Link(from: Uid, to: Uid, created: DateTime = new DateTime(), uid: Uid = Uid()) extends Node 
	
	case class Person(name: String, created: DateTime = new DateTime(), uid: Uid = Uid()) extends Node
	
	case class Phone(value: String, created: DateTime = new DateTime(), uid: Uid = Uid()) extends Node 

	case class Offer(description: String, created: DateTime = new DateTime(), uid: Uid = Uid()) extends Node 

	case class Need(description: String, created: DateTime = new DateTime(), uid: Uid = Uid()) extends Node 
	
	case class Image(dataInBase64: String, agent: Uid, filename: String, created: DateTime = new DateTime(), uid: Uid = Uid()) extends Node {
	  def url = "/blobs/" + agent.value + "/" + uid.value + "." + ext
	  def ext = filename.substring(filename.lastIndexOf(".")+1)
	}
	
	case class TextMessage(text: String, created: DateTime = new DateTime(), uid: Uid = Uid()) extends Node
	
}


class AgentDataSet(kvdb: KvdbInterop) {
  
  import AgentDataSet._
	
  def nodes = kvdb.allNodes
  
  def add[T<:Node](n: T) = {
    kvdb.store(n)
    n
  }
  
  def remove(uid: Uid) = kvdb.delete(uid)
  
  def clear = kvdb.clearDatabase
  
  def asCreateNodes = CreateNodes(kvdb.agentUid, nodes)

}


