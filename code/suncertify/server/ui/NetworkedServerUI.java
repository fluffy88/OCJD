package suncertify.server.ui;

public class NetworkedServerUI extends ServerUI {

	private static final long serialVersionUID = -3296709080437304273L;

	@Override
	void initUIElements() {
		this.getContentPane().add(new NetworkedServerPage());
	}

}
