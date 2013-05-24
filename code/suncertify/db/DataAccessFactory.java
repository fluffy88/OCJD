package suncertify.db;

import suncertify.db.ui.DatabaseLocator;
import suncertify.shared.App;
import suncertify.shared.Preferences;

public class DataAccessFactory {

	public static final String DB_LOCATION = "database.location";

	private static DataAccessFactory instance;

	private DBMain dataService;
	private String location;

	public static DBMain getDataService() {
		if (instance == null) {
			instance = new DataAccessFactory();
		}
		return instance.dataService;
	}

	private DataAccessFactory() {
		this.getDBLocation();
		dataService = new Data(location);
	}

	private void getDBLocation() {
		final Preferences props = Preferences.getInstance();
		this.location = props.get(DB_LOCATION);

		if (this.location == null) {
			this.location = DatabaseLocator.getLocation();
			if (this.location == null) {
				App.showErrorAndExit("You did not select a database location.");
			}
		}
	}
}