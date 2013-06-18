package suncertify.server.ui;

import static suncertify.shared.App.DEP_APPLICATION;
import static suncertify.shared.App.PROP_DB_LOCATION;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import suncertify.Application;
import suncertify.shared.App;
import suncertify.shared.Properties;

/**
 * This class contains all the UI components that make up the server user interface.
 * 
 * @author Sean Dunne
 */
public class ServerPage extends JPanel {

	private static final long serialVersionUID = 5317757024984525594L;
	private JTextField dbFileLocTxt;
	private JButton browseBtn;
	private AbstractButton shutdownBtn;
	private JButton startBtn;

	/**
	 * Create a Page containing the server ui components.
	 */
	public ServerPage() {
		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(layout);

		createDBLocationPanel();
		createServerButtons();
	}

	/**
	 * This method will create the ui to display and change the database location.
	 */
	private void createDBLocationPanel() {
		JPanel middle = new JPanel();
		GridBagLayout middleLayout = new GridBagLayout();
		middle.setLayout(middleLayout);

		JLabel dbFileLocLbl = new JLabel("Database file:");
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.insets = new Insets(5, 5, 5, 5);
		middle.add(dbFileLocLbl, c);

		dbFileLocTxt = new JTextField(Properties.get(PROP_DB_LOCATION));
		dbFileLocTxt.setEditable(false);
		c = new GridBagConstraints();
		c.gridx = 1;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.insets = new Insets(5, 0, 5, 0);
		middle.add(dbFileLocTxt, c);

		browseBtn = new JButton("Locate");
		browseBtn.setMnemonic(KeyEvent.VK_L);
		browseBtn.addActionListener(new BrowseListener());
		c = new GridBagConstraints();
		c.gridx = 2;
		c.insets = new Insets(5, 5, 5, 5);
		middle.add(browseBtn, c);
		this.add(middle);
	}

	/**
	 * This method creates a servers buttons, start and stop.
	 */
	private void createServerButtons() {
		JPanel bottom = new JPanel();
		FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.RIGHT);
		bottom.setLayout(layout);

		startBtn = new JButton("Start");
		startBtn.setMnemonic(KeyEvent.VK_S);
		startBtn.addActionListener(new StartListener());
		bottom.add(startBtn);

		shutdownBtn = new JButton("Shutdown");
		shutdownBtn.setMnemonic(KeyEvent.VK_D);
		shutdownBtn.addActionListener(new ShutdownListener());
		bottom.add(shutdownBtn);
		this.add(bottom);
	}

	/**
	 * This listener handles how to locate a new database file.
	 * 
	 * @author Sean Dunne
	 */
	private class BrowseListener implements ActionListener {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void actionPerformed(ActionEvent arg0) {
			String location = DatabaseLocator.getLocation();
			if (location != null) {
				dbFileLocTxt.setText(location);
			}
		}
	}

	/**
	 * This listener handles how to start the server.
	 * 
	 * @author Sean Dunne
	 */
	private class StartListener implements ActionListener {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			if (dbFileLocTxt.getText().equals("")) {
				App.showError("You must choose a database file before the server can start.");
			} else {
				Application server = (Application) App.getDependancy(DEP_APPLICATION);
				server.start();

				startBtn.setEnabled(false);
				browseBtn.setEnabled(false);
			}
		}
	}

	/**
	 * This listener will shutdown the application.
	 * 
	 * @author Sean Dunne
	 */
	private class ShutdownListener implements ActionListener {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
}