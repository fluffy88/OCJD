package suncertify.server;

import java.rmi.RemoteException;

import suncertify.db.DBMain;
import suncertify.db.DuplicateKeyException;
import suncertify.db.RecordNotFoundException;
import suncertify.db.ServerFactory;

public class DataServiceImpl implements DataService {

	DBMain data = ServerFactory.createDataService();

	@Override
	public String[] read(final int recNo) throws RecordNotFoundException, RemoteException {
		return this.data.read(recNo);
	}

	@Override
	public void update(final int recNo, final String[] data) throws RecordNotFoundException, RemoteException {
		this.data.update(recNo, data);
	}

	@Override
	public void delete(final int recNo) throws RecordNotFoundException, RemoteException {
		this.data.delete(recNo);
	}

	@Override
	public int[] find(final String[] criteria) throws RecordNotFoundException, RemoteException {
		return this.data.find(criteria);
	}

	@Override
	public int create(final String[] data) throws DuplicateKeyException, RemoteException {
		return this.data.create(data);
	}

}