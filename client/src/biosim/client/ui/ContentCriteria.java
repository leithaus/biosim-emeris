package biosim.client.ui;

import java.util.List;

import m3.gwt.lang.ListX;
import biosim.client.model.Person;

public class ContentCriteria {
	public final List<String> paths = ListX.create();
	public final List<biosim.client.model.Label> labels = ListX.create();
	public final List<Person> connections = ListX.create();
}