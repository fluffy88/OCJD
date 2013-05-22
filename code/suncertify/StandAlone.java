package suncertify;

import static suncertify.Client.DATASERVICE;
import suncertify.client.ui.ClientUI;
import suncertify.server.DataService;
import suncertify.server.DataServiceImpl;
import suncertify.shared.App;

public class StandAlone implements Application {

	@Override
	public void start() {
		final DataService dataService = new DataServiceImpl();
		this.publish(dataService);

		ClientUI.start();
	}

	private void publish(final DataService dataService) {
		App.publish(DATASERVICE, dataService);
	}
}