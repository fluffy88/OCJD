package suncertify.server;

import java.rmi.Remote;

import suncertify.db.DuplicateKeyException;
import suncertify.db.RecordNotFoundException;

public interface DataService extends Remote {

	String[] read(int recNo) throws RecordNotFoundException;

	void update(int recNo, String[] data) throws RecordNotFoundException;

	void delete(int recNo) throws RecordNotFoundException;

	int[] find(String[] criteria) throws RecordNotFoundException;

	int create(String[] data) throws DuplicateKeyException;

}