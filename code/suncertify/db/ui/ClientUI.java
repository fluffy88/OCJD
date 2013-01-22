package suncertify.db.ui;

import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import suncertify.db.DBMain;

public class ClientUI extends JFrame {

	private static final long serialVersionUID = 4914465385167890566L;
	private final DBMain dataService;

	public ClientUI(DBMain data) {
		this.dataService = data;

		this.setTitle("Simple example");
		this.setSize(900, 500);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		initUIElements();
		this.setVisible(true);
	}

	private void initUIElements() {
		createMenuBar();

		BoxLayout layout = new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS);
		this.getContentPane().setLayout(layout);
		this.getContentPane().add(Box.createVerticalStrut(10));
		this.getContentPane().add(new SearchPanel());
		this.getContentPane().add(new SearchResultsPanel());
	}

	private void createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);

		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(fileMenu);

		JMenuItem exit = new JMenuItem("Exit");
		exit.setMnemonic(KeyEvent.VK_E);
		fileMenu.add(exit);
	}

}