/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.entitymanager;

import edu.common.dynamicextensions.util.global.Constants;
import edu.common.dynamicextensions.util.global.Variables;

/**
 * This class returns the queryBuilder depending on the database
 * @author Rahul Ner
 *
 */
public class QueryBuilderFactory
{

	/**
	 * Instance of database specific query builder.
	 */
	private static DynamicExtensionBaseQueryBuilder queryBuilder = null;

	/**
	 * This method returns the query builder depending on the database
	 * @return
	 */
	public static DynamicExtensionBaseQueryBuilder getQueryBuilder()
	{
		if (queryBuilder != null)
		{
			return queryBuilder;
		}

		if (Variables.databaseName.equalsIgnoreCase(Constants.MYSQL_DATABASE))
		{
			queryBuilder = new DynamicExtensionMySQLQueryBuilder();
		}
		else if (Variables.databaseName.equalsIgnoreCase(Constants.ORACLE_DATABASE))
		{
			queryBuilder = new DynamicExtensionOracleQueryBuilder();
		}
		else if (Variables.databaseName.equalsIgnoreCase(Constants.POSTGRESQL_DATABASE))
		{
			queryBuilder = new DynamicExtensionPostGreSQLQueryBuilder();
		}
		return queryBuilder;
	}

}
