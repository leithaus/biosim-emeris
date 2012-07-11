package biosim.client.messages.protocol;

import m3.fj.data.FList;
import biosim.client.messages.model.Uid;



public class RootLabelsResponse extends ResponseBody {

	private FList<Uid> _uids = FList.nil();	

	public RootLabelsResponse() {
	}
	
	public RootLabelsResponse(FList<Uid> _uids) {
		this._uids = _uids;
	}
	
	public void addUid(Uid uid) {
		_uids = _uids.cons(uid);
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
	public static class RootLabelsResponseContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext uids = new m3.gwt.props.impl.AbstractPropertyContext<RootLabelsResponse,m3.fj.data.FList<biosim.client.messages.model.Uid>>(this, "uids", m3.fj.data.FList.class, 0, biosim.client.messages.model.Uid.class, false) {
	    	    protected m3.fj.data.FList<biosim.client.messages.model.Uid> getImpl(RootLabelsResponse bean) { return bean.getUids(); }
	    	    protected void setImpl(RootLabelsResponse bean, m3.fj.data.FList<biosim.client.messages.model.Uid> value ) { bean.setUids(value);}
	    };
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.messages.protocol.RootLabelsResponse");
	        set = set.insert("biosim.client.messages.protocol.ResponseBody");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.uids);
	        return list;
	    }
	    public RootLabelsResponse newInstance() {
	        return new RootLabelsResponse();
	    }
	    RootLabelsResponseContainerContext(Class<?> actualClass) {
	        super(biosim.client.messages.protocol.RootLabelsResponse.class, RootLabelsResponse.Context);
	    }
	}
	public static final RootLabelsResponseContainerContext Context = new RootLabelsResponseContainerContext(RootLabelsResponse.class);

	// END_GENERATED_CODE
}
