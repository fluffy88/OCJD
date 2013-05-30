package suncertify.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

import suncertify.shared.Contractor;

public interface RemoteObserver extends Remote {

	void update(Contractor contractor, String cmd) throws RemoteException;

}