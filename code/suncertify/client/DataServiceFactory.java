package suncertify.client;

import static suncertify.StandAlone.STANDALONE_DATA_SERVICE;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import suncertify.AppType;
import suncertify.Server;
import suncertify.server.DataService;
import suncertify.shared.App;

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
		return (DataService) App.getDependancy(STANDALONE_DATA_SERVICE);
	}

	private DataService getRemoteService() {
		try {
			Registry registry = LocateRegistry.getRegistry();
			DataService dataService = (DataService) registry.lookup(Server.RMI_SERVER);
			return dataService;
		} catch (RemoteException e) {
			App.showErrorAndExit("Cannot connect to remote server.");
		} catch (NotBoundException e) {
			App.showErrorAndExit("Server not started.");
		}
		return null;
	}
}