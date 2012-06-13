package biosim.client.model;

import biosim.client.eventlist.ObservableList;
import biosim.client.eventlist.Observables;

import com.google.gwt.json.client.JSONObject;


public class Person extends Node {

	private String _name;

	private Person() {
	}
	
	public Person(DataSet dataSet, Uid uid, String name) {
		super(dataSet, uid);
		_name = name;
	}
	
	public Label getMessagesLabel() {
		return getChildLabel("Messages");
	}
	
	public Label getChildLabel(String name) {
		for ( Label label : getChildLabels() ) {
			if ( label.getName().equals(name) ) {
				return label;
			}
		}
		return null;
	}
	
	public Label getNeedsLabel() {
		return getChildLabel("Needs");		
	}
	
	public Label getOffersLabel() {
		return getChildLabel("Offers");
	}

	public Person(DataSet dataSet, String name) {
		super(dataSet);
		_name = name;
	}

	public Person(DataSet dataSet, JSONObject jo) {
		super(dataSet, jo);
		this._name = jo.get("name").isString().stringValue();
	}

	public String getVisualId() {
		return _name;
	}

	public String getIconUrl() {
		return "/friends/" + _name.toLowerCase() + ".jpg";
	}
	
	public String getName() {
		return _name;
	}
	
	@Override
	public String toString() {
		return _name;
	}

	public ObservableList<Node> getConnections() {
		final ObservableList<Node> list = Observables.create();
		visitDescendants(new NodeVisitor() {
			@Override
			public void visit(Node node) {
				if ( node instanceof Person && !list.contains(node) ) {
					list.add(node);
				}
			}
		});
		return list;
	}

	public ObservableList<Node> getAllContent() {
		final ObservableList<Node> list = Observables.create();
		visitDescendants(new NodeVisitor() {
			@Override
			public void visit(Node node) {
				if ( !list.contains(node) && !(node instanceof Person) ) {
					list.add(node);
				}
			}
		});
		return list;
	}

	// BEGIN_GENERATED_CODE
	
	public void setName(java.lang.String name0) {
	    _setName(name0);
	}
	protected void _setName(java.lang.String name0) {
	    java.lang.String before = _name;
	     _name = name0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.name, before, name0);
	}
	public static class PersonContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext name = new m3.gwt.props.impl.AbstractPropertyContext<Person,java.lang.String>(this, "name", java.lang.String.class, 0, null, false) {
	    	    protected java.lang.String getImpl(Person bean) { return bean.getName(); }
	    	    protected void setImpl(Person bean, java.lang.String value ) { bean.setName(value);}
	    };
	    public m3.gwt.props.PropertyContext uid = Node.Context.uid;
	    public m3.gwt.props.PropertyContext created = Node.Context.created;
	    public m3.gwt.props.PropertyContext dataSet = Node.Context.dataSet;
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("biosim.client.model.Person");
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.model.Node");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.uid);
	        list = list.cons(this.created);
	        list = list.cons(this.dataSet);
	        list = list.cons(this.name);
	        return list;
	    }
	    public Person newInstance() {
	        return new Person();
	    }
	    PersonContainerContext(Class<?> actualClass) {
	        super(biosim.client.model.Person.class, Person.Context);
	    }
	}
	public static final PersonContainerContext Context = new PersonContainerContext(Person.class);

	// END_GENERATED_CODE
}
