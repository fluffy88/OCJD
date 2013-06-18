package suncertify.server.ui;

import static suncertify.shared.App.PROP_DB_LOCATION;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import suncertify.shared.App;
import suncertify.shared.Properties;

/**
 * This class is responsible for prompting the user to enter the location of the database file.
 * 
 * @author Sean Dunne
 */
public class DatabaseLocator {

	/**
	 * This method will display a file chooser dialog to the user allowing the user chose a new database file.
	 * 
	 * @return The location of the file the user picked.
	 */
	public static String getLocation() {
		JFileChooser chooser = createDialog();

		String location = null;
		int action = chooser.showOpenDialog(null);
		if (action == JFileChooser.APPROVE_OPTION) {
			File choice = chooser.getSelectedFile();
			if (choice.getName().endsWith(".db")) {
				location = choice.getAbsolutePath();
				Properties.set(PROP_DB_LOCATION, location);
			} else {
				App.showError("The selected file is not a valid database file.");
			}
		}

		return location;
	}

	/**
	 * This method creates the {@link JFileChooser} with the correct properties.
	 * 
	 * @return A JFileChooser allowing selection of .db files.
	 */
	private static JFileChooser createDialog() {
		JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));

		chooser.setFileFilter(new FileNameExtensionFilter(".db files only", "db"));
		chooser.setDialogTitle("Database location");

		return chooser;
	}
}