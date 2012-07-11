package com.biosimilarity.emeris


import java.util.UUID
import m3.predef._
import net.liftweb.json._
import net.liftweb.json.JsonDSL._
import com.biosimilarity.emeris.newmodel.DatabaseFactory
import biosim.client.messages.protocol.Request
import biosim.client.messages.protocol.QueryRequest
import com.biosimilarity.emeris.newmodel.Model.Node
import biosim.client.messages.model.{ 
   Uid => ClientUid
   }
import biosim.client.messages.model.{ BlobRef => ClientBlobRef }
import com.biosimilarity.emeris.newmodel.Model.Uid
import scala.collection.JavaConverters._
import biosim.client.messages.protocol.CreateNodesRequest
import biosim.client.messages.protocol.Response
import predef._
import com.biosimilarity.emeris.newmodel.Model._
import biosim.client.messages.model.MConnection
import biosim.client.messages.model.MLabel
import biosim.client.messages.model.MNode
import biosim.client.messages.protocol.FetchRequest
import biosim.client.messages.protocol.FetchResponse
import biosim.client.messages.protocol.SelectRequest
import biosim.client.messages.protocol.SelectResponse
import biosim.client.messages.protocol.CreateNodesResponse
import biosim.client.messages.model.MBlob
import biosim.client.messages.model.MLink
import biosim.client.messages.protocol.ConnectionScopedRequestBody

object SwitchBoard extends Logging {

  implicit def toServerUid(clientUid: ClientUid) = Uid(clientUid.getValue)
  implicit def toClientUid(serverUid: Uid) = new ClientUid(serverUid.value)
  
  implicit def toServerBlobRef(cbr: ClientBlobRef) = cbr match {
    case null => None
    case _ => Some(BlobRef(cbr.getAgentUid, cbr.getBlobUid, cbr.getFilename))
  }
  implicit def toClientBlobRef(bro: Option[BlobRef]) = bro match {
    case None => null
    case Some(br) =>new ClientBlobRef(br.agentUid, br.blobUid, br.filename)
  }

  def onConnect(socket: Socket) = {}
  
  def onJsonMessage(socket: Socket, json: String) = {
    logger.debug("parsing incoming json message \n{}", json)
    val request = BiosimServerSerializer.fromJson[Request](json, classOf[Request])
    onRequest(socket, request)
  }

  def onRequest(socket: Socket, request: Request) = {
  	val localAgentDb = DatabaseFactory.database(socket.agentUid)
    val responseBody = request.getRequestBody match {
      case csrb: ConnectionScopedRequestBody => {
        val db = csrb.getConnectionUid match {
          case null => localAgentDb
          case uid => {
            val localConn = localAgentDb.fetch[Connection](uid).get
            val remoteAgentDb = DatabaseFactory.database(localConn.remoteAgent)
            val remoteConn = remoteAgentDb.fetch[Connection](uid).get
            new FilteredDatabase(remoteAgentDb, remoteConn)
          }
        }
        val dao = db.dao
        csrb match {
          case req: FetchRequest => {
            val body = new FetchResponse
            val serverNode = db.fetch[Node](req.getUid).get
            body.setNode(toClientNode(serverNode, dao))
            Some(body)
          }
          case qr: QueryRequest => None
          case sr: SelectRequest => {
            sr.getShortClassname match {
              case "MConnection" => {
                val connections = db.
                  nodes.
                  collect { case c: Connection => c }.
                  map(c => toClientNode(c, dao))
                val resp = new SelectResponse
                resp.setNodes(connections.toList)
                Some(resp)
              }
              case _ => sys.error("don't know how to handle " + sr.getShortClassname)
            }
          }
        }
      }
      case cnr: CreateNodesRequest => {
        val nodes = cnr.
          getNodes.
          asScala.
          map(toServerNode)
        nodes.
          foreach(n => localAgentDb.insert(n))
        
        val response = new CreateNodesResponse
        response.setNodes(
            cnr.
              getNodes.
              asScala.
              filterNot(_.isInstanceOf[MBlob]).
              asJava
        )
        SocketManager.broadcast(socket.agentUid, response)
        None
      }
      case _ => sys.error("don't know how to handle type " + request.getClass)
    }
    responseBody.foreach(rb=>socket.send(rb, request))
  }
  
  def toClientNode(sn: Node, dao: AgentDAO): MNode = {
    sn match {
      case sa: Agent => { // hack to make agent look like a set of labels since that is the idiom we use to get the root labels 
        val l = new MLabel
        val children = dao.
          childLabels(sn).
          map(cl => toClientUid(cl.uid)).
          toList
        l.setName(sa.name)
        l.setUid(sa.uid)
        l.setChildren(children)
        l
      }
      case label: Label => {
        val l = new MLabel
        l.setName(label.name)
        l.setUid(label.uid)
        l.setIcon(label.icon)
        val children = dao.
          childLabels(sn).
          map(cl => toClientUid(cl.uid)).
          toList
        l.setChildren(children)
        l
      }
      case conn: Connection => 
        new MConnection(conn.uid, conn.name, conn.icon, conn.remoteAgent)
        
      case _ => sys.error("don't know how to handle type " + sn)
      
    }
  }
  
  def toServerNode(cn: MNode): Node = cn match {
    case b: MBlob => Blob(b.getRef.getAgentUid, b.getRef.getFilename, b.getDataInBase64, b.getUid)
    case l: MLabel => Label(l.getName, l.getIcon, l.getUid)
    case l: MLink => Link(l.getFrom, l.getTo)
    case _ => sys.error("don't know how to handle type " + cn)
  }
  
}
