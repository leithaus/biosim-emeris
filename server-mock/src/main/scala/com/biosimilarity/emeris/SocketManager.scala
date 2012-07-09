package com.biosimilarity.emeris


import m3.predef._
import com.biosimilarity.emeris.newmodel.Model._
import biosim.client.messages.protocol.Request
import biosim.client.messages.protocol.QueryRequest
import com.biosimilarity.emeris.newmodel.DatabaseFactory

object SocketManager extends Logging{
    
  var sockets = List[Socket]()
  var socketsByAgentUid = Map[Uid,Vector[Socket]]()
  
  def create(agentUid: Uid) = {
    val s = new WebSocketImpl(agentUid)
	synchronized { 
	  sockets ::= s 
	  socketsByAgentUid += 
	    (  agentUid ->
	       (s +: socketsByAgentUid.
	        get(agentUid).
	        getOrElse(Vector()))
	    )
	}
    s
  }
  
  def remove(s: Socket) = {
	synchronized {
	  sockets = sockets.filterNot(_ == s)
	}	
  }
  
  def messageReceived(s: Socket, json: String) = try {
    SwitchBoard.onJsonMessage(s, json)
  } catch {
    case th => logger.warn("error with {}\n{}", s.agentUid, json, th)
  }
  
}
