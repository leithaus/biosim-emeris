package com.biosimilarity.emeris.testdata


import com.biosimilarity.emeris.AgentDataSet.Label
import com.biosimilarity.emeris.KvdbFactory
import com.biosimilarity.emeris.AgentDataSet.Uid

object RoundTripDataTest extends App {

  val kvdb = KvdbFactory.database(Uid("agent_one"))

  try {
      println("------ putting record -------")
      kvdb.store(Label("test"))
      
      println("------ fetching record -------")
      kvdb.allNodes.foreach(n=>println(n.asJsonStr))
  } catch {
    case e: Exception => e.printStackTrace()
  }

  System.exit(0)
  
}
