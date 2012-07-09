package biosim.client.messages.protocol;

import biosim.client.utils.BiosimWebSocket;




public class MessageHandler {

	public void process(Response response) {
		if ( Boolean.FALSE ) {

		} else {
			throw new RuntimeException("don't know how to handle type " + response.getClass());
		}
	}
	
	public void connect(BiosimWebSocket socket) {
	}
	
}
