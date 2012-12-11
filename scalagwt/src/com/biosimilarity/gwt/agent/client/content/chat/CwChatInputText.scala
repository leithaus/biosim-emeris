/*
 * Copyright 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
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
//import java.net.URI
//import java.util.UUID
//import java.util.logging.Level
//import java.util.logging.Logger

@ShowcaseStyle(Array(
    ".gwt-RichTextArea", ".hasRichTextToolbar", ".gwt-RichTextToolbar",
    ".cw-RichText", ".gwt-Button"))
object CwChatInputText {
  /**
   * The constants used in this Content Widget.
   */
  @ShowcaseSource
  trait CwConstants extends Constants with ContentWidget.CwConstants {
    def cwChatInputTextDescription() : String
    def cwChatInputTextName() : String

    def cwChatIOTextDescription() : String
    def cwChatIOTextName() : String
    def cwChatDisplayTextLabel() : String

    def cwChatInputButtonClickMessage() : String
    def cwChatInputButtonDescription() : String
    def cwChatInputButtonName() : String
    def cwChatInputButtonSend() : String
    
    def cwChatLeftRoom() : String    
    def cwChatJoinedRoom() : String
    def cwChatRoomConnected() : String
    def cwChatRoomDisconnected() : String
    def cwChatRoomError() : String

    def cwChatColorSystemMessage() : String
    def cwChatColorMessageSelf() : String
    def cwChatColorMessageOthers() : String

    def cwChatRoomOneTitle() : String
    def cwChatRoomTwoTitle() : String
    def cwChatRoomThreeTitle() : String
    def cwChatRoomFourTitle() : String

    def cwChatRoomOneName() : String
    def cwChatRoomTwoName() : String
    def cwChatRoomThreeName() : String
    def cwChatRoomFourName() : String    
  }
}

/**
 * Example file.
 */
@ShowcaseStyle(Array(
    ".gwt-RichTextArea", ".hasRichTextToolbar", ".gwt-RichTextToolbar",
    ".cw-RichText", ".gwt-Button"))
class CwChatInputText(@ShowcaseData val constants : CwChatInputText.CwConstants)
extends ContentWidget( constants ) {
  val enclosingChat = this

  object ChatWebPortMgr extends AgentWebSocketMgr( enclosingChat )

  override def getDescription() = constants.cwChatInputTextDescription
  override def getName() = constants.cwChatInputTextName  

  //lazy val logger : Logger = Logger.getLogger( classOf[CwChatInputText].getName );  

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  override def onInitialize(): Widget = {
    val hPanel = new HorizontalPanel()
    hPanel.setSpacing(10);    

    val vPanel = new VerticalPanel()
    vPanel.setSpacing(5)

    //val chatDisplayTextArea = new TextArea()
    //chatDisplayTextArea.setSize("100%", "14em")
    //chatDisplayTextArea.ensureDebugId("cwBasicText-textbox")

    val logPanel : HTMLPanel = new HTMLPanel("") {
      override def add( widget : Widget ) : Unit = {
        super.add( widget )
        widget.getElement().scrollIntoView()
      }
    }

    //Logger.getLogger( "" ).addHandler( new HasWidgetsLogHandler( logPanel ) )

    vPanel.add( new HTML( constants.cwChatDisplayTextLabel() ) )
    vPanel.add( logPanel )

    // Create the text area and toolbar
    val area = new RichTextArea()
    area.ensureDebugId( "cwRichText-area" )
    area.setSize( "100%", "14em" )
    val toolbar = new RichTextToolbar( area )
    toolbar.ensureDebugId( "cwRichText-toolbar" )
    toolbar.setWidth( "100%" )

    // Add the components to a panel
    val grid = new Grid( 2, 1 )
    grid.setStyleName("cw-RichText")
    grid.setWidget(0, 0, toolbar)
    grid.setWidget(1, 0, area)
    //grid

    val roomSelect : ListBox = new ListBox()
    roomSelect.addItem( constants.cwChatRoomOneTitle, constants.cwChatRoomOneName )
    roomSelect.addItem( constants.cwChatRoomTwoTitle, constants.cwChatRoomTwoName )
    roomSelect.addItem( constants.cwChatRoomThreeTitle, constants.cwChatRoomThreeName )
    roomSelect.addItem( constants.cwChatRoomFourTitle, constants.cwChatRoomFourName )
    roomSelect.setSelectedIndex( 0 )    

    val room = roomSelect.getValue( roomSelect.getSelectedIndex() )

    lazy val port : WebSocket =
      ChatWebPortMgr.openPort(
	getUrlRoom( Some( room ) ),
	ChatState(
	  Some( port ), area, logPanel, None,
	  roomSelect, None, None, Some( room )
	)
      )

    val chatState : ChatState = ChatWebPortMgr.stateMap.get( port )

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
		ChatWebPortMgr.openPort( getUrl( room ), chatState ),
		room
	      )
	    )
	  }
	})

    vPanel.add( grid )

    vPanel.add( roomSelect )

    hPanel.add( vPanel )            

    // Add a normal button
    lazy val sendButton : Button =
      new Button(
        constants.cwChatInputButtonSend,
        ( clickEvent : ClickEvent ) => {	  	  	  
	  sendChat( chatState.withSendBtn( sendButton ), clickEvent )
	}
      )            

    sendButton.ensureDebugId( "cwChatInputButton-send" )
    vPanel.add( sendButton )

    hPanel
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
	ChatWebPortMgr.registerState( port, chatState )

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
		ChatWebPortMgr.openPort( getUrl( room ), chatState ),
		room
	      )
	    )		
	  }
	})
  }

  override def asyncOnInitialize(callback: AsyncCallback[Widget]) = {
    GWT.runAsync(new RunAsyncCallback() {
      def onFailure(caught: Throwable) = callback.onFailure(caught)

      def onSuccess() = callback.onSuccess(onInitialize())
    })
  }  
}
