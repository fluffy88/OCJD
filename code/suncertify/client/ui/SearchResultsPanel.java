package suncertify.client.ui;

import static suncertify.shared.App.DEP_TABLE_MODEL;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import suncertify.client.CurrencyComparator;
import suncertify.client.NumberComparator;
import suncertify.shared.App;

public class SearchResultsPanel extends JPanel {

	private static final long serialVersionUID = -2713791197021056298L;

	private JTable table;

	public SearchResultsPanel() {
		this.setLayout(new BorderLayout());
		this.setBorder(new EmptyBorder(15, 10, 1, 10));

		this.createTableArea();
		this.createButtonArea();
	}

	private void createTableArea() {
		final SearchResultsTableModel tableModel = new SearchResultsTableModel();
		App.publish(DEP_TABLE_MODEL, tableModel);

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

	private void createButtonArea() {
		final JPanel bottomPanel = new JPanel();
		final FlowLayout layout = (FlowLayout) bottomPanel.getLayout();
		layout.setHgap(0);
		layout.setAlignment(FlowLayout.LEFT);
		final JButton deleteBtn = new JButton("Delete (0)");
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				deleteBtn.setText("Delete (" + table.getSelectedRowCount() + ")");
			}
		});
		deleteBtn.addActionListener(new DeleteActionListener(this.table));
		bottomPanel.add(deleteBtn);
		this.add(bottomPanel, BorderLayout.SOUTH);
	}

	private void setColumnWidth(final int idx, final int width) {
		final TableColumnModel columnModel = this.table.getColumnModel();
		if (columnModel != null) {
			final TableColumn column = columnModel.getColumn(idx);
			column.setPreferredWidth(width);
		}
	}
}