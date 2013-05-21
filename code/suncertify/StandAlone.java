package suncertify;

import suncertify.server.DataService;
import suncertify.server.DataServiceImpl;
import suncertify.shared.App;

public class StandAlone implements Application {

	@Override
	public void start() {
		final DataService dataService = new DataServiceImpl();
		this.publish(dataService);

		final Client client = new Client(AppType.StandAlone);
		client.start();
	}

	private void publish(final DataService dataService) {
		App.instance.publish("DataServer", dataService);
	}
}