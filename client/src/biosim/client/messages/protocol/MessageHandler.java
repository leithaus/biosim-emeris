package biosim.client.messages.protocol;

import biosim.client.messages.model.MNode;
import biosim.client.messages.model.NodeContainer;
import biosim.client.utils.BiosimWebSocket;




public class MessageHandler {

	NodeContainer _nodeContainer = NodeContainer.get();
	
	public void process(Response response) {
		ResponseBody body = response.getResponseBody();
		if ( body instanceof CreateNodesResponse ) {
			for ( MNode newNode : ((CreateNodesResponse) body).getNodes() ) {
				_nodeContainer.insertOrUpdate(newNode);
			}
		} else {
			throw new RuntimeException("don't know how to handle type " + response.getClass());
		}
	}
	
	public void connect(BiosimWebSocket socket) {
	}
	
}
