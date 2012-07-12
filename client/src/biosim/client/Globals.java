package biosim.client;

import biosim.client.messages.model.Uid;

public class Globals {
	
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
	
}
