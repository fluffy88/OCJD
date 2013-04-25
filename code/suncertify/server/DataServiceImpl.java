package suncertify.server;

import java.rmi.RemoteException;
import java.util.ArrayList;

import suncertify.db.DBMain;
import suncertify.db.DuplicateKeyException;
import suncertify.db.RecordNotFoundException;
import suncertify.db.ServerFactory;

public class DataServiceImpl implements DataService {

	DBMain data = ServerFactory.createDataService();

	@Override
	public String[] read(final int recNo) throws RecordNotFoundException, RemoteException {
		this.data.lock(recNo);
		String[] record = this.data.read(recNo);
		this.data.unlock(recNo);

		return record;
	}

	@Override
	public void update(final int recNo, final String[] data) throws RecordNotFoundException, RemoteException {
		this.data.lock(recNo);
		this.data.update(recNo, data);
		this.data.unlock(recNo);
	}

	@Override
	public void delete(final int recNo) throws RecordNotFoundException, RemoteException {
		this.data.lock(recNo);
		this.data.delete(recNo);
		this.data.unlock(recNo);
	}

	@Override
	public int[] find(final String[] criteria, final boolean exactMatch) throws RecordNotFoundException, RemoteException {
		final int[] rawResults = this.data.find(criteria);
		if (exactMatch) {
			final ArrayList<Integer> filteredResults = new ArrayList<Integer>();
			for (int recNo : rawResults) {
				String[] record = this.read(recNo);
				for (int i = 0; i < criteria.length; i++) {
					if (criteria[i] != null && !criteria[i].equals("") && record[i].equalsIgnoreCase(criteria[i])) {
						filteredResults.add(recNo);
					}
				}
			}
			// TODO: Need to return (int[]) filteredResults;
		}
		return rawResults;
	}

	@Override
	public int create(final String[] data) throws DuplicateKeyException, RemoteException {
		return this.data.create(data);
	}

}