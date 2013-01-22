package suncertify.db.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 4914465385167890566L;

	public MainFrame() {
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
		JPanel panel = new JPanel(new GridLayout(2, 7, 10, 1));
		panel.setMaximumSize(new Dimension(800, 100));
		panel.setBorder(BorderFactory.createTitledBorder("Search"));
		this.getContentPane().add(panel);

		JLabel nameLabel = new JLabel("Name:");
		JTextField nameField = new JTextField(10);
		JLabel cityLabel = new JLabel("City:");
		JTextField cityField = new JTextField(10);
		JLabel workLabel = new JLabel("Type of Work:");
		JTextField workField = new JTextField(10);
		JLabel staffLabel = new JLabel("Number of Staff:");
		JTextField staffField = new JTextField(10);
		JLabel chargeLabel = new JLabel("Hourly Charge:");
		JTextField chargeField = new JTextField(10);
		JLabel customerLabel = new JLabel("Customer:");
		JTextField customerField = new JTextField(10);

		panel.add(nameLabel);
		panel.add(cityLabel);
		panel.add(workLabel);
		panel.add(staffLabel);
		panel.add(chargeLabel);
		panel.add(customerLabel);

		panel.add(new JLabel());

		panel.add(nameField);
		panel.add(cityField);
		panel.add(workField);
		panel.add(staffField);
		panel.add(chargeField);
		panel.add(customerField);

		JButton button = new JButton("Search");
		panel.add(button);
	}

	private void createTableArea() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new EmptyBorder(15, 10, 10, 10));
		this.getContentPane().add(panel);

		String[] columnNames = new String[] { "Subcontractor Name", "City", "Types of work", "Number of staff", "Hourly charge", "Customer holding this record" };
		Object[][] fakeData = new Object[][] { { "Joe Duffy", "Dublin", "Radio Presenter", "4", "€600", "" }, { "Joe Duffy", "Dublin", "Radio Presenter", "4", "€600", "" },
				{ "Joe Duffy", "Dublin", "Radio Presenter", "4", "€600", "" }, { "Joe Duffy", "Dublin", "Radio Presenter", "4", "€600", "" } };

		JTable table = new JTable(fakeData, columnNames);
		table.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(table);
		panel.add(scrollPane, BorderLayout.CENTER);
	}
}