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

/**
 * This class is responsible for establishing a networked server.
 * 
 * @author Sean Dunne
 */
public class Server implements Application {

	public static final String RMI_SERVER = "remote.database.server";

	@Override
	public void launch() {
		ServerUI.start();

		App.publish(DEP_SERVER_APPLICATION, this);
	}

	@Override
	public void start() {
		final DataService dataService = new DataServiceImpl();
		this.publish(dataService);
	}

	/**
	 * This method is responsible for publishing the {@link DataService} interface via RMI for networked clients to connect to.
	 * 
	 * @param dataService
	 *            The remote server interface to be published.
	 */
	private void publish(final DataService dataService) {
		try {
			final DataService rmiStub = (DataService) UnicastRemoteObject.exportObject(dataService, Registry.REGISTRY_PORT);
			final Registry registry = this.getRMIRegistry();
			registry.rebind(RMI_SERVER, rmiStub);
		} catch (RemoteException e) {
			App.showErrorAndExit("Cannot publish the RMI server, check no other applications are using the default RMI port 1099.");
		}
	}

	/**
	 * This method is responsible for creating a new RMI registry.
	 * 
	 * @return A reference to an RMI registry that can be used to publish objects over RMI.
	 * @throws RemoteException
	 *             If an RMI registry is already started on the default RMI port.
	 */
	private Registry getRMIRegistry() throws RemoteException {
		return LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
	}
}