package suncertify;

public class StandAlone implements Application {

	@Override
	public void start() {
		Application server = new Server(AppType.StandAlone);
		server.start();

		Client client = new Client(AppType.StandAlone);
		client.start();
	}
}
