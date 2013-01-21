package suncertify.db.ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 4914465385167890566L;

	public MainFrame() {
		this.setTitle("Simple example");
		this.setSize(600, 300);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		initUIElements();

		this.setVisible(true);
	}

	private void initUIElements() {
		createMenuBar();

		this.getContentPane().setLayout(new GridLayout(2, 1));
		createSearchArea();
		createTableArea();
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

	private void createSearchArea() {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(100, 100));
		panel.setBorder(BorderFactory.createEtchedBorder());
		this.getContentPane().add(panel);

		JLabel label = new JLabel("S'up World!!");
		panel.add(label);

		JTextField name = new JTextField(5);
		panel.add(name);

		JButton button = new JButton("Click me?");
		panel.add(button);
	}

	private void createTableArea() {
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEtchedBorder());
		this.getContentPane().add(panel);

		JLabel label = new JLabel("S'up World!!");
		panel.add(label);

		JTextField name = new JTextField(5);
		panel.add(name);

		JButton button = new JButton("Click me?");
		panel.add(button);
	}
}