package suncertify.db;

/**
 * This interface describes how a client can access the database records. A
 * class that provides database access must implement this interface.
 * 
 * @author Sean Dunne
 */
public interface DBMain {

	/**
	 * Returns the record at the specified position. The record number is the
	 * position of the record in the Database.
	 * 
	 * @param recNo
	 *            The number of the record to search for.
	 * @return An array containing the values of the fields for this record.
	 * @throws RecordNotFoundException
	 *             If no record is found at the position.
	 */
	public String[] read(int recNo) throws RecordNotFoundException;

	/**
	 * Modifies the fields of a record. The new value for field n appears in
	 * data[n].
	 * 
	 * @param recNo
	 *            The record to be updated.
	 * @param data
	 *            The new values of the records fields.
	 * @throws RecordNotFoundException
	 *             If no record is found at the position.
	 */
	public void update(int recNo, String[] data) throws RecordNotFoundException;

	/**
	 * Deletes a record, making the record number and associated disk storage
	 * available for reuse.
	 * 
	 * @param recNo
	 *            The record to be deleted.
	 * @throws RecordNotFoundException
	 *             If no record is found at the position.
	 */
	public void delete(int recNo) throws RecordNotFoundException;

	/**
	 * Returns an array of record numbers that match the specified criteria.
	 * Field n in the database file is described by criteria[n]. A null value in
	 * criteria[n] matches any field value. A non-null value in criteria[n]
	 * matches any field value that begins with criteria[n].
	 * 
	 * @param criteria
	 *            The search criteria used for the search.
	 * @return An array containing the record numbers of all the records that
	 *         match the search criteria.
	 * @throws RecordNotFoundException
	 *             If reading a record from the database fails.
	 */
	public int[] find(String[] criteria) throws RecordNotFoundException;

	/**
	 * Creates a new record in the database (possibly reusing a deleted entry).
	 * Inserts the given data, and returns the record number of the new record.
	 * 
	 * @param data
	 *            The fields of the new record.
	 * @return The record number of the new record.
	 * @throws DuplicateKeyException
	 *             If a record with the primary key of name & location already
	 *             exists.
	 */
	public int create(String[] data) throws DuplicateKeyException;

	/**
	 * Locks a record so that it can only be updated or deleted by this client.
	 * If the specified record is already locked, the current thread gives up
	 * the CPU and consumes no CPU cycles until the record is unlocked.
	 * 
	 * @param recNo
	 *            The record to be locked.
	 * @throws RecordNotFoundException
	 *             If no record is found at the position.
	 */
	public void lock(int recNo) throws RecordNotFoundException;

	/**
	 * Releases the lock on a record.
	 * 
	 * @param recNo
	 *            The record to be unlocked.
	 * @throws RecordNotFoundException
	 *             If no record is found at the position.
	 */
	public void unlock(int recNo) throws RecordNotFoundException;

	/**
	 * Determines if a record is currently locked.
	 * 
	 * @param recNo
	 *            The record to be unlocked.
	 * @return true if the record is locked, false otherwise.
	 * @throws RecordNotFoundException
	 *             If no record is found at the position.
	 */
	public boolean isLocked(int recNo) throws RecordNotFoundException;
}