package suncertify.server.ui;

import static suncertify.db.ServerFactory.DB_LOCATION;

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
		JLabel dbFileLocLbl = new JLabel("Database file: ");
		JTextField dbFileLocTxt = new JTextField(Preferences.getInstance().get(DB_LOCATION));

		this.add(dbFileLocLbl);
		this.add(dbFileLocTxt);

		JButton b = new JButton("Shutdown");
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		this.add(b);
	}
}