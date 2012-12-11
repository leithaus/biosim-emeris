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
  override val port : Option[WebSocket],
  override val inputArea : RichTextArea,
  override val displayArea : HTMLPanel,
  override val roomSelect : ListBox,
  override val changeHandler : Option[ChangeHandler],
  override val author : Option[String],
  override val room : Option[String]    
) extends ChatStateDescription {
  def withRoom( newRoom : String ) : ChatState = {
    ChatState(
      port,
      inputArea,
      displayArea,
      roomSelect,
      changeHandler,
      author,
      Some( newRoom )
    )
  }
  def withAuthor( newAuthor : String ) : ChatState = {
    ChatState(
      port,
      inputArea,
      displayArea,
      roomSelect,
      changeHandler,
      Some( newAuthor ),
      room
    )
  }
  def withAuthorRoom( newAuthor : String, newRoom : String ) : ChatState = {
    ChatState(
      port,
      inputArea,
      displayArea,
      roomSelect,
      changeHandler,
      Some( newAuthor ),
      Some( newRoom )
    )
  }
  def withCHR(
    newChangeHandler : ChangeHandler,
    newRoom : String
  ) : ChatState = {
    ChatState(
      port,
      inputArea,
      displayArea,
      roomSelect,
      Some( newChangeHandler ),
      author,
      Some( newRoom )
    )
  }
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


