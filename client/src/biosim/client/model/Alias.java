package biosim.client.model;

import m3.gwt.lang.Utils;
import biosim.client.eventlist.ObservableList;
import biosim.client.eventlist.Observables;


public class Alias extends Node {

	public static String JsonClass = "Alias";

	private String _name;
	private Uid _agent;
	private String _iconUrl;

	private Alias() {
	}

	public Person getAgentNode() {
		return (Person) getDataSet().getNode(_agent);
	}
	
	public String getVisualId() {
		return _name;
	}

	public String getIconUrl() {
		return Utils.buildUrl(_iconUrl);
	}
	
	public String getName() {
		return _name;
	}
	
	@Override
	public String toString() {
		return _name;
	}

	public ObservableList<Node> getConnections() {
		final ObservableList<Node> list = Observables.create();
		visitDescendants(new NodeVisitor() {
			@Override
			public void visit(Node node) {
				if ( node instanceof Alias && !list.contains(node) ) {
					list.add(node);
				}
			}
		});
		return list;
	}

	public ObservableList<Node> getAllContent() {
		final ObservableList<Node> list = Observables.create();
		visitDescendants(new NodeVisitor() {
			@Override
			public void visit(Node node) {
				if ( !list.contains(node) && !(node instanceof Alias) ) {
					list.add(node);
				}
			}
		});
		return list;
	}

	// BEGIN_GENERATED_CODE
	
	public void setName(java.lang.String name0) {
	    _setName(name0);
	}
	protected void _setName(java.lang.String name0) {
	    java.lang.String before = _name;
	     _name = name0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.name, before, name0);
	}
	public biosim.client.model.Uid getAgent() {
	    return _agent;
	}
	public void setAgent(biosim.client.model.Uid agent0) {
	    _setAgent(agent0);
	}
	protected void _setAgent(biosim.client.model.Uid agent0) {
	    biosim.client.model.Uid before = _agent;
	     _agent = agent0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.agent, before, agent0);
	}
	public void setIconUrl(java.lang.String iconUrl0) {
	    _setIconUrl(iconUrl0);
	}
	protected void _setIconUrl(java.lang.String iconUrl0) {
	    java.lang.String before = _iconUrl;
	     _iconUrl = iconUrl0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.iconUrl, before, iconUrl0);
	}
	public static class AliasContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext name = new m3.gwt.props.impl.AbstractPropertyContext<Alias,java.lang.String>(this, "name", java.lang.String.class, 0, null, false) {
	    	    protected java.lang.String getImpl(Alias bean) { return bean.getName(); }
	    	    protected void setImpl(Alias bean, java.lang.String value ) { bean.setName(value);}
	    };
	    public m3.gwt.props.PropertyContext agent = new m3.gwt.props.impl.AbstractPropertyContext<Alias,biosim.client.model.Uid>(this, "agent", biosim.client.model.Uid.class, 1, null, false) {
	    	    protected biosim.client.model.Uid getImpl(Alias bean) { return bean.getAgent(); }
	    	    protected void setImpl(Alias bean, biosim.client.model.Uid value ) { bean.setAgent(value);}
	    };
	    public m3.gwt.props.PropertyContext iconUrl = new m3.gwt.props.impl.AbstractPropertyContext<Alias,java.lang.String>(this, "iconUrl", java.lang.String.class, 2, null, false) {
	    	    protected java.lang.String getImpl(Alias bean) { return bean.getIconUrl(); }
	    	    protected void setImpl(Alias bean, java.lang.String value ) { bean.setIconUrl(value);}
	    };
	    public m3.gwt.props.PropertyContext uid = Node.Context.uid;
	    public m3.gwt.props.PropertyContext created = Node.Context.created;
	    public m3.gwt.props.PropertyContext dataSet = Node.Context.dataSet;
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.model.Node");
	        set = set.insert("biosim.client.model.Alias");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.uid);
	        list = list.cons(this.created);
	        list = list.cons(this.dataSet);
	        list = list.cons(this.name);
	        list = list.cons(this.agent);
	        list = list.cons(this.iconUrl);
	        return list;
	    }
	    public Alias newInstance() {
	        return new Alias();
	    }
	    AliasContainerContext(Class<?> actualClass) {
	        super(biosim.client.model.Alias.class, Alias.Context);
	    }
	}
	public static final AliasContainerContext Context = new AliasContainerContext(Alias.class);

	// END_GENERATED_CODE
}
