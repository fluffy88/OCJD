package suncertify.ui;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class SearchPage extends JPanel {

	private static final long serialVersionUID = -601627346143259905L;

	public SearchPage() {
		final BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(layout);

		this.add(Box.createVerticalStrut(10));
		this.add(new SearchPanel());
		this.add(new SearchResultsPanel());
	}
}