package suncertify;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import suncertify.db.DBMain;
import suncertify.db.Data;
import suncertify.db.io.Database;
import suncertify.shared.Injection;
import suncertify.shared.Preferences;

public class Server implements Application {

	private final AppType type;

	public Server(AppType type) {
		this.type = type;
	}

	@Override
	public void start() {

		Database db = new Database();
		String dbLoc = db.getLocation();

		DBMain data = new Data(dbLoc);
		publish(data);

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				Server.this.shutdown();
			}
		});
	}

	private void publish(DBMain data) {
		if (this.type == AppType.Server) {
			try {
				DBMain rmiStub = (DBMain) UnicastRemoteObject.exportObject(data, 0);
				Registry registry = LocateRegistry.getRegistry();
				registry.rebind("Remote Database Server", rmiStub);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (this.type == AppType.StandAlone) {
			Injection.instance.add("DataServer", data);
		}
	}

	public void shutdown() {
		Preferences props = Preferences.getInstance();
		props.save();
	}
}
