package suncertify.db;

import java.util.Arrays;

public class ConcurrentWrites {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new ConcurrentWrites().start();
	}

	private void start() {
		new Thread(new WriterOne("first")).start();
		new Thread(new WriterOne("second")).start();
		new Thread(new WriterOne("third")).start();
		new Thread(new WriterOne("fourth")).start();
		new Thread(new WriterOne("fifth")).start();
		new Thread(new WriterOne("sixth")).start();
		new Thread(new WriterOne("seventh")).start();
		new Thread(new WriterOne("eight")).start();
		new Thread(new WriterOne("ninth")).start();
		new Thread(new WriterOne("tenth")).start();
		new Thread(new WriterOne("eleventh")).start();
	}

	class WriterOne implements Runnable {

		private final String name;

		public WriterOne(String string) {
			this.name = string;
		}

		@Override
		public void run() {
			Data dataService = new Data();

			for (int i = 0; i < 1000; i++) {

				int n = (int) (Math.random() * 100000);
				String[] data = new String[] { "WriterOne_" + n, "Somewhere nearby", "Testing", "5", "$0", "" };

				try {
					// int recNo = (int) (Math.random() * 26);
					// System.out.println("ConcurrentWrites: [" + name + "] - " + recNo + " - " + Arrays.toString(data));
					System.out.println("ConcurrentWrites: [" + name + "] - " + Arrays.toString(data));
					dataService.create(data);
					System.out.println();
				} catch (DuplicateKeyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

}
