package suncertify;

public class ApplicationFactory {

	private static ApplicationFactory instance;

	private ApplicationFactory() {
	}

	public static ApplicationFactory getInstance() {
		if (instance == null) {
			instance = new ApplicationFactory();
		}
		return instance;
	}

	public Application createApplication(final String mode) {
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