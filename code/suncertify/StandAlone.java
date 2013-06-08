package suncertify;

import static suncertify.shared.App.DEP_DATASERVICE;
import static suncertify.shared.App.DEP_SERVER_APPLICATION;

import java.rmi.RemoteException;

import suncertify.client.ServerUpdateObserver;
import suncertify.client.ui.ClientUI;
import suncertify.server.DataService;
import suncertify.server.DataServiceImpl;
import suncertify.server.ui.ServerUI;
import suncertify.shared.App;

public class StandAlone implements Application, Startable {

	private ServerUI serverUI;

	@Override
	public void launch() {
		serverUI = new ServerUI();
		serverUI.open();

		App.publish(DEP_SERVER_APPLICATION, this);
	}

	@Override
	public void start() {
		final DataService dataService = new DataServiceImpl();
		this.publish(dataService);

		serverUI.setVisible(false);
		ClientUI.start();

		try {
			final ServerUpdateObserver callback = new ServerUpdateObserver();
			dataService.addObserver(callback);
		} catch (RemoteException e) {
			// will not be thrown in StandAlone mode
		}
	}

	private void publish(final DataService dataService) {
		App.publish(DEP_DATASERVICE, dataService);
	}
}