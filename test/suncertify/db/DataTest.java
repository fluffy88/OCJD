package suncertify.db;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

import java.rmi.RemoteException;

import org.junit.BeforeClass;
import org.junit.Test;

import suncertify.db.io.DBSchema;
import suncertify.shared.App;
import suncertify.shared.Properties;

public class DataTest {

	public static final String DATABASE_FILE = "db-2x2.db";

	private static final int READ_NO = 11;
	private static final int DELETE_TWICE_NO = 27;

	private final DBMain dataService = DAOFactory.getDataService();

	@BeforeClass
	public static void setupClass() {
		Properties.set(App.PROP_DB_LOCATION, DataTest.DATABASE_FILE);
	}

	@Test
	public void testRead() throws RecordNotFoundException, RemoteException {
		this.dataService.lock(READ_NO);
		final String[] record = this.dataService.read(READ_NO);
		this.dataService.unlock(READ_NO);

		assertThat(record, is(notNullValue()));
		assertThat(record.length, is(equalTo(6)));
		assertThat(record[0], is(not(equalTo(""))));
		assertThat(record[1], is(not(equalTo(""))));
		assertThat(record[2], is(not(equalTo(""))));
		assertThat(Integer.parseInt(record[3]), is(notNullValue()));
		assertThat(Float.parseFloat(record[4].substring(1)), is(notNullValue()));
		// assertThat(record[5], is(not(equalTo(""))));
	}

	@Test(expected = RecordNotFoundException.class)
	public void testReadException() throws RecordNotFoundException, RemoteException {
		this.dataService.lock(500000);
		this.dataService.read(500000);
		this.dataService.unlock(500000);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testReadNegativeException() throws RecordNotFoundException, RemoteException {
		this.dataService.lock(-1);
		this.dataService.read(-1);
		this.dataService.unlock(-1);
	}

	@Test
	public void testUpdate() throws RecordNotFoundException, RemoteException {
		final int recNo = 5;

		final String[] newRec = new String[] { "Slappers Plasters", "In your head", "Plastering", "13", "$15.50", "" };

		this.dataService.lock(recNo);
		this.dataService.update(recNo, newRec);
		this.dataService.unlock(recNo);
		this.dataService.lock(recNo);
		final String[] afterRec = this.dataService.read(recNo);
		this.dataService.unlock(recNo);

		assertThat(afterRec[0], is(equalTo(newRec[0])));
		assertThat(afterRec[1], is(equalTo(newRec[1])));
		assertThat(afterRec[2], is(equalTo(newRec[2])));
		assertThat(afterRec[3], is(equalTo(newRec[3])));
		assertThat(afterRec[4], is(equalTo(newRec[4])));
		assertThat(afterRec[5], is(equalTo(newRec[5])));
	}

	@Test(expected = RecordNotFoundException.class)
	public void testDelete() throws RecordNotFoundException, RemoteException {
		this.dataService.lock(2);
		this.dataService.delete(2);
		this.dataService.unlock(2);
		this.dataService.lock(2);
		this.dataService.read(2);
		this.dataService.unlock(2);
	}

	@Test(expected = RecordNotFoundException.class)
	public void testDeleteTwice() throws RecordNotFoundException, RemoteException {
		this.dataService.lock(DELETE_TWICE_NO);
		this.dataService.delete(DELETE_TWICE_NO);
		this.dataService.unlock(DELETE_TWICE_NO);
		this.dataService.lock(DELETE_TWICE_NO);
		this.dataService.delete(DELETE_TWICE_NO);
		this.dataService.unlock(DELETE_TWICE_NO);
	}

	@Test
	public void testFindEmptyCriteria() throws RecordNotFoundException, RemoteException {
		String[] criteria = new String[] { null, null, null, null, null, null };
		int[] results = this.dataService.find(criteria);
		assertThat(results.length, is(not(0)));

		criteria = new String[] { "", "", "", "", "", "" };
		results = this.dataService.find(criteria);
		assertThat(results.length, is(not(0)));
	}

	@Test
	public void testFindCriteriaShort() throws RecordNotFoundException, RemoteException {
		final String[] criteria = new String[DBSchema.NUMBER_OF_FIELDS - 2];
		final int[] results = this.dataService.find(criteria);
		assertThat(results.length, is(not(0)));
	}

	@Test
	public void testFindNoResults() throws RecordNotFoundException, RemoteException {
		final String[] criteria = new String[DBSchema.NUMBER_OF_FIELDS];
		criteria[0] = "A fake name that doesn't exist in the database";
		final int[] empty = this.dataService.find(criteria);

		assertArrayEquals(new int[] {}, empty);
	}

	@Test
	public void testFindResults() throws RecordNotFoundException, RemoteException {
		final String[] criteria = new String[DBSchema.NUMBER_OF_FIELDS];
		criteria[0] = "M";
		final int[] results = this.dataService.find(criteria);
		assertThat(results.length, is(not(0)));

		for (final int recNo : results) {
			this.dataService.lock(recNo);
			final String[] record = this.dataService.read(recNo);
			this.dataService.unlock(recNo);
			assertThat(record, is(notNullValue()));
			assertThat(record.length, is(equalTo(6)));
			assertThat(record[0], is(not(equalTo(""))));
			assertThat(record[1], is(not(equalTo(""))));
			assertThat(record[2], is(not(equalTo(""))));
			assertThat(Integer.parseInt(record[3]), is(notNullValue()));
			assertThat(Float.parseFloat(record[4].substring(1)), is(notNullValue()));
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateEmpty() throws DuplicateKeyException {
		this.dataService.create(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateEmptyArray() throws DuplicateKeyException {
		this.dataService.create(new String[] {});
	}

	@Test
	public void testCreate() throws DuplicateKeyException, RecordNotFoundException, RemoteException {
		final String streetNo = Integer.toString((int) (Math.random() * 100000));
		final String[] data = new String[] { "Jammies", String.format("The Shire %s", streetNo), "Door stop making/fitting", "57", "$0", "" };
		final int newRecNo = this.dataService.create(data);

		this.dataService.lock(newRecNo);
		final String[] results = this.dataService.read(newRecNo);
		this.dataService.unlock(newRecNo);
		for (int i = 0; i < data.length; i++) {
			assertSame(data[i], results[i]);
		}
	}

	@Test(expected = DuplicateKeyException.class)
	public void testCreateDuplicate() throws DuplicateKeyException, RecordNotFoundException, RemoteException {
		final String[] data = new String[] { "Jammies_DUPPED", "The Shire", "Door stop making/fitting", "57", "$0", "" };

		final int recNo = this.dataService.create(data);
		this.dataService.lock(recNo);
		final String[] results = this.dataService.read(recNo);
		this.dataService.unlock(recNo);
		for (int i = 0; i < data.length; i++) {
			assertSame(data[i], results[i]);
		}

		this.dataService.create(data);
	}

	@Test
	public void testLock() throws RemoteException, RecordNotFoundException {
		assertThat(this.dataService.isLocked(18), is(equalTo(false)));
		this.dataService.lock(18);
		assertThat(this.dataService.isLocked(18), is(equalTo(true)));
		this.dataService.unlock(18);
		assertThat(this.dataService.isLocked(18), is(equalTo(false)));
	}

	@Test
	public void testUnlock() throws RemoteException, RecordNotFoundException {
		assertThat(this.dataService.isLocked(18), is(equalTo(false)));
		this.dataService.lock(18);
		assertThat(this.dataService.isLocked(18), is(equalTo(true)));
		this.dataService.unlock(18);
		assertThat(this.dataService.isLocked(18), is(equalTo(false)));
	}

	@Test
	public void testIsLocked() throws RemoteException, RecordNotFoundException {
		assertThat(this.dataService.isLocked(18), is(equalTo(false)));
		this.dataService.lock(18);
		assertThat(this.dataService.isLocked(18), is(equalTo(true)));
		this.dataService.unlock(18);
		assertThat(this.dataService.isLocked(18), is(equalTo(false)));
	}

}
