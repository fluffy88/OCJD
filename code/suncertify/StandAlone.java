package suncertify;

import static suncertify.shared.App.DEP_DATASERVICE;
import static suncertify.shared.App.DEP_SERVER_INSTANCE;
import suncertify.client.ui.ClientUI;
import suncertify.server.DataService;
import suncertify.server.DataServiceImpl;
import suncertify.server.ui.ServerUI;
import suncertify.shared.App;

public class StandAlone implements Application, Startable {

	private ServerUI ui;

	@Override
	public void launch() {
		ui = new ServerUI();
		ui.open();

		App.publish(DEP_SERVER_INSTANCE, this);
	}

	@Override
	public void start() {
		final DataService dataService = new DataServiceImpl();
		this.publish(dataService);

		ui.setVisible(false);
		ClientUI.start();
	}

	private void publish(final DataService dataService) {
		App.publish(DEP_DATASERVICE, dataService);
	}
}