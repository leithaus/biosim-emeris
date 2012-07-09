package biosim.client.messages.protocol;

import m3.gwt.props.ApplyCodeGeneration;

@ApplyCodeGeneration
public abstract class ResponseBody {

	// BEGIN_GENERATED_CODE
	
	@Override
	public String toString() {
	    return m3.gwt.props.ToStringBuilder.toString(this, Context);
	}
	public static class ResponseBodyContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.messages.protocol.ResponseBody");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        return list;
	    }
	    public ResponseBody newInstance() {
	        throw new RuntimeException("cannot instantiate an interface or abstract class");
	    }
	    ResponseBodyContainerContext(Class<?> actualClass) {
	        super(biosim.client.messages.protocol.ResponseBody.class);
	    }
	}
	public static final ResponseBodyContainerContext Context = new ResponseBodyContainerContext(ResponseBody.class);

	// END_GENERATED_CODE
}
