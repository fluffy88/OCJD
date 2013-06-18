package suncertify.client.ui;

import static suncertify.shared.App.DEP_DATASERVICE;
import static suncertify.shared.App.PROP_EXACT_MATCH;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import suncertify.db.RecordNotFoundException;
import suncertify.server.DataService;
import suncertify.shared.App;
import suncertify.shared.Contractor;
import suncertify.shared.Properties;

/**
 * This class is responsible for maintaining the data records on the client UI.
 * 
 * @author Sean Dunne
 */
public class SearchResultsTableModel extends AbstractTableModel implements
		TableModel {

	private static final long serialVersionUID = -6527725876159983929L;

	private final DataService dataService = (DataService) App
			.getDependancy(DEP_DATASERVICE);
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
	 * Create and populate the table model.
	 */
	public SearchResultsTableModel() {
		this.columns.add(CONTRACTOR_NAME);
		this.columns.add(CITY);
		this.columns.add(TYPES_OF_WORK);
		this.columns.add(NUMBER_OF_STAFF);
		this.columns.add(HOURLY_CHARGE);
		this.columns.add(CUSTOMER_ID);

		try {
			final List<Contractor> contractors = this.dataService.find(
					new String[1], Properties.getBoolean(PROP_EXACT_MATCH));
			this.addAll(contractors);
		} catch (final RemoteException e) {
			// could not get records, add empty data so table headers show up
			// and can be resized.
			this.data
					.add(new Contractor(0, null, null, null, null, null, null));
		}
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
		final int index = this.getContractorIndex(rec);
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
		final int index = this.getContractorIndex(rec);
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
		final int index = this.getContractorIndex(rec);
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
	public Contractor getContractorAt(final int row) {
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
			final Contractor c = this.data.get(index);
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
		if ((rowIndex >= 0) && (rowIndex < this.data.size())) {
			return this.data.get(rowIndex).toArray()[columnIndex];
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValueAt(final Object value, final int row, final int column) {
		if ((this.data != null) && (row < this.data.size())) {
			final Contractor contractor = this.data.get(row);
			final String updatedValue = ((String) value).trim();

			final String[] record = contractor.toArray();
			record[column] = updatedValue;
			final Contractor updatedContractor = new Contractor(
					contractor.getRecordId(), record);

			try {
				this.dataService.update(updatedContractor);
			} catch (final RecordNotFoundException e) {
				App.showError(e.getMessage());
			} catch (final RemoteException e) {
				App.showErrorAndExit("The remote server is no longer available.");
			}
		}
	}
}