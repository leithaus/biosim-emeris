package biosim.client.messages.protocol;

import m3.gwt.props.ApplyCodeGeneration;
import biosim.client.messages.model.Uid;

@ApplyCodeGeneration
public class GetRemoteConnectionResponse extends ResponseBody {

	Uid _connectionUid;
	
	private GetRemoteConnectionResponse() {
	}

	public GetRemoteConnectionResponse(Uid _connectionUid) {
		this._connectionUid = _connectionUid;
	}
	
	// BEGIN_GENERATED_CODE
	
	public biosim.client.messages.model.Uid getConnectionUid() {
	    return _connectionUid;
	}
	public void setConnectionUid(biosim.client.messages.model.Uid connectionUid0) {
	    _setConnectionUid(connectionUid0);
	}
	protected void _setConnectionUid(biosim.client.messages.model.Uid connectionUid0) {
	    biosim.client.messages.model.Uid before = _connectionUid;
	     _connectionUid = connectionUid0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.connectionUid, before, connectionUid0);
	}
	@Override
	public String toString() {
	    return m3.gwt.props.ToStringBuilder.toString(this, Context);
	}
	public static class GetRemoteConnectionResponseContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext connectionUid = new m3.gwt.props.impl.AbstractPropertyContext<GetRemoteConnectionResponse,biosim.client.messages.model.Uid>(this, "connectionUid", biosim.client.messages.model.Uid.class, 0, null, false) {
	    	    protected biosim.client.messages.model.Uid getImpl(GetRemoteConnectionResponse bean) { return bean.getConnectionUid(); }
	    	    protected void setImpl(GetRemoteConnectionResponse bean, biosim.client.messages.model.Uid value ) { bean.setConnectionUid(value);}
	    };
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.messages.protocol.GetRemoteConnectionResponse");
	        set = set.insert("biosim.client.messages.protocol.ResponseBody");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.connectionUid);
	        return list;
	    }
	    public GetRemoteConnectionResponse newInstance() {
	        return new GetRemoteConnectionResponse();
	    }
	    GetRemoteConnectionResponseContainerContext(Class<?> actualClass) {
	        super(biosim.client.messages.protocol.GetRemoteConnectionResponse.class, GetRemoteConnectionResponse.Context);
	    }
	}
	public static final GetRemoteConnectionResponseContainerContext Context = new GetRemoteConnectionResponseContainerContext(GetRemoteConnectionResponse.class);

	// END_GENERATED_CODE
}
