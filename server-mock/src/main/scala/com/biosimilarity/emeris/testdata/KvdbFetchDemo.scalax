package com.biosimilarity.emeris.testdata


import com.biosimilarity.emeris.KvdbFactory
import com.biosimilarity.emeris.AgentDataSet.Uid

object KvdbFetchDemo extends App {

  val kvdb = KvdbFactory.database(Uid("agent_one"))
  
  (1 to 1) foreach { i =>
    val start = System.currentTimeMillis
    val nodes = kvdb.allNodes
    nodes.foreach(n=>println(n.asJsonStr))
    val finish = System.currentTimeMillis

    println("retrieved " + nodes.size + " in " + (finish - start) + " ms")
  }

}
