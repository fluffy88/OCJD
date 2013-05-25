package suncertify.db;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import suncertify.db.io.DBParser;
import suncertify.db.io.DBSchema;
import suncertify.db.io.DBWriter;
import suncertify.shared.App;

/**
 * Singleton class to access the Database.
 * 
 * @author Sean Dunne
 * 
 */
public class Data implements DBMain {

	private List<ReentrantLock> locks;
	private ReentrantLock createLock;

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
			this.createLock = new ReentrantLock();

			this.contractors = parser.getAllRecords();
			this.locks = new ArrayList<ReentrantLock>(this.contractors.size());
			for (int i = 0; i < this.contractors.size(); i++) {
				locks.add(new ReentrantLock());
			}
		} catch (FileNotFoundException e) {
			App.showErrorAndExit("Cannot open database file.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] read(int recNo) throws RecordNotFoundException {
		this.checkRecordNumber(recNo);
		final String[] contractor = this.contractors.get(recNo);
		System.out.println("Read: " + recNo + " - " + Arrays.toString(contractor));

		return Arrays.copyOf(contractor, contractor.length);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(int recNo, String[] data) throws RecordNotFoundException {
		System.out.println("Update: " + recNo + " - " + Arrays.toString(data));
		this.checkRecordNumber(recNo);

		this.dbWriter.write(recNo, data);
		this.contractors.set(recNo, data);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(int recNo) throws RecordNotFoundException {
		System.out.println("Delete: " + recNo);
		this.checkRecordNumber(recNo);

		this.dbWriter.delete(recNo);
		this.contractors.set(recNo, new String[DBSchema.NUMBER_OF_FIELDS]);
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
		this.createLock.lock();
		System.out.println("Create: " + Arrays.toString(data));
		if (data == null || data.length < 2 || data[0] == null || data[1] == null || data[0].equals("") || data[1].equals("")) {
			this.createLock.unlock();
			throw new IllegalArgumentException("The Name & Address cannot be empty!");
		}

		int deletedPos = -1;
		for (int i = 0; i < this.contractors.size(); i++) {
			final String[] record = this.contractors.get(i);
			if (record[0] == null) {
				deletedPos = i;
				break;
			} else if (record[0].equals(data[0]) && record[1].equals(data[1])) {
				this.createLock.lock();
				throw new DuplicateKeyException("A record with this Name & Address already exists.");
			}
		}

		this.dbWriter.create(data);

		int recNo = deletedPos;
		if (deletedPos != -1) {
			this.contractors.set(deletedPos, data);
		} else {
			this.contractors.add(data);
			this.locks.add(new ReentrantLock());
			recNo = this.contractors.size() - 1;
		}
		this.createLock.unlock();
		return recNo;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void lock(int recNo) throws RecordNotFoundException {
		this.checkRecordNumber(recNo);
		this.locks.get(recNo).lock();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void unlock(int recNo) throws RecordNotFoundException {
		this.checkRecordNumber(recNo);
		this.locks.get(recNo).unlock();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLocked(int recNo) throws RecordNotFoundException {
		this.checkRecordNumber(recNo);
		return this.isRecordLocked(recNo);
	}

	private boolean isRecordLocked(int recNo) {
		return this.locks.get(recNo).isLocked();
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