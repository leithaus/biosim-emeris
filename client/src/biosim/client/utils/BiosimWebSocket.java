package biosim.client.utils;

import java.util.Map;

import m3.fj.F1;
import m3.gwt.lang.Function1;
import m3.gwt.lang.LogTool;
import m3.gwt.lang.MapX;
import m3.gwt.websocket.SimpleWebSocketFactory;
import m3.gwt.websocket.WebSocket;
import m3.gwt.websocket.WebSocket.IncomingMessage;
import biosim.client.messages.model.Uid;
import biosim.client.messages.protocol.MessageHandler;
import biosim.client.messages.protocol.Request;
import biosim.client.messages.protocol.RequestBody;
import biosim.client.messages.protocol.Response;

import com.google.gwt.core.client.GWT;

public class BiosimWebSocket {

    WebSocket _impl;
    Map<Uid,F1<Object,Object>> _responseHandlers = MapX.create();

    public BiosimWebSocket(String url, final MessageHandler messageHandler, final Function1<Void,Void> onConnect) {
        _impl = new SimpleWebSocketFactory().create(url, new WebSocket.AbstractHandler() {
        	@Override
            public void onError(String msg) {
                LogTool.warn("websocket error " + msg);
            }
            @Override
            public void onOpen() {
                GWT.log("websocket openned");
                messageHandler.connect(BiosimWebSocket.this);
                onConnect.apply(null);
            }
            @Override
            public IncomingMessage deserialize(String jsonMsg) {
                GWT.log("received message " + jsonMsg);
                Response msg = BiosimSerializer.get().fromJson(jsonMsg, Response.class);
                return msg.asIncomingMessage();
            }
            @Override
            public void incomingMessage(IncomingMessage message) {
            	Response response = message.delegate();
            	F1<Object,Object> responseHandler = _responseHandlers.get(response.getRequestUid());
            	if ( responseHandler != null ) {
            		responseHandler.f(response.getResponseBody());
            	} else {
            		messageHandler.process(response);
            	}
            }            
        });
    }

    @SuppressWarnings("unchecked")
	public void send(final RequestBody body, Function1<?,?> responseHandler) {
    	Request request = send(body);
    	_responseHandlers.put(request.getUid(), (Function1<Object,Object>) responseHandler);
    }

    public Request send(final RequestBody body) {
    	final Request request = new Request();
    	request.setUid(Uid.random());
    	request.setRequestBody(body);
		final String jsonMsg = BiosimSerializer.get().toJson(request, request.getClass());
		GWT.log("sending -- " + jsonMsg);
    	_impl.send(new WebSocket.OutgoingMessage() {
			@Override
			public String id() {
				return request.getUid().asString();
			}
			@Override
			public String body() {
				return jsonMsg;
			}
			@Override
			public boolean ack () {
				return false;
			}
		});
    	return request;
    }


}
