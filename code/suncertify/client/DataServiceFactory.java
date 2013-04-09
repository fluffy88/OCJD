package suncertify.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import suncertify.AppType;
import suncertify.Server;
import suncertify.db.DBMain;
import suncertify.shared.Injection;

public class DataServiceFactory {

	private static DataServiceFactory instance;

	public static DBMain getService(AppType type) {

		if (type == AppType.Client) {
			return instance.getRemoteService();
		} else if (type == AppType.StandAlone) {
			return (DBMain) Injection.instance.get("DataServer");
		}

		return null;
	}

	private DBMain getRemoteService() {
		try {
			Registry registry = LocateRegistry.getRegistry();
			DBMain dataService = (DBMain) registry.lookup(Server.RMI_SERVER);
			return dataService;
		} catch (RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}