package biosim.client.messages.protocol;



public class RootLabelsRequest extends RequestBody {

	public RootLabelsRequest() {
	}
	
	// BEGIN_GENERATED_CODE
	
	@Override
	public String toString() {
	    return m3.gwt.props.ToStringBuilder.toString(this, Context);
	}
	public static class RootLabelsRequestContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("biosim.client.messages.protocol.RequestBody");
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.messages.protocol.RootLabelsRequest");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        return list;
	    }
	    public RootLabelsRequest newInstance() {
	        return new RootLabelsRequest();
	    }
	    RootLabelsRequestContainerContext(Class<?> actualClass) {
	        super(biosim.client.messages.protocol.RootLabelsRequest.class, RootLabelsRequest.Context);
	    }
	}
	public static final RootLabelsRequestContainerContext Context = new RootLabelsRequestContainerContext(RootLabelsRequest.class);

	// END_GENERATED_CODE
}
