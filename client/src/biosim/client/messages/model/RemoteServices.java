package biosim.client.messages.model;

import m3.gwt.lang.Function1;

public interface RemoteServices {
	
	<T extends MNode> void fetch(Uid uid, Function1<T,Void> asyncCallback);
	<T extends MNode> void fetch(Iterable<Uid> uids, boolean bypassCache, Function1<Iterable<T>, Void> asyncCallback);

	void query(Iterable<MNode> labels,  Uid uid, Function1<Iterable<FilterAcceptCriteria>,Void> asyncCallback);
	<T extends MNode> void select(Class<T> clazz, Function1<Iterable<T>,Void> asyncCallback);
	
	void insertOrUpdate(MNode...nodes);
	void insertTextNode(MNode parent, String text);
	void insertChild(MNode parent, MNode child);
	void removeLink(MNode p, MNode node);
	
	void rootLabels(Uid uid, Function1<Iterable<MLabel>, Void> asyncCallback);

}

