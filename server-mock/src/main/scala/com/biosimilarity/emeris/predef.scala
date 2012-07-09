package com.biosimilarity.emeris

import m3.fj.data.FList

object predef {

  implicit def listToFList[T](l: List[T]): FList[T] =
    l.foldLeft(FList.nil[T]) { 
      case (fl,i) => fl.cons(i) 
    }
  
}