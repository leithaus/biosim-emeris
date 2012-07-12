package biosim.client.messages.model;

public class MText extends MNode {

	private String _text;
	
	public MText() {
	}
	
	public MText(String _text) {
		this._text = _text;
	}

	// BEGIN_GENERATED_CODE
	
	public java.lang.String getText() {
	    return _text;
	}
	public void setText(java.lang.String text0) {
	    _setText(text0);
	}
	protected void _setText(java.lang.String text0) {
	    java.lang.String before = _text;
	     _text = text0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.text, before, text0);
	}
	@Override
	public String toString() {
	    return m3.gwt.props.ToStringBuilder.toString(this, Context);
	}
	public static class MTextContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext text = new m3.gwt.props.impl.AbstractPropertyContext<MText,java.lang.String>(this, "text", java.lang.String.class, 0, null, false) {
	    	    protected java.lang.String getImpl(MText bean) { return bean.getText(); }
	    	    protected void setImpl(MText bean, java.lang.String value ) { bean.setText(value);}
	    };
	    public m3.gwt.props.PropertyContext uid = MNode.Context.uid;
	    public m3.gwt.props.PropertyContext linkHints = MNode.Context.linkHints;
	    public m3.gwt.props.PropertyContext agentServices = MNode.Context.agentServices;
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("biosim.client.messages.model.MNode");
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.messages.model.MText");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.text);
	        list = list.cons(this.uid);
	        list = list.cons(this.linkHints);
	        list = list.cons(this.agentServices);
	        return list;
	    }
	    public MText newInstance() {
	        return new MText();
	    }
	    MTextContainerContext(Class<?> actualClass) {
	        super(biosim.client.messages.model.MText.class, MText.Context);
	    }
	}
	public static final MTextContainerContext Context = new MTextContainerContext(MText.class);

	// END_GENERATED_CODE
}
