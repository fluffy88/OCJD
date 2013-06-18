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
 * This class contains all the UI components that make up the server user
 * interface.
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
		final BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(layout);

		this.createDBLocationPanel();
		this.createServerButtons();
	}

	/**
	 * This method will create the ui to display and change the database
	 * location.
	 */
	private void createDBLocationPanel() {
		final JPanel middle = new JPanel();
		final GridBagLayout middleLayout = new GridBagLayout();
		middle.setLayout(middleLayout);

		final JLabel dbFileLocLbl = new JLabel("Database file:");
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.insets = new Insets(5, 5, 5, 5);
		middle.add(dbFileLocLbl, c);

		this.dbFileLocTxt = new JTextField(Properties.get(PROP_DB_LOCATION));
		this.dbFileLocTxt.setEditable(false);
		c = new GridBagConstraints();
		c.gridx = 1;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.insets = new Insets(5, 0, 5, 0);
		middle.add(this.dbFileLocTxt, c);

		this.browseBtn = new JButton("Locate");
		this.browseBtn.setMnemonic(KeyEvent.VK_L);
		this.browseBtn.addActionListener(new BrowseListener());
		c = new GridBagConstraints();
		c.gridx = 2;
		c.insets = new Insets(5, 5, 5, 5);
		middle.add(this.browseBtn, c);
		this.add(middle);
	}

	/**
	 * This method creates a servers buttons, start and stop.
	 */
	private void createServerButtons() {
		final JPanel bottom = new JPanel();
		final FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.RIGHT);
		bottom.setLayout(layout);

		this.startBtn = new JButton("Start");
		this.startBtn.setMnemonic(KeyEvent.VK_S);
		this.startBtn.addActionListener(new StartListener());
		bottom.add(this.startBtn);

		this.shutdownBtn = new JButton("Shutdown");
		this.shutdownBtn.setMnemonic(KeyEvent.VK_D);
		this.shutdownBtn.addActionListener(new ShutdownListener());
		bottom.add(this.shutdownBtn);
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
		public void actionPerformed(final ActionEvent arg0) {
			final String location = DatabaseLocator.getLocation();
			if (location != null) {
				ServerPage.this.dbFileLocTxt.setText(location);
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
		public void actionPerformed(final ActionEvent e) {
			if (ServerPage.this.dbFileLocTxt.getText().equals("")) {
				App.showError("You must choose a database file before the server can start.");
			} else {
				final Application server = (Application) App
						.getDependancy(DEP_APPLICATION);
				server.start();

				ServerPage.this.startBtn.setEnabled(false);
				ServerPage.this.browseBtn.setEnabled(false);
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
		public void actionPerformed(final ActionEvent e) {
			System.exit(0);
		}
	}
}