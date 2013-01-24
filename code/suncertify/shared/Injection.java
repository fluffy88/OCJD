package suncertify.shared;

import java.util.HashMap;
import java.util.Map;

public class Injection {

	public static final Injection instance = new Injection();

	private Map<String, Object> injectableItems = new HashMap<String, Object>();

	public void add(String key, Object value) {
		injectableItems.put(key, value);
	}

	public Object get(String key) {
		return injectableItems.get(key);
	}
}