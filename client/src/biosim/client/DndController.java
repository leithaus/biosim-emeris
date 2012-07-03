package biosim.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import m3.gwt.lang.Function1;
import m3.gwt.lang.ListX;
import m3.gwt.lang.LogTool;
import m3.gwt.lang.MapX;
import biosim.client.eventlist.ObservableList;
import biosim.client.model.Node;
import biosim.client.model.NodeVisitor;
import biosim.client.model.Person;
import biosim.client.ui.ContentCriteria;
import biosim.client.ui.NodeWidgetBuilder;
import biosim.client.ui.dnd.DndType;
import biosim.client.ui.dnd.DropAction;
import biosim.client.ui.dnd.MyDragController;
import biosim.client.utils.DialogHelper;
import biosim.client.utils.Pair;
import biosim.client.utils.SetX;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.AbstractDropController;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;


public class DndController {
	
	final MyDragController _delegate;
	
	final Map<Widget,DragSite> _widgetToDragSiteMap = MapX.create();
	final List<DropSite> _dropSites = ListX.create();
	final List<DragSite> _dragSites = ListX.create();

	final Set<Widget> _styledWidgets = SetX.create();
	
	final Map<Pair<DndType,DndType>, DropAction<?, ?>> _actionGrid = MapX.create();
	
	final DropAction<Node, Node> _nullDropAction = new DropAction<Node, Node>() {
		@Override
		public boolean canDrop(Node dragee, Node dropTarget) {
			return false;
		}
		@Override
		public void processDrop(Node draggee, Node dropTarget) {
			throw new RuntimeException("this should never happen");
		}
	};
	
	final ArrayList<NodeChildCounter> _nodeChildCounters = new ArrayList<NodeChildCounter>();

	
	public DndController(final Biosim biosim, AbsolutePanel boundaryPanel) {
		final DatabaseAccessLayer databaseAccessLayer = biosim.getDatabaseAccessLayer();
		_delegate = new MyDragController(boundaryPanel, false) {
			public void dragStart() {
			    LogTool.debug("dragStart() override start");
				_styledWidgets.clear();
				super.dragStart();
				for ( DropSite ds : _dropSites ) {
					if ( canDrop(context, ds) ) {
						ds.widget.addStyleName(Biosim._connectionIconDragging);
						_styledWidgets.add(ds.widget);
					}
				}
                LogTool.debug("dragStart() override end");
			}
			public void dragEnd() {
				super.dragEnd();
				for ( Widget w : _styledWidgets ) {
					w.removeStyleName(Biosim._connectionIconDragging);
				}
			}
		};

		_delegate.setBehaviorDragProxy(true);
		_delegate.setBehaviorMultipleSelection(false);

		DropAction<Node, biosim.client.model.Label> labelAction = new DropAction<Node, biosim.client.model.Label>() {
			@Override
			public boolean canDrop(Node dragee, biosim.client.model.Label dropTarget) {
				return !(dragee.equals(dropTarget) || dropTarget.hasChild(dragee));
			}
			@Override
			public void processDrop(Node dragee, biosim.client.model.Label dropTarget) {
				databaseAccessLayer.addLink(dropTarget, dragee);
			}
		}; 
		registerDropAction(DndType.Content, DndType.Label, labelAction);
		registerDropAction(DndType.Label, DndType.Label, labelAction);
		
		DropAction<Node, Person> authorizeAction = new DropAction<Node, Person>() {
			@Override
			public boolean canDrop(Node dragee, Person dropTarget) {
				return !(dragee.equals(dropTarget) || dragee.canBeSeenBy(dropTarget) != null);
			}
			@Override
			public void processDrop(Node dragee, Person dropTarget) {
				databaseAccessLayer.addLink(dropTarget, dragee);
			}
		}; 
		registerDropAction(DndType.Label, DndType.Connection, authorizeAction);
		registerDropAction(DndType.Content, DndType.Connection, authorizeAction);
		
		DropAction<Person, Node> authorizeReverseDndAction = new DropAction<Person, Node>() {
			@Override
			public boolean canDrop(Person dragee, Node dropTarget) {
				return !(dropTarget.equals(dragee) || dropTarget.canBeSeenBy(dragee) != null);
			}
			@Override
			public void processDrop(Person dragee, Node dropTarget) {
				databaseAccessLayer.addLink(dragee, dropTarget);
			}
		}; 
		registerDropAction(DndType.Connection, DndType.Label, authorizeReverseDndAction);
		registerDropAction(DndType.Connection, DndType.Content, authorizeReverseDndAction);
		
		DropAction<Node, Node> addToFilterAction = new DropAction<Node, Node>() {
			@Override
			public boolean canDrop(Node dragee, Node dropTarget) {
				return biosim._filtersBar.getFilter().canAddFilter(dragee);
			}
			@Override
			public void processDrop(Node dragee, Node dropTarget) {
				biosim._filtersBar.addToFilter(dragee);
				databaseAccessLayer.fireRefreshContentPane();
			}
		}; 
		registerDropAction(DndType.Label, DndType.Filter, addToFilterAction);
		registerDropAction(DndType.Connection, DndType.Filter, addToFilterAction);
		
		DropAction<Node, Node> scissorsAction = new DropAction<Node, Node>() {
			@Override
			public boolean canDrop(Node dragee, Node dropTarget) {
				Node node;
				if ( dragee != null ) {
					node = dragee;
				} else {
					node = dropTarget;
				}
								
				NodeWidgetBuilder builder = biosim._contentController.builder(node);
				ContentCriteria criteria = builder.getFilterAcceptCriteria();

				if ( criteria == null ) {
					return false;
				} else if ( criteria.connections.size() > 0 ) {
					boolean r = true;
					for ( Person p : criteria.connections ) {
						if ( !p.isParentOf(node) ) {
							r = false;
							break;
						}
					}
					return r;
				} else {
					return true;
				}
			}
			@Override
			public void processDrop(Node dragee, Node dropTarget) {
				final Node DRAGEE = dragee;
				final Node DROP_TARGET = dropTarget;
				
				DndController.this.resetNodeChileCounters();
				
				dropTarget.visitDescendants(new NodeVisitor() {
					@Override
					public void visit(Node node) {
						DndController.this.checkNode(node);
					}
				});
				
				StringBuilder alertText = new StringBuilder();
				alertText.append("Are you sure you want to delete ");
				alertText.append(dropTarget.getName());
				alertText.append("?");
				
				List<String> strings = DndController.this.getNodeChildStrings();
				
				if (strings.size() > 0) {
					alertText.append("<br/>This action will also delete: ");
					for (int i = 0; i < strings.size(); i += 1){
						alertText.append(strings.get(i));
						if (i < strings.size() - 1) {
							alertText.append(", ");
						}
					}
				}
				
				DialogHelper.confirm(alertText.toString(), new Function1<String,Void>() {
					public Void apply(String s) {
						biosim.removeContentLinks(DRAGEE);
						biosim.removeContentLinks(DROP_TARGET);
						return null;
					}
				});
			}
		}; 
		registerDropAction(DndType.Content, DndType.Scissors, scissorsAction);
		registerDropAction(DndType.Scissors, DndType.Content, scissorsAction);

		DropAction<Node, Node> viewConnectionAction = new DropAction<Node, Node>() {
			@Override
			public boolean canDrop(Node dragee, Node dropTarget) {
				return dragee instanceof Person;
			}
			@Override
			public void processDrop(Node dragee, Node dropTarget) {
				ObservableList<Node> labelRoots = biosim.getDatabaseAccessLayer().getLabelRoots();
				if ( !labelRoots.contains(dragee) ) {
					labelRoots.add(dragee);
				}
			}
		}; 
		registerDropAction(DndType.Connection, DndType.ViewConnection, viewConnectionAction);
		
		registerDropSite(DndType.Filter, null, biosim._filtersBar.getPanel());
		this.inititlizeNodeChildCounters();
	}
	
	
	void inititlizeNodeChildCounters() {
		_nodeChildCounters.add(new NodeChildCounter(biosim.client.model.Address.class, "address"));
		_nodeChildCounters.add(new NodeChildCounter(biosim.client.model.Image.class, "image"));
		_nodeChildCounters.add(new NodeChildCounter(biosim.client.model.Label.class, "label"));
		_nodeChildCounters.add(new NodeChildCounter(biosim.client.model.Link.class, "link"));
		_nodeChildCounters.add(new NodeChildCounter(biosim.client.model.Need.class, "need"));
		_nodeChildCounters.add(new NodeChildCounter(biosim.client.model.Offer.class, "offer"));
		_nodeChildCounters.add(new NodeChildCounter(biosim.client.model.Person.class, "person"));
		_nodeChildCounters.add(new NodeChildCounter(biosim.client.model.Phone.class, "phone"));
		_nodeChildCounters.add(new NodeChildCounter(biosim.client.model.TextMessage.class, "message"));
	}
	
	List<String> getNodeChildStrings() {
		ArrayList<String> ret = new ArrayList<String>();
		
		for (int i = 0; i < _nodeChildCounters.size(); i += 1){
			if (!_nodeChildCounters.get(i).toString().isEmpty()) {
				ret.add(_nodeChildCounters.get(i).toString());
			}
		}
		
		return ret;
	}
	
	void resetNodeChileCounters() {
		for (int i = 0; i < _nodeChildCounters.size(); i += 1){
			_nodeChildCounters.get(i).count = 0;
		}
	}
	
	void checkNode(Node node) {
		for (int i = 0; i < _nodeChildCounters.size(); i += 1){
			_nodeChildCounters.get(i).incrementIfOfType(node);
		}		
	}
	
	Node node(DragContext dc) {
		return dragSite(dc).node;
	}
	
	DragSite dragSite(DragContext dc) {
		return _widgetToDragSiteMap.get(dc.draggable);
	}
	
	boolean canDrop(DragContext context, DropSite dropSite) {
		try {
			DropAction<Node, Node> dropAction = dropAction(context, dropSite);
			return dropAction.canDrop(node(context), dropSite.node);
		} catch ( ClassCastException e ) {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	DropAction<Node,Node> dropAction(DragContext dc, DropSite dropSite) {
		Pair<DndType, DndType> key = Pair.create(dragSite(dc).type, dropSite.type);
		DropAction<Node,Node> da = (DropAction<Node,Node>) _actionGrid.get(key);
		if ( da == null ) {
			da = _nullDropAction;
		}
		return da;
	}

	public void makeDraggable(final DndType type, Node node, Widget draggable, Widget dragHandle) {
		DragSite dragSite = new DragSite(type, node, draggable, dragHandle) ;
		_widgetToDragSiteMap.put(draggable, dragSite);
		_widgetToDragSiteMap.put(dragHandle, dragSite);
		_delegate.makeDraggable(draggable, dragHandle);
		_dragSites.add(dragSite);
	}

	public void registerDropAction(DndType dragType, DndType dropType, DropAction<?,?> dropAction) {
		_actionGrid.put(Pair.create(dragType, dropType), dropAction);
	}

	public void registerDropSite(final DndType dropType, Node dropTargetNode, Widget dropTargetWidget) {
		final DropSite dropSite = new DropSite(dropType, dropTargetNode, dropTargetWidget);
		_dropSites.add(dropSite);
		_delegate.registerDropController(new AbstractDropController(dropTargetWidget) {
			Widget _enteredDropTarget;
			String predropStyle = null;
			@Override
			public void onPreviewDrop(DragContext context) throws VetoDragException {
				if ( !canDrop(context, dropSite) ) {
					throw new VetoDragException();
				}
			}
			@Override
			public void onDrop(DragContext context) {
				DragSite dragSite = dragSite(context);
				dropAction(context, dropSite).processDrop(dragSite.node, dropSite.node);
			}
			@Override
			public void onEnter(DragContext context) {
				if ( canDrop(context, dropSite) ) {
					predropStyle = Biosim.getCurrentUiStateCssClass(getDropTarget().getStyleName());
					if(predropStyle != null) {
						getDropTarget().removeStyleName(predropStyle);
					}
					_enteredDropTarget = getDropTarget();
					_enteredDropTarget.addStyleName(Biosim._connectionDropHover);
				}
			}
			@Override
			public void onLeave(DragContext context) {
				if ( _enteredDropTarget != null ) {
					getDropTarget().removeStyleName(Biosim._connectionDropHover);
					if(predropStyle != null) {
						getDropTarget().addStyleName(predropStyle);
					}
				}
				_enteredDropTarget = null;
			}
		});
	}

	public void addDropHandler(DndType dragType, DndType dropType, DropAction<?, ?> dropAction) {
		_actionGrid.put(Pair.create(dragType, dropType), dropAction);
	}

	class DragSite {
		
		final DndType type;
		final Node node;
		final Widget draggable;
		final Widget dragHandle;
		
		public DragSite(DndType type, Node node, Widget draggable, Widget dragHandle) {
			this.type = type;
			this.node = node;
			this.draggable = draggable;
			this.dragHandle = dragHandle;
		}

		@SuppressWarnings("unchecked")
		public boolean isDroppable(DropSite dropSite) {
			DropAction<Node, Node> da = (DropAction<Node, Node>) _actionGrid.get(Pair.create(type, dropSite.type));
			if ( da != null && da.canDrop(node, dropSite.node) ) {
				return true;
			} else {
				return false;
			}
		}
		
	}
	
	class DropSite {
		
		final DndType type;
		final Node node;
		final Widget widget;
		
		public DropSite(DndType type, Node node, Widget widget) {
			this.type = type;
			this.node = node;
			this.widget = widget;
		}
		
	}
	
	class NodeChildCounter {
		Integer count = 0;
		final String kind;
		final Class<?> clazz;
		
		public NodeChildCounter(Class<?> clazz, String kind) {
			this.clazz = clazz;
			this.kind = kind;
		}
		
		public void incrementIfOfType(Object obj) {
			if (this.clazz.getName() ==   obj.getClass().getName()) {
				count += 1;
			}
		}
		
		public String toString() {
			StringBuilder sb = new StringBuilder();
			if (count > 0) {
				sb.append(count.toString() + " " + this.kind);
				if (count > 1) {
					sb.append("s");
				}
			}
			return sb.toString();
		}
	}	
}
