package edu.wustl.cab2b.client.ui.pagination;

import java.util.EventListener;

import javax.swing.event.ListSelectionEvent;

/**
 * Page element selection event listener class.
 * @author chetan_bh
 */
public interface PageSelectionListener extends EventListener{
	/**
	   * Called whenever the value of the selection changes.
	   * @param e the event that characterizes the change.
	   */
	void selectionChanged(PageSelectionEvent e);
}
