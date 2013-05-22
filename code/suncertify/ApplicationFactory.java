package suncertify;

public class ApplicationFactory {

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