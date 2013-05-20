package suncertify.server.ui;

import static suncertify.db.DataAccessFactory.DB_LOCATION;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import suncertify.Server;
import suncertify.db.ui.DatabaseLocator;
import suncertify.shared.Injection;
import suncertify.shared.Preferences;

public class ServerPage extends JPanel {

	private static final long serialVersionUID = 5317757024984525594L;
	private JButton startBtn;
	private JButton shutdownBtn;
	private JTextField dbFileLocTxt;
	private JButton browseBtn;

	public ServerPage() {
		this.setLayout(new GridLayout(2, 1));

		createCentreArea();
		createBottomArea();
	}

	private void createCentreArea() {
		JPanel middle = new JPanel();
		GridBagLayout middleLayout = new GridBagLayout();
		Insets inset = new Insets(5, 5, 5, 5);
		middle.setLayout(middleLayout);

		JLabel dbFileLocLbl = new JLabel("Database file: ");
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.insets = inset;
		middle.add(dbFileLocLbl, c);

		dbFileLocTxt = new JTextField(Preferences.getInstance().get(DB_LOCATION));
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

	private void createBottomArea() {
		JPanel bottom = new JPanel();
		FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.RIGHT);
		bottom.setLayout(layout);

		startBtn = new JButton("Start");
		startBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (dbFileLocTxt.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "You must choose a database file first!", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					Server server = (Server) Injection.instance.get("server.instance");
					server.init();

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