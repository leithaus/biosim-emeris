package m3.gwt.websocket;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;

public interface WebSocketHandler {    
    public void onClose( WebSocket ws );
	
    public void onOpen( WebSocket ws );
	    	
    public void onError( WebSocket ws, String msg);
	
    public void onMessage( WebSocket ws, String rawMsg);
}
