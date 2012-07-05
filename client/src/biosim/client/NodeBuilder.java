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
import biosim.client.model.Address;
import biosim.client.model.Blob;
import biosim.client.model.Connection;
import biosim.client.model.DataSet;
import biosim.client.model.Image;
import biosim.client.model.Label;
import biosim.client.model.Link;
import biosim.client.model.Node;
import biosim.client.model.Phone;
import biosim.client.model.TextMessage;
import biosim.client.utils.Base64;
import biosim.client.utils.DialogHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.VerticalPanel;

public class NodeBuilder {
    
    final PopupMenu _popup = new PopupMenu();
    {
    	_popup.addOption("Add Info", new Function0<Void>() {
            @Override
            public Void apply() {
                addAddress();
                return null;
            }
        });
    	_popup.addOption("Add Label", new Function0<Void>() {
            @Override
            public Void apply() {
                addLeaf();
                return null;
            }
        });
    	_popup.addOption("Add Message", new Function0<Void>() {
            @Override
            public Void apply() {
                addTextMessage();
                return null;
            }
        });
    	_popup.addOption("Add Photo", new Function0<Void>() {
            @Override
            public Void apply() {
                addPhoto();
                return null;
            }
        });
    }

    final Iterable<Label> _labels;
    final Iterable<Connection> _people;

    final DatabaseAccessLayer _databaseAccessLayer = Biosim.get().getDatabaseAccessLayer();

    public NodeBuilder(Iterable<Label> labels, Iterable<Connection> people) {
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
        for ( Connection cnxn : _people ) {
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
                                    DataSet dataSet = Biosim.get().getDatabaseAccessLayer().getDataSet();
									Image image = new Image(dataSet);
                                    Blob blob = new Blob(dataSet, Biosim.get().getAgentUid(), file.getName());
                                    blob.setDataInBase64(base64);
                                    image.setBlob(blob);
                                    _databaseAccessLayer.addNode(blob);
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
}
