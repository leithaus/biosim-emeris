package biosim.client.messages.model;

import java.util.Map;

import m3.gwt.lang.MapX;
import biosim.client.eventlist.FineGrainedListListener;
import biosim.client.eventlist.ListEvent;
import biosim.client.eventlist.ObservableList;
import biosim.client.eventlist.Observables;
import biosim.client.model.Uid;

public class NodeContainer {

	static NodeContainer _instance = new NodeContainer();
	
	public static NodeContainer get() {
		return _instance;
	}
		
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
	
	public final Map<Uid,MNode> nodesByUid = MapX.create();

	private NodeContainer() {
	}

	@SuppressWarnings("unchecked")
	public <T extends MNode> T fetch(Uid uid) {
		return (T) nodesByUid.get(uid);
	}
	
}
