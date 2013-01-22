package suncertify.db.ui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableColumn;

public class SearchResultsPanel extends JPanel {

	private static final long serialVersionUID = -2713791197021056298L;

	private JTable table;

	public SearchResultsPanel() {
		this.setLayout(new BorderLayout());
		this.setBorder(new EmptyBorder(15, 10, 10, 10));

		this.createTableArea();
	}

	private void createTableArea() {
		this.table = new JTable(new SearchResultsTableModel());
		this.table.setFillsViewportHeight(true);
		this.setColumnWidth(3, 50);
		this.setColumnWidth(4, 50);
		this.setColumnWidth(5, 120);

		JScrollPane scrollPane = new JScrollPane(this.table);
		this.add(scrollPane, BorderLayout.CENTER);
	}

	private void setColumnWidth(int idx, int width) {
		TableColumn column = this.table.getColumnModel().getColumn(idx);
		column.setPreferredWidth(width);
	}
}
