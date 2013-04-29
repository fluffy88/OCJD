package suncertify.db;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

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
		JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
		chooser.setFileFilter(new FileNameExtensionFilter(".db files only", "db"));
		chooser.setDialogTitle("Database location");

		String location = null;
		while (location == null) {

			int action = chooser.showOpenDialog(null);
			if (action == JFileChooser.APPROVE_OPTION) {
				File choice = chooser.getSelectedFile();
				if (isDBFileValid(choice)) {
					location = choice.getAbsolutePath();
				}
			} else if (action == JFileChooser.CANCEL_OPTION) {
				JOptionPane.showMessageDialog(null, "You did not select a database location. \nApplication will exit!", "Error",
						JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			}
		}

		return location;
	}

	private boolean isDBFileValid(File choice) {
		if (!choice.exists()) {
			return false;
		}
		// TODO: Check if Magic Cookie matches
		// TODO: Check if File is a valid DB file
		return true;
	}
}