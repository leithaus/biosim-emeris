package biosim.client.model;




public class Link extends Node {

	private Uid _from;
	private Uid _to;
	
	private Link() {
	}
	
	public Link(DataSet dataSet, Uid from, Uid to) {
		super(dataSet);
		_from = from;
		_to = to;
	}

	public Node getFromNode() {
		return getDataSet().getNode(_from);
	}

	public Node getToNode() {
		return getDataSet().getNode(_to);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_from == null) ? 0 : _from.hashCode());
		result = prime * result + ((_to == null) ? 0 : _to.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Link other = (Link) obj;
		if (_from == null) {
			if (other._from != null)
				return false;
		} else if (!_from.equals(other._from))
			return false;
		if (_to == null) {
			if (other._to != null)
				return false;
		} else if (!_to.equals(other._to))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Link(" + getFromNode() + ", " + getToNode() + ")";
	}

	@Override
	public String getName() {
		return null;
	}
	
	// BEGIN_GENERATED_CODE
	
	public biosim.client.model.Uid getFrom() {
	    return _from;
	}
	public void setFrom(biosim.client.model.Uid from0) {
	    _setFrom(from0);
	}
	protected void _setFrom(biosim.client.model.Uid from0) {
	    biosim.client.model.Uid before = _from;
	     _from = from0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.from, before, from0);
	}
	public biosim.client.model.Uid getTo() {
	    return _to;
	}
	public void setTo(biosim.client.model.Uid to0) {
	    _setTo(to0);
	}
	protected void _setTo(biosim.client.model.Uid to0) {
	    biosim.client.model.Uid before = _to;
	     _to = to0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.to, before, to0);
	}
	public static class LinkContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext from = new m3.gwt.props.impl.AbstractPropertyContext<Link,biosim.client.model.Uid>(this, "from", biosim.client.model.Uid.class, 0, null, false) {
	    	    protected biosim.client.model.Uid getImpl(Link bean) { return bean.getFrom(); }
	    	    protected void setImpl(Link bean, biosim.client.model.Uid value ) { bean.setFrom(value);}
	    };
	    public m3.gwt.props.PropertyContext to = new m3.gwt.props.impl.AbstractPropertyContext<Link,biosim.client.model.Uid>(this, "to", biosim.client.model.Uid.class, 1, null, false) {
	    	    protected biosim.client.model.Uid getImpl(Link bean) { return bean.getTo(); }
	    	    protected void setImpl(Link bean, biosim.client.model.Uid value ) { bean.setTo(value);}
	    };
	    public m3.gwt.props.PropertyContext uid = Node.Context.uid;
	    public m3.gwt.props.PropertyContext created = Node.Context.created;
	    public m3.gwt.props.PropertyContext dataSet = Node.Context.dataSet;
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.model.Node");
	        set = set.insert("biosim.client.model.Link");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.uid);
	        list = list.cons(this.created);
	        list = list.cons(this.dataSet);
	        list = list.cons(this.from);
	        list = list.cons(this.to);
	        return list;
	    }
	    public Link newInstance() {
	        return new Link();
	    }
	    LinkContainerContext(Class<?> actualClass) {
	        super(biosim.client.model.Link.class, Link.Context);
	    }
	}
	public static final LinkContainerContext Context = new LinkContainerContext(Link.class);

	// END_GENERATED_CODE
}
