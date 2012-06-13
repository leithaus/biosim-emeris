package com.biosimilarity.emeris


import com.biosimilarity.emeris.SocketProtocol.Message
import com.biosimilarity.emeris.SocketProtocol.CreateNodes
import m3.LockFreeMap
import AgentDataSet.Uid
import m3.predef._

object SocketManager extends Logging{
  
  var sockets = List[Socket]()
  
  val switchBoard = new ModelMutatingSwitchBoard {
    def getDataSet(socket: Socket) = DataSetManager(socket.agentUid)
    override def onTypedMessage(source: Socket, msg: Message) = {
      super.onTypedMessage(source, msg)
    }

  }
  
  def create(agentUid: Uid) = {
    val s = new WebSocketImpl(agentUid, switchBoard)
	synchronized { 
	  sockets ::= s 
	}
    s
  }
  
  def remove(s: Socket) = {
	synchronized {
	  sockets = sockets.filterNot(_ == s)
	}	
  }
  
  def messageReceived(s: Socket, json: String) = try {
    switchBoard.onJsonMessage(s, json)
  } catch {
    case th => logger.warn("error with {}\n{}", s.agentUid, json, th)
  }
  
}
