package biosim.client;

import java.util.List;
import java.util.Map;
import java.util.Set;

import m3.gwt.lang.Function1;
import m3.gwt.lang.ListX;
import m3.gwt.lang.LogTool;
import m3.gwt.lang.MapX;
import biosim.client.messages.model.FilterAcceptCriteria;
import biosim.client.messages.model.LocalAgent;
import biosim.client.messages.model.MConnection;
import biosim.client.messages.model.MLabel;
import biosim.client.messages.model.MLink;
import biosim.client.messages.model.MNode;
import biosim.client.messages.model.Uid;
import biosim.client.ui.FilterBar;
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
	
	final DropAction<MNode, MNode> _nullDropAction = new DropAction<MNode, MNode>() {
		@Override
		public boolean canDrop(MNode dragee, MNode dropTarget) {
			return false;
		}
		@Override
		public void processDrop(MNode draggee, MNode dropTarget) {
			throw new RuntimeException("this should never happen");
		}
	};
	
	final Callback _callback;
	
	public DndController(AbsolutePanel boundaryPanel, final Callback callback) {
		_callback = callback;
		_delegate = new MyDragController(boundaryPanel, false) {
			public void dragStart() {
			    LogTool.debug("dragStart() override start");
				_styledWidgets.clear();
				super.dragStart();
				for ( DropSite ds : _dropSites ) {
					if ( canDrop(context, ds) ) {
						ds.widget.addStyleName(Globals._connectionIconDragging);
						_styledWidgets.add(ds.widget);
					}
				}
                LogTool.debug("dragStart() override end");
			}
			public void dragEnd() {
				super.dragEnd();
				for ( Widget w : _styledWidgets ) {
					w.removeStyleName(Globals._connectionIconDragging);
				}
			}
		};

		_delegate.setBehaviorDragProxy(true);
		_delegate.setBehaviorMultipleSelection(false);

		DropAction<MNode, MLabel> labelAction = new DropAction<MNode, MLabel>() {
			@Override
			public boolean canDrop(MNode dragee, MLabel dropTarget) {
				return !(dragee.equals(dropTarget) || dropTarget.hasChild(dragee));
			}
			@Override
			public void processDrop(MNode dragee, MLabel dropTarget) {
				callback.getLocalAgent().insertOrUpdate(new MLink(dropTarget, dragee));
			}
		}; 
		registerDropAction(DndType.Content, DndType.Label, labelAction);
		registerDropAction(DndType.Label, DndType.Label, labelAction);
		
		DropAction<MNode, MConnection> authorizeAction = new DropAction<MNode, MConnection>() {
			@Override
			public boolean canDrop(MNode dragee, MConnection dropTarget) {
				return !(dragee.equals(dropTarget) || dragee.canBeSeenBy(dropTarget) != null);
			}
			@Override
			public void processDrop(MNode dragee, MConnection dropTarget) {
				callback.getLocalAgent().insertOrUpdate(new MLink(dropTarget, dragee));
			}
		}; 
		registerDropAction(DndType.Label, DndType.Connection, authorizeAction);
		registerDropAction(DndType.Content, DndType.Connection, authorizeAction);
		
		DropAction<MConnection, MNode> authorizeReverseDndAction = new DropAction<MConnection, MNode>() {
			@Override
			public boolean canDrop(MConnection dragee, MNode dropTarget) {
				return !(dropTarget.equals(dragee) || dropTarget.canBeSeenBy(dragee) != null);
			}
			@Override
			public void processDrop(MConnection dragee, MNode dropTarget) {
				callback.getLocalAgent().insertOrUpdate(new MLink(dragee, dropTarget));
			}
		}; 
		registerDropAction(DndType.Connection, DndType.Label, authorizeReverseDndAction);
		registerDropAction(DndType.Connection, DndType.Content, authorizeReverseDndAction);
		
		DropAction<MNode, MNode> addToFilterAction = new DropAction<MNode, MNode>() {
			@Override
			public boolean canDrop(MNode dragee, MNode dropTarget) {
				return callback.getFilterBar().getFilter().canAddFilter(dragee);
			}
			@Override
			public void processDrop(MNode dragee, MNode dropTarget) {
				callback.getFilterBar().addToFilter(dragee);
			}
		}; 
		registerDropAction(DndType.Label, DndType.Filter, addToFilterAction);
		registerDropAction(DndType.Connection, DndType.Filter, addToFilterAction);
		
		DropAction<MNode, MNode> scissorsAction = new DropAction<MNode, MNode>() {
			@Override
			public boolean canDrop(MNode dragee, MNode dropTarget) {
				MNode node;
				if ( dragee != null ) {
					node = dragee;
				} else {
					node = dropTarget;
				}
								
				NodeWidgetBuilder builder = callback.getContentController().builder(node);
				FilterAcceptCriteria criteria = builder.getFilterAcceptCriteria();

				if ( criteria == null ) {
					return false;
				} else if ( criteria.getConnections().size() > 0 ) {
					boolean r = true;
					for ( Uid connUid : criteria.getConnections() ) {
						MConnection p = _callback.getLocalAgent().getAgentServices().cacheFetch(connUid);
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
			public void processDrop(MNode dragee, MNode dropTarget) {
				final MNode DRAGEE = dragee;
				final MNode DROP_TARGET = dropTarget;
				
				StringBuilder alertText = new StringBuilder();
				alertText.append("Are you sure you want to delete ");
				if (dropTarget.getName() == null) {
					alertText.append("this node?");
				} else {
					alertText.append(dropTarget.getName() + "?");
				}

				if ( Boolean.TRUE ) {
					throw new RuntimeException("implement a proper algorithm to determine if we create any orphans with this action");
				}
				List<String> strings = ListX.create(); //DndController.this.getNodeChildStrings();
				
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
						callback.removeContentLinks(DRAGEE);
						callback.removeContentLinks(DROP_TARGET);
						return null;
					}
				});
			}
		}; 
		registerDropAction(DndType.Content, DndType.Scissors, scissorsAction);
		registerDropAction(DndType.Scissors, DndType.Content, scissorsAction);

		DropAction<MNode, MNode> viewConnectionAction = new DropAction<MNode, MNode>() {
			@Override
			public boolean canDrop(MNode dragee, MNode dropTarget) {
				return dragee instanceof MConnection;
			}
			@Override
			public void processDrop(MNode dragee, MNode dropTarget) {
				if (dragee instanceof MConnection) {
					Biosim.get().getLabelTreeBuilder().addRootLabelsForAgent(((MConnection) dragee).getRemoteAgent());
				}
			}
		}; 
		registerDropAction(DndType.Connection, DndType.ViewConnection, viewConnectionAction);
		
		registerDropSite(DndType.Filter, null, callback.getFilterBar().getPanel());
	}
	
	MNode node(DragContext dc) {
		return dragSite(dc).node;
	}
	
	DragSite dragSite(DragContext dc) {
		return _widgetToDragSiteMap.get(dc.draggable);
	}
	
	boolean canDrop(DragContext context, DropSite dropSite) {
		try {
			DropAction<MNode, MNode> dropAction = dropAction(context, dropSite);
			return dropAction.canDrop(node(context), dropSite.node);
		} catch ( ClassCastException e ) {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	DropAction<MNode,MNode> dropAction(DragContext dc, DropSite dropSite) {
		Pair<DndType, DndType> key = Pair.create(dragSite(dc).type, dropSite.type);
		DropAction<MNode,MNode> da = (DropAction<MNode,MNode>) _actionGrid.get(key);
		if ( da == null ) {
			da = _nullDropAction;
		}
		return da;
	}

	public void makeDraggable(final DndType type, MNode node, Widget draggable, Widget dragHandle) {
		DragSite dragSite = new DragSite(type, node, draggable, dragHandle) ;
		_widgetToDragSiteMap.put(draggable, dragSite);
		_widgetToDragSiteMap.put(dragHandle, dragSite);
		_delegate.makeDraggable(draggable, dragHandle);
		_dragSites.add(dragSite);
	}

	public void registerDropAction(DndType dragType, DndType dropType, DropAction<?,?> dropAction) {
		_actionGrid.put(Pair.create(dragType, dropType), dropAction);
	}

	public void registerDropSite(final DndType dropType, MNode dropTargetNode, Widget dropTargetWidget) {
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
					predropStyle = Globals.get().getCurrentUiStateCssClass(getDropTarget().getStyleName());
					if(predropStyle != null) {
						getDropTarget().removeStyleName(predropStyle);
					}
					_enteredDropTarget = getDropTarget();
					_enteredDropTarget.addStyleName(Globals._connectionDropHover);
				}
			}
			@Override
			public void onLeave(DragContext context) {
				if ( _enteredDropTarget != null ) {
					getDropTarget().removeStyleName(Globals._connectionDropHover);
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
		final MNode node;
		final Widget draggable;
		final Widget dragHandle;
		
		public DragSite(DndType type, MNode node, Widget draggable, Widget dragHandle) {
			this.type = type;
			this.node = node;
			this.draggable = draggable;
			this.dragHandle = dragHandle;
		}

		@SuppressWarnings("unchecked")
		public boolean isDroppable(DropSite dropSite) {
			DropAction<MNode, MNode> da = (DropAction<MNode, MNode>) _actionGrid.get(Pair.create(type, dropSite.type));
			if ( da != null && da.canDrop(node, dropSite.node) ) {
				return true;
			} else {
				return false;
			}
		}
		
	}
	
	class DropSite {
		
		final DndType type;
		final MNode node;
		final Widget widget;
		
		public DropSite(DndType type, MNode node, Widget widget) {
			this.type = type;
			this.node = node;
			this.widget = widget;
		}
		
	}
	
	public static interface Callback {
		FilterBar getFilterBar();
		void removeContentLinks(MNode node);
		ContentController getContentController();
		LocalAgent getLocalAgent();
	}
	
}
