package biosim.client.ui;

import m3.gwt.lang.Function1;

import com.google.gwt.event.dom.client.HasMouseMoveHandlers;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;


public class ToolTipListener implements MouseOverHandler, MouseOutHandler, MouseMoveHandler {

	private static final String DEFAULT_TOOLTIP_STYLE = "TooltipPopup";
	private static final int DEFAULT_OFFSET_X = 10;
	private static final int DEFAULT_OFFSET_Y = 35;

	class Tooltip extends PopupPanel {
		Tooltip( int left
				, int top
				, final Widget popupWidget
				, final String styleName
		) {
			super(true);

			add(popupWidget);

			setPopupPosition(left, top);
			setStyleName(styleName);
			addStyleName("ui-corner-all");
		}
	}

	private Tooltip _tooltip;
	private Function1<Widget,Widget> _widgetCreator;
	private String _styleName;
	private int _popupDelay;
	private int _offsetX = DEFAULT_OFFSET_X;
	private int _offsetY = DEFAULT_OFFSET_Y;
	
	Timer _currentPopupTimer;

	int _relativeX;
	int _relativeY;
	
	public ToolTipListener(Function1<Widget,Widget> widgetCreator) {
		this(500, DEFAULT_TOOLTIP_STYLE, widgetCreator);
	}

	public ToolTipListener(int popupDelay, Function1<Widget,Widget> widgetCreator) {
		this(popupDelay, DEFAULT_TOOLTIP_STYLE, widgetCreator);
	}

	public ToolTipListener(int popupDelay, String styleName, Function1<Widget,Widget> widgetCreator) {
		_widgetCreator = widgetCreator;
		_popupDelay = popupDelay;
		_styleName = styleName;
	}

	@Override
	public void onMouseMove(MouseMoveEvent e) {
		setMousePosition(e);
	}

	private void setMousePosition(MouseEvent<?> e) {
		_relativeX = e.getRelativeX(e.getRelativeElement());
		_relativeY = e.getRelativeY(e.getRelativeElement());
	}
	
	@Override
	public void onMouseOver(final MouseOverEvent e) {
		if (_tooltip != null) {
			_tooltip.hide();
		}
		final Widget source = (Widget)e.getSource();
		setMousePosition(e);
		_currentPopupTimer = new Timer() {
			public void run() {
				final int left = source.getAbsoluteLeft() + _relativeX + _offsetX;
				final int top = source.getAbsoluteTop() + _relativeY + _offsetY;
				Widget tooltipWidget = _widgetCreator.apply(source);
				if ( tooltipWidget != null ) {
					_tooltip = new Tooltip(left, top, tooltipWidget, _styleName);
					_tooltip.show();
					_currentPopupTimer = null;
				}
			}
		};
		_currentPopupTimer.schedule(_popupDelay);
	}
	
	@Override
	public void onMouseOut(MouseOutEvent arg0) {
		if (_tooltip != null) {
			_tooltip.hide();
		}
		if ( _currentPopupTimer != null ) {
			_currentPopupTimer.cancel();
			_currentPopupTimer = null;
		}
	}

	public void attach(Widget widget) {
		((HasMouseOverHandlers)widget).addMouseOverHandler(this);
		((HasMouseOutHandlers)widget).addMouseOutHandler(this);
		((HasMouseMoveHandlers)widget).addMouseMoveHandler(this);
	}
	
	public String getStyleName() {
		return _styleName;
	}

	public void setStyleName(String styleName) {
		this._styleName = styleName;
	}

	public int getOffsetX() {
		return _offsetX;
	}

	public void setOffsetX(int offsetX) {
		this._offsetX = offsetX;
	}

	public int getOffsetY() {
		return _offsetY;
	}

	public void setOffsetY(int offsetY) {
		this._offsetY = offsetY;
	}
}
