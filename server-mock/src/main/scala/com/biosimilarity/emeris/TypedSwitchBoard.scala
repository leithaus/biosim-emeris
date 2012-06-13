package com.biosimilarity.emeris

import m3.predef._
import com.biosimilarity.emeris.SocketProtocol.Message

trait TypedSwitchBoard extends SwitchBoard with Logging {
  
  abstract override def onJsonMessage(source: Socket, json: String) = {
    logger.debug("parsing incoming json message \n{}", json)
    val typedMsg = SocketProtocol.parseJson(json)
    logger.debug("typed message {}", typedMsg)
    onTypedMessage(source, typedMsg)
    super.onJsonMessage(source, json)
  }

  def onTypedMessage(source: Socket, msg: Message) = {}

}
