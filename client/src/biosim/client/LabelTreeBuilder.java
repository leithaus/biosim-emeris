package biosim.client;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import m3.gwt.lang.Function0;
import m3.gwt.lang.Function1;
import m3.gwt.lang.Pair;

import org.vectomatic.file.File;
import org.vectomatic.file.FileList;
import org.vectomatic.file.FileReader;
import org.vectomatic.file.FileUploadExt;
import org.vectomatic.file.events.LoadEndEvent;
import org.vectomatic.file.events.LoadEndHandler;

import biosim.client.eventlist.FineGrainedListListener;
import biosim.client.eventlist.ListEvent;
import biosim.client.eventlist.ObservableList;
import biosim.client.eventlist.ui.PopupMenu;
import biosim.client.model.Agent;
import biosim.client.model.Blob;
import biosim.client.model.DataSet;
import biosim.client.model.Image;
import biosim.client.model.Label;
import biosim.client.model.Link;
import biosim.client.model.Node;
import biosim.client.model.NodeVisitor;
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
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;

@SuppressWarnings("serial")
public class LabelTreeBuilder {
    
	final ObservableList<Label> _roots;
	final Tree _tree = new Tree();
	final DndController _dndController;
	final Uid _agentUid;
	
	Map<Link,Set<TreeItem>> _treeItemsByLink = new GeneratorMap<Link, Set<TreeItem>>() {
		public Set<TreeItem> generate(Link k) {
			return new HashSet<TreeItem>();
		}
	};

	Map<Node,Set<TreeItem>> _treeItemsByNode = new GeneratorMap<Node, Set<TreeItem>>() {
		public Set<TreeItem> generate(Node k) {
			return new HashSet<TreeItem>();
		}
	};
	
	LabelTreeBuilder(DataSet dataSet, Uid agentUid, ObservableList<Label> roots, DndController dndController) {
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
		dataSet.nodes.addListener(new FineGrainedListListener<Node>() {
			@Override
			public void changed(ListEvent<Node> event) {
				Set<TreeItem> l = _treeItemsByNode.get(event.getElement());
				if ( l != null ) {
					for ( TreeItem ti : l ) {
						updateTreeItem(ti);
					}
				}
			}
		});
		roots.addListener(new FineGrainedListListener<Label>() {
			@Override
			public void added(ListEvent<Label> event) {
				addRootNode(event.getElement());
			}
			@Override
			public void removed(ListEvent<Label> event) {
			}
		});
	}
	
	TreeItem createTreeItem(TreeItem parent, Label label, Link link) {
		TreeItem ti = new TreeItem();
		if ( parent != null ) {
			parent.addItem(ti);
		}
		ti.setUserObject(Pair.create(label, link));
		updateTreeItem(ti);
		return ti;
	}
	
    void updateTreeItem(TreeItem ti) {
    	Pair<Label,Link> pair = getUserObject(ti);
		final Label label = pair.getLeft();
		final Link link = pair.getRight();
		final NodeWidgetBuilder nwbuilder = new NodeWidgetBuilder(label, _dndController, DndType.Label); 
		final FlowPanel w = nwbuilder.getFlowPanel();
		final PopupMenu popup = new PopupMenu();
		ti.setWidget(w);
		if ( link != null ) {
			_treeItemsByLink.get(link).add(ti);
		}
		ti.setUserObject(Pair.create(label, link));
		_treeItemsByNode.get(label).add(ti);
				
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
	
	void editLabel(final Label label) {
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
	                    Biosim.get().getDatabaseAccessLayer().addNode(label);
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
                                    Blob blob = new Blob(dataSet, Biosim.get().getAgentUid(), file.getName());
                                    blob.setDataInBase64(base64);
                                    label.setIconRef(blob.getRef());
                                    Biosim.get().getDatabaseAccessLayer().addNode(blob);
                                    Biosim.get().getDatabaseAccessLayer().addNode(label);
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
		Pair<Label,Link> p = getUserObject(ti);
		if ( p.getRight() == null ) {
			return p.getLeft();
		} else {
			return p.getRight();
		}
	}
	
	@SuppressWarnings("unchecked")
	Pair<Label,Link> getUserObject(TreeItem ti) {
		return (Pair<Label,Link>) ti.getUserObject();		
	}
	
	boolean isEditable(Node n) {
		final boolean[] editable = new boolean[] { false };
		NodeVisitor visitor = new NodeVisitor() {
			@Override
			public void visit(Node node) {
			    if (node instanceof Agent) {
			    	Agent a = (Agent) node;
				    if (a.getUid().equals(_agentUid)) {
						editable[0] = true;
				    }
			    }
			}
		};
		n.visitAncestry(visitor);
		visitor.visit(n);
		return editable[0];
	}

	public void addLink(Link l) {
		for ( TreeItem p_ti : _treeItemsByNode.get(l.getFromNode()) ) {
			createTreeItem(p_ti, (Label) l.getToNode(), l);
		}
	}

	public void addRootNode(Label rootLabel) {
		TreeItem ti = createTreeItem(null, rootLabel, null);
		_tree.addItem(ti);
		addChildren(rootLabel, ti);
	}
	
	void addChildren(Label parentLabel, TreeItem parentTi) {
		List<Link> outgoingLinks = parentLabel.getOutgoingLinks();
		for ( Link l : outgoingLinks ) {
			Node to = l.getToNode();
			if ( to instanceof Label ) {
				Label toLabel = (Label)l.getToNode();
				TreeItem ti = createTreeItem(parentTi, toLabel, l);
				addChildren(toLabel, ti);
			}
		}
	}

	public void removeLink(Link l) {
		Set<TreeItem> nodeList = _treeItemsByNode.get(l.getToNode());
		for ( TreeItem ti : _treeItemsByLink.get(l) ) {
			ti.getParentItem().removeItem(ti);
			nodeList.remove(ti);
		}
		if ( nodeList.isEmpty() ) {
			_treeItemsByNode.remove(l.getToNode());
		}
		_treeItemsByLink.remove(l);
	}
}
