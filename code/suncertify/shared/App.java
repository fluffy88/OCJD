package suncertify.shared;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

/**
 * This class is a utility class for the overall application. Providing convenience methods for dependency injection, logging and displaying
 * error messages.
 * 
 * @author Sean Dunne
 */
public class App {

	/** The applications logging name. */
	public static final String LOGGER = "scr.logger";

	private static Map<String, Object> injectableItems = new HashMap<>();

	/** The name used to publish the DataService instance */
	public static final String DEP_DATASERVICE = "dataservice";
	/** The name used to publish the Client instance */
	public static final String DEP_CLIENT_APPLICATION = "client.application";
	/** The name used to publish the Server instance */
	public static final String DEP_SERVER_APPLICATION = "server.application";
	/** The name used to publish the TableModel instance */
	public static final String DEP_TABLE_MODEL = "search.results.tablemodel";

	/** The name used to persist the hostname of the remote server */
	public static final String PROP_SERVER_HOSTNAME = "server.hostname";
	/** The name used to persist the location of the database file */
	public static final String PROP_DB_LOCATION = "database.location";
	/** The name used to persist the whether the state of the Exact match checkbox on the Client UI */
	public static final String PROP_EXACT_MATCH = "exact.match.enabled";

	/**
	 * This method globally publishes objects that can be used by any other classes in the application.
	 * 
	 * @param key
	 *            The String used to identify the published object.
	 * @param value
	 *            The object you wish to publish.
	 */
	public static void publish(final String key, final Object value) {
		injectableItems.put(key, value);
	}

	/**
	 * The method is used to get an object that has been published via the {@link #publish(String, Object)} method.
	 * 
	 * @param key
	 *            The key used to publish the object.
	 * @return The object that was published or null if not found.
	 */
	public static Object getDependancy(final String key) {
		return injectableItems.get(key);
	}

	/**
	 * This method is a convenience method to show an error dialog to the user.
	 * 
	 * @param msg
	 *            The message you wish to display.
	 */
	public static void showError(final String msg) {
		logWarning(msg);
		JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * As with {@link #showError(String)} this method is a convenience method to show an error dialog to the user. However once the user
	 * dismisses the error dialog this method will then exit the application with an error code of 1.
	 * 
	 * @param msg
	 *            The message you wish to display before exiting.
	 */
	public static void showErrorAndExit(final String msg) {
		logError(msg);
		JOptionPane.showMessageDialog(null, msg + "\nApplication will now exit!", "Error", JOptionPane.ERROR_MESSAGE);
		System.exit(1);
	}

	/**
	 * This is a convenience method to log a warning message to the application log. It uses the {@link Level#WARNING} as it's logging
	 * level.
	 * 
	 * @param msg
	 *            The message to log.
	 */
	public static void logWarning(final String msg) {
		log(msg, Level.WARNING);
	}

	/**
	 * This is a convenience method to log an application error to the application log. It uses the {@link Level#SEVERE} as it's logging
	 * level.
	 * 
	 * @param msg
	 *            The message to log.
	 */
	public static void logError(final String msg) {
		log(msg, Level.SEVERE);
	}

	/**
	 * This is a convenience method to log a message with the specified log severity to the application log.
	 * 
	 * @param msg
	 *            The message to log.
	 * @param lvl
	 *            The logging level to use.
	 */
	public static void log(final String msg, final Level lvl) {
		Logger.getLogger(LOGGER).log(lvl, msg);
	}
}