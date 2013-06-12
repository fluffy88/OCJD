package suncertify.db;

import static suncertify.shared.App.PROP_DB_LOCATION;
import suncertify.server.ui.DatabaseLocator;
import suncertify.shared.App;
import suncertify.shared.Properties;

/**
 * This is a factory class responsible for creating and retrieving an instance of DBMain.java.
 * 
 * @author Sean Dunne
 */
public enum DAOFactory {

	instance;

	private final DBMain dataService;
	private String location;

	/**
	 * This class is a singleton therefore it's an enum with a private constructor.
	 */
	private DAOFactory() {
		this.getDBLocation();
		dataService = new Data(location);
	}

	/**
	 * This is the factory method to get the instance of the Data factory.
	 * 
	 * @return The factory instance.
	 */
	public static DAOFactory getInstance() {
		return instance;
	}

	/**
	 * Getter for the instance of the Data class DBMain.
	 * 
	 * @return An instance of DBMain.
	 */
	public DBMain getDataService() {
		return this.dataService;
	}

	/**
	 * This method is responsible for getting to location of the database file, used to construct the instance of DBMain.
	 */
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