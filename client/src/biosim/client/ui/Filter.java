package biosim.client.ui;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import m3.gwt.lang.Function1;
import m3.gwt.lang.ListX;
import m3.gwt.lang.MapX;
import m3.gwt.lang.Utils;
import biosim.client.model.Connection;
import biosim.client.model.Label;
import biosim.client.model.Node;
import biosim.client.utils.SetX;

public class Filter {

	final List<Node> _nodes = ListX.create();
	final List<Connection> _people = ListX.create();
	final Set<biosim.client.model.Label> _labels = SetX.create();
	final Map<biosim.client.model.Label, List<String>> _labelsAndChildren = MapX.create();
	
	public Filter() {
		this(new ArrayList<Node>());
	}
	
	public Filter(List<Node> nodes) {
		
		for ( Node node : nodes ) {
			if ( !_nodes.contains(node) && canAddFilter(node) ) {
				if ( node instanceof biosim.client.model.Label ) {
					for ( Node f : nodes ) {
						if ( f.isDescendantOf(node) ) {
							_labels.remove(node);
						}
					}
				}
				
				if ( node instanceof Connection ) {
					_people.add((Connection)node);
				}
				if ( node instanceof biosim.client.model.Label ) {
					biosim.client.model.Label label = (biosim.client.model.Label) node;
					_labels.add(label);
				}
			}
			_nodes.add(node);
		}

		_labelsAndChildren.clear();
		for ( biosim.client.model.Label l : _labels ) {
			List<String> paths = _labelsAndChildren.get(l);
			if ( paths == null ) {
				paths = ListX.create();
				_labelsAndChildren.put(l, paths);
			}
			paths.add(l.getName());
			l.addChildLabels(_labelsAndChildren);
		}

	}
	
	public boolean canAddFilter(Node node) {
		if ( _nodes.contains(node) ) {
			return false;
		}
		for ( Node f : _labels ) {
			if ( f.equals(node) || f.isAncestorOf(node) ) {
				return false;
			}
		}
		return true;
	}
	
	public Filter remove(Node f) {
		if (_nodes.contains(f)) {
			List<Node> newNodes = ListX.copy(_nodes);
			newNodes.remove(f);
			return new Filter(newNodes);			
		} else {
			return this;
		}
	}
	
	public Filter add(Node f) {
		if ( canAddFilter(f) ) {
			List<Node> newNodes = ListX.copy(_nodes);
			newNodes.add(f);
			return new Filter(newNodes);
		} else {
			return this;
		}
	}
	
	public boolean accept(Node node) {
		return acceptVerbose(node) != null;
	}
	
	/**
	 * returns null to say no match and a string representing what matched
	 */
	public ContentCriteria acceptVerbose(Node node) {
		
		ContentCriteria ac = new ContentCriteria();
		
		// labels are OR'ed and no label filters means show anything
		boolean hasAtLeastOneLabel = false;
		if ( _labelsAndChildren.isEmpty() ) {
			hasAtLeastOneLabel = true;
		} else {
			for ( biosim.client.model.Label l : node.linkedNodes(biosim.client.model.Label.class, false)) {
				List<String> paths = _labelsAndChildren.get(l);
				if ( paths != null ) {
					hasAtLeastOneLabel = true;
					ac.labels.add(l);
					ac.paths.addAll(paths);
				}
			}
		}
		
		boolean hasAllPeople = true; 
		if ( hasAtLeastOneLabel ) {
			// people are AND'ed
			for ( Connection p : _people ) {
				String cbsb = node.canBeSeenBy(p);
				if ( cbsb == null ) {
					hasAllPeople = false;
					break;
				}
				cbsb = node.canBeSeenBy(p);
				ac.paths.add(cbsb);
				ac.connections.add(p);
			}
		}
		
		if ( _people.isEmpty() && _labels.isEmpty() ) {
			return null;
		} else if ( hasAtLeastOneLabel && hasAllPeople ) {
			return ac;
		} else {
			return null;
		}
		
	}
	
	public String getDescription() {
		return Utils.join(_nodes, " : ", new Function1<Node, String>() {
			@Override
			public String apply(Node node) {
				return node.getName();
			}
		});
	}

	public boolean isVisible(Node n) {
		if ( n instanceof Label ) return _labels.contains(n);
		else return _nodes.contains(n);
	}
	
}
