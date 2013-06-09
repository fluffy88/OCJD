package suncertify.shared;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

public class App {

	public static final String LOGGER = "scr.logger";

	private static Map<String, Object> injectableItems = new HashMap<>();
	public static final String DEP_DATASERVICE = "dataservice";
	public static final String DEP_CLIENT_APPLICATION = "client.application";
	public static final String DEP_SERVER_APPLICATION = "server.application";
	public static final String DEP_TABLE_MODEL = "search.results.tablemodel";

	public static final String PROP_SERVER_HOSTNAME = "server.hostname";
	public static final String PROP_DB_LOCATION = "database.location";
	public static final String PROP_EXACT_MATCH = "exact.match.enabled";

	public static void publish(String key, Object value) {
		injectableItems.put(key, value);
	}

	public static Object getDependancy(String key) {
		return injectableItems.get(key);
	}

	public static void showError(String msg) {
		logWarning(msg);
		JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
	}

	public static void showErrorAndExit(String msg) {
		logError(msg);
		JOptionPane.showMessageDialog(null, msg + "\nApplication will now exit!", "Error", JOptionPane.ERROR_MESSAGE);
		System.exit(1);
	}

	public static void logWarning(String msg) {
		log(msg, Level.WARNING);
	}

	public static void logError(String msg) {
		log(msg, Level.SEVERE);
	}

	public static void log(String msg, Level lvl) {
		Logger.getLogger(LOGGER).log(lvl, msg);
	}
}