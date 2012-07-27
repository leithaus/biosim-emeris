package biosim.client.messages.model;

import m3.gwt.lang.Function1;
import m3.gwt.lang.Function2;

public interface AgentServices {
	
	Uid getAgentUid();
	Uid getConnectionUid();
	
	<T extends MNode> void fetch(Uid uid, Function1<T,Void> asyncCallback);
	<T extends MNode> void fetch(Iterable<Uid> uids, boolean bypassCache, Function1<Iterable<T>, Void> asyncCallback);

	<T extends MNode> void select(Class<T> clazz, Function1<Iterable<T>,Void> asyncCallback);
	
	void query(Iterable<MNode> nodes, Uid uid, Function2<Uid,Iterable<FilterAcceptCriteria>,Void> asyncCallback);

	NodeContainer getNodeContainer();
		
	<T extends MNode> T cacheFetch(Uid uid);

	void getRemoteConnection(Function1<MConnection,Void> asyncCallback);
	
}

