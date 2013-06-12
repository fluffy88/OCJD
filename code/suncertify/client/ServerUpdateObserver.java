package suncertify.client;

import static suncertify.shared.App.DEP_TABLE_MODEL;
import suncertify.client.ui.SearchResultsTableModel;
import suncertify.shared.App;
import suncertify.shared.Contractor;

/**
 * Class to handle the updating of the client when an update occurs to the server database.
 * 
 * @author Sean Dunne
 */
public class ServerUpdateObserver implements RemoteObserver {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(final Contractor contractor, final String cmd) {
		final SearchResultsTableModel model = (SearchResultsTableModel) App.getDependancy(DEP_TABLE_MODEL);

		switch (cmd) {
		case CREATE:
			model.add(contractor);
			break;
		case UPDATE:
			model.update(contractor);
			break;
		case DELETE:
			model.remove(contractor);
			break;
		}
	}
}