package suncertify;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;

import suncertify.db.DBMain;
import suncertify.db.RecordNotFoundException;
import suncertify.db.ui.MainFrame;

public class Client implements Application {

	@Override
	public void start() {

		try {
			Registry registry = LocateRegistry.getRegistry();
			DBMain data = (DBMain) registry.lookup("Remote Database Server");

			new MainFrame();

			System.out.println("Client:");
			System.out.println(Arrays.toString(data.read(0)));

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RecordNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
