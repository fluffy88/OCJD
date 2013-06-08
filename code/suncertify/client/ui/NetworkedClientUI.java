package suncertify.client.ui;

import static suncertify.shared.App.DEP_CLIENT_APPLICATION;
import static suncertify.shared.App.PROP_SERVER_HOSTNAME;

import java.awt.Color;
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

import suncertify.Server;
import suncertify.Startable;
import suncertify.server.DataService;
import suncertify.shared.App;
import suncertify.shared.Properties;

public class NetworkedClientUI extends JFrame {

	private static final long serialVersionUID = 6636073318499699241L;
	private JTextField textField;
	private JLabel status;
	private DataService dataService;
	private JButton ok;

	public static NetworkedClientUI start() {
		return new NetworkedClientUI();
	}

	private NetworkedClientUI() {
		this.setTitle("Server Hostname");
		this.setSize(300, 130);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final JPanel panel = this.getContentPanel();
		this.getContentPane().add(panel);
		this.setVisible(true);
	}

	private JPanel getContentPanel() {
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
		textField.addActionListener(new TextFieldActionListener());
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		panel.add(textField, c);

		status = new JLabel(" ");
		status.setForeground(new Color(255, 50, 50));
		c.gridy = 2;
		panel.add(status, c);

		ok = new JButton("OK");
		ok.addActionListener(new OKActionListener());
		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		panel.add(ok, c);

		final JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new CancelActionListener());
		c.gridx = 2;
		c.gridy = 3;
		panel.add(cancel, c);

		final JLabel blank = new JLabel("");
		c.gridx = 3;
		c.insets = new Insets(2, 25, 2, 25);
		panel.add(blank, c);

		final JLabel leftBlank = new JLabel("");
		c.gridx = 0;
		panel.add(leftBlank, c);

		return panel;
	}

	public DataService getDataService() {
		return dataService;
	}

	private class TextFieldActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			ok.doClick();
		}
	}

	private class OKActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			status.setText(" ");
			final String hostname = textField.getText();

			if (hostname.equals("")) {
				status.setText("You must enter a location for the server.");
			} else {
				try {
					final Registry registry = LocateRegistry.getRegistry(hostname);
					dataService = (DataService) registry.lookup(Server.RMI_SERVER);
					Properties.set(PROP_SERVER_HOSTNAME, hostname);

					Startable client = (Startable) App.getDependancy(DEP_CLIENT_APPLICATION);
					client.start();
				} catch (RemoteException e) {
					status.setText("Cannot connect to remote server.");
				} catch (NotBoundException e) {
					status.setText("Server found but cannot connect.");
				}
			}
		}
	}

	private class CancelActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
}