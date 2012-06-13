package biosim.client.utils;

import m3.gwt.lang.Function1;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class DialogHelper {

	public static void showTextPrompt(String prompt, String initialText, String size, final Function1<String,Void> handler) {
	    // Add some text to the top of the dialog
	    final TextArea widget = new TextArea();
	    widget.setText(initialText);
	    showWidgetPrompt(prompt, widget, size, new Function1<TextArea, Void>() {
	    	@Override
	    	public Void apply(TextArea t) {
	    		if ( t != null ) {
	    			handler.apply(t.getText());
	    		}
	    		return null;
	    	}
		});
	}
	
	public static void applySize(Widget w, String size) {
		if ( size != null ) {
			String[] parts = size.split(" ");
			w.setSize(parts[0], parts[1]);
		}
	}
	
	public static <T extends Widget> void showWidgetPrompt(String prompt, final T widget, String size, final Function1<T,Void> handler) {
		final DialogBox dialogBox = new DialogBox();
	    dialogBox.ensureDebugId("cwDialogBox");
	    dialogBox.setText(prompt);
	    dialogBox.setGlassEnabled(true);
	    dialogBox.setAnimationEnabled(true);

	    // Create a table to layout the content
	    VerticalPanel dialogContents = new VerticalPanel();
	    dialogContents.setSpacing(4);
	    dialogBox.setWidget(dialogContents);

	    dialogContents.add(widget);
	    dialogContents.setCellHorizontalAlignment(widget, HasHorizontalAlignment.ALIGN_CENTER);

	    HorizontalPanel buttons = new HorizontalPanel();
	    Button okay = new Button("Okay");
	    okay.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent e) {
				handler.apply(widget);
				dialogBox.hide();
			}
		});
	    Button cancel = new Button("Cancel");
	    cancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent e) {
				handler.apply(null);
				dialogBox.hide();
			}
		});

	    buttons.add(cancel);
	    buttons.add(okay);
	    
	    dialogContents.add(buttons);
	    dialogContents.setCellHorizontalAlignment(buttons, HasHorizontalAlignment.ALIGN_RIGHT);
	    
	    applySize(widget, size);
	    
	    dialogBox.center();
	    dialogBox.show();
	    
	    if ( widget instanceof TextBoxBase ) {
	    	((TextBoxBase)widget).setFocus(true);
	    }
	    
	}

}
