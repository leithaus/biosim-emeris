package com.biosimilarity.emeris.newmodel

import java.net.URL
import Model._


object Impl extends App { outer =>

  def random = _Uid(java.util.UUID.randomUUID.toString)
  
  case class _Uid(value: String) extends Uid {
    def asTUid[T <: Node] = _TUid[T](value)
  }
  case class _TUid[T <: Node](value: String) extends TUid[T] {
    def asTUid[T <: Node] = _TUid[T](value)
  }
  
  case class _Alias(name: String, iconUrl: String, uid: _Uid = random) extends Alias
  case class _Node(uid: _Uid = random) extends Node  
  case class _Agent(url: URL, uid: _Uid = random) extends Agent  
  case class _Label(name: String, uid: _Uid = random) extends Label
  case class _Connection(name: String, url: URL, uid: _Uid = random) extends Connection
  case class _Link(from: TUid[Node], to: TUid[Node], uid: Uid = random) extends Link

  implicit object database extends AgentDatabase {

    var _nodes = Vector[Node]()

    def link(from: Node, to: Node) = 
      add(_Link(from.uid.asTUid[Node], to.uid.asTUid[Node]))
    
    def add[T <: Node](n: T) = {
      _nodes +:= n
      n
    }
    
    val agent = add(_Agent(new URL("http://localhost/glen")))
    val rootAlias = add(_Agent(new URL("http://localhost/glen")))
    
    link(agent, rootAlias)
    
    object connections {
      lazy val bob = add(_Connection("Bob", new URL("http://localhost/bob")))
      lazy val bill = add(_Connection("Bill", new URL("http://localhost/bill")))
    }
    
    object labels {
      
      lazy val one = add(_Label("one"))
      lazy val oneA = add(_Label("A"))
      lazy val oneB = add(_Label("B"))
      lazy val two = add(_Label("two"))
      lazy val twoX = add(_Label("X"))
      lazy val twoY = add(_Label("Y"))

      lazy val linkup = {
        link(rootAlias, one)
        link(rootAlias, two)

        link(one, oneA)
        link(one, oneB)

        link(two, twoX)
        link(two, twoY)
      }
      
    }
    
    lazy val initialize = {
      labels.linkup
      link(connections.bob, labels.one)
    }
    
    def nodes = {
      initialize
      _nodes
    }
    
  }
  
  object dao extends LocalAgentDAO {
    val agentDatabase = outer.database
  }
  
  
}

