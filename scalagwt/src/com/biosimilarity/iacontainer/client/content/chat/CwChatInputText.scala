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

import com.sksamuel.gwt.websockets._

import java.io.Serializable

import java.util.HashMap
import java.util.Map
import java.net.URI
import java.util.logging.Level
import java.util.logging.Logger

case class WSListener(
  onClose : () => Unit,
  onMessage : String => Unit,
  onOpen : () => Unit
)

trait WSChannelMgr {
  lazy val socketsMap : Map[String,Websocket] = new HashMap

  // create sockets
  
  implicit def createWS( /* uri : URI */ uri : String ) : Websocket = {
    val socket : Websocket = new Websocket( uri )
    socketsMap.put( uri, socket )
    socket
  }
  
  implicit def toListener( wsl : WSListener ) : WebsocketListener = {
    new WebsocketListener() {
      override def onClose() : Unit = {
        wsl.onClose()
      }
      
      override def onMessage( msg : String ) : Unit = {
        wsl.onMessage( msg )
      }
      
      override def onOpen() : Unit = {
        wsl.onOpen()
      }
    }
  }

  def withSocket( ws : Websocket )(
    body : Websocket => Unit
  ) : Unit = {
    body( ws )
  }

  def withSocket( ws : Websocket, wsl : WSListener )(
    body : Websocket => Unit
  ) : Unit = {
    ws.addListener( wsl )
    body( ws )
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
    area.ensureDebugId("cwRichText-area")
    area.setSize("100%", "14em")
    val toolbar = new RichTextToolbar(area)
    toolbar.ensureDebugId("cwRichText-toolbar")
    toolbar.setWidth("100%")

    // Add the components to a panel
    val grid = new Grid( 2, 1 )
    grid.setStyleName("cw-RichText")
    grid.setWidget(0, 0, toolbar)
    grid.setWidget(1, 0, area)
    //grid

    vPanel.add( grid )

    hPanel.add( vPanel )

    // Add a normal button
    val sendButton : Button =
      new Button(
        constants.cwChatInputButtonSend,
        ( clickEvent : ClickEvent ) => {
	  val chatCluster =
	    ChatCluster(
	      area,
	      logPanel,
	      None, 
	      "ws://localhost:8888/room1"
	    )
	  sendChat( chatCluster, clickEvent )
	}
      )

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
      //Window.alert( "found a websocket for the room" )
      addChatLine(
	chatCluster.displayArea,
	line,
	constants.cwChatColorMessageSelf
      )
      chatCluster.inputArea.setText( "" )
      ws.send( line )
    }
  }

  override def asyncOnInitialize(callback: AsyncCallback[Widget]) = {
    GWT.runAsync(new RunAsyncCallback() {
      def onFailure(caught: Throwable) = callback.onFailure(caught)

      def onSuccess() = callback.onSuccess(onInitialize())
    })
  }
}
