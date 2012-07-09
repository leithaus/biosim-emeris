package biosim.client.messages.model;

import biosim.client.model.Uid;

public class MAgent extends MNode {

	private Uid _rootLabel;
	
	// BEGIN_GENERATED_CODE
	
	public biosim.client.model.Uid getRootLabel() {
	    return _rootLabel;
	}
	public void setRootLabel(biosim.client.model.Uid rootLabel0) {
	    _setRootLabel(rootLabel0);
	}
	protected void _setRootLabel(biosim.client.model.Uid rootLabel0) {
	    biosim.client.model.Uid before = _rootLabel;
	     _rootLabel = rootLabel0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.rootLabel, before, rootLabel0);
	}
	@Override
	public String toString() {
	    return m3.gwt.props.ToStringBuilder.toString(this, Context);
	}
	public static class MAgentContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext rootLabel = new m3.gwt.props.impl.AbstractPropertyContext<MAgent,biosim.client.model.Uid>(this, "rootLabel", biosim.client.model.Uid.class, 0, null, false) {
	    	    protected biosim.client.model.Uid getImpl(MAgent bean) { return bean.getRootLabel(); }
	    	    protected void setImpl(MAgent bean, biosim.client.model.Uid value ) { bean.setRootLabel(value);}
	    };
	    public m3.gwt.props.PropertyContext uid = MNode.Context.uid;
	    public m3.gwt.props.PropertyContext name = MNode.Context.name;
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("biosim.client.messages.model.MNode");
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.messages.model.MAgent");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.uid);
	        list = list.cons(this.name);
	        list = list.cons(this.rootLabel);
	        return list;
	    }
	    public MAgent newInstance() {
	        return new MAgent();
	    }
	    MAgentContainerContext(Class<?> actualClass) {
	        super(biosim.client.messages.model.MAgent.class, MAgent.Context);
	    }
	}
	public static final MAgentContainerContext Context = new MAgentContainerContext(MAgent.class);

	// END_GENERATED_CODE
}
