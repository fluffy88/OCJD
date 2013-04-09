package suncertify;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import suncertify.db.DBMain;
import suncertify.db.ServerFactory;
import suncertify.db.ui.ServerUI;
import suncertify.shared.Injection;
import suncertify.shared.Preferences;

public class Server implements Application {

	public static final String RMI_SERVER = "Remote Database Server";
	private final AppType type;

	public Server(final AppType type) {
		this.type = type;
	}

	@Override
	public void start() {

		final DBMain data = ServerFactory.createDataService();
		this.publish(data);
		this.startServerUI();

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				Server.this.shutdown();
			}
		});
	}

	private void startServerUI() {
		if (this.type == AppType.Server) {
			ServerUI.start();
		}
	}

	private void publish(final DBMain data) {
		if (this.type == AppType.Server) {
			try {
				final DBMain rmiStub = (DBMain) UnicastRemoteObject.exportObject(data, 0);
				final Registry registry = this.getRMIRegistry();
				registry.rebind(RMI_SERVER, rmiStub);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (this.type == AppType.StandAlone) {
			Injection.instance.add("DataServer", data);
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

	public void shutdown() {
		final Preferences props = Preferences.getInstance();
		props.save();
	}
}
