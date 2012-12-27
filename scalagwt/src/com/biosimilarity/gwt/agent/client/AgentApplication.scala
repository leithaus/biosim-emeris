// -*- mode: Scala;-*- 
// Filename:    AgentApplication.scala 
// Authors:     lgm                                                    
// Creation:    Wed Dec 26 15:35:49 2012 
// Copyright:   Not supplied 
// Description: 
// ------------------------------------------------------------------------

package com.biosimilarity.gwt.agent.client


import com.google.gwt.core.client.GWT
import com.google.gwt.event.logical.shared.HasSelectionHandlers
import com.google.gwt.event.logical.shared.ResizeEvent
import com.google.gwt.event.logical.shared.ResizeHandler
import com.google.gwt.event.logical.shared.SelectionHandler
import com.google.gwt.event.shared.HandlerRegistration
import com.google.gwt.i18n.client.LocaleInfo
import com.google.gwt.resources.client.ImageResource
import com.google.gwt.user.client.Window
import com.google.gwt.user.client.ui.AbstractImagePrototype
import com.google.gwt.user.client.ui.Composite
import com.google.gwt.user.client.ui.DecoratorPanel
import com.google.gwt.user.client.ui.FlexTable
import com.google.gwt.user.client.ui.FlowPanel
import com.google.gwt.user.client.ui.Grid
import com.google.gwt.user.client.ui.HTML
import com.google.gwt.user.client.ui.HTMLPanel
import com.google.gwt.user.client.ui.HasHorizontalAlignment
import com.google.gwt.user.client.ui.HasVerticalAlignment
import com.google.gwt.user.client.ui.HorizontalPanel
import com.google.gwt.user.client.ui.RichTextArea
import com.google.gwt.user.client.ui.ScrollPanel
import com.google.gwt.user.client.ui.SimplePanel
import com.google.gwt.user.client.ui.Tree
import com.google.gwt.user.client.ui.TreeItem
import com.google.gwt.user.client.ui.VerticalPanel
import com.google.gwt.user.client.ui.Widget
import com.google.gwt.resources.client.ClientBundle.Source

object AgentApplication {
   /**
    * Images used in the  { @link Application }.
    */
   trait AgentApplicationImages extends Tree.Resources {
      /**
       * An image indicating a leaf.
       *
       * @return a prototype of this image
       */
      @Source(Array("LabelShape.png"))
      def treeLeaf: ImageResource
   }

   /**
    * The base style name.
    */
   val DEFAULT_STYLE_NAME = "Application"
}
trait AgentApplication {
  import AgentApplication._
  val userEngagementPanel = new HorizontalPanel
  val userEngagementLayout = new HorizontalPanel /* new Grid( 1, 3 ) */

  //val labelPanelDecorator = new DecoratorPanel
  val labelPanelScroll = new ScrollPanel
  val labelPanel = new VerticalPanel
  val labelTree =
    new Tree(GWT.create(classOf[AgentApplicationImages]).asInstanceOf[AgentApplicationImages])
  
  //val userContentPanelDecorator = new DecoratorPanel
  val userContentPanelScroll = new ScrollPanel  
  val userContentPanel = new VerticalPanel  

  //val connectionPanelDecorator = new DecoratorPanel
  val connectionPanelScroll = new ScrollPanel
  val connectionPanel = new VerticalPanel
  val connectionTree =
    new Tree(GWT.create(classOf[AgentApplicationImages]).asInstanceOf[AgentApplicationImages])

  val userContentFilterDecorator = new DecoratorPanel
  val userContentFilterPanel = new HorizontalPanel
  //val userContentPostDecorator = new DecoratorPanel
  val userContentPostPanel = new HorizontalPanel
  val userContentPostInput = new RichTextArea
  //val userContentDisplayDecorator = new DecoratorPanel
  val userContentDisplayPanel = new VerticalPanel  
  val userContentDisplayLog = new HTMLPanel("") {
      override def add( widget : Widget ) : Unit = {
        super.add( widget )
        widget.getElement().scrollIntoView()
      }
    }

