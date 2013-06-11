package suncertify.client.ui;

import static suncertify.shared.App.DEP_DATASERVICE;
import static suncertify.shared.App.DEP_TABLE_MODEL;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import suncertify.db.RecordNotFoundException;
import suncertify.server.DataService;
import suncertify.shared.App;
import suncertify.shared.Contractor;

/**
 * This class contains the components that display and enable manipulating of the search results.
 * 
 * @author Sean Dunne
 */
public class SearchResultsPanel extends JPanel {

	private static final long serialVersionUID = -2713791197021056298L;

	private JTable table;
	private JButton deleteBtn;

	/**
	 * Create the components responsible for displaying the search results.
	 */
	public SearchResultsPanel() {
		this.setLayout(new BorderLayout());
		this.setBorder(new EmptyBorder(15, 10, 1, 10));

		this.createTableArea();
		this.createButtonArea();
	}

	/**
	 * This method creates a JTable to display the search results.
	 */
	private void createTableArea() {
		final SearchResultsTableModel tableModel = new SearchResultsTableModel();
		App.publish(DEP_TABLE_MODEL, tableModel);

		this.table = new JTable(tableModel);
		this.table.setFillsViewportHeight(true);
		this.setColumnWidth(3, 20);
		this.setColumnWidth(4, 20);
		this.setColumnWidth(5, 120);

		final JScrollPane scrollPane = new JScrollPane(this.table);
		this.add(scrollPane, BorderLayout.CENTER);
	}

	/**
	 * This method creates the buttons to manipulate the search results.
	 */
	private void createButtonArea() {
		final JPanel bottomPanel = new JPanel();
		final FlowLayout layout = (FlowLayout) bottomPanel.getLayout();
		layout.setHgap(0);
		layout.setAlignment(FlowLayout.LEFT);

		deleteBtn = new JButton("Delete (0)");
		deleteBtn.addActionListener(new DeleteActionListener());
		table.getSelectionModel().addListSelectionListener(new TableSelectionListener());

		bottomPanel.add(deleteBtn);
		this.add(bottomPanel, BorderLayout.SOUTH);
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

	/**
	 * This listener deletes the data records that are selected in the JTable from the server.
	 * 
	 * @author Sean Dunne
	 */
	private class DeleteActionListener implements ActionListener {

		private SearchResultsTableModel tableModel = (SearchResultsTableModel) App.getDependancy(DEP_TABLE_MODEL);
		private DataService dataService = (DataService) App.getDependancy(DEP_DATASERVICE);

		@Override
		public void actionPerformed(ActionEvent event) {
			int[] rows = SearchResultsPanel.this.table.getSelectedRows();
			for (int i = 0; i < rows.length; i++) {
				int selectedRow = rows[i] - i;
				Contractor contractor = this.tableModel.getContractorAt(selectedRow);

				try {
					this.dataService.delete(contractor);
				} catch (RecordNotFoundException e) {
					App.showError(e.getMessage());
				} catch (RemoteException e) {
					App.showErrorAndExit("Cannot connect to remote server.");
				}
			}
		}
	}

	/**
	 * This listener sets the count of selected rows on the delete button.
	 * 
	 * @author Sean Dunne
	 */
	private class TableSelectionListener implements ListSelectionListener {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			deleteBtn.setText("Delete (" + table.getSelectedRowCount() + ")");
		}
	}
}