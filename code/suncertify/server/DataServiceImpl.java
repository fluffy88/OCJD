package suncertify.server;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import suncertify.client.RemoteObserver;
import suncertify.db.DAOFactory;
import suncertify.db.DBMain;
import suncertify.db.DuplicateKeyException;
import suncertify.db.RecordNotFoundException;
import suncertify.shared.App;
import suncertify.shared.Contractor;

public class DataServiceImpl implements DataService {

	DBMain data = DAOFactory.getInstance().getDataService();
	private List<RemoteObserver> observers = new ArrayList<>();

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
	public List<Contractor> find(final String[] criteria, final boolean exactMatch) throws RecordNotFoundException {
		final int[] rawResults = this.data.find(criteria);
		final List<Contractor> finalResults = new ArrayList<Contractor>();

		add_loop: for (int recNo : rawResults) {
			Contractor record = this.read(recNo);
			if (exactMatch) {
				String[] dataArray = record.toArray();
				for (int i = 0; i < criteria.length; i++) {
					if (criteria[i] != null && !criteria[i].equals("") && !dataArray[i].equals(criteria[i])) {
						continue add_loop;
					}
				}
			}
			finalResults.add(record);
		}

		return finalResults;
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

	public void notifyObservers(final Contractor contractor, final String cmd) {
		final List<RemoteObserver> staleRefs = new ArrayList<>();
		for (RemoteObserver o : this.observers) {
			try {
				o.update(contractor, cmd);
			} catch (RemoteException e) {
				App.logWarning("Found an uncontactable Client, removing it from the Server.");
				staleRefs.add(o);
			}
		}
		this.observers.removeAll(staleRefs);
	}

	public void addObserver(RemoteObserver o) {
		App.logWarning("Client connected");
		this.observers.add(o);
	}

	public void deleteObserver(RemoteObserver o) {
		App.logWarning("Client disconnected");
		this.observers.remove(o);
	}
}