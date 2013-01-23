package suncertify.db.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.List;

public class DBParser {

	public List<String[]> get() {
		List<String[]> contractors = new LinkedList<String[]>();
		try (RandomAccessFile is = new RandomAccessFile("db-2x2.db", "r")) {

			// headers
			int magicCookie = is.readInt();
			int startOfRecords = is.readInt();
			int fields = is.readShort();

			// data headers
			int[] fieldLengths = new int[fields];
			String[] fieldNames = new String[fields];
			for (int i = 0; i < fields; i++) {
				// 2 byte numeric, length in bytes of field name
				int fieldNameLength = is.readShort();

				// n bytes (defined by previous entry), field name
				byte[] fieldName = new byte[fieldNameLength];
				is.read(fieldName);
				fieldNames[i] = new String(fieldName, "US-ASCII");

				// 2 byte numeric, field length in bytes
				int fieldLength = is.readShort();
				fieldLengths[i] = fieldLength;
			}

			// data
			is.seek(startOfRecords);
			while (is.getFilePointer() != is.length()) {
				byte[] bytes = new byte[2];
				is.read(bytes);

				// TODO: IF IS DELETED RECORD: Arrays.toString(bytes)
				String[] modelItem = new String[6];
				modelItem[0] = readString(is, fieldLengths[0]);
				modelItem[1] = readString(is, fieldLengths[1]);
				modelItem[2] = readString(is, fieldLengths[2]);
				modelItem[3] = readString(is, fieldLengths[3]);
				modelItem[4] = readString(is, fieldLengths[4]);
				modelItem[5] = readString(is, fieldLengths[5]);

				contractors.add(modelItem);
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
