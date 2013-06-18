package suncertify.client.ui;

import static suncertify.shared.App.DEP_DATASERVICE;
import static suncertify.shared.App.DEP_TABLE;
import static suncertify.shared.App.DEP_TABLE_MODEL;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;

import suncertify.db.RecordNotFoundException;
import suncertify.server.DataService;
import suncertify.shared.App;
import suncertify.shared.Contractor;

/**
 * This class is responsible for creating the buttons used to interact with the
 * search results displayed in the JTable.
 * 
 * @author Sean Dunne
 */
public class SearchResultsButtonPanel extends JPanel {

	private static final long serialVersionUID = 4889568662774126914L;

	private JButton deleteBtn;
	private JButton bookBtn;
	private JButton unbookBtn;

	private final JTable table = (JTable) App.getDependancy(DEP_TABLE);

	/**
	 * Create a JPanel with the buttons for interacting with the search results.
	 */
	public SearchResultsButtonPanel() {
		final GridLayout layout = new GridLayout(1, 2);
		this.setLayout(layout);
		this.add(this.createBookButtonArea());
		this.add(this.createCRUDButtonArea());

		this.table.getSelectionModel().addListSelectionListener(
				new TableSelectionListener());
		this.table.getModel().addTableModelListener(new TableModelListener());

		this.setButtonState();
	}

	/**
	 * This method creates a JPanel containing the booking and un-booking
	 * buttons.
	 * 
	 * @return A JPanel containing the booking and un-booking buttons.
	 */
	private JPanel createBookButtonArea() {
		final FlowLayout layout = new FlowLayout();
		final JPanel lPanel = new JPanel(layout);
		layout.setAlignment(FlowLayout.LEFT);

		this.bookBtn = new JButton("Book");
		this.bookBtn.setMnemonic(KeyEvent.VK_B);
		this.bookBtn.addActionListener(new BookListener());
		this.unbookBtn = new JButton("Remove Booking");
		this.unbookBtn.setMnemonic(KeyEvent.VK_R);
		this.unbookBtn.addActionListener(new UnBookListener());
		lPanel.add(this.bookBtn);
		lPanel.add(this.unbookBtn);

		return lPanel;
	}

	/**
	 * This method creates a JPanel containing the buttons to interact with the
	 * Contractors displayed in the search results JTable.
	 * 
	 * @return A JPanel with buttons allowing the user interact with search
	 *         results.
	 */
	private JPanel createCRUDButtonArea() {
		final FlowLayout layout = new FlowLayout();
		final JPanel rPanel = new JPanel(layout);
		layout.setAlignment(FlowLayout.RIGHT);

		this.deleteBtn = new JButton("Delete (0)");
		this.deleteBtn.setMnemonic(KeyEvent.VK_D);
		this.deleteBtn.addActionListener(new DeleteListener());
		rPanel.add(this.deleteBtn);

		return rPanel;
	}

	/**
	 * This method is responsible for setting the state of the buttons based on
	 * the selection in the JTable.
	 */
	private void setButtonState() {
		final String deleteText = String.format("Delete (%s)",
				this.table.getSelectedRowCount());
		this.deleteBtn.setText(deleteText);

		final String custId = (String) this.table.getValueAt(
				this.table.getSelectedRow(), this.table.getColumnCount() - 1);

		final boolean isOneRowSelected = this.table.getSelectedRowCount() == 1;
		final boolean isRecordFree = (custId != null) && custId.isEmpty();

		this.bookBtn.setEnabled(isOneRowSelected && isRecordFree);
		this.unbookBtn.setEnabled(isOneRowSelected && !isRecordFree);
	}

	/**
	 * This listener books the selected Contractor on the JTable.
	 * 
	 * @author Sean Dunne
	 */
	private class BookListener implements ActionListener {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void actionPerformed(final ActionEvent e) {
			final String id = JOptionPane.showInputDialog(null,
					"Enter Customer ID", "Book Contractor",
					JOptionPane.QUESTION_MESSAGE);
			if (id != null) {
				if (id.matches("^\\d{8}$")) {
					final int row = SearchResultsButtonPanel.this.table
							.getSelectedRow();
					final int col = SearchResultsButtonPanel.this.table
							.getColumnCount() - 1;

					SearchResultsButtonPanel.this.table
							.setValueAt(id, row, col);
					SearchResultsButtonPanel.this.table.requestFocus();
				} else {
					App.showError("The Customer ID must be 8 digits.");
				}
			}
		}
	}

	/**
	 * This listener removes the booking on the selected Contractor in the
	 * JTable.
	 * 
	 * @author Sean Dunne
	 */
	private class UnBookListener implements ActionListener {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void actionPerformed(final ActionEvent e) {
			SearchResultsButtonPanel.this.table.setValueAt("",
					SearchResultsButtonPanel.this.table.getSelectedRow(),
					SearchResultsButtonPanel.this.table.getColumnCount() - 1);
			SearchResultsButtonPanel.this.table.requestFocus();
		}
	}

	/**
	 * This listener deletes the data records that are selected in the JTable
	 * from the server.
	 * 
	 * @author Sean Dunne
	 */
	private class DeleteListener implements ActionListener {

		private final SearchResultsTableModel tableModel = (SearchResultsTableModel) App
				.getDependancy(DEP_TABLE_MODEL);
		private final DataService dataService = (DataService) App
				.getDependancy(DEP_DATASERVICE);

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void actionPerformed(final ActionEvent event) {
			final ArrayList<Contractor> deletedContractors = new ArrayList<Contractor>();
			final int[] selectedRows = SearchResultsButtonPanel.this.table
					.getSelectedRows();

			for (final int i : selectedRows) {
				deletedContractors.add(this.tableModel.getContractorAt(i));
			}

			for (final Contractor contractor : deletedContractors) {
				try {
					this.dataService.delete(contractor);
				} catch (final RecordNotFoundException e) {
					App.showError(e.getMessage());
				} catch (final RemoteException e) {
					App.showErrorAndExit("The remote server is no longer available.");
				}
			}
		}
	}

	/**
	 * This listener sets the state of the buttons when the user changes the
	 * selection on the JTable.
	 * 
	 * @author Sean Dunne
	 */
	private class TableSelectionListener implements ListSelectionListener {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void valueChanged(final ListSelectionEvent e) {
			SearchResultsButtonPanel.this.setButtonState();
		}
	}

	/**
	 * This listener sets the state of the buttons when the TableModel data
	 * changes.
	 * 
	 * @author Sean Dunne
	 */
	private class TableModelListener implements
			javax.swing.event.TableModelListener {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void tableChanged(final TableModelEvent e) {
			SearchResultsButtonPanel.this.setButtonState();
		}
	}
}