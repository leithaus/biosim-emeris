// -*- mode: Scala;-*- 
// Filename:    ChatController.scala 
// Authors:     lgm                                                    
// Creation:    Tue Dec 11 11:19:49 2012 
// Copyright:   Not supplied 
// Description: 
// ------------------------------------------------------------------------

package com.biosimilarity.gwt.agent.client.content.chat

import com.google.gwt.core.client.GWT
import com.google.gwt.core.client.RunAsyncCallback
import com.google.gwt.core.client.Scheduler
import com.google.gwt.event.dom.client.ChangeEvent
import com.google.gwt.event.dom.client.ChangeHandler
import com.google.gwt.event.dom.client.ClickEvent
import com.google.gwt.event.dom.client.ClickHandler
import com.google.gwt.event.shared.HandlerRegistration
import com.google.gwt.i18n.client.Constants
import com.biosimilarity.gwt.agent.client.ContentWidget
import com.biosimilarity.gwt.agent.client.Handlers._
import com.biosimilarity.gwt.agent.client.ShowcaseAnnotations.ShowcaseData
import com.biosimilarity.gwt.agent.client.ShowcaseAnnotations.ShowcaseSource
import com.biosimilarity.gwt.agent.client.ShowcaseAnnotations.ShowcaseStyle
import com.google.gwt.logging.client.HasWidgetsLogHandler
import com.google.gwt.user.client.Window
import com.google.gwt.user.client.rpc.AsyncCallback
import com.google.gwt.user.client.ui.Button
import com.google.gwt.user.client.ui.Grid
import com.google.gwt.user.client.ui.HTML
import com.google.gwt.user.client.ui.HTMLPanel
import com.google.gwt.user.client.ui.HorizontalPanel
import com.google.gwt.user.client.ui.ListBox
import com.google.gwt.user.client.ui.RichTextArea
import com.google.gwt.user.client.ui.TextArea
import com.google.gwt.user.client.ui.TextBox
import com.google.gwt.user.client.ui.VerticalPanel
import com.google.gwt.user.client.ui.Widget

import com.google.gwt.user.client.rpc.StatusCodeException

import com.biosimilarity.gwt.agent.client.content.text.RichTextToolbar

import m3.gwt.websocket._

import java.io.Serializable

import java.util.HashMap
import java.util.Map

class ChatController(
  val portMgr : AgentWebSocketMgr,
  val constants : CwChatInputText.CwConstants
) {
  def initialChatState(
    room : String,
    area : RichTextArea,
    logPanel : HTMLPanel,
    roomSelect : ListBox
  ) : ChatState = {
    lazy val port : WebSocket =
      portMgr.openPort(
	getUrlRoom( Some( room ) ),
	ChatState(
	  Some( port ), area, logPanel, None,
	  roomSelect, None, None, Some( room )
	)
      )
    
    val chatState : ChatState = portMgr.stateMap.get( port )

    lazy val handlerRegistration : HandlerRegistration =
      chatState.roomSelect.addChangeHandler(
	new ChangeHandler() {      
	  override def onChange( event : ChangeEvent ) : Unit = {
	    val room = 
	      chatState.roomSelect.getValue(
		chatState.roomSelect.getSelectedIndex()
	      )
	    changeRoom(
	      handlerRegistration,
	      chatState.withCHPR(
		this,
		portMgr.openPort( getUrl( room ), chatState ),
		room
	      )
	    )
	  }
	})

    chatState
  }
  
  def chatSendButton( chatState : ChatState ) : Button = {
    lazy val sendButton : Button =
      new Button(
        constants.cwChatInputButtonSend,
        ( clickEvent : ClickEvent ) => {	  	  	  
	  sendChat( chatState.withSendBtn( sendButton ), clickEvent )
	  //Window.alert( "Send button clicked" )
	}
      )

    sendButton
  }
  
  def clearChat( chatState : ChatState ) : Unit = {
    chatState.displayArea.getElement().setInnerHTML("");
  }

  def addChatLine( chatState : ChatState, line : String, color : String ) : Unit = {
    val newLine : HTML = new HTML( line )
    newLine.getElement().getStyle().setColor( color )
    chatState.displayArea.add( newLine )
    newLine.getElement().scrollIntoView()
  }

  def sendChat( chatState : ChatState, click : ClickEvent ) : Unit = {    
    val line = chatState.inputArea.getText      
    //Window.alert( line )    
    
    addChatLine(
      chatState,
      line,
      constants.cwChatColorMessageSelf
    )

    chatState.inputArea.setText( "" )

    chatState.port match {      
      case None => {
	Window.alert( "attempting to send on a state with uninitialized port" )    
      }
      case Some( port : WebSocket ) => {
	val auth = 
	  chatState.author match {
	    case Some( author ) => author
	    case None => "anonymous"
	  }	
	val chatEvent =
	  new Event( port._url, auth, line )
	port.send( chatEvent.toString )
      }      
    }
  }

  def getUrl( chatState : ChatState ) : String = {
    (
      GWT.getModuleBaseURL()
      + "gwtComet/"
      + getUrlRoom( chatState.room ) 
    )
  }

  def getUrl( room : String ) : String = {
    (
      GWT.getModuleBaseURL()
      + "gwtComet/"
      + getUrlRoom( Some( room ) )      
    )
  }

  def getUrlRoom( room : Option[String] ) : String = {
    room match {
      case Some( room ) => room
      case None => constants.cwChatRoomOneName
    }
  }

  def changeRoom(
    handlerRegistration : HandlerRegistration,
    chatState : ChatState
  ) : Unit = {
    chatState.port match {
      case None => {
	Window.alert( "attempting to send on a state with uninitialized port" )    
      }
      case Some( port : WebSocket ) => {
	portMgr.registerState( port, chatState )

	val auth = 
	  chatState.author match {
	    case Some( author ) => author
	    case None => "anonymous"
	  }
	val chatEvent = 
	  Event(
	    port._url,
	    auth,
	    constants.cwChatJoinedRoom
	  )

	port.send( chatEvent.toString )
      }      
    }

    handlerRegistration.removeHandler()

    lazy val handlerRegistration : HandlerRegistration = 
      chatState.roomSelect.addChangeHandler(
	new ChangeHandler() {      
	  override def onChange( event : ChangeEvent ) : Unit = {	    
	    val room = 
	      chatState.roomSelect.getValue(
		chatState.roomSelect.getSelectedIndex()
	      )
	    changeRoom(
	      handlerRegistration,
	      chatState.withCHPR(
		this,
		portMgr.openPort( getUrl( room ), chatState ),
		room
	      )
	    )		
	  }
	})
  }  
}
