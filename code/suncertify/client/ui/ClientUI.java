package suncertify.client.ui;

import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class ClientUI extends JFrame {

	private static final long serialVersionUID = 4914465385167890566L;

	public ClientUI() {
		this.setTitle("CSR Application");
		this.setSize(900, 500);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		initUIElements();
		this.setVisible(true);
	}

	private void initUIElements() {
		createMenuBar();

		final JPanel panel = new SearchPage();
		this.getContentPane().add(panel);
	}

	private void createMenuBar() {
		final JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);

		final JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(fileMenu);

		final JMenuItem exit = new JMenuItem("Exit");
		exit.setMnemonic(KeyEvent.VK_E);
		fileMenu.add(exit);
	}
}