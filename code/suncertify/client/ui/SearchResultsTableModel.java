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

public class SearchResultsTableModel extends AbstractTableModel implements TableModel {

	private static final long serialVersionUID = -6527725876159983929L;

	private final DataService dataService = (DataService) App.getDependancy(DEP_DATASERVICE);
	private final ArrayList<Contractor> data = new ArrayList<Contractor>();
	private final ArrayList<String> columns = new ArrayList<>();

	static final String CONTRACTOR_NAME = "Name";
	static final String CITY = "City";
	static final String TYPES_OF_WORK = "Types of work";
	static final String NUMBER_OF_STAFF = "Number of staff";
	static final String HOURLY_CHARGE = "Hourly charge";
	static final String CUSTOMER_ID = "Customer ID";

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

	public void clearData() {
		this.data.clear();
		this.fireTableDataChanged();
	}

	public void add(final Contractor rec) {
		this.data.add(rec);
		int index = this.getContractorIndex(rec);
		this.fireTableRowsInserted(index, index);
	}

	public void addAll(final List<Contractor> records) {
		for (final Contractor rec : records) {
			this.data.add(rec);
		}
		this.fireTableDataChanged();
	}

	public void remove(final Contractor rec) {
		int index = this.getContractorIndex(rec);
		this.data.remove(index);
		this.fireTableRowsDeleted(index, index);
	}

	public void update(final Contractor rec) {
		int index = this.getContractorIndex(rec);
		this.data.set(index, rec);
		this.fireTableRowsUpdated(index, index);
	}

	public Contractor getContractorAt(int row) {
		return this.data.get(row);
	}

	private int getContractorIndex(final Contractor rec) {
		for (int index = 0; index < this.data.size(); index++) {
			Contractor c = this.data.get(index);
			if (c.getRecordId() == rec.getRecordId()) {
				return index;
			}
		}
		return -1;
	}

	@Override
	public String getColumnName(final int column) {
		return this.columns.get(column);
	}

	@Override
	public int getRowCount() {
		return this.data.size();
	}

	@Override
	public int getColumnCount() {
		return this.columns.size();
	}

	@Override
	public Object getValueAt(final int rowIndex, final int columnIndex) {
		return this.data.get(rowIndex).toArray()[columnIndex];
	}

	@Override
	public boolean isCellEditable(final int rowIndex, final int columnIndex) {
		return true;
	}

	@Override
	public void setValueAt(final Object value, final int row, final int column) {
		if (this.data != null && row < this.data.size()) {
			final Contractor contractor = this.data.get(row);
			final String updatedValue = (String) value;

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

	private boolean isValidValue(final String value, final int column) {
		if (column == this.columns.indexOf(CUSTOMER_ID) && !value.matches("^(\\d{8}|)$")) {
			App.showError("The Customer ID must be 8 digits.");
			return false;
		} else if (column != this.columns.indexOf(CUSTOMER_ID) && value.equals("")) {
			App.showError("You cannot leave the '" + this.columns.get(column) + "'" + " field empty.");
			return false;
		}

		return true;
	}
}