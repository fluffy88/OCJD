package suncertify;

public class StandAlone implements Application {

	@Override
	public void start() {
		Application server = new Server(AppType.StandAlone);

		new Client(AppType.StandAlone);
	}
}
