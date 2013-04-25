package suncertify.db;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;

import suncertify.db.io.DBParser;
import suncertify.db.io.DBSchema;
import suncertify.db.io.DBWriter;

/**
 * Singleton class to access the Database.
 * 
 * @author Sean Dunne
 * 
 */
public class Data implements DBMain {

	private List<Semaphore> locks;
	private Semaphore createLock;

	private RandomAccessFile is;
	private List<String[]> contractors;
	private DBWriter dbWriter;
	private final String dbLocation;

	Data(String dbLoc) {
		this.dbLocation = dbLoc;
		init();
	}

	private void init() {
		try {
			this.is = new RandomAccessFile(this.dbLocation, "rw");
			final DBParser parser = new DBParser(this.is);
			this.dbWriter = new DBWriter(this.is);
			this.createLock = new Semaphore(1);

			this.contractors = parser.getAllRecords();
			this.locks = new ArrayList<Semaphore>(this.contractors.size());
			for (int i = 0; i < this.contractors.size(); i++) {
				locks.add(new Semaphore(1));
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] read(int recNo) throws RecordNotFoundException {
		// this.lock(recNo);

		this.checkRecordNumber(recNo);
		final String[] contractor = this.contractors.get(recNo);
		System.out.println("Read: " + recNo + " - " + Arrays.toString(contractor));

		// this.unlock(recNo);
		return Arrays.copyOf(contractor, contractor.length);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(int recNo, String[] data) throws RecordNotFoundException {
		// this.lock(recNo);

		System.out.println("Update: " + recNo + " - " + Arrays.toString(data));
		this.checkRecordNumber(recNo);

		final boolean succeeded = this.dbWriter.write(recNo, data);

		// TODO if database failed, roll back cache and handle error
		if (succeeded) {
			this.contractors.set(recNo, data);
		}

		// this.unlock(recNo);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(int recNo) throws RecordNotFoundException {
		// this.lock(recNo);

		System.out.println("Delete: " + recNo);
		this.checkRecordNumber(recNo);

		final boolean succeeded = this.dbWriter.delete(recNo);

		// TODO if database failed, roll back cache and handle error
		if (succeeded) {
			this.contractors.set(recNo, new String[DBSchema.NUMBER_OF_FIELDS]);
		}

		// this.unlock(recNo);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int[] find(String[] criteria) throws RecordNotFoundException {
		System.out.println("Find: " + Arrays.toString(criteria));

		final List<Integer> results = new ArrayList<Integer>();
		for (int n = 0; n < this.contractors.size(); n++) {
			if (this.contractors.get(n)[0] == null) {
				continue;
			}
			this.lock(n);

			boolean match = true;
			for (int i = 0; i < criteria.length; i++) {
				if (criteria[i] != null) {
					String record = this.contractors.get(n)[i];
					if (record != null) {
						record = record.toLowerCase();
						final String recordTest = criteria[i].toLowerCase();
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

			this.unlock(n);
		}

		final int[] intResults = new int[results.size()];
		for (int i = 0; i < results.size(); i++) {
			intResults[i] = results.get(i);
		}

		return intResults;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int create(String[] data) throws DuplicateKeyException {
		try {
			this.createLock.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Create: " + Arrays.toString(data));
		if (data == null || data.length < 2 || data[0] == null || data[1] == null || data[0].equals("") || data[1].equals("")) {
			this.createLock.release();
			throw new IllegalArgumentException("The Name & Address cannot be empty!");
		}

		int deletedPos = -1;
		for (int i = 0; i < this.contractors.size(); i++) {
			final String[] record = this.contractors.get(i);
			if (record[0] == null) {
				deletedPos = i;
				break;
			} else if (record[0].equals(data[0]) && record[1].equals(data[1])) {
				this.createLock.release();
				throw new DuplicateKeyException("A record with this Name & Address already exists.");
			}
		}

		// TODO when write to db fails what now??
		final boolean succeeded = this.dbWriter.create(data);

		int recNo = deletedPos;
		if (succeeded) {
			if (deletedPos != -1) {
				this.contractors.set(deletedPos, data);
				// for (int i = 0; i < contractors.size(); i++) { if (contractors.get(i)[0] == null) { contractors.set(i, data); recNo = i;
				// break; } }
			} else {
				this.contractors.add(data);
				this.locks.add(new Semaphore(1));
				recNo = this.contractors.size() - 1;
			}
		}
		this.createLock.release();
		return recNo;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void lock(int recNo) throws RecordNotFoundException {
		this.checkRecordNumber(recNo);
		try {
			this.locks.get(recNo).acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void unlock(int recNo) throws RecordNotFoundException {
		this.checkRecordNumber(recNo);
		this.locks.get(recNo).release();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLocked(int recNo) throws RecordNotFoundException {
		// TODO Auto-generated method stub
		this.checkRecordNumber(recNo);
		return this.isRecordLocked(recNo);
	}

	private boolean isRecordLocked(int recNo) {
		int permits = this.locks.get(recNo).availablePermits();
		if (permits == 0) {
			return true;
		}
		return false;
	}

	private void checkRecordNumber(int recNo) throws RecordNotFoundException {
		if (recNo < 0) {
			throw new IllegalArgumentException("The record number cannot be negative.");
		}
		if (this.contractors.size() <= recNo) {
			throw new RecordNotFoundException("No record found for record number: " + recNo);
		}
		final String[] record = this.contractors.get(recNo);
		if (record[0] == null && !this.isRecordLocked(recNo)) {
			throw new RecordNotFoundException("Record number " + recNo + " has been deleted.");
		}
	}
}