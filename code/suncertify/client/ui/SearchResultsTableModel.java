package suncertify.client.ui;

import static suncertify.shared.App.DEP_DATASERVICE;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import suncertify.db.RecordNotFoundException;
import suncertify.server.DataService;
import suncertify.shared.App;
import suncertify.shared.Contractor;

/**
 * This class is responsible for maintaining the data records on the client UI.
 * 
 * @author Sean Dunne
 */
public class SearchResultsTableModel extends AbstractTableModel implements TableModel {

	private static final long serialVersionUID = -6527725876159983929L;

	private final DataService dataService = (DataService) App.getDependancy(DEP_DATASERVICE);
	private final ArrayList<Contractor> data = new ArrayList<Contractor>();
	private final ArrayList<String> columns = new ArrayList<>();

	/** Display name for the Contractor name */
	static final String CONTRACTOR_NAME = "Name";
	/** Display name for the Contractor location */
	static final String CITY = "City";
	/** Display name for the Contractor specialties */
	static final String TYPES_OF_WORK = "Types of work";
	/** Display name for the Contractors number of staff */
	static final String NUMBER_OF_STAFF = "Number of staff";
	/** Display name for the Contractors hourly rate */
	static final String HOURLY_CHARGE = "Hourly charge";
	/** Display name for the customer ID that holds this Contractor */
	static final String CUSTOMER_ID = "Customer ID";

	/**
	 * Create the table model.
	 */
	public SearchResultsTableModel() {
		columns.add(CONTRACTOR_NAME);
		columns.add(CITY);
		columns.add(TYPES_OF_WORK);
		columns.add(NUMBER_OF_STAFF);
		columns.add(HOURLY_CHARGE);
		columns.add(CUSTOMER_ID);
		// add empty data so table headers show up and can be resized.
		this.data.add(new Contractor(0, null, null, null, null, null, null));
	}

	/**
	 * Clear all records from the table model.
	 */
	public void clearData() {
		this.data.clear();
		this.fireTableDataChanged();
	}

	/**
	 * Add a new Contractor to the table model.
	 * 
	 * @param rec
	 *            The new Contractor to add.
	 */
	public void add(final Contractor rec) {
		this.data.add(rec);
		int index = this.getContractorIndex(rec);
		this.fireTableRowsInserted(index, index);
	}

	/**
	 * Add all the Contractors from a list to the table model
	 * 
	 * @param records
	 *            The list of Contractors to add.
	 */
	public void addAll(final List<Contractor> records) {
		for (final Contractor rec : records) {
			this.data.add(rec);
		}
		this.fireTableDataChanged();
	}

	/**
	 * Remove one Contractor from the table model.
	 * 
	 * @param rec
	 *            The Contractor to remove.
	 */
	public void remove(final Contractor rec) {
		int index = this.getContractorIndex(rec);
		this.data.remove(index);
		this.fireTableRowsDeleted(index, index);
	}

	/**
	 * Update a Contractor in the table model.
	 * 
	 * @param rec
	 *            The Contractor to update.
	 */
	public void update(final Contractor rec) {
		int index = this.getContractorIndex(rec);
		this.data.set(index, rec);
		this.fireTableRowsUpdated(index, index);
	}

	/**
	 * The the Contractor object of the specified row in the table.
	 * 
	 * @param row
	 *            The table row.
	 * @return The Contractor displayed at this row in the table.
	 */
	public Contractor getContractorAt(int row) {
		return this.data.get(row);
	}

	/**
	 * Method to get the row index of a Contractor object in the table.
	 * 
	 * @param rec
	 *            The Contractor to search for.
	 * @return The row index of this Contractor in the table.
	 */
	private int getContractorIndex(final Contractor rec) {
		for (int index = 0; index < this.data.size(); index++) {
			Contractor c = this.data.get(index);
			if (c.getRecordId() == rec.getRecordId()) {
				return index;
			}
		}
		return -1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getColumnName(final int column) {
		return this.columns.get(column);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getRowCount() {
		return this.data.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getColumnCount() {
		return this.columns.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getValueAt(final int rowIndex, final int columnIndex) {
		return this.data.get(rowIndex).toArray()[columnIndex];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isCellEditable(final int rowIndex, final int columnIndex) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValueAt(final Object value, final int row, final int column) {
		if (this.data != null && row < this.data.size()) {
			final Contractor contractor = this.data.get(row);
			final String updatedValue = ((String) value).trim();

			if (isValidValue(updatedValue, column)) {
				final String[] record = contractor.toArray();
				record[column] = updatedValue;
				final Contractor updatedContractor = new Contractor(contractor.getRecordId(), record);

				try {
					dataService.update(updatedContractor);
				} catch (RecordNotFoundException e) {
					App.showError(e.getMessage());
				} catch (RemoteException e) {
					App.showErrorAndExit("Cannot connect to remote server.");
				}
			}
		}
	}

	/**
	 * Determines if a value entered in a table cell is valid based on it's column.
	 * 
	 * @param value
	 *            The updated value to validate.
	 * @param column
	 *            The column at which the value was entered.
	 * @return true if this value is acceptable otherwise false.
	 */
	private boolean isValidValue(final String value, final int column) {
		if (column == this.columns.indexOf(CUSTOMER_ID) && !value.matches("^(\\d{8}|)$")) {
			App.showError("The Customer ID must be 8 digits.");
			return false;
		} else if (column == this.columns.indexOf(HOURLY_CHARGE) && !value.matches("^\\$\\d+(\\.\\d{1,2})?$")) {
			App.showError("You must enter a valid dollar amount.");
			return false;
		} else if (column == this.columns.indexOf(NUMBER_OF_STAFF) && !value.matches("^\\d+$")) {
			App.showError("You can only enter digits for the number of staff.");
			return false;
		} else if (column != this.columns.indexOf(CUSTOMER_ID) && value.equals("")) {
			App.showError("You cannot leave the '" + this.columns.get(column) + "'" + " field empty.");
			return false;
		}

		return true;
	}
}