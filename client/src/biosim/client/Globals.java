package biosim.client;

import biosim.client.messages.model.Uid;

public class Globals {

	public static final String _connectionDropHover = "ui-state-active";
	public static final String _connectionIconDragging = "ui-state-highlight";
	public static final String _connectionSelected = "ui-state-focus";
	public static final String _boxStyle = "biosimbox ui-widget-content ui-corner-all";

	private static final Globals _instance = new Globals();
	
	public static Globals get() {
		return _instance;
	}
	
	private Uid _agentUid;

	private Globals() {
	} 
	
	public Uid getAgentUid() {
		return _agentUid;
	}
	
	public void setAgentUid(Uid agentUid) {
		this._agentUid = agentUid;
	}

	public String getCurrentUiStateCssClass(String styleNames) {
		String uiStateCssClass = null;
		if(styleNames != null && styleNames.length() > 0) {
			styleNames = styleNames.trim();
			String[] classes = styleNames.split(" ");
			for(String cssClass : classes) {
				if(cssClass.startsWith("ui-state")) {
					uiStateCssClass = cssClass;
				}
			}
		}
		return uiStateCssClass;
	}
	
}
