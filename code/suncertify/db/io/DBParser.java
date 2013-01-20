package suncertify.db.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import suncertify.model.Contractor;

public class DBParser {

	public List<Contractor> get() {
		List<Contractor> contractors = new LinkedList<Contractor>();
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

				Contractor modelItem = new Contractor();
				modelItem.setDeleted(Arrays.toString(bytes));
				modelItem.setName(readString(is, fieldLengths[0]));
				modelItem.setLocation(readString(is, fieldLengths[1]));
				modelItem.setSpecialities(readString(is, fieldLengths[2]));
				modelItem.setSize(readString(is, fieldLengths[3]));
				modelItem.setRate(readString(is, fieldLengths[4]));
				modelItem.setOwner(readString(is, fieldLengths[5]));

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
		return new String(bytes, "US-ASCII");
	}
}
