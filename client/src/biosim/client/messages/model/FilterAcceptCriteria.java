package biosim.client.messages.model;

import java.util.List;

import m3.gwt.props.ApplyCodeGeneration;

@ApplyCodeGeneration
public class FilterAcceptCriteria {
	
	private Uid _node;
	private List<String> _paths;
	private List<Uid> _labels;
	private List<Uid> _connections;


	// BEGIN_GENERATED_CODE
	
	public biosim.client.messages.model.Uid getNode() {
	    return _node;
	}
	public void setNode(biosim.client.messages.model.Uid node0) {
	    _setNode(node0);
	}
	protected void _setNode(biosim.client.messages.model.Uid node0) {
	    biosim.client.messages.model.Uid before = _node;
	     _node = node0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.node, before, node0);
	}
	public java.util.List<java.lang.String> getPaths() {
	    return _paths;
	}
	public void setPaths(java.util.List<java.lang.String> paths0) {
	    _setPaths(paths0);
	}
	protected void _setPaths(java.util.List<java.lang.String> paths0) {
	    java.util.List<java.lang.String> before = _paths;
	     _paths = paths0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.paths, before, paths0);
	}
	public java.util.List<biosim.client.messages.model.Uid> getLabels() {
	    return _labels;
	}
	public void setLabels(java.util.List<biosim.client.messages.model.Uid> labels0) {
	    _setLabels(labels0);
	}
	protected void _setLabels(java.util.List<biosim.client.messages.model.Uid> labels0) {
	    java.util.List<biosim.client.messages.model.Uid> before = _labels;
	     _labels = labels0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.labels, before, labels0);
	}
	public java.util.List<biosim.client.messages.model.Uid> getConnections() {
	    return _connections;
	}
	public void setConnections(java.util.List<biosim.client.messages.model.Uid> connections0) {
	    _setConnections(connections0);
	}
	protected void _setConnections(java.util.List<biosim.client.messages.model.Uid> connections0) {
	    java.util.List<biosim.client.messages.model.Uid> before = _connections;
	     _connections = connections0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.connections, before, connections0);
	}
	@Override
	public String toString() {
	    return m3.gwt.props.ToStringBuilder.toString(this, Context);
	}
	public static class FilterAcceptCriteriaContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext node = new m3.gwt.props.impl.AbstractPropertyContext<FilterAcceptCriteria,biosim.client.messages.model.Uid>(this, "node", biosim.client.messages.model.Uid.class, 0, null, false) {
	    	    protected biosim.client.messages.model.Uid getImpl(FilterAcceptCriteria bean) { return bean.getNode(); }
	    	    protected void setImpl(FilterAcceptCriteria bean, biosim.client.messages.model.Uid value ) { bean.setNode(value);}
	    };
	    public m3.gwt.props.PropertyContext paths = new m3.gwt.props.impl.AbstractPropertyContext<FilterAcceptCriteria,java.util.List<java.lang.String>>(this, "paths", java.util.List.class, 1, java.lang.String.class, false) {
	    	    protected java.util.List<java.lang.String> getImpl(FilterAcceptCriteria bean) { return bean.getPaths(); }
	    	    protected void setImpl(FilterAcceptCriteria bean, java.util.List<java.lang.String> value ) { bean.setPaths(value);}
	    };
	    public m3.gwt.props.PropertyContext labels = new m3.gwt.props.impl.AbstractPropertyContext<FilterAcceptCriteria,java.util.List<biosim.client.messages.model.Uid>>(this, "labels", java.util.List.class, 2, biosim.client.messages.model.Uid.class, false) {
	    	    protected java.util.List<biosim.client.messages.model.Uid> getImpl(FilterAcceptCriteria bean) { return bean.getLabels(); }
	    	    protected void setImpl(FilterAcceptCriteria bean, java.util.List<biosim.client.messages.model.Uid> value ) { bean.setLabels(value);}
	    };
	    public m3.gwt.props.PropertyContext connections = new m3.gwt.props.impl.AbstractPropertyContext<FilterAcceptCriteria,java.util.List<biosim.client.messages.model.Uid>>(this, "connections", java.util.List.class, 3, biosim.client.messages.model.Uid.class, false) {
	    	    protected java.util.List<biosim.client.messages.model.Uid> getImpl(FilterAcceptCriteria bean) { return bean.getConnections(); }
	    	    protected void setImpl(FilterAcceptCriteria bean, java.util.List<biosim.client.messages.model.Uid> value ) { bean.setConnections(value);}
	    };
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("biosim.client.messages.model.FilterAcceptCriteria");
	        set = set.insert("java.lang.Object");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.node);
	        list = list.cons(this.paths);
	        list = list.cons(this.labels);
	        list = list.cons(this.connections);
	        return list;
	    }
	    public FilterAcceptCriteria newInstance() {
	        return new FilterAcceptCriteria();
	    }
	    FilterAcceptCriteriaContainerContext(Class<?> actualClass) {
	        super(biosim.client.messages.model.FilterAcceptCriteria.class);
	    }
	}
	public static final FilterAcceptCriteriaContainerContext Context = new FilterAcceptCriteriaContainerContext(FilterAcceptCriteria.class);

	// END_GENERATED_CODE
}
