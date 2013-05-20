package suncertify.client.ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import suncertify.db.RecordNotFoundException;
import suncertify.server.DataService;
import suncertify.shared.Contractor;
import suncertify.shared.Injection;

public class SearchPanel extends JPanel {

	private static final long serialVersionUID = 8203310258095403940L;

	private JTextField nameField;
	private JTextField cityField;
	private JTextField workField;
	private JTextField staffField;
	private JTextField chargeField;
	private JTextField customerField;

	public SearchPanel() {
		this.setLayout(new GridLayout(2, 7, 10, 1));
		this.setMaximumSize(new Dimension(800, 100));
		this.setBorder(BorderFactory.createTitledBorder("Search"));

		this.createSearchArea();
	}

	private void createSearchArea() {
		JLabel nameLabel = new JLabel("Name:");
		nameField = new JTextField(10);
		JLabel cityLabel = new JLabel("City:");
		cityField = new JTextField(10);
		JLabel workLabel = new JLabel("Type of Work:");
		workField = new JTextField(10);
		JLabel staffLabel = new JLabel("Number of Staff:");
		staffField = new JTextField(10);
		JLabel chargeLabel = new JLabel("Hourly Charge:");
		chargeField = new JTextField(10);
		JLabel customerLabel = new JLabel("Customer:");
		customerField = new JTextField(10);

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
			DataService dataService = (DataService) Injection.instance.get("DataService");
			SearchResultsTableModel tableModel = (SearchResultsTableModel) Injection.instance.get("SearchResultsModel");
			tableModel.clearData();
			try {
				String[] searchCriteria = new String[6];
				searchCriteria[0] = nameField.getText().trim();
				searchCriteria[1] = cityField.getText().trim();
				searchCriteria[2] = workField.getText().trim();
				searchCriteria[3] = staffField.getText().trim();
				searchCriteria[4] = chargeField.getText().trim();
				searchCriteria[5] = customerField.getText().trim();

				List<Contractor> records = dataService.find(searchCriteria, true);

				for (Contractor rec : records) {
					tableModel.add(rec);
				}
				tableModel.fireTableDataChanged();

			} catch (RecordNotFoundException exp) {
				// TODO Auto-generated catch block
				exp.printStackTrace();
			} catch (ConnectException exp) {
				JOptionPane.showMessageDialog(null, "Cannot connect to server. \nApplication will exit!", "Error",
						JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			} catch (RemoteException exp) {
				// TODO Auto-generated catch block
				exp.printStackTrace();
			}
		}
	}
}