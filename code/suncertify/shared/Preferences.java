package suncertify.shared;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class Preferences {

	private static final String PROPS_FILE = "suncertify.properties";

	private Properties props;

	private static Preferences preferences;

	public static Preferences getInstance() {
		if (preferences == null) {
			preferences = new Preferences();
		}
		return preferences;
	}

	private Preferences() {
		this.init();
	}

	private void init() {
		this.props = new Properties();

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

	public void save() {
		try (OutputStream stream = new BufferedOutputStream(new FileOutputStream(PROPS_FILE))) {
			this.props.store(stream, null);
		} catch (IOException e) {
			App.showError("Could not write to the properties file.");
		}
	}

	public String get(String key) {
		return this.props.getProperty(key);
	}

	public void set(String key, String value) {
		this.props.setProperty(key, value);
		this.save();
	}

	public boolean getBoolean(String key) {
		String value = this.props.getProperty(key);
		return Boolean.parseBoolean(value);
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		String value = this.props.getProperty(key);
		if (value == null) {
			value = Boolean.toString(defaultValue);
			this.set(key, value);
		}
		return Boolean.parseBoolean(value);
	}

	public void set(String key, boolean value) {
		this.props.setProperty(key, Boolean.toString(value));
		this.save();
	}
}