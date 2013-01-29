/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.domain.databaseproperties;

import edu.common.dynamicextensions.domaininterface.databaseproperties.TablePropertiesInterface;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:09 PM
 * @hibernate.joined-subclass table="DYEXTN_TABLE_PROPERTIES" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class TableProperties extends DatabaseProperties implements TablePropertiesInterface {
	
	/**
	 * Empty constructor.
	 */
	public TableProperties(){
		
	}
	
	
}