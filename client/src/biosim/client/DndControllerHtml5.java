package biosim.client;

import java.util.List;
import java.util.Map;
import java.util.Set;

import m3.gwt.lang.Function1;
import m3.gwt.lang.ListX;
import m3.gwt.lang.LogTool;
import m3.gwt.lang.MapX;
import biosim.client.messages.model.FilterAcceptCriteria;
import biosim.client.messages.model.MConnection;
import biosim.client.messages.model.MLabel;
import biosim.client.messages.model.MLink;
import biosim.client.messages.model.MNode;
import biosim.client.messages.model.Uid;
import biosim.client.ui.NodeWidgetBuilder;
import biosim.client.ui.dnd.DndType;
import biosim.client.ui.dnd.DropAction;
import biosim.client.utils.DialogHelper;
import biosim.client.utils.Pair;
import biosim.client.utils.SetX;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.DragEndEvent;
import com.google.gwt.event.dom.client.DragEndHandler;
import com.google.gwt.event.dom.client.DragEnterEvent;
import com.google.gwt.event.dom.client.DragEnterHandler;
import com.google.gwt.event.dom.client.DragLeaveEvent;
import com.google.gwt.event.dom.client.DragLeaveHandler;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragOverHandler;
import com.google.gwt.event.dom.client.DragStartEvent;
import com.google.gwt.event.dom.client.DragStartHandler;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.DropHandler;
import com.google.gwt.user.client.ui.Widget;

import static com.google.gwt.query.client.GQuery.*;
import com.google.gwt.query.client.Function;

public class DndControllerHtml5 implements DndController {
	
	final List<DropSite> _dropSites = ListX.create();
	final List<DragSite> _dragSites = ListX.create();

	final Set<DropSite> _activeDropSites = SetX.create();
	
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
	
	DragSite _currentDragSite;
	
	public DndControllerHtml5(final Callback callback) {
		_callback = callback;

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
					_callback.getLabelTreeBuilder().addRootLabelsForConnection((MConnection) dragee);
				}
			}
		}; 
		registerDropAction(DndType.Connection, DndType.ViewConnection, viewConnectionAction);
		
		registerDropSite(DndType.Filter, null, callback.getFilterBar().getPanel());
	}
	
	boolean canDrop(DropSite dropSite) {
		try {
			DropAction<MNode, MNode> dropAction = dropAction(dropSite);
			return dropAction.canDrop(_currentDragSite.node, dropSite.node);
		} catch ( ClassCastException e ) {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	DropAction<MNode,MNode> dropAction(DropSite dropSite) {
		Pair<DndType, DndType> key = Pair.create(_currentDragSite.type, dropSite.type);
		DropAction<MNode,MNode> da = (DropAction<MNode,MNode>) _actionGrid.get(key);
		if ( da == null ) {
			da = _nullDropAction;
		}
		return da;
	}

	public void makeDraggable(final DndType type, final MNode node, final Widget draggable, Widget dragHandle0) {
		
		final Widget dragHandle;
		if ( dragHandle0 == null ) {
			dragHandle = draggable;
		} else {
			dragHandle = dragHandle0;
		}
		
		final DragSite dragSite = new DragSite(type, node, draggable, dragHandle) ;
		
		dragHandle.addDomHandler(new DragStartHandler() {
			@Override
			public void onDragStart(DragStartEvent event) {
				_currentDragSite = dragSite;
				dragStart();
				event.setData("text", node.getUid().getValue());
				
				int x = event.getNativeEvent().getClientX();
				int y = event.getNativeEvent().getClientY();
				
				int dx = x - draggable.getAbsoluteLeft();
				int dy = y - draggable.getAbsoluteTop();
				
				event.getDataTransfer().setDragImage(draggable.getElement(), dx, dy);
				
			}
		}, DragStartEvent.getType());
		
		dragHandle.addDomHandler(new DragEndHandler() {
			@Override
			public void onDragEnd(DragEndEvent event) {
				_currentDragSite = null;
				dragEnd();
			}
		}, DragEndEvent.getType());
		
		_dragSites.add(dragSite);
	}

	public void registerDropAction(DndType dragType, DndType dropType, DropAction<?,?> dropAction) {
		_actionGrid.put(Pair.create(dragType, dropType), dropAction);
	}

	public void registerDropSite(final DndType dropType, final MNode dropTargetNode, final Widget dropTargetWidget) {
		
		final DropSite dropSite = new DropSite(dropType, dropTargetNode, dropTargetWidget);
		_dropSites.add(dropSite);

		dropTargetWidget.addDomHandler(new DragEnterHandler() {
			@Override
			public void onDragEnter(DragEnterEvent event) {
				LogTool.debug("onDragEnter for " + dropTargetNode);
				event.preventDefault();
				
				if (canDrop(dropSite)) {
					dropSite.dragEnter();
				}
			}
		}, DragEnterEvent.getType());

		dropTargetWidget.addDomHandler(new DragOverHandler() {
			@Override
			public void onDragOver(DragOverEvent event) {
				event.preventDefault();
			}
		}, DragOverEvent.getType());
		
		dropTargetWidget.addDomHandler(new DragLeaveHandler() {
			@Override
			public void onDragLeave(DragLeaveEvent event) {
				LogTool.debug("onDragLeave for " + dropTargetNode);
				event.preventDefault();
				dropSite.dragLeave(event);
			}
		}, DragLeaveEvent.getType());
		
		dropTargetWidget.addDomHandler(new DropHandler() {
			@Override
			public void onDrop(DropEvent event) {
				LogTool.debug("onDrop for " + dropSite);
				event.preventDefault();
				dropAction(dropSite).processDrop(_currentDragSite.node, dropSite.node);
				dropSite.drop();
			}
		}, DropEvent.getType());		
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
	}
	
	void dragStart() {
	    LogTool.debug("dragStart()");
	    _activeDropSites.clear();
		for (DropSite ds : _dropSites) {
			if (canDrop(ds)) {
				ds.dragStart();
				_activeDropSites.add(ds);
			}
		}
	}
	
	void dragEnd() {
	    LogTool.debug("dragEnd()");
		for (DropSite ds : _activeDropSites) {
			ds.dragEnd();
		}
	}

	class DropSite {
		final DndType type;
		final MNode node;
		final Widget widget;
		
		String preDropStyle;
		
		public DropSite(DndType type, MNode node, Widget widget) {
			this.type = type;
			this.node = node;
			this.widget = widget;
		}
		
		void dragStart() {
			widget.addStyleName(Globals._connectionIconDragging);
		}
		
		void dragEnter() {
			if (preDropStyle == null) {
				preDropStyle = Globals.get().getCurrentUiStateCssClass(widget.getStyleName());
			}
			if (preDropStyle != null) {
				widget.removeStyleName(preDropStyle);
			}
			widget.addStyleName(Globals._connectionDropHover);
		}
		
		private void resetDragStyle() {
			widget.removeStyleName(Globals._connectionDropHover);
			if (preDropStyle != null) {
				widget.addStyleName(preDropStyle);
			}			
		}

		void dragLeave(DragLeaveEvent event) {
			Integer top    = widget.getAbsoluteTop();
			Integer left   = widget.getAbsoluteLeft();
			Integer width  = widget.getOffsetWidth();
			Integer height = widget.getOffsetHeight();
			
			NativeEvent e = event.getNativeEvent();

			// Check the mouseEvent coordinates are outside of the rectangle
            if (e.getClientX() > left + width  || e.getClientX() < left
             || e.getClientY() > top  + height || e.getClientY() < top) {
            	resetDragStyle();
            }				
		}

		void drop() {
			resetDragStyle();
			/*
			//display the text with effects and animate its background color
	        $(widget.getElement()).as(Effects)
	          .clipDown()
	          .animate("backgroundColor: 'yellow'", 500)
	          .delay(1000)
	          .animate("backgroundColor: '#fff'", 1500, new Function(){
	        	  public void f(){
	        	       resetDragStyle();
	        	  }
	          });
	          */	        
		}
		
		void dragEnd() {
			widget.removeStyleName(Globals._connectionIconDragging);
		}
	}
	
}
