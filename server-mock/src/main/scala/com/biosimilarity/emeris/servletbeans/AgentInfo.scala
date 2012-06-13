package com.biosimilarity.emeris.servletbeans


import javax.servlet.http.HttpServletResponse
import net.model3.newfile.File
import net.model3.newfile.Path
import net.liftweb.json._
import net.liftweb.json.JsonDSL._
import com.google.inject.Inject
import com.biosimilarity.emeris.SocketManager
import scala.reflect.BeanProperty
import com.biosimilarity.emeris.AgentDataSet.Uid
import com.biosimilarity.emeris.KvdbFactory

class AgentInfo @Inject() (
    response: HttpServletResponse
) {
  
  def doGet: Unit = {

    response.setContentType("application/json")
    
    val agents = KvdbFactory.databases.map { db => db.agentUid.value }
    
    val json = pretty(render(agents))
        
    response.getWriter.print(json)
    response.getWriter.flush

  }
  
}
