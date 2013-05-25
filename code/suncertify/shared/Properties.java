package suncertify.shared;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Properties {

	private static final String PROPS_FILE = "suncertify.properties";

	private java.util.Properties props;

	private static Properties instance;

	static {
		instance = new Properties();
	}

	private Properties() {
		this.init();
	}

	private void init() {
		this.props = new java.util.Properties();

		File propsFile = new File(PROPS_FILE);
		if (propsFile.exists()) {
			try (InputStream stream = new BufferedInputStream(new FileInputStream(propsFile))) {
				this.props.load(stream);
			} catch (IllegalArgumentException e) {
				App.showError("Cannot read the properties file, it is corrupted.");
			} catch (IOException e) {
				App.showError("Cannot open the properties file for reading.");
			}
		}
	}

	public static void save() {
		try (OutputStream stream = new BufferedOutputStream(new FileOutputStream(PROPS_FILE))) {
			instance.props.store(stream, null);
		} catch (IOException e) {
			App.showError("Could not write to the properties file.");
		}
	}

	public static String get(String key) {
		return instance.props.getProperty(key);
	}

	public static void set(String key, String value) {
		instance.props.setProperty(key, value);
		save();
	}

	public static boolean getBoolean(String key) {
		return Boolean.parseBoolean(get(key));
	}

	public static boolean getBoolean(String key, boolean defaultValue) {
		String value = get(key);
		if (value == null) {
			value = Boolean.toString(defaultValue);
			set(key, value);
		}
		return Boolean.parseBoolean(value);
	}

	public static void set(String key, boolean value) {
		set(key, Boolean.toString(value));
	}
}