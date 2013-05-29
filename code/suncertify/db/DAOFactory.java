package suncertify.db;

import static suncertify.shared.App.PROP_DB_LOCATION;
import suncertify.server.ui.DatabaseLocator;
import suncertify.shared.App;
import suncertify.shared.Properties;

public class DAOFactory {

	private static DAOFactory instance;

	private final DBMain dataService;
	private String location;

	private DAOFactory() {
		this.getDBLocation();
		dataService = new Data(location);
	}

	public static DAOFactory getInstance() {
		if (instance == null) {
			instance = new DAOFactory();
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