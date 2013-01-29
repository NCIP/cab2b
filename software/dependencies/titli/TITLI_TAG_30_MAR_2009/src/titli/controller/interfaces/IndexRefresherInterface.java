/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package titli.controller.interfaces;

import titli.controller.RecordIdentifier;
import titli.model.TitliException;

public interface IndexRefresherInterface 
{

	/**
	 * insert the record identified by parameters into the index
	 * 
	 * @param identifier the record identifier
	 * @throws TitliException if problems occur
	 */
	void insert(RecordIdentifier identifier)
			throws TitliException;
	

	/**
	 * update the record identified by parameters in the the index 
	 * 
	 * @param identifier the record identifier
	 * @throws TitliException if problems occur
	 */
	void update(RecordIdentifier identifier) throws TitliException;

	/**
	 * delete the record identified by parameters from the index 
	 * @param identifier the record identifier
	 * @throws TitliException if problems occur
	 */
	void delete(RecordIdentifier identifier)
			throws TitliException;

}