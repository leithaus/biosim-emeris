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
package com.biosimilarity.iacontainer.client.content.chat

import com.google.gwt.core.client.GWT
import com.google.gwt.core.client.RunAsyncCallback
import com.google.gwt.event.dom.client.ClickEvent
import com.google.gwt.event.dom.client.ClickHandler
import com.google.gwt.i18n.client.Constants
import com.biosimilarity.iacontainer.client.ContentWidget
import com.biosimilarity.iacontainer.client.Handlers._
import com.biosimilarity.iacontainer.client.ShowcaseAnnotations.ShowcaseData
import com.biosimilarity.iacontainer.client.ShowcaseAnnotations.ShowcaseSource
import com.biosimilarity.iacontainer.client.ShowcaseAnnotations.ShowcaseStyle
import com.google.gwt.logging.client.HasWidgetsLogHandler
import com.google.gwt.user.client.Window
import com.google.gwt.user.client.rpc.AsyncCallback
import com.google.gwt.user.client.ui.Button
import com.google.gwt.user.client.ui.Grid
import com.google.gwt.user.client.ui.HTML
import com.google.gwt.user.client.ui.HTMLPanel
import com.google.gwt.user.client.ui.HorizontalPanel
import com.google.gwt.user.client.ui.RichTextArea
import com.google.gwt.user.client.ui.TextArea
import com.google.gwt.user.client.ui.TextBox
import com.google.gwt.user.client.ui.VerticalPanel
import com.google.gwt.user.client.ui.Widget

import com.biosimilarity.iacontainer.client.content.text.RichTextToolbar

//import com.sksamuel.gwt.websockets._
import m3.gwt.websocket._

import java.io.Serializable

import java.util.HashMap
import java.util.Map
import java.net.URI
import java.util.UUID
import java.util.logging.Level
import java.util.logging.Logger

trait WSChannelMgr {  
  case class WSListener(
    onClose : () => Unit,
    onMessage : String => Unit,
    onOpen : () => Unit
  )

  class WebPort(
    uri : String,
    wsl : WSListener
  ) extends SimpleWebSocket(
    uri,
    null
  ){
    trait Identified {
      val _id = UUID.randomUUID
      def id() : String = _id.toString
    }
    class WebPortOutMsg(
      val body : String
    ) extends WebSocket.OutgoingMessage with Identified {
      override def ack() = false
    }
    object WebPortOutMsg {
      def apply( body : String ) : WebPortOutMsg = {
	new WebPortOutMsg( body )
      }
      def unapply( wpom : WebPortOutMsg ) : Option[( String )] = {
	Some( ( wpom.body ) )
      }
    }
    class WebPortInMsg(
      val body : String
    ) extends WebSocket.IncomingMessage with Identified {
      override def ack() = false
      override def acks() = {
	throw new Exception( "acks not yet defined" )
      }
      override def delegate[T]() : T = {
	throw new Exception( "delegate not yet defined" )
      }
    }
    object WebPortInMsg {
      def apply( body : String ) : WebPortInMsg = {
	new WebPortInMsg( body )
      }
      def unapply( wpom : WebPortInMsg ) : Option[( String )] = {
	Some( ( wpom.body ) )
      }
    }
    class WebPortHandler(
    ) extends WebSocket.Handler {
      override def stateChange( state : WebSocket.State ) : Unit = {
      }
      override def incomingMessage(
	message : WebSocket.IncomingMessage
      ) : Unit = {	
      }
      override def deserialize( body : String ) : WebSocket.IncomingMessage = {
	new WebPortInMsg( body )
      }
      override def onOpen() : Unit = {	
      }
      override def onError( msg : String ) : Unit = {
      }
      override def onClose( msg : String ) : Unit = {
      }
    }    
  
    implicit def toHandler( wsl : WSListener ) : WebPortHandler = {
      new WebPortHandler() {
	override def onClose( msg : String ) : Unit = {
          wsl.onClose()
	}
	
	override def incomingMessage( msg : WebSocket.IncomingMessage ) : Unit = {
	  msg match {
	    case wpim : WebPortInMsg => {
	      wsl.onMessage( wpim.body )
	    }
	    case _ => {
	      throw new Exception( "unexpected message type: " + msg )
	    }
	  }          
	}
	
	override def onOpen() : Unit = {
          wsl.onOpen()
	}
      }
    }
  }
  
  lazy val socketsMap : Map[String,WebPort] = new HashMap

  // create sockets

  def getWS( key : String ) : Option[WebPort] = {
    socketsMap.get( key ) match {
      case null => None
      case ws => Some( ws )
    }
  }
  
  implicit def createWS( /* uri : URI */ uri : String ) : WebPort = {
    val socket : WebPort = new WebPort( uri, null )
    socketsMap.put( uri, socket )
    socket
  }
  
}

object CwChatInputText {
  /**
   * The constants used in this Content Widget.
   */
  @ShowcaseSource
  trait CwConstants extends Constants with ContentWidget.CwConstants {
    def cwChatIOTextDescription() : String

    def cwChatIOTextName() : String

    def cwChatDisplayTextLabel() : String

    def cwChatInputButtonClickMessage() : String

    def cwChatInputButtonDescription() : String

    def cwChatInputButtonName() : String

    def cwChatInputButtonSend() : String
    
    def cwChatLeftRoom() : String
    
    def cwChatJoinedRoom() : String

    def cwChatColorMessageSelf() : String
  }
}

/**
 * Example file.
 */
@ShowcaseStyle(Array(
    ".gwt-RichTextArea", ".hasRichTextToolbar", ".gwt-RichTextToolbar",
    ".cw-RichText", ".gwt-Button"))
class CwChatInputText(
  constants: CwChatInputText.CwConstants
) extends ContentWidget( constants ) with WSChannelMgr {

  override def getDescription() = constants.cwChatIOTextDescription

  override def getName() = constants.cwChatIOTextName

  case class ChatCluster(
    inputArea : RichTextArea,
    displayArea : HTMLPanel,
    sendButton : Option[Button],
    room : String
  )

  //lazy val logger : Logger = Logger.getLogger( classOf[CwChatInputText].getName );  

  /**
   * Initialize this example.
   */
  @ShowcaseSource
  override def onInitialize(): Widget = {
    // set up websockets
    createWS( "ws://localhost:8180/ws" )
    createWS( "ws://localhost:8888/room1" )
    createWS( "ws://localhost:8888/room2" )
    createWS( "ws://localhost:8888/room3" )
    createWS( "ws://localhost:8888/room4" )

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

    vPanel.add( grid )

    hPanel.add( vPanel )    

    val chatCluster =
      ChatCluster(
	area,
	logPanel,
	None, 
	"ws://localhost:8180/ws"
      )

    // Add a normal button
    val sendButton : Button =
      new Button(
        constants.cwChatInputButtonSend,
        ( clickEvent : ClickEvent ) => {	  	  	  
	  sendChat( chatCluster, clickEvent )
	}
      )        

    val wsl =
      WSListener(
	() => {
	  addChatLine(
	    chatCluster.displayArea,
	    constants.cwChatLeftRoom,
	    constants.cwChatColorMessageSelf
	  )
	},
	( msg : String ) => {
	  addChatLine(
	    chatCluster.displayArea,
	    msg,
	    constants.cwChatColorMessageSelf
	  )
	},
	() => {
	  addChatLine(
	    chatCluster.displayArea,
	    constants.cwChatJoinedRoom,
	    constants.cwChatColorMessageSelf
	  )
	}
      )

    // for( ws <- getWS( "ws://localhost:8180/ws" ) ) {
//       ws.addListener( toListener( wsl ) )
//     }

    sendButton.ensureDebugId( "cwChatInputButton-send" )
    vPanel.add( sendButton )

    hPanel
  }

  def addChatLine( logPanel : HTMLPanel, line : String, color : String ) : Unit = {
    val newLine : HTML = new HTML( line )
    newLine.getElement().getStyle().setColor( color )
    logPanel.add( newLine )
    newLine.getElement().scrollIntoView()
  }

  def sendChat( chatCluster : ChatCluster, click : ClickEvent ) : Unit = {    
    val line = chatCluster.inputArea.getText      
    Window.alert( line )

    val ws = socketsMap.get( chatCluster.room )
    
    if ( ws != null ) {      
      Window.alert( "found a websocket for the room" )
      addChatLine(
	chatCluster.displayArea,
	line,
	constants.cwChatColorMessageSelf
      )
      chatCluster.inputArea.setText( "" )
      ws.send( new ws.WebPortOutMsg( line ) )
    }
  }

  override def asyncOnInitialize(callback: AsyncCallback[Widget]) = {
    GWT.runAsync(new RunAsyncCallback() {
      def onFailure(caught: Throwable) = callback.onFailure(caught)

      def onSuccess() = callback.onSuccess(onInitialize())
    })
  }
}
