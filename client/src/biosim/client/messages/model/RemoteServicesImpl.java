package biosim.client.messages.model;

import m3.fj.data.FList;
import m3.gwt.lang.ClassX;
import m3.gwt.lang.Function1;
import biosim.client.messages.protocol.CreateNodesRequest;
import biosim.client.messages.protocol.FetchRequest;
import biosim.client.messages.protocol.FetchResponse;
import biosim.client.messages.protocol.QueryRequest;
import biosim.client.messages.protocol.QueryResponse;
import biosim.client.messages.protocol.RootLabelsRequest;
import biosim.client.messages.protocol.RootLabelsResponse;
import biosim.client.messages.protocol.SelectRequest;
import biosim.client.messages.protocol.SelectResponse;
import biosim.client.utils.BiosimWebSocket;


public class RemoteServicesImpl implements RemoteServices {

	
	final BiosimWebSocket _socket;
	final NodeContainer _nodeContainer;
	
	public RemoteServicesImpl(BiosimWebSocket socket, NodeContainer nodeContainer) {
		_socket = socket;
		_nodeContainer = nodeContainer;
	}

	@Override
	public <T extends MNode> void fetch(Uid uid, final Function1<T, Void> asyncCallback) {
		fetch(FList.create(uid), false, new Function1<Iterable<T>, Void>() {
			@Override
			public Void apply(Iterable<T> list) {
				asyncCallback.apply(list.iterator().next());
				return null;
			}
		});
	}
	
	<T extends MNode> void fetchImpl(Iterable<Uid> uids, final Function1<FList<T>, Void> asyncCallback) {
		_socket.send(new FetchRequest(uids), new Function1<FetchResponse, Void>() {
			@SuppressWarnings("unchecked")
			@Override
			public Void apply(FetchResponse response) {
				asyncCallback.apply((FList<T>)response.getNodes());
				for ( MNode t : response.getNodes() ) {
					_nodeContainer.insertOrUpdate(t);
				}
				return null;
			}
		});
	}
	
	@Override
	public <T extends MNode> void fetch(Iterable<Uid> uids, boolean bypassCache, final Function1<Iterable<T>, Void> asyncCallback) {
		
		FList<T> results = FList.nil();
		Iterable<Uid> uidsToSendToServer;
		
		if ( !bypassCache ) {
			FList<Uid> temp = FList.nil();
			for ( Uid uid : uids ) {
				T t = _nodeContainer.fetch(uid);
				if ( t != null ) {
					results = results.cons(t);
				} else {
					temp = temp.cons(uid);
				}
			}
			uidsToSendToServer = temp;
		} else {
			uidsToSendToServer = uids;
		}
		
		if ( uidsToSendToServer.iterator().hasNext() ) {
			final FList<T> results_f = results;
			fetchImpl(uidsToSendToServer, new Function1<FList<T>, Void>() {
				@Override
				public Void apply(FList<T> list) {
					if ( !results_f.isEmpty() ) {
						for ( T t : results_f ) {
							list = list.cons(t);
						}
					}
					asyncCallback.apply(list);
					return null;
				}
			});			
		} else {
			asyncCallback.apply(results);
		}
		
	}
	
	@Override
	public <T extends MNode> void select(Class<T> clazz, final Function1<Iterable<T>, Void> asyncCallback) {
		SelectRequest req = new SelectRequest();
		req.setShortClassname(ClassX.getShortName(clazz));
		_socket.send(req, new Function1<SelectResponse,Void>() {
			@SuppressWarnings("unchecked")
			@Override
			public Void apply(SelectResponse response) {
				for ( MNode n : response.getNodes() ) {
					_nodeContainer.insertOrUpdate(n);
				}
				return asyncCallback.apply((FList<T>)response.getNodes());
			}
		});
	}
	
	@Override
	public void insertOrUpdate(MNode...nodes) {
		_socket.send(new CreateNodesRequest(nodes));
	}
	
	@Override
	public void query(Iterable<MNode> labels, Uid uid, final Function1<Iterable<FilterAcceptCriteria>, Void> asyncCallback) {
		_socket.send(new QueryRequest(), new Function1<QueryResponse, Void>() {
			@Override
			public Void apply(QueryResponse response) {
				asyncCallback.apply(response.getResults());
				return null;
			}
		});
	}
	
	@Override
	public void insertChild(MNode parent, MNode child) {
		MLink link = new MLink(parent, child);
		insertOrUpdate(child, link);
	}
	
	@Override
	public void insertTextNode(MNode parent, String text) {
		insertChild(parent, new MText(text));
	}
	
	@Override
	public void removeLink(MNode p, MNode node) {
		// TDGlen implement me
		throw new RuntimeException("implement me");
	}
	
	@Override
	public void rootLabels(Uid uid, final Function1<Iterable<MLabel>, Void> asyncCallback) {
		_socket.send(new RootLabelsRequest(), new Function1<RootLabelsResponse, Void>() {
			@Override
			public Void apply(RootLabelsResponse response) {
				fetch(response.getUids(), false, asyncCallback);
				return null;
			}
		});
	}

}
