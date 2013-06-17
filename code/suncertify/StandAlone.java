package suncertify;

import static suncertify.shared.App.DEP_APPLICATION;
import static suncertify.shared.App.DEP_DATASERVICE;

import java.rmi.RemoteException;

import suncertify.client.ServerUpdateObserver;
import suncertify.client.ui.ClientUI;
import suncertify.server.DataService;
import suncertify.server.DataServiceImpl;
import suncertify.server.ui.ServerUI;
import suncertify.shared.App;

/**
 * This class is responsible for starting a non-networked client and server..
 * 
 * @author Sean Dunne
 */
public class StandAlone implements Application {

	private ServerUI serverUI;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void launch() {
		serverUI = ServerUI.start();

		App.publish(DEP_APPLICATION, this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start() {
		final DataService dataService = new DataServiceImpl();
		this.publish(dataService);

		serverUI.dispose();
		ClientUI.start();

		try {
			final ServerUpdateObserver callback = new ServerUpdateObserver();
			dataService.addObserver(callback);
		} catch (RemoteException e) {
			// will not be thrown in StandAlone mode
		}
	}

	/**
	 * This method is responsible for publishing the {@link DataService} interface via dependency injection for the local client to use.
	 * 
	 * @param dataService
	 *            The local server interface to be published.
	 */
	private void publish(final DataService dataService) {
		App.publish(DEP_DATASERVICE, dataService);
	}
}