package suncertify.db;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import suncertify.db.io.DBParser;
import suncertify.db.io.DBSchema;
import suncertify.db.io.DBWriter;

/**
 * Singleton class to access the Database.
 * 
 * @author Sean
 * 
 */
public class Data implements DBMain {

	public static final Data INSTANCE = new Data();

	private RandomAccessFile is;
	private List<String[]> contractors;
	private DBWriter dbWriter;

	private List<WriteLock> locks;

	private Data() {
		init();
	}

	private void init() {
		locks = new ArrayList<WriteLock>();

		try {
			this.is = new RandomAccessFile("db-2x2.db", "rw");
			DBParser parser = new DBParser(is);
			contractors = parser.get();
			dbWriter = new DBWriter(is);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String[] read(int recNo) throws RecordNotFoundException {
		checkRecordNumber(recNo);

		String[] contractor = contractors.get(recNo);
		return Arrays.copyOf(contractor, contractor.length);
	}

	@Override
	public void update(int recNo, String[] data) throws RecordNotFoundException {
		checkRecordNumber(recNo);

		boolean succeeded = dbWriter.write(recNo, data);

		// TODO if database failed, roll back cache and handle error
		if (succeeded) {
			contractors.set(recNo, data);
		}
	}

	@Override
	public void delete(int recNo) throws RecordNotFoundException {
		checkRecordNumber(recNo);

		boolean succeeded = dbWriter.delete(recNo);

		// TODO if database failed, roll back cache and handle error
		if (succeeded) {
			contractors.set(recNo, new String[DBSchema.NUMBER_OF_FIELDS]);
		}
	}

	@Override
	public int[] find(String[] criteria) throws RecordNotFoundException {
		List<Integer> results = new ArrayList<Integer>();
		for (int n = 0; n < contractors.size(); n++) {
			boolean match = true;
			for (int i = 0; i < criteria.length; i++) {
				if (criteria[i] != null) {
					String record = contractors.get(n)[i];
					if (record != null) {
						record = record.toLowerCase();
						String recordTest = criteria[i].toLowerCase();
						if (!record.startsWith(recordTest)) {
							match = false;
						}
					} else {
						match = false;
					}
				}
			}

			if (match) {
				results.add(n);
			}
		}

		int[] intResults = new int[results.size()];
		for (int i = 0; i < results.size(); i++) {
			intResults[i] = results.get(i);
		}

		return intResults;
	}

	@Override
	public int create(String[] data) throws DuplicateKeyException {
		int deletedPos = -1;
		for (int i = 0; i < contractors.size(); i++) {
			String[] record = contractors.get(i);
			if (record[0] == null) {
				deletedPos = i;
			} else if (record[0].equals(data[0]) && record[1].equals(data[1])) {
				throw new DuplicateKeyException("A record with this Name & Address already exists.");
			}
		}

		// TODO when write to db fails what now??
		boolean succeeded = dbWriter.create(data);

		int recNo = deletedPos;
		if (succeeded) {
			if (deletedPos != -1) {
				contractors.set(deletedPos, data);
				// for (int i = 0; i < contractors.size(); i++) { if (contractors.get(i)[0] == null) { contractors.set(i, data); recNo = i;
				// break; } }
			} else {
				contractors.add(data);
				recNo = contractors.size() - 1;
			}
		}
		return recNo;
	}

	@Override
	public void lock(int recNo) throws RecordNotFoundException {
		// TODO Auto-generated method stub
		checkRecordNumber(recNo);

		WriteLock lock = locks.get(recNo);
		lock.lock();
	}

	@Override
	public void unlock(int recNo) throws RecordNotFoundException {
		// TODO Auto-generated method stub
		checkRecordNumber(recNo);

		WriteLock lock = locks.get(recNo);
		lock.unlock();
	}

	@Override
	public boolean isLocked(int recNo) throws RecordNotFoundException {
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
		String[] record = contractors.get(recNo);
		if (record[0] == null) {
			throw new RecordNotFoundException("Record number " + recNo + " has been deleted.");
		}
	}
}