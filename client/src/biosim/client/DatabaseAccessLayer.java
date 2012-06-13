package biosim.client;

import m3.gwt.lang.Function1;
import biosim.client.eventlist.EventManager;
import biosim.client.eventlist.ListEvent;
import biosim.client.eventlist.ListListener;
import biosim.client.eventlist.ObservableList;
import biosim.client.eventlist.Observables;
import biosim.client.messages.CreateNodes;
import biosim.client.messages.RemoveNodes;
import biosim.client.model.Address;
import biosim.client.model.DataSet;
import biosim.client.model.Label;
import biosim.client.model.Link;
import biosim.client.model.Node;
import biosim.client.model.NodeVisitor;
import biosim.client.model.Person;
import biosim.client.model.Phone;
import biosim.client.model.TextMessage;
import biosim.client.model.Uid;
import biosim.client.utils.BiosimWebSocket;

public class DatabaseAccessLayer {
	
	final DataSet _dataSet = new DataSet();
	
	public final EventManager<DatabaseAccessLayerListener> refreshListeners = EventManager.create();
	
	final ObservableList<Node> _content = _dataSet.nodes.filter(new Function1<Node, Boolean>() {
		public Boolean apply(Node node) {
			return !(node instanceof Person);
		}
	});

	final ObservableList<Node> _connections = _dataSet.nodes.filter(new Function1<Node, Boolean>() {
		public Boolean apply(Node node) {
			return node instanceof Person && !_labelRoots.contains(node);
		}
	});

	final ObservableList<Node> _labelRoots = Observables.create();
	
	{
		_labelRoots.addListener(new ListListener<Node>() {
			@Override
			public void event(ListEvent<Node> event) {
				_connections.reapply();
			}
		});
	}
	
	BiosimWebSocket _socket;

	public DatabaseAccessLayer() {
	}
	
	public void setSocket(BiosimWebSocket socket) {
		_socket = socket;
	}
	
	public ObservableList<Node> getLabelRoots() {
		return _labelRoots;
	}

	public void addChildLabel(Node parent, String childName) {
		Label label = new Label(_dataSet, childName);
		Link link = parent.link(label);
		_socket.send(new CreateNodes(label, link));
		fireRefreshContentPane();
	}
	
	public void fireRefreshContentPane() {
		refreshListeners.fire(new Function1<DatabaseAccessLayerListener, Void>() {
			@Override
			public Void apply(DatabaseAccessLayerListener l) {
				l.refreshContentPane();
				return null;
			}
		});
	}

	public void removeLink(Link link) {
		_dataSet.nodes.remove(link);
		_socket.send(new RemoveNodes(link));
		NodeVisitor visitor = new NodeVisitor() {
			@Override
			public void visit(Node node) {
				int i = _dataSet.nodes.indexOf(node);
				_dataSet.nodes.set(i, node);
			}
		};
		visitor.visit(link.getToNode());
		link.getToNode().visitChildren(visitor);
	}

	public void addLink(Node from, Node to) {
		Link link = new Link(_dataSet, from.getUid(), to.getUid());
		_socket.send(new CreateNodes(link));
	}

	public Node getNode(Uid nodeId) {
		return _dataSet.nodesByUid.get(nodeId);
	}

	public Node getNode(String nodeId) {
		return getNode(new Uid(nodeId));
	}

	public ObservableList<Node> getConnections() {
		return _connections;
	}
	
	public ObservableList<Node> getContent() {
		return _content;
	}

	public ObservableList<Node> getNodes() {
		return _dataSet.nodes;
	}

	public DataSet getDataSet() {
		return _dataSet;
	}

	public void removeLink(Node from, Node to) {
		Link deleteMe = null;
		for ( Link l : from.getOutgoingLinks() ) {
			if ( l.getToNode().equals(to) ) {
				deleteMe = l;
				break;
			}
		}
		removeLink(deleteMe);
	}

	public void removeNode(Uid uid) {
		Node node = getNode(uid);
		_dataSet.nodes.remove(node);
		fireRefreshContentPane();
	}

	public void addTextMessage(Node parent, String text) {
		TextMessage message = new TextMessage(_dataSet, text);
		Link link = parent.link(message);
		_socket.send(new CreateNodes(message, link));
		fireRefreshContentPane();		
	}

	public void addPhone(Node parent, String text) {
		Phone phone = new Phone(_dataSet, text);
		addNode(phone, parent);				
	}

	public void addNode(Node nodeToAdd, Node parent) {
		Link link = parent.link(nodeToAdd);
		_socket.send(new CreateNodes(nodeToAdd, link));
	}

    public void addNode(Node nodeToAdd) {
        _socket.send(new CreateNodes(nodeToAdd));
    }

	public void addAddress(Node parent, String address) {
		addNode(new Address(getDataSet(), address), parent);
	}

}
