package suncertify.db.ui;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ServerUI extends JFrame {

	private static final long serialVersionUID = 4825206061500231551L;

	public static void start() {
		new ServerUI();
	}

	private ServerUI() {
		this.setTitle("CSR Server application");
		this.setSize(200, 100);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		initUIElements();
		this.setVisible(true);
	}

	private void initUIElements() {
		final JPanel panel = new ServerPage();
		this.getContentPane().add(panel);
	}
}