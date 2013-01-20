package suncertify.db;

import java.util.List;

import suncertify.db.io.DBParser;
import suncertify.model.Contractor;

/**
 * Singleton class to access the Database.
 * 
 * @author Sean
 * 
 */
public enum Data implements DBMain {

	INSTANCE;

	private DBParser parser = new DBParser();
	private List<Contractor> contractors = parser.get();

	// parser.get();

	@Override
	public String[] read(int recNo) throws RecordNotFoundException {
		// TODO Auto-generated method stub
		Contractor record = contractors.get(recNo);
		return record.getRecord();
	}

	@Override
	public void update(int recNo, String[] data) throws RecordNotFoundException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(int recNo) throws RecordNotFoundException {
		// TODO Auto-generated method stub

	}

	@Override
	public int[] find(String[] criteria) throws RecordNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int create(String[] data) throws DuplicateKeyException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void lock(int recNo) throws RecordNotFoundException {
		// TODO Auto-generated method stub

	}

	@Override
	public void unlock(int recNo) throws RecordNotFoundException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isLocked(int recNo) throws RecordNotFoundException {
		// TODO Auto-generated method stub
		return false;
	}

}
