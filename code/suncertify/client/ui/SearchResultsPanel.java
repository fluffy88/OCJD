package suncertify.client.ui;

import static suncertify.shared.App.DEP_DATASERVICE;
import static suncertify.shared.App.DEP_TABLE_MODEL;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
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

	private JButton bookBtn;

	private JButton unbookBtn;

	private JButton createBtn;

	private JButton editBtn;

	/**
	 * Create the components responsible for displaying the search results.
	 */
	public SearchResultsPanel() {
		this.setLayout(new BorderLayout());
		this.setBorder(new EmptyBorder(15, 10, 1, 10));

		this.createTableArea();
		this.createButtonArea();
		setButtonState();
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
		final FlowLayout lLayout = new FlowLayout();
		final JPanel lPanel = new JPanel(lLayout);
		lLayout.setAlignment(FlowLayout.LEFT);

		bookBtn = new JButton("Book");
		bookBtn.addActionListener(new BookListener());
		unbookBtn = new JButton("Cancel Booking");
		unbookBtn.addActionListener(new UnBookListener());
		lPanel.add(bookBtn);
		lPanel.add(unbookBtn);

		final FlowLayout rLayout = new FlowLayout();
		final JPanel rPanel = new JPanel(rLayout);
		rLayout.setAlignment(FlowLayout.RIGHT);

		createBtn = new JButton("Create");
		editBtn = new JButton("Edit");
		rPanel.add(createBtn);
		rPanel.add(editBtn);

		deleteBtn = new JButton("Delete (0)");
		deleteBtn.addActionListener(new DeleteListener());
		rPanel.add(deleteBtn);

		table.getSelectionModel().addListSelectionListener(new TableSelectionListener());
		table.getModel().addTableModelListener(new TableModelListener());

		final GridLayout bLayout = new GridLayout(1, 2);
		final JPanel bPanel = new JPanel(bLayout);
		bPanel.add(lPanel);
		bPanel.add(rPanel);
		this.add(bPanel, BorderLayout.SOUTH);
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

	private void setButtonState() {
		deleteBtn.setText("Delete (" + table.getSelectedRowCount() + ")");

		boolean isOneRowSelected = table.getSelectedRowCount() == 1;
		bookBtn.setEnabled(isOneRowSelected);
		unbookBtn.setEnabled(isOneRowSelected);
		createBtn.setEnabled(isOneRowSelected);
		editBtn.setEnabled(isOneRowSelected);

		final String custId = (String) table.getValueAt(table.getSelectedRow(), table.getColumnCount() - 1);
		if (custId != null && custId.isEmpty()) {
			unbookBtn.setEnabled(false);
		} else {
			bookBtn.setEnabled(false);
		}
	}

	/**
	 * This listener deletes the data records that are selected in the JTable from the server.
	 * 
	 * @author Sean Dunne
	 */
	private class DeleteListener implements ActionListener {

		private SearchResultsTableModel tableModel = (SearchResultsTableModel) App.getDependancy(DEP_TABLE_MODEL);
		private DataService dataService = (DataService) App.getDependancy(DEP_DATASERVICE);

		/**
		 * {@inheritDoc}
		 */
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
					App.showErrorAndExit("The remote server is no longer available.");
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
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void valueChanged(ListSelectionEvent e) {
			setButtonState();
		}
	}

	/**
	 * This listener sets the count of selected rows on the delete button.
	 * 
	 * @author Sean Dunne
	 */
	private class TableModelListener implements javax.swing.event.TableModelListener {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void tableChanged(TableModelEvent e) {
			setButtonState();
		}
	}

	private class BookListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			final String id = JOptionPane.showInputDialog("Enter Customer ID");
			if (id != null) {
				if (id.matches("^(\\d{8}|)$")) {
					table.setValueAt(id, table.getSelectedRow(), table.getColumnCount() - 1);
				} else {
					App.showError("The Customer ID must be 8 digits.");
				}
			}
		}
	}

	private class UnBookListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			table.setValueAt("", table.getSelectedRow(), table.getColumnCount() - 1);
		}
	}
}