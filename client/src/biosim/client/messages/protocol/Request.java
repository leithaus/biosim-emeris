package biosim.client.messages.protocol;

import m3.gwt.props.ApplyCodeGeneration;
import biosim.client.messages.model.Uid;

@ApplyCodeGeneration
public class Request {
	
	private Uid _uid;
	private RequestBody _requestBody;

	
	// BEGIN_GENERATED_CODE
	
	public biosim.client.messages.model.Uid getUid() {
	    return _uid;
	}
	public void setUid(biosim.client.messages.model.Uid uid0) {
	    _setUid(uid0);
	}
	protected void _setUid(biosim.client.messages.model.Uid uid0) {
	    biosim.client.messages.model.Uid before = _uid;
	     _uid = uid0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.uid, before, uid0);
	}
	public biosim.client.messages.protocol.RequestBody getRequestBody() {
	    return _requestBody;
	}
	public void setRequestBody(biosim.client.messages.protocol.RequestBody requestBody0) {
	    _setRequestBody(requestBody0);
	}
	protected void _setRequestBody(biosim.client.messages.protocol.RequestBody requestBody0) {
	    biosim.client.messages.protocol.RequestBody before = _requestBody;
	     _requestBody = requestBody0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.requestBody, before, requestBody0);
	}
	@Override
	public String toString() {
	    return m3.gwt.props.ToStringBuilder.toString(this, Context);
	}
	public static class RequestContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext uid = new m3.gwt.props.impl.AbstractPropertyContext<Request,biosim.client.messages.model.Uid>(this, "uid", biosim.client.messages.model.Uid.class, 0, null, false) {
	    	    protected biosim.client.messages.model.Uid getImpl(Request bean) { return bean.getUid(); }
	    	    protected void setImpl(Request bean, biosim.client.messages.model.Uid value ) { bean.setUid(value);}
	    };
	    public m3.gwt.props.PropertyContext requestBody = new m3.gwt.props.impl.AbstractPropertyContext<Request,biosim.client.messages.protocol.RequestBody>(this, "requestBody", biosim.client.messages.protocol.RequestBody.class, 1, null, false) {
	    	    protected biosim.client.messages.protocol.RequestBody getImpl(Request bean) { return bean.getRequestBody(); }
	    	    protected void setImpl(Request bean, biosim.client.messages.protocol.RequestBody value ) { bean.setRequestBody(value);}
	    };
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("biosim.client.messages.protocol.Request");
	        set = set.insert("java.lang.Object");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.uid);
	        list = list.cons(this.requestBody);
	        return list;
	    }
	    public Request newInstance() {
	        return new Request();
	    }
	    RequestContainerContext(Class<?> actualClass) {
	        super(biosim.client.messages.protocol.Request.class);
	    }
	}
	public static final RequestContainerContext Context = new RequestContainerContext(Request.class);

	// END_GENERATED_CODE
}
