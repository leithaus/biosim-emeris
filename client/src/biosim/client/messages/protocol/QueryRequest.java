package biosim.client.messages.protocol;

import java.util.List;

import m3.gwt.props.ApplyCodeGeneration;
import biosim.client.messages.model.Uid;

@ApplyCodeGeneration
public class QueryRequest extends RequestBody {

	private List<Uid> _labels;
	private List<Uid> _connections;
	
	// BEGIN_GENERATED_CODE
	
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
	public static class QueryRequestContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext labels = new m3.gwt.props.impl.AbstractPropertyContext<QueryRequest,java.util.List<biosim.client.messages.model.Uid>>(this, "labels", java.util.List.class, 0, biosim.client.messages.model.Uid.class, false) {
	    	    protected java.util.List<biosim.client.messages.model.Uid> getImpl(QueryRequest bean) { return bean.getLabels(); }
	    	    protected void setImpl(QueryRequest bean, java.util.List<biosim.client.messages.model.Uid> value ) { bean.setLabels(value);}
	    };
	    public m3.gwt.props.PropertyContext connections = new m3.gwt.props.impl.AbstractPropertyContext<QueryRequest,java.util.List<biosim.client.messages.model.Uid>>(this, "connections", java.util.List.class, 1, biosim.client.messages.model.Uid.class, false) {
	    	    protected java.util.List<biosim.client.messages.model.Uid> getImpl(QueryRequest bean) { return bean.getConnections(); }
	    	    protected void setImpl(QueryRequest bean, java.util.List<biosim.client.messages.model.Uid> value ) { bean.setConnections(value);}
	    };
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("biosim.client.messages.protocol.RequestBody");
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.messages.protocol.QueryRequest");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.labels);
	        list = list.cons(this.connections);
	        return list;
	    }
	    public QueryRequest newInstance() {
	        return new QueryRequest();
	    }
	    QueryRequestContainerContext(Class<?> actualClass) {
	        super(biosim.client.messages.protocol.QueryRequest.class, QueryRequest.Context);
	    }
	}
	public static final QueryRequestContainerContext Context = new QueryRequestContainerContext(QueryRequest.class);

	// END_GENERATED_CODE
}
