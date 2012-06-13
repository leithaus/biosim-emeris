package biosim.client.messages;

import java.util.List;

import m3.gwt.lang.ListX;
import biosim.client.Biosim;
import biosim.client.model.Node;
import biosim.client.model.Uid;

public class CreateNodes extends MessageBody {
	
	private Uid _agentUid;
	private List<Node> _nodes;
	
	private CreateNodes() {
	}
	
	public CreateNodes(Node...nodes) {
		_agentUid = Biosim.get().getAgentUid();
		_nodes = ListX.create();
		for ( Node n : nodes ) {
			_nodes.add(n);
		}
	}

	// BEGIN_GENERATED_CODE
	
	public biosim.client.model.Uid getAgentUid() {
	    return _agentUid;
	}
	public void setAgentUid(biosim.client.model.Uid agentUid0) {
	    _setAgentUid(agentUid0);
	}
	protected void _setAgentUid(biosim.client.model.Uid agentUid0) {
	    biosim.client.model.Uid before = _agentUid;
	     _agentUid = agentUid0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.agentUid, before, agentUid0);
	}
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
	public static class CreateNodesContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext agentUid = new m3.gwt.props.impl.AbstractPropertyContext<CreateNodes,biosim.client.model.Uid>(this, "agentUid", biosim.client.model.Uid.class, 0, null, false) {
	    	    protected biosim.client.model.Uid getImpl(CreateNodes bean) { return bean.getAgentUid(); }
	    	    protected void setImpl(CreateNodes bean, biosim.client.model.Uid value ) { bean.setAgentUid(value);}
	    };
	    public m3.gwt.props.PropertyContext nodes = new m3.gwt.props.impl.AbstractPropertyContext<CreateNodes,java.util.List<biosim.client.model.Node>>(this, "nodes", java.util.List.class, 1, biosim.client.model.Node.class, false) {
	    	    protected java.util.List<biosim.client.model.Node> getImpl(CreateNodes bean) { return bean.getNodes(); }
	    	    protected void setImpl(CreateNodes bean, java.util.List<biosim.client.model.Node> value ) { bean.setNodes(value);}
	    };
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("biosim.client.messages.CreateNodes");
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.messages.MessageBody");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.agentUid);
	        list = list.cons(this.nodes);
	        return list;
	    }
	    public CreateNodes newInstance() {
	        return new CreateNodes();
	    }
	    CreateNodesContainerContext(Class<?> actualClass) {
	        super(biosim.client.messages.CreateNodes.class, CreateNodes.Context);
	    }
	}
	public static final CreateNodesContainerContext Context = new CreateNodesContainerContext(CreateNodes.class);

	// END_GENERATED_CODE
}
