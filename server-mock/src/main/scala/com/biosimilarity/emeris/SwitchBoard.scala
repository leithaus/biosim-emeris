package com.biosimilarity.emeris


import java.util.UUID
import m3.predef._

import net.liftweb.json._
import net.liftweb.json.JsonDSL._
import JsonHelper._

trait SwitchBoard extends Logging {

  def getDataSet(socket: Socket): AgentDataSet 

  def onConnect(socket: Socket) = {
    val jsonObject = getDataSet(socket).asCreateNodes.createMessage.asJValue
    val json = pretty(render(jsonObject))
    logger.debug("initialDataLoad\n{}", json)
    socket.rawSend(json)
  }
  
  def onJsonMessage(socket: Socket, message: String) = {}
  
}
