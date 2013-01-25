package suncertify.db.io;

import static suncertify.db.io.DBSchema.FIELD_LENGTHS;
import static suncertify.db.io.DBSchema.NUM_BYTES_RECORD_DELETED_FLAG;
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

			this.writeRecord(pos, data);
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

	public boolean create(String[] data) {
		try {
			is.seek(START_OF_RECORDS);

			while (is.getFilePointer() != is.length()) {
				int flag = is.readShort();
				if (flag != 0) {
					this.writeRecord(is.getFilePointer() - NUM_BYTES_RECORD_DELETED_FLAG, data);
					return true;
				}
				// skip the record
				is.seek(is.getFilePointer() + RECORD_LENGTH - NUM_BYTES_RECORD_DELETED_FLAG);
			}

			this.writeRecord(is.getFilePointer(), data);
			return true;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	private void writeRecord(long pos, String[] data) throws IOException {
		is.seek(pos);

		// write 2 byte flag to indicate not deleted
		is.writeShort(RECORD_VALID);
		for (int i = 0; i < data.length; i++) {
			byte[] updatedData = Arrays.copyOf(data[i].getBytes(US_ASCII), FIELD_LENGTHS[i]);
			is.write(updatedData);
		}
	}
}