package com.biosimilarity.emeris

import scala.collection.JavaConverters._
import com.biosimilarity.emeris.newmodel.Model._
import com.biosimilarity.emeris.newmodel.Model.Node
import com.biosimilarity.emeris.newmodel.Model.Uid
import com.biosimilarity.emeris.newmodel.DatabaseFactory
import biosim.client.messages.model.{BlobRef => ClientBlobRef}
import biosim.client.messages.model.MBlob
import biosim.client.messages.model.MConnection
import biosim.client.messages.model.MLabel
import biosim.client.messages.model.MLink
import biosim.client.messages.model.MNode
import biosim.client.messages.model.{Uid => ClientUid}
import biosim.client.messages.protocol.ConnectionScopedRequestBody
import biosim.client.messages.protocol.CreateNodesRequest
import biosim.client.messages.protocol.CreateNodesResponse
import biosim.client.messages.protocol.FetchRequest
import biosim.client.messages.protocol.FetchResponse
import biosim.client.messages.protocol.QueryRequest
import biosim.client.messages.protocol.Request
import biosim.client.messages.protocol.ResponseBody
import biosim.client.messages.protocol.SelectRequest
import biosim.client.messages.protocol.SelectResponse
import m3.predef._
import net.liftweb.json.JsonDSL._
import predef._
import biosim.client.messages.model.MAgent
import biosim.client.messages.protocol.QueryResponse
import biosim.client.messages.model.FilterAcceptCriteria
import biosim.client.messages.model.MBlob
import biosim.client.messages.model.MBlob
import biosim.client.messages.model.MImage

object SwitchBoard extends Logging {

  implicit def toServerUid(clientUid: ClientUid) = Uid(clientUid.getValue)
  implicit def toClientUid(serverUid: Uid) = new ClientUid(serverUid.value)
  
  implicit def toServerBlobRef(cbr: ClientBlobRef) = cbr match {
    case null => None
    case _ => Some(BlobRef(cbr.getAgentUid, cbr.getBlobUid, cbr.getFilename))
  }
  implicit def toServerBlobRef2(cbr: ClientBlobRef) = 
    BlobRef(cbr.getAgentUid, cbr.getBlobUid, cbr.getFilename)
    
  implicit def toClientBlobRef(bro: Option[BlobRef]) = bro match {
    case None => null
    case Some(br) =>new ClientBlobRef(br.agentUid, br.blobUid, br.filename)
  }
  implicit def toClientBlobRef(br: BlobRef) = 
    new ClientBlobRef(br.agentUid, br.blobUid, br.filename)

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
        implicit val db = csrb.getConnectionUid match {
          case null => localAgentDb
          case uid => {
            val localConn = localAgentDb.fetch[Connection](uid).get
            val remoteAgentDb = DatabaseFactory.database(localConn.remoteAgent)
            val remoteConn = remoteAgentDb.nodes.collect { case c: Connection => c }.find(_.remoteAgent == socket.agentUid).get
            new FilteredDatabase(remoteAgentDb, remoteConn)
          }
        }
        val dao = db.dao
        csrb match {
          case req: FetchRequest => {
            val body = new FetchResponse
            val nodes = req.
              getUids.
              asScala.
              flatMap(uid=>db.fetch[Node](uid)).
              distinct.
              map(n=>toClientNode(n, dao))
            Some(new FetchResponse(nodes))
          }
          case qr: QueryRequest => {
            val nodes = qr.getNodes.asScala.flatMap(uid=>db.fetch[Node](uid))
            val labels = nodes.collect { case l: Label => l}
            val conns = nodes.collect { case c: Connection => c}
            val responseCriteria = dao.
              query(labels, conns).
              map { sfac =>
                val mfac = new FilterAcceptCriteria()
                mfac.setConnections(sfac.connections.map(toClientUid).toList.asJava)
                mfac.setLabels(sfac.labels.map(toClientUid).toList.asJava)
                mfac.setPaths(sfac.paths.toList.asJava)
                mfac.setNode(sfac.node)
                mfac
              }
            Some(new QueryResponse(qr.getQueryUid, responseCriteria.toList.asJava))
          }
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
        
        val response = new CreateNodesResponse()
        response.setAgentUid(localAgentDb.uid)
        response.setConnectionUid(null)
        response.setNodes(
            nodes.
              filterNot(_.isInstanceOf[Blob]).
              map(sn=>toClientNode(sn, localAgentDb.dao)(localAgentDb)).
              asJava
        )
        SocketManager.broadcast(socket.agentUid, response)
        None
      }
      case rb => sys.error("don't know how to handle type " + rb)
    }
    responseBody.foreach(rb=>socket.send(rb, request))
  }
  
  def toClientNode(sn: Node, dao: AgentDAO)(implicit db: AgentDatabase): MNode = {
    val cn = sn match {
      
      case blob: Blob =>
        new MBlob(
          blob.uid
          , blob.asBlobRef
        )
      
      case agent: Agent => 
        new MAgent
      
      case image: Image => {
        val i = new MImage
        i.setUid(image.uid)
        i.setBlobRef(image.blobRef)
        i
      }
      
      case link: Link => 
        new MLink(link.from, link.to)
      
      case label: Label => 
        new MLabel(label.name, label.icon)
      
      case conn: Connection => 
        new MConnection(conn.uid, conn.name, conn.icon, conn.remoteAgent)
        
      case _ => sys.error("don't know how to handle type " + sn)
      
    }
    cn.setUid(sn.uid)
    cn.setLinkHints(sn.linkUids.map(toClientUid).toList)
    cn
  }
  
  def toServerNode(cn: MNode): Node = cn match {
    case i: MImage => Image(i.getBlobRef, i.getUid)
    case b: MBlob => Blob(b.getRef.getAgentUid, b.getRef.getFilename, b.getDataInBase64, b.getUid)
    case l: MLabel => Label(l.getName, l.getIcon, l.getUid)
    case l: MLink => Link(l.getFrom, l.getTo)
    case _ => sys.error("don't know how to handle type " + cn)
  }
  
}
