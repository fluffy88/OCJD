package suncertify;

import java.rmi.RemoteException;
import java.util.Arrays;

import javax.swing.SwingUtilities;

import suncertify.db.Data;
import suncertify.db.RecordNotFoundException;
import suncertify.db.ui.ClientUI;

public class StandAlone implements Application {

	@Override
	public void start() {
		final Data dataService = Data.INSTANCE;

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ClientUI jFrame = new ClientUI(dataService);
			}
		});

		try {
			System.out.println("StandAlone:");
			System.out.println(Arrays.toString(dataService.read(0)));
		} catch (RemoteException | RecordNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
