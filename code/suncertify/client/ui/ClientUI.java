package suncertify.client.ui;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * This class is responsible for creating, displaying and populating the Client user interface.
 * 
 * @author Sean Dunne
 */
public class ClientUI extends JFrame {

	private static final long serialVersionUID = 4914465385167890566L;

	/**
	 * This method is responsible for creating the ClientUI JFrame.
	 * 
	 * @return A reference to the ClientUI JFrame.
	 */
	public static ClientUI start() {
		return new ClientUI();
	}

	/**
	 * Creates the JFrame, sets it's properties, adds the contents and displays the JFrame.
	 */
	private ClientUI() {
		this.setTitle("CSR Application");
		this.setSize(900, 500);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		initUIElements();
		this.setVisible(true);
	}

	/**
	 * This method is responsible for adding the components of the ClientUI to the JFrame.
	 */
	private void initUIElements() {
		final JPanel panel = new SearchPage();
		this.getContentPane().add(panel);
	}
}