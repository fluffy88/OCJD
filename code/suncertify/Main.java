package suncertify;

import java.util.Arrays;

import suncertify.db.Data;
import suncertify.db.RecordNotFoundException;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Data data = Data.INSTANCE;

		try {
			String[] contractor = data.read(2);
			System.out.println(Arrays.toString(contractor));
		} catch (RecordNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}