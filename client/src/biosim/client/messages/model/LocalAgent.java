package biosim.client.messages.model;

import java.util.Map;

import m3.gwt.lang.MapX;
import biosim.client.messages.protocol.CreateNodesRequest;
import biosim.client.messages.protocol.DeleteLinkRequest;
import biosim.client.utils.BiosimWebSocket;

public class LocalAgent {
	
	final Uid _agentUid;
	final BiosimWebSocket _socket;
	final AgentServices _agentServices;
	final Map<Uid,AgentServices> _remoteAgentServices = MapX.create();
	
	public LocalAgent(Uid agentUid, BiosimWebSocket _socket) {
		this._agentUid = agentUid;
		this._socket = _socket;
		this._agentServices = new AgentServicesImpl(agentUid, _socket, NodeContainer.get());
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

	public void removeLink(Uid fromUid, Uid toUid) {
		_socket.send(new DeleteLinkRequest(fromUid, toUid));
	}

	public AgentServices getAgentServices() {
		return _agentServices;
	}
	
	public AgentServices getAgentServices(Uid connectionUid) {
		if ( connectionUid == null ) {
			return getAgentServices();
		}
		for ( AgentServices as : _remoteAgentServices.values() ) {
			if ( as.getConnectionUid().equals(connectionUid) ) {
				return as;
			}
		}
		return null;
	}
	
	public AgentServices getAgentServices(MConnection conn) {
		AgentServices ras = _remoteAgentServices.get(conn.getUid());
		if ( ras == null ) {
			ras = new AgentServicesImpl(conn, _socket, new NodeContainer(conn));
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
		if ( owner != null && owner.equals(_agentUid) ) {
			return true;
		} else {
			return false;
		}
	}
	
}
