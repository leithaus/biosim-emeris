package biosim.client.messages.protocol;

import java.util.List;

import m3.gwt.lang.ListX;
import m3.gwt.props.ApplyCodeGeneration;
import biosim.client.messages.model.MNode;
import biosim.client.messages.model.Uid;

@ApplyCodeGeneration
public class DeleteNodesRequest extends RequestBody {
	
	private List<Uid> _nodes;
	
	public DeleteNodesRequest(MNode...nodes) {
		_nodes = ListX.create();
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
	@Override
	public String toString() {
	    return m3.gwt.props.ToStringBuilder.toString(this, Context);
	}
	public static class DeleteNodesRequestContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext nodes = new m3.gwt.props.impl.AbstractPropertyContext<DeleteNodesRequest,java.util.List<biosim.client.messages.model.Uid>>(this, "nodes", java.util.List.class, 0, biosim.client.messages.model.Uid.class, false) {
	    	    protected java.util.List<biosim.client.messages.model.Uid> getImpl(DeleteNodesRequest bean) { return bean.getNodes(); }
	    	    protected void setImpl(DeleteNodesRequest bean, java.util.List<biosim.client.messages.model.Uid> value ) { bean.setNodes(value);}
	    };
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("biosim.client.messages.protocol.RequestBody");
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.messages.protocol.DeleteNodesRequest");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.nodes);
	        return list;
	    }
	    public DeleteNodesRequest newInstance() {
	        return new DeleteNodesRequest();
	    }
	    DeleteNodesRequestContainerContext(Class<?> actualClass) {
	        super(biosim.client.messages.protocol.DeleteNodesRequest.class, DeleteNodesRequest.Context);
	    }
	}
	public static final DeleteNodesRequestContainerContext Context = new DeleteNodesRequestContainerContext(DeleteNodesRequest.class);

	// END_GENERATED_CODE
}
