package suncertify.server.ui;

import static suncertify.db.DataAccessFactory.DB_LOCATION;

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

import suncertify.db.ui.DatabaseLocator;
import suncertify.shared.Preferences;

public abstract class ServerPage extends JPanel {

	private static final long serialVersionUID = 5317757024984525594L;
	JTextField dbFileLocTxt;
	JButton browseBtn;

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

	abstract void createServerButtons();
}