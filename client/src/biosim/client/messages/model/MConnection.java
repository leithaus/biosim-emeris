package biosim.client.messages.model;


public class MConnection extends MIconNode {

	private Uid _remoteAgent;
	
	public MConnection() {
	}
	
	public MConnection(Uid _uid, String _name, BlobRef _icon, Uid _remoteAgent) {
		super(_uid, _name, _icon);
		this._remoteAgent = _remoteAgent;
	}
	
	@Override
	public String toHtmlString() {
		return getName();
	}

	@Override
	public String getDefaultIconUrl() {
		return null;
	}
	
	// BEGIN_GENERATED_CODE
	
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
	    public m3.gwt.props.PropertyContext remoteAgent = new m3.gwt.props.impl.AbstractPropertyContext<MConnection,biosim.client.messages.model.Uid>(this, "remoteAgent", biosim.client.messages.model.Uid.class, 0, null, false) {
	    	    protected biosim.client.messages.model.Uid getImpl(MConnection bean) { return bean.getRemoteAgent(); }
	    	    protected void setImpl(MConnection bean, biosim.client.messages.model.Uid value ) { bean.setRemoteAgent(value);}
	    };
	    public m3.gwt.props.PropertyContext icon = MIconNode.Context.icon;
	    public m3.gwt.props.PropertyContext name = MIconNode.Context.name;
	    public m3.gwt.props.PropertyContext uid = MIconNode.Context.uid;
	    public m3.gwt.props.PropertyContext linkHints = MIconNode.Context.linkHints;
	    public m3.gwt.props.PropertyContext agentServices = MIconNode.Context.agentServices;
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("biosim.client.messages.model.MIconNode");
	        set = set.insert("biosim.client.messages.model.MConnection");
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.messages.model.MNode");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.remoteAgent);
	        list = list.cons(this.icon);
	        list = list.cons(this.name);
	        list = list.cons(this.uid);
	        list = list.cons(this.linkHints);
	        list = list.cons(this.agentServices);
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
