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
import biosim.client.messages.model.LocalAgent;
import biosim.client.messages.model.MBlob;
import biosim.client.messages.model.MConnection;
import biosim.client.messages.model.MImage;
import biosim.client.messages.model.MLabel;
import biosim.client.messages.model.MLink;
import biosim.client.messages.model.MNode;
import biosim.client.messages.model.MText;
import biosim.client.utils.Base64;
import biosim.client.utils.DialogHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.VerticalPanel;

public class NodeBuilder {
    
    final PopupMenu _popup = new PopupMenu();
    {
    	_popup.addOption("Add Label...", new Function0<Void>() {
            @Override
            public Void apply() {
                addLeaf();
                return null;
            }
        });
    	_popup.addOption("Add Info/Text...", new Function0<Void>() {
            @Override
            public Void apply() {
                addText();
                return null;
            }
        });
    	_popup.addOption("Add Photo...", new Function0<Void>() {
            @Override
            public Void apply() {
                addPhoto();
                return null;
            }
        });
    }

    final Iterable<MLabel> _labels;
    final Iterable<MConnection> _connections;

    public NodeBuilder(Iterable<MLabel> labels, Iterable<MConnection> people) {
	    _labels = labels;
	    _connections = people;
	}
	
	void addLeaf() {
		DialogHelper.showTextPrompt("Enter the Name of child label to add.", "", "200px 20px", new Function1<String,Void>() {
			public Void apply(String t) {
				if ( t != null && t.trim().length() > 0 ) {
				    String labelName = t.trim();
				    GWT.log("set a default icon", new Throwable());
				    MLabel label = new MLabel();
				    label.setName(labelName);
				    insert(label);
				}
				return null;
			}
		});
	}

	void insert(MNode node) {
		LocalAgent la = Biosim.get().getLocalAgent();
        la.insertOrUpdate(node);
        for ( MLabel parent : _labels ) {
            MLink link = new MLink(parent, node);                     
            la.insertOrUpdate(link);
        }
        for ( MConnection cnxn : _connections ) {
            la.insertOrUpdate(new MLink(cnxn, node));
        }
	}
	
    void addPhone(final MNode node) {
        DialogHelper.showTextPrompt("Enter the Phone #.", "", "200px 20px", new Function1<String,Void>() {
            public Void apply(String t) {
                if ( t != null && t.trim().length() > 0 ) {
                    insert(new MText(t.trim()));
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
									MImage image = new MImage();
                                    MBlob blob = new MBlob(Biosim.get().getAgentUid(), file.getName());
                                    blob.setDataInBase64(base64);
                                    image.setBlob(blob);
                                    Biosim.get().getLocalAgent().insertOrUpdate(blob);
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
	
//	void addNode(final Node parent, String prompt, String size, final Function1<String,Node> instantiator) {
//		DialogHelper.showTextPrompt(prompt, "", size, new Function1<String,Void>() {
//			public Void apply(String s) {
//				if ( s != null && s.trim().length() > 0 ) {
//					Node node = instantiator.apply(s);
//					Biosim.get().getDatabaseAccessLayer().addNode(node, parent);					
//				}
//				return null;
//			}
//		});		
//	}

//	void addTextMessage() {
//		DialogHelper.showTextPrompt("Enter the message text.", "", "300px 200px", new Function1<String,Void>() {
//			public Void apply(String t) {
//				if ( t != null && t.trim().length() > 0 ) {
//					insert(new MMessage(getDataSet(), t.trim()));
//				}
//				return null;
//			}
//		});
//	}

	void addText() {
		DialogHelper.showTextPrompt("Enter the text.", "", "300px 200px", new Function1<String,Void>() {
			public Void apply(String t) {
				if ( t != null && t.trim().length() > 0 ) {
					insert(new MText(t.trim()));
				}
				return null;
			}
		});
	}
	
	public void showPopupMenu(ClickEvent event) {
	    _popup.show(event);
	}
}
