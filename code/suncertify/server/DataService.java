package suncertify.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import suncertify.db.DuplicateKeyException;
import suncertify.db.RecordNotFoundException;

public interface DataService extends Remote {

	String[] read(int recNo) throws RecordNotFoundException, RemoteException;

	void update(int recNo, String[] data) throws RecordNotFoundException, RemoteException;

	void delete(int recNo) throws RecordNotFoundException, RemoteException;

	int[] find(String[] criteria, boolean exactMatch) throws RecordNotFoundException, RemoteException;

	int create(String[] data) throws DuplicateKeyException, RemoteException;

}