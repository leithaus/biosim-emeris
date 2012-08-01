package com.biosimilarity.emeris.newmodel


import com.biosimilarity.emeris.newmodel.Model._
import m3.predef._
import m3.FileSystem.Directory
import com.biosimilarity.emeris.predef._

object DatabaseFactory extends FileSystemDatabaseFactory

trait DatabaseFactory {
  
  def createDatabase(uid: Uid): AgentDatabase
  def database(uid: Uid): AgentDatabase
  
  def databases: Iterable[AgentDatabase]
  
  def removeConnections(uid: Uid) = {
    databases.foreach { implicit db =>
      db.
        nodes.
        collect { case c: Connection => c }.
        filter(_.remoteAgent == uid).
        foreach { conn =>
          conn.incomingLinks.foreach(l=>db.delete(l.uid))
          conn.outgoingLinks.foreach(l=>db.delete(l.uid))
          db.delete(conn.uid)
        }
    }
  }
  
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
    val rootDirName = getConfigProperty("DATABASE_ROOT", "database")
    val d = Directory("database")
    logger.debug("database root = " + d.canonicalPath)
    d.makeDirectories
    d
  }
  
  def createDatabase(uid: Uid) = {
    logger.debug("createDatabase({})", uid)
    (databasesRootDirectory \\ uid.value)
      .makeDirectories()
    val dbs = databases
    val newDb = database(uid)
    dbs.foreach { db0 =>
      val conns = db0.nodes.collect{ case c: Connection => c }
      dbs.filter(_.uid != db0.uid).foreach { db1 =>
        if ( !conns.exists(_.remoteAgent == db1.uid ) ) {
          val conn = Connection(db1.uid.value, None, db1.uid)
          db0.insert(conn)
        }
      }
    }
    newDb
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