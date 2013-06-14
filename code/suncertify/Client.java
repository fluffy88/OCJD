package suncertify;

import static suncertify.shared.App.DEP_APPLICATION;
import static suncertify.shared.App.DEP_DATASERVICE;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import suncertify.client.ServerUpdateObserver;
import suncertify.client.ui.ClientUI;
import suncertify.client.ui.NetworkedClientUI;
import suncertify.server.DataService;
import suncertify.shared.App;

/**
 * This class is responsible for establishing a networked client.
 * 
 * @author Sean Dunne
 */
public class Client implements Application {

	private ServerUpdateObserver rmiCallback;
	private NetworkedClientUI hostnameDialog;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void launch() {
		hostnameDialog = NetworkedClientUI.start();
		App.publish(DEP_APPLICATION, this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start() {
		final DataService dataService = hostnameDialog.getDataService();
		App.publish(DEP_DATASERVICE, dataService);

		ClientUI.start();

		try {
			rmiCallback = new ServerUpdateObserver();
			UnicastRemoteObject.exportObject(rmiCallback);
			dataService.addObserver(rmiCallback);
		} catch (RemoteException e) {
			App.showErrorAndExit("Could not subscribe to Server.");
		}

		this.setShutdownHook();
	}

	/**
	 * This method is a simple method to add a shutdown hook that is responsible for cleaning up before this application is ready to
	 * shutdown.
	 */
	private void setShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			/**
			 * {@inheritDoc}
			 */
			@Override
			public void run() {
				final DataService dataService = (DataService) App.getDependancy(DEP_DATASERVICE);
				try {
					dataService.deleteObserver(rmiCallback);
				} catch (RemoteException e) {
					App.logError("Could not contact the Server when attempting to unsubscribe.");
				}
			}
		});
	}
}