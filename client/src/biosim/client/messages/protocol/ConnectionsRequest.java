package biosim.client.messages.protocol;

import m3.gwt.props.ApplyCodeGeneration;


@ApplyCodeGeneration
public class ConnectionsRequest extends RequestBody {
	
	// BEGIN_GENERATED_CODE
	
	@Override
	public String toString() {
	    return m3.gwt.props.ToStringBuilder.toString(this, Context);
	}
	public static class ConnectionsRequestContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("biosim.client.messages.protocol.ConnectionsRequest");
	        set = set.insert("biosim.client.messages.protocol.RequestBody");
	        set = set.insert("java.lang.Object");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        return list;
	    }
	    public ConnectionsRequest newInstance() {
	        return new ConnectionsRequest();
	    }
	    ConnectionsRequestContainerContext(Class<?> actualClass) {
	        super(biosim.client.messages.protocol.ConnectionsRequest.class, ConnectionsRequest.Context);
	    }
	}
	public static final ConnectionsRequestContainerContext Context = new ConnectionsRequestContainerContext(ConnectionsRequest.class);

	// END_GENERATED_CODE
}
