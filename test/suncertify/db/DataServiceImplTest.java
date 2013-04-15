package suncertify.db;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.rmi.RemoteException;

import org.junit.Before;
import org.junit.Test;

import suncertify.db.DuplicateKeyException;
import suncertify.db.RecordNotFoundException;
import suncertify.server.DataService;
import suncertify.server.DataServiceImpl;

public class DataServiceImplTest {

	private DataService dataService;

	private static final int READ_REC_NO = 14;
	private static final int UPDATE_REC_NO = 15;
	private static final int DELETE_REC_NO = 16;

	@Before
	public void setup() {
		this.dataService = new DataServiceImpl();
	}

	@Test
	public void testRead() throws RemoteException, RecordNotFoundException {
		String[] record = this.dataService.read(READ_REC_NO);

		assertThat(record[0], is(equalTo("Hamner & Tong")));
		assertThat(record[1], is(equalTo("EmeraldCity")));
		assertThat(record[2], is(equalTo("Roofing, Electrical")));
		assertThat(record[3], is(equalTo("7")));
		assertThat(record[4], is(equalTo("$45.00")));
		assertThat(record[5], is(equalTo("")));
	}

	@Test
	public void testUpdate() throws RemoteException, RecordNotFoundException {
		String[] origRecord = this.dataService.read(UPDATE_REC_NO);
		assertThat(origRecord[0], is(equalTo("Bitter Homes & Gardens")));
		assertThat(origRecord[1], is(equalTo("EmeraldCity")));
		assertThat(origRecord[2], is(equalTo("Heating, Plumbing, Painting")));
		assertThat(origRecord[3], is(equalTo("6")));
		assertThat(origRecord[4], is(equalTo("$90.00")));
		assertThat(origRecord[5], is(equalTo("")));

		String[] newRecordData = new String[] { "Belter", "Greenland", "Green house building", "32", "$53.50", "12" };
		this.dataService.update(UPDATE_REC_NO, newRecordData);
		String[] updatedRecord = this.dataService.read(UPDATE_REC_NO);
		for (int i = 0; i < newRecordData.length; i++) {
			assertThat(updatedRecord[i], is(equalTo(newRecordData[i])));
		}

		this.dataService.update(UPDATE_REC_NO, origRecord);
	}

	@Test(expected = RecordNotFoundException.class)
	public void testDelete() throws RemoteException, RecordNotFoundException, DuplicateKeyException {
		String[] record = this.dataService.read(DELETE_REC_NO);
		this.dataService.delete(DELETE_REC_NO);
		this.dataService.read(DELETE_REC_NO);
	}

	@Test
	public void testFind() throws RemoteException, RecordNotFoundException {
		String[] findCriteria = new String[] { "Hamner" };
		int[] records = this.dataService.find(findCriteria);
		assertThat(records.length, is(equalTo(0)));

		findCriteria = new String[] { "Bitter Homes & Gardens" };
		records = this.dataService.find(findCriteria);
		assertThat(records.length, is(equalTo(2)));
		assertThat(records[0], is(equalTo(6)));
		assertThat(records[1], is(equalTo(15)));
	}

	@Test
	public void testCreate() throws RemoteException, DuplicateKeyException {
		String[] newRecord = new String[] { "Hello Kitty", "Japan", "Bags", "12345", "$99.00", "" };
		int newRecordNum = this.dataService.create(newRecord);

		assertThat(newRecordNum, is(equalTo(16)));
	}

	@Test(expected = DuplicateKeyException.class)
	public void testCreateDup() throws RemoteException, DuplicateKeyException {
		String[] newRecord = new String[] { "DUPPLICATE", "DUPPLICATE", "DUPPLICATE", "DUPPLICATE", "DUPPLICATE", "DUPPLICATE" };
		this.dataService.create(newRecord);
		this.dataService.create(newRecord);
	}

	@Test
	public void testCreateEmpty() throws RemoteException, DuplicateKeyException {
		String[] newRecord = new String[] {};
		this.dataService.create(newRecord);
	}
}