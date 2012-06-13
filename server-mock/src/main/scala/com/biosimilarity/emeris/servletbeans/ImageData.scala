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
import com.biosimilarity.emeris.AgentDataSet.Image
import net.model3.util.Base64
import net.model3.servlet.HttpStatusCodes
import javax.servlet.http.HttpServletRequest
import com.biosimilarity.emeris.AgentDataSet
import m3.predef._
import com.biosimilarity.emeris.AgentDataSet.Node
import com.biosimilarity.emeris.AgentDataSet.NodeWrapper
import com.biosimilarity.emeris.KvdbFactory

object ImageData {
  
  val fileExtensionToContentType = Map(
    "jpg" -> "image/jpeg"    
    , "jpeg" -> "image/jpeg"    
    , "png" -> "image/png"    
  )
  
}

class ImageData @Inject() (
    request: HttpServletRequest
    , response: HttpServletResponse
) extends Logging {
  
  def doGet = {
        
    val rawPath = request.getServletPath
    rawPath.split("/").reverse.take(2).toList match {
      case List(imageName, agentUid) => respondWithImageData(Uid(agentUid), imageName)
      case _ => sys.error("unable to determine db and image uid from " + rawPath)
    }
  }
  
  def respondWithImageData(agentUid: Uid, imageName: String) = {
  
    val imageUid = Uid(imageName.lastIndexOf(".") match {
      case -1 => imageName
      case to => imageName.substring(0, to)
    })

    logger.debug("getting image data for {}", imageUid)

    val nodeOpt: Option[Node] = KvdbFactory.database(agentUid).fetchOne(imageUid)
    
    val kvdb = nodeOpt match {
	  case Some(image: Image) => {
	    val bytes = Base64.base64ToByteArray(image.dataInBase64)
	    response.getOutputStream.write(bytes)
	  }
	  case Some(n) => {
	    val json = NodeWrapper(n).asJsonStr
	    logger.debug("not an image {}", json)
	    sys.error(imageName + " is not an image \n" + json)
	  }
	  case None => sys.error(imageName + " not found in " + agentUid)
	}
    
  }
  
}
