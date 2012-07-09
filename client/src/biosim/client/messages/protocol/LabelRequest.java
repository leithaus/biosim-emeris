package biosim.client.messages.protocol;

import m3.gwt.props.ApplyCodeGeneration;
import biosim.client.model.Uid;

@ApplyCodeGeneration
public class LabelRequest extends RequestBody {

	private Uid _parent;
	
	public LabelRequest() {
	}

	public LabelRequest(Uid parent) {
		_parent = parent;
	}
	
	// BEGIN_GENERATED_CODE
	
	public biosim.client.model.Uid getParent() {
	    return _parent;
	}
	public void setParent(biosim.client.model.Uid parent0) {
	    _setParent(parent0);
	}
	protected void _setParent(biosim.client.model.Uid parent0) {
	    biosim.client.model.Uid before = _parent;
	     _parent = parent0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.parent, before, parent0);
	}
	@Override
	public String toString() {
	    return m3.gwt.props.ToStringBuilder.toString(this, Context);
	}
	public static class LabelRequestContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext parent = new m3.gwt.props.impl.AbstractPropertyContext<LabelRequest,biosim.client.model.Uid>(this, "parent", biosim.client.model.Uid.class, 0, null, false) {
	    	    protected biosim.client.model.Uid getImpl(LabelRequest bean) { return bean.getParent(); }
	    	    protected void setImpl(LabelRequest bean, biosim.client.model.Uid value ) { bean.setParent(value);}
	    };
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("biosim.client.messages.protocol.RequestBody");
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.messages.protocol.LabelRequest");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.parent);
	        return list;
	    }
	    public LabelRequest newInstance() {
	        return new LabelRequest();
	    }
	    LabelRequestContainerContext(Class<?> actualClass) {
	        super(biosim.client.messages.protocol.LabelRequest.class, LabelRequest.Context);
	    }
	}
	public static final LabelRequestContainerContext Context = new LabelRequestContainerContext(LabelRequest.class);

	// END_GENERATED_CODE
}
