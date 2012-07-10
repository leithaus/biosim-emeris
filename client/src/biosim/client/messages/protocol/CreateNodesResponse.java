package biosim.client.messages.protocol;

import java.util.List;

import m3.gwt.lang.ListX;
import m3.gwt.props.ApplyCodeGeneration;
import biosim.client.model.Node;

@ApplyCodeGeneration
public class CreateNodesResponse extends ResponseBody {
	
	private List<Node> _nodes;
	
	public CreateNodesResponse	(Node...nodes) {
		_nodes = ListX.create();
		for ( Node n : nodes ) {
			_nodes.add(n);
		}
	}

	// BEGIN_GENERATED_CODE
	
	public java.util.List<biosim.client.model.Node> getNodes() {
	    return _nodes;
	}
	public void setNodes(java.util.List<biosim.client.model.Node> nodes0) {
	    _setNodes(nodes0);
	}
	protected void _setNodes(java.util.List<biosim.client.model.Node> nodes0) {
	    java.util.List<biosim.client.model.Node> before = _nodes;
	     _nodes = nodes0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.nodes, before, nodes0);
	}
	@Override
	public String toString() {
	    return m3.gwt.props.ToStringBuilder.toString(this, Context);
	}
	public static class CreateNodesResponseContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext nodes = new m3.gwt.props.impl.AbstractPropertyContext<CreateNodesResponse,java.util.List<biosim.client.model.Node>>(this, "nodes", java.util.List.class, 0, biosim.client.model.Node.class, false) {
	    	    protected java.util.List<biosim.client.model.Node> getImpl(CreateNodesResponse bean) { return bean.getNodes(); }
	    	    protected void setImpl(CreateNodesResponse bean, java.util.List<biosim.client.model.Node> value ) { bean.setNodes(value);}
	    };
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("biosim.client.messages.protocol.CreateNodesResponse");
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.messages.protocol.ResponseBody");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.nodes);
	        return list;
	    }
	    public CreateNodesResponse newInstance() {
	        return new CreateNodesResponse();
	    }
	    CreateNodesResponseContainerContext(Class<?> actualClass) {
	        super(biosim.client.messages.protocol.CreateNodesResponse.class, CreateNodesResponse.Context);
	    }
	}
	public static final CreateNodesResponseContainerContext Context = new CreateNodesResponseContainerContext(CreateNodesResponse.class);

	// END_GENERATED_CODE
}
