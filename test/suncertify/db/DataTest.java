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

	private Data data = Data.INSTANCE;

	@Test
	public void testRead() throws RecordNotFoundException, RemoteException {
		String[] record = data.read(0);

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
		data.read(500000);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testReadNegativeException() throws RecordNotFoundException, RemoteException {
		data.read(-1);
	}

	@Test
	public void testUpdate() {
		fail("Not yet implemented");
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
