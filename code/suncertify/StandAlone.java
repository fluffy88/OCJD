package suncertify;

import java.rmi.RemoteException;
import java.util.Arrays;

import suncertify.db.Data;
import suncertify.db.RecordNotFoundException;

public class StandAlone implements Application {

	@Override
	public void start() {
		Data data = Data.INSTANCE;

		try {
			System.out.println("StandAlone:");
			System.out.println(Arrays.toString(data.read(0)));
		} catch (RemoteException | RecordNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
