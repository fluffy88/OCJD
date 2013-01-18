package suncertify.db.io;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class DBParser {

	public void get() {
		try (DataInputStream is = new DataInputStream(new FileInputStream("db-2x2.db"))) {

			byte[] byteArr = new byte[4];
			
			is.read(byteArr);
			System.out.print("Magic Cookie Value: ");
			for(byte b : byteArr) {
				System.out.print(b);
			}
			System.out.println();
			
			is.read(byteArr);			
			System.out.print("Offset to Start of Record Zero: ");
			for(byte b : byteArr) {
				System.out.print(b);
			}
			System.out.println();
			
			byteArr = new byte[2];
			is.read(byteArr);			
			System.out.print("Number of Fields in Each Record: ");
			for(byte b : byteArr) {
				System.out.print(b);
			}
			System.out.println();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
