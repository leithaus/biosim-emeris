package biosim.client.messages.model;

import java.util.List;

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

	final Uid _connectionUid;
	final BiosimWebSocket _socket;
	final NodeContainer _nodeContainer;
	
	public AgentServicesImpl(Uid connectionUid, BiosimWebSocket socket, NodeContainer nodeContainer) {
		_connectionUid = connectionUid;
		_socket = socket;
		_nodeContainer = nodeContainer;
	}

	@Override
	public <T extends MNode> void fetch(Uid uid, final Function1<T, Void> asyncCallback) {
		fetch(uid, false, asyncCallback);
	}
	
	<T extends MNode> void fetchImpl(Uid uid, final Function1<T, Void> asyncCallback) {
		send(new FetchRequest(uid), new Function1<FetchResponse, Void>() {
			@SuppressWarnings("unchecked")
			@Override
			public Void apply(FetchResponse response) {
				asyncCallback.apply((T)response.getNode());
				_nodeContainer.nodes.add(response.getNode());
				return null;
			}
		});
	}
	
	@Override
	public <T extends MNode> void fetch(Uid uid, boolean bypassCache, Function1<T, Void> asyncCallback) {
		
		T t = null;
		if ( !bypassCache ) {
			t = _nodeContainer.fetch(uid);
		}
		
		if ( t == null ) {
			fetchImpl(uid, asyncCallback);			
		} else {
			asyncCallback.apply(t);
		}
		
	}
	
	@Override
	public <T extends MNode> void select(Class<T> clazz, final Function1<FList<T>, Void> asyncCallback) {
		SelectRequest req = new SelectRequest();
		req.setShortClassname(ClassX.getShortName(clazz));
		send(req, new Function1<SelectResponse,Void>() {
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
	public void query(List<MNode> labels, Uid uid, final Function1<List<FilterAcceptCriteria>, Void> asyncCallback) {
		send(new QueryRequest(), new Function1<QueryResponse, Void>() {
			@Override
			public Void apply(QueryResponse response) {
				asyncCallback.apply(response.getResults());
				return null;
			}
		});
	}

	public void send(final RequestBody body, Function1<?,?> responseHandler) {
    	if ( body instanceof ConnectionScopedRequestBody ) {
    		((ConnectionScopedRequestBody) body).setConnectionUid(_connectionUid);
    	}
    	_socket.send(body, responseHandler);
    }
	
	public NodeContainer getNodeContainer() {
		return _nodeContainer;
	}
	
}
