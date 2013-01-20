package suncertify;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import suncertify.db.DBMain;
import suncertify.db.Data;

public class Server implements Application {

	@Override
	public void start() {
		try {

			DBMain data = Data.INSTANCE;
			DBMain rmiStub = (DBMain) UnicastRemoteObject.exportObject(data, 0);
			Registry registry = LocateRegistry.getRegistry();
			registry.rebind("Remote Database Server", rmiStub);

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
