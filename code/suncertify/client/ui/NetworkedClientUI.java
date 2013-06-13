package suncertify.client.ui;

import static suncertify.shared.App.DEP_APPLICATION;
import static suncertify.shared.App.PROP_SERVER_HOSTNAME;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import suncertify.Client;
import suncertify.Server;
import suncertify.server.DataService;
import suncertify.shared.App;
import suncertify.shared.Properties;

/**
 * This class is responsible for creating, displaying and populating the networked client user interface. This UI's
 * purpose is to allow the user specify the hostname of the remote server to connect to.
 * 
 * @author Sean Dunne
 */
public class NetworkedClientUI extends JFrame {

	private static final long serialVersionUID = 6636073318499699241L;
	private JTextField textField;
	private DataService dataService;
	private JButton ok;

	/**
	 * This method is responsible for creating the NetworkedClientUI JFrame.
	 * 
	 * @return A reference to the NetworkedClientUI JFrame.
	 */
	public static NetworkedClientUI start() {
		return new NetworkedClientUI();
	}

	/**
	 * Creates the JFrame, sets it's properties, adds the contents and displays the JFrame.
	 */
	private NetworkedClientUI() {
		this.setTitle("Server Hostname");
		this.setSize(300, 130);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		initUIElements();
		this.setVisible(true);
	}

	/**
	 * This method is responsible for adding the components of the NetworkedClientUI to the JFrame.
	 */
	private void initUIElements() {
		final GridBagLayout layout = new GridBagLayout();
		final GridBagConstraints c = new GridBagConstraints();
		final JPanel panel = new JPanel(layout);

		final JLabel label = new JLabel("Enter server hostname");
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(2, 2, 2, 2);
		c.gridwidth = GridBagConstraints.REMAINDER;
		panel.add(label, c);

		textField = new JTextField(Properties.get(PROP_SERVER_HOSTNAME, "localhost"));
		textField.addActionListener(new TextFieldListener());
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		panel.add(textField, c);

		ok = new JButton("OK");
		ok.addActionListener(new OKListener());
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		panel.add(ok, c);

		final JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new CancelListener());
		c.gridx = 2;
		c.gridy = 2;
		panel.add(cancel, c);

		final JLabel blank = new JLabel("");
		c.gridx = 3;
		c.insets = new Insets(2, 25, 2, 25);
		panel.add(blank, c);

		final JLabel leftBlank = new JLabel("");
		c.gridx = 0;
		panel.add(leftBlank, c);

		this.getContentPane().add(panel);
	}

	/**
	 * Getter for the remote DataService which is created when the user clicks the OK button.
	 * 
	 * @return The remote DataService.
	 */
	public DataService getDataService() {
		return dataService;
	}

	/**
	 * A listener that emulates clicking the OK button when the user presses enter in the hostname text box.
	 * 
	 * @author Sean Dunne
	 */
	private class TextFieldListener implements ActionListener {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			ok.doClick();
		}
	}

	/**
	 * Listener to validate server hostname and to attempt to connect to the remote server once the user clicks the OK
	 * button.
	 * 
	 * @author Sean Dunne
	 */
	private class OKListener implements ActionListener {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void actionPerformed(ActionEvent event) {
			final String hostname = textField.getText();

			if (hostname.equals("")) {
				App.showError("You must enter a location for the server.");
			} else {
				try {
					final Registry registry = LocateRegistry.getRegistry(hostname);
					dataService = (DataService) registry.lookup(Server.RMI_SERVER);
					Properties.set(PROP_SERVER_HOSTNAME, hostname);

					Client client = (Client) App.getDependancy(DEP_APPLICATION);
					client.start();
				} catch (RemoteException e) {
					App.showError("Cannot connect to the remote server.\nCheck the hostname is correct.");
				} catch (NotBoundException e) {
					App.showError("Server found but cannot connect.\nThe server may not have started correctly.");
				}
			}
		}
	}

	/**
	 * Listener to exit the application when the user clicks the Cancel button.
	 * 
	 * @author Sean Dunne
	 */
	private class CancelListener implements ActionListener {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
}