package biosim.client.utils;

import m3.gwt.props.json.JsonReader;
import m3.gwt.props.json.JsonSerializer;
import m3.gwt.props.json.JsonWriter;
import m3.gwt.props.json.SingleTypeHandler;
import biosim.client.BiosimUberContext;
import biosim.client.model.Uid;

public class BiosimSerializer extends JsonSerializer {

	private static final BiosimSerializer _instance = new BiosimSerializer();
	public static BiosimSerializer get() {
		return _instance;
	}
	
	public BiosimSerializer() {
		super(BiosimUberContext.get());
		
		add(new SingleTypeHandler(Uid.class) {
			@Override
			public void write(Object javaValue, JsonWriter writer, Class<?> type, Class<?> elementType) {
				Uid uid = (Uid) javaValue;
				writer.write(uid.asString(), String.class, null);
			}
			@Override
			public Object read(Object jsonValue, JsonReader reader, Class<?> type, Class<?> elementType) {
				return new Uid((String)jsonValue);
			}
		});
		
	}
	
	@Override
	public Object postJsonLoad(Object o) {
		return o;
	}
	
}
