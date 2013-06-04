package suncertify;

import suncertify.shared.Properties;

public class Main {

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		checkIsSupportedParam(args);

		String mode = "";
		if (args.length != 0) {
			mode = args[0];
		}

		final Application app = ApplicationFactory.getInstance().createApplication(mode);
		app.launch();
		setShutdownHook();
	}

	private static void checkIsSupportedParam(final String[] args) {
		if (args.length == 0 || args[0].equalsIgnoreCase("server") || args[0].equalsIgnoreCase("alone")) {
			// do nothing
		} else {
			printUsage();
		}
	}

	private static void printUsage() {
		System.out.println("Usage: java -jar <path_and_filename> [mode]");
		System.out.println("Mode can be one of,");
		System.out.println("\\tserver:\\tTo start networked server.");
		System.out.println("\\alone:\\tTo start unnetworked standalone application.");
		System.out.println("\\No mode:\\tTo start networked client.");
		System.exit(1);
	}

	private static void setShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				Properties.save();
			}
		});
	}
}