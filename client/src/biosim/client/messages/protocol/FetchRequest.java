package biosim.client.messages.protocol;

import m3.fj.data.FList;
import m3.gwt.props.ApplyCodeGeneration;
import biosim.client.messages.model.Uid;

@ApplyCodeGeneration
public class FetchRequest extends RequestBody {

	private FList<Uid> _uids = FList.nil();
	
	public FetchRequest() {
	}

	public FetchRequest(Uid uid) {
		_uids = _uids.cons(uid);
	}

	public FetchRequest(Iterable<Uid> uids) {
		for ( Uid uid : uids ) {
			_uids = _uids.cons(uid);
		}
	}

	public FetchRequest(Uid...uids) {
		for ( Uid uid : uids ) {
			_uids = _uids.cons(uid);
		}
	}
	
	// BEGIN_GENERATED_CODE
	
	public m3.fj.data.FList<biosim.client.messages.model.Uid> getUids() {
	    return _uids;
	}
	public void setUids(m3.fj.data.FList<biosim.client.messages.model.Uid> uids0) {
	    _setUids(uids0);
	}
	protected void _setUids(m3.fj.data.FList<biosim.client.messages.model.Uid> uids0) {
	    m3.fj.data.FList<biosim.client.messages.model.Uid> before = _uids;
	     _uids = uids0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.uids, before, uids0);
	}
	@Override
	public String toString() {
	    return m3.gwt.props.ToStringBuilder.toString(this, Context);
	}
	public static class FetchRequestContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext uids = new m3.gwt.props.impl.AbstractPropertyContext<FetchRequest,m3.fj.data.FList<biosim.client.messages.model.Uid>>(this, "uids", m3.fj.data.FList.class, 0, biosim.client.messages.model.Uid.class, false) {
	    	    protected m3.fj.data.FList<biosim.client.messages.model.Uid> getImpl(FetchRequest bean) { return bean.getUids(); }
	    	    protected void setImpl(FetchRequest bean, m3.fj.data.FList<biosim.client.messages.model.Uid> value ) { bean.setUids(value);}
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
	        list = list.cons(this.uids);
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
