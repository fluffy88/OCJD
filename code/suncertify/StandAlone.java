package suncertify;

import static suncertify.shared.App.DEP_DATASERVICE;
import static suncertify.shared.App.DEP_STANDALONE_INSTANCE;
import suncertify.client.ui.ClientUI;
import suncertify.server.DataService;
import suncertify.server.DataServiceImpl;
import suncertify.server.ui.ServerUI;
import suncertify.server.ui.StandAloneServerUI;
import suncertify.shared.App;

public class StandAlone implements Application {

	@Override
	public void start() {
		ServerUI ui = new StandAloneServerUI();
		ui.open();

		App.publish(DEP_STANDALONE_INSTANCE, this);
	}

	public void init() {
		final DataService dataService = new DataServiceImpl();
		this.publish(dataService);

		ClientUI.start();
	}

	private void publish(final DataService dataService) {
		App.publish(DEP_DATASERVICE, dataService);
	}
}