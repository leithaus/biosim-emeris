package biosim.client.messages.model;

import java.util.Map;

import m3.gwt.lang.Function1;
import m3.gwt.lang.MapX;
import biosim.client.Biosim;
import biosim.client.eventlist.FineGrainedListListener;
import biosim.client.eventlist.ListEvent;
import biosim.client.eventlist.ObservableList;
import biosim.client.eventlist.Observables;

public class NodeContainer {

	static NodeContainer _instance = new NodeContainer();
	
	public static NodeContainer get() {
		return _instance;
	}
	
	MConnection _connection;
		
	public final ObservableList<MNode> nodes = Observables.create();
	
	{
		nodes.addListener(new FineGrainedListListener<MNode>() {
			@Override
			public void added(ListEvent<MNode> event) {
				MNode node = event.getElement();
				nodesByUid.put(node.getUid(), node);
			}
			@Override
			public void changed(ListEvent<MNode> event) {
			}
			@Override
			public void removed(ListEvent<MNode> event) {
				MNode node = event.getElement();
				nodesByUid.remove(node.getUid());
			}
		});
	}

	public final ObservableList<MConnection> connections = nodes.
			filter(new Function1<MNode, Boolean>() {
				@Override
				public Boolean apply(MNode t) {
					return t instanceof MConnection;
				}
			}).
			map(new Function1<MNode,MConnection>() {
				public MConnection apply(MNode t) {
					return (MConnection) t;
				}
			});
	
	public final Map<Uid,MNode> nodesByUid = MapX.create();
	

	private NodeContainer() {
		_connection = null;
	}

	public NodeContainer(MConnection conn) {
		_connection = conn;
	}
	
	public void insertOrUpdate(MNode node) {
		MNode localNode = nodesByUid.get(node.getUid());
		if ( localNode == null ) {
			nodes.add(node);
		} else {
			int i = nodes.indexOf(localNode);
			nodes.set(i, node);
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T extends MNode> T fetch(Uid uid) {
		return (T) nodesByUid.get(uid);
	}
	
	public Uid getAgentUid() {
		if ( _connection == null ) {
			return Biosim.get().getAgentUid();
		} else {
			return _connection.getRemoteAgent();
		}
	}
	
}
