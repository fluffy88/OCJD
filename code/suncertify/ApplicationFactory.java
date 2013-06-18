package suncertify;

/**
 * A factory class used to create an instance of {@link Application} based on the start mode of the application.
 * 
 * @author Sean Dunne
 */
public class ApplicationFactory {

	/**
	 * This is the factory method responsible for creating new instances of {@link Application}. Start mode can be one of
	 * <ul>
	 * <li>'server' - creates a networked {@link Server}</li>
	 * <li>'alone' - creates a {@link StandAlone} that completely bypasses networking</li>
	 * <li>'' - an empty string creates a networked {@link Client}</li>
	 * </ul>
	 * 
	 * @param mode
	 *            The start mode of the application entered on the command line.
	 * @return A new instance of {@link Application} based on start mode.
	 */
	public static Application createApplication(final String mode) {
		final String appMode = mode.toLowerCase();
		final Application app;

		switch (appMode) {
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

		return app;
	}
}