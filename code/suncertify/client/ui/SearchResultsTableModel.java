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
import suncertify.shared.Injection;

public class SearchResultsTableModel extends AbstractTableModel implements TableModel {

	private static final long serialVersionUID = -6527725876159983929L;

	private final String[] columnNames = new String[] { "Subcontractor Name", "City", "Types of work", "Number of staff", "Hourly charge",
			"Customer holding this record" };

	private ArrayList<Contractor> data = new ArrayList<Contractor>();

	public SearchResultsTableModel() {
		// add empty data so table headers show up and can be resized.
		data.add(new Contractor(0, null, null, null, null, null, null));

		this.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				int row = e.getFirstRow();

				if (row == e.getLastRow()) {
					DataService dataService = (DataService) Injection.instance.get("DataService");
					Contractor updatedData = data.get(row);

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
		data.clear();
	}

	public void add(Contractor rec) {
		data.add(rec);
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	@Override
	public int getRowCount() {
		// if (data != null) {
		return data.size();
		// }
		// return 0;
	}

	@Override
	public int getColumnCount() {
		// if (data != null && data.size() > 0) {
		return columnNames.length;
		// }
		// return 0;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// if (data != null && rowIndex < data.size()) {
		return data.get(rowIndex).toArray()[columnIndex];
		// }

		// return "";
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		if (data != null && row < data.size()) {
			Contractor contractor = data.get(row);
			String[] record = contractor.toArray();
			record[col] = (String) value;
			data.set(row, new Contractor(contractor.getRecordId(), record));
			fireTableCellUpdated(row, col);
		}
	}

}