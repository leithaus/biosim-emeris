package biosim.client.ui;

import java.util.List;

import org.vectomatic.file.File;
import org.vectomatic.file.FileList;
import org.vectomatic.file.FileReader;
import org.vectomatic.file.FileUploadExt;
import org.vectomatic.file.events.LoadEndEvent;
import org.vectomatic.file.events.LoadEndHandler;

import m3.fj.F1;
import m3.gwt.lang.Function0;
import m3.gwt.lang.Function1;
import m3.gwt.lang.ListX;
import biosim.client.Biosim;
import biosim.client.DndController;
import biosim.client.Globals;
import biosim.client.eventlist.ui.PopupMenu;
import biosim.client.messages.model.FilterAcceptCriteria;
import biosim.client.messages.model.MBlob;
import biosim.client.messages.model.MIconNode;
import biosim.client.messages.model.MImage;
import biosim.client.messages.model.MLabel;
import biosim.client.messages.model.MNode;
import biosim.client.ui.dnd.DndType;
import biosim.client.utils.Base64;
import biosim.client.utils.DialogHelper;
import biosim.client.utils.GqueryUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class NodeWidgetBuilder {

	CustomFlowPanel _widget = new CustomFlowPanel();
	SimplePanel _wrapper = new SimplePanel();
	
	final MNode _node;
	final DndType _dndType;
	final DndController _dndController;
	
	Image _icon;
	Widget _content;
	CustomFlowPanel _sourceLabels = new CustomFlowPanel();
	List<Widget> _sourceLabelWidgets = ListX.create();
	FilterAcceptCriteria _filterAcceptCriteria;
	final F1<MNode,String> _labelProvider;

	public NodeWidgetBuilder(MNode node, DndController dndController, DndType dndType) {
		this(node, dndController, dndType, null);
	}
	
	public NodeWidgetBuilder(MNode node, DndController dndController, DndType dndType, F1<MNode,String> labelProvider) {
		_node = node;
		_dndType = dndType;
		_dndController = dndController;
		if ( labelProvider == null ) {
			labelProvider = new F1<MNode, String>() {
				@Override
				public String f(MNode a) {
					return a.toHtmlString();
				}
			};
		}
		_labelProvider = labelProvider;
		rebuild();
	}
	
	HTMLPanel createDragHandle() {
		HTMLPanel dragHandle = new HTMLPanel("div", "");
		dragHandle.getElement().getStyle().setHeight(100.0, Unit.PCT);
		
		Image dragTexture = new Image("images/drag_background_wide.png");
		dragTexture.setWidth("20px");
		dragTexture.setHeight("100%");
		dragTexture.addStyleName("ui-corner-tl ui-corner-bl");
		Style textureStyle = dragTexture.getElement().getStyle();
		textureStyle.setMargin(0, Unit.PX);

		dragHandle.add(dragTexture);
		_dndController.makeDraggable(_dndType, _node, _widget, dragTexture);

		return dragHandle;
	}
	
	void addChildLabel(final MIconNode parent) {
		DialogHelper.showSingleLineTextPrompt("Enter the name of child label to add:", "", "200px 20px", new Function1<String,Void>() {
			public Void apply(final String t) {
				if ( t != null && t.trim().length() > 0 ) {
					Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {						
						@Override
						public void execute() {
							Biosim.get().getLocalAgent().insertChild(parent, new MLabel(t));
						}
					});
				}
				return null;
			}
		});
	}
	
	void editLabel(final MIconNode node) {
		final VerticalPanel panel = new VerticalPanel();
		HTML l1 = new HTML("Name:");
		panel.add(l1);
		final TextBox textBox = new TextBox();
		textBox.setText(node.getName());
		panel.add(textBox);
		
		HTML l2 = new HTML("Icon:");
		panel.add(l2);
        final FileUploadExt fileUploadExt = new FileUploadExt();
		panel.add(fileUploadExt);
		DialogHelper.showWidgetPrompt("Edit Label", panel, "200px 20px", new Function1<VerticalPanel,Void>() {
			public Void apply(VerticalPanel p) {
				if (p != null) {
					// Set the new name the node, if it is set
					if (!textBox.getText().isEmpty()) {
						node.setName(textBox.getText());
						Biosim.get().getLocalAgent().insertOrUpdate(node);
					}
					
					// Get the files that were selected 
                    FileList fileList = fileUploadExt.getFiles();
                    for ( File file0 : fileList ) {
                        final File file = file0;
                        final FileReader reader = new FileReader();
                        reader.addLoadEndHandler(new LoadEndHandler() {
                            @Override
                            public void onLoadEnd(LoadEndEvent event) {
                                try {
                                    String buffer = reader.getStringResult();
                                    String base64 = Base64.toBase64(buffer);
                                    MBlob blob = new MBlob(_node.getAgentServices().getAgentUid(), file.getName());
                                    blob.setDataInBase64(base64);
                                    node.setIcon(blob.getRef());
                                    Biosim.get().getLocalAgent().insertOrUpdate(blob, node);
                                } catch ( Exception e ) {
                                    GWT.log("something bad happened", e);
                                }
                            }
                        });
                        reader.readAsBinaryString(file);
                    }
				}
				return null;
			}
		});		
	}
	
	void createPopupMenu() {
		if (!_node.isEditable() || !(_node instanceof MIconNode)) return;
		
		final PopupMenu popup = new PopupMenu();
		final Button pop = new Button("&raquo;");			
		_widget.add(pop);
		pop.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
			    popup.show(event);
			}
		});
		popup.addOption("Add Child Label...", new Function0<Void>() {
            @Override
            public Void apply() {
			    addChildLabel((MIconNode)_node);
                return null;
            }
        });

		popup.addOption("Edit...", new Function0<Void>() {
            @Override
            public Void apply() {
			    editLabel((MIconNode)_node);
                return null;
            }
        });
		
		_widget.addDomHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				GqueryUtils.setVisibility(pop.getElement(), Visibility.HIDDEN);
			}
		},  MouseOutEvent.getType());
		
		_widget.addDomHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				GqueryUtils.setVisibility(pop.getElement(), Visibility.VISIBLE);
			}
		},  MouseOverEvent.getType());
		
		GqueryUtils.setVisibility(pop.getElement(), Visibility.HIDDEN);
	}
	
	public void rebuild() {
		
		_widget = new CustomFlowPanel();
		_widget.setStyleName(Globals._boxStyle, true);
		
		_widget.add(createDragHandle(), "vertFill fleft ui-corner-tl ui-corner-bl");

		if ( _node.getIconUrl() != null ) {
			_icon = new Image(_node.getIconUrl());
			_icon.addStyleName("node-icon");
			_widget.add(_icon);
		}
		
		String label = _labelProvider.f(_node);
		
		if ( _node instanceof MImage ) {
			
			HorizontalPanel p = new HorizontalPanel();
			p.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			
			// Create the image element
			final String imageURL = ((MImage)_node).getBlobRef().getUrl();
			Image i = new Image(imageURL);
			if (i.getHeight() > i.getWidth()) {
				i.setHeight("150px");
			} else {
				i.setWidth("150px");
			}
			p.add(i);
			
			// Create a button to display a full sized version of the image
			Button b = new Button("View...", new ClickHandler() {
			      public void onClick(ClickEvent event) {
			          Window.open(imageURL, "imageURL", "");
			        }
			});
			b.setTitle("View full sized image in a new window");
			p.add(b);
			
			_content = p;
			_widget.setHeight("175px");
		} else if ( label != null ) {
			_content = new HTML(label);
			_widget.setHeight("70px");
		}

		if ( _content != null ) {
			_widget.add(_content);
		}

		_dndController.registerDropSite(_dndType, _node, _widget);
		
		_widget.add(_sourceLabels);
		
		if ( _content != null ) {
			Scheduler.get().scheduleFinally(new ScheduledCommand() {
		        @Override
		        public void execute() {
		        	GqueryUtils.center(_content.getElement());
		        }
		    });
		} else {
			toString();
		}
		
		createPopupMenu();
		
		_wrapper.setWidget(_widget);
	}
	
	public void setFilterAcceptCriteria(FilterAcceptCriteria filterAcceptCriteria) {
		_filterAcceptCriteria = filterAcceptCriteria;
		for ( Widget w : _sourceLabelWidgets ) {
			_sourceLabels.remove(w);
		}
		_sourceLabelWidgets.clear();
		if ( _filterAcceptCriteria != null ) {
			for ( String p : _filterAcceptCriteria.getPaths() ) {
				Widget w = FilterBar.createFilterWidget(p);
				_sourceLabelWidgets.add(w);
				_sourceLabels.add(w);
			}
		}
	}
	public FilterAcceptCriteria getFilterAcceptCriteria() {
		return _filterAcceptCriteria;
	}
	
	public MNode getNode() {
		return _node;
	}
	
	public Image getImage() {
		return _icon;
	}
	
	public Widget getPanel() {
		return _wrapper;
	}
	
	public FlowPanel getFlowPanel() {
		return _widget;
	}

	public boolean isVisible() {
		return _widget.isVisible();
	}
	
}
