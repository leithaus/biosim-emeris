package biosim.client.messages.protocol;

import m3.fj.data.FList;
import m3.gwt.props.ApplyCodeGeneration;
import biosim.client.messages.model.MNode;

@ApplyCodeGeneration
public class SelectResponse extends ResponseBody {

	private FList<MNode> _nodes;
	
	public SelectResponse() {
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
	public static class SelectResponseContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext nodes = new m3.gwt.props.impl.AbstractPropertyContext<SelectResponse,m3.fj.data.FList<biosim.client.messages.model.MNode>>(this, "nodes", m3.fj.data.FList.class, 0, biosim.client.messages.model.MNode.class, false) {
	    	    protected m3.fj.data.FList<biosim.client.messages.model.MNode> getImpl(SelectResponse bean) { return bean.getNodes(); }
	    	    protected void setImpl(SelectResponse bean, m3.fj.data.FList<biosim.client.messages.model.MNode> value ) { bean.setNodes(value);}
	    };
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.messages.protocol.SelectResponse");
	        set = set.insert("biosim.client.messages.protocol.ResponseBody");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.nodes);
	        return list;
	    }
	    public SelectResponse newInstance() {
	        return new SelectResponse();
	    }
	    SelectResponseContainerContext(Class<?> actualClass) {
	        super(biosim.client.messages.protocol.SelectResponse.class, SelectResponse.Context);
	    }
	}
	public static final SelectResponseContainerContext Context = new SelectResponseContainerContext(SelectResponse.class);

	// END_GENERATED_CODE
}
