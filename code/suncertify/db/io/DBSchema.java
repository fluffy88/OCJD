package suncertify.db.io;

public class DBSchema {

	public static final String US_ASCII = "US-ASCII";

	// File Headers
	public static final int NUM_BYTES_MAGIC_COOKIE = 4;
	public static final int NUM_BYTES_START_OF_RECORDS = 4;
	public static final int NUM_BYTES_NUMBER_OF_FIELDS = 2;

	// Describing data structure
	public static final int NUM_BYTES_FIELD_HEADER_NUM_BYTES = 2;
	public static final int NUM_BYTES_FIELD_LENGTH = 2;

	// File Headers - Values
	public static int MAGIC_COOKIE;
	public static int START_OF_RECORDS;
	public static int NUMBER_OF_FIELDS;

	// Fields
	public static String[] FIELD_HEADERS;
	public static int[] FIELD_LENGTHS;
	public static int RECORD_LENGTH = 2;

}
