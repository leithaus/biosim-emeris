package biosim.client.ui;

import java.util.Map;
import java.util.Map.Entry;

import m3.gwt.lang.MapX;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class CustomTabPanel extends FlowPanel {
	private FlowPanel _tabBar;
//	private FlowPanel _rootContentPanel;
	private SimplePanel _contentPanel;
	private SimplePanel _clearPanel;
	private Map<String, FlowPanel> _tabToWidget = MapX.create();
	private Map<Integer, String> _indexToTab = MapX.create();
	private Map<String, Widget> _tabToTab = MapX.create();
	private String _tabStyleNames = "";
	private String _selectedTabStyleNames = "";
	
	public CustomTabPanel() {
		init();
	}
	
	private void init() {
		_tabBar = new FlowPanel();
		_tabBar.setStylePrimaryName("tabBar");
		_tabBar.setStyleName("ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all");
//		_filtersBar.setStyleName("ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all");
//		_rootContentPanel = new FlowPanel();
//		_rootContentPanel.getElement().getStyle().setPaddingTop(3, Unit.PX);
		_contentPanel = new SimplePanel();
		_contentPanel.setStylePrimaryName("tabContent");
		_clearPanel = new SimplePanel();
		_clearPanel.setStylePrimaryName("clearBoth");
		add(_tabBar);
		add(_clearPanel);
		add(_contentPanel);
//		add(_rootContentPanel);
//		_rootContentPanel.add(_clearPanel);
//		_rootContentPanel.add(_contentPanel);
	}
	
	public FlowPanel getTabBar() {
		return _tabBar;
	}
	
	public Widget getContentPanel() {
		return _contentPanel;
	}
	
	public FlowPanel createTab(final String tabName) {
		Label l = new Label(tabName);
		FocusPanel sp = new FocusPanel(l);
		sp.setStyleName("tabBar-tab " + _tabStyleNames);
		l.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Integer index = 0;
				for(Entry<Integer,String> entry : _indexToTab.entrySet()) {
					if(entry.getValue().equals(tabName)) {
						index = entry.getKey();
						break;
					}
				}
				selectTab(index);
			}
		});
		sp.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Integer index = 0;
				for(Entry<Integer,String> entry : _indexToTab.entrySet()) {
					if(entry.getValue().equals(tabName)) {
						index = entry.getKey();
						break;
					}
				}
				selectTab(index);
			}
		});
		FlowPanel fp = new FlowPanel();
		fp.setSize("100%", "100%");
		_tabToWidget.put(tabName, fp);
		_tabBar.add(sp);
		int currentSize = _indexToTab.size();
		_indexToTab.put(currentSize, tabName);
		_tabToWidget.put(tabName, fp);
		_tabToTab.put(tabName, sp);
		return fp;
	}
	
	public void selectTab(int index) {
		String tabName = _indexToTab.get(index);
		Widget w = _tabToWidget.get(tabName);
		Widget t = _tabToTab.get(tabName);
		for(Entry<String,Widget> entry : _tabToTab.entrySet()) {
			entry.getValue().setStyleName("tabBar-tab " + _tabStyleNames);
		}
		t.setStyleName("tabBar-tab " + _selectedTabStyleNames);
		_contentPanel.clear();
		_contentPanel.add(w);
	}
	
	public void setTabStyleNames(String tabStyleNames) {
		_tabStyleNames = tabStyleNames;
	}
	
	public void setSelectedTabStyleNames(String selectedTabStyleNames) {
		_selectedTabStyleNames = selectedTabStyleNames;
	}
}
