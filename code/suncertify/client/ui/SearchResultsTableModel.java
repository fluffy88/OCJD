package suncertify.client.ui;

import static suncertify.shared.App.DATASERVICE;

import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import suncertify.db.RecordNotFoundException;
import suncertify.server.DataService;
import suncertify.shared.App;
import suncertify.shared.Contractor;

public class SearchResultsTableModel extends AbstractTableModel implements TableModel {

	private static final long serialVersionUID = -6527725876159983929L;

	private final String[] columnNames = new String[] { "Subcontractor Name", "City", "Types of work", "Number of staff", "Hourly charge",
			"Customer holding this record" };

	private final ArrayList<Contractor> data = new ArrayList<Contractor>();

	public SearchResultsTableModel() {
		// add empty data so table headers show up and can be resized.
		this.data.add(new Contractor(0, null, null, null, null, null, null));

		this.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(final TableModelEvent e) {
				final int row = e.getFirstRow();

				if (row == e.getLastRow()) {
					final DataService dataService = (DataService) App.getDependancy(DATASERVICE);
					final Contractor updatedData = data.get(row);

					try {
						dataService.update(updatedData);
					} catch (RecordNotFoundException exp) {
						App.showError(exp.getMessage());
					} catch (RemoteException e1) {
						App.showErrorAndExit("Cannot connect to remote server.");
					}
				}
			}
		});
	}

	public void clearData() {
		this.data.clear();
	}

	public void add(final Contractor rec) {
		this.data.add(rec);
	}

	@Override
	public String getColumnName(final int column) {
		return this.columnNames[column];
	}

	@Override
	public int getRowCount() {
		// if (data != null) {
		return this.data.size();
		// }
		// return 0;
	}

	@Override
	public int getColumnCount() {
		// if (data != null && data.size() > 0) {
		return this.columnNames.length;
		// }
		// return 0;
	}

	@Override
	public Object getValueAt(final int rowIndex, final int columnIndex) {
		// if (data != null && rowIndex < data.size()) {
		return this.data.get(rowIndex).toArray()[columnIndex];
		// }

		// return "";
	}

	@Override
	public boolean isCellEditable(final int rowIndex, final int columnIndex) {
		return true;
	}

	@Override
	public void setValueAt(final Object value, final int row, final int col) {
		if (this.data != null && row < this.data.size()) {
			final Contractor contractor = this.data.get(row);
			String updatedValue = (String) value;

			if (col == columnNames.length - 1 && !updatedValue.matches("^(\\d{8}|)$")) {
				App.showError("The Customer ID must be 8 digits.");
				return;
			}

			hasRecordChanged(row);

			final String[] record = contractor.toArray();
			record[col] = updatedValue;

			this.data.set(row, new Contractor(contractor.getRecordId(), record));
			fireTableCellUpdated(row, col);
		}
	}

	private void hasRecordChanged(int row) {
		final Contractor contractor = this.data.get(row);
		try {
			final DataService dataService = (DataService) App.getDependancy(DATASERVICE);
			final Contractor cachedContractor = dataService.read(contractor.getRecordId());

			final String[] record = contractor.toArray();
			final String[] cachedRecord = cachedContractor.toArray();
			for (int i = 0; i < record.length; i++) {
				if (!record[i].equals(cachedRecord[i])) {
					this.data.set(row, cachedContractor);
					fireTableRowsUpdated(row, row);
					App.showError("The record value has changed on the server!\nThe table has been automatically updated with the latest values, please try again.");
					return;
				}
			}
		} catch (RecordNotFoundException exp) {
			App.showError(exp.getMessage());
		} catch (RemoteException exp) {
			App.showErrorAndExit("Cannot connect to remote server.");
		}
	}
}