package biosim.client;

import m3.gwt.lang.Function0;
import m3.gwt.lang.Function1;

import org.vectomatic.file.File;
import org.vectomatic.file.FileList;
import org.vectomatic.file.FileReader;
import org.vectomatic.file.FileUploadExt;
import org.vectomatic.file.events.LoadEndEvent;
import org.vectomatic.file.events.LoadEndHandler;

import biosim.client.eventlist.ListEvent;
import biosim.client.eventlist.ListEventType;
import biosim.client.eventlist.ListListener;
import biosim.client.eventlist.ui.PopupMenu;
import biosim.client.messages.model.LocalAgent;
import biosim.client.messages.model.MAgent;
import biosim.client.messages.model.MBlob;
import biosim.client.messages.model.MImage;
import biosim.client.messages.model.MLabel;
import biosim.client.messages.model.MLink;
import biosim.client.messages.model.MNode;
import biosim.client.messages.model.NodeContainer;
import biosim.client.messages.model.Uid;
import biosim.client.ui.NodeWidgetBuilder;
import biosim.client.ui.dnd.DndType;
import biosim.client.utils.Base64;
import biosim.client.utils.BiosimWebSocket;
import biosim.client.utils.DialogHelper;
import biosim.client.utils.GqueryUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LabelTreeBuilder {
    
	final Tree _tree = new Tree();

	final Uid _agentUid;
	final DndController _dndController;
	final LocalAgent _localAgent;
//	final AgentServices _remoteServices;
	final BiosimWebSocket _socket;
	
	LabelTreeBuilder(
			Uid agentUid, 
			DndController dndController, 
			LocalAgent localAgent,
			BiosimWebSocket socket
	) {
		_agentUid = agentUid;
		_dndController = dndController;
		_localAgent = localAgent;
		_socket = socket;

		NodeContainer.get().nodes.addListener(new ListListener<MNode>() {
			@Override
			public void event(ListEvent<MNode> event) {
				TreeItem ti = getTreeItemFromUid(event.getElement().getUid());
				
				if (event.getElement() instanceof MLabel) {
					MLabel label = (MLabel)event.getElement();
					if (event.getType() == ListEventType.Added) {
						onAddedLabel(ti, label);
					} else if (event.getType() == ListEventType.Changed) {
						onChangedLabel(ti, label);
					} else if (event.getType() == ListEventType.Removed) {
						onRemovedLabel(ti, label);
					}
				} else if (event.getElement() instanceof MLink) {
					//MLink link = (MLink)event.getElement();
				}
			}
		});
		
		addRootLabelsForAgent(_agentUid);
	}
	
	TreeItem itemFromUid(TreeItem ti, Uid uid) {
		MLabel ml = getUserObject(ti);
		if (ml.getUid().equals(uid)) {
			return ti;
		} else {
			TreeItem t2 = null;
			for (int j = 0; j < ti.getChildCount() && t2 == null; j++) {
				t2 = this.itemFromUid(ti.getChild(j), uid);
			}
			return t2;
		}
	}
	
	TreeItem getTreeItemFromUid(Uid uid) {
		TreeItem ret = null;
		for (int i = 0; i < _tree.getItemCount() && ret == null; i += 1) {
			ret = itemFromUid(_tree.getItem(i), uid);
		}
		
		return ret;
	}
	
	void onAddedLabel(TreeItem ti, MLabel label) {
		if (ti != null) {
			
		}
	}
	
	void onChangedLabel(TreeItem ti, MLabel label) {
		if (ti != null){
			ti.setUserObject(label);
			updateTreeItem(ti);
		}
	}

	void onRemovedLabel(TreeItem ti, MLabel label) {
		if (ti != null) {
			TreeItem pi = ti.getParentItem();
			MLabel l = this.getUserObject(pi);
			
			MLabel newLabel = (MLabel)NodeContainer.get().nodesByUid.get(l.getUid());
			pi.setUserObject(newLabel);
			updateTreeItem(pi);
			
			_tree.removeItem(ti);
		} else {
			// We have to check to see if the MLabel is the child of an existing
			// node in the tree, then get the MLabel for that parent and update the 
			// tree with it there.
		}
	}
	
	TreeItem createTreeItem(TreeItem parent, MLabel label) {
		TreeItem ti = new TreeItem();
		if ( parent != null ) {
			parent.addItem(ti);
		} else {
			_tree.addItem(ti);
		}
		ti.setUserObject(label);
		updateTreeItem(ti);
		return ti;
	}
	
    void updateTreeItem(TreeItem ti) {
		final MLabel label = getUserObject(ti);
		final NodeWidgetBuilder nwbuilder = new NodeWidgetBuilder(label, _dndController, DndType.Label); 
		final FlowPanel w = nwbuilder.getFlowPanel();
		final PopupMenu popup = new PopupMenu();
		ti.setWidget(w);
				
		if ( label.isEditable() ) {	
			final Button pop = new Button("&raquo;");			
			w.add(pop);
			pop.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
				    popup.show(event);
				}
			});
			popup.addOption("Add Child Label...", new Function0<Void>() {
	            @Override
	            public Void apply() {
				    addLeaf(label);
	                return null;
	            }
	        });

			popup.addOption("Edit...", new Function0<Void>() {
	            @Override
	            public Void apply() {
				    editLabel(label);
	                return null;
	            }
	        });
			
			popup.addOption("Add Connection...", new Function0<Void>() {
	            @Override
	            public Void apply() {
				    addLeaf(label);
	                return null;
	            }
	        });
			
			w.addDomHandler(new MouseOutHandler() {
				@Override
				public void onMouseOut(MouseOutEvent event) {
					GqueryUtils.setVisibility(pop.getElement(), Visibility.HIDDEN);
				}
			},  MouseOutEvent.getType());
			
			w.addDomHandler(new MouseOverHandler() {
				@Override
				public void onMouseOver(MouseOverEvent event) {
					GqueryUtils.setVisibility(pop.getElement(), Visibility.VISIBLE);
				}
			},  MouseOverEvent.getType());
			
			GqueryUtils.setVisibility(pop.getElement(), Visibility.HIDDEN);
		}
		
	}
	
	void addLeaf(final MLabel parent) {
		DialogHelper.showSingleLineTextPrompt("Enter the name of child label to add:", "", "200px 20px", new Function1<String,Void>() {
			public Void apply(String t) {
				if ( t != null && t.trim().length() > 0 ) {
					_localAgent.insertChild(parent, new MLabel(t));
				}
				return null;
			}
		});
	}
	
	void editLabel(final MLabel label) {
		final VerticalPanel panel = new VerticalPanel();
		HTML l1 = new HTML("Name:");
		panel.add(l1);
		final TextBox textBox = new TextBox();
		textBox.setText(label.getName());
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
						label.setName(textBox.getText());
						_localAgent.insertOrUpdate(label);
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
                                    MBlob blob = new MBlob(_agentUid, file.getName());
                                    blob.setDataInBase64(base64);
                                    label.setIcon(blob.getRef());
                                    _localAgent.insertOrUpdate(blob, label);
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

    void addPhone(final MNode parent) {
        DialogHelper.showSingleLineTextPrompt("Enter the Phone #:", "", "200px 20px", new Function1<String,Void>() {
            public Void apply(String t) {
                if ( t != null && t.trim().length() > 0 ) {
                	_localAgent.insertTextNode(parent, t);
                }
                return null;
            }
        });
    }

    void addPhoto(final MNode parent) {
        final VerticalPanel photoLoader = new VerticalPanel();
        final FileUploadExt fileUploadExt = new FileUploadExt();
        photoLoader.add(fileUploadExt);
        DialogHelper.showWidgetPrompt("Load Photo", photoLoader, "200px 200px", new Function1<VerticalPanel,Void>() {
            public Void apply(VerticalPanel p) {
                if ( p != null ) {
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
									MImage image = new MImage();
                                    MBlob blob = new MBlob(_agentUid, file.getName());
                                    blob.setDataInBase64(base64);
                                    image.setBlob(blob);
                                    MLink link = new MLink(parent, image);
                                    // order is important here (create links last)
                                    _localAgent.insertOrUpdate(blob, image, link);
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
	
	void addNode(final MNode parent, String prompt, String size, final Function1<String,MNode> instantiator) {
		DialogHelper.showTextPrompt(prompt, "", size, new Function1<String,Void>() {
			public Void apply(String s) {
				if ( s != null && s.trim().length() > 0 ) {
					MNode child = instantiator.apply(s);
					_localAgent.insertChild(parent, child);					
				}
				return null;
			}
		});		
	}

	void addTextMessage(final MNode parent) {
		DialogHelper.showTextPrompt("Enter the message text.", "", "300px 200px", new Function1<String,Void>() {
			public Void apply(String t) {
				if ( t != null && t.trim().length() > 0 ) {
					_localAgent.insertTextNode(parent, t);					
				}
				return null;
			}
		});
	}

	void addAddress(final MNode parent) {
		DialogHelper.showTextPrompt("Enter the info.", "", "300px 200px", new Function1<String,Void>() {
			public Void apply(String t) {
				if ( t != null && t.trim().length() > 0 ) {
					_localAgent.insertTextNode(parent, t);					
				}
				return null;
			}
		});
	}
	
	public Tree getTree() {
		return _tree;
	}
	
	public void addRootLabelsForAgent(Uid agentUid) {
		_localAgent.getAgentServices().fetch(agentUid, new Function1<MAgent, Void>() {
			@Override
			public Void apply(MAgent agent) {
				addChildren(agent, null);
				return null;
			}
		});
	}

	MLabel getUserObject(TreeItem ti) {
		return (MLabel) ti.getUserObject();		
	}
	
	void addChildren(final biosim.client.messages.model.MNode parent, final TreeItem parentTi) {
		parent.getChildren(new AsyncCallback<Iterable<MNode>>() {
			@Override
			public Void apply(Iterable<MNode> nodes) {
				for ( MNode node : nodes ) {
					if ( node instanceof MLabel) {
						addChild((MLabel)node, parentTi);
					}
				}
				return null;
			}
		});
	}
	
	void addChild(MLabel label, TreeItem parentTi) {
		TreeItem ti = createTreeItem(parentTi, label);
		addChildren(label, ti);
	}
	
}
