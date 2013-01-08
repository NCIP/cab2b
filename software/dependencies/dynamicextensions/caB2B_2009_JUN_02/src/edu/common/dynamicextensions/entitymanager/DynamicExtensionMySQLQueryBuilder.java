/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.entitymanager;

import edu.common.dynamicextensions.domaininterface.EntityInterface;

/**
 * This class provides the methods that builds the queries that are specific to ORACLE database 
 * 
 * @author Rahul Ner
 */

public class DynamicExtensionMySQLQueryBuilder extends DynamicExtensionBaseQueryBuilder
{
	
	/**
	 * This method returns the query to add foreign key constraint in the given child entity
	 * that refers to identifier column of the parent.
	 * @param entity
	 * @return
	 */
	protected String getForeignKeyRemoveConstraintQueryForInheritance(EntityInterface entity)
	{
		StringBuffer foreignKeyConstraint = new StringBuffer();
		EntityInterface parentEntity = entity.getParentEntity();
		String foreignConstraintName = "FK" + "E" + entity.getId() + "E" + parentEntity.getId();

		foreignKeyConstraint.append(ALTER_TABLE).append(WHITESPACE).append(
				entity.getTableProperties().getName()).append(WHITESPACE).append(DROP_KEYWORD)
				.append(WHITESPACE).append(FOREIGN_KEY_KEYWORD).append(WHITESPACE).append(
						foreignConstraintName);

		return foreignKeyConstraint.toString();
	}
    
    /**
     * @see edu.common.dynamicextensions.entitymanager.DynamicExtensionBaseQueryBuilder#getEscapedStringValue(java.lang.String)
     */
    protected  String getEscapedStringValue(String value) {
        return value.replaceAll("'", "\\\\'");
    }

}
