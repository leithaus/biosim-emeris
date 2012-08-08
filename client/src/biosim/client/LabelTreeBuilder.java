package biosim.client;

import java.util.List;

import m3.fj.F1;
import m3.gwt.lang.Function0;
import m3.gwt.lang.Function1;
import m3.gwt.lang.ListX;

import org.vectomatic.file.File;
import org.vectomatic.file.FileList;
import org.vectomatic.file.FileReader;
import org.vectomatic.file.FileUploadExt;
import org.vectomatic.file.events.LoadEndEvent;
import org.vectomatic.file.events.LoadEndHandler;

import biosim.client.eventlist.ListEvent;
import biosim.client.eventlist.ListListener;
import biosim.client.messages.model.AgentServices;
import biosim.client.messages.model.LocalAgent;
import biosim.client.messages.model.MAgent;
import biosim.client.messages.model.MBlob;
import biosim.client.messages.model.MConnection;
import biosim.client.messages.model.MIconNode;
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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;


public class LabelTreeBuilder {

	final String DUMMY_TREE_NODE = "DUMMY_TREE_NODE";

	final Tree _tree = new Tree();

	final Uid _agentUid;
	final DndController _dndController;
	final Function0<LocalAgent> _localAgentGetter;
	final BiosimWebSocket _socket;
	
	final F1<MNode,String> _labelProvider = new F1<MNode, String>() {
		@Override
		public String f(MNode node) {
			if ( node instanceof MConnection ) {
				return node.getAgentServices().getAgentUid().getValue();
			} else {
				return node.toHtmlString();
			}
		}
	};
	
	LabelTreeBuilder(
			Uid agentUid, 
			DndController dndController, 
			Function0<LocalAgent> localAgentGetter,
			BiosimWebSocket socket
	) {
		_agentUid = agentUid;
		_dndController = dndController;
		_localAgentGetter = localAgentGetter;
		_socket = socket;

		NodeContainer.get().nodes.addListener(new ListListener<MNode>() {
			@Override
			public void event(ListEvent<MNode> event) {
				
				if (event.getElement() instanceof MLabel) {
//					GWT.log("Label " + event.getType().toString());
					MIconNode node = (MIconNode)event.getElement();

					switch(event.getType()) {
					case Added:
						onAddedLabel(node);
						break;
					case Changed:
						onChangedLabel(node);
						break;
					case Removed:
						onRemovedLabel(node);
						break;
					}
				} else if (event.getElement() instanceof MLink) {
//					GWT.log("Link " + event.getType().toString());
					MLink link = (MLink)event.getElement();

					switch(event.getType()) {
					case Added:
						onAddedLink(link);
						break;
					case Removed:
						onRemovedLink(link);
						break;
					}
				}
			}
		});
		
		_tree.addOpenHandler(new OpenHandler<TreeItem>() {
			@Override
			public void onOpen(OpenEvent<TreeItem> event) {
				final TreeItem ti = event.getTarget();
				if (hasDummyNode(ti)) {					
					// Close the item immediately
					ti.setState(false, false);

					// Add the children of the node
					MIconNode node = getUserObject(ti);
					node.getChildLabels(new AsyncCallback<Iterable<MLabel>>() {
						@Override
						public Void apply(Iterable<MLabel> labels) {
							for (MLabel l : labels) {
								addChild(l, ti);
							}
							
							// Remove the temporary item when we finish loading
							ti.getChild(0).remove();

							// Reopen the item
							ti.setState(true, false);

							return null;
						}
					});
				}
			}
		});
		
		addRootLabelsForAgent(_agentUid);
	}
	
	private void _treeItemsFromUid(TreeItem ti, Uid uid, List<TreeItem> l) {
		MIconNode node = getUserObject(ti);
		if (node != null && node.getUid().equals(uid)) {
			l.add(ti);
		} else {
			for (int j = 0; j < ti.getChildCount(); j++) {
				_treeItemsFromUid(ti.getChild(j), uid, l);
			}
		}
	}
	List<TreeItem> treeItemsFromUid(Uid uid) {
		List<TreeItem> ret = ListX.create();
		for (int i = 0; i < _tree.getItemCount(); i += 1) {
			_treeItemsFromUid(_tree.getItem(i), uid, ret);
		}
		return ret;
	}

	void onAddedLabel(final MIconNode node) {
		node.getParentLabels(new AsyncCallback<Iterable<MLabel>>() {
			@Override
			public Void apply(Iterable<MLabel> labels) {
				for (MLabel l : labels) {
					List<TreeItem> treeItems = treeItemsFromUid(l.getUid());
					for (TreeItem treeItem : treeItems) {
						treeItem.setUserObject(l);
						updateTreeItem(treeItem);
						addChildIfNecessary(treeItem, node);
					}
				}
				return null;
			}
		});
		updateParentNodes(node);
	}
	
	void onChangedLabel(MIconNode node) {
		List<TreeItem> treeItems = treeItemsFromUid(node.getUid());
		for (TreeItem ti : treeItems) {
			ti.setUserObject(node);
			updateTreeItem(ti);
		}
	}

	void onRemovedLabel(final MIconNode node) {
		node.getParentLabels(new AsyncCallback<Iterable<MLabel>>() {
			@Override
			public Void apply(Iterable<MLabel> labels) {
				for (MLabel l : labels) {
					List<TreeItem> treeItems = treeItemsFromUid(l.getUid());
					for (TreeItem ti : treeItems) {
						removeChildWithUid(ti, node.getUid());
					}
				}
				return null;
			}
		});
		updateParentNodes(node);
	}
	
	void updateParentNodes(MIconNode node) {
		node.getParentLabels(new AsyncCallback<Iterable<MLabel>>() {
			@Override
			public Void apply(Iterable<MLabel> labels) {
				for (MLabel l : labels) {
					List<TreeItem> treeItems = treeItemsFromUid(l.getUid());
					for (TreeItem treeItem : treeItems) {
						treeItem.setUserObject(l);
						updateTreeItem(treeItem);
						addChildren(l, treeItem);
					}
				}
				return null;
			}
		});
	}
	
	void removeChildWithUid(TreeItem ti, Uid uid) {
		for (int i = 0; i < ti.getChildCount(); i += 1) {
			MIconNode node = getUserObject(ti.getChild(i));
			if (node !=  null && node.getUid().equals(uid)) {
				ti.removeItem(ti.getChild(i));
				break;
			}
		}
	}
	
	boolean hasChildWithUid(TreeItem ti, Uid uid) {
		for (int i = 0; i < ti.getChildCount(); i += 1) {
			MIconNode node = getUserObject(ti.getChild(i));
			if (node !=  null && node.getUid().equals(uid)) {
				return true;
			}
		}
		
		return false;
	}

	void onAddedLink(MLink link) {
		// Get a tree item associated with the from node
		List<TreeItem> treeItems = treeItemsFromUid(link.getFrom());
		for (TreeItem ti : treeItems) {
			final TreeItem ti_f = ti;
			link.linkTo(new Function1<MNode, Void>() {
				@Override
				public Void apply(MNode node) {
					if (node instanceof MLabel) {
						MLabel label = (MLabel) node;
						addChildIfNecessary(ti_f, label);
					}
					return null;
				}
			});
		}
	}

	void onRemovedLink(MLink link) {
		// Get a tree item associated with the from node
		List<TreeItem> treeItems = treeItemsFromUid(link.getFrom());
		for (TreeItem ti : treeItems) {
			removeChildWithUid(ti, link.getTo());
		}
	}

	TreeItem createTreeItem(TreeItem parent, MIconNode node) {
		TreeItem ti = new TreeItem();
		if ( parent != null ) {
			parent.addItem(ti);
		} else {
			_tree.addItem(ti);
		}
		ti.setUserObject(node);
		updateTreeItem(ti);
		return ti;
	}
	
	boolean hasDummyNode(TreeItem ti) {
		return (ti.getChildCount() == 1 && ti.getChild(0).getText().equals(DUMMY_TREE_NODE));
	}
	
	LocalAgent getLocalAgent() {
		return _localAgentGetter.apply();
	}
	
    void updateTreeItem(final TreeItem ti) {
		final MIconNode node = getUserObject(ti);
		final NodeWidgetBuilder nwbuilder = new NodeWidgetBuilder(node, _dndController, DndType.Label, _labelProvider, _localAgentGetter); 
		final FlowPanel w = nwbuilder.getFlowPanel();
		ti.setWidget(w);

		// Make sure that the open/close button is set properly
		node.getChildLabels(new AsyncCallback<Iterable<MLabel>>() {
			@Override
			public Void apply(Iterable<MLabel> labels) {
				// If there are no children in the model, remove them from the tree
				if (!labels.iterator().hasNext()) {
					ti.removeItems();					
				}
				return null;
			}				
		});
	}

    void addPhone(final MNode parent) {
        DialogHelper.showSingleLineTextPrompt("Enter the Phone #:", "", "200px 20px", new Function1<String,Void>() {
            public Void apply(String t) {
                if ( t != null && t.trim().length() > 0 ) {
                	getLocalAgent().insertTextNode(parent, t);
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
                                    getLocalAgent().insertOrUpdate(blob, image, link);
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
					getLocalAgent().insertChild(parent, child);					
				}
				return null;
			}
		});		
	}

	void addTextMessage(final MNode parent) {
		DialogHelper.showTextPrompt("Enter the message text.", "", "300px 200px", new Function1<String,Void>() {
			public Void apply(String t) {
				if ( t != null && t.trim().length() > 0 ) {
					getLocalAgent().insertTextNode(parent, t);					
				}
				return null;
			}
		});
	}

	void addAddress(final MNode parent) {
		DialogHelper.showTextPrompt("Enter the info.", "", "300px 200px", new Function1<String,Void>() {
			public Void apply(String t) {
				if ( t != null && t.trim().length() > 0 ) {
					getLocalAgent().insertTextNode(parent, t);					
				}
				return null;
			}
		});
	}
	
	public Tree getTree() {
		return _tree;
	}
	
	public void addRootLabelsForAgent(Uid agentUid) {
		getLocalAgent().getAgentServices().fetch(agentUid, new Function1<MAgent, Void>() {
			@Override
			public Void apply(MAgent agent) {
				addChildren(agent, null);
				return null;
			}
		});
	}
	
	public void addRootLabelsForConnection(MConnection connection) {
		AgentServices agentServices = getLocalAgent().getAgentServices(connection);
		agentServices.getRemoteConnection(new Function1<MConnection, Void>() {
			@Override
			public Void apply(MConnection conn) {
				addChild(conn, null);
				return null;
			}
		});		
	}

	MIconNode getUserObject(TreeItem ti) {
		return (MIconNode) ti.getUserObject();		
	}
	
	void addChildren(final biosim.client.messages.model.MNode parent, final TreeItem parentTi) {
		parent.getChildren(new AsyncCallback<Iterable<MNode>>() {
			@Override
			public Void apply(Iterable<MNode> nodes) {
				if (parentTi == null) {
					for (MNode node : nodes) {
						if (node instanceof MIconNode) {
							addChild((MLabel)node, parentTi);
						}
					}
				} else {
					if (nodes.iterator().hasNext()) {
						if (parentTi.getChildCount() == 0) {
							parentTi.addItem(DUMMY_TREE_NODE);
						}
					} else {
						if (parentTi.getChildCount() > 0) {
							parentTi.removeItems();
						}
					}
				}
				return null;
			}
		});
	}
	
	void addChild(MIconNode node, TreeItem parentTi) {
		TreeItem ti = createTreeItem(parentTi, node);
		addChildren(node, ti);
	}
	
	void addChildIfNecessary(TreeItem treeItem, MIconNode node) {
		if (!hasDummyNode(treeItem)) {
			if (!hasChildWithUid(treeItem, node.getUid())) {
				addChild(node, treeItem);
			}
		}

	}
}
