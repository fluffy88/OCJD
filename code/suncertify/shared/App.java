package suncertify.shared;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

public class App {

	public static final String LOGGER = "scr.logger";

	private static Map<String, Object> injectableItems = new HashMap<String, Object>();

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
		JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
		System.exit(1);
	}

	public static void logWarning(String msg) {
		Logger.getLogger(LOGGER).log(Level.WARNING, msg);
	}

	public static void logError(String msg) {
		Logger.getLogger(LOGGER).log(Level.SEVERE, msg);
	}

	public static void log(String msg, Level lvl) {
		Logger.getLogger(LOGGER).log(lvl, msg);
	}
}