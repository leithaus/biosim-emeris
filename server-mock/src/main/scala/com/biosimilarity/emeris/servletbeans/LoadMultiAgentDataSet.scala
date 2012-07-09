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
import net.model3.servlet.upload.CommonsFileUploadDriver
import net.model3.newfile.Directory
import com.biosimilarity.emeris.SocketManager
import com.biosimilarity.emeris.Socket
import scala.reflect.BeanProperty
import com.biosimilarity.emeris.newmodel.DatabaseFactory
import com.biosimilarity.emeris.newmodel.Model._
import com.biosimilarity.emeris.newmodel.JsonHelper._

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
    
    val allData = jsonAST match {
      case JArray(l) => {
        l.map { cnjv => 
	      cnjv.extract[AgentData]
        }
      }
      case jv => sys.error("don't know how to handle " + jv)
    }
    
    // delete all databases
    DatabaseFactory.databases.foreach(_.dropDatabase())

    allData.foreach { data =>
      val db = DatabaseFactory.createDatabase(data.uid)
	  data.nodes.foreach(db.insert)
    }
  }
  
}
