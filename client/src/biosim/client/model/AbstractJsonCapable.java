package biosim.client.model;

import biosim.client.messages.JsonCapable;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public abstract class AbstractJsonCapable implements JsonCapable {

	public JSONObject toJsonObject() {
		JSONObject jo = new JSONObject();
		jo.put("jsonClass", new JSONString(getJsonClass()));
		loadJsonObject(jo);
		return jo;
	}

	protected abstract void loadJsonObject(JSONObject jo);

}
