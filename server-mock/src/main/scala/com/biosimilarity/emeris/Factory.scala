package com.biosimilarity.emeris

import m3.LockFreeMap

trait Factory[T,U] {
  
  val widgetsByKey = LockFreeMap[U,T]()
  
  def create(u: U): T

  def apply(u: U) = {
    widgetsByKey.getOrElseUpdate(u, create(u))
  }
  
}
