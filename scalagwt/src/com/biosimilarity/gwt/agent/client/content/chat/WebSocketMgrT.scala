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

trait WebSocketMgrT {
  def onClose( port : WebSocket ) : Unit
  def onMessage( port : WebSocket, rawMsg : String ) : Unit
  def onOpen( port : WebSocket ) : Unit
}

