package biosim.client.messages.protocol;

import m3.gwt.props.ApplyCodeGeneration;
import biosim.client.model.Uid;

@ApplyCodeGeneration
public class FetchRequest extends RequestBody {

	private Uid _uid;
	
	public FetchRequest() {
	}

	public FetchRequest(Uid uid) {
		_uid = uid;
	}
	
	// BEGIN_GENERATED_CODE
	
	public biosim.client.model.Uid getUid() {
	    return _uid;
	}
	public void setUid(biosim.client.model.Uid uid0) {
	    _setUid(uid0);
	}
	protected void _setUid(biosim.client.model.Uid uid0) {
	    biosim.client.model.Uid before = _uid;
	     _uid = uid0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.uid, before, uid0);
	}
	@Override
	public String toString() {
	    return m3.gwt.props.ToStringBuilder.toString(this, Context);
	}
	public static class FetchRequestContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext uid = new m3.gwt.props.impl.AbstractPropertyContext<FetchRequest,biosim.client.model.Uid>(this, "uid", biosim.client.model.Uid.class, 0, null, false) {
	    	    protected biosim.client.model.Uid getImpl(FetchRequest bean) { return bean.getUid(); }
	    	    protected void setImpl(FetchRequest bean, biosim.client.model.Uid value ) { bean.setUid(value);}
	    };
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("biosim.client.messages.protocol.FetchRequest");
	        set = set.insert("biosim.client.messages.protocol.RequestBody");
	        set = set.insert("java.lang.Object");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.uid);
	        return list;
	    }
	    public FetchRequest newInstance() {
	        return new FetchRequest();
	    }
	    FetchRequestContainerContext(Class<?> actualClass) {
	        super(biosim.client.messages.protocol.FetchRequest.class, FetchRequest.Context);
	    }
	}
	public static final FetchRequestContainerContext Context = new FetchRequestContainerContext(FetchRequest.class);

	// END_GENERATED_CODE
}
