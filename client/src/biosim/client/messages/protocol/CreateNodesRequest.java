package biosim.client.messages.protocol;

import java.util.List;

import m3.gwt.lang.ListX;
import m3.gwt.props.ApplyCodeGeneration;
import biosim.client.model.Node;

@ApplyCodeGeneration
public class CreateNodesRequest extends RequestBody {
	
	private List<Node> _nodes;
	
	public CreateNodesRequest	(Node...nodes) {
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
	public static class CreateNodesRequestContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext nodes = new m3.gwt.props.impl.AbstractPropertyContext<CreateNodesRequest,java.util.List<biosim.client.model.Node>>(this, "nodes", java.util.List.class, 0, biosim.client.model.Node.class, false) {
	    	    protected java.util.List<biosim.client.model.Node> getImpl(CreateNodesRequest bean) { return bean.getNodes(); }
	    	    protected void setImpl(CreateNodesRequest bean, java.util.List<biosim.client.model.Node> value ) { bean.setNodes(value);}
	    };
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("biosim.client.messages.protocol.RequestBody");
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.messages.protocol.CreateNodesRequest");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.nodes);
	        return list;
	    }
	    public CreateNodesRequest newInstance() {
	        return new CreateNodesRequest();
	    }
	    CreateNodesRequestContainerContext(Class<?> actualClass) {
	        super(biosim.client.messages.protocol.CreateNodesRequest.class, CreateNodesRequest.Context);
	    }
	}
	public static final CreateNodesRequestContainerContext Context = new CreateNodesRequestContainerContext(CreateNodesRequest.class);

	// END_GENERATED_CODE
}
