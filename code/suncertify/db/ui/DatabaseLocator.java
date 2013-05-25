package suncertify.db.ui;

import static suncertify.shared.App.PROP_DB_LOCATION;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import suncertify.shared.Properties;

public class DatabaseLocator {

	public static String getLocation() {
		JFileChooser chooser = createDialog();

		String location = null;
		int action = chooser.showOpenDialog(null);
		if (action == JFileChooser.APPROVE_OPTION) {
			File choice = chooser.getSelectedFile();
			if (isDBFileValid(choice)) {
				location = choice.getAbsolutePath();
				Properties.set(PROP_DB_LOCATION, location);
			}
		}

		return location;
	}

	private static JFileChooser createDialog() {
		JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));

		chooser.setFileFilter(new FileNameExtensionFilter(".db files only", "db"));
		chooser.setDialogTitle("Database location");

		return chooser;
	}

	private static boolean isDBFileValid(File choice) {
		if (!choice.exists()) {
			return false;
		}
		// TODO: Check if Magic Cookie matches
		// TODO: Check if File is a valid DB file
		return true;
	}
}