package suncertify.client.ui;

import static suncertify.shared.App.DEP_TABLE;
import static suncertify.shared.App.DEP_TABLE_MODEL;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import suncertify.shared.App;

/**
 * This class contains the components that display and enable manipulating of the search results.
 * 
 * @author Sean Dunne
 */
public class SearchResultsPanel extends JPanel {

	private static final long serialVersionUID = -2713791197021056298L;

	private JTable table;

	/**
	 * Create the components responsible for displaying the search results.
	 */
	public SearchResultsPanel() {
		this.setLayout(new BorderLayout());
		this.setBorder(new EmptyBorder(15, 10, 1, 10));

		this.add(this.createTableArea(), BorderLayout.CENTER);
		App.publish(DEP_TABLE, this.table);

		this.add(new SearchResultsButtonPanel(), BorderLayout.SOUTH);
	}

	/**
	 * This method creates a JTable to display the search results.
	 * 
	 * @return
	 */
	private JScrollPane createTableArea() {
		final SearchResultsTableModel tableModel = new SearchResultsTableModel();
		App.publish(DEP_TABLE_MODEL, tableModel);

		this.table = new JTable(tableModel);
		this.table.setFillsViewportHeight(true);
		this.setColumnWidth(3, 20);
		this.setColumnWidth(4, 20);
		this.setColumnWidth(5, 120);

		final JScrollPane scrollPane = new JScrollPane(this.table);
		this.add(scrollPane);

		return scrollPane;
	}

	/**
	 * This method resizes a column of the JTable.
	 * 
	 * @param idx
	 *            The column index to resize.
	 * @param width
	 *            The new width of the column.
	 */
	private void setColumnWidth(final int idx, final int width) {
		final TableColumnModel columnModel = this.table.getColumnModel();
		if (columnModel != null) {
			final TableColumn column = columnModel.getColumn(idx);
			column.setPreferredWidth(width);
		}
	}
}