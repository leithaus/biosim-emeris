package biosim.client.messages.model;

import java.util.List;

import m3.gwt.lang.ClassX;
import m3.gwt.lang.Function1;
import biosim.client.messages.protocol.FetchRequest;
import biosim.client.messages.protocol.FetchResponse;
import biosim.client.messages.protocol.InsertOrUpdateRequest;
import biosim.client.messages.protocol.QueryRequest;
import biosim.client.messages.protocol.QueryResponse;
import biosim.client.messages.protocol.SelectRequest;
import biosim.client.messages.protocol.SelectResponse;
import biosim.client.model.Uid;
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
		fetch(uid, false, asyncCallback);
	}
	
	<T extends MNode> void fetchImpl(Uid uid, final Function1<T, Void> asyncCallback) {
		_socket.send(new FetchRequest(uid), new Function1<FetchResponse, Void>() {
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
	public <T extends MNode> void select(Class<T> clazz, final Function1<List<T>, Void> asyncCallback) {
		SelectRequest req = new SelectRequest();
		req.setShortClassname(ClassX.getShortName(clazz));
		_socket.send(req, new Function1<SelectResponse,Void>() {
			@SuppressWarnings("unchecked")
			@Override
			public Void apply(SelectResponse response) {
				return asyncCallback.apply((List<T>)response.getNodes());
			}
		});
	}
	
	@Override
	public void insertOrUpdate(MNode node) {
		_socket.send(new InsertOrUpdateRequest(node));
	}
	
	@Override
	public void query(List<MNode> labels, Uid uid, final Function1<List<FilterAcceptCriteria>, Void> asyncCallback) {
		_socket.send(new QueryRequest(), new Function1<QueryResponse, Void>() {
			@Override
			public Void apply(QueryResponse response) {
				asyncCallback.apply(response.getResults());
				return null;
			}
		});
	}

}
