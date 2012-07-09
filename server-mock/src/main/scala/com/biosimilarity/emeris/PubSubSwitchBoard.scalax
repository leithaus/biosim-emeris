package com.biosimilarity.emeris

import m3.predef._
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.SynchronizedBuffer

trait PubSubSwitchBoard extends SwitchBoard with Logging {
  
  abstract override def onJsonMessage(source: Socket, message: String) = {  
    super.onJsonMessage(source, message)
    logger.debug("received message '{}' from {}", message, source)
//    SocketManager.sockets
//            .filterNot(_ == source)
//            .foreach(_.rawSend(message))
  }
  
}

