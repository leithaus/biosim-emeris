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
import net.model3.servlet.upload.CommonsFileUploadDriver
import net.model3.newfile.Directory
import com.biosimilarity.emeris.AgentDataSet.Uid
import scala.reflect.BeanProperty

object PhotoUploader {
  val multipartDriver = new CommonsFileUploadDriver
  
  multipartDriver.setTemp(new Directory("."))
  multipartDriver.setPrefix("loadDataSet-")
  multipartDriver.setSuffix(".json")
  
}


class PhotoUploader @Inject() (
    request0: HttpServletRequest
) {

  val request = PhotoUploader.multipartDriver.processRequest(request0).asInstanceOf[HttpServletRequest]

  @BeanProperty
  var agentUid: String = _ 
  
  def doPost = {
    
    val attributes = request.getAttributeNames.asScala.map(attr=>request.getAttribute(attr)) toList
    val uploadedFile = attributes collect { case uf: UploadedFile  => uf } head
    
    val uid = Uid(request.getParameter("uid"))
    
    val uploadedLocation = new File( request.getServletContext.getRealPath("/photos/" + uid.value + ".jpeg") )
    val jsonMessage = uploadedFile.getTempFile.copyFileTo(uploadedLocation)
    
  }
  
}
