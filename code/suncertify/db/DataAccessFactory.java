package suncertify.db;

import static suncertify.shared.App.PROP_DB_LOCATION;
import suncertify.server.ui.DatabaseLocator;
import suncertify.shared.App;
import suncertify.shared.Properties;

public class DataAccessFactory {

	private static DataAccessFactory instance;

	private final DBMain dataService;
	private String location;

	private DataAccessFactory() {
		this.getDBLocation();
		dataService = new Data(location);
	}

	public static DataAccessFactory getInstance() {
		if (instance == null) {
			instance = new DataAccessFactory();
		}
		return instance;
	}

	public DBMain getDataService() {
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