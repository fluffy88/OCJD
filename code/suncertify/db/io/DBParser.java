package suncertify.db.io;

import static suncertify.db.io.DBSchema.FIELD_HEADERS;
import static suncertify.db.io.DBSchema.FIELD_LENGTHS;
import static suncertify.db.io.DBSchema.MAGIC_COOKIE;
import static suncertify.db.io.DBSchema.NUMBER_OF_FIELDS;
import static suncertify.db.io.DBSchema.RECORD_LENGTH;
import static suncertify.db.io.DBSchema.START_OF_RECORDS;
import static suncertify.db.io.DBSchema.US_ASCII;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class DBParser {

	private RandomAccessFile is;

	public DBParser(RandomAccessFile is) {
		this.is = is;
	}

	public List<String[]> get() {
		List<String[]> contractors = new LinkedList<String[]>();
		try {

			// headers
			MAGIC_COOKIE = is.readInt();
			START_OF_RECORDS = is.readInt();
			NUMBER_OF_FIELDS = is.readShort();

			// data headers
			FIELD_LENGTHS = new int[NUMBER_OF_FIELDS];
			FIELD_HEADERS = new String[NUMBER_OF_FIELDS];
			for (int i = 0; i < NUMBER_OF_FIELDS; i++) {
				// 2 byte numeric, length in bytes of field name
				int fieldNameLength = is.readShort();

				// n bytes (defined by previous entry), field name
				byte[] fieldName = new byte[fieldNameLength];
				is.read(fieldName);
				FIELD_HEADERS[i] = new String(fieldName, US_ASCII);

				// 2 byte numeric, field length in bytes
				int fieldLength = is.readShort();
				FIELD_LENGTHS[i] = fieldLength;
				RECORD_LENGTH += fieldLength;
			}

			// data
			is.seek(START_OF_RECORDS);
			while (is.getFilePointer() != is.length()) {
				byte[] bytes = new byte[2];
				is.read(bytes);

				// TODO: IF IS DELETED RECORD: Arrays.toString(bytes)
				String[] dataItem = new String[6];
				dataItem[0] = readString(is, FIELD_LENGTHS[0]);
				dataItem[1] = readString(is, FIELD_LENGTHS[1]);
				dataItem[2] = readString(is, FIELD_LENGTHS[2]);
				dataItem[3] = readString(is, FIELD_LENGTHS[3]);
				dataItem[4] = readString(is, FIELD_LENGTHS[4]);
				dataItem[5] = readString(is, FIELD_LENGTHS[5]);

				contractors.add(dataItem);

				System.out.println(new String(bytes) + " - " + Arrays.toString(dataItem));
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return contractors;
	}

	private String readString(RandomAccessFile is, int n) throws IOException {
		byte[] bytes = new byte[n];
		is.read(bytes);
		return new String(bytes, "US-ASCII").trim();
	}
}
