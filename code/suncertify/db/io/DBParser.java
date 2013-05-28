package suncertify.db.io;

import static suncertify.db.io.DBSchema.FIELD_HEADERS;
import static suncertify.db.io.DBSchema.FIELD_LENGTHS;
import static suncertify.db.io.DBSchema.MAGIC_COOKIE;
import static suncertify.db.io.DBSchema.NUMBER_OF_FIELDS;
import static suncertify.db.io.DBSchema.NUM_BYTES_RECORD_DELETED_FLAG;
import static suncertify.db.io.DBSchema.RECORD_LENGTH;
import static suncertify.db.io.DBSchema.START_OF_RECORDS;
import static suncertify.db.io.DBSchema.US_ASCII;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import suncertify.shared.App;

public class DBParser {

	private final RandomAccessFile is;

	public DBParser(final RandomAccessFile is) {
		this.is = is;
	}

	public List<String[]> getAllRecords() {
		final List<String[]> contractors = new ArrayList<String[]>();
		try {

			this.readDataFileHeaders();

			// data
			this.is.seek(START_OF_RECORDS);
			while (this.is.getFilePointer() != this.is.length()) {
				final String[] dataItem = readNextRecord();
				contractors.add(dataItem);
			}

		} catch (IOException e) {
			App.showErrorAndExit("Cannot read from database file.");
		}
		return contractors;
	}

	private void readDataFileHeaders() throws IOException {
		// headers
		MAGIC_COOKIE = this.is.readInt();
		START_OF_RECORDS = this.is.readInt();
		NUMBER_OF_FIELDS = this.is.readShort();

		// data headers
		FIELD_LENGTHS = new int[NUMBER_OF_FIELDS];
		FIELD_HEADERS = new String[NUMBER_OF_FIELDS];
		for (int i = 0; i < NUMBER_OF_FIELDS; i++) {
			// 2 byte numeric, length (in bytes) of field name
			final int fieldNameLength = this.is.readShort();

			// n bytes (defined by previous entry), field name
			FIELD_HEADERS[i] = this.readString(fieldNameLength);

			// 2 byte numeric, field length in bytes
			final int fieldLength = this.is.readShort();
			FIELD_LENGTHS[i] = fieldLength;
			RECORD_LENGTH += fieldLength;
		}
	}

	private String[] readNextRecord() throws IOException {
		final short flag = this.is.readShort();

		final String[] dataItem = new String[NUMBER_OF_FIELDS];
		if (flag == 0) {
			for (int i = 0; i < dataItem.length; i++) {
				dataItem[i] = this.readString(FIELD_LENGTHS[i]);
			}
		} else {
			// skip the next record as it's marked deleted
			this.is.seek(this.is.getFilePointer() + RECORD_LENGTH - NUM_BYTES_RECORD_DELETED_FLAG);
		}
		return dataItem;
	}

	/**
	 * Simple implementation of a RandomAccessFile#readString() as it's not included by default.
	 * 
	 * @param is
	 *            The RandomAccessFile instance to read from.
	 * @param n
	 *            The number of bytes to be read for the String.
	 * @return The trimmed result of reading n bytes from the RandomAccessFile and converting to a String.
	 * @throws IOException
	 *             If something does wrong when reading from the RandomAccessFile.
	 */
	private String readString(final int n) throws IOException {
		final byte[] bytes = new byte[n];
		this.is.read(bytes);
		return new String(bytes, US_ASCII).trim();
	}
}
