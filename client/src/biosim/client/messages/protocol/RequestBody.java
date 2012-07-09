package biosim.client.messages.protocol;

import m3.gwt.props.ApplyCodeGeneration;

@ApplyCodeGeneration
public abstract class RequestBody {

	// BEGIN_GENERATED_CODE
	
	@Override
	public String toString() {
	    return m3.gwt.props.ToStringBuilder.toString(this, Context);
	}
	public static class RequestBodyContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("biosim.client.messages.protocol.RequestBody");
	        set = set.insert("java.lang.Object");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        return list;
	    }
	    public RequestBody newInstance() {
	        throw new RuntimeException("cannot instantiate an interface or abstract class");
	    }
	    RequestBodyContainerContext(Class<?> actualClass) {
	        super(biosim.client.messages.protocol.RequestBody.class);
	    }
	}
	public static final RequestBodyContainerContext Context = new RequestBodyContainerContext(RequestBody.class);

	// END_GENERATED_CODE
}
