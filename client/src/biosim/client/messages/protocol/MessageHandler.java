package biosim.client.messages.protocol;

import biosim.client.messages.model.AgentServices;
import biosim.client.messages.model.LocalAgent;
import biosim.client.messages.model.MNode;
import biosim.client.messages.model.NodeContainer;
import biosim.client.utils.BiosimWebSocket;




public class MessageHandler {

	NodeContainer _nodeContainer = NodeContainer.get();
	LocalAgent _localAgent;
	
	public void process(Response response) {
		ResponseBody body = response.getResponseBody();
		if ( body instanceof CreateNodesResponse ) {
			CreateNodesResponse cnr = (CreateNodesResponse) body;
			AgentServices agentServices;
			if ( cnr.getConnectionUid() == null ) {
				agentServices = _localAgent.getAgentServices(cnr.getConnectionUid());
			} else {
				agentServices = _localAgent.getAgentServices(cnr.getConnectionUid());
			}
			for ( MNode newNode : ((CreateNodesResponse) body).getNodes() ) {
				newNode.setAgentServices(agentServices);
				_nodeContainer.insertOrUpdate(newNode);
			}
		} else {
			throw new RuntimeException("don't know how to handle type " + response.getClass());
		}
	}
	
	public void connect(BiosimWebSocket socket) {
	}
	
	public void setLocalAgent(LocalAgent localAgent) {
		_localAgent = localAgent;
	}
	
}
