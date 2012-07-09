package biosim.client.messages.protocol;

import m3.gwt.props.ApplyCodeGeneration;
import biosim.client.messages.model.MLabel;

@ApplyCodeGeneration
public class LabelResponse extends ResponseBody {

	private MLabel _label;
	
	// BEGIN_GENERATED_CODE
	
	public biosim.client.messages.model.MLabel getLabel() {
	    return _label;
	}
	public void setLabel(biosim.client.messages.model.MLabel label0) {
	    _setLabel(label0);
	}
	protected void _setLabel(biosim.client.messages.model.MLabel label0) {
	    biosim.client.messages.model.MLabel before = _label;
	     _label = label0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.label, before, label0);
	}
	@Override
	public String toString() {
	    return m3.gwt.props.ToStringBuilder.toString(this, Context);
	}
	public static class LabelResponseContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext label = new m3.gwt.props.impl.AbstractPropertyContext<LabelResponse,biosim.client.messages.model.MLabel>(this, "label", biosim.client.messages.model.MLabel.class, 0, null, false) {
	    	    protected biosim.client.messages.model.MLabel getImpl(LabelResponse bean) { return bean.getLabel(); }
	    	    protected void setImpl(LabelResponse bean, biosim.client.messages.model.MLabel value ) { bean.setLabel(value);}
	    };
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.messages.protocol.ResponseBody");
	        set = set.insert("biosim.client.messages.protocol.LabelResponse");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.label);
	        return list;
	    }
	    public LabelResponse newInstance() {
	        return new LabelResponse();
	    }
	    LabelResponseContainerContext(Class<?> actualClass) {
	        super(biosim.client.messages.protocol.LabelResponse.class, LabelResponse.Context);
	    }
	}
	public static final LabelResponseContainerContext Context = new LabelResponseContainerContext(LabelResponse.class);

	// END_GENERATED_CODE
}
