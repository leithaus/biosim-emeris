package com.biosimilarity.emeris

import AgentDataSet.Uid

object DataSetManager extends Factory[AgentDataSet, Uid] {
    def create(agentUid: Uid) = {
      val kvdb = KvdbFactory.database(agentUid)
      new AgentDataSet(kvdb)
    }
}
