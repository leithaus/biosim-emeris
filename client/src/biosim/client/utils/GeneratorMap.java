package biosim.client.utils;

import java.util.HashMap;

@SuppressWarnings("serial")
public abstract class GeneratorMap<K, V> extends HashMap<K, V> {

	@SuppressWarnings("unchecked")
	@Override
	public V get(Object key0) {
		K key = (K) key0;
		V value = super.get(key);
		if ( value == null ) {
			value = generate(key);
			put(key,value);
		}
		return value;
	}
	
	
	public abstract V generate(K k);
	
	
}
