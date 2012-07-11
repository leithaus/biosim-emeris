package com.biosimilarity.emeris

import m3.fj.data.FList
import scala.collection.JavaConverters._

object predef {

  implicit def listToFList[T](l: List[T]): FList[T] =
    l.foldLeft(FList.nil[T]) { 
      case (fl,i) => fl.cons(i) 
    }
  
  implicit def flistToList[T](l: FList[T]) = new {
    def asScala = l.asScala.toList
  }
  
}