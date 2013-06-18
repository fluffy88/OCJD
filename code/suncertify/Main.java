package suncertify;

import suncertify.shared.Properties;

/**
 * This class is the entry point for the application. It is responsible for
 * ensuring the parameters passed on the command line are valid and creating a
 * new application based on it's start mode.
 * 
 * @author Sean Dunne
 */
public class Main {

	/**
	 * The entry point of the application.
	 * 
	 * @param args
	 *            Should be a single parameter describing the applications start
	 *            mode.
	 */
	public static void main(final String[] args) {
		checkIsSupportedParam(args);

		String mode = "";
		if (args.length != 0) {
			mode = args[0];
		}

		final Application app = ApplicationFactory.createApplication(mode);
		app.launch();
		setShutdownHook();
	}

	/**
	 * This method is responsible for ensuring the parameters passed to the
	 * application from the command line are valid.
	 * 
	 * @param args
	 *            The command line parameters.
	 */
	private static void checkIsSupportedParam(final String[] args) {
		if ((args.length == 0) || args[0].equalsIgnoreCase("server")
				|| args[0].equalsIgnoreCase("alone")) {
			// do nothing
		} else {
			printUsage();
		}
	}

	/**
	 * This method will display a usage message on the command line and exit
	 * with an error.
	 */
	private static void printUsage() {
		System.out.println("Usage: java -jar <path_and_filename> [mode]");
		System.out.println("Mode can be one of,");
		System.out.println("\\tserver:\\tTo start networked server.");
		System.out
				.println("\\alone:\\tTo start unnetworked standalone application.");
		System.out.println("\\No mode:\\tTo start networked client.");
		System.exit(1);
	}

	/**
	 * This method is a simple method to add a shutdown hook that is responsible
	 * for cleaning up before this application is ready to shutdown.
	 */
	private static void setShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			/**
			 * {@inheritDoc}
			 */
			@Override
			public void run() {
				Properties.save();
			}
		});
	}
}