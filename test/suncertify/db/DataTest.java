package suncertify.db;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.rmi.RemoteException;

import org.junit.Test;

public class DataTest {

	private Data dataService = Data.INSTANCE;

	@Test
	public void testRead() throws RecordNotFoundException, RemoteException {
		String[] record = dataService.read(0);

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
		dataService.read(500000);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testReadNegativeException() throws RecordNotFoundException, RemoteException {
		dataService.read(-1);
	}

	@Test
	public void testUpdate() throws RecordNotFoundException {
		int recNo = 5;

		String[] origRec = dataService.read(recNo);
		String[] newRec = new String[] { "Maggi's Gears", "Crazy town", "Cooking, Managing", "4", "$8.65", "00447799" };

		dataService.update(recNo, newRec);
		String[] afterRec = dataService.read(recNo);

		assertThat(afterRec[0], is(equalTo(newRec[0])));
		assertThat(afterRec[1], is(equalTo(newRec[1])));
		assertThat(afterRec[2], is(equalTo(newRec[2])));
		assertThat(afterRec[3], is(equalTo(newRec[3])));
		assertThat(afterRec[4], is(equalTo(newRec[4])));
		assertThat(afterRec[5], is(equalTo(newRec[5])));

		dataService.update(recNo, origRec);
		afterRec = dataService.read(recNo);

		assertThat(afterRec[0], is(equalTo(origRec[0])));
		assertThat(afterRec[1], is(equalTo(origRec[1])));
		assertThat(afterRec[2], is(equalTo(origRec[2])));
		assertThat(afterRec[3], is(equalTo(origRec[3])));
		assertThat(afterRec[4], is(equalTo(origRec[4])));
		assertThat(afterRec[5], is(equalTo(origRec[5])));
	}

	@Test
	public void testDelete() {
		fail("Not yet implemented");
	}

	@Test
	public void testFind() {
		fail("Not yet implemented");
	}

	@Test
	public void testCreate() {
		fail("Not yet implemented");
	}

	@Test
	public void testLock() {
		fail("Not yet implemented");
	}

	@Test
	public void testUnlock() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsLocked() {
		fail("Not yet implemented");
	}

}
