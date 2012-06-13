package biosim.client.model;


public class TextMessage extends Node {

	private String _text;

	private TextMessage() {
		super();
	}
	
	public TextMessage(DataSet dataSet, String text) {
		super(dataSet);
		this._text = text;
	}
	
	@Override
	public String toHtmlString() {
		return _text;
	}
	
	@Override
	public String getName() {
		return null;
	}
	
	@Override
	public String toString() {
		return "TextMessage: " + _text;
	}
	
	@Override
	public String getIconUrl() {
		return "/images/message.png";
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
	public static class TextMessageContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext text = new m3.gwt.props.impl.AbstractPropertyContext<TextMessage,java.lang.String>(this, "text", java.lang.String.class, 0, null, false) {
	    	    protected java.lang.String getImpl(TextMessage bean) { return bean.getText(); }
	    	    protected void setImpl(TextMessage bean, java.lang.String value ) { bean.setText(value);}
	    };
	    public m3.gwt.props.PropertyContext uid = Node.Context.uid;
	    public m3.gwt.props.PropertyContext created = Node.Context.created;
	    public m3.gwt.props.PropertyContext dataSet = Node.Context.dataSet;
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.model.Node");
	        set = set.insert("biosim.client.model.TextMessage");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.uid);
	        list = list.cons(this.created);
	        list = list.cons(this.dataSet);
	        list = list.cons(this.text);
	        return list;
	    }
	    public TextMessage newInstance() {
	        return new TextMessage();
	    }
	    TextMessageContainerContext(Class<?> actualClass) {
	        super(biosim.client.model.TextMessage.class, TextMessage.Context);
	    }
	}
	public static final TextMessageContainerContext Context = new TextMessageContainerContext(TextMessage.class);

	// END_GENERATED_CODE
}
