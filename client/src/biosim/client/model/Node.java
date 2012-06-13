package biosim.client.model;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import m3.gwt.lang.ListX;
import m3.gwt.lang.LogTool;
import m3.gwt.props.ApplyCodeGeneration;

import com.google.gwt.json.client.JSONObject;


@ApplyCodeGeneration
public abstract class Node {
	
	private Uid _uid;
	private Date _created;

	transient private DataSet _dataSet;
	
	protected Node() {
	}
	
	protected Node(DataSet dataSet) {
		_dataSet = dataSet;
		_uid = Uid.random();
		_created = new Date();
	}
	
	protected Node(DataSet dataSet, Uid uid) {
		_dataSet = dataSet;
		_uid = uid;
		_created = new Date();
	}

	protected Node(DataSet dataSet, JSONObject jo) {
		_dataSet = dataSet;
		_uid = new Uid(jo.get("uid").isString().stringValue());
	}
	
	public abstract String getName();
	
	public Uid getUid() {
		return _uid;
	}
	
	public DataSet getDataSet() {
		return _dataSet;
	}

	public String getVisualId() {
		return toString();
	}
	
	public String getIconUrl() {
		return null;
	}
	
	public Link link(Node to) {
		return new Link(_dataSet, this._uid, to._uid);
	}
	
	public String toHtmlString() {
		return toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_uid == null) ? 0 : _uid.hashCode());
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
		Node other = (Node) obj;
		if (_uid == null) {
			if (other._uid != null)
				return false;
		} else if (!_uid.equals(other._uid))
			return false;
		return true;
	}
	
	public <T extends Node> void visitChildren(NodeVisitor v) {
		for ( Link l : getOutgoingLinks() ) {
			v.visit(l.getToNode());
		}
	}
	
	public <T extends Node> void visitDescendants(NodeVisitor v) {
		for ( Link l : getOutgoingLinks() ) {
			v.visit(l.getToNode());
			l.getToNode().visitDescendants(v);
		}
	}
	
	public <T extends Node> void visitAncestry(NodeVisitor v) {
		List<Link> incomingLinks = getIncomingLinks();
		for ( Link l : incomingLinks ) {
			v.visit(l.getFromNode());
			l.getFromNode().visitAncestry(v);
		}
	}
	
	public List<Label> getChildLabels() {
		return linkedNodes(Label.class, true);
	}

	public boolean isParentOf(Node child) {
		for ( Link l : getOutgoingLinks() ) {
			if ( l.getToNode().equals(child) ) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		return getClass().getName() + "@" + _uid;
	}

	public String canBeSeenBy(Node n) {
		Stack<Node> s = canBeSeenByImpl(n, new Stack<Node>());
		if ( s != null ) {
			List<Node> list = ListX.create();
			s.remove(this);
			list.addAll(s);
			list.add(n);
			java.util.Collections.reverse(list);
			return ListX.join(list, ":");
		} else {
			return null;
		}
	}

	private Stack<Node> canBeSeenByImpl(final Node n, Stack<Node> stackState) {
		if ( this.equals(n) ) {
			return stackState;
		}
		for ( Link l : getIncomingLinks() ) {
			if ( !stackState.contains(l.getToNode()) ) {
				stackState.add(l.getToNode());
				Stack<Node> s = l.getFromNode().canBeSeenByImpl(n, stackState);
				if ( s != null ) {
					return s;
				} else {
					stackState.pop();
				}
			}
		}
		return null;
	}

	public boolean isDescendantOf(final Node parent) {
		try {
			visitAncestry(new NodeVisitor() {
				@Override
				public void visit(Node node) {
					if ( node.equals(parent) ) {
						throw new ExitVisitor();
					}
				}
			});
			return false;
		} catch ( ExitVisitor e ) {
			return true;
		}
	}

	public boolean isAncestorOf(final Node child) {
		try {
			child.visitAncestry(new NodeVisitor() {
				@Override
				public void visit(Node node) {
					if ( node.equals(Node.this) ) {
						throw new ExitVisitor();
					}
				}
			});
			return false;
		} catch ( ExitVisitor e ) {
			return true;
		}		
	}
	
	public boolean hasChild(final Node possibleChild) {
		final boolean[] hasChild = { false };
		visitChildren(new NodeVisitor() {
			@Override
			public void visit(Node node) {
			    if ( node == null ) {
			        LogTool.warn("node is null possibleChild is " + possibleChild.getUid(), new Throwable());
			    } else if ( possibleChild == null ) {
			        LogTool.warn("possibleChild is null");			         
			    } else {
    				if ( node.equals(possibleChild) ) {
    					hasChild[0] = true;
    				}
			    }
			}
		});
		return hasChild[0];
	}

	public boolean hasConnectionsOnly() {
		final Boolean[] connectionsOnly = { null };
		visitChildren(new NodeVisitor() {
			@Override
			public void visit(Node node) {
				if ( connectionsOnly[0] == null ) {
					connectionsOnly[0] = true;
				}
				if ( !(node instanceof Person) ) {
					connectionsOnly[0] = false;
				}
			}
		});
		if ( connectionsOnly[0] == null || !connectionsOnly[0] ) {
			return false;
		} else {
			return true;
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends Node> List<T> linkedNodes(Class<T> clazz, boolean children) {
		List<T> list = ListX.create();
		Map<Node,List<Link>> links;
		if ( children ) {
			links = _dataSet.linksByFrom;
		} else {
			links = _dataSet.linksByTo;
		}
		for ( Link l : links.get(this) ) {
			Node n;
			if ( children ) {
				n = l.getToNode();
			} else {
				n = l.getFromNode();
			}
			if ( n.getClass().equals(clazz)) {
				list.add((T)n);
			}
		}
		return list;
	}
	
	public List<Link> getOutgoingLinks() {
		return _dataSet.linksByFrom.get(this);
	}

	public List<Link> getIncomingLinks() {
		return _dataSet.linksByTo.get(this);
	}

	public void unlink(Node to) {
		for ( Link l : getOutgoingLinks() ) {
			if ( l.getToNode().equals(to) ) {
				_dataSet.removeLink(l);
				break;
			}
		}
	}
	
	// BEGIN_GENERATED_CODE
	
	public void setUid(biosim.client.model.Uid uid0) {
	    _setUid(uid0);
	}
	protected void _setUid(biosim.client.model.Uid uid0) {
	    biosim.client.model.Uid before = _uid;
	     _uid = uid0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.uid, before, uid0);
	}
	public java.util.Date getCreated() {
	    return _created;
	}
	public void setCreated(java.util.Date created0) {
	    _setCreated(created0);
	}
	protected void _setCreated(java.util.Date created0) {
	    java.util.Date before = _created;
	     _created = created0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.created, before, created0);
	}
	public void setDataSet(biosim.client.model.DataSet dataSet0) {
	    _setDataSet(dataSet0);
	}
	protected void _setDataSet(biosim.client.model.DataSet dataSet0) {
	    biosim.client.model.DataSet before = _dataSet;
	     _dataSet = dataSet0;
	    m3.gwt.props.Txn.getPropertyChangeManager().fireChangeEvent(this, Context.dataSet, before, dataSet0);
	}
	public static class NodeContainerContext extends m3.gwt.props.impl.AbstractContainerContext {
	    public m3.gwt.props.PropertyContext uid = new m3.gwt.props.impl.AbstractPropertyContext<Node,biosim.client.model.Uid>(this, "uid", biosim.client.model.Uid.class, 0, null, false) {
	    	    protected biosim.client.model.Uid getImpl(Node bean) { return bean.getUid(); }
	    	    protected void setImpl(Node bean, biosim.client.model.Uid value ) { bean.setUid(value);}
	    };
	    public m3.gwt.props.PropertyContext created = new m3.gwt.props.impl.AbstractPropertyContext<Node,java.util.Date>(this, "created", java.util.Date.class, 1, null, false) {
	    	    protected java.util.Date getImpl(Node bean) { return bean.getCreated(); }
	    	    protected void setImpl(Node bean, java.util.Date value ) { bean.setCreated(value);}
	    };
	    public m3.gwt.props.PropertyContext dataSet = new m3.gwt.props.impl.AbstractPropertyContext<Node,biosim.client.model.DataSet>(this, "dataSet", biosim.client.model.DataSet.class, 2, null, true) {
	    	    protected biosim.client.model.DataSet getImpl(Node bean) { return bean.getDataSet(); }
	    	    protected void setImpl(Node bean, biosim.client.model.DataSet value ) { bean.setDataSet(value);}
	    };
	    protected m3.fj.data.FSet<String> createImplementsList() {
	        m3.fj.data.FSet<String> set = m3.fj.data.FSet.empty();
	        set = set.insert("java.lang.Object");
	        set = set.insert("biosim.client.model.Node");
	        return set;
	    }
	    protected m3.fj.data.FList<m3.gwt.props.PropertyContext> createPropertyList() {
	        m3.fj.data.FList<m3.gwt.props.PropertyContext> list = m3.fj.data.FList.nil();
	        list = list.cons(this.uid);
	        list = list.cons(this.created);
	        list = list.cons(this.dataSet);
	        return list;
	    }
	    public Node newInstance() {
	        throw new RuntimeException("cannot instantiate an interface or abstract class");
	    }
	    NodeContainerContext(Class<?> actualClass) {
	        super(biosim.client.model.Node.class);
	    }
	}
	public static final NodeContainerContext Context = new NodeContainerContext(Node.class);

	// END_GENERATED_CODE
}
