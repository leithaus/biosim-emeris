package biosim.client.ui;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import m3.gwt.lang.ListX;
import m3.gwt.lang.MapX;
import biosim.client.messages.model.MConnection;
import biosim.client.messages.model.MLabel;
import biosim.client.messages.model.MNode;
import biosim.client.utils.SetX;

public class Filter {

	final List<MNode> _nodes = ListX.create();
	final List<MConnection> _connections = ListX.create();
	final Set<MLabel> _labels = SetX.create();
	final Map<MLabel, List<String>> _labelsAndChildren = MapX.create();
	
	public Filter() {
		this(new ArrayList<MNode>());
	}
	
	public Filter(List<MNode> nodes) {
		
//		for ( MNode node : nodes ) {
//			if ( !_nodes.contains(node) && canAddFilter(node) ) {
//				if ( node instanceof MLabel ) {
//					for ( MNode f : nodes ) {
//						if ( f.isDescendantOf(node) ) {
//							_labels.remove(node);
//						}
//					}
//				}
//				
//				if ( node instanceof MConnection ) {
//					_connections.add((MConnection)node);
//				}
//				if ( node instanceof MLabel ) {
//					MLabel label = (MLabel) node;
//					_labels.add(label);
//				}
//			}
//			_nodes.add(node);
//		}
//
//		_labelsAndChildren.clear();
//		for ( MLabel l : _labels ) {
//			List<String> paths = _labelsAndChildren.get(l);
//			if ( paths == null ) {
//				paths = ListX.create();
//				_labelsAndChildren.put(l, paths);
//			}
//			paths.add(l.getName());
//			l.addChildLabels(_labelsAndChildren);
//		}

	}
	
	public boolean canAddFilter(MNode node) {
//		if ( _nodes.contains(node) ) {
//			return false;
//		}
//		for ( MNode f : _labels ) {
//			if ( f.equals(node) || f.isAncestorOf(node) ) {
//				return false;
//			}
//		}
		return true;
	}
	
//	public Filter remove(MNode f) {
//		if (_nodes.contains(f)) {
//			List<MNode> newNodes = ListX.copy(_nodes);
//			newNodes.remove(f);
//			return new Filter(newNodes);			
//		} else {
//			return this;
//		}
//	}
	
	public Filter add(MNode f) {
		if ( canAddFilter(f) ) {
			List<MNode> newNodes = ListX.copy(_nodes);
			newNodes.add(f);
			return new Filter(newNodes);
		} else {
			return this;
		}
	}
	
	public boolean accept(MNode node) {
		return acceptVerbose(node) != null;
	}
	
	/**
	 * returns null to say no match and a string representing what matched
	 */
	public ContentCriteria acceptVerbose(MNode node) {
		return null;
	}
		
//		ContentCriteria ac = new ContentCriteria();
//		
//		// labels are OR'ed and no label filters means show anything
//		boolean hasAtLeastOneLabel = false;
//		if ( _labelsAndChildren.isEmpty() ) {
//			hasAtLeastOneLabel = true;
//		} else {
//			for ( MLabel l : node.linkedNodes(MLabel.class, false)) {
//				List<String> paths = _labelsAndChildren.get(l);
//				if ( paths != null ) {
//					hasAtLeastOneLabel = true;
//					ac.labels.add(l);
//					ac.paths.addAll(paths);
//				}
//			}
//		}
//		
//		boolean hasAllPeople = true; 
//		if ( hasAtLeastOneLabel ) {
//			// people are AND'ed
//			for ( MConnection p : _connections ) {
//				String cbsb = node.canBeSeenBy(p);
//				if ( cbsb == null ) {
//					hasAllPeople = false;
//					break;
//				}
//				cbsb = node.canBeSeenBy(p);
//				ac.paths.add(cbsb);
//				ac.connections.add(p);
//			}
//		}
//		
//		if ( _connections.isEmpty() && _labels.isEmpty() ) {
//			return null;
//		} else if ( hasAtLeastOneLabel && hasAllPeople ) {
//			return ac;
//		} else {
//			return null;
//		}
//		
//	}
//	
//	public String getDescription() {
//		return Utils.join(_nodes, " : ", new Function1<MNode, String>() {
//			@Override
//			public String apply(MNode node) {
//				return node.getName();
//			}
//		});
//	}

	public boolean isVisible(MNode n) {
		if ( n instanceof MLabel ) return _labels.contains(n);
		else return _nodes.contains(n);
	}
	
}
