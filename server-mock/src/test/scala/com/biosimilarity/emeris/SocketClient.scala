package com.biosimilarity.emeris


import org.eclipse.jetty.websocket.WebSocketClientFactory
import java.net.URI
import org.eclipse.jetty.websocket.WebSocket
import java.util.concurrent.TimeUnit
import java.util.UUID
import java.util.Date

object SocketClient extends App {

  
  
  val factory = new WebSocketClientFactory();
  factory.start();

  val client = factory.newWebSocketClient();

  val connection = client.open(new URI("ws://127.0.0.1:8080/ws?userId=1"), new WebSocket.OnTextMessage() {
     def onOpen(connection: WebSocket.Connection) = Unit
     def onClose(closeCode: Int, message: String) = Unit
     def onMessage(message: String) = println(message)
  }).get(5, TimeUnit.SECONDS);

  val uuid = UUID.randomUUID().toString()   
  while ( true ) { {
    val message = uuid + " Hello World " + new Date
    println("sending " + message)
	connection.sendMessage(message)
	synchronized(wait(1000))
    }
  }
  
}

class SocketClient {}

