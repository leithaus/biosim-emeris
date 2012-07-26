package biosim.client.ui;


import java.util.ArrayList;
import java.util.List;

import m3.fj.F1;
import m3.fj.data.FList;
import m3.gwt.lang.ListX;
import biosim.client.messages.model.MConnection;
import biosim.client.messages.model.MLabel;
import biosim.client.messages.model.MNode;

public class Filter {

	FList<MNode> _nodes = FList.nil();
	
	public Filter() {
		this(new ArrayList<MNode>());
	}
	
	public Filter(Iterable<MNode> nodes) {
		for ( MNode n : nodes ) {
			_nodes = _nodes.cons(n);
		}
	}
	
    @SuppressWarnings("unchecked")
	public Iterable<MLabel> getLabels() {
    	return (Iterable<MLabel>)(Object) _nodes.filter(new F1<MNode, Boolean>() {
    		public Boolean f(MNode n) {
    			return n instanceof MLabel;
    		}
    	});
    }

    @SuppressWarnings("unchecked")
    public Iterable<MConnection> getConnections() {
    	return (Iterable<MConnection>)(Object) _nodes.filter(new F1<MNode, Boolean>() {
    		public Boolean f(MNode n) {
    			return n instanceof MConnection;
    		}
    	});
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
		return true;
//		if ( n instanceof MLabel ) return _labels.contains(n);
//		else return _nodes.contains(n);
	}

	public boolean accept(MNode t) {
		throw new RuntimeException("fix me");
//		return false;
	}
	
}
