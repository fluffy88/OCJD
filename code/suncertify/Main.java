package suncertify;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String mode = "";
		if (args.length != 0) {
			mode = args[0];
		}

		Application app = null;

		switch (mode) {
		case "alone":
			app = new StandAlone();
			break;

		case "server":
			app = new Server();
			break;

		default: // client
			app = new Client();
			break;
		}

		app.start();
	}
}