package com.biosimilarity.emeris.servletbeans


import javax.servlet.http.HttpServletResponse
import net.model3.newfile.File
import net.model3.newfile.Path
import net.liftweb.json._
import net.liftweb.json.JsonDSL._
import com.google.inject.Inject
import com.biosimilarity.emeris.SocketManager
import scala.reflect.BeanProperty
import com.biosimilarity.emeris.KvdbFactory
import com.biosimilarity.emeris.AgentDataSet
import AgentDataSet.Uid
import com.biosimilarity.emeris.DataSetManager

class DumpDataSet @Inject() (
    response: HttpServletResponse
) {
  
  @BeanProperty
  var agentUid: String = _ 
  
  def doGet = {
    
    val filename = "biosim-" + Path.getFileSystemCompatibleTimestamp + ".json"
    response.setContentType("application/json")
    response.setHeader("Content-disposition", "attachment; filename=" + filename)
    
    val dataSet = DataSetManager(Uid(agentUid))
    val createNodes = dataSet.asCreateNodes
    val json = pretty(render(createNodes.createMessage.asJValue))
    
    response.getWriter.write(json)
    response.getWriter.flush
    
  }
  
}
