package biosim.client.model;


import java.util.List;
import java.util.Map;
import java.util.Stack;

import m3.gwt.lang.ListX;

public class Label extends Node {

	private String _name;
		
	private Label() {
	}
	
	public Label(DataSet dataSet, String name) {
		super(dataSet);
		this._name = name;
	}

	public Label(DataSet dataSet, Uid uid, String name) {
		super(dataSet, uid);
		this._name = name;
	}

	public String getName() {
		return _name;
	}
	
	@Override
	public String toHtmlString() {
		return _name;
	}

	@Override
	public String toString() {
		return _name;
	}

	public void addChildLabels(final Map<Label, List<String>> map) {
		Stack<Label> s = new Stack<Label>();
		s.push(this);
		addChildLabels(map, s);
	}
	
	public void addChildLabels(final Map<Label, List<String>> map, Stack<Label> path) {
		for ( Label label : getChildLabels() ) {
			if ( !path.contains(label) ) {
				path.push(label);
				List<String> paths = map.get(label);
				if ( paths == null ) {
					paths = ListX.create();
					map.put(label, paths);
				}
				paths.add( ListX.join(path, ":") );
				label.addChildLabels(map, path);
				path.pop();
			}
		}
	}

	@Override
	public String getIconUrl() {
		return "/images/tag.png";
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
	public static class LabelContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext name = new m3.gwt.props.impl.AbstractPropertyContext<Label,java.lang.String>(this, "name", java.lang.String.class, 0, null, false) {
	    	    protected java.lang.String getImpl(Label bean) { return bean.getName(); }
	    	    protected void setImpl(Label bean, java.lang.String value ) { bean.setName(value);}
	    };
	    public m3.gwt.props.PropertyContext uid = Node.Context.uid;
	    public m3.gwt.props.PropertyContext created = Node.Context.created;
	    public m3.gwt.props.PropertyContext dataSet = Node.Context.dataSet;
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.model.Node");
	        set = set.insert("biosim.client.model.Label");
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
	    public Label newInstance() {
	        return new Label();
	    }
	    LabelContainerContext(Class<?> actualClass) {
	        super(biosim.client.model.Label.class, Label.Context);
	    }
	}
	public static final LabelContainerContext Context = new LabelContainerContext(Label.class);

	// END_GENERATED_CODE
}
