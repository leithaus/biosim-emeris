package biosim.client.messages.model;

import biosim.client.model.Uid;

public class MConnection extends MNode {

	private BlobRef _icon;
	
	public MConnection() {
	}
	
	public MConnection(Uid _uid, String _name, BlobRef _icon) {
		this(_uid, _name);
		this._icon = _icon;
	}
	
	public MConnection(Uid _uid, String _name) {
		super(_uid, _name);
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
	@Override
	public String toString() {
	    return m3.gwt.props.ToStringBuilder.toString(this, Context);
	}
	public static class MConnectionContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext icon = new m3.gwt.props.impl.AbstractPropertyContext<MConnection,biosim.client.messages.model.BlobRef>(this, "icon", biosim.client.messages.model.BlobRef.class, 0, null, false) {
	    	    protected biosim.client.messages.model.BlobRef getImpl(MConnection bean) { return bean.getIcon(); }
	    	    protected void setImpl(MConnection bean, biosim.client.messages.model.BlobRef value ) { bean.setIcon(value);}
	    };
	    public m3.gwt.props.PropertyContext uid = MNode.Context.uid;
	    public m3.gwt.props.PropertyContext name = MNode.Context.name;
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("biosim.client.messages.model.MConnection");
	        set = set.insert("biosim.client.messages.model.MNode");
	        set = set.insert("java.lang.Object");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.uid);
	        list = list.cons(this.name);
	        list = list.cons(this.icon);
	        return list;
	    }
	    public MConnection newInstance() {
	        return new MConnection();
	    }
	    MConnectionContainerContext(Class<?> actualClass) {
	        super(biosim.client.messages.model.MConnection.class, MConnection.Context);
	    }
	}
	public static final MConnectionContainerContext Context = new MConnectionContainerContext(MConnection.class);

	// END_GENERATED_CODE
}
