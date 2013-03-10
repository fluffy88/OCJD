package suncertify;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		sanitiseParams(args);

		String mode = "";
		if (args.length != 0) {
			mode = args[0];
		}

		ApplicationFactory factory = new ApplicationFactory();
		Application app = factory.createApplication(mode);

		app.start();
	}

	private static void sanitiseParams(String[] args) {
		boolean tests = true;
		tests = tests && args.length > 1;
		tests = tests && args.length == 1;
		tests = tests && !args[0].equalsIgnoreCase("server");
		tests = tests && !args[0].equalsIgnoreCase("alone");

		if (tests) {
			printUsage();
		}
	}

	private static void printUsage() {
		System.out.println("Usage: java -jar <path_and_filename> [mode]");
		System.out.println("Mode can be one of,");
		System.out.println("\\tserver:\\tTo start networked server.");
		System.out.println("\\alone:\\tTo start unnetworked standalone application.");
		System.out.println("\\No mode:\\tTo start networked client.");
	}
}