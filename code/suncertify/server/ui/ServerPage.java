package suncertify.server.ui;

import static suncertify.db.DataAccessFactory.DB_LOCATION;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import suncertify.shared.Preferences;

public class ServerPage extends JPanel {

	private static final long serialVersionUID = 5317757024984525594L;

	public ServerPage() {
		this.setLayout(new GridBagLayout());
		Insets inset = new Insets(5, 5, 5, 5);

		JButton startBtn = new JButton("Start");
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.insets = inset;
		this.add(startBtn, c);

		JButton stopBtn = new JButton("Stop");
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.insets = inset;
		this.add(stopBtn, c);

		JLabel serverStatusLbl = new JLabel("Online");
		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 0;
		c.gridheight = 2;
		c.gridwidth = 2;
		c.insets = inset;
		this.add(serverStatusLbl, c);

		JLabel dbFileLocLbl = new JLabel("Database file: ");
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 2;
		c.insets = inset;
		this.add(dbFileLocLbl, c);

		JTextField dbFileLocTxt = new JTextField(Preferences.getInstance().get(DB_LOCATION));
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 2;
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 2;
		c.weightx = 1.0;
		c.insets = inset;
		this.add(dbFileLocTxt, c);

		JButton browseBtn = new JButton("Browse");
		c = new GridBagConstraints();
		c.gridx = 3;
		c.gridy = 2;
		c.insets = inset;
		this.add(browseBtn, c);

		JButton b = new JButton("Shutdown");
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		c = new GridBagConstraints();
		c.gridx = 3;
		c.gridy = 3;
		c.insets = inset;
		this.add(b, c);
	}
}