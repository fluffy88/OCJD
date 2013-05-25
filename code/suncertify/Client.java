package suncertify;

import static suncertify.shared.App.DEP_DATASERVICE;
import static suncertify.shared.App.PROP_SERVER_HOSTNAME;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.JOptionPane;

import suncertify.client.ui.ClientUI;
import suncertify.server.DataService;
import suncertify.shared.App;
import suncertify.shared.Properties;

public class Client implements Application {

	@Override
	public void start() {
		final DataService dataService = this.getRemoteService();
		App.publish(DEP_DATASERVICE, dataService);

		ClientUI.start();
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
		String host = null;
		while (host == null || host.equals("")) {
			host = JOptionPane.showInputDialog("Enter the server hostname", Properties.get(PROP_SERVER_HOSTNAME, "localhost"));
		}
		Properties.set(PROP_SERVER_HOSTNAME, host);
		Registry registry = LocateRegistry.getRegistry(host);
		return registry;
	}
}