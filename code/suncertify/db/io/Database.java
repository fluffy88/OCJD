package suncertify.db.io;

import suncertify.shared.Preferences;

public class Database {

	public static final String DB_LOCATION = "databaseLocation";

	private String location;

	public Database() {
		this.init();
	}

	private void init() {
		Preferences props = Preferences.getInstance();

		String dbLoc = props.get(DB_LOCATION);
		if (dbLoc == null) {
			// TODO: prompt user for File location
			dbLoc = this.promptForLocation();
			props.set(DB_LOCATION, dbLoc);
		}

		this.location = dbLoc;
	}

	private String promptForLocation() {
		System.out.println("Enter database location .....");

		String dbLoc = "db-2x2.db";
		return dbLoc;
	}

	public String getLocation() {
		return this.location;
	}

}
