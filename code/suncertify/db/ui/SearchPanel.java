package suncertify.db.ui;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SearchPanel extends JPanel {

	private static final long serialVersionUID = 8203310258095403940L;

	public SearchPanel() {
		this.setLayout(new GridLayout(2, 7, 10, 1));
		this.setMaximumSize(new Dimension(800, 100));
		this.setBorder(BorderFactory.createTitledBorder("Search"));

		this.createSearchArea();
	}

	private void createSearchArea() {
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

		this.add(nameLabel);
		this.add(cityLabel);
		this.add(workLabel);
		this.add(staffLabel);
		this.add(chargeLabel);
		this.add(customerLabel);

		this.add(new JLabel());

		this.add(nameField);
		this.add(cityField);
		this.add(workField);
		this.add(staffField);
		this.add(chargeField);
		this.add(customerField);

		JButton button = new JButton("Search");
		this.add(button);
	}

}
