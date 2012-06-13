package com.biosimilarity.emeris

import org.eclipse.jetty.websocket.WebSocket
import java.util.UUID
import com.biosimilarity.emeris.AgentDataSet.Uid
import m3.predef._

case class WebSocketImpl(val agentUid: Uid, switchBoard: SwitchBoard, val uuid: UUID = UUID.randomUUID) 
		extends WebSocket 
		with WebSocket.OnTextMessage 
		with Socket
		with Logging
{
  
  var connection: WebSocket.Connection = _
  
  def onOpen(connection: WebSocket.Connection) = {
    this.connection = connection
    connection.setMaxTextMessageSize(Integer.MAX_VALUE)
    switchBoard.onConnect(this)
  }
  
  def onClose(closeCode: Int, message: String) = SocketManager.remove(this)
  
  def onMessage(message: String) = SocketManager.messageReceived(this, message)
  
  def rawSend(data: String) = {
    logger.debug("sending message\n{}", data)
    connection.sendMessage(data)
  }
      
}
