package suncertify.server.ui;

import javax.swing.JFrame;

/**
 * This class is responsible for creating, displaying and populating the Server
 * user interface.
 * 
 * @author Sean Dunne
 */
public class ServerUI extends JFrame {

	private static final long serialVersionUID = 4825206061500231551L;

	/**
	 * This method is responsible for creating the ServerUI JFrame that contains
	 * the UI components.
	 * 
	 * @return A reference to the ServerUI JFrame.
	 */
	public static ServerUI start() {
		return new ServerUI();
	}

	/**
	 * Creates the JFrame, sets it's properties, adds the contents and displays
	 * the JFrame.
	 */
	private ServerUI() {
		this.setTitle("CSR Server application");
		this.setSize(600, 100);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.initUIElements();
		this.setVisible(true);
	}

	/**
	 * This method is responsible for adding the components of the ServerUI to
	 * the JFrame.
	 */
	private void initUIElements() {
		this.getContentPane().add(new ServerPage());
	}
}