package com.biosimilarity.emeris.testdata

import com.biosimilarity.emeris.newmodel.DatabaseFactory
import com.biosimilarity.emeris.newmodel.Model.{ TextMessage, Uid } 

object FilterDemo extends App {

  implicit val db = DatabaseFactory.database(Uid("a"))
  
  val n = db.fetch[TextMessage](Uid("OMT5b0kr1L3tOrMVvsqYdJSPAwoTMbRp")).get
  
  val pl = n.parentLabels
  
  pl.foreach(l=>println(l.name))
  
}