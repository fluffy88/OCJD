package suncertify.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import suncertify.AppType;
import suncertify.Server;
import suncertify.server.DataService;
import suncertify.shared.Injection;

public class DataServiceFactory {

	private static DataServiceFactory instance;

	static {
		instance = new DataServiceFactory();
	}

	public static DataService getService(AppType type) {

		if (type == AppType.Client) {
			return instance.getRemoteService();
		} else if (type == AppType.StandAlone) {
			return instance.getLocalService();
		}

		return null;
	}

	private DataService getLocalService() {
		return (DataService) Injection.instance.get("DataServer");
	}

	private DataService getRemoteService() {
		try {
			Registry registry = LocateRegistry.getRegistry();
			DataService dataService = (DataService) registry.lookup(Server.RMI_SERVER);
			return dataService;
		} catch (RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}