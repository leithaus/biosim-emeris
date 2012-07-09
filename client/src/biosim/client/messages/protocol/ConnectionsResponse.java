package biosim.client.messages.protocol;

import biosim.client.messages.model.MConnection;
import m3.fj.data.FList;
import m3.gwt.props.ApplyCodeGeneration;

@ApplyCodeGeneration
public class ConnectionsResponse extends ResponseBody {

	private FList<MConnection> _connections;
	
	public ConnectionsResponse() {
	}
	
	public ConnectionsResponse(FList<MConnection> connections) {
		_connections = connections;
	}

	// BEGIN_GENERATED_CODE
	
	public m3.fj.data.FList<biosim.client.messages.model.MConnection> getConnections() {
	    return _connections;
	}
	public void setConnections(m3.fj.data.FList<biosim.client.messages.model.MConnection> connections0) {
	    _setConnections(connections0);
	}
	protected void _setConnections(m3.fj.data.FList<biosim.client.messages.model.MConnection> connections0) {
	    m3.fj.data.FList<biosim.client.messages.model.MConnection> before = _connections;
	     _connections = connections0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.connections, before, connections0);
	}
	@Override
	public String toString() {
	    return m3.gwt.props.ToStringBuilder.toString(this, Context);
	}
	public static class ConnectionsResponseContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext connections = new m3.gwt.props.impl.AbstractPropertyContext<ConnectionsResponse,m3.fj.data.FList<biosim.client.messages.model.MConnection>>(this, "connections", m3.fj.data.FList.class, 0, biosim.client.messages.model.MConnection.class, false) {
	    	    protected m3.fj.data.FList<biosim.client.messages.model.MConnection> getImpl(ConnectionsResponse bean) { return bean.getConnections(); }
	    	    protected void setImpl(ConnectionsResponse bean, m3.fj.data.FList<biosim.client.messages.model.MConnection> value ) { bean.setConnections(value);}
	    };
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("biosim.client.messages.protocol.ConnectionsResponse");
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.messages.protocol.ResponseBody");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.connections);
	        return list;
	    }
	    public ConnectionsResponse newInstance() {
	        return new ConnectionsResponse();
	    }
	    ConnectionsResponseContainerContext(Class<?> actualClass) {
	        super(biosim.client.messages.protocol.ConnectionsResponse.class, ConnectionsResponse.Context);
	    }
	}
	public static final ConnectionsResponseContainerContext Context = new ConnectionsResponseContainerContext(ConnectionsResponse.class);

	// END_GENERATED_CODE
}
