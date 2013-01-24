package suncertify.db.io;

import static suncertify.db.io.DBSchema.FIELD_LENGTHS;
import static suncertify.db.io.DBSchema.RECORD_DELETED;
import static suncertify.db.io.DBSchema.RECORD_LENGTH;
import static suncertify.db.io.DBSchema.RECORD_VALID;
import static suncertify.db.io.DBSchema.START_OF_RECORDS;
import static suncertify.db.io.DBSchema.US_ASCII;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class DBWriter {

	private RandomAccessFile is;

	public DBWriter(RandomAccessFile is) throws FileNotFoundException {
		this.is = is;
	}

	public boolean write(int recNo, String[] data) {
		try {

			int pos = START_OF_RECORDS + (RECORD_LENGTH * recNo);
			is.seek(pos);

			// write 2 byte flag to indicate not deleted
			is.writeShort(RECORD_VALID);
			for (int i = 0; i < data.length; i++) {
				byte[] updatedData = Arrays.copyOf(data[i].getBytes(US_ASCII), FIELD_LENGTHS[i]);
				is.write(updatedData);
			}
			return true;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public boolean delete(int recNo) {

		int pos = START_OF_RECORDS + (RECORD_LENGTH * recNo);
		try {
			is.seek(pos);
			is.writeShort(RECORD_DELETED);
			return true;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

}
