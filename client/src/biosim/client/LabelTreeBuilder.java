package biosim.client;

import m3.gwt.lang.Function0;
import m3.gwt.lang.Function1;

import org.vectomatic.file.File;
import org.vectomatic.file.FileList;
import org.vectomatic.file.FileReader;
import org.vectomatic.file.FileUploadExt;
import org.vectomatic.file.events.LoadEndEvent;
import org.vectomatic.file.events.LoadEndHandler;

import biosim.client.eventlist.ui.PopupMenu;
import biosim.client.messages.model.MBlob;
import biosim.client.messages.model.MImage;
import biosim.client.messages.model.MLabel;
import biosim.client.messages.model.MLink;
import biosim.client.messages.model.MNode;
import biosim.client.messages.model.RemoteServices;
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
	final RemoteServices _remoteServices;	
	final BiosimWebSocket _socket;
	
	LabelTreeBuilder(
			Uid agentUid, 
			DndController dndController, 
			RemoteServices remoteServices,
			BiosimWebSocket socket
	) {
		_agentUid = agentUid;
		_dndController = dndController;
		_remoteServices = remoteServices;
		_socket = socket;
		addRootLabelsForAgent(_agentUid);
	}
	
	TreeItem createTreeItem(TreeItem parent, MLabel label) {
		TreeItem ti = new TreeItem();
		if ( parent != null ) {
			parent.addItem(ti);
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
				
		if ( isEditable(label) ) {	
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
					_remoteServices.insertChild(parent, new MLabel(t));
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
						_remoteServices.insertOrUpdate(label);
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
                                    _remoteServices.insertOrUpdate(blob, label);
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
                	_remoteServices.insertTextNode(parent, t);
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
                                    _remoteServices.insertOrUpdate(blob, image, link);
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
					_remoteServices.insertChild(parent, child);					
				}
				return null;
			}
		});		
	}

	void addTextMessage(final MNode parent) {
		DialogHelper.showTextPrompt("Enter the message text.", "", "300px 200px", new Function1<String,Void>() {
			public Void apply(String t) {
				if ( t != null && t.trim().length() > 0 ) {
					_remoteServices.insertTextNode(parent, t);					
				}
				return null;
			}
		});
	}

	void addAddress(final MNode parent) {
		DialogHelper.showTextPrompt("Enter the info.", "", "300px 200px", new Function1<String,Void>() {
			public Void apply(String t) {
				if ( t != null && t.trim().length() > 0 ) {
					_remoteServices.insertTextNode(parent, t);					
				}
				return null;
			}
		});
	}
	
	public Tree getTree() {
		return _tree;
	}
	
	public void addRootLabelsForAgent(Uid agentUid) {
		_remoteServices.fetch(agentUid, new Function1<MLabel,Void>() {
			@Override
			public Void apply(MLabel label) {
				addChildren(label, null);
				return null;
			}
		});
	}

	MLabel getUserObject(TreeItem ti) {
		return (MLabel) ti.getUserObject();		
	}
	
	boolean isEditable(MLabel n) {
		GWT.log("implement me", new Throwable());
		return true;
	}
	
	void addChildren(final biosim.client.messages.model.MLabel parent, final TreeItem parentTi) {
		for ( Uid ch0 : parent.getChildren() ) {
			final Uid ch = ch0;
			_remoteServices.fetch(ch, new Function1<MLabel,Void>() {
				@Override
				public Void apply(MLabel label) {
					TreeItem ti = createTreeItem(parentTi, label);
					if ( parentTi == null ) {
						_tree.addItem(ti);
					}
					addChildren(label, ti);
					return null;
				}
			});
		}	
	}

//	public void addLink(Link l) {
//		for ( TreeItem p_ti : _treeItemsByNode.get(l.getFromNode()) ) {
//			createTreeItem(p_ti, (Label) l.getToNode(), l);
//		}
//	}
//	
//	public void removeLink(Link l) {
//		Set<TreeItem> nodeList = _treeItemsByNode.get(l.getToNode());
//		for ( TreeItem ti : _treeItemsByLink.get(l) ) {
//			ti.getParentItem().removeItem(ti);
//			nodeList.remove(ti);
//		}
//		if ( nodeList.isEmpty() ) {
//			_treeItemsByNode.remove(l.getToNode());
//		}
//		_treeItemsByLink.remove(l);
//	}
	
}
