package biosim.client.ui;

import java.util.List;

import m3.gwt.lang.ListX;
import biosim.client.messages.model.MConnection;
import biosim.client.messages.model.MLabel;

public class ContentCriteria {
	public final List<String> paths = ListX.create();
	public final List<MLabel> labels = ListX.create();
	public final List<MConnection> connections = ListX.create();
}