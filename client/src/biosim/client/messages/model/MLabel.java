package biosim.client.messages.model;

import java.util.List;

import biosim.client.model.Uid;

public class MLabel extends MNode {

	private BlobRef _icon;
	private List<Uid> _children;
	
	// BEGIN_GENERATED_CODE
	
	public biosim.client.messages.model.BlobRef getIcon() {
	    return _icon;
	}
	public void setIcon(biosim.client.messages.model.BlobRef icon0) {
	    _setIcon(icon0);
	}
	protected void _setIcon(biosim.client.messages.model.BlobRef icon0) {
	    biosim.client.messages.model.BlobRef before = _icon;
	     _icon = icon0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.icon, before, icon0);
	}
	public java.util.List<biosim.client.model.Uid> getChildren() {
	    return _children;
	}
	public void setChildren(java.util.List<biosim.client.model.Uid> children0) {
	    _setChildren(children0);
	}
	protected void _setChildren(java.util.List<biosim.client.model.Uid> children0) {
	    java.util.List<biosim.client.model.Uid> before = _children;
	     _children = children0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.children, before, children0);
	}
	@Override
	public String toString() {
	    return m3.gwt.props.ToStringBuilder.toString(this, Context);
	}
	public static class MLabelContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext icon = new m3.gwt.props.impl.AbstractPropertyContext<MLabel,biosim.client.messages.model.BlobRef>(this, "icon", biosim.client.messages.model.BlobRef.class, 0, null, false) {
	    	    protected biosim.client.messages.model.BlobRef getImpl(MLabel bean) { return bean.getIcon(); }
	    	    protected void setImpl(MLabel bean, biosim.client.messages.model.BlobRef value ) { bean.setIcon(value);}
	    };
	    public m3.gwt.props.PropertyContext children = new m3.gwt.props.impl.AbstractPropertyContext<MLabel,java.util.List<biosim.client.model.Uid>>(this, "children", java.util.List.class, 1, biosim.client.model.Uid.class, false) {
	    	    protected java.util.List<biosim.client.model.Uid> getImpl(MLabel bean) { return bean.getChildren(); }
	    	    protected void setImpl(MLabel bean, java.util.List<biosim.client.model.Uid> value ) { bean.setChildren(value);}
	    };
	    public m3.gwt.props.PropertyContext uid = MNode.Context.uid;
	    public m3.gwt.props.PropertyContext name = MNode.Context.name;
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("biosim.client.messages.model.MNode");
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.messages.model.MLabel");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.uid);
	        list = list.cons(this.name);
	        list = list.cons(this.icon);
	        list = list.cons(this.children);
	        return list;
	    }
	    public MLabel newInstance() {
	        return new MLabel();
	    }
	    MLabelContainerContext(Class<?> actualClass) {
	        super(biosim.client.messages.model.MLabel.class, MLabel.Context);
	    }
	}
	public static final MLabelContainerContext Context = new MLabelContainerContext(MLabel.class);

	// END_GENERATED_CODE
}
