package suncertify.db;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import suncertify.db.io.DBParser;
import suncertify.model.Contractor;

/**
 * Singleton class to access the Database.
 * 
 * @author Sean
 * 
 */
public class Data implements DBMain {

	public static final Data INSTANCE = new Data();

	private DBParser parser = new DBParser();
	private List<Contractor> contractors = parser.get();

	private CopyOnWriteArrayList<Contractor> syncContractors = new CopyOnWriteArrayList<>(contractors);

	private List<WriteLock> locks = new ArrayList<WriteLock>();

	private Data() {
	}

	@Override
	public String[] read(int recNo) throws RecordNotFoundException, RemoteException {
		if (recNo < 0) {
			throw new IllegalArgumentException("The record number cannot be negative.");
		}
		if (contractors.size() > recNo) {
			Contractor contractor = contractors.get(recNo);
			return contractor.getRecord();
		}
		throw new RecordNotFoundException("No record found for record number: " + recNo);
	}

	@Override
	public void update(int recNo, String[] data) throws RecordNotFoundException, RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(int recNo) throws RecordNotFoundException, RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public int[] find(String[] criteria) throws RecordNotFoundException, RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int create(String[] data) throws DuplicateKeyException, RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void lock(int recNo) throws RecordNotFoundException, RemoteException {
		// TODO Auto-generated method stub
		checkRecordNumber(recNo);

		WriteLock lock = locks.get(recNo);
		lock.lock();
	}

	@Override
	public void unlock(int recNo) throws RecordNotFoundException, RemoteException {
		// TODO Auto-generated method stub
		checkRecordNumber(recNo);

		WriteLock lock = locks.get(recNo);
		lock.unlock();
	}

	@Override
	public boolean isLocked(int recNo) throws RecordNotFoundException, RemoteException {
		// TODO Auto-generated method stub
		checkRecordNumber(recNo);

		WriteLock lock = locks.get(recNo);
		boolean check = lock.tryLock();
		lock.unlock();
		return check;
	}

	private void checkRecordNumber(int recNo) throws RecordNotFoundException {
		if (recNo < 0) {
			throw new IllegalArgumentException("The record number cannot be negative.");
		}
		if (contractors.size() <= recNo) {
			throw new RecordNotFoundException("No record found for record number: " + recNo);
		}
	}
}