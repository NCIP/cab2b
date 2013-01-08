/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.domain.databaseproperties;

import edu.common.dynamicextensions.domaininterface.databaseproperties.ColumnPropertiesInterface;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:07 PM
 * @hibernate.joined-subclass table="DYEXTN_COLUMN_PROPERTIES" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class ColumnProperties extends DatabaseProperties implements ColumnPropertiesInterface{

    /**
     * Empty constructor
     */
	public ColumnProperties(){

	}
    
}