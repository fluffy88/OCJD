package suncertify.db.ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import suncertify.Injection;
import suncertify.db.DBMain;
import suncertify.db.RecordNotFoundException;

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
		button.addActionListener(new SearchButtonListener());
		this.add(button);
	}

	class SearchButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			DBMain dataService = (DBMain) Injection.instance.get("DataService");
			SearchResultsTableModel tableModel = (SearchResultsTableModel) Injection.instance.get("SearchResultsModel");
			tableModel.clearData();
			try {
				for (int i = 0; i < 5; i++) {
					String[] record = dataService.read(i);

					tableModel.add(record);
				}
				tableModel.fireTableStructureChanged();

			} catch (RecordNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}
}