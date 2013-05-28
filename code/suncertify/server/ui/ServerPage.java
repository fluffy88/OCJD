package suncertify.server.ui;

import static suncertify.shared.App.DEP_SERVER_INSTANCE;
import static suncertify.shared.App.PROP_DB_LOCATION;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import suncertify.Startable;
import suncertify.shared.App;
import suncertify.shared.Properties;

public class ServerPage extends JPanel {

	private static final long serialVersionUID = 5317757024984525594L;
	private JTextField dbFileLocTxt;
	private JButton browseBtn;
	private AbstractButton shutdownBtn;
	private JButton startBtn;

	public ServerPage() {
		this.setLayout(new GridLayout(2, 1));

		createDBLocationPanel();
		createServerButtons();
	}

	private void createDBLocationPanel() {
		JPanel middle = new JPanel();
		GridBagLayout middleLayout = new GridBagLayout();
		Insets inset = new Insets(5, 5, 5, 5);
		middle.setLayout(middleLayout);

		JLabel dbFileLocLbl = new JLabel("Database file: ");
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.insets = inset;
		middle.add(dbFileLocLbl, c);

		dbFileLocTxt = new JTextField(Properties.get(PROP_DB_LOCATION));
		dbFileLocTxt.setEditable(false);
		c = new GridBagConstraints();
		c.gridx = 1;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.insets = inset;
		middle.add(dbFileLocTxt, c);

		browseBtn = new JButton("Locate");
		browseBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String location = DatabaseLocator.getLocation();
				if (location != null) {
					dbFileLocTxt.setText(location);
				}
			}
		});
		c = new GridBagConstraints();
		c.gridx = 2;
		c.insets = inset;
		middle.add(browseBtn, c);
		this.add(middle);
	}

	void createServerButtons() {
		JPanel bottom = new JPanel();
		FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.RIGHT);
		bottom.setLayout(layout);

		startBtn = new JButton("Start");
		startBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (dbFileLocTxt.getText().equals("")) {
					App.showError("You must choose a database file first!");
				} else {
					Startable server = (Startable) App.getDependancy(DEP_SERVER_INSTANCE);
					server.start();

					startBtn.setEnabled(false);
					browseBtn.setEnabled(false);
				}
			}
		});
		bottom.add(startBtn);

		shutdownBtn = new JButton("Shutdown");
		shutdownBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		bottom.add(shutdownBtn);
		this.add(bottom);
	}
}