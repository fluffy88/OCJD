package suncertify.client;

import static suncertify.shared.App.DEP_TABLE_MODEL;
import suncertify.client.ui.SearchResultsTableModel;
import suncertify.shared.App;
import suncertify.shared.Contractor;

public class ServerUpdateObserver implements RemoteObserver {

	@Override
	public void update(final Contractor contractor, final String cmd) {
		final SearchResultsTableModel model = (SearchResultsTableModel) App.getDependancy(DEP_TABLE_MODEL);

		switch (cmd) {
		case "create":
			model.add(contractor);
			break;
		case "update":
			model.update(contractor);
			break;
		case "delete":
			model.remove(contractor);
			break;
		}
	}
}