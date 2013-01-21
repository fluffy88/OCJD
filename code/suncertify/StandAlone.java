package suncertify;

import java.rmi.RemoteException;
import java.util.Arrays;

import javax.swing.SwingUtilities;

import suncertify.db.Data;
import suncertify.db.RecordNotFoundException;
import suncertify.db.ui.MainFrame;

public class StandAlone implements Application {

	@Override
	public void start() {
		Data data = Data.INSTANCE;

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainFrame jFrame = new MainFrame();
			}
		});

		try {
			System.out.println("StandAlone:");
			System.out.println(Arrays.toString(data.read(0)));
		} catch (RemoteException | RecordNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
