package suncertify.client.ui;

import static suncertify.shared.App.DEP_DATASERVICE;
import static suncertify.shared.App.DEP_TABLE_MODEL;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JTable;

import suncertify.db.RecordNotFoundException;
import suncertify.server.DataService;
import suncertify.shared.App;
import suncertify.shared.Contractor;

public class DeleteActionListener implements ActionListener {

	private final JTable table;
	private final SearchResultsTableModel tableModel;
	private final DataService dataService;

	public DeleteActionListener(JTable table) {
		this.table = table;
		this.tableModel = (SearchResultsTableModel) App.getDependancy(DEP_TABLE_MODEL);
		this.dataService = (DataService) App.getDependancy(DEP_DATASERVICE);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		int[] rows = this.table.getSelectedRows();
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