package biosim.client;

import m3.fj.F1;
import m3.fj.data.FList;
import biosim.client.model.Connection;
import biosim.client.model.Label;
import biosim.client.model.Node;

public interface ServerSideServices {

	void childLabels(Connection connection, Node node, F1<FList<Label>, Void> callback);
	void connections(Connection connection, F1<FList<Connection>, Void> callback);
	void filter(Connection connection, FList<Label> labels, FList<Connection> people, F1<FList<FilterResult>, Void> callback);
	
	static class FilterResult {
		public Node node;
		public FList<Label> labels;
		public FList<Connection> people;
	}
	
}
