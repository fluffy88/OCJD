package suncertify.db.ui;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class SearchResultsTableModel extends AbstractTableModel implements TableModel {

	private static final long serialVersionUID = -6527725876159983929L;

	private final String[] columnNames = new String[] { "Subcontractor Name", "City", "Types of work", "Number of staff", "Hourly charge", "Customer holding this record" };

	private Object[][] data = new Object[][] { { "Joe Duffy", "Dublin", "Radio Presenter", "4", "€600", "" }, { "Joe Duffy", "Dublin", "Radio Presenter", "4", "€600", "" },
			{ "Joe Duffy", "Dublin", "Radio Presenter", "4", "€600", "" }, { "Joe Duffy", "Dublin", "Radio Presenter", "4", "€600", "" } };

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	@Override
	public int getRowCount() {
		if (data != null) {
			return data.length;
		}
		return 0;
	}

	@Override
	public int getColumnCount() {
		if (data != null && data.length > 0) {
			return data[0].length;
		}
		return 0;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (data != null && rowIndex < data.length) {
			return data[rowIndex][columnIndex];
		}

		return "";
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		data[row][col] = value;
		fireTableCellUpdated(row, col);
	}

}
