package suncertify;

import static suncertify.shared.App.DATASERVICE;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import suncertify.client.ui.ClientUI;
import suncertify.server.DataService;
import suncertify.shared.App;

public class Client implements Application {

	@Override
	public void start() {
		final DataService dataService = this.getRemoteService();
		App.publish(DATASERVICE, dataService);

		ClientUI.start();
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