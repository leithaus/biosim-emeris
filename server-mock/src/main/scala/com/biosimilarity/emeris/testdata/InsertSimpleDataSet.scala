package com.biosimilarity.emeris.testdata;

import com.biosimilarity.emeris.AgentDataSet.{ TextMessage, Image, Phone, Person, Node, Link, Label, Address }
import com.biosimilarity.emeris.JsonHelper.decompose
import com.biosimilarity.emeris.AgentDataSet
import com.biosimilarity.emeris.KvdbFactory
import net.liftweb.json.{ render, pretty }
import net.model3.newfile.File
import net.model3.util.Base64
import net.model3.lang.ObjectX
import net.model3.lang.ThreadX
import net.model3.logging.SimpleLoggingConfigurator
import AgentDataSet.Uid

object InsertSimpleDataSet extends App {

  new SimpleLoggingConfigurator().addEclipseConsoleAppender()

  val dataSet = new SmallTestDataSet
  
  dataSet.clear
  dataSet.init()
  
  val kvdb = KvdbFactory.database(Uid("agent_one"))
  
  println(pretty(render(decompose(dataSet.asCreateNodes))))
  
  class SmallTestDataSet extends AgentDataSet(kvdb) {

    def newPerson(name: String) = add(Person(name))
    def newLabel(name: String) = add(Label(name))
    def newAddress(address: String) = add(Address(address))
    def newPhone(phone: String) = add(Phone(phone))
    def newMessage(msg: String) = add(TextMessage(msg))
    def addLink(from: Node, to: Node) = add(Link(from.uid, to.uid))

    def init() = {
      val glen = newPerson("glen")
//      val tracey = newPerson("tracey")
//      val david = newPerson("david")

      val home = newLabel("Home")

      addLink(glen, home)

      addLink(home, newPhone("123-456-7890"))
      addLink(home, newAddress("21 Jump St.\nBolton, PA 12653"))
      addLink(home, newMessage("A blatant demo message"))

      nodes.collect { case p: Person => p }
        .filter(_ != glen)
        .foreach(addLink(glen, _))

      addLink(home, add(Address("1617 Ward Rd\nDecatur, TN 37322")))

    }

  }

}