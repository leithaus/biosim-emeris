package biosim.client;

import java.util.List;

import m3.gwt.lang.Function1;
import m3.gwt.lang.HttpHelper;
import m3.gwt.lang.ListX;
import m3.gwt.lang.LogTool;
import biosim.client.messages.model.Uid;
import biosim.client.utils.DialogHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AgentManagerPanel {
	
	VerticalPanel _mainPanel = new VerticalPanel();
	
	SimplePanel _agentCrudPanel = new SimplePanel();
	
	AgentManagerPanel() {
		
		HorizontalPanel formFieldsPanel = new HorizontalPanel();
		final FormPanel form = new FormPanel();
	    form.setAction(getUrl("/api/loadMultiAgentDataSet"));
	    form.setWidget(formFieldsPanel);
	    form.setEncoding(FormPanel.ENCODING_MULTIPART);
	    form.setMethod(FormPanel.METHOD_POST);
	    
	    // Create a FileUpload widget.
	    FileUpload upload = new FileUpload();
	    upload.setName("dataset");
	    formFieldsPanel.add(upload);

	    // Add a 'submit' button.
	    formFieldsPanel.add(new Button("Load Multi Agent Data Set", new ClickHandler() {
	      public void onClick(ClickEvent event) {
	        form.submit();
	      }
	    }));
	    form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
	      public void onSubmitComplete(SubmitCompleteEvent event) {
	        DialogHelper.alert("Multi Agent Data Set has been loaded");
	      }
	    });
	    
	    form.setWidget(formFieldsPanel);
	    
	    _mainPanel.add(form);
	    _mainPanel.add(
	    		new Anchor(
	    				"Download Multi Agent Data Set"
	    				, getUrl("/api/dumpMultiAgentDataSet")
	    		)
	    );

	    _mainPanel.add(_agentCrudPanel);
	    
	    {
	    	List<String> l = ListX.create();
	    	setAgentInfo(l);
	    }
	    
	    loadAgentInfo();
	    
	}
	
	void loadAgentInfo() {
		HttpHelper.doGet("/api/agentInfo", new Function1<Response, Void>() {
			@Override
			public Void apply(Response response) {
				String responseJson = response.getText();
				try {
					JSONArray ja = JSONParser.parseLenient(responseJson).isArray();
					List<String> agents = ListX.create();
					for ( int i=0 ; i<ja.size() ; i++ ) {
						agents.add(ja.get(i).isString().stringValue());
					}
					setAgentInfo(agents);
				} catch ( Exception e ) {
					LogTool.warn("invalid response for agentInfo\n" + responseJson, e);
				}
				return null;
			}
		});
	}
	
	String getUrl(String href) {
		if ( GWT.isProdMode() ) {
			return href;
		} else {
			String sl;
			if ( href.startsWith("/") ) {
				sl = "";
			} else {
				sl = "/";
			}
			return "http://localhost:8080" + sl + href;
		}
	}
	
	public VerticalPanel getPanel() {
		return _mainPanel;
	}
	
	public void setAgentInfo(List<String> agents) {
		Grid grid = new Grid(agents.size()+1, 2);
		grid.setWidget(0, 0, new Label("Agent Name"));
		for ( int i = 0 ; i < agents.size() ; i++ ) {
			final String agentName = agents.get(i);
			final Uid agentUid = new Uid(agentName);
			int row = i+1;
			grid.setWidget(row, 0, new Label(agentName));
			
			HorizontalPanel buttonPanel = new HorizontalPanel();
			grid.setWidget(row, 1, buttonPanel);
			
			Button dump = new Button("dump");
			buttonPanel.add(dump);
			dump.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					String url = getUrl("/api/dumpDataSet?agentUid=" + agentUid.asString());
					Window.open(url, "download", "");
				}
			});
			
			Button delete = new Button("delete");
			buttonPanel.add(delete);
			delete.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					DialogHelper.confirm("Are you sure you want to delete " + agentName, new Function1<String,Void>() {
						public Void apply(String s) {
							deleteAgent(agentUid);
							return null;
						}
					});
				}
			});
			
			Button ui = new Button("ui");
			buttonPanel.add(ui);
			ui.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					String url;
					if ( GWT.isProdMode() ) {
						url = getUrl("Biosim.html?agentUid=" + agentUid.asString());
					} else {
						url = "http://127.0.0.1:8888/Biosim.html?gwt.codesvr=127.0.0.1:9997&agentUid=" + agentUid.asString();
					}
					Window.open(url, agentName, "");
				}
			});
			
		}
		
		VerticalPanel vert = new VerticalPanel();
		
		HorizontalPanel createAgent = new HorizontalPanel();
		
		createAgent.add(new Label("Name:"));
		final TextBox agentName = new TextBox();
		createAgent.add(agentName);
		Button addAgent = new Button("Add Agent");
		createAgent.add(addAgent);
		addAgent.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String name = agentName.getText().trim();
				if ( name.length() > 0 ) {
					insertAgent(new Uid(name));
				}
			}
		});
		
		vert.add(grid);
		vert.add(new Label("Add Agent"));
		vert.add(createAgent);
		
		_agentCrudPanel.setWidget(vert);
		
	}

	void insertAgent(Uid agentUid) {
		HttpHelper.doGet("/api/insertAgent?agentUid=" + agentUid.asString(), new Function1<Response, Void>() {
			@Override
			public Void apply(Response response) {
				loadAgentInfo();
				return null;
			}
		});		
	}

	void deleteAgent(Uid agentUid) {
		HttpHelper.doGet("/api/deleteAgent?agentUid=" + agentUid.asString(), new Function1<Response, Void>() {
			@Override
			public Void apply(Response response) {
				loadAgentInfo();
				return null;
			}
		});
	}

	protected void loadAgentDataSet(final Uid uid) {
		HorizontalPanel formFieldsPanel = new HorizontalPanel();
		final FormPanel form = new FormPanel();
	    form.setAction(getUrl("/api/loadDataSet?agentUid" + uid.asString()));
	    form.setWidget(formFieldsPanel);
	    form.setEncoding(FormPanel.ENCODING_MULTIPART);
	    form.setMethod(FormPanel.METHOD_POST);
	    
	    // Create a FileUpload widget.
	    FileUpload upload = new FileUpload();
	    upload.setName("dataset");
	    formFieldsPanel.add(upload);

	    // Add a 'submit' button.
	    formFieldsPanel.add(new Button("Load Data Set", new ClickHandler() {
	      public void onClick(ClickEvent event) {
	        form.submit();
	      }
	    }));
	    form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
	      public void onSubmitComplete(SubmitCompleteEvent event) {
	        DialogHelper.alert("Data Set for Agent " + uid.asString() + " has been loaded");
	      }
	    });
	    
	    form.setWidget(formFieldsPanel);
		
	}

}
