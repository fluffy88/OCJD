package suncertify.db;

import static suncertify.shared.App.PROP_DB_LOCATION;
import suncertify.db.ui.DatabaseLocator;
import suncertify.shared.App;
import suncertify.shared.Properties;

public class DataAccessFactory {

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

	public DBMain getDataServie() {
		return this.dataService;
	}

	private void getDBLocation() {
		this.location = Properties.get(PROP_DB_LOCATION);

		if (this.location == null) {
			this.location = DatabaseLocator.getLocation();
			if (this.location == null) {
				App.showErrorAndExit("You did not select a database location.");
			}
		}
	}
}