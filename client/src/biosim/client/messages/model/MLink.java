package biosim.client.messages.model;

public class MLink extends MNode {

	private Uid _from;
	private Uid _to;
	
	private MLink() {
	}
	
	public MLink(Uid from, Uid to) {
		_from = from;
		_to = to;
	}
	
	public MLink(MNode from, MNode to) {
		this(from.getUid(), to.getUid());
	}
	
	@Override
	public String toString() {
		return "Link(" + _from + ", " + _to + ")";
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
	public static class MLinkContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext from = new m3.gwt.props.impl.AbstractPropertyContext<MLink,biosim.client.messages.model.Uid>(this, "from", biosim.client.messages.model.Uid.class, 0, null, false) {
	    	    protected biosim.client.messages.model.Uid getImpl(MLink bean) { return bean.getFrom(); }
	    	    protected void setImpl(MLink bean, biosim.client.messages.model.Uid value ) { bean.setFrom(value);}
	    };
	    public m3.gwt.props.PropertyContext to = new m3.gwt.props.impl.AbstractPropertyContext<MLink,biosim.client.messages.model.Uid>(this, "to", biosim.client.messages.model.Uid.class, 1, null, false) {
	    	    protected biosim.client.messages.model.Uid getImpl(MLink bean) { return bean.getTo(); }
	    	    protected void setImpl(MLink bean, biosim.client.messages.model.Uid value ) { bean.setTo(value);}
	    };
	    public m3.gwt.props.PropertyContext uid = MNode.Context.uid;
	    public m3.gwt.props.PropertyContext linkHints = MNode.Context.linkHints;
	    public m3.gwt.props.PropertyContext agentServices = MNode.Context.agentServices;
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("biosim.client.messages.model.MNode");
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.messages.model.MLink");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.from);
	        list = list.cons(this.to);
	        list = list.cons(this.uid);
	        list = list.cons(this.linkHints);
	        list = list.cons(this.agentServices);
	        return list;
	    }
	    public MLink newInstance() {
	        return new MLink();
	    }
	    MLinkContainerContext(Class<?> actualClass) {
	        super(biosim.client.messages.model.MLink.class, MLink.Context);
	    }
	}
	public static final MLinkContainerContext Context = new MLinkContainerContext(MLink.class);

	// END_GENERATED_CODE
}
