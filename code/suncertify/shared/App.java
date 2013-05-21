package suncertify.shared;

import java.util.HashMap;
import java.util.Map;

public class App {

	private static Map<String, Object> injectableItems = new HashMap<String, Object>();

	public static void publish(String key, Object value) {
		injectableItems.put(key, value);
	}

	public static Object getDependancy(String key) {
		return injectableItems.get(key);
	}
}