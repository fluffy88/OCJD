package suncertify.client.ui;

import static suncertify.shared.App.DATASERVICE;
import static suncertify.shared.App.TABLE_MODEL;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import suncertify.db.RecordNotFoundException;
import suncertify.server.DataService;
import suncertify.shared.App;
import suncertify.shared.Contractor;
import suncertify.shared.Preferences;

public class SearchPanel extends JPanel {

	public static final String EXACT_MATCH = "exact.match.enabled";

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
		final JLabel nameLabel = new JLabel("Name:");
		nameField = new JTextField(10);
		final JLabel cityLabel = new JLabel("City:");
		cityField = new JTextField(10);
		final JLabel workLabel = new JLabel("Type of Work:");
		workField = new JTextField(10);
		final JLabel staffLabel = new JLabel("Number of Staff:");
		staffField = new JTextField(10);
		final JLabel chargeLabel = new JLabel("Hourly Charge:");
		chargeField = new JTextField(10);
		final JLabel customerLabel = new JLabel("Customer:");
		customerField = new JTextField(10);

		this.add(nameLabel);
		this.add(cityLabel);
		this.add(workLabel);
		this.add(staffLabel);
		this.add(chargeLabel);
		this.add(customerLabel);

		boolean state = Preferences.getInstance().getBoolean(EXACT_MATCH, true);
		final JCheckBox exactMatch = new JCheckBox("Exact match", state);
		exactMatch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Preferences.getInstance().set(EXACT_MATCH, exactMatch.isSelected());
			}
		});
		this.add(exactMatch);

		this.add(nameField);
		this.add(cityField);
		this.add(workField);
		this.add(staffField);
		this.add(chargeField);
		this.add(customerField);

		final JButton button = new JButton("Search");
		button.addActionListener(new SearchButtonListener());
		this.add(button);
	}

	class SearchButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			final DataService dataService = (DataService) App.getDependancy(DATASERVICE);
			final SearchResultsTableModel tableModel = (SearchResultsTableModel) App.getDependancy(TABLE_MODEL);
			tableModel.clearData();
			try {
				final String[] searchCriteria = new String[6];
				searchCriteria[0] = nameField.getText().trim();
				searchCriteria[1] = cityField.getText().trim();
				searchCriteria[2] = workField.getText().trim();
				searchCriteria[3] = staffField.getText().trim();
				searchCriteria[4] = chargeField.getText().trim();
				searchCriteria[5] = customerField.getText().trim();

				boolean exactMatch = Preferences.getInstance().getBoolean(EXACT_MATCH);
				final List<Contractor> records = dataService.find(searchCriteria, exactMatch);

				for (final Contractor rec : records) {
					tableModel.add(rec);
				}
				tableModel.fireTableDataChanged();

			} catch (RecordNotFoundException exp) {
				App.showError(exp.getMessage());
			} catch (RemoteException exp) {
				App.showErrorAndExit("Cannot connect to remote server.");
			}
		}
	}
}