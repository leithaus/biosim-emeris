package biosim.client;

import static com.google.gwt.query.client.GQuery.$;
import m3.gwt.lang.Function1;
import m3.gwt.lang.LogTool;
import biosim.client.eventlist.ListEvent;
import biosim.client.eventlist.ListListener;
import biosim.client.eventlist.ObservableList;
import biosim.client.eventlist.Observables;
import biosim.client.messages.model.MConnection;
import biosim.client.messages.model.MNode;
import biosim.client.messages.model.NodeContainer;
import biosim.client.messages.model.RemoteServices;
import biosim.client.messages.model.RemoteServicesImpl;
import biosim.client.messages.model.Uid;
import biosim.client.messages.protocol.MessageHandler;
import biosim.client.ui.ContentCriteria;
import biosim.client.ui.CustomTabPanel;
import biosim.client.ui.FilterBar;
import biosim.client.ui.NodePanel;
import biosim.client.ui.NodeWidgetBuilder;
import biosim.client.ui.dnd.DndType;
import biosim.client.ui.score.EventScoreBuilder;
import biosim.client.utils.BiosimWebSocket;
import biosim.client.utils.DialogHelper;
import biosim.client.utils.GqueryUtils;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.query.client.Function;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Biosim implements EntryPoint {

	public static final String _connectionDropHover = "ui-state-active";
	public static final String _connectionIconDragging = "ui-state-highlight";
	public static final String _connectionSelected = "ui-state-focus";
	public static final String _boxStyle = "biosimbox ui-widget-content ui-corner-all";
	
	static Biosim _instance;
	public static Biosim get() {
		return _instance;
	}
	
	CustomTabPanel _labelsSection = new CustomTabPanel();
	CustomTabPanel _contentSection = new CustomTabPanel();
	CustomTabPanel _connectionsSection = new CustomTabPanel();

	FlowPanel _labelsSectionContent;
	
	ContentController _contentController;
	
	FilterBar _filtersBar = new FilterBar(this);

	MConnection _selectedConnection;
	
	DndController _dndController;
	
	boolean _devMode = Window.Location.getParameter("devmode") != null;

	Image _scissors = new Image("/images/scissors.png");

	Uid _senderUid = Uid.random();
	
	BiosimWebSocket _socket;
	RemoteServices _remoteServices;
	
	AgentManagerPanel _agentManagerPanel;
	
	LabelTreeBuilder _labelTreeBuilder;
	
	EventScoreBuilder _eventScore;
	
	public Biosim() {
	}

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		
		BiosimUberContext.get();
		
	    GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
            @Override
            public void onUncaughtException(Throwable e) {
                if ( e instanceof UmbrellaException ) {
                    UmbrellaException ue = (UmbrellaException) e;
                    for ( Throwable th : ue.getCauses() ) {
                        onUncaughtException(th);
                    }
                }
                StringBuilder sb = new StringBuilder();
                sb.append(e.getMessage());
                sb.append("\n");
                for ( StackTraceElement el : e.getStackTrace() ) {
                    sb.append("    " + el.toString());
                    sb.append("\n");
                }
                LogTool.warn(sb.toString());
                Throwable cause = e.getCause();
                if ( cause != null && cause != e ) {
                    onUncaughtException(cause);
                }
            }
        });
	    
		initAgentManagerPanel();

	    if ( RootPanel.get("tags") != null ) {
			
			initWebSocket();
			
			NodeContainer.get().nodes.addListener(new ListListener<MNode>() {
				@Override
				public void event(ListEvent<MNode> event) {
					_contentController.refilterContent();
				}
			});
	
			_instance = this;
	//		_composeMessageBuilder = new ComposeMessageBuilder(this);
	
			_dndController = new DndController(this, RootPanel.get());
			
			// TDGlen wire in proper query stuff here
			ObservableList<MNode> fakeContent = Observables.create();
			_contentController = new ContentController(this, fakeContent, _dndController);
//			_contentController = new ContentController(this, _databaseAccessLayer.getContent(), _dndController);
	
			DockPanel dock = new DockPanel();
	    
			dock.setSize("100%","100%");
			
			_labelsSection.setSize("100%", "100%");
			_labelsSection.setTabStyleNames("ui-state-default ui-corner-top");
			_labelsSection.setSelectedTabStyleNames("ui-state-active ui-corner-top");
			_labelsSectionContent = _labelsSection.createTab("Labels");
			_labelsSection.selectTab(0);
			
			_contentSection.setSize("100%", "100%");
			_contentSection.setTabStyleNames("ui-state-default ui-corner-top");
			_contentSection.setSelectedTabStyleNames("ui-state-active ui-corner-top");
			FlowPanel contentSectionContent = _contentSection.createTab("Content");
			contentSectionContent.getElement().getStyle().setOverflow(Overflow.HIDDEN);
	//		FlowPanel composeTab = _contentSection.createTab("Compose");
	//		composeTab.add(_composeMessageBuilder.getPanel());
			
			_contentSection.selectTab(0);
			
			contentSectionContent.add(_filtersBar.getPanel());
			
			_connectionsSection.setSize("100%", "100%");
			_connectionsSection.setTabStyleNames("ui-state-default ui-corner-top");
			_connectionsSection.setSelectedTabStyleNames("ui-state-active ui-corner-top");
			FlowPanel connectionsSectionContent = _connectionsSection.createTab("Connections");
			_connectionsSection.selectTab(0);
			
			addToRootPanel("tags", _labelsSection);
			addToRootPanel("contents", _contentSection);
			addToRootPanel("connections", _connectionsSection);
	
			contentSectionContent.add(_contentController._contentPanel);
			
			connectionsSectionContent.add(NodePanel.create(NodeContainer.get().connections, _dndController, DndType.Connection));
			
			_contentController.refilterContent();
			
			_scissors.addStyleName("fright");
			_contentSection.getTabBar().add(_scissors);
			_dndController.makeDraggable(DndType.Scissors, null, _scissors, _scissors);
			_dndController.registerDropSite(DndType.Scissors, null, _scissors);
			
			Scheduler.get().scheduleFinally(new ScheduledCommand() {
		        @Override
		        public void execute() {
		        	$(".vertCenter").each(new Function() {
		    			@Override
		    			public void f(Element e) {
		    				GqueryUtils.center(e);
		    			}
		    		});  
		        	$(".vertFill").each(new Function() {
		    			@Override
		    			public void f(Element e) {
		    				GqueryUtils.fill(e);
		    			}
		    		});  
		        	fitContentSectionInJava();
		        }
		    });
			
			// TDGlen fix this we no longer have a data set
			_eventScore = new EventScoreBuilder();
			RootPanel scoreTab = RootPanel.get("scoreTab");
			if ( scoreTab != null ) {
				scoreTab.add(_eventScore.getWidget());
			}
	
			RootPanel developerTab = RootPanel.get("devTab");
			if ( developerTab != null ) {
				DeveloperPanel developerPanel = new DeveloperPanel();
				developerTab.add(developerPanel.getPanel());
			}
	    }
		
//		fitContentSectionInJava();
	}

	void addToRootPanel(String tag, Widget widget) {
		RootPanel rootPanel = RootPanel.get(tag);
		if ( rootPanel != null ) {
			rootPanel.add(widget);
		}
	}

	private void initAgentManagerPanel() {
		RootPanel agentManagerTab = RootPanel.get("agentProvisioningTab");
		if ( agentManagerTab != null ) {
			_agentManagerPanel = new AgentManagerPanel();
			agentManagerTab.add(_agentManagerPanel.getPanel());
		}
	}
	
	native void fitContentSectionInJava() /*-{
	  $wnd.fitContentSection(); // $wnd is a JSNI synonym for 'window'
	}-*/;
	
	void initWebSocket() {
		
		String wsUrl;
		if ( GWT.isProdMode() ) {
			wsUrl = "ws://" + Window.Location.getHostName() + ":" + Window.Location.getPort() + "/ws";
		} else {
			wsUrl = "ws://127.0.0.1:8080/ws";
		}
		
		wsUrl += "?agentUid=" + getAgentUid().asString();

//		DialogHelper.alert("using wsUrl = " + wsUrl);
		
		_socket = new BiosimWebSocket(wsUrl, new MessageHandler(), new Function1<Void, Void>() {
			
			@Override
			public Void apply(Void t) {
				_remoteServices = new RemoteServicesImpl(_socket, NodeContainer.get());
				
				_labelTreeBuilder = 
						new LabelTreeBuilder(
								getAgentUid()
								, _dndController
								, _remoteServices
								, _socket
						);
				_labelsSectionContent.add(_labelTreeBuilder.getTree());
				
				_labelsSectionContent.add(new ConnectionViewDropSiteBuilder(_dndController).getWidget());
				
				_remoteServices.select(MConnection.class, new Function1<Iterable<MConnection>, Void>() {
					public Void apply(Iterable<MConnection> t) {
						// do nothing since this call alone will trigger an update to the NodeContainer
						return null;
					}
				});

				return null;
			}
		});

	}

	void removeContentLinks(MNode node) {
		if ( node != null ) {
			NodeWidgetBuilder builder = _contentController.builder(node);
			ContentCriteria ac = builder.getFilterAcceptCriteria();
			
			if ( ac.connections.size() > 0 ) {
				for ( MConnection p : ac.connections ) {
					Biosim.get().getRemoteServices().removeLink(p, node);
				}
			} else {
				for ( MNode n : ac.labels ) {
					Biosim.get().getRemoteServices().removeLink(n, node);
				}
			}
		}
	}
	
	public ContentController getContentController() {
		return _contentController;
	}
	
	public static String getCurrentUiStateCssClass(String styleNames) {
		String uiStateCssClass = null;
		if(styleNames != null && styleNames.length() > 0) {
			styleNames = styleNames.trim();
			String[] classes = styleNames.split(" ");
			for(String cssClass : classes) {
				if(cssClass.startsWith("ui-state")) {
					uiStateCssClass = cssClass;
				}
			}
		}
		return uiStateCssClass;
	}

	public Uid getAgentUid() {
		String agentUid = Window.Location.getParameter("agentUid");
		if ( agentUid == null ) {
			String msg = "agentUid url parm is required";
			DialogHelper.alert(msg);
			throw new RuntimeException(msg);
		} else {
			return new Uid(agentUid);
		}
	}
	
	public LabelTreeBuilder getLabelTreeBuilder() {
		return _labelTreeBuilder;
	}
	
	public Uid getSenderUid() {
		return _senderUid;
	}

	public EventScoreBuilder getEventScore() {
		return _eventScore;
	}
	
	public BiosimWebSocket getSocket() {
		return _socket;
	}

	public RemoteServices getRemoteServices() {
		return _remoteServices;
	}
	
}
