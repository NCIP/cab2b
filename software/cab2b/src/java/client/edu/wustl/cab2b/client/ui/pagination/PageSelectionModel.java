/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.pagination;

import java.util.Vector;

import javax.swing.event.EventListenerList;

/**
 * PageSelctionModel to work in the ListSelectionModel way, but this selection model
 * has to take into account that selected elements need two indexing.
 * 
 * 1) There will be many page, so first index the page. and
 * 2) In the indexed page index the element which was selected.
 * 
 * @author chetan_bh
 */
public interface PageSelectionModel {
	
	static int SINGLE_SELECTION = 1;
	
	static int MULTIPLE_SELECTION = 2;
	
	public boolean isSelectedIndex(String pageIndex, int index);
	
	public boolean 	isSelectionEmpty();
	
	public int 	getSelectionMode();
	
	public void	setSelectionMode(int selectionMode); 
	
	//public void addPageSelectionListener(PageSelectionListener x);
	
	//public void removePageSelectionListener(PageSelectionListener x); 
	
	//public EventListenerList getPageSelectionListenerList();
	
	public int getSelectionCount();
	
	public void clearPage(String pageIndex);
	
	public void selectPage(String pageIndex);
	
	public void clearAll();
	
	public void selectAll();
	
	public void invertAll();
	
	public void invertPageSelection(String pageIndex);
	
	public Vector<PageElementIndex> getSelectedPageIndices();
}
