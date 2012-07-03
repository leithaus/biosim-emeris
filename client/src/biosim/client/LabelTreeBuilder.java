package biosim.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import m3.gwt.lang.Function0;
import m3.gwt.lang.Function1;

import org.vectomatic.file.File;
import org.vectomatic.file.FileList;
import org.vectomatic.file.FileReader;
import org.vectomatic.file.FileUploadExt;
import org.vectomatic.file.events.LoadEndEvent;
import org.vectomatic.file.events.LoadEndHandler;

import biosim.client.eventlist.FineGrainedListListener;
import biosim.client.eventlist.ListEvent;
import biosim.client.eventlist.ObservableList;
import biosim.client.model.Alias;
import biosim.client.model.Blob;
import biosim.client.model.DataSet;
import biosim.client.model.Image;
import biosim.client.model.Label;
import biosim.client.model.Link;
import biosim.client.model.Node;
import biosim.client.model.NodeVisitor;
import biosim.client.model.Person;
import biosim.client.model.Uid;
import biosim.client.ui.NodeWidgetBuilder;
import biosim.client.ui.dnd.DndType;
import biosim.client.utils.Base64;
import biosim.client.utils.DialogHelper;
import biosim.client.utils.GeneratorMap;
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
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

@SuppressWarnings("serial")
public class LabelTreeBuilder {
    
//	final Popup _popup = new Popup();
    
	final ObservableList<Node> _roots;
	final Tree _tree = new Tree();
	final DndController _dndController;
	final Uid _agentUid;
	
	Map<Link,List<TreeItem>> _treeItemsByLink = new GeneratorMap<Link, List<TreeItem>>() {
		public java.util.List<TreeItem> generate(Link k) {
			return new ArrayList<TreeItem>();
		}
	};

	Map<Node,List<TreeItem>> _treeItemsByNode = new GeneratorMap<Node, List<TreeItem>>() {
		public java.util.List<TreeItem> generate(Node k) {
			return new ArrayList<TreeItem>();
		}
	};
	
	LabelTreeBuilder(DataSet dataSet, Uid agentUid, ObservableList<Node> roots, DndController dndController) {
		_roots = roots;
		_agentUid = agentUid;
		_dndController = dndController;
		dataSet.links.addListener(new FineGrainedListListener<Link>() {
			@Override
			public void added(ListEvent<Link> event) {
				Link l = event.getElement();
				if ( 
						l.getToNode() instanceof Label
						&& _treeItemsByNode.containsKey(l.getFromNode())
				) {
					addLink(l);
				}
			}
			@Override
			public void removed(ListEvent<Link> event) {
				removeLink(event.getElement());
			}
		});
		roots.addListener(new FineGrainedListListener<Node>() {
			@Override
			public void added(ListEvent<Node> event) {
				addRootNode(event.getElement());
			}
			@Override
			public void removed(ListEvent<Node> event) {
			}
		});
	}
	
	TreeItem createTreeItem(TreeItem parent, final Node node, final Link link) {
		final NodeWidgetBuilder nwbuilder = new NodeWidgetBuilder(node, _dndController, DndType.Label); 
		final FlowPanel w = nwbuilder.getWidget();
		final TreeItem ti = new TreeItem(w);
		if ( link != null ) {
			_treeItemsByLink.get(link).add(ti);
			ti.setUserObject(link);
		} else {
			ti.setUserObject(node);
		}
		_treeItemsByNode.get(node).add(ti);
		
		if ( parent != null ) {
			parent.addItem(ti);
		}
		
		if ( isEditable(node) ) {
			
			final Button add = new Button("+");
			final Button edit = new Button("~");
			
			w.add(add);
			w.add(edit);
			
			add.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
				    addLeaf(node);
//					_popup.show(node, event);
				}
			});
			edit.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					editLabel(node);
				}
			});
			
			w.addDomHandler(new MouseOutHandler() {
				@Override
				public void onMouseOut(MouseOutEvent event) {
					GqueryUtils.setVisibility(add.getElement(), Visibility.HIDDEN);
					GqueryUtils.setVisibility(edit.getElement(), Visibility.HIDDEN);
//					add.setVisible(false);
				}
			},  MouseOutEvent.getType());
			
			w.addDomHandler(new MouseOverHandler() {
				@Override
				public void onMouseOver(MouseOverEvent event) {
					GqueryUtils.setVisibility(add.getElement(), Visibility.VISIBLE);
					GqueryUtils.setVisibility(edit.getElement(), Visibility.VISIBLE);
//					add.setVisible(true);
				}
			},  MouseOverEvent.getType());
			
//			add.setVisible(false);
			GqueryUtils.setVisibility(add.getElement(), Visibility.HIDDEN);
			GqueryUtils.setVisibility(edit.getElement(), Visibility.HIDDEN);
			
		}
		
		return ti;
	}
	
	void addLeaf(final Node node) {
		DialogHelper.showSingleLineTextPrompt("Enter the name of child label to add:", "", "200px 20px", new Function1<String,Void>() {
			public Void apply(String t) {
				if ( t != null && t.trim().length() > 0 ) {
					Biosim.get().getDatabaseAccessLayer().addChildLabel(node, t);
				}
				return null;
			}
		});
	}
	
	void editLabel(final Node node) {
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
                                    DataSet dataSet = Biosim.get().getDatabaseAccessLayer().getDataSet();
									Image image = new Image(dataSet);
                                    Blob blob = new Blob(dataSet, Biosim.get().getAgentUid(), file.getName());
                                    blob.setDataInBase64(base64);
                                    image.setBlob(blob);
                                    node.setIconRef(blob.getRef());
                                    Biosim.get().getDatabaseAccessLayer().addNode(blob);
                                    Biosim.get().getDatabaseAccessLayer().addNode(image, node);
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

    void addPhone(final Node node) {
        DialogHelper.showSingleLineTextPrompt("Enter the Phone #:", "", "200px 20px", new Function1<String,Void>() {
            public Void apply(String t) {
                if ( t != null && t.trim().length() > 0 ) {
                    Biosim.get().getDatabaseAccessLayer().addPhone(node, t);
                }
                return null;
            }
        });
    }

    void addPhoto(final Node parent) {
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
                                    DataSet dataSet = Biosim.get().getDatabaseAccessLayer().getDataSet();
									Image image = new Image(dataSet);
                                    Blob blob = new Blob(dataSet, Biosim.get().getAgentUid(), file.getName());
                                    blob.setDataInBase64(base64);
                                    image.setBlob(blob);
                                    Biosim.get().getDatabaseAccessLayer().addNode(blob);
                                    Biosim.get().getDatabaseAccessLayer().addNode(image, parent);
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
	
	void addNode(final Node parent, String prompt, String size, final Function1<String,Node> instantiator) {
		DialogHelper.showTextPrompt(prompt, "", size, new Function1<String,Void>() {
			public Void apply(String s) {
				if ( s != null && s.trim().length() > 0 ) {
					Node node = instantiator.apply(s);
					Biosim.get().getDatabaseAccessLayer().addNode(node, parent);					
				}
				return null;
			}
		});		
	}

	void addTextMessage(final Node node) {
		DialogHelper.showTextPrompt("Enter the message text.", "", "300px 200px", new Function1<String,Void>() {
			public Void apply(String t) {
				if ( t != null && t.trim().length() > 0 ) {
					Biosim.get().getDatabaseAccessLayer().addTextMessage(node, t);					
				}
				return null;
			}
		});
	}

	void addAddress(final Node node) {
		DialogHelper.showTextPrompt("Enter the info.", "", "300px 200px", new Function1<String,Void>() {
			public Void apply(String t) {
				if ( t != null && t.trim().length() > 0 ) {
					Biosim.get().getDatabaseAccessLayer().addAddress(node, t);					
				}
				return null;
			}
		});
	}
	
	public Tree getTree() {
		return _tree;
	}
	
	Node getTreeItemRoot(TreeItem ti) {
		TreeItem pr = ti;
		while (pr.getParentItem() != null) {
			pr = pr.getParentItem();
		}
		return (Node) pr.getUserObject();
	}
	
	boolean isEditable(Node n) {
		final boolean[] editable = new boolean[] { false };
		NodeVisitor visitor = new NodeVisitor() {
			@Override
			public void visit(Node node) {
				Person p = null;
				if ( node instanceof Alias ) {
					p = ((Alias)node).getAgentNode();
				}
			    if ( node instanceof Person ) {
			    	p = (Person) node;
			    }
			    if ( p != null && p.getUid().equals(_agentUid) ) {
					editable[0] = true;
			    }
			}
		};
		n.visitAncestry(visitor);
		visitor.visit(n);
		return editable[0];
	}

	public void addLink(Link l) {
		for ( TreeItem p_ti : _treeItemsByNode.get(l.getFromNode()) ) {
			TreeItem ti = createTreeItem(p_ti, l.getToNode(), l);
			ti.getElement().getStyle().setProperty("display", "table-cell");
		}
	}

	public void addRootNode(Node n) {
		TreeItem ti = createTreeItem(null, n, null);
		_tree.addItem(ti);
		addChildren(n, ti);
	}
	
	void addChildren(Node parentNode, TreeItem parentTi) {
		List<Link> outgoingLinks = parentNode.getOutgoingLinks();
		for ( Link l : outgoingLinks ) {
			Node to = l.getToNode();
			if ( to instanceof Label ) {
				TreeItem ti = createTreeItem(parentTi, l.getToNode(), l);
				addChildren(l.getToNode(), ti);
			}
		}
	}

	public void removeLink(Link l) {
		List<TreeItem> nodeList = _treeItemsByNode.get(l.getToNode());
		for ( TreeItem ti : _treeItemsByLink.get(l) ) {
			ti.getParentItem().removeItem(ti);
			nodeList.remove(ti);
		}
		if ( nodeList.isEmpty() ) {
			_treeItemsByNode.remove(l.getToNode());
		}
		_treeItemsByLink.remove(l);
	}
	
	
	class Popup {
		DecoratedPopupPanel _popup = new DecoratedPopupPanel(true);
		VerticalPanel _panel = new VerticalPanel();
		Node _currentNode;
		{
			_popup.ensureDebugId("cwBasicPopup-simplePopup");
			_popup.setWidth("150px");
			_popup.setWidget(_panel);
		}
		Popup() {
			addOption("Add Info", new Function0<Void>() {
				@Override
				public Void apply() {
					addAddress(_currentNode);
					return null;
				}
			});
			addOption("Add Label", new Function0<Void>() {
				@Override
				public Void apply() {
					addLeaf(_currentNode);
					return null;
				}
			});
			addOption("Add Message", new Function0<Void>() {
				@Override
				public Void apply() {
					addTextMessage(_currentNode);
					return null;
				}
			});
//			addOption("Add Need", new Function0<Void>() {
//				@Override
//				public Void apply() {
//					addNode(_currentNode, "Describe the need.", "300px 400px", new Function1<String, Node>() {
//						@Override
//						public Node apply(String s) {
//							return new Need(Biosim.get().getDatabaseAccessLayer().getDataSet(), s);
//						}
//					});
//					return null;
//				}
//			});
//			addOption("Add Offer", new Function0<Void>() {
//				@Override
//				public Void apply() {
//					addNode(_currentNode, "Describe the offer.", "300px 400px", new Function1<String, Node>() {
//						@Override
//						public Node apply(String s) {
//							return new Offer(Biosim.get().getDatabaseAccessLayer().getDataSet(), s);
//						}
//					});
//					return null;
//				}
//			});
//			addOption("Add Phone", new Function0<Void>() {
//				@Override
//				public Void apply() {
//					addPhone(_currentNode);
//					return null;
//				}
//			});
			addOption("Add Photo", new Function0<Void>() {
				@Override
				public Void apply() {
				    addPhoto(_currentNode);
					return null;
				}
			});
		}

		void show(Node node, ClickEvent event) {
			_currentNode = node;
            // Reposition the popup relative to the button
            Widget source = (Widget) event.getSource();
            int left = source.getAbsoluteLeft() + 10;
            int top = source.getAbsoluteTop() + 10;
            _popup.setPopupPosition(left, top);
            // Show the popup
            _popup.show();
		}
		
		void addOption(String name, final Function0<Void> handler) {
			Button b = new Button(name);
			_panel.add(b);
			b.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					_popup.hide();
					handler.apply();
				}
			});
		}
	}
	
}
