package suncertify.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

import suncertify.shared.Contractor;

/**
 * This interface describes an object that can listen for updates to
 * {@link Contractor} objects which are stored in the server database. It also
 * allows for implementing classes to register for and receive updates via RMI.
 * 
 * @author Sean Dunne
 */
public interface RemoteObserver extends Remote {

	/** The command used to notify observers a new Contractor has been created */
	String CREATE = "create";
	/** The command used to notify observers a Contractor has been deleted */
	String DELETE = "delete";
	/** The command used to notify observers a Contractor has been updated */
	String UPDATE = "update";

	/**
	 * The method called by the server when a {@link Contractor} has changed in
	 * the database.
	 * 
	 * @param contractor
	 *            The updated Contractor.
	 * @param cmd
	 *            A String representing in what way the Contractor changed,
	 *            create, update or delete.
	 * @throws RemoteException
	 *             Thrown when being called via RMI and registered client cannot
	 *             be contacted.
	 */
	void update(final Contractor contractor, final String cmd)
			throws RemoteException;

}