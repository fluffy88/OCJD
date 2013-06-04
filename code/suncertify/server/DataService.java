package suncertify.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import suncertify.client.RemoteObserver;
import suncertify.db.DuplicateKeyException;
import suncertify.db.RecordNotFoundException;
import suncertify.shared.Contractor;

public interface DataService extends Remote {

	Contractor read(final int recNo) throws RecordNotFoundException, RemoteException;

	void update(final Contractor data) throws RecordNotFoundException, RemoteException;

	void delete(final Contractor record) throws RecordNotFoundException, RemoteException;

	List<Contractor> find(final String[] criteria, boolean exactMatch) throws RecordNotFoundException, RemoteException;

	int create(final String[] data) throws DuplicateKeyException, RemoteException;

	void addObserver(RemoteObserver o) throws RemoteException;

	void deleteObserver(RemoteObserver o) throws RemoteException;

}