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
//import com.google.gson.JsonSerializer
import com.biosimilarity.emeris.JsonHelper

class DumpMultiAgentDataSet @Inject() (
    response: HttpServletResponse
) {
  
  def doGet = {
    
    val filename = "biosim-" + Path.getFileSystemCompatibleTimestamp + ".json"
    response.setContentType("application/json")
    response.setHeader("Content-disposition", "attachment; filename=" + filename)

    val out = response.getWriter
    
    val createNodes = KvdbFactory.databases.map { db =>
      val dataSet = new AgentDataSet(db)
      dataSet.asCreateNodes.asJValue
    }

    val json = pretty(render(createNodes))

    response.getWriter.write(json)
    response.getWriter.flush
    
  }
  
}
