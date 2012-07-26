package biosim.client.messages.protocol;

import java.util.List;

import m3.gwt.lang.ListX;
import m3.gwt.props.ApplyCodeGeneration;
import biosim.client.messages.model.MNode;
import biosim.client.messages.model.Uid;

@ApplyCodeGeneration
public class QueryRequest extends ConnectionScopedRequestBody {

	private List<Uid> _nodes = ListX.create();
	private Uid _queryUid;
	
	
	private QueryRequest() {
	}
	
	public QueryRequest(Uid queryUid, Iterable<MNode> nodes) {
		_queryUid = queryUid;
		for ( MNode n : nodes ) {
			_nodes.add(n.getUid());
		}
	}
	
	// BEGIN_GENERATED_CODE
	
	public java.util.List<biosim.client.messages.model.Uid> getNodes() {
	    return _nodes;
	}
	public void setNodes(java.util.List<biosim.client.messages.model.Uid> nodes0) {
	    _setNodes(nodes0);
	}
	protected void _setNodes(java.util.List<biosim.client.messages.model.Uid> nodes0) {
	    java.util.List<biosim.client.messages.model.Uid> before = _nodes;
	     _nodes = nodes0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.nodes, before, nodes0);
	}
	public biosim.client.messages.model.Uid getQueryUid() {
	    return _queryUid;
	}
	public void setQueryUid(biosim.client.messages.model.Uid queryUid0) {
	    _setQueryUid(queryUid0);
	}
	protected void _setQueryUid(biosim.client.messages.model.Uid queryUid0) {
	    biosim.client.messages.model.Uid before = _queryUid;
	     _queryUid = queryUid0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.queryUid, before, queryUid0);
	}
	@Override
	public String toString() {
	    return m3.gwt.props.ToStringBuilder.toString(this, Context);
	}
	public static class QueryRequestContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext nodes = new m3.gwt.props.impl.AbstractPropertyContext<QueryRequest,java.util.List<biosim.client.messages.model.Uid>>(this, "nodes", java.util.List.class, 0, biosim.client.messages.model.Uid.class, false) {
	    	    protected java.util.List<biosim.client.messages.model.Uid> getImpl(QueryRequest bean) { return bean.getNodes(); }
	    	    protected void setImpl(QueryRequest bean, java.util.List<biosim.client.messages.model.Uid> value ) { bean.setNodes(value);}
	    };
	    public m3.gwt.props.PropertyContext queryUid = new m3.gwt.props.impl.AbstractPropertyContext<QueryRequest,biosim.client.messages.model.Uid>(this, "queryUid", biosim.client.messages.model.Uid.class, 1, null, false) {
	    	    protected biosim.client.messages.model.Uid getImpl(QueryRequest bean) { return bean.getQueryUid(); }
	    	    protected void setImpl(QueryRequest bean, biosim.client.messages.model.Uid value ) { bean.setQueryUid(value);}
	    };
	    public m3.gwt.props.PropertyContext connectionUid = ConnectionScopedRequestBody.Context.connectionUid;
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("biosim.client.messages.protocol.RequestBody");
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.messages.protocol.QueryRequest");
	        set = set.insert("biosim.client.messages.protocol.ConnectionScopedRequestBody");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.nodes);
	        list = list.cons(this.queryUid);
	        list = list.cons(this.connectionUid);
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
