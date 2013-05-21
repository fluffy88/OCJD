package suncertify.client.ui;

import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import suncertify.db.RecordNotFoundException;
import suncertify.server.DataService;
import suncertify.shared.Contractor;
import suncertify.shared.App;

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
					final DataService dataService = (DataService) App.instance.getDependancy("DataService");
					final Contractor updatedData = data.get(row);

					try {
						dataService.update(updatedData);
					} catch (RemoteException | RecordNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
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
			final String[] record = contractor.toArray();
			record[col] = (String) value;
			this.data.set(row, new Contractor(contractor.getRecordId(), record));
			fireTableCellUpdated(row, col);
		}
	}

}