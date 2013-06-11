package suncertify.client.ui;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ClientUI extends JFrame {

	private static final long serialVersionUID = 4914465385167890566L;

	public static ClientUI start() {
		return new ClientUI();
	}

	private ClientUI() {
		this.setTitle("CSR Application");
		this.setSize(900, 500);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		initUIElements();
		this.setVisible(true);
	}

	private void initUIElements() {
		final JPanel panel = new SearchPage();
		this.getContentPane().add(panel);
	}
}