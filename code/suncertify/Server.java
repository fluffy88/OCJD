package suncertify;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import suncertify.server.DataService;
import suncertify.server.DataServiceImpl;
import suncertify.server.ui.ServerUI;
import suncertify.shared.App;

public class Server implements Application {

	public static final String RMI_SERVER = "Remote Database Server";

	@Override
	public void start() {
		ServerUI.start();
		App.publish("server.instance", this);
	}

	public void init() {
		final DataService dataService = new DataServiceImpl();
		this.publish(dataService);
	}

	private void publish(final DataService dataService) {
		try {
			final DataService rmiStub = (DataService) UnicastRemoteObject.exportObject(dataService, Registry.REGISTRY_PORT);
			final Registry registry = this.getRMIRegistry();
			registry.rebind(RMI_SERVER, rmiStub);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Registry getRMIRegistry() throws RemoteException {
		Registry registry = null;
		try {
			registry = LocateRegistry.createRegistry(1099);
		} catch (Exception e) {
			registry = LocateRegistry.getRegistry(1099);
		}
		return registry;
	}
}