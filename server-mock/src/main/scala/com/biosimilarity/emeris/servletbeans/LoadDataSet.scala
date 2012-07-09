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
import com.biosimilarity.emeris.newmodel.Model._


object LoadDataSet {
  val multipartDriver = new CommonsFileUploadDriver
  
  multipartDriver.setTemp(new Directory("."))
  multipartDriver.setPrefix("loadDataSet-")
  multipartDriver.setSuffix(".json")
  
}


class LoadDataSet @Inject() (
    request0: HttpServletRequest
) { bean =>

  val request = LoadDataSet.multipartDriver.processRequest(request0).asInstanceOf[HttpServletRequest]
  
  @BeanProperty
  var agentUid: String = _
  
  def doPost = {
    
    val attributes: List[Object] = request.getAttributeNames.asScala.map(attr=>request.getAttribute(attr.asInstanceOf[String])).toList
    val uploadedFile = attributes collect { case uf: UploadedFile  => uf } head
    
    val jsonMessage = uploadedFile.getTempFile.readText
    val tempSocket = new Socket {
        val agentUid = Uid(bean.agentUid)
    	val uuid = java.util.UUID.randomUUID 
    	val userName = "bob"
    	def rawSend(data: String) = {}
    }
    
  }
  
}
