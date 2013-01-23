package suncertify;

import javax.swing.SwingUtilities;

import suncertify.db.Data;
import suncertify.db.ui.ClientUI;

public class StandAlone implements Application {

	@Override
	public void start() {
		final Data dataService = Data.INSTANCE;

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new ClientUI(dataService);
			}
		});

	}

}
