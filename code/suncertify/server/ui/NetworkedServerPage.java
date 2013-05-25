package suncertify.server.ui;

import static suncertify.shared.App.DEP_SERVER_INSTANCE;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import suncertify.Server;
import suncertify.shared.App;

public class NetworkedServerPage extends ServerPage {

	private static final long serialVersionUID = 3452483301263152453L;
	private JButton startBtn;
	private JButton shutdownBtn;

	@Override
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
					Server server = (Server) App.getDependancy(DEP_SERVER_INSTANCE);
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