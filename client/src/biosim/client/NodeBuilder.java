package biosim.client;

import m3.gwt.lang.Function0;
import m3.gwt.lang.Function1;

import org.vectomatic.file.File;
import org.vectomatic.file.FileList;
import org.vectomatic.file.FileReader;
import org.vectomatic.file.FileUploadExt;
import org.vectomatic.file.events.LoadEndEvent;
import org.vectomatic.file.events.LoadEndHandler;

import biosim.client.model.Address;
import biosim.client.model.DataSet;
import biosim.client.model.Image;
import biosim.client.model.Label;
import biosim.client.model.Link;
import biosim.client.model.Node;
import biosim.client.model.Person;
import biosim.client.model.Phone;
import biosim.client.model.TextMessage;
import biosim.client.utils.Base64;
import biosim.client.utils.DialogHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class NodeBuilder {
    
    final Popup _popup = new Popup();
    final Iterable<Label> _labels;
    final Iterable<Person> _people;

    final DatabaseAccessLayer _databaseAccessLayer = Biosim.get().getDatabaseAccessLayer();

    public NodeBuilder(Iterable<Label> labels, Iterable<Person> people) {
	    _labels = labels;
	    _people = people;
	}

	DataSet getDataSet() {
        return _databaseAccessLayer.getDataSet();
	}
	
	void addLeaf() {
		DialogHelper.showTextPrompt("Enter the Name of child label to add.", "", "200px 20px", new Function1<String,Void>() {
			public Void apply(String t) {
				if ( t != null && t.trim().length() > 0 ) {
				    String labelName = t.trim();
				    insert(new Label(getDataSet(), labelName));
				}
				return null;
			}
		});
	}

	void insert(Node node) {
        _databaseAccessLayer.addNode(node);
        for ( Label parent : _labels ) {
            Link link = parent.link(node);                     
            _databaseAccessLayer.addNode(link);
        }
        for ( Person cnxn : _people ) {
            _databaseAccessLayer.addLink(cnxn, node);
        }
        _databaseAccessLayer.fireRefreshContentPane();
	}
	
    void addPhone(final Node node) {
        DialogHelper.showTextPrompt("Enter the Phone #.", "", "200px 20px", new Function1<String,Void>() {
            public Void apply(String t) {
                if ( t != null && t.trim().length() > 0 ) {
                    insert(new Phone(getDataSet(), t.trim()));
                }
                return null;
            }
        });
    }

    void addPhoto() {
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
                                    Image image = new Image(Biosim.get().getDatabaseAccessLayer().getDataSet());
                                    image.setAgent(Biosim.get().getAgentUid());
                                    image.setDataInBase64(base64);
                                    image.setFilename(file.getName());
                                    insert(image);
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

	void addTextMessage() {
		DialogHelper.showTextPrompt("Enter the message text.", "", "300px 200px", new Function1<String,Void>() {
			public Void apply(String t) {
				if ( t != null && t.trim().length() > 0 ) {
					insert(new TextMessage(getDataSet(), t.trim()));
				}
				return null;
			}
		});
	}

	void addAddress() {
		DialogHelper.showTextPrompt("Enter the info.", "", "300px 200px", new Function1<String,Void>() {
			public Void apply(String t) {
				if ( t != null && t.trim().length() > 0 ) {
				    insert(new Address(getDataSet(), t.trim()));
				}
				return null;
			}
		});
	}
	
	public void showPopupMenu(ClickEvent event) {
	    _popup.show(event);
	}

    class Popup {
        DecoratedPopupPanel _popup = new DecoratedPopupPanel(true);
        VerticalPanel _panel = new VerticalPanel();
        {
            _popup.ensureDebugId("cwBasicPopup-simplePopup");
            _popup.setWidth("150px");
            _popup.setWidget(_panel);
        }
        Popup() {
            addOption("Add Info", new Function0<Void>() {
                @Override
                public Void apply() {
                    addAddress();
                    return null;
                }
            });
            addOption("Add Label", new Function0<Void>() {
                @Override
                public Void apply() {
                    addLeaf();
                    return null;
                }
            });
            addOption("Add Message", new Function0<Void>() {
                @Override
                public Void apply() {
                    addTextMessage();
                    return null;
                }
            });
//          addOption("Add Need", new Function0<Void>() {
//              @Override
//              public Void apply() {
//                  addNode(_currentNode, "Describe the need.", "300px 400px", new Function1<String, Node>() {
//                      @Override
//                      public Node apply(String s) {
//                          return new Need(Biosim.get().getDatabaseAccessLayer().getDataSet(), s);
//                      }
//                  });
//                  return null;
//              }
//          });
//          addOption("Add Offer", new Function0<Void>() {
//              @Override
//              public Void apply() {
//                  addNode(_currentNode, "Describe the offer.", "300px 400px", new Function1<String, Node>() {
//                      @Override
//                      public Node apply(String s) {
//                          return new Offer(Biosim.get().getDatabaseAccessLayer().getDataSet(), s);
//                      }
//                  });
//                  return null;
//              }
//          });
//          addOption("Add Phone", new Function0<Void>() {
//              @Override
//              public Void apply() {
//                  addPhone(_currentNode);
//                  return null;
//              }
//          });
            addOption("Add Photo", new Function0<Void>() {
                @Override
                public Void apply() {
                    addPhoto();
                    return null;
                }
            });
        }

        void show(ClickEvent event) {
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
