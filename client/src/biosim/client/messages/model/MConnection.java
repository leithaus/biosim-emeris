package biosim.client.messages.model;


public class MConnection extends MNamedNode {

	private BlobRef _icon;
	private Uid _remoteAgent;
	
	public MConnection() {
	}
	
	public MConnection(Uid _uid, String _name, BlobRef _icon, Uid _remoteAgent) {
		this._icon = _icon;
		this._remoteAgent = _remoteAgent;
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
	public biosim.client.messages.model.Uid getRemoteAgent() {
	    return _remoteAgent;
	}
	public void setRemoteAgent(biosim.client.messages.model.Uid remoteAgent0) {
	    _setRemoteAgent(remoteAgent0);
	}
	protected void _setRemoteAgent(biosim.client.messages.model.Uid remoteAgent0) {
	    biosim.client.messages.model.Uid before = _remoteAgent;
	     _remoteAgent = remoteAgent0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.remoteAgent, before, remoteAgent0);
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
	    public m3.gwt.props.PropertyContext remoteAgent = new m3.gwt.props.impl.AbstractPropertyContext<MConnection,biosim.client.messages.model.Uid>(this, "remoteAgent", biosim.client.messages.model.Uid.class, 1, null, false) {
	    	    protected biosim.client.messages.model.Uid getImpl(MConnection bean) { return bean.getRemoteAgent(); }
	    	    protected void setImpl(MConnection bean, biosim.client.messages.model.Uid value ) { bean.setRemoteAgent(value);}
	    };
	    public m3.gwt.props.PropertyContext name = MNamedNode.Context.name;
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("biosim.client.messages.model.MConnection");
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.messages.model.MNode");
	        set = set.insert("biosim.client.messages.model.MNamedNode");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.name);
	        list = list.cons(this.icon);
	        list = list.cons(this.remoteAgent);
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
