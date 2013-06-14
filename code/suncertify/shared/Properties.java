package suncertify.shared;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This class is responsible for persisting properties of the application between launches.
 * 
 * @author Sean Dunne
 */
public class Properties {

	private static final String PROPS_FILE = "suncertify.properties";

	private java.util.Properties props;

	private static Properties instance;

	static {
		instance = new Properties();
	}

	/**
	 * Private constructor created statically as this is a singleton.
	 */
	private Properties() {
		this.init();
	}

	/**
	 * This method is responsible for initializing the instance. It should read up all the persisted properties and make them available for
	 * use by the application.
	 */
	private void init() {
		this.props = new java.util.Properties();

		File propsFile = new File(PROPS_FILE);
		if (propsFile.exists()) {
			try (InputStream stream = new BufferedInputStream(new FileInputStream(propsFile))) {
				this.props.load(stream);
			} catch (IllegalArgumentException e) {
				App.showError("Cannot read the properties file, it is corrupted.\nTherefore all application settings will return to default.");
			} catch (IOException e) {
				App.showError("Cannot open the properties file for reading.\nTherefore all application settings will return to default.");
			}
		}
	}

	/**
	 * This method can be called to force the properties in memory to be written to disk.
	 */
	public static void save() {
		try (OutputStream stream = new BufferedOutputStream(new FileOutputStream(PROPS_FILE))) {
			instance.props.store(stream, null);
		} catch (IOException e) {
			App.showError("Could not write to the properties file.\nNo changes to application properties will be recorded.");
		}
	}

	/**
	 * Get the value of a saved property.
	 * 
	 * @param key
	 *            The string used to identify this property.
	 * @return The property value.
	 */
	public static String get(String key) {
		return instance.props.getProperty(key);
	}

	/**
	 * Get the value of a saved property. If the property does not exist set the supplied value as the default value.
	 * 
	 * @param key
	 *            The string used to identify this property.
	 * @param defaultValue
	 *            If the property has no value, set this as it's value and return it.
	 * @return The property value.
	 */
	public static String get(String key, String defaultValue) {
		String value = instance.props.getProperty(key);
		if (value == null) {
			set(key, defaultValue);
			value = defaultValue;
		}
		return value;
	}

	/**
	 * Set the value of a property.
	 * 
	 * @param key
	 *            The identifier of this property.
	 * @param value
	 *            The value to set for this property.
	 */
	public static void set(String key, String value) {
		instance.props.setProperty(key, value);
		save();
	}

	/**
	 * Get the boolean value of a property.
	 * 
	 * @param key
	 *            The string used to identify this property.
	 * @return A boolean representing this property.
	 */
	public static boolean getBoolean(String key) {
		return Boolean.parseBoolean(get(key));
	}

	/**
	 * Get the boolean value of a property. If the property does not exist set the supplied value as the default value.
	 * 
	 * @param key
	 *            The string used to identify this property.
	 * @param defaultValue
	 *            If the property has no value, set this as it's value and return it.
	 * @return A boolean representing this property.
	 */
	public static boolean getBoolean(String key, boolean defaultValue) {
		String value = get(key, Boolean.toString(defaultValue));
		return Boolean.parseBoolean(value);
	}

	/**
	 * Set the value of a property to a boolean.
	 * 
	 * @param key
	 *            The identifier of this property.
	 * @param value
	 *            The value to set for this property.
	 */
	public static void setBoolean(String key, boolean value) {
		set(key, Boolean.toString(value));
	}
}