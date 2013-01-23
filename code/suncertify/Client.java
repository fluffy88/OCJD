package suncertify;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import suncertify.db.DBMain;
import suncertify.db.ui.ClientUI;

public class Client implements Application {

	@Override
	public void start() {

		try {
			Registry registry = LocateRegistry.getRegistry();
			DBMain dataService = (DBMain) registry.lookup("Remote Database Server");

			new ClientUI(dataService);

		} catch (RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
