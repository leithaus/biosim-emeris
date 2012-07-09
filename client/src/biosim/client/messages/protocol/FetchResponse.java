package biosim.client.messages.protocol;

import m3.gwt.props.ApplyCodeGeneration;
import biosim.client.messages.model.MNode;

@ApplyCodeGeneration
public class FetchResponse extends ResponseBody {

	private MNode _node;
	
	// BEGIN_GENERATED_CODE
	
	public biosim.client.messages.model.MNode getNode() {
	    return _node;
	}
	public void setNode(biosim.client.messages.model.MNode node0) {
	    _setNode(node0);
	}
	protected void _setNode(biosim.client.messages.model.MNode node0) {
	    biosim.client.messages.model.MNode before = _node;
	     _node = node0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.node, before, node0);
	}
	@Override
	public String toString() {
	    return m3.gwt.props.ToStringBuilder.toString(this, Context);
	}
	public static class FetchResponseContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext node = new m3.gwt.props.impl.AbstractPropertyContext<FetchResponse,biosim.client.messages.model.MNode>(this, "node", biosim.client.messages.model.MNode.class, 0, null, false) {
	    	    protected biosim.client.messages.model.MNode getImpl(FetchResponse bean) { return bean.getNode(); }
	    	    protected void setImpl(FetchResponse bean, biosim.client.messages.model.MNode value ) { bean.setNode(value);}
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
	        list = list.cons(this.node);
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
