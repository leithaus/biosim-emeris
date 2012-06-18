package biosim.client.utils;

import m3.gwt.lang.LogTool;
import m3.gwt.websocket.SimpleWebSocketFactory;
import m3.gwt.websocket.WebSocket;
import m3.gwt.websocket.WebSocket.IncomingMessage;
import biosim.client.messages.Message;
import biosim.client.messages.MessageBody;
import biosim.client.messages.MessageHandler;

import com.google.gwt.core.client.GWT;

public class BiosimWebSocket {

    WebSocket _impl;

    public BiosimWebSocket(String url, final MessageHandler messageHandler) {
        _impl = new SimpleWebSocketFactory().create(url, new WebSocket.AbstractHandler() {
        	@Override
            public void onError(String msg) {
                LogTool.warn("websocket error " + msg);
            }
            @Override
            public void onOpen() {
                GWT.log("websocket openned");
                messageHandler.connect();
            }
            @Override
            public IncomingMessage deserialize(String jsonMsg) {
                GWT.log("received message " + jsonMsg);
                Message msg = BiosimSerializer.get().fromJson(jsonMsg, Message.class);
                return msg.asIncomingMessage();
            }
            @Override
            public void incomingMessage(IncomingMessage message) {
            	Message msg = message.delegate();
                messageHandler.process(msg);
            }            
        });
    }

    public void send(final MessageBody body) {
		final Message msg = body.createMessage();
		final String jsonMsg = BiosimSerializer.get().toJson(msg, Message.class);
		GWT.log("sending -- " + jsonMsg);
    	_impl.send(new WebSocket.OutgoingMessage() {
			@Override
			public String id() {
				return msg.getUid().asString();
			}
			@Override
			public String body() {
				return jsonMsg;
			}
			@Override
			public boolean ack () {
				return body.ack();
			}
		});
    }


}
