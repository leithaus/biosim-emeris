package biosim.client.messages.protocol;

import m3.gwt.props.ApplyCodeGeneration;

@ApplyCodeGeneration
public class GetRemoteConnectionRequest extends ConnectionScopedRequestBody {

	public GetRemoteConnectionRequest() {
	}
	
	// BEGIN_GENERATED_CODE
	
	@Override
	public String toString() {
	    return m3.gwt.props.ToStringBuilder.toString(this, Context);
	}
	public static class GetRemoteConnectionRequestContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext connectionUid = ConnectionScopedRequestBody.Context.connectionUid;
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("biosim.client.messages.protocol.RequestBody");
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.messages.protocol.GetRemoteConnectionRequest");
	        set = set.insert("biosim.client.messages.protocol.ConnectionScopedRequestBody");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.connectionUid);
	        return list;
	    }
	    public GetRemoteConnectionRequest newInstance() {
	        return new GetRemoteConnectionRequest();
	    }
	    GetRemoteConnectionRequestContainerContext(Class<?> actualClass) {
	        super(biosim.client.messages.protocol.GetRemoteConnectionRequest.class, GetRemoteConnectionRequest.Context);
	    }
	}
	public static final GetRemoteConnectionRequestContainerContext Context = new GetRemoteConnectionRequestContainerContext(GetRemoteConnectionRequest.class);

	// END_GENERATED_CODE
}
