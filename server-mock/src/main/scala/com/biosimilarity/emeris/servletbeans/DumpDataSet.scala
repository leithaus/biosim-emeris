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
import com.biosimilarity.emeris.newmodel.Model._

class DumpDataSet @Inject() (
    response: HttpServletResponse
) {
  
  @BeanProperty
  var agentUid: String = _ 
  
  def doGet = {
    
    val filename = "biosim-" + Path.getFileSystemCompatibleTimestamp + ".json"
    response.setContentType("application/json")
    response.setHeader("Content-disposition", "attachment; filename=" + filename)
    
    val db = DatabaseFactory.database(Uid(agentUid))
    response.getWriter.write(db.asJsonStr)
    response.getWriter.flush
    
  }
  
}
