package suncertify.client.ui;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 * This class contains all the client search components, including the search
 * boxes and the result table.
 * 
 * @author Sean Dunne
 */
public class SearchPage extends JPanel {

	private static final long serialVersionUID = -601627346143259905L;

	/**
	 * Create a new JPanel containing all the search components.
	 */
	public SearchPage() {
		final BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(layout);

		this.add(Box.createVerticalStrut(10));
		this.add(new SearchPanel());
		this.add(new SearchResultsPanel());
		this.add(new SearchResultsButtonPanel());
	}
}