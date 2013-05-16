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
import javax.swing.JPanel;
import javax.swing.JTextField;

import suncertify.shared.Preferences;

public class ServerPage extends JPanel {

	private static final long serialVersionUID = 5317757024984525594L;

	public ServerPage() {
		this.setLayout(new GridLayout(3, 1));

		createTopArea();
		createCentreArea();
		createBottomArea();
	}

	private void createTopArea() {
		JPanel top = new JPanel();
		FlowLayout topLayout = new FlowLayout();
		topLayout.setAlignment(FlowLayout.RIGHT);
		top.setLayout(topLayout);

		JLabel serverStatusLbl = new JLabel("Online");
		top.add(serverStatusLbl);
		this.add(top);
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

		JTextField dbFileLocTxt = new JTextField(Preferences.getInstance().get(DB_LOCATION));
		c = new GridBagConstraints();
		c.gridx = 1;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.insets = inset;
		middle.add(dbFileLocTxt, c);

		JButton browseBtn = new JButton("Locate");
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

		JButton startBtn = new JButton("Start");
		bottom.add(startBtn);

		JButton b = new JButton("Shutdown");
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		bottom.add(b);
		this.add(bottom);
	}
}