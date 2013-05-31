package suncertify;

import static suncertify.shared.App.DEP_DATASERVICE;
import static suncertify.shared.App.PROP_SERVER_HOSTNAME;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.JOptionPane;

import suncertify.client.DataModelObserver;
import suncertify.client.ui.ClientUI;
import suncertify.server.DataService;
import suncertify.shared.App;
import suncertify.shared.Properties;

public class Client implements Application {

	@Override
	public void launch() {
		final DataService dataService = this.getRemoteService();
		App.publish(DEP_DATASERVICE, dataService);

		ClientUI.start();

		try {
			final DataModelObserver rmiCallback = new DataModelObserver();
			UnicastRemoteObject.exportObject(rmiCallback);
			dataService.addObserver(rmiCallback);
		} catch (RemoteException e) {
			App.showErrorAndExit("Could not register client for automatic updates from the server.");
		}
	}

	private DataService getRemoteService() {
		try {
			Registry registry = this.getRegistry();
			DataService dataService = (DataService) registry.lookup(Server.RMI_SERVER);
			return dataService;
		} catch (RemoteException e) {
			App.showErrorAndExit("Cannot connect to remote server.");
		} catch (NotBoundException e) {
			App.showErrorAndExit("Server not started.");
		}
		return null;
	}

	private Registry getRegistry() throws RemoteException {
		String host = "";
		while (host.equals("")) {
			host = JOptionPane.showInputDialog("Enter the server hostname", Properties.get(PROP_SERVER_HOSTNAME, "localhost"));
			if (host == null) {
				System.exit(0);
			}
		}
		Properties.set(PROP_SERVER_HOSTNAME, host);
		Registry registry = LocateRegistry.getRegistry(host);
		return registry;
	}
}