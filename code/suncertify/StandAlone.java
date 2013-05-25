package suncertify;

import static suncertify.Client.DATASERVICE;
import suncertify.client.ui.ClientUI;
import suncertify.server.DataService;
import suncertify.server.DataServiceImpl;
import suncertify.server.ui.ServerUI;
import suncertify.server.ui.StandAloneServerUI;
import suncertify.shared.App;

public class StandAlone implements Application {

	public static final String STANDALONE_INSTANCE = "standalone.instance";

	@Override
	public void start() {
		ServerUI ui = new StandAloneServerUI();
		ui.open();

		App.publish(STANDALONE_INSTANCE, this);
	}

	public void init() {
		final DataService dataService = new DataServiceImpl();
		this.publish(dataService);

		ClientUI.start();
	}

	private void publish(final DataService dataService) {
		App.publish(DATASERVICE, dataService);
	}
}