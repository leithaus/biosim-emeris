// -*- mode: Scala;-*- 
// Filename:    Port.scala 
// Authors:     lgm                                                    
// Creation:    Tue Nov 27 09:46:32 2012 
// Copyright:   Not supplied 
// Description: 
// ------------------------------------------------------------------------

package com.biosimilarity.gwt.agent.client.content.chat

import com.google.gwt.event.dom.client.ChangeHandler
import com.google.gwt.user.client.Window
import com.google.gwt.user.client.ui.Button
import com.google.gwt.user.client.ui.HTMLPanel
import com.google.gwt.user.client.ui.ListBox
import com.google.gwt.user.client.ui.RichTextArea

import m3.gwt.websocket._

import java.util.HashMap

trait WebSocketMgr extends WebSocketHandler {
  def portMap : HashMap[String,WebSocket]
  def stateMap : HashMap[WebSocket,ChatState]
  def openPort( url : String ) : WebSocket = {
    portMap.get( url ) match {
      case null => {
	val port = new WebSocket( url, this )
	portMap.put( url, port )
	port
      }
      case port : WebSocket => port      
    }
  }
  def openPort( url : String, chatState : ChatState ) : WebSocket = {
    val port = openPort( url )
    stateMap.put( port, chatState )
    port
  }

  def closePort( url : String ) : Unit = {
    portMap.remove( url )
  }

  def registerState( port : WebSocket, chatState : ChatState ) : Unit = {
    stateMap.put( port, chatState )
  }

  def unregisterState( port : WebSocket ) : Unit = {
    stateMap.remove( port )
  }

  override def onClose( port : WebSocket ) : Unit = {
    stateMap.get( port._url ) match {      
      case null => {
	Window.alert( "unmapped port " + port._url )    
      }
      case chatState : ChatState => {
	// addChatLine(
// 	  chatState,
// 	  "room closing: " + port._url,
// 	  constants.cwChatColorSystemMessage
// 	)
	stateMap.remove( port )
	portMap.remove( port._url )
      }
    }
  }
  
  override def onError( port : WebSocket, msg : String ) : Unit = {
  }

  override def onMessage( port : WebSocket, rawMsg : String ) : Unit = {
    stateMap.get( port ) match {
      case null => {
	Window.alert( rawMsg )    
      }
      case chatState : ChatState => {
	Window.alert( rawMsg )    
	// addChatLine(
	// 	    chatState,
	// 	    rawMsg,
	// 	    constants.cwChatColorMessageSelf
	// 	  )
      }      
    }
  }    
  
  override def onOpen( port : WebSocket ) : Unit = {
    stateMap.get( port ) match {
      case null => {
	Window.alert( "unmapped port " + port._url )    
      }
      case chatState : ChatState => {
	Window.alert( "room opening " + port._url )    
	// addChatLine(
	// 	    chatState,
	// 	    "room opening: " + port._url,
	// 	    constants.cwChatColorSystemMessage
	// 	  )
      }      
    }
  }
}


