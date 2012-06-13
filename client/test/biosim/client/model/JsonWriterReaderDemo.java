package biosim.client.model;

import m3.gwt.props.json.JsonSerializer;
import biosim.client.utils.BiosimSerializer;

public class JsonWriterReaderDemo {

	public static void main(String[] args) {
		new JsonWriterReaderDemo().run();
	}

	void run() {
		
		UidList l = new UidList();
		l.getUids().add(Uid.random());
		l.getUids().add(Uid.random());
		
		JsonSerializer serializer = BiosimSerializer.get();
		
		String json = serializer.toJson(l);
		
		UidList l2 = serializer.createReader().read(json);
		
		l2.toString();
		
	}
	
}
