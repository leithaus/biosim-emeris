package com.biosimilarity.emeris.testdata;

import com.biosimilarity.emeris.AgentDataSet.{ TextMessage, Image, Phone, Person, Node, Link, Label, Address, Uid }
import com.biosimilarity.emeris.JsonHelper.decompose
import com.biosimilarity.emeris.AgentDataSet
import net.liftweb.json.{ render, pretty }
import net.model3.newfile.File
import net.model3.util.Base64
import com.biosimilarity.emeris.KvdbFactory
import AgentDataSet.Uid

object CreateTestDataSet extends App {

    def createDataSet = new SmallTestDataSet
//  def createDataSet = new RichTestDataSet

  val dataSet = new SmallTestDataSet
//  val dataSet = new RichTestDataSet
  
  val kvdb = KvdbFactory.createDatabase(Uid("agent_one"))
  
  println(pretty(render(decompose(dataSet.asCreateNodes))))
  
  println("================================= FETCHING =======================================")
  (1 to 5) foreach { i =>
    val start = System.currentTimeMillis
    val nodes = kvdb.allNodes
    val finish = System.currentTimeMillis
    println("retrieved " + nodes.size + " in " + (finish - start) + " ms")
  }

  class SmallTestDataSet extends AgentDataSet(kvdb) {

    def newPerson(name: String) = add(Person(name))
    def newLabel(name: String) = add(Label(name))
    def newAddress(address: String) = add(Address(address))
    def newPhone(phone: String) = add(Phone(phone))
    def newMessage(msg: String) = add(TextMessage(msg))
    def addLink(from: Node, to: Node) = add(Link(from.uid, to.uid))

    init

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

  class RichTestDataSet extends AgentDataSet(kvdb) {

    def newPerson(name: String, createMessagesLink: Boolean = true) = {
      val p = add(Person(name))
      addLink(p, newLabel("Offers"))
      addLink(p, newLabel("Needs"))
      if (createMessagesLink) addLink(p, newLabel("Messages"))
      p
    }

    def newLabel(name: String) = add(Label(name))
    def newAddress(address: String) = add(Address(address))
    def newPhone(phone: String) = add(Phone(phone))
    def newMessage(msg: String) = add(TextMessage(msg))
    def addLink(from: Node, to: Node) = add(Link(from.uid, to.uid))

    init

    def init() = {
      val glen = newPerson("glen")
      val david = newPerson("david")
      val doreen = newPerson("doreen")
      val hildreth = newPerson("hildreth")
      val james = newPerson("james")
      val kate = newPerson("kate")
      val kenny = newPerson("kenny")
      val lisa = newPerson("lisa")
      val paul = newPerson("paul")
      val tracey = newPerson("tracey")
      val abundx = newPerson("AbundX", false)

      val business = newLabel("Business")
      val home = newLabel("Home")
      val photos = newLabel("Photos")

      val photoLabels = List(
        newLabel("Lyon"), newLabel("Venice"), newLabel("Genoa"), newLabel("Paris"), newLabel("Marseille"))

      addLink(glen, business)
      addLink(glen, home)
      addLink(glen, photos)

      addLink(home, newPhone("123-456-7890"))
      addLink(home, newAddress("21 Jump St.\nBolton, PA 12653"))
      addLink(home, newMessage("A blatant demo message"))

      nodes.collect { case p: Person => p }
        .filter(_ != glen)
        .foreach(addLink(glen, _))

      photoLabels.foreach(addLink(photos, _))

      val photoFiles = List("Basilique St Martin d'Ainay Lyon Paris 7600318.jpg", "Ca' d'Oro Venice 5123063.jpg", "Genoa Cathedral 6800122.jpg", "Genoa Doge's Palace 4372807.jpg", "La Brasserie du Louvre Paris 5091990.jpg", "Marseille Fort Saint-Jean.jpg", "Muro Venezia Rialto Venice 6689350.jpg", "Musee du Petit Palais Paris 1602654.jpg", "Musee Historique et Archeologique de l'Orleanais .jpeg", "Museo Luzzati Genoa.jpg", "Orleans Cathedral 5489615.jpg", "Palazzo Grassi Venice 12049810.jpg", "Paris Arc de Triomphe 11514834.jpg", "Place de la Concorde Paris 3758555.jpg", "Regione Del Veneto Venice 20551401.jpg", "Rialto Bridge Venice 2868379.jpg", "Tuileries Garden Paris 507153.jpg")

      addLink(home, add(Address("1617 Ward Rd\nDecatur, TN 37322")))
      addLink(business, add(Address("1855 Squeech Pike\nAthens, TN 37320")))

      photoFiles.foreach { filename =>
        val file = new File("../client/war/events/" + filename)
        val base64Data = Base64.byteArrayToBase64(file.readBytes)
        val photo = add(Image(base64Data, glen.uid, filename))
        photoLabels.foreach { photoLabel =>
          if (filename.contains(photoLabel.name)) {
            addLink(photoLabel, photo)
          }
        }
      }

      val davidHome = newLabel("Home")
      addLink(davidHome, add(Address("907 S Shore Terr\nNewton, NJ 07860")));
      val davidBusiness = newLabel("Business")
      addLink(davidBusiness, add(Address("122 Main St Suite 106\nNewton, NJ 07860")));
      addLink(david, davidHome)
      addLink(david, davidBusiness)

    }

  }

}