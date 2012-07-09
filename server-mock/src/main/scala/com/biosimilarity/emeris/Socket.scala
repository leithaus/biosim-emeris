package com.biosimilarity.emeris

import java.util.UUID
import com.biosimilarity.emeris.newmodel.Model.Uid

trait Socket {

  val uuid: UUID
  val agentUid: Uid

  def rawSend(data: String)

}
