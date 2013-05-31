package suncertify;

import static suncertify.shared.App.DEP_DATASERVICE;
import static suncertify.shared.App.DEP_SERVER_INSTANCE;

import java.rmi.RemoteException;

import suncertify.client.DataModelObserver;
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

		App.publish(DEP_SERVER_INSTANCE, this);
	}

	@Override
	public void start() {
		final DataService dataService = new DataServiceImpl();
		this.publish(dataService);

		serverUI.setVisible(false);
		ClientUI.start();

		try {
			final DataModelObserver modelCallback = new DataModelObserver();
			dataService.addObserver(modelCallback);
		} catch (RemoteException e) {
			App.showErrorAndExit("Could not register client for automatic updates from the server.");
		}
	}

	private void publish(final DataService dataService) {
		App.publish(DEP_DATASERVICE, dataService);
	}
}