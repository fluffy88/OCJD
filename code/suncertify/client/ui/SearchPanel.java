package suncertify.client.ui;

import static suncertify.client.ui.SearchResultsTableModel.CITY;
import static suncertify.client.ui.SearchResultsTableModel.CONTRACTOR_NAME;
import static suncertify.client.ui.SearchResultsTableModel.CUSTOMER_ID;
import static suncertify.client.ui.SearchResultsTableModel.HOURLY_CHARGE;
import static suncertify.client.ui.SearchResultsTableModel.NUMBER_OF_STAFF;
import static suncertify.client.ui.SearchResultsTableModel.TYPES_OF_WORK;
import static suncertify.shared.App.DEP_DATASERVICE;
import static suncertify.shared.App.DEP_TABLE_MODEL;
import static suncertify.shared.App.PROP_EXACT_MATCH;

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

import suncertify.server.DataService;
import suncertify.shared.App;
import suncertify.shared.Contractor;
import suncertify.shared.Properties;

/**
 * This class contains all the components responsible for allowing the user to search for data records.
 * 
 * @author Sean Dunne
 */
public class SearchPanel extends JPanel {

	private static final long serialVersionUID = 8203310258095403940L;

	private JTextField nameField;
	private JTextField cityField;
	private JTextField workField;
	private JTextField staffField;
	private JTextField chargeField;
	private JTextField customerField;

	private JButton button;
	private JCheckBox exactMatch;

	/**
	 * Create a new JPanel containing the search components.
	 */
	public SearchPanel() {
		this.setLayout(new GridLayout(2, 7, 10, 1));
		this.setMaximumSize(new Dimension(800, 100));
		this.setBorder(BorderFactory.createTitledBorder("Search"));

		this.createSearchArea();
	}

	/**
	 * Create the search components and add them to this JPanel.
	 */
	private void createSearchArea() {
		final ActionListener enterAct = new EnterListener();
		final JLabel nameLabel = new JLabel(CONTRACTOR_NAME + ":");
		nameField = new JTextField(10);
		nameField.addActionListener(enterAct);
		final JLabel cityLabel = new JLabel(CITY + ":");
		cityField = new JTextField(10);
		cityField.addActionListener(enterAct);
		final JLabel workLabel = new JLabel(TYPES_OF_WORK + ":");
		workField = new JTextField(10);
		workField.addActionListener(enterAct);
		final JLabel staffLabel = new JLabel(NUMBER_OF_STAFF + ":");
		staffField = new JTextField(10);
		staffField.addActionListener(enterAct);
		final JLabel chargeLabel = new JLabel(HOURLY_CHARGE + ":");
		chargeField = new JTextField(10);
		chargeField.addActionListener(enterAct);
		final JLabel customerLabel = new JLabel(CUSTOMER_ID + ":");
		customerField = new JTextField(10);
		customerField.addActionListener(enterAct);

		boolean state = Properties.getBoolean(PROP_EXACT_MATCH, true);
		exactMatch = new JCheckBox("Exact match", state);
		exactMatch.addActionListener(new ExactMatchListener());

		button = new JButton("Search");
		button.addActionListener(new SearchListener());

		this.add(nameLabel);
		this.add(cityLabel);
		this.add(workLabel);
		this.add(staffLabel);
		this.add(chargeLabel);
		this.add(customerLabel);

		this.add(exactMatch);

		this.add(nameField);
		this.add(cityField);
		this.add(workField);
		this.add(staffField);
		this.add(chargeField);
		this.add(customerField);

		this.add(button);
	}

	/**
	 * This listener emulates the pressing of the Search button.
	 * 
	 * @author Sean Dunne
	 */
	private class EnterListener implements ActionListener {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			button.doClick();
		}
	}

	/**
	 * This listener persists the state of the exact match checkbox when it's state changes.
	 * 
	 * @author Sean Dunne
	 */
	private class ExactMatchListener implements ActionListener {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			Properties.setBoolean(PROP_EXACT_MATCH, exactMatch.isSelected());
		}
	}

	/**
	 * This listener does a search of the server and adds the results to the TableModel when the user clicks the Search button.
	 * 
	 * @author Sean Dunne
	 */
	private class SearchListener implements ActionListener {

		private final DataService dataService = (DataService) App.getDependancy(DEP_DATASERVICE);

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			final String[] searchCriteria = new String[6];
			searchCriteria[0] = nameField.getText().trim();
			searchCriteria[1] = cityField.getText().trim();
			searchCriteria[2] = workField.getText().trim();
			searchCriteria[3] = staffField.getText().trim();
			searchCriteria[4] = chargeField.getText().trim();
			searchCriteria[5] = customerField.getText().trim();
			boolean exactMatch = Properties.getBoolean(PROP_EXACT_MATCH);

			try {
				final SearchResultsTableModel tableModel = (SearchResultsTableModel) App.getDependancy(DEP_TABLE_MODEL);
				final List<Contractor> records = dataService.find(searchCriteria, exactMatch);

				tableModel.clearData();
				tableModel.addAll(records);
			} catch (RemoteException exp) {
				App.showErrorAndExit("Cannot connect to remote server.");
			}
		}
	}
}