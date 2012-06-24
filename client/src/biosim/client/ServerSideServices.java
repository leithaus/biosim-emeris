package biosim.client;

import biosim.client.model.Label;
import biosim.client.model.Node;
import biosim.client.model.Person;
import m3.fj.F1;
import m3.fj.data.FList;

public interface ServerSideServices {

	void childLabels(Person connection, Node node, F1<FList<Label>, Void> callback);
	void connections(Person connection, F1<FList<Person>, Void> callback);
	void filter(Person connection, FList<Label> labels, FList<Person> people, F1<FList<FilterResult>, Void> callback);
	
	static class FilterResult {
		public Node node;
		public FList<Label> labels;
		public FList<Person> people;
	}
	
}
