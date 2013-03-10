package suncertify;

import suncertify.client.DataServiceFactory;
import suncertify.db.DBMain;
import suncertify.shared.Injection;
import suncertify.ui.ClientUI;

public class Client implements Application {

	private final AppType type;

	public Client(AppType type) {
		this.type = type;
	}

	@Override
	public void start() {

		DataServiceFactory factory = new DataServiceFactory();
		DBMain dataService = factory.getService(type);

		Injection.instance.add("DataService", dataService);

		new ClientUI();

	}

}