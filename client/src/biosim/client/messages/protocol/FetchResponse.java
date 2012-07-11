package biosim.client.messages.protocol;

import m3.fj.data.FList;
import m3.gwt.props.ApplyCodeGeneration;
import biosim.client.messages.model.MNode;

@ApplyCodeGeneration
public class FetchResponse extends ResponseBody {

	private FList<MNode> _nodes = FList.nil();
	
	public FetchResponse() {
	}

	public FetchResponse(FList<MNode> nodes) {
		_nodes = nodes;
	}
	
	// BEGIN_GENERATED_CODE
	
	public m3.fj.data.FList<biosim.client.messages.model.MNode> getNodes() {
	    return _nodes;
	}
	public void setNodes(m3.fj.data.FList<biosim.client.messages.model.MNode> nodes0) {
	    _setNodes(nodes0);
	}
	protected void _setNodes(m3.fj.data.FList<biosim.client.messages.model.MNode> nodes0) {
	    m3.fj.data.FList<biosim.client.messages.model.MNode> before = _nodes;
	     _nodes = nodes0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.nodes, before, nodes0);
	}
	@Override
	public String toString() {
	    return m3.gwt.props.ToStringBuilder.toString(this, Context);
	}
	public static class FetchResponseContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext nodes = new m3.gwt.props.impl.AbstractPropertyContext<FetchResponse,m3.fj.data.FList<biosim.client.messages.model.MNode>>(this, "nodes", m3.fj.data.FList.class, 0, biosim.client.messages.model.MNode.class, false) {
	    	    protected m3.fj.data.FList<biosim.client.messages.model.MNode> getImpl(FetchResponse bean) { return bean.getNodes(); }
	    	    protected void setImpl(FetchResponse bean, m3.fj.data.FList<biosim.client.messages.model.MNode> value ) { bean.setNodes(value);}
	    };
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("biosim.client.messages.protocol.FetchResponse");
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.messages.protocol.ResponseBody");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.nodes);
	        return list;
	    }
	    public FetchResponse newInstance() {
	        return new FetchResponse();
	    }
	    FetchResponseContainerContext(Class<?> actualClass) {
	        super(biosim.client.messages.protocol.FetchResponse.class, FetchResponse.Context);
	    }
	}
	public static final FetchResponseContainerContext Context = new FetchResponseContainerContext(FetchResponse.class);

	// END_GENERATED_CODE
}
