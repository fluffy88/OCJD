package suncertify.server.ui;

import static suncertify.shared.App.STANDALONE_INSTANCE;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import suncertify.StandAlone;
import suncertify.shared.App;

public class StandAloneServerPage extends ServerPage {

	private static final long serialVersionUID = -8751629687918713643L;

	private JButton startBtn;

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
					StandAlone server = (StandAlone) App.getDependancy(STANDALONE_INSTANCE);
					server.init();

					StandAloneServerPage.this.getTopLevelAncestor().setVisible(false);
				}
			}
		});
		bottom.add(startBtn);
		this.add(bottom);
	}
}