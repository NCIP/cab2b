/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package titli.controller.interfaces;


/**
 * Interface that represents object metadata like tableName and identifier
 * @author pooja_deshpande
 *
 */
public interface ObjectMetadataInterface
{
	public String getTableName(Object obj);
	public String getUniqueIdentifier(Object obj);
}
