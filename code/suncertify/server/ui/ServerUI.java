package suncertify.server.ui;

import javax.swing.JFrame;

public class ServerUI extends JFrame {

	private static final long serialVersionUID = 4825206061500231551L;

	public static ServerUI start() {
		return new ServerUI();
	}

	private ServerUI() {
		this.setTitle("CSR Server application");
		this.setSize(600, 100);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		initUIElements();
		this.setVisible(true);
	}

	private void initUIElements() {
		this.getContentPane().add(new ServerPage());
	}
}