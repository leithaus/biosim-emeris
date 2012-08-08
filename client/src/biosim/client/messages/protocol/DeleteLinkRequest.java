package biosim.client.messages.protocol;

import m3.gwt.props.ApplyCodeGeneration;
import biosim.client.messages.model.Uid;

@ApplyCodeGeneration
public class DeleteLinkRequest extends RequestBody {
	
	private Uid _from;
	private Uid _to;
	
	private DeleteLinkRequest() {
	}
	
	public DeleteLinkRequest(Uid from, Uid to) {
		super();
		_from = from;
		_to = to;
	}
	
	// BEGIN_GENERATED_CODE
	
	public biosim.client.messages.model.Uid getFrom() {
	    return _from;
	}
	public void setFrom(biosim.client.messages.model.Uid from0) {
	    _setFrom(from0);
	}
	protected void _setFrom(biosim.client.messages.model.Uid from0) {
	    biosim.client.messages.model.Uid before = _from;
	     _from = from0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.from, before, from0);
	}
	public biosim.client.messages.model.Uid getTo() {
	    return _to;
	}
	public void setTo(biosim.client.messages.model.Uid to0) {
	    _setTo(to0);
	}
	protected void _setTo(biosim.client.messages.model.Uid to0) {
	    biosim.client.messages.model.Uid before = _to;
	     _to = to0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.to, before, to0);
	}
	@Override
	public String toString() {
	    return m3.gwt.props.ToStringBuilder.toString(this, Context);
	}
	public static class DeleteLinkRequestContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext from = new m3.gwt.props.impl.AbstractPropertyContext<DeleteLinkRequest,biosim.client.messages.model.Uid>(this, "from", biosim.client.messages.model.Uid.class, 0, null, false) {
	    	    protected biosim.client.messages.model.Uid getImpl(DeleteLinkRequest bean) { return bean.getFrom(); }
	    	    protected void setImpl(DeleteLinkRequest bean, biosim.client.messages.model.Uid value ) { bean.setFrom(value);}
	    };
	    public m3.gwt.props.PropertyContext to = new m3.gwt.props.impl.AbstractPropertyContext<DeleteLinkRequest,biosim.client.messages.model.Uid>(this, "to", biosim.client.messages.model.Uid.class, 1, null, false) {
	    	    protected biosim.client.messages.model.Uid getImpl(DeleteLinkRequest bean) { return bean.getTo(); }
	    	    protected void setImpl(DeleteLinkRequest bean, biosim.client.messages.model.Uid value ) { bean.setTo(value);}
	    };
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("biosim.client.messages.protocol.RequestBody");
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.messages.protocol.DeleteLinkRequest");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.from);
	        list = list.cons(this.to);
	        return list;
	    }
	    public DeleteLinkRequest newInstance() {
	        return new DeleteLinkRequest();
	    }
	    DeleteLinkRequestContainerContext(Class<?> actualClass) {
	        super(biosim.client.messages.protocol.DeleteLinkRequest.class, DeleteLinkRequest.Context);
	    }
	}
	public static final DeleteLinkRequestContainerContext Context = new DeleteLinkRequestContainerContext(DeleteLinkRequest.class);

	// END_GENERATED_CODE
}
