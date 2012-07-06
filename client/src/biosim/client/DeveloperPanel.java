package biosim.client;

import biosim.client.utils.DialogHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DeveloperPanel {
	
	VerticalPanel _mainPanel = new VerticalPanel();
	HorizontalPanel _formFieldsPanel = new HorizontalPanel();
	
	DeveloperPanel() {
		
		String agentUid = Biosim.get().getAgentUid().asString();
		
		final FormPanel form = new FormPanel();
	    form.setAction(getUrl("/loadDataSet"));
	    form.setWidget(_formFieldsPanel);
	    form.setEncoding(FormPanel.ENCODING_MULTIPART);
	    form.setMethod(FormPanel.METHOD_POST);
	    _formFieldsPanel.add(new Hidden("agentUid", agentUid));
	    
	    // Create a FileUpload widget.
	    FileUpload upload = new FileUpload();
	    upload.setName("dataset");
	    _formFieldsPanel.add(upload);

	    // Add a 'submit' button.
	    _formFieldsPanel.add(new Button("Load Data Set", new ClickHandler() {
	      public void onClick(ClickEvent event) {
	        form.submit();
	      }
	    }));
	    form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
	      public void onSubmitComplete(SubmitCompleteEvent event) {
	        // When the form submission is successfully completed, this event is
	        // fired. Assuming the service returned a response of type text/html,
	        // we can get the result text here (see the FormPanel documentation for
	        // further explanation).
	        DialogHelper.alert("Data Set has been loaded");
	      }
	    });
	    
	    form.setWidget(_formFieldsPanel);
	    
//	    _mainPanel.add(_formFieldsPanel);
	    _mainPanel.add(form);
	    _mainPanel.add(
	    		new Anchor(
	    				"Download Data Set"
	    				, getUrl("/api/dumpDataSet?agentUid=" + agentUid)
	    		)
	    );
	    
//	    _mainPanel.add(new ObservableListDivWidgetDemo().getPanel());
		
	}
	
	String getUrl(String href) {
		if ( GWT.isProdMode() ) {
			return href;
		} else {
			return "http://localhost:8080" + href;
		}
	}
	
	public VerticalPanel getPanel() {
		return _mainPanel;
	}

}
