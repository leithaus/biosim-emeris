package com.biosimilarity.emeris

import SocketProtocol._
import AgentDataSet._
import net.liftweb.json.JsonAST.JString
import SocketProtocol._
import AgentDataSet._
import net.liftweb.json.JsonAST.JString
import com.biosimilarity.emeris.testdata.InsertMinimalDataSet

trait ModelMutatingSwitchBoard extends Object with TypedSwitchBoard with PubSubSwitchBoard {

  override def onTypedMessage(source: Socket, msg: Message) = {
    logger.debug("received typed message '{}' from {}", msg, source)
    val dataSet = getDataSet(source)
    msg.body match {
      case cn: CreateNodes => {
        cn.nodes.foreach(dataSet.add)
        source.rawSend(msg.asJsonStr)
      }
      case rn: RemoveNodes => rn.uidList.foreach(dataSet.remove)
      case ClearDataSet() => dataSet.clear
      case b => logger.warn("ignoring message {}", b)
    }
    super.onTypedMessage(source, msg)
  }
  
}
