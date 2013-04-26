package suncertify;

public class StandAlone implements Application {

	@Override
	public void start() {
		final Application server = new Server(AppType.StandAlone);
		server.start();

		final Client client = new Client(AppType.StandAlone);
		client.start();
	}
}