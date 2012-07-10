package com.biosimilarity.emeris


import m3.predef._
import com.biosimilarity.emeris.newmodel.Model._
import biosim.client.messages.protocol.Request
import biosim.client.messages.protocol.QueryRequest
import com.biosimilarity.emeris.newmodel.DatabaseFactory
import org.eclipse.jetty.io.EofException
import biosim.client.messages.protocol.ResponseBody

object SocketManager extends Logging {
    
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
  
  def broadcast(agentUid: Uid, responseBody: ResponseBody) = {
    socketsByAgentUid(agentUid).
      foreach { socket =>
        try {
          socket.send(responseBody)
        } catch {
          case e: EofException => {
            logger.debug("removing dead web socket " + socket.agentUid + "-" + socket.uuid)
            remove(socket)
          }
        }
      }
  }
  
  def remove(s: Socket) = {
	synchronized {
      logger.debug("remove socket " + s.agentUid + "-" + s.uuid)
	  sockets = sockets.filterNot(_ == s)
	  socketsByAgentUid +=
	    (  s.agentUid ->
	       (socketsByAgentUid.
	         get(s.agentUid).
	         getOrElse(Vector()).
	         filterNot(_.uuid == s.uuid)
	       )
	    )
	}	
  }
  
  def messageReceived(s: Socket, json: String) = try {
    SwitchBoard.onJsonMessage(s, json)
  } catch {
    case th => logger.warn("error with {}\n{}", s.agentUid, json, th)
  }
  
}
