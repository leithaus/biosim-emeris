package biosim.client.eventlist.ui;

import m3.gwt.lang.Function0;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class PopupMenu {
    DecoratedPopupPanel _popup = new DecoratedPopupPanel(true);
    VerticalPanel _panel = new VerticalPanel();
    {
        _popup.ensureDebugId("cwBasicPopup-simplePopup");
        _popup.setWidth("150px");
        _popup.setWidget(_panel);
    }
    
    public PopupMenu() {
    }
    

    public void show(ClickEvent event) {
        // Reposition the popup relative to the button
        Widget source = (Widget) event.getSource();
        int left = source.getAbsoluteLeft() + 10;
        int top = source.getAbsoluteTop() + 10;
        _popup.setPopupPosition(left, top);
        
        // Show the popup
        _popup.show();
    }
    
    public void addOption(String name, final Function0<Void> handler) {
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