  val images : ShowcaseImages =
    GWT.create(classOf[ShowcaseImages]).asInstanceOf[ShowcaseImages]

  def installAgentPanel( layout : FlowPanel, userEngagementPanel : HorizontalPanel ) : Unit = {
    userEngagementPanel.setWidth("100%")
    userEngagementPanel.setSpacing(0)
    userEngagementPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP)
    userEngagementPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER)
    
    //userEngagementLayout.setCellPadding(0)
    //userEngagementLayout.setCellSpacing(0)
    userEngagementLayout.setWidth("100%")

    userEngagementPanel.add( userEngagementLayout )

    installUserEngagementPanel( userEngagementLayout )

    layout.add( userEngagementPanel )
  }

  def installUserEngagementPanel( userEngagementLayout : HorizontalPanel /* Grid */ ) : Unit = {
    installLabelPanel( labelPanel, labelTree )
    installContentPanel( userContentPanel )
    installConnectionPanel( connectionPanel )

    //labelPanelDecorator.setWidget( labelPanel )
    //labelPanelDecorator.addStyleName(DEFAULT_STYLE_NAME + "-content-decorator")
    //userEngagementLayout.setWidget( 0, 0, labelPanelDecorator )
    userEngagementLayout.add( labelPanelScroll )

    //userContentPanelDecorator.setWidget( userContentPanel )
    //userContentPanelDecorator.addStyleName(DEFAULT_STYLE_NAME + "-content-decorator")
    //userEngagementLayout.setWidget( 0, 1, userContentPanelDecorator )

    //userContentPanelScroll.setWidget( userContentPanelDecorator )    

    //userEngagementLayout.add( userContentPanelScroll )
    userEngagementLayout.add( userContentPanel )

    //connectionPanelDecorator.setWidget( connectionPanel )
    //connectionPanelDecorator.addStyleName(DEFAULT_STYLE_NAME + "-content-decorator")
    //userEngagementLayout.setWidget( 0, 2, connectionPanelDecorator )
    userEngagementLayout.add( connectionPanelScroll )
  }

  def installLabelPanel( labelPanel : VerticalPanel, labelTree : Tree ) : Unit = {
    val item =
      labelTree.addItem(
	AbstractImagePrototype.create( images.catWidgets ).getHTML + " " + "My Agent"
      )    

    item.addItem( AbstractImagePrototype.create( images.catWidgets ).getHTML + " " + "Apps" )

    item.addItem( AbstractImagePrototype.create( images.catWidgets ).getHTML + " " + "Video" )

    item.addItem( AbstractImagePrototype.create( images.catWidgets ).getHTML + " " + "Audio" )

    item.addItem( AbstractImagePrototype.create( images.catWidgets ).getHTML + " " + "Photos" )

    item.addItem( AbstractImagePrototype.create( images.catWidgets ).getHTML + " " + "Docs" )

    item.addItem( AbstractImagePrototype.create( images.catWidgets ).getHTML + " " + "Notes" )

    labelPanelScroll.setWidget( labelTree )
    labelPanelScroll.setAlwaysShowScrollBars( true )

    labelPanel.add( labelPanelScroll )
  }

  def installContentPanel( userContentPanel : VerticalPanel ) : Unit = {    
    userContentPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER)
    userEngagementPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP)

    installContentFilterPanel( userContentFilterPanel )
    installContentPostPanel( userContentPostPanel )
    installContentDisplayPanel( userContentDisplayPanel )                

    //userContentPanelScroll.setAlwaysShowScrollBars( true )
    //userContentPanelScroll.setWidget( userContentPanel )

    //userContentDisplayDecorator.setWidget( userContentDisplayPanel )    
    //userContentPanel.add( userContentPanelScroll )
  }

  def installContentFilterPanel( userContentFilterPanel : HorizontalPanel ) : Unit = {
    userContentFilterPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER)
    userContentFilterPanel.add( new HTML( "Drop labels and connections here" ) )

    userContentFilterPanel.setSize( "100%", "6em" )    
    userContentFilterDecorator.setWidget( userContentFilterPanel )
    userContentPanel.add( userContentFilterDecorator )    
  }

  def installContentPostPanel( userContentPostPanel : HorizontalPanel ) : Unit = {
    // Create the text area and toolbar
    val area = userContentPostInput
    area.ensureDebugId( "cwRichText-area" )
    area.setSize( "100%", "4em" )
    //area.getBasicFormatter.setJustification( RichTextArea.Justification.CENTER )
    area.setHTML( "Share something!" )
    // val toolbar = new RichTextToolbar( area )
//     toolbar.ensureDebugId( "cwRichText-toolbar" )
//     toolbar.setWidth( "100%" )

    // Add the components to a panel
    val grid = new Grid( 2, 1 )
    grid.setStyleName("cw-RichText")
    //grid.setWidget(0, 0, toolbar)
    grid.setWidget(1, 0, area)

    userContentPostPanel.add( grid )
    
    userContentPostPanel.setSize( "100%", "6em" )
    userContentPanel.add( userContentPostPanel )
  }

  def installContentDisplayPanel( userContentDisplayPanel : VerticalPanel ) : Unit = {
    val logPanel : HTMLPanel = userContentDisplayLog

    logPanel.add( new HTML( "Welcome to your agent" ) )    
    userContentDisplayPanel.add( logPanel )

    userContentDisplayPanel.setSize( "100%", "28em" )
    userContentPanel.add( userContentDisplayPanel )
  }  

  def installConnectionPanel( connectionPanel : VerticalPanel ) : Unit = {    
    val item =
      connectionTree.addItem(
	AbstractImagePrototype.create( images.catTables ).getHTML + " " + "My Connections"
      )        
    item.addItem( AbstractImagePrototype.create( images.catLists ).getHTML + " " + "Friends" )
    item.addItem( AbstractImagePrototype.create( images.catLists ).getHTML + " " + "Family" )
    item.addItem( AbstractImagePrototype.create( images.catLists ).getHTML + " " + "Colleagues" )

    connectionPanelScroll.setWidget( connectionTree )
    connectionPanelScroll.setAlwaysShowScrollBars( true )

    connectionPanel.add( connectionPanelScroll )

    // val logPanel : HTMLPanel = new HTMLPanel("") {
//       override def add( widget : Widget ) : Unit = {
//         super.add( widget )
//         widget.getElement().scrollIntoView()
//       }
//     }
//     connectionPanel.add( logPanel )
  }

  def onWindowResizedAgent( width : Int ) : Unit = {
    val labelPanelWidth = labelPanelScroll.getOffsetWidth
    val connectionPanelWidth = connectionPanelScroll.getOffsetWidth
    val adjustedWidth = width - ( ( labelPanelWidth + connectionPanelWidth ) - 20 )
    val contentWidth = math.max( adjustedWidth, 1 )
    val contentWidthInner = math.max( contentWidth - 20, 1 )

    //Window.alert( "contentWidth = " + contentWidth )

    userContentPanel.setWidth( ( contentWidthInner ) + "px" )
    userContentFilterPanel.setWidth( ( contentWidthInner ) + "px" )
    userContentPostPanel.setWidth( ( contentWidthInner ) + "px" )
    userContentPostInput.setWidth( ( contentWidthInner ) + "px" )    
    userContentDisplayPanel.setWidth( ( contentWidthInner ) + "px" )
    userContentDisplayLog.setWidth( ( contentWidthInner ) + "px" )

    labelPanel.setWidth( labelPanelWidth + "px" )

    connectionPanel.setWidth( connectionPanelWidth + "px" )
  }
}
