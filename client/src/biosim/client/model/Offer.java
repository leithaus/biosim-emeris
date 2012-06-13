package biosim.client.model;



public class Offer extends Node {

	private String _description;
	
	private Offer() {
	}
	
	public Offer(DataSet dataSet, Uid uid, String description) {
		super(dataSet, uid);
		_description = description;
	}
	
	public Offer(DataSet dataSet, String description) {
		super(dataSet);
		_description = description;
	}

	public String getDescription() {
		return _description;
	}
	
	@Override
	public String getIconUrl() {
		return "/images/offer.png";
	}

	@Override
	public String getName() {
		return null;
	}
	
	@Override
	public String toHtmlString() {
		return _description;
	}

	// BEGIN_GENERATED_CODE
	
	public void setDescription(java.lang.String description0) {
	    _setDescription(description0);
	}
	protected void _setDescription(java.lang.String description0) {
	    java.lang.String before = _description;
	     _description = description0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.description, before, description0);
	}
	@Override
	public String toString() {
	    return m3.gwt.props.ToStringBuilder.toString(this, Context);
	}
	public static class OfferContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext description = new m3.gwt.props.impl.AbstractPropertyContext<Offer,java.lang.String>(this, "description", java.lang.String.class, 0, null, false) {
	    	    protected java.lang.String getImpl(Offer bean) { return bean.getDescription(); }
	    	    protected void setImpl(Offer bean, java.lang.String value ) { bean.setDescription(value);}
	    };
	    public m3.gwt.props.PropertyContext uid = Node.Context.uid;
	    public m3.gwt.props.PropertyContext created = Node.Context.created;
	    public m3.gwt.props.PropertyContext dataSet = Node.Context.dataSet;
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.model.Node");
	        set = set.insert("biosim.client.model.Offer");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.uid);
	        list = list.cons(this.created);
	        list = list.cons(this.dataSet);
	        list = list.cons(this.description);
	        return list;
	    }
	    public Offer newInstance() {
	        return new Offer();
	    }
	    OfferContainerContext(Class<?> actualClass) {
	        super(biosim.client.model.Offer.class, Offer.Context);
	    }
	}
	public static final OfferContainerContext Context = new OfferContainerContext(Offer.class);

	// END_GENERATED_CODE
}
