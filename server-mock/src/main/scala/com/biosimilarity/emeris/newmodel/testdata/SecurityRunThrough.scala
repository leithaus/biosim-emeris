package com.biosimilarity.emeris.newmodel.testdata

import com.biosimilarity.emeris.newmodel.Model.Uid
import com.biosimilarity.emeris.newmodel.DatabaseFactory
import com.biosimilarity.emeris.newmodel.Model.FilteredDatabase
import com.biosimilarity.emeris.newmodel.Model.Connection
import com.biosimilarity.emeris.newmodel.Model.Label
import com.biosimilarity.emeris.newmodel.Model.Agent
import com.biosimilarity.emeris.newmodel.Model.Link

object SecurityRunThrough extends App {
  
  val a = Uid("a")
  val b = Uid("b")

  val a_db = DatabaseFactory.database(a)
  val b_db = DatabaseFactory.database(b)
    
  InsertMinimalDataSet.apply(a)
  InsertMinimalDataSet.apply(b)
  
  val a_connToB = a_db.
    nodes.
    collect { case c: Connection => c }.
    find(_.remoteAgent == b).
    get
    
  println("a conn to b => " + a_connToB)
  def linkup = {
    val agent = a_db.fetch[Agent](a).get
    val aa = a_db.insert(Label("aa", None))
    a_db.insert(Link(agent.uid, aa.uid))
    val aa00 = a_db.insert(Label("aa00", None))
    a_db.insert(Link(aa.uid, aa00.uid))
    a_db.insert(Link(a_connToB.uid, aa.uid))
  }
  linkup

  val b_viewOfA =
    new FilteredDatabase(a_db, a_connToB)
  
  val me = a_db.
    nodes.
    collect{ case l: Label => l }.
    find(_.name == "me").
    get
    
  val aa00 = a_db.
    nodes.
    collect{ case l: Label => l }.
    find(_.name == "aa00").
    get

//  println( "B can see label me " + me.canBeSeenBy(a_connToB)(a_db))
  
//  val bCanSeeAa00 = aa00.canBeSeenBy(a_connToB)(a_db)
//  println( "B can see label aa00 " + bCanSeeAa00)
    
  println("==== B can see the links as well as the content")
  aa00.incomingLinks(a_db).foreach { l =>
    println( l + "   -->   " + l.canBeSeenBy(a_connToB)(a_db))
  }
  
  println("==== all nodes of a ====")
  a_db.nodes.foreach(println)
  
  println("==== nodes in a that b can see ====")
  b_viewOfA.nodes.foreach(println)

}