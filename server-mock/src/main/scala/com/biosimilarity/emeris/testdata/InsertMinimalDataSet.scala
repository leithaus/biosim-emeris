package com.biosimilarity.emeris.testdata;

import com.biosimilarity.emeris.AgentDataSet
import AgentDataSet.{ Alias, TextMessage, Image, Phone, Person, Node, Link, Label, Address, Uid }
import com.biosimilarity.emeris.JsonHelper.decompose
import com.biosimilarity.emeris.KvdbFactory
import net.liftweb.json.{ render, pretty }
import net.model3.newfile.File
import net.model3.util.Base64
import com.biosimilarity.emeris.JsonHelper
import com.biosimilarity.emeris.SocketProtocol
import com.biosimilarity.emeris.KvdbInterop
import com.biosimilarity.emeris.KvdbInterop

object InsertMinimalDataSet extends App {


  lazy val agentUid = Uid("agent_one")

  val dataSet = apply(agentUid)
  
  lazy val base64DefaultIcon = """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"""
  
  def apply(agentUid: Uid) = {
    
    val kvdb = (
      KvdbFactory
      	.databases.find(_.agentUid == agentUid)
      	.getOrElse(KvdbFactory.createDatabase(agentUid))
    )
    
	val dataSet = new AgentDataSet(kvdb)

	dataSet.clear

	def add[T<:Node](n: T) = dataSet.add(n)

    lazy val agent = add(Person("me", uid=agentUid))

    def newPerson(name: String) = dataSet.add(Person(name))
    def newLabel(name: String) = dataSet.add(Label(name))
    def newAddress(address: String) = add(Address(address))
    def newPhone(phone: String) = add(Phone(phone))
    def newMessage(msg: String) = add(TextMessage(msg))
    
    
    def newImageFromFile(filename: String) = {
      val fileBytes = new File(filename).readBytes
      val base64 = Base64.byteArrayToBase64(fileBytes)
      add(Image(base64, agent.uid, filename))
    }

    def newImage(filename: String, base64: String) = {
      add(Image(base64, agent.uid, filename))
    }

    def addLink(from: Node, to: Node) = add(Link(from.uid, to.uid))

//    val icon = newImageFromFile("../client/war/friends/cool-icon.png")
    val icon = newImage("defaultAliasIcon.png", base64DefaultIcon)
    
    val alias = add(Alias("rootAlias", agent.uid, icon.url))
    addLink(agent, alias)

    dataSet 
    
  }


  println(pretty(render(decompose(dataSet.asCreateNodes))))
  
}
