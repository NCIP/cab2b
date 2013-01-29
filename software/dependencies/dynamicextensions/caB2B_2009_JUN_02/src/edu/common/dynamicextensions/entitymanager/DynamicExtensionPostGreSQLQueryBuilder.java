/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.entitymanager;

import java.util.ArrayList;
import java.util.List;

import edu.common.dynamicextensions.domain.Attribute;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;

/**
 * This class provides the methods that builds the queries that are specific to ORACLE database 
 * 
 * @author Rahul Ner
 */
public class DynamicExtensionPostGreSQLQueryBuilder extends DynamicExtensionBaseQueryBuilder
{

	/**
	 * This method returns the query for the attribute to modify its data type.
	 * @param attribute
	 * @param savedAttribute
	 * @param modifyAttributeRollbackQuery
	 * @param tableName
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	protected List getAttributeDataTypeChangedQuery(Attribute attribute, Attribute savedAttribute,
			List modifyAttributeRollbackQueryList) throws DynamicExtensionsSystemException
	{
		String tableName = attribute.getEntity().getTableProperties().getName();
		String columnName = attribute.getColumnProperties().getName();

		String type = "TYPE";
		String modifyAttributeRollbackQuery = "";
		String modifyAttributeQuery = getQueryPartForAttribute(attribute, type, false);
		modifyAttributeQuery = ALTER_TABLE + WHITESPACE + tableName + WHITESPACE + ALTER_KEYWORD
				+ WHITESPACE + modifyAttributeQuery;

		modifyAttributeRollbackQuery = getQueryPartForAttribute(savedAttribute, type, false);
		modifyAttributeRollbackQuery = ALTER_TABLE + WHITESPACE + tableName + WHITESPACE
				+ ALTER_KEYWORD + WHITESPACE + modifyAttributeRollbackQuery;

		String nullPartQuery = "";
		String nullPartRollbackQuery = "";

		if (attribute.getIsNullable() && !savedAttribute.getIsNullable())
		{
			nullPartQuery = ALTER_TABLE + WHITESPACE + tableName + WHITESPACE + ALTER_KEYWORD
					+ WHITESPACE + columnName + WHITESPACE + DROP_KEYWORD + WHITESPACE
					+ NOT_KEYWORD + WHITESPACE + NULL_KEYWORD;
			nullPartRollbackQuery = ALTER_TABLE + WHITESPACE + tableName + WHITESPACE
					+ ALTER_KEYWORD + WHITESPACE + columnName + WHITESPACE + SET_KEYWORD
					+ WHITESPACE + NOT_KEYWORD + WHITESPACE + NULL_KEYWORD;
		}
		else if (!attribute.getIsNullable() && savedAttribute.getIsNullable())
		{
			nullPartQuery = ALTER_TABLE + WHITESPACE + tableName + WHITESPACE + ALTER_KEYWORD
					+ WHITESPACE + columnName + WHITESPACE + SET_KEYWORD + WHITESPACE + NOT_KEYWORD
					+ WHITESPACE + NULL_KEYWORD;
			nullPartRollbackQuery = ALTER_TABLE + WHITESPACE + tableName + WHITESPACE
					+ ALTER_KEYWORD + WHITESPACE + columnName + WHITESPACE + DROP_KEYWORD
					+ WHITESPACE + NOT_KEYWORD + WHITESPACE + NULL_KEYWORD;
		}

		List modifyAttributeQueryList = new ArrayList();
		modifyAttributeQueryList.add(modifyAttributeQuery);
		modifyAttributeQueryList.add(nullPartQuery);

		modifyAttributeRollbackQueryList.add(modifyAttributeRollbackQuery);
		modifyAttributeRollbackQueryList.add(nullPartRollbackQuery);

		return modifyAttributeQueryList;
	}
}
