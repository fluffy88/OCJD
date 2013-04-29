package suncertify;

import suncertify.client.DataServiceFactory;
import suncertify.client.ui.ClientUI;
import suncertify.server.DataService;
import suncertify.shared.Injection;

public class Client implements Application {

	private final AppType type;

	public Client(final AppType type) {
		this.type = type;
	}

	@Override
	public void start() {
		final DataService dataService = DataServiceFactory.getService(type);
		Injection.instance.add("DataService", dataService);

		ClientUI.start();
	}
}