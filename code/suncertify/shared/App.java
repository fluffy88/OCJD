package suncertify.shared;

import java.util.HashMap;
import java.util.Map;

public class App {

	public static final App instance = new App();

	private Map<String, Object> injectableItems = new HashMap<String, Object>();

	public void publish(String key, Object value) {
		injectableItems.put(key, value);
	}

	public Object getDependancy(String key) {
		return injectableItems.get(key);
	}
}