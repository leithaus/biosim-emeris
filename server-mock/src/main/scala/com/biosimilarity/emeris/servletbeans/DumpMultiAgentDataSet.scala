package com.biosimilarity.emeris.servletbeans


import javax.servlet.http.HttpServletResponse
import net.model3.newfile.File
import net.model3.newfile.Path
import net.liftweb.json._
import net.liftweb.json.JsonDSL._
import com.google.inject.Inject
import com.biosimilarity.emeris.SocketManager
import scala.reflect.BeanProperty
import com.biosimilarity.emeris.newmodel.DatabaseFactory

class DumpMultiAgentDataSet @Inject() (
    response: HttpServletResponse
) {
  
  def doGet = {
    
    val filename = "biosim-" + Path.getFileSystemCompatibleTimestamp + ".json"
    response.setContentType("application/json")
    response.setHeader("Content-disposition", "attachment; filename=" + filename)

    val out = response.getWriter
    
    val data = DatabaseFactory.databases.map(_.asJValue)

    val json = pretty(render(data))

    response.getWriter.write(json)
    response.getWriter.flush
    
  }
  
}
