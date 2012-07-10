package com.biosimilarity.emeris

import java.util.UUID
import com.biosimilarity.emeris.newmodel.Model.Uid
import biosim.client.messages.protocol.ResponseBody
import biosim.client.messages.protocol.Response
import biosim.client.messages.protocol.Request
import biosim.client.messages.model.{ Uid => ClientUid }

trait Socket {

  val uuid: UUID
  val agentUid: Uid

  def rawSend(data: String)
  
  def send(responseBody: ResponseBody): Unit = send(responseBody, None)
  def send(responseBody: ResponseBody, request: Request): Unit = send(responseBody, Some(request))

  private def send(responseBody: ResponseBody, request: Option[Request]): Unit = {
    val response = new Response
    response.setUid(ClientUid.random)
    request.map(_.getUid).foreach(response.setRequestUid)
    response.setResponseBody(responseBody)
    val jsonResponse = BiosimServerSerializer.toJson(response, response.getClass)
    rawSend(jsonResponse)
  }

}
