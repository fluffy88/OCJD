package suncertify.db;

/**
 * Exception thrown when an attempting to create a new record with a Key that
 * already exists. When a new record is being created and added to the database,
 * if the database Key of the new record is not unique the a
 * DuplicateKeyException will be thrown.
 * 
 * @author Seán Dunne
 * 
 */
public class DuplicateKeyException extends Exception {

	private static final long serialVersionUID = 4104980693153062865L;

	/**
	 * Constructs a DuplicateKeyException with no specified detail message.
	 */
	public DuplicateKeyException() {
	}

	/**
	 * Constructs a DuplicateKeyException with the specified detail message.
	 * 
	 * @param message
	 *            the detail message
	 */
	public DuplicateKeyException(String message) {
		super(message);
	}
}
