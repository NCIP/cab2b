/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */
package titli.controller.interfaces.entry;

import java.util.List;

import titli.controller.interfaces.record.RecordInterface;

/**
 * Part of the EntryInterface.
 * Many SubEntries make up an EntryInterface in a tree manner
 * @author Juber Patel
 *
 */
public interface SubEntryInterface extends RecordInterface
{
	/**
	 * Get the list of SubEntries that are at the next immediate level of the tree
	 * @return the list of SubEntries, null if this is a leaf SubEntryInterface
	 */
	List<SubEntryInterface> getNextLevelSubEntries();
	
	
}
