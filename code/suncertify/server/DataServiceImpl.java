package suncertify.server;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import suncertify.db.DBMain;
import suncertify.db.DAOFactory;
import suncertify.db.DuplicateKeyException;
import suncertify.db.RecordNotFoundException;
import suncertify.shared.Contractor;

public class DataServiceImpl implements DataService {

	DBMain data = DAOFactory.getInstance().getDataService();

	@Override
	public Contractor read(final int recNo) throws RecordNotFoundException, RemoteException {
		this.data.lock(recNo);
		Contractor record = new Contractor(recNo, this.data.read(recNo));
		this.data.unlock(recNo);

		return record;
	}

	@Override
	public void update(final Contractor data) throws RecordNotFoundException, RemoteException {
		this.data.lock(data.getRecordId());
		this.data.update(data.getRecordId(), data.toArray());
		this.data.unlock(data.getRecordId());
	}

	@Override
	public void delete(final Contractor record) throws RecordNotFoundException, RemoteException {
		this.data.lock(record.getRecordId());
		this.data.delete(record.getRecordId());
		this.data.unlock(record.getRecordId());
	}

	@Override
	public List<Contractor> find(final String[] criteria, final boolean exactMatch) throws RecordNotFoundException, RemoteException {
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
	public int create(final String[] data) throws DuplicateKeyException, RemoteException {
		return this.data.create(data);
	}

}