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
import com.biosimilarity.emeris.testdata.InsertMinimalDataSet

class CreateAgent @Inject() (
    response: HttpServletResponse
) {
  
  @BeanProperty
  var agentUid: String = _
  
  def doGet: Unit = {

    if ( agentUid == null ) sys.error("uid is required")
    val uid = Uid(agentUid)
    KvdbFactory.createDatabase(uid)
    InsertMinimalDataSet.apply(uid)
    
  }
  
}
