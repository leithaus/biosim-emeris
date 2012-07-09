package biosim.client.messages.model;

import m3.gwt.props.ApplyCodeGeneration;
import biosim.client.model.Uid;

@ApplyCodeGeneration
public class MConnection {

	private Uid _uid;
	private String _name;
	private BlobRef _icon;
	
	public MConnection() {
	}
	
	public MConnection(Uid _uid, String _name, BlobRef _icon) {
		this._uid = _uid;
		this._name = _name;
		this._icon = _icon;
	}
	
	public MConnection(Uid _uid, String _name) {
		this._uid = _uid;
		this._name = _name;
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
	public java.lang.String getName() {
	    return _name;
	}
	public void setName(java.lang.String name0) {
	    _setName(name0);
	}
	protected void _setName(java.lang.String name0) {
	    java.lang.String before = _name;
	     _name = name0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.name, before, name0);
	}
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
	    public m3.gwt.props.PropertyContext uid = new m3.gwt.props.impl.AbstractPropertyContext<MConnection,biosim.client.model.Uid>(this, "uid", biosim.client.model.Uid.class, 0, null, false) {
	    	    protected biosim.client.model.Uid getImpl(MConnection bean) { return bean.getUid(); }
	    	    protected void setImpl(MConnection bean, biosim.client.model.Uid value ) { bean.setUid(value);}
	    };
	    public m3.gwt.props.PropertyContext name = new m3.gwt.props.impl.AbstractPropertyContext<MConnection,java.lang.String>(this, "name", java.lang.String.class, 1, null, false) {
	    	    protected java.lang.String getImpl(MConnection bean) { return bean.getName(); }
	    	    protected void setImpl(MConnection bean, java.lang.String value ) { bean.setName(value);}
	    };
	    public m3.gwt.props.PropertyContext icon = new m3.gwt.props.impl.AbstractPropertyContext<MConnection,biosim.client.messages.model.BlobRef>(this, "icon", biosim.client.messages.model.BlobRef.class, 2, null, false) {
	    	    protected biosim.client.messages.model.BlobRef getImpl(MConnection bean) { return bean.getIcon(); }
	    	    protected void setImpl(MConnection bean, biosim.client.messages.model.BlobRef value ) { bean.setIcon(value);}
	    };
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("biosim.client.messages.model.MConnection");
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
	        super(biosim.client.messages.model.MConnection.class);
	    }
	}
	public static final MConnectionContainerContext Context = new MConnectionContainerContext(MConnection.class);

	// END_GENERATED_CODE
}
