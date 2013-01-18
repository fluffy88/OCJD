package suncertify;

import suncertify.db.io.DBParser;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		System.out.println("Hello");
		
		DBParser parser = new DBParser();
		
		parser.get();
	}
}