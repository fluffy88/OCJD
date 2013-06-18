package suncertify.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import suncertify.client.RemoteObserver;
import suncertify.db.DuplicateKeyException;
import suncertify.db.RecordNotFoundException;
import suncertify.shared.Contractor;

/**
 * This is the main interface of the server side. It specifies how clients can
 * interact with the server.
 * 
 * @author Sean Dunne
 */
public interface DataService extends Remote {

	/**
	 * This method reads a Contractor object from the database.
	 * 
	 * @param recNo
	 *            The record number of the Contractor.
	 * @return The Contractor object.
	 * @throws RecordNotFoundException
	 *             When no Contractor was found with the specified record
	 *             number.
	 * @throws RemoteException
	 *             If called over RMI and the server cannot be contacted.
	 */
	Contractor read(final int recNo) throws RecordNotFoundException,
			RemoteException;

	/**
	 * This method will update the Contractor in the database.
	 * 
	 * @param record
	 *            The Contractor object to update.
	 * @throws RecordNotFoundException
	 *             When no Contractor was found that matches the record number
	 *             of the specified Contractor object.
	 * @throws RemoteException
	 *             If called over RMI and the server cannot be contacted.
	 */
	void update(final Contractor record) throws RecordNotFoundException,
			RemoteException;

	/**
	 * This method deletes a Contractor from the database.
	 * 
	 * @param record
	 *            The Contractor object to delete.
	 * @throws RecordNotFoundException
	 *             When no Contractor was found that matches the record number
	 *             of the specified Contractor object.
	 * @throws RemoteException
	 *             If called over RMI and the server cannot be contacted.
	 */
	void delete(final Contractor record) throws RecordNotFoundException,
			RemoteException;

	/**
	 * This method searches the database for records that match the given search
	 * criteria.
	 * 
	 * @param criteria
	 *            An array with the search criteria for each field of a
	 *            Contractor.
	 * @param exactMatch
	 *            If the results should only contain Contractors whose fields
	 *            exactly match the search criteria.
	 * @return All the Contractor objects that match the criteria.
	 * @throws RemoteException
	 *             If called over RMI and the server cannot be contacted.
	 */
	List<Contractor> find(final String[] criteria, boolean exactMatch)
			throws RemoteException;

	/**
	 * This method will create a new Contractor in the database.
	 * 
	 * @param data
	 *            An array containing the fields used to create the Contractor.
	 * @return The record number of the new Contractor.
	 * @throws DuplicateKeyException
	 *             If this Contractor already exists.
	 * @throws RemoteException
	 *             If called over RMI and the server cannot be contacted.
	 */
	int create(final String[] data) throws DuplicateKeyException,
			RemoteException;

	/**
	 * This method allows clients register to receive updates when the database
	 * records are changed.
	 * 
	 * @param o
	 *            The client object that will be informed of database changes.
	 * @throws RemoteException
	 *             If called over RMI and the server cannot be contacted.
	 */
	void addObserver(RemoteObserver o) throws RemoteException;

	/**
	 * This method allows clients removed themselves for receiving updates for
	 * the server.
	 * 
	 * @param o
	 *            The client object that is registered with the server.
	 * @throws RemoteException
	 *             If called over RMI and the server cannot be contacted.
	 */
	void deleteObserver(RemoteObserver o) throws RemoteException;

}