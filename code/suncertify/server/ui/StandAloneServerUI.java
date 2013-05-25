package suncertify.server.ui;

public class StandAloneServerUI extends ServerUI {

	private static final long serialVersionUID = 8041610137119212753L;

	@Override
	void initUIElements() {
		this.getContentPane().add(new StandAloneServerPage());
	}

}
