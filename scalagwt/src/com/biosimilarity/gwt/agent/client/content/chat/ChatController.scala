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
  lazy val handlerRegistrationMap : HashMap[ChatState,HandlerRegistration] =
    new HashMap[ChatState,HandlerRegistration]()

  def initialChatState(
    room : String,
    area : RichTextArea,
    logPanel : HTMLPanel,
    roomSelect : ListBox
  ) : ChatState = {
    val port : WebSocket =
      portMgr.openPort(	getUrlRoom( Some( room ) ) )

    val chatState : ChatState =
      ChatState(
	Some( port ), area, logPanel,
	roomSelect, None, None, Some( room )
      )

    portMgr.registerState( port, chatState )   

    val handlerRegistration : HandlerRegistration =
      chatState.roomSelect.addChangeHandler(
	new ChangeHandler() {      
	  override def onChange( event : ChangeEvent ) : Unit = {
	    val( _, newPort, newChatState ) =
	      getRoomPortStateFromSelection( chatState, this )
	    portMgr.registerState( newPort, newChatState )
	    changeRoom( newChatState )
	  }
	})

    handlerRegistrationMap.put( chatState, handlerRegistration )

    chatState
  }

  def getRoomPortStateFromSelection(
    chatState : ChatState,
    changeHandler : ChangeHandler
  ) : ( String, WebSocket, ChatState ) = {
    val room = 
      chatState.roomSelect.getValue(
	chatState.roomSelect.getSelectedIndex()
      )
    val port = portMgr.openPort( getUrl( room ) )
    val state = chatState.withCHPR( changeHandler, port, room )

    ( room, port, state )
  }
  
  def chatSendButton( oChatState : Option[ChatState] ) : Button = {
    new Button(
      constants.cwChatInputButtonSend,
      oChatState match {
	case Some( chatState ) => {	 
          ( clickEvent : ClickEvent ) => {	  	  	  
	    sendChat( chatState, clickEvent )
	    //Window.alert( "Send button clicked" )
	  }
	}
	case None => {
	  ( clickEvent : ClickEvent ) => {	  	  	  
	    //sendChat( chatState, clickEvent )
	    Window.alert( "Send button clicked" )
	  }	    
	}
      }
    )
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
    chatState : ChatState
  ) : Unit = {
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
	  Event(
	    port._url,
	    auth,
	    constants.cwChatJoinedRoom
	  )

	port.send( chatEvent.toString )
      }      
    }

    val handlerRegistration = handlerRegistrationMap.get( chatState )
    if ( handlerRegistration != null ) {
      handlerRegistration.removeHandler()
    }
    
    val newHandlerRegistration : HandlerRegistration =
      chatState.roomSelect.addChangeHandler(
	new ChangeHandler() {      
	  override def onChange( event : ChangeEvent ) : Unit = {
	    val( _, newPort, newChatState ) =
	      getRoomPortStateFromSelection( chatState, this )
	    portMgr.registerState( newPort, newChatState )
	    changeRoom( newChatState )
	  }
	})

    handlerRegistrationMap.put( chatState, newHandlerRegistration )
  }  
}
