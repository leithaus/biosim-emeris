package biosim.client.messages.model;

import m3.fj.data.FList;

public class MLabel extends MNamedNode {

	private BlobRef _icon;
	private FList<Uid> _children;
	
	public MLabel() {
	}

	public MLabel(String _name) {
	}

	@Override
	public String getIconUrl() {
		return _icon.getUrl();
	}
	
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
	public m3.fj.data.FList<biosim.client.messages.model.Uid> getChildren() {
	    return _children;
	}
	public void setChildren(m3.fj.data.FList<biosim.client.messages.model.Uid> children0) {
	    _setChildren(children0);
	}
	protected void _setChildren(m3.fj.data.FList<biosim.client.messages.model.Uid> children0) {
	    m3.fj.data.FList<biosim.client.messages.model.Uid> before = _children;
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
	    public m3.gwt.props.PropertyContext children = new m3.gwt.props.impl.AbstractPropertyContext<MLabel,m3.fj.data.FList<biosim.client.messages.model.Uid>>(this, "children", m3.fj.data.FList.class, 1, biosim.client.messages.model.Uid.class, false) {
	    	    protected m3.fj.data.FList<biosim.client.messages.model.Uid> getImpl(MLabel bean) { return bean.getChildren(); }
	    	    protected void setImpl(MLabel bean, m3.fj.data.FList<biosim.client.messages.model.Uid> value ) { bean.setChildren(value);}
	    };
	    public m3.gwt.props.PropertyContext name = MNamedNode.Context.name;
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.messages.model.MNode");
	        set = set.insert("biosim.client.messages.model.MLabel");
	        set = set.insert("biosim.client.messages.model.MNamedNode");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
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
