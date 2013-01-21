package suncertify.db;

/**
 * Exception thrown when a specified record cannot be found. When an attempt to
 * find a particular record fails or the record does not exist then a
 * RecordNotFoundException is thrown.
 * 
 * @author Se�n Dunne
 * 
 */
public class RecordNotFoundException extends Exception {

	private static final long serialVersionUID = 2823045322464184130L;

	/**
	 * Constructs a RecordNotFoundException with no specified detail message.
	 */
	public RecordNotFoundException() {
	}

	/**
	 * Constructs a RecordNotFoundException with the specified detail message.
	 * 
	 * @param message
	 *            the detail message
	 */
	public RecordNotFoundException(String message) {
		super(message);
	}
}
