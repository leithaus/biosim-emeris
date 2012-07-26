package biosim.client.messages.protocol;

import java.util.List;

import biosim.client.messages.model.FilterAcceptCriteria;
import biosim.client.messages.model.Uid;

import m3.gwt.props.ApplyCodeGeneration;

@ApplyCodeGeneration
public class QueryResponse extends ResponseBody {

	private Uid _queryUid;
	private List<FilterAcceptCriteria> _results;

	private QueryResponse() {
	}
	
	public QueryResponse(Uid _queryUid, List<FilterAcceptCriteria> _results) {
		super();
		this._queryUid = _queryUid;
		this._results = _results;
	}

	// BEGIN_GENERATED_CODE
	
	public java.util.List<biosim.client.messages.model.FilterAcceptCriteria> getResults() {
	    return _results;
	}
	public void setResults(java.util.List<biosim.client.messages.model.FilterAcceptCriteria> results0) {
	    _setResults(results0);
	}
	protected void _setResults(java.util.List<biosim.client.messages.model.FilterAcceptCriteria> results0) {
	    java.util.List<biosim.client.messages.model.FilterAcceptCriteria> before = _results;
	     _results = results0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.results, before, results0);
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
	public static class QueryResponseContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext results = new m3.gwt.props.impl.AbstractPropertyContext<QueryResponse,java.util.List<biosim.client.messages.model.FilterAcceptCriteria>>(this, "results", java.util.List.class, 0, biosim.client.messages.model.FilterAcceptCriteria.class, false) {
	    	    protected java.util.List<biosim.client.messages.model.FilterAcceptCriteria> getImpl(QueryResponse bean) { return bean.getResults(); }
	    	    protected void setImpl(QueryResponse bean, java.util.List<biosim.client.messages.model.FilterAcceptCriteria> value ) { bean.setResults(value);}
	    };
	    public m3.gwt.props.PropertyContext queryUid = new m3.gwt.props.impl.AbstractPropertyContext<QueryResponse,biosim.client.messages.model.Uid>(this, "queryUid", biosim.client.messages.model.Uid.class, 1, null, false) {
	    	    protected biosim.client.messages.model.Uid getImpl(QueryResponse bean) { return bean.getQueryUid(); }
	    	    protected void setImpl(QueryResponse bean, biosim.client.messages.model.Uid value ) { bean.setQueryUid(value);}
	    };
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("biosim.client.messages.protocol.QueryResponse");
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.messages.protocol.ResponseBody");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.results);
	        list = list.cons(this.queryUid);
	        return list;
	    }
	    public QueryResponse newInstance() {
	        return new QueryResponse();
	    }
	    QueryResponseContainerContext(Class<?> actualClass) {
	        super(biosim.client.messages.protocol.QueryResponse.class, QueryResponse.Context);
	    }
	}
	public static final QueryResponseContainerContext Context = new QueryResponseContainerContext(QueryResponse.class);

	// END_GENERATED_CODE
}
