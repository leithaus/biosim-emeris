package com.biosimilarity.emeris.newmodel

import scala.collection.immutable.Stack
import Model._

case class FilterAcceptCriteria(
    node: Uid
	, paths: Iterable[String]
	, labels: Iterable[Uid]
	, connections: Iterable[Uid]
)


case class Filter(labels: Iterable[Label], connections: Iterable[Connection], implicit val agentDatabase: AgentDatabase) {
  
  lazy val labelsAndChildren = {
    def addLabelsAndChildren(l: Label, stack0: Stack[Label], pathsMap0: Map[Label,List[String]]): Map[Label,List[String]] = {
      if ( !stack0.contains(l) ) {
    	val stack = stack0.push(l)
    	val paths = stack.reverse.map(_.name).mkString(":") :: pathsMap0.getOrElse(l, List()) 
    	val pathsMap = pathsMap0 + (l -> paths)
        l.childLabels.foldLeft(pathsMap)( (acc,ch) => addLabelsAndChildren(ch, stack, acc))
      } else {
        pathsMap0
      }
    }
    labels.foldLeft(Map[Label,List[String]]())((acc,l) => addLabelsAndChildren(l, Stack(), acc))
  }
  
  def accept(node: Node): List[FilterAcceptCriteria] = {
    
    // labels are OR'ed and no label filters means show anything
    val labelPass = labels.isEmpty || node.parentLabels.exists(labelsAndChildren.contains)
    
    // connections are AND'ed
    val connectionPass = connections.isEmpty || connections.forall(node.canBeSeenBy)
      
    if ( labelPass && connectionPass ) {
      val labels = node.parentLabels.filter(labelsAndChildren.contains)
      List(
        FilterAcceptCriteria(
          node.uid
          , labels.flatMap(labelsAndChildren.apply) 
          , labels.map(_.uid)
          , connections.map(_.uid)
        )
      )
    } else {
      Nil
    } 
    
  }

}