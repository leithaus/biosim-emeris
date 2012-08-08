package biosim.client.messages.protocol;

import java.util.List;

import m3.gwt.lang.ListX;
import m3.gwt.props.ApplyCodeGeneration;
import biosim.client.messages.model.Uid;

@ApplyCodeGeneration
public class DeleteNodesResponse extends ResponseBody {
	
	private Uid _agentUid;
	private Uid _connectionUid;
	private List<Uid> _nodes = ListX.create();
	
	public DeleteNodesResponse() {
	}

	// BEGIN_GENERATED_CODE
	
	public biosim.client.messages.model.Uid getAgentUid() {
	    return _agentUid;
	}
	public void setAgentUid(biosim.client.messages.model.Uid agentUid0) {
	    _setAgentUid(agentUid0);
	}
	protected void _setAgentUid(biosim.client.messages.model.Uid agentUid0) {
	    biosim.client.messages.model.Uid before = _agentUid;
	     _agentUid = agentUid0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.agentUid, before, agentUid0);
	}
	public biosim.client.messages.model.Uid getConnectionUid() {
	    return _connectionUid;
	}
	public void setConnectionUid(biosim.client.messages.model.Uid connectionUid0) {
	    _setConnectionUid(connectionUid0);
	}
	protected void _setConnectionUid(biosim.client.messages.model.Uid connectionUid0) {
	    biosim.client.messages.model.Uid before = _connectionUid;
	     _connectionUid = connectionUid0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.connectionUid, before, connectionUid0);
	}
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
	public static class DeleteNodesResponseContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext agentUid = new m3.gwt.props.impl.AbstractPropertyContext<DeleteNodesResponse,biosim.client.messages.model.Uid>(this, "agentUid", biosim.client.messages.model.Uid.class, 0, null, false) {
	    	    protected biosim.client.messages.model.Uid getImpl(DeleteNodesResponse bean) { return bean.getAgentUid(); }
	    	    protected void setImpl(DeleteNodesResponse bean, biosim.client.messages.model.Uid value ) { bean.setAgentUid(value);}
	    };
	    public m3.gwt.props.PropertyContext connectionUid = new m3.gwt.props.impl.AbstractPropertyContext<DeleteNodesResponse,biosim.client.messages.model.Uid>(this, "connectionUid", biosim.client.messages.model.Uid.class, 1, null, false) {
	    	    protected biosim.client.messages.model.Uid getImpl(DeleteNodesResponse bean) { return bean.getConnectionUid(); }
	    	    protected void setImpl(DeleteNodesResponse bean, biosim.client.messages.model.Uid value ) { bean.setConnectionUid(value);}
	    };
	    public m3.gwt.props.PropertyContext nodes = new m3.gwt.props.impl.AbstractPropertyContext<DeleteNodesResponse,java.util.List<biosim.client.messages.model.Uid>>(this, "nodes", java.util.List.class, 2, biosim.client.messages.model.Uid.class, false) {
	    	    protected java.util.List<biosim.client.messages.model.Uid> getImpl(DeleteNodesResponse bean) { return bean.getNodes(); }
	    	    protected void setImpl(DeleteNodesResponse bean, java.util.List<biosim.client.messages.model.Uid> value ) { bean.setNodes(value);}
	    };
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.messages.protocol.ResponseBody");
	        set = set.insert("biosim.client.messages.protocol.DeleteNodesResponse");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.agentUid);
	        list = list.cons(this.connectionUid);
	        list = list.cons(this.nodes);
	        return list;
	    }
	    public DeleteNodesResponse newInstance() {
	        return new DeleteNodesResponse();
	    }
	    DeleteNodesResponseContainerContext(Class<?> actualClass) {
	        super(biosim.client.messages.protocol.DeleteNodesResponse.class, DeleteNodesResponse.Context);
	    }
	}
	public static final DeleteNodesResponseContainerContext Context = new DeleteNodesResponseContainerContext(DeleteNodesResponse.class);

	// END_GENERATED_CODE
}
