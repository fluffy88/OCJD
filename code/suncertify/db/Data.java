package suncertify.db;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import suncertify.db.io.DBParser;
import suncertify.db.io.DBSchema;
import suncertify.db.io.DBWriter;

/**
 * Singleton class to access the Database.
 * 
 * @author Sean
 * 
 */
public final class Data implements DBMain {

	private List<AtomicBoolean> locks;

	private RandomAccessFile is;
	private List<String[]> contractors;
	private DBWriter dbWriter;
	private final String dbLocation;

	private static Data instance;

	private Data(String dbLoc) {
		this.dbLocation = dbLoc;
		init();
	}

	public static final Data getInstance(String dbLoc) {
		if (instance == null) {
			instance = new Data(dbLoc);
		}
		return instance;
	}

	private final void init() {
		try {
			this.is = new RandomAccessFile(this.dbLocation, "rw");
			final DBParser parser = new DBParser(is);
			dbWriter = new DBWriter(is);

			contractors = parser.getAllRecords();
			locks = new ArrayList<AtomicBoolean>(contractors.size());
			for (int i = 0; i < contractors.size(); i++) {
				locks.add(new AtomicBoolean(false));
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public final String[] read(int recNo) throws RecordNotFoundException {
		this.lock(recNo);
		final String[] contractor = contractors.get(recNo);
		System.out.println("Read: " + recNo + " - " + Arrays.toString(contractor));
		this.unlock(recNo);
		return Arrays.copyOf(contractor, contractor.length);
	}

	@Override
	public final void update(int recNo, String[] data) throws RecordNotFoundException {
		System.out.println("Update: " + recNo + " - " + Arrays.toString(data));
		checkRecordNumber(recNo);

		final boolean succeeded = dbWriter.write(recNo, data);

		// TODO if database failed, roll back cache and handle error
		if (succeeded) {
			contractors.set(recNo, data);
		}
	}

	@Override
	public final void delete(int recNo) throws RecordNotFoundException {
		System.out.println("Delete: " + recNo);
		checkRecordNumber(recNo);

		final boolean succeeded = dbWriter.delete(recNo);

		// TODO if database failed, roll back cache and handle error
		if (succeeded) {
			contractors.set(recNo, new String[DBSchema.NUMBER_OF_FIELDS]);
		}
	}

	@Override
	public final int[] find(String[] criteria) throws RecordNotFoundException {
		System.out.println("Find: " + Arrays.toString(criteria));

		final List<Integer> results = new ArrayList<Integer>();
		for (int n = 0; n < contractors.size(); n++) {
			boolean match = true;
			for (int i = 0; i < criteria.length; i++) {
				if (criteria[i] != null) {
					String record = contractors.get(n)[i];
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
		}

		final int[] intResults = new int[results.size()];
		for (int i = 0; i < results.size(); i++) {
			intResults[i] = results.get(i);
		}

		return intResults;
	}

	@Override
	public final int create(String[] data) throws DuplicateKeyException {
		System.out.println("Create: " + Arrays.toString(data));

		int deletedPos = -1;
		for (int i = 0; i < contractors.size(); i++) {
			final String[] record = contractors.get(i);
			if (record[0] == null) {
				deletedPos = i;
			} else if (record[0].equals(data[0]) && record[1].equals(data[1])) {
				throw new DuplicateKeyException("A record with this Name & Address already exists.");
			}
		}

		// TODO when write to db fails what now??
		final boolean succeeded = dbWriter.create(data);

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
	public final void lock(int recNo) throws RecordNotFoundException {
		this.locks.get(recNo).set(true);
		checkRecordNumber(recNo);
	}

	@Override
	public final void unlock(int recNo) throws RecordNotFoundException {
		// TODO Auto-generated method stub
		checkRecordNumber(recNo);
		this.locks.get(recNo).set(false);
	}

	@Override
	public final boolean isLocked(int recNo) throws RecordNotFoundException {
		// TODO Auto-generated method stub
		checkRecordNumber(recNo);
		return false;
	}

	private final void checkRecordNumber(int recNo) throws RecordNotFoundException {
		if (recNo < 0) {
			throw new IllegalArgumentException("The record number cannot be negative.");
		}
		if (contractors.size() <= recNo) {
			throw new RecordNotFoundException("No record found for record number: " + recNo);
		}
		final String[] record = contractors.get(recNo);
		if (record[0] == null) {
			throw new RecordNotFoundException("Record number " + recNo + " has been deleted.");
		}
	}
}