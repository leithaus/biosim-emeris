package biosim.client.messages.model;

import m3.fj.data.FList;
import m3.gwt.lang.ClassX;
import m3.gwt.lang.Function1;
import biosim.client.messages.protocol.ConnectionScopedRequestBody;
import biosim.client.messages.protocol.FetchRequest;
import biosim.client.messages.protocol.FetchResponse;
import biosim.client.messages.protocol.QueryRequest;
import biosim.client.messages.protocol.QueryResponse;
import biosim.client.messages.protocol.RequestBody;
import biosim.client.messages.protocol.SelectRequest;
import biosim.client.messages.protocol.SelectResponse;
import biosim.client.utils.BiosimWebSocket;


public class AgentServicesImpl implements AgentServices {

	final Uid _agentUid;
	final MConnection _connection;
	final BiosimWebSocket _socket;
	final NodeContainer _nodeContainer;
	
	public AgentServicesImpl(Uid agentUid, BiosimWebSocket socket, NodeContainer nodeContainer) {
		_agentUid = agentUid;
		_connection = null;
		_socket = socket;
		_nodeContainer = nodeContainer;
	}

	public AgentServicesImpl( MConnection connection, BiosimWebSocket socket, NodeContainer nodeContainer) {
		_agentUid = connection.getRemoteAgent();
		_connection = connection;
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
	
	<T extends MNode> void directFetch(Iterable<Uid> uids, final Function1<FList<T>, Void> asyncCallback) {
		send(new FetchRequest(uids), new Function1<FetchResponse, Void>() {
			@SuppressWarnings("unchecked")
			@Override
			public Void apply(FetchResponse response) {
				insertOrUpdate(response.getNodes());
				asyncCallback.apply((FList<T>)response.getNodes());
				return null;
			}

		});
	}

	private void insertOrUpdate(Iterable<MNode> nodes) {
		for ( MNode n : nodes ) {
			insertOrUpdate(n);
		}
	}

	private void insertOrUpdate(MNode t) {
		t.setAgentServices(this);
		_nodeContainer.insertOrUpdate(t);
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
			directFetch(uidsToSendToServer, new Function1<FList<T>, Void>() {
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
		send(req, new Function1<SelectResponse,Void>() {
			@SuppressWarnings("unchecked")
			@Override
			public Void apply(SelectResponse response) {
				insertOrUpdate(response.getNodes());
				return asyncCallback.apply((FList<T>)response.getNodes());
			}
		});
	}
	
	@Override
	public void query(Iterable<MNode> labels, Uid uid, final Function1<Iterable<FilterAcceptCriteria>, Void> asyncCallback) {
		send(new QueryRequest(), new Function1<QueryResponse, Void>() {
			@Override
			public Void apply(QueryResponse response) {
				asyncCallback.apply(response.getResults());
				return null;
			}
		});
	}
	
	public Uid getAgentUid() {
		return _agentUid;
	}

	@Override
	public Uid getConnectionUid() {
		if ( _connection == null ) {
			return null;
		} else {
			return _connection.getUid();
		}
	}
	
	public void send(final RequestBody body, Function1<?,?> responseHandler) {
    	if ( body instanceof ConnectionScopedRequestBody && _connection != null ) {
    		((ConnectionScopedRequestBody) body).setConnectionUid(_connection.getUid());
    	}
    	_socket.send(body, responseHandler);
    }
	
	public NodeContainer getNodeContainer() {
		return _nodeContainer;
	}
}
