package suncertify;

public class ApplicationFactory {

	public Application createApplication(final String mode) {
		String appMode = mode.toLowerCase();
		Application app;

		switch (appMode) {
		case "alone":
			app = new StandAlone();
			break;

		case "server":
			app = new Server(AppType.Server);
			break;

		default: // client
			app = new Client(AppType.Client);
			break;
		}

		return app;
	}

}
