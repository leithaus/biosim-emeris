package biosim.client.messages.model;

import java.util.Map;

import m3.gwt.lang.MapX;
import biosim.client.Biosim;
import biosim.client.messages.protocol.CreateNodesRequest;
import biosim.client.utils.BiosimWebSocket;

public class LocalAgent {
	
	BiosimWebSocket _socket;
	AgentServices _agentServices;
	Map<Uid,AgentServices> _remoteAgentServices = MapX.create();
	
	public LocalAgent(BiosimWebSocket _socket) {
		this._socket = _socket;
		this._agentServices = new AgentServicesImpl(null, _socket, NodeContainer.get());
	}

	public void insertOrUpdate(MNode...nodes) {
		_socket.send(new CreateNodesRequest(nodes));
	}
	
	public void insertChild(MNode parent, MNode child) {
		MLink link = new MLink(parent, child);
		insertOrUpdate(child, link);
	}
	
	public void insertTextNode(MNode parent, String text) {
		insertChild(parent, new MText(text));
	}
	
	public void removeLink(MNode p, MNode node) {
		// TDGlen implement me
		throw new RuntimeException("implement me");
	}

	public AgentServices getAgentServices() {
		return _agentServices;
	}
	
	public AgentServices getRemoteAgentServices(MConnection conn) {
		AgentServices ras = _remoteAgentServices.get(conn.getUid());
		if ( ras == null ) {
			ras = new AgentServicesImpl(conn.getUid(), _socket, new NodeContainer(conn));
			_remoteAgentServices.put(conn.getUid(), ras);
		}
		return ras;
	}

	public Uid getOwner(MNode node) {
		Uid key = node.getUid();
		if ( _agentServices.getNodeContainer().nodesByUid.containsKey(key) ) {
			return _agentServices.getNodeContainer().getAgentUid();
		}
		for (AgentServices as : _remoteAgentServices.values() ) {
			if ( as.getNodeContainer().nodesByUid.containsKey(key) ) {
				return as.getNodeContainer().getAgentUid();
			}						
		}
		return null;
	}

	public boolean isEditable(MLabel n) {
		Uid owner = getOwner(n);
		if ( owner != null && owner.equals(Biosim.get().getAgentUid()) ) {
			return true;
		} else {
			return false;
		}
	}
}
