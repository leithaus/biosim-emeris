package com.biosimilarity.emeris.newmodel

import org.scalatest.junit.JUnitSuite
import scala.collection.mutable.ListBuffer
import org.junit.Assert._
import org.junit.Test
import org.junit.Before

import Model._
import Impl._
import database.labels
import database.connections

class ImplTest extends JUnitSuite {

  @Before def initialize() {
  }

  @Test def links = { // Uses JUnit-style assertions
    
	// oneA should have no outgoing links
    assert(labels.oneA.outgoingLinks.isEmpty)

    // one should have two outgoing links
    assertEquals(labels.one.outgoingLinks.size, 2)

    // oneA should have one incoming link from one
    assertEquals(labels.oneA.incomingLinks.size, 1)
    assertEquals(labels.oneA.incomingLinks.iterator.next.from.node, labels.one)
    
  }
  
  @Test def canBeSeenBy = {
  
    // oneA can be seen by bob
    assert( labels.oneA.canBeSeenBy(connections.bob) )
    
    // oneB can be seen by bob
    assert( labels.oneB.canBeSeenBy(connections.bob) )
    
    // one can be seen by bob
    assert( labels.one.canBeSeenBy(connections.bob) )
    
    // two cannot be seen by bob
    assert( labels.two.canBeSeenBy(connections.bob) === false )

  }
  
  @Test def query = {
    
    doQuery(
      Set(
        (labels.one, Set("one"))    
        , (labels.oneA, Set("one:A"))    
        , (labels.oneB, Set("one:B"))    
      )
      , List(labels.one)
      , List(connections.bob)
    )
    
    doQuery(
      Set()
      , List(labels.one)
      , List(connections.bob, connections.bill)
    )
    
    doQuery(
      Set(
        (labels.oneA, Set("A"))    
      )
      , List(labels.oneA)
      , List(connections.bob)
    )
    
  }
  
  def doQuery(expected: Set[(Label,Set[String])], labels: Iterable[Label], cnxns: Iterable[Connection]) = {
    val results = dao.query(labels, cnxns)
    val actual = results.map( r => (r.node, r.paths.toSet)).toSet
    assertEquals(actual, expected)
  }

}
