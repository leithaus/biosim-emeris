package m3.gwt.websocket;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;

public class WebSocket {
	
    public String _url;
    public WebSocketHandler _wshndlr;
    WsImpl _impl;
	
    public WebSocket(String url, WebSocketHandler wshndlr) {
	GWT.log("ws connect to -- " + url);
	_url = url;
	_wshndlr = wshndlr;
	_impl = WsImpl.create(url, this);
    }
	
    public void send(String message) {
	_impl.send(message);
    }

    public void onClose() {
	_wshndlr.onClose( this );
    }
	
    public void onOpen(){
	_wshndlr.onOpen( this );
    }
	
    public void close() {
	_impl.close();
    }
	
    public void onError(String msg){
	_wshndlr.onError( this, msg );
    }
	
    public void onMessage(String rawMsg) {
	_wshndlr.onMessage( this, rawMsg );
    }
	
    static class WsImpl extends JavaScriptObject {
		
	/**
	 * Creates an WebSocket object.
	 * 
	 * @return the created object
	 */
		static native WsImpl create(String url, WebSocket webSocket) /*-{
		    var ws = null;
			if ($wnd.MozWebSocket) {
				ws = new MozWebSocket(url);
			} else {
				ws = new WebSocket(url);
			}
			ws.onopen = $entry(function() {
				webSocket.@m3.gwt.websocket.WebSocket::onOpen()();
			});
			ws.onmessage = $entry(function(event) {
				webSocket.@m3.gwt.websocket.WebSocket::onMessage(Ljava/lang/String;)(event.data);
			});
			ws.onclose = $entry(function() {
				webSocket.@m3.gwt.websocket.WebSocket::onClose()();
			});
		    ws.onerror = $entry(function() {
				webSocket.@m3.gwt.websocket.WebSocket::onError(Ljava/lang/String;)(event.data);
			});
			return ws;
		}-*/;

		protected WsImpl() {
		}
		
		final native int getReadyState() /*-{
			return this.readyState;
		}-*/;
		
		final native int getBufferedAmount() /*-{
			return this.bufferedAmount;
		}-*/;

		final native void send(String data) /*-{
			this.send(data);
		}-*/;
		
		final native void close() /*-{
			this.close();
		}-*/;
	}
}
