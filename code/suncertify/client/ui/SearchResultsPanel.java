package suncertify.client.ui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import suncertify.shared.Injection;

public class SearchResultsPanel extends JPanel {

	private static final long serialVersionUID = -2713791197021056298L;

	private JTable table;

	public SearchResultsPanel() {
		this.setLayout(new BorderLayout());
		this.setBorder(new EmptyBorder(15, 10, 10, 10));

		this.createTableArea();
	}

	private void createTableArea() {
		final TableModel tableModel = new SearchResultsTableModel();
		Injection.instance.add("SearchResultsModel", tableModel);

		this.table = new JTable(tableModel);
		this.table.setFillsViewportHeight(true);
		this.table.setAutoCreateRowSorter(true);
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