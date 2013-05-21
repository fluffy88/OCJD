package suncertify;

import suncertify.client.DataServiceFactory;
import suncertify.client.ui.ClientUI;
import suncertify.server.DataService;
import suncertify.shared.App;

public class Client implements Application {

	public static final String DATASERVICE = "dataservice";

	private final AppType type;

	public Client(final AppType type) {
		this.type = type;
	}

	@Override
	public void start() {
		final DataService dataService = DataServiceFactory.getService(type);
		App.publish(DATASERVICE, dataService);

		ClientUI.start();
	}
}