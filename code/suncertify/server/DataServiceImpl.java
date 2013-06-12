package suncertify.server;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import suncertify.client.RemoteObserver;
import suncertify.db.DAOFactory;
import suncertify.db.DBMain;
import suncertify.db.DuplicateKeyException;
import suncertify.db.RecordNotFoundException;
import suncertify.shared.App;
import suncertify.shared.Contractor;

/**
 * This class is the main implementation of the Server interface.
 * 
 * @author Sean Dunne
 */
public class DataServiceImpl implements DataService {

	private final DBMain data = DAOFactory.getInstance().getDataService();
	private final List<RemoteObserver> observers = new ArrayList<>();
	private final ExecutorService executor = Executors.newFixedThreadPool(5);

	@Override
	public Contractor read(final int recNo) throws RecordNotFoundException {
		this.data.lock(recNo);
		Contractor record = new Contractor(recNo, this.data.read(recNo));
		this.data.unlock(recNo);

		return record;
	}

	@Override
	public void update(final Contractor data) throws RecordNotFoundException {
		this.data.lock(data.getRecordId());
		this.data.update(data.getRecordId(), data.toArray());
		this.data.unlock(data.getRecordId());
		this.notifyObservers(data, "update");
	}

	@Override
	public void delete(final Contractor record) throws RecordNotFoundException {
		this.data.lock(record.getRecordId());
		this.data.delete(record.getRecordId());
		this.data.unlock(record.getRecordId());
		this.notifyObservers(record, "delete");
	}

	@Override
	public List<Contractor> find(final String[] criteria, final boolean findExactMatches) {
		final List<Contractor> finalResults = new ArrayList<Contractor>();
		int[] rawResults = new int[0];
		try {
			rawResults = this.data.find(criteria);
		} catch (RecordNotFoundException e) {
			// cannot happen
		}

		for (int recNo : rawResults) {
			try {
				Contractor record = this.read(recNo);
				if (!findExactMatches || isExactMatch(record, criteria)) {
					finalResults.add(record);
				}
			} catch (RecordNotFoundException e) {
				// record has been deleted, ignore it
			}
		}
		return finalResults;
	}

	/**
	 * This method is used to determine if a Contractor exactly matches search criteria.
	 * 
	 * @param record
	 *            The Contractor to check.
	 * @param criteria
	 *            The search criteria used to check the Contractor.
	 * @return true if the Contractor is an exact match otherwise false.
	 */
	private boolean isExactMatch(final Contractor record, final String[] criteria) {
		final String[] dataArray = record.toArray();
		for (int i = 0; i < criteria.length; i++) {
			if (criteria[i] != null && !criteria[i].equals("") && !dataArray[i].equals(criteria[i])) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int create(final String[] data) throws DuplicateKeyException {
		int recNo = this.data.create(data);
		try {
			this.notifyObservers(this.read(recNo), "create");
		} catch (RecordNotFoundException e) {
			// contractor deleted, no need to update clients
		}
		return recNo;
	}

	/**
	 * This method is responsible for updating all the clients that registered for updates when an update occurs to the database.
	 * 
	 * @param contractor
	 *            The Contractor object that has been updated.
	 * @param cmd
	 *            A String indicating how the Contractor has changed, create, update, delete.
	 */
	private void notifyObservers(final Contractor contractor, final String cmd) {
		final List<RemoteObserver> staleRefs = new ArrayList<>();
		for (final RemoteObserver o : this.observers) {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						o.update(contractor, cmd);
					} catch (RemoteException e) {
						App.logWarning("Found an uncontactable Client, removing it from the Server.");
						staleRefs.add(o);
					}
				}
			});
		}
		this.observers.removeAll(staleRefs);
	}

	@Override
	public void addObserver(RemoteObserver o) {
		App.logWarning("Client connected");
		this.observers.add(o);
	}

	@Override
	public void deleteObserver(RemoteObserver o) {
		App.logWarning("Client disconnected");
		this.observers.remove(o);
	}
}