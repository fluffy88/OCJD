package suncertify;

import suncertify.db.io.DBParser;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DBParser parser = new DBParser();

		parser.get();
	}
}