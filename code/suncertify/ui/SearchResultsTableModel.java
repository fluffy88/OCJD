package suncertify.ui;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class SearchResultsTableModel extends AbstractTableModel implements TableModel {

	private static final long serialVersionUID = -6527725876159983929L;

	private final String[] columnNames = new String[] { "Subcontractor Name", "City", "Types of work", "Number of staff", "Hourly charge", "Customer holding this record" };

	private ArrayList<Object[]> data = new ArrayList<Object[]>();

	// { { "Joe Duffy", "Dublin", "Radio Presenter", "4", "€600", "" }, {
	// "Joe Duffy", "Dublin", "Radio Presenter", "4", "€600", "" },
	// { "Joe Duffy", "Dublin", "Radio Presenter", "4", "€600", "" }, {
	// "Joe Duffy", "Dublin", "Radio Presenter", "4", "€600", "" } };

	public SearchResultsTableModel() {
		data.add(new String[] { "", "", "", "", "", "" });
	}

	public void clearData() {
		data.clear();
	}

	public void add(String[] record) {
		data.add(record);
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
		return data.get(0).length;
		// }
		// return 0;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// if (data != null && rowIndex < data.size()) {
		return data.get(rowIndex)[columnIndex];
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
			data.get(row)[col] = value;
			fireTableCellUpdated(row, col);
		}
	}

}