package suncertify.db;

import javax.swing.JOptionPane;

import suncertify.db.ui.DatabaseLocator;
import suncertify.shared.Preferences;

public class DataAccessFactory {

	public static final String DB_LOCATION = "DatabaseLocation";
	private static DataAccessFactory instance;

	private DBMain dataService;
	private String location;

	public static DBMain getDataService() {
		if (instance == null) {
			instance = new DataAccessFactory();
		}
		if (instance.dataService == null) {
			instance.dataService = new Data(instance.location);
		}
		return instance.dataService;
	}

	private DataAccessFactory() {
		this.init();
	}

	private void init() {
		final Preferences props = Preferences.getInstance();
		this.location = props.get(DB_LOCATION);

		if (this.location == null) {
			this.location = DatabaseLocator.getLocation();
			if (this.location == null) {
				JOptionPane.showMessageDialog(null, "You did not select a database location. \nApplication will exit!", "Error",
						JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			}
		}
	}
}