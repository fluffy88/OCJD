package suncertify.db;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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

	private static RandomAccessFile is;
	private static List<String[]> contractors;
	private static DBWriter dbWriter;

	private static List<ReentrantReadWriteLock> locks;

	public Data() {
		// init();
	}

	static {
		try {
			Data.is = new RandomAccessFile("db-2x2.db", "rw");
			DBParser parser = new DBParser(Data.is);
			Data.contractors = parser.get();
			Data.dbWriter = new DBWriter(Data.is);

			Data.locks = new ArrayList<ReentrantReadWriteLock>();
			Data.locks.addAll(Collections.nCopies(Data.contractors.size(), new ReentrantReadWriteLock()));

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String[] read(int recNo) throws RecordNotFoundException {
		checkRecordNumber(recNo);

		this.readLock(recNo);
		String[] contractor = this.contractors.get(recNo);
		System.out.println("Read: " + recNo + " - " + Arrays.toString(contractor));
		this.readUnLock(recNo);
		return Arrays.copyOf(contractor, contractor.length);
	}

	@Override
	public void update(int recNo, String[] data) throws RecordNotFoundException {
		checkRecordNumber(recNo);

		this.writeLock(recNo);
		System.out.println("Update: " + recNo + " - " + Arrays.toString(data));
		boolean succeeded = this.dbWriter.write(recNo, data);

		// TODO if database failed, roll back cache and handle error
		if (succeeded) {
			contractors.set(recNo, data);
		}
		this.writeUnLock(recNo);
	}

	@Override
	public void delete(int recNo) throws RecordNotFoundException {
		System.out.println("Delete: " + recNo);
		checkRecordNumber(recNo);

		this.writeLock(recNo);
		boolean succeeded = dbWriter.delete(recNo);

		// TODO if database failed, roll back cache and handle error
		if (succeeded) {
			contractors.set(recNo, new String[DBSchema.NUMBER_OF_FIELDS]);
		}
		this.writeUnLock(recNo);
	}

	@Override
	public int[] find(String[] criteria) throws RecordNotFoundException {
		System.out.println("Find: " + Arrays.toString(criteria));

		List<Integer> results = new ArrayList<Integer>();
		for (int n = 0; n < contractors.size(); n++) {
			this.readLock(n);
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
			this.readUnLock(n);
		}

		int[] intResults = new int[results.size()];
		for (int i = 0; i < results.size(); i++) {
			intResults[i] = results.get(i);
		}

		return intResults;
	}

	@Override
	public int create(String[] data) throws DuplicateKeyException {
		int recNo = -1;

		for (int i = 0; i < contractors.size(); i++) {
			this.readLock(i);
			String[] record = contractors.get(i);
			if (record[0] == null && recNo == -1) {
				recNo = i;
			} else if (record[0].equals(data[0]) && record[1].equals(data[1])) {
				this.readUnLock(i);
				throw new DuplicateKeyException("A record with this Name & Address already exists.");
			}
			this.readUnLock(i);
		}

		if (recNo != -1) {
			// TODO when write to db fails what now??
			this.writeLock(recNo);
			boolean succeeded = dbWriter.create(data);

			if (succeeded) {
				contractors.set(recNo, data);
			}
			this.writeUnLock(recNo);
		} else {
			synchronized (Data.is) {
				boolean succeeded = dbWriter.create(data);
				if (succeeded) {
					contractors.add(data);
					locks.add(new ReentrantReadWriteLock());
					recNo = contractors.size() - 1;
				}
			}
		}

		System.out.println("Create: " + recNo + " - " + Arrays.toString(data));

		return recNo;
	}

	@Override
	public void lock(int recNo) throws RecordNotFoundException {
		// TODO Auto-generated method stub
		checkRecordNumber(recNo);

		this.writeLock(recNo);
	}

	@Override
	public void unlock(int recNo) throws RecordNotFoundException {
		// TODO Auto-generated method stub
		checkRecordNumber(recNo);

		this.writeUnLock(recNo);
	}

	@Override
	public boolean isLocked(int recNo) throws RecordNotFoundException {
		// TODO Auto-generated method stub
		checkRecordNumber(recNo);

		Lock lock = locks.get(recNo).writeLock();
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

	private void readLock(int recNo) {
		System.out.println("** Read Lock (" + recNo + ") **");
		Data.locks.get(recNo).readLock().lock();
	}

	private void readUnLock(int recNo) {
		Data.locks.get(recNo).readLock().unlock();
		System.out.println("** Read UnLock (" + recNo + ") **");
	}

	private void writeLock(int recNo) {
		System.out.println("** Write Lock (" + recNo + ") **");
		Data.locks.get(recNo).writeLock().lock();
	}

	private void writeUnLock(int recNo) {
		Data.locks.get(recNo).writeLock().unlock();
		System.out.println("** Write UnLock (" + recNo + ") **");
	}
}