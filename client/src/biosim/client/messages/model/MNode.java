package biosim.client.messages.model;

import java.util.List;

import m3.fj.data.FList;
import m3.gwt.lang.Function1;
import m3.gwt.lang.ListX;
import m3.gwt.props.ApplyCodeGeneration;
import biosim.client.AsyncCallback;
import biosim.client.Globals;

@ApplyCodeGeneration
public class MNode {

	// TDGlen only allocate a Uid when it is absolutely necessary
	private Uid _uid = Uid.random();
	
	private FList<Uid> _linkHints;
	
	private transient AgentServices _agentServices;
	
	public MNode() {
	}
	
	public MNode(Uid _uid) {
		this._uid = _uid;
	}

	public String getVisualId() {
		return null;
	}
	
	public void getChildren(Function1<Iterable<MNode>, Void> asyncCallback) {
		getLinks(true, asyncCallback);		
	}
	
	public void getParents(Function1<Iterable<MNode>, Void> asyncCallback) {
		getLinks(false, asyncCallback);
	}
	
	private void getLinks(final boolean children, final Function1<Iterable<MNode>, Void> asyncCallback) {
		if ( _linkHints == null ) {
			throw new RuntimeException("linkHints null for " + this + "  " + getUid());
		}
		_agentServices.fetch(_linkHints, false, new AsyncCallback<Iterable<MLink>>() {
			@Override
			public Void apply(Iterable<MLink> links) {
				List<Uid> nodesToFetch = ListX.create(); 
				for ( MLink l : links ) {
					Uid sourceUid;
					Uid targetUid;
					if ( children ) {
						sourceUid = l.getFrom();
						targetUid = l.getTo();
					} else {
						sourceUid = l.getTo();
						targetUid = l.getFrom();
					}
					if ( getUid().equals(sourceUid) ) {
						nodesToFetch.add(targetUid);
					}
				}
				_agentServices.fetch(nodesToFetch, false, asyncCallback);
				return null;
			}
		});
	}

	public String getName() {
		return null;
	}

	public String toHtmlString() {
		return null;
	}

	public String getIconUrl() {
		return null;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getUid() == null) ? 0 : getUid().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		MNode other = (MNode) obj;
		if (!getUid().equals(other.getUid())) {
			return false;
		}
		
		return true;
	}

	protected void incomingLinks(Function1<Iterable<MLink>,Void> fun) {
		
	}
	
	public boolean isEditable() {
		return _agentServices.getNodeContainer().getAgentUid().equals(Globals.get().getAgentUid());
	}
	
	public biosim.client.messages.model.Uid getUid() {
	    return _uid;
	}

	// TDGlen remove me
	public String canBeSeenBy(MConnection dropTarget) {
		return null;
	}
	// TDGlen remove me
	public boolean isParentOf(MNode node) {
		return false;
	}
	// TDGlen remove me
	public boolean hasChild(MNode node) {
		return false;
	}
	
	// BEGIN_GENERATED_CODE
	
	public void setUid(biosim.client.messages.model.Uid uid0) {
	    _setUid(uid0);
	}
	protected void _setUid(biosim.client.messages.model.Uid uid0) {
	    biosim.client.messages.model.Uid before = _uid;
	     _uid = uid0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.uid, before, uid0);
	}
	public m3.fj.data.FList<biosim.client.messages.model.Uid> getLinkHints() {
	    return _linkHints;
	}
	public void setLinkHints(m3.fj.data.FList<biosim.client.messages.model.Uid> linkHints0) {
	    _setLinkHints(linkHints0);
	}
	protected void _setLinkHints(m3.fj.data.FList<biosim.client.messages.model.Uid> linkHints0) {
	    m3.fj.data.FList<biosim.client.messages.model.Uid> before = _linkHints;
	     _linkHints = linkHints0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.linkHints, before, linkHints0);
	}
	public biosim.client.messages.model.AgentServices getAgentServices() {
	    return _agentServices;
	}
	public void setAgentServices(biosim.client.messages.model.AgentServices agentServices0) {
	    _setAgentServices(agentServices0);
	}
	protected void _setAgentServices(biosim.client.messages.model.AgentServices agentServices0) {
	    biosim.client.messages.model.AgentServices before = _agentServices;
	     _agentServices = agentServices0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.agentServices, before, agentServices0);
	}
	@Override
	public String toString() {
	    return m3.gwt.props.ToStringBuilder.toString(this, Context);
	}
	public static class MNodeContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext uid = new m3.gwt.props.impl.AbstractPropertyContext<MNode,biosim.client.messages.model.Uid>(this, "uid", biosim.client.messages.model.Uid.class, 0, null, false) {
	    	    protected biosim.client.messages.model.Uid getImpl(MNode bean) { return bean.getUid(); }
	    	    protected void setImpl(MNode bean, biosim.client.messages.model.Uid value ) { bean.setUid(value);}
	    };
	    public m3.gwt.props.PropertyContext linkHints = new m3.gwt.props.impl.AbstractPropertyContext<MNode,m3.fj.data.FList<biosim.client.messages.model.Uid>>(this, "linkHints", m3.fj.data.FList.class, 1, biosim.client.messages.model.Uid.class, false) {
	    	    protected m3.fj.data.FList<biosim.client.messages.model.Uid> getImpl(MNode bean) { return bean.getLinkHints(); }
	    	    protected void setImpl(MNode bean, m3.fj.data.FList<biosim.client.messages.model.Uid> value ) { bean.setLinkHints(value);}
	    };
	    public m3.gwt.props.PropertyContext agentServices = new m3.gwt.props.impl.AbstractPropertyContext<MNode,biosim.client.messages.model.AgentServices>(this, "agentServices", biosim.client.messages.model.AgentServices.class, 2, null, true) {
	    	    protected biosim.client.messages.model.AgentServices getImpl(MNode bean) { return bean.getAgentServices(); }
	    	    protected void setImpl(MNode bean, biosim.client.messages.model.AgentServices value ) { bean.setAgentServices(value);}
	    };
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.messages.model.MNode");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.uid);
	        list = list.cons(this.linkHints);
	        list = list.cons(this.agentServices);
	        return list;
	    }
	    public MNode newInstance() {
	        return new MNode();
	    }
	    MNodeContainerContext(Class<?> actualClass) {
	        super(biosim.client.messages.model.MNode.class);
	    }
	}
	public static final MNodeContainerContext Context = new MNodeContainerContext(MNode.class);

	// END_GENERATED_CODE
}
