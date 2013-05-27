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
	public void actionPerformed(ActionEvent arg0) {
		for (int i : this.table.getSelectedRows()) {
			Contractor contractor = this.tableModel.getContractorAt(i);

			try {
				this.dataService.delete(contractor);
			} catch (RemoteException | RecordNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}