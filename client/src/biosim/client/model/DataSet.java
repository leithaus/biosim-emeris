package biosim.client.model;

import java.util.List;
import java.util.Map;

import m3.gwt.lang.Function1;
import m3.gwt.lang.ListX;
import m3.gwt.lang.MapX;
import biosim.client.eventlist.FineGrainedListListener;
import biosim.client.eventlist.ListEvent;
import biosim.client.eventlist.ObservableList;
import biosim.client.eventlist.Observables;
import biosim.client.utils.GeneratorMap;

@SuppressWarnings("serial")
public class DataSet {

	public final ObservableList<Node> nodes = Observables.create();
	public final ObservableList<Link> links = nodes.filter(new Function1<Node, Boolean>() {
		@Override
		public Boolean apply(Node t) {
			return t instanceof Link;
		}
	}).map(new Function1<Node,Link>() {
		@Override
		public Link apply(Node n) {
			return (Link) n;
		}
	});
	
	public final Map<Uid,Node> nodesByUid = MapX.create();
	
	public final Map<Node,List<Link>> linksByFrom = new GeneratorMap<Node, List<Link>>() {
		public java.util.List<Link> generate(Node k) {
			return ListX.create();
		}
	};

	public final Map<Node,List<Link>> linksByTo = new GeneratorMap<Node, List<Link>>() {
		public java.util.List<Link> generate(Node k) {
			return ListX.create();
		}
	};
	
	{
		nodes.addListener(new FineGrainedListListener<Node>() {
			@Override
			public void added(ListEvent<Node> event) {
				nodesByUid.put(event.getElement().getUid(), event.getElement());
			}
			@Override
			public void removed(ListEvent<Node> event) {
				Uid key = event.getElement().getUid();
				nodesByUid.remove(key);
				linksByTo.remove(key);
				linksByFrom.remove(key);
			}
		});
		
		links.addListener(new FineGrainedListListener<Link>() {
			@Override
			public void added(ListEvent<Link> event) {
				Link l = event.getElement();
				linksByTo.get(l.getToNode()).add(l);
				linksByFrom.get(l.getFromNode()).add(l);
			}
			@Override
			public void removed(ListEvent<Link> event) {
				Link l = event.getElement();
				linksByTo.get(l.getToNode()).remove(l);
				linksByFrom.get(l.getFromNode()).remove(l);
			}
		});
		
	}
	
	public Link addLink(Node from, Node to) {
		return addLink(from.getUid(), to.getUid());
	}
	
	public Link addLink(Uid from, Uid to) {
		Link l = new Link(this, from, to);
		if ( !nodes.contains(l) ) {
			nodes.add(l);
		}
		return l;
	}

	public Node getNode(Uid nodeUid) {
		return nodesByUid.get(nodeUid);
	}

	public void removeLink(Link link) {
		links.remove(link);
	}

	public void clear() {
		nodes.clear();
		nodesByUid.clear();
	}
	
}
