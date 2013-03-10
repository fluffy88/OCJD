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
				// TODO Auto-generated catch block
				// Will be thrown when props file is malformed ... handle this!
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void save() {
		try (OutputStream stream = new BufferedOutputStream(new FileOutputStream(PROPS_FILE))) {
			this.props.store(stream, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String get(String key) {
		return this.props.getProperty(key);
	}

	public void set(String key, String value) {
		this.props.setProperty(key, value);
	}
}