package com.biosimilarity.emeris.newmodel


import com.biosimilarity.emeris.newmodel.Model._
import m3.predef._
import m3.FileSystem.Directory

object DatabaseFactory extends FileSystemDatabaseFactory

trait DatabaseFactory {
  
  def createDatabase(uid: Uid): AgentDatabase
  def database(uid: Uid): AgentDatabase
  
  def databases: Iterable[AgentDatabase]
  
}

trait LoggingDatabaseFactory extends DatabaseFactory with Logging {
  
  abstract override def createDatabase(uid: Uid) = {
    logger.debug("createDatabase({})", uid)
    super.createDatabase(uid)
  }
  
  abstract override def database(uid: Uid) = {
    logger.debug("database({})", uid)
    super.database(uid)
  }

  abstract override def databases = {
    logger.debug("databases")
    super.databases
  }

}

trait FileSystemDatabaseFactory extends DatabaseFactory with Logging {

  lazy val databasesRootDirectory = {
    val d = Directory("database")
    d.makeDirectories
    d
  }
  
  def createDatabase(uid: Uid) = {
    logger.debug("createDatabase({})", uid)
    (databasesRootDirectory \\ uid.value)
      .makeDirectories()
    database(uid)
  }
  
  def databaseGen(uid: Uid) = {
	(databasesRootDirectory \\ uid.value) match {
	  case d if d.exists => new FileSystemDatabase(d, uid)
	  case d => sys.error("database " + d + " does not exist")
	}
  }
  
  def database(agentUid: Uid) = { 
    logger.debug("database({})", agentUid)
    databaseGen(agentUid)
  }
  
  def databases = 
    databasesRootDirectory
      .subDirectories
      .map(d=>databaseGen(Uid(d.name)))
 
}