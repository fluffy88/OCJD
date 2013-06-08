package suncertify;

import static suncertify.shared.App.DEP_SERVER_APPLICATION;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import suncertify.server.DataService;
import suncertify.server.DataServiceImpl;
import suncertify.server.ui.ServerUI;
import suncertify.shared.App;

public class Server implements Application, Startable {

	public static final String RMI_SERVER = "remote.database.server";

	@Override
	public void launch() {
		ServerUI ui = new ServerUI();
		ui.open();

		App.publish(DEP_SERVER_APPLICATION, this);
	}

	@Override
	public void start() {
		final DataService dataService = new DataServiceImpl();
		this.publish(dataService);
	}

	private void publish(final DataService dataService) {
		try {
			final DataService rmiStub = (DataService) UnicastRemoteObject.exportObject(dataService, Registry.REGISTRY_PORT);
			final Registry registry = this.getRMIRegistry();
			registry.rebind(RMI_SERVER, rmiStub);
		} catch (RemoteException e) {
			App.showErrorAndExit("Cannot publish the RMI server, check no other applications are using the default RMI port 1099.");
		}
	}

	private Registry getRMIRegistry() throws RemoteException {
		Registry registry = null;
		try {
			registry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
		} catch (Exception e) {
			registry = LocateRegistry.getRegistry(Registry.REGISTRY_PORT);
		}
		return registry;
	}
}