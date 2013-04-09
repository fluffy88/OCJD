package suncertify.db.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ServerPage extends JPanel {

	private static final long serialVersionUID = 5317757024984525594L;

	public ServerPage() {
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