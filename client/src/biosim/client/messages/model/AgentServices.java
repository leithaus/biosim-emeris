package biosim.client.messages.model;

import java.util.List;

import m3.fj.data.FList;
import m3.gwt.lang.Function1;

public interface AgentServices {
	
	<T extends MNode> void fetch(Uid uid, Function1<T,Void> asyncCallback);
	<T extends MNode> void fetch(Uid uid, boolean bypassCache, Function1<T,Void> asyncCallback);
	
	void query(List<MNode> labels,  Uid uid, Function1<List<FilterAcceptCriteria>,Void> asyncCallback);

	<T extends MNode> void select(Class<T> clazz, Function1<FList<T>,Void> asyncCallback);

	NodeContainer getNodeContainer();
	
}

