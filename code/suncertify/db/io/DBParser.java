package suncertify.db.io;

import static suncertify.db.io.DBSchema.EXPECTED_MAGIC_COOKIE;
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

/**
 * This class is responsible for the reading of the database file.
 * 
 * @author Sean Dunne
 */
public class DBParser {

	private final RandomAccessFile is;

	/**
	 * Create a new instance of this class to parse the database file.
	 * 
	 * @param is
	 *            The database file to be parsed.
	 */
	public DBParser(final RandomAccessFile is) {
		this.is = is;
	}

	/**
	 * This method reads all records from the database file.
	 * 
	 * @return A List containing all the database records.
	 */
	public List<String[]> getAllRecords() {
		final List<String[]> contractors = new ArrayList<String[]>();
		try {
			this.checkMagicCookie();
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

	/**
	 * This method checks that the value of the magic cookie in the database file matches the cookie value of the database for this
	 * application.
	 * 
	 * @throws IOException
	 *             If reading the magic cookie value from the file fails.
	 */
	private void checkMagicCookie() throws IOException {
		MAGIC_COOKIE = this.is.readInt();
		if (MAGIC_COOKIE != EXPECTED_MAGIC_COOKIE) {
			App.showErrorAndExit("The selected database file is not compatible with this application.");
		}
	}

	/**
	 * This method is used to read all the metadata file headers. It stores all the read values in {@link DBSchema}.
	 * 
	 * @throws IOException
	 *             If reading from the database file fails.
	 */
	private void readDataFileHeaders() throws IOException {
		// headers
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

	/**
	 * This method is used to read one full record from the database. It assumes the file-pointer is at the beginning of a record.
	 * 
	 * @return An array containing the fields for this record.
	 * @throws IOException
	 *             If reading from the database file fails.
	 */
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
	 * @param n
	 *            The number of bytes to be read for the String.
	 * @return The trimmed result of reading n bytes from the RandomAccessFile and converting to a String.
	 * @throws IOException
	 *             If something goes wrong when reading from the RandomAccessFile.
	 */
	private String readString(final int n) throws IOException {
		final byte[] bytes = new byte[n];
		this.is.read(bytes);
		return new String(bytes, US_ASCII).trim();
	}
}
