package suncertify.server.ui;

import javax.swing.JFrame;

public abstract class ServerUI extends JFrame {

	private static final long serialVersionUID = 4825206061500231551L;

	public void open() {
		this.setTitle("CSR Server application");
		this.setSize(600, 125);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		initUIElements();
		this.setVisible(true);
	}

	abstract void initUIElements();
}