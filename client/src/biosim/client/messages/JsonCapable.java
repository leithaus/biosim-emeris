package biosim.client.messages;

import com.google.gwt.json.client.JSONObject;

public interface JsonCapable {

	String getJsonClass();
	JSONObject toJsonObject();
	
}
