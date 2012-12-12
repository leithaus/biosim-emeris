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

case class ChatState(
  val port : Option[WebSocket],
  val inputArea : RichTextArea,
  val displayArea : HTMLPanel,
  val roomSelect : ListBox,
  val changeHandler : Option[ChangeHandler],
  val author : Option[String],
  val room : Option[String]    
) /* extends ChatStateDescription */ {  
  def withCHPR(
    newChangeHandler : ChangeHandler,
    newPort : WebSocket,
    newRoom : String
  ) : ChatState = {
    ChatState(
      Some( newPort ),
      inputArea,
      displayArea,
      roomSelect,
      Some( newChangeHandler ),
      author,
      Some( newRoom )
    )
  }
  
}


