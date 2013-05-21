package suncertify.client.ui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import suncertify.client.CurrencyComparator;
import suncertify.client.NumberComparator;
import suncertify.shared.App;

public class SearchResultsPanel extends JPanel {

	private static final long serialVersionUID = -2713791197021056298L;

	public static final String TABLE_MODEL = "search.results.tablemodel";

	private JTable table;

	public SearchResultsPanel() {
		this.setLayout(new BorderLayout());
		this.setBorder(new EmptyBorder(15, 10, 10, 10));

		this.createTableArea();
	}

	private void createTableArea() {
		final TableModel tableModel = new SearchResultsTableModel();
		App.publish(TABLE_MODEL, tableModel);

		this.table = new JTable(tableModel);
		this.table.setFillsViewportHeight(true);

		final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableModel);
		sorter.setComparator(3, new NumberComparator());
		sorter.setComparator(4, new CurrencyComparator());
		sorter.setComparator(5, new NumberComparator());
		this.table.setRowSorter(sorter);

		this.setColumnWidth(3, 20);
		this.setColumnWidth(4, 20);
		this.setColumnWidth(5, 120);

		final JScrollPane scrollPane = new JScrollPane(this.table);
		this.add(scrollPane, BorderLayout.CENTER);
	}

	private void setColumnWidth(final int idx, final int width) {
		// TODO when TableModel data is empty we get null here....
		final TableColumn column = this.table.getColumnModel().getColumn(idx);
		column.setPreferredWidth(width);
	}
}