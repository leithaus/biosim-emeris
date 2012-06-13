package com.biosimilarity.emeris.servletbeans


import javax.servlet.http.HttpServletResponse
import net.model3.newfile.File
import net.model3.newfile.Path
import net.liftweb.json._
import net.liftweb.json.JsonDSL._
import com.google.inject.Inject
import javax.servlet.http.HttpServletRequest
import net.model3.servlet.upload.DefaultFileUploadDriver
import scala.collection.JavaConverters._
import net.model3.servlet.upload.UploadedFile
import com.biosimilarity.emeris.SocketProtocol.ClearDataSet
import com.biosimilarity.emeris.SocketProtocol.CreateNodes
import net.model3.servlet.upload.CommonsFileUploadDriver
import net.model3.newfile.Directory
import com.biosimilarity.emeris.SocketManager
import com.biosimilarity.emeris.Socket
import com.biosimilarity.emeris.AgentDataSet.Uid
import scala.reflect.BeanProperty
import com.biosimilarity.emeris.KvdbFactory
import com.biosimilarity.emeris.JsonHelper

object LoadMultiAgentDataSet {
  val multipartDriver = new CommonsFileUploadDriver
  
  multipartDriver.setTemp(new Directory("."))
  multipartDriver.setPrefix("loadMultiAgentDataSet-")
  multipartDriver.setSuffix(".json")
  
}


class LoadMultiAgentDataSet @Inject() (
    request0: HttpServletRequest
) { bean =>

  val request = LoadDataSet.multipartDriver.processRequest(request0).asInstanceOf[HttpServletRequest]
  
  def doPost = {
    
    val attributes = request.getAttributeNames.asScala.map(attr=>request.getAttribute(attr)) toList
    val uploadedFile = attributes collect { case uf: UploadedFile  => uf } head
    
    val jsonMessage = uploadedFile.getTempFile.readText
    
    val jsonAST = parse(jsonMessage)
    
    val createNodes = jsonAST match {
      case JArray(l) => {
        l.map { cnjv => 
	      import JsonHelper._
	      cnjv.extract[CreateNodes]
        }
      }
      case jv => sys.error("don't know how to handle " + jv)
    }
    
    // delete all databases
    KvdbFactory.databases.foreach(_.dropDatabase())

    createNodes.foreach { cn =>
      val db = KvdbFactory.createDatabase(cn.agentUid)
	  cn.nodes.foreach(db.store)
    }
  }
  
}
