package suncertify.db.io;

/**
 * This class contains the database file structure. It is also populated with
 * the database metadata headers once the database file is read by
 * {@link DBParser}.
 * 
 * @author Sean Dunne
 */
public class DBSchema {

	/** The character encoding used for the database file. */
	public static final String US_ASCII = "US-ASCII";

	// File Headers
	/** The number of bytes used to store the magic cookie value. */
	public static final int NUM_BYTES_MAGIC_COOKIE = 4;
	/**
	 * The number of bytes used to store the position where the database records
	 * begin.
	 */
	public static final int NUM_BYTES_START_OF_RECORDS = 4;
	/**
	 * The number of bytes used to store the number of fields each record
	 * contains.
	 */
	public static final int NUM_BYTES_NUMBER_OF_FIELDS = 2;

	// Describing data structure
	/** The number of bytes used to store the length of each field name. */
	public static final int NUM_BYTES_FIELD_HEADER = 2;
	/** The number of bytes used to store the length of each field. */
	public static final int NUM_BYTES_FIELD_LENGTH = 2;
	/** The number of bytes used to store the deleted flag. */
	public static final int NUM_BYTES_RECORD_DELETED_FLAG = 2;

	// Deleted Flags
	/** The flag used to indicate a record is valid/not deleted. */
	public static final int RECORD_VALID = 00;
	/** The falg used to indicate a record is deleted from the database. */
	public static final int RECORD_DELETED = 0x8000;

	// File Headers - Values
	/** The magic cookie for the database of this application. */
	public static int EXPECTED_MAGIC_COOKIE = 514;
	/** The actual magic cookie read from the database file by {@link DBParser}. */
	public static int MAGIC_COOKIE;
	/**
	 * The position where the records begin in the database file. Read in by
	 * {@link DBParser}.
	 */
	public static int START_OF_RECORDS;
	/** The number of fields each record has. Read in by {@link DBParser}. */
	public static int NUMBER_OF_FIELDS;

	// Fields
	/** The headers of each field in the database. Read in by {@link DBParser}. */
	public static String[] FIELD_HEADERS;
	/**
	 * The length in bytes of each field in the database. Read in by
	 * {@link DBParser}.
	 */
	public static int[] FIELD_LENGTHS;
	/** The length of each record in the database. Read in by {@link DBParser}. */
	public static int RECORD_LENGTH = NUM_BYTES_RECORD_DELETED_FLAG;

}
