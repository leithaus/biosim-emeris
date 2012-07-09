package com.biosimilarity.emeris


import java.util.UUID
import m3.predef._
import net.liftweb.json._
import net.liftweb.json.JsonDSL._
import com.biosimilarity.emeris.newmodel.DatabaseFactory
import biosim.client.messages.protocol.Request
import biosim.client.messages.protocol.QueryRequest
import biosim.client.messages.protocol.ConnectionsRequest
import biosim.client.messages.protocol.ConnectionsResponse
import com.biosimilarity.emeris.newmodel.Model.Node
import biosim.client.model.{ 
   Connection => ClientConnection, 
   Blob => ClientBlob, 
   Label => ClientLabel, 
   Link => ClientLink, 
   Uid => ClientUid
   }
import biosim.client.messages.model.{ BlobRef => ClientBlobRef }
import com.biosimilarity.emeris.newmodel.Model.Uid
import scala.collection.JavaConverters._
import biosim.client.messages.protocol.CreateNodesRequest
import biosim.client.messages.protocol.Response
import predef._
import com.biosimilarity.emeris.newmodel.Model._
import biosim.client.messages.protocol.LabelRequest
import biosim.client.messages.protocol.LabelResponse
import biosim.client.messages.model.MConnection
import biosim.client.messages.model.MLabel

object SwitchBoard extends Logging {

  implicit def toServerUid(clientUid: ClientUid) = Uid(clientUid.getValue)
  implicit def toClientUid(serverUid: Uid) = new ClientUid(serverUid.value)
  
  implicit def toServerBlobRef(cbr: ClientBlobRef) = BlobRef(cbr.getAgentUid, cbr.getBlobUid, cbr.getFilename)
  implicit def toClientBlobRef(br: BlobRef) = new ClientBlobRef(br.agentUid, br.blobUid, br.filename)

  def onConnect(socket: Socket) = {}
  
  def onJsonMessage(socket: Socket, json: String) = {
    logger.debug("parsing incoming json message \n{}", json)
    val request = BiosimServerSerializer.fromJson[Request](json, classOf[Request])
    onRequest(socket, request)
  }

  def onRequest(socket: Socket, request: Request) = {
    val db = DatabaseFactory.database(socket.agentUid)
    val dao = db.dao
    val responseBody = request.getRequestBody match {
      case req: LabelRequest => {
        val body = new LabelResponse
        val parent = db.fetch[Node](req.getParent).get
        val l = new MLabel
        val children = dao.
          childLabels(parent).
          map(cl=>toClientUid(cl.uid)).
          toList
        parent match {
          case label: Label => {
            l.setName(label.name)
            l.setUid(label.uid)
            l.setIcon(label.icon)
          }
          case agent: Agent => {
            l.setName(agent.name)
            l.setUid(agent.uid)
          }
          case _ => sys.error("don't know how to handle type " + parent)
        }
        l.setChildren(children.asJava)
        
        body.setLabel(l)
        Some(body)
      }
      case qr: QueryRequest => None
      case cr: ConnectionsRequest => {
        val connections = dao.connections.map { c =>
          val cc = new MConnection(c.uid, c.name)
          c.icon.foreach(i=>cc.setIcon(i))
          cc
        }
        Some(new ConnectionsResponse(connections.toList))
      }
      case cnr: CreateNodesRequest => {
        cnr.getNodes.asScala.map {
          case b: ClientBlob => Blob(b.getRef.getAgentUid, b.getRef.getFilename, b.getDataInBase64, b.getUid)
          case l: ClientLabel => Label(l.getName, l.getIconRef, l.getUid)
          case l: ClientLink => Link(l.getFrom, l.getTo)
        } foreach (n => db.insert(n))
        None
      }
      case _ => sys.error("don't know how to handle type " + request.getClass)
    }
    responseBody.foreach { body =>
      val response = new Response
      response.setRequestUid(request.getUid)
      response.setResponseBody(body)
      val jsonResponse = BiosimServerSerializer.toJson(response, response.getClass)
      socket.rawSend(jsonResponse)
    }
  }
  
}
