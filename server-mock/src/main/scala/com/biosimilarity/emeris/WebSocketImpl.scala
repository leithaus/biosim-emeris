package com.biosimilarity.emeris

import org.eclipse.jetty.websocket.WebSocket
import java.util.UUID
import m3.predef._
import com.biosimilarity.emeris.newmodel.Model.Uid

case class WebSocketImpl(val agentUid: Uid, val uuid: UUID = UUID.randomUUID) 
		extends WebSocket 
		with WebSocket.OnTextMessage 
		with Socket
		with Logging
{
  
  var connection: WebSocket.Connection = _
  
  def onOpen(connection: WebSocket.Connection) = {
    this.connection = connection
    connection.setMaxTextMessageSize(Integer.MAX_VALUE)
    SwitchBoard.onConnect(this)
  }
  
  def onClose(closeCode: Int, message: String) = SocketManager.remove(this)
  
  def onMessage(message: String) = SocketManager.messageReceived(this, message)
  
  def rawSend(data: String) = {
    logger.debug("sending message\n{}", data)
    connection.sendMessage(data)
  }
      
}
