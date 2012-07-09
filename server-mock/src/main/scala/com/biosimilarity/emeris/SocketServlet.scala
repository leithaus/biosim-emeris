package com.biosimilarity.emeris


import javax.servlet.http.HttpServletRequest
import java.io.IOException
import java.util.Set
import java.util.concurrent.CopyOnWriteArraySet
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.eclipse.jetty.websocket.WebSocketServlet
import m3.predef._
import org.eclipse.jetty.websocket.WebSocket
import com.biosimilarity.emeris.newmodel.Model.Uid

class SocketServlet extends org.eclipse.jetty.websocket.WebSocketServlet with Logging {

  def doWebSocketConnect(request: HttpServletRequest, protocol: String): WebSocket = {
    val agentUid = 
      Option(request.getParameter("agentUid"))
        .map(Uid(_))
        .getOrElse(sys.error("agentUid parm is required"))
    logger.debug("web socket for agentUid {}", agentUid)
    SocketManager.create(agentUid);
  }
  
}
