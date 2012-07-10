package biosim.client.messages.protocol;

import java.util.List;

import m3.gwt.lang.ListX;
import m3.gwt.props.ApplyCodeGeneration;
import m3.gwt.websocket.WebSocket.IncomingMessage;
import biosim.client.messages.model.Uid;

@ApplyCodeGeneration
public class Response {
	
	private Uid _uid;
	private Uid _requestUid;
	private ResponseBody _responseBody;
	
	public IncomingMessage asIncomingMessage() {
		return new IncomingMessage() {
			@Override
			public String id() {
				return _uid.getValue();
			}
			
			@SuppressWarnings("unchecked")
			@Override
			public <T> T delegate() {
				return (T) Response.this;
			}
			
			@Override
			public List<String> acks() {
				return ListX.create();
			}
			
			@Override
			public boolean ack() {
				return false;
			}
		};
	}

	
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
	public biosim.client.messages.model.Uid getRequestUid() {
	    return _requestUid;
	}
	public void setRequestUid(biosim.client.messages.model.Uid requestUid0) {
	    _setRequestUid(requestUid0);
	}
	protected void _setRequestUid(biosim.client.messages.model.Uid requestUid0) {
	    biosim.client.messages.model.Uid before = _requestUid;
	     _requestUid = requestUid0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.requestUid, before, requestUid0);
	}
	public biosim.client.messages.protocol.ResponseBody getResponseBody() {
	    return _responseBody;
	}
	public void setResponseBody(biosim.client.messages.protocol.ResponseBody responseBody0) {
	    _setResponseBody(responseBody0);
	}
	protected void _setResponseBody(biosim.client.messages.protocol.ResponseBody responseBody0) {
	    biosim.client.messages.protocol.ResponseBody before = _responseBody;
	     _responseBody = responseBody0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.responseBody, before, responseBody0);
	}
	@Override
	public String toString() {
	    return m3.gwt.props.ToStringBuilder.toString(this, Context);
	}
	public static class ResponseContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext uid = new m3.gwt.props.impl.AbstractPropertyContext<Response,biosim.client.messages.model.Uid>(this, "uid", biosim.client.messages.model.Uid.class, 0, null, false) {
	    	    protected biosim.client.messages.model.Uid getImpl(Response bean) { return bean.getUid(); }
	    	    protected void setImpl(Response bean, biosim.client.messages.model.Uid value ) { bean.setUid(value);}
	    };
	    public m3.gwt.props.PropertyContext requestUid = new m3.gwt.props.impl.AbstractPropertyContext<Response,biosim.client.messages.model.Uid>(this, "requestUid", biosim.client.messages.model.Uid.class, 1, null, false) {
	    	    protected biosim.client.messages.model.Uid getImpl(Response bean) { return bean.getRequestUid(); }
	    	    protected void setImpl(Response bean, biosim.client.messages.model.Uid value ) { bean.setRequestUid(value);}
	    };
	    public m3.gwt.props.PropertyContext responseBody = new m3.gwt.props.impl.AbstractPropertyContext<Response,biosim.client.messages.protocol.ResponseBody>(this, "responseBody", biosim.client.messages.protocol.ResponseBody.class, 2, null, false) {
	    	    protected biosim.client.messages.protocol.ResponseBody getImpl(Response bean) { return bean.getResponseBody(); }
	    	    protected void setImpl(Response bean, biosim.client.messages.protocol.ResponseBody value ) { bean.setResponseBody(value);}
	    };
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.messages.protocol.Response");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.uid);
	        list = list.cons(this.requestUid);
	        list = list.cons(this.responseBody);
	        return list;
	    }
	    public Response newInstance() {
	        return new Response();
	    }
	    ResponseContainerContext(Class<?> actualClass) {
	        super(biosim.client.messages.protocol.Response.class);
	    }
	}
	public static final ResponseContainerContext Context = new ResponseContainerContext(Response.class);

	// END_GENERATED_CODE
}
