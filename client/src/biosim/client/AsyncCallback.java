package biosim.client;

import m3.gwt.lang.Function1;

public abstract class AsyncCallback<T> extends Function1<T,Void> {

	public static <T> AsyncCallback<T> noop() {
		return new AsyncCallback<T>() {
			public Void apply(T t) {
				return null;
			}
		};
	}
	
}
