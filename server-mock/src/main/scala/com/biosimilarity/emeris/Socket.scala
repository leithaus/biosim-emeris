package com.biosimilarity.emeris

import com.biosimilarity.emeris.AgentDataSet.Uid
import java.util.UUID

trait Socket {

  val uuid: UUID
  val agentUid: Uid

  def rawSend(data: String)

}
