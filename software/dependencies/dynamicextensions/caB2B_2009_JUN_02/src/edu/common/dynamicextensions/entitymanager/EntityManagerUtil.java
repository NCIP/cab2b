/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions.entitymanager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.dbManager.DBUtil;

public class EntityManagerUtil implements DynamicExtensionsQueryBuilderConstantsInterface
{

	private static Map idMap = new HashMap();

	/**
	 * @param query query to be executed
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	public static ResultSet executeQuery(String query) throws DynamicExtensionsSystemException
	{
		//System.out.println("[DE_QUERY] : " + query);

		Connection conn = null;
		try
		{
			conn = DBUtil.getConnection();
			Statement statement = null;
			statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			return resultSet;
		}

		catch (Exception e)
		{
			try
			{
				conn.rollback();
			}
			catch (SQLException e1)
			{
				throw new DynamicExtensionsSystemException(e.getMessage(), e);
			}
			throw new DynamicExtensionsSystemException(e.getMessage(), e);
		}

	}

	/**
	 * @param inputList
	 * @return
	 */
	public static String getListToString(List inputList)
	{

		String queryString = inputList.toString();
		queryString = queryString.replace("[", OPENING_BRACKET);
		queryString = queryString.replace("]", CLOSING_BRACKET);

		return queryString;
	}

	/**
	 * @param tableName
	 * @param columnName
	 * @param columnValueCondition
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	public static int getNoOfRecord(String query) throws DynamicExtensionsSystemException
	{
		//		StringBuffer query = new StringBuffer();
		//		query.append(SELECT_KEYWORD + COUNT_KEYWORD + "(*)");
		//		query.append(FROM_KEYWORD + tableName);
		//		query.append(WHERE_KEYWORD + columnName + IN_KEYWORD + getListToString(recordIdList));
		//		query.append(" and " +  DynamicExtensionBaseQueryBuilder.getRemoveDisbledRecordsQuery());

		ResultSet resultSet = null;
		try
		{
			resultSet = executeQuery(query);
			resultSet.next();
			return resultSet.getInt(1);
		}
		catch (SQLException e)
		{
			throw new DynamicExtensionsSystemException(e.getMessage(), e);
		}
		finally
		{
			if (resultSet != null)
			{
				try
				{

					resultSet.close();

				}
				catch (SQLException e)
				{
					throw new DynamicExtensionsSystemException(e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * @param query query to be executed
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	public int executeDML(String query) throws DynamicExtensionsSystemException
	{
		System.out.println(query);
		Connection conn = null;
		try
		{
			conn = DBUtil.getConnection();
			Statement statement = null;
			statement = conn.createStatement();
			return statement.executeUpdate(query);
		}
		catch (Exception e)
		{
			try
			{
				conn.rollback();
			}
			catch (SQLException e1)
			{
				throw new DynamicExtensionsSystemException(e.getMessage(), e);
			}
			throw new DynamicExtensionsSystemException(e.getMessage(), e);
		}
	}

	/**
	 * @param queryList
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	public int executeDML(List<String> queryList) throws DynamicExtensionsSystemException
	{
		int result = -1;
		for (String query : queryList)
		{
			result = executeDML(query);
		}
		return result;
	}

	/**
	 * Method generates the next identifier for the table that stores the value of the passes entity.
	 * @param entity
	 * @return
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	synchronized public static Long getNextIdentifier(String entityTableName)
			throws DynamicExtensionsSystemException
	{

		StringBuffer queryToGetNextIdentifier = new StringBuffer("SELECT MAX(IDENTIFIER) FROM "
				+ entityTableName);
		try
		{
			Long identifier = null;
			if (idMap.containsKey(entityTableName))
			{
				Long id = (Long) idMap.get(entityTableName);
				identifier = id + 1;
			}
			else
			{
				ResultSet resultSet = executeQuery(queryToGetNextIdentifier.toString());
				resultSet.next();
				identifier = resultSet.getLong(1);
				identifier = identifier + 1;
			}
			idMap.put(entityTableName,identifier);
			return identifier;
		}
		catch (SQLException e)
		{
			throw new DynamicExtensionsSystemException(
					"Could not fetch the next identifier for table " + entityTableName);
		}
	}

	/**
	 * This method is used in case result of the query is miltiple records.
	 *
	 * @param query query to be executed.
	 * @return List of records.
	 * @throws DynamicExtensionsSystemException
	 */
	public List getResultInList(String query) throws DynamicExtensionsSystemException
	{
		List resultList = new ArrayList();
		Session session = null;
		Connection con = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try
		{
			session = DBUtil.getCleanSession();
			con = session.connection();
			statement = con.createStatement();
			resultSet = statement.executeQuery(query);
			while (resultSet.next())
			{
				Long id = resultSet.getLong(1);
				resultList.add(id);
			}
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (BizLogicException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try
			{
				resultSet.close();
				statement.close();
				session.close();
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return resultList;
	}

	/**
	 * This method returns all the entity groups reachable from given entity.
	 *
	 * @param entity
	 * @param processedEntities
	 * @param processedEntityGroups
	 */
	public static void getAllEntityGroups(EntityInterface entity,
			Set<EntityInterface> processedEntities, Set<EntityGroupInterface> processedEntityGroups)
	{

		if (processedEntities.contains(entity))
		{
			return;
		}

		processedEntities.add(entity);

		// get all entity Groups of given entity
		for (EntityGroupInterface entityGroup : entity.getEntityGroupCollection())
		{

			if (!processedEntityGroups.contains(entityGroup))
			{
				processedEntityGroups.add(entityGroup);
				// process  all entities of each entity Groups
				for (EntityInterface anotherEntity : entityGroup.getEntityCollection())
				{
					getAllEntityGroups(anotherEntity, processedEntities, processedEntityGroups);
				}
			}
		}
	}

	/**
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	public static boolean isValuePresent(AttributeInterface attribute, Object value, Map<String, String> parameterMap)
			throws DynamicExtensionsSystemException
	{
		return new DynamicExtensionBaseQueryBuilder().isValuePresent(attribute, value, parameterMap);
	}

	public static List<AbstractAttributeInterface> filterSystemAttributes(
			List<AbstractAttributeInterface> attributeCollection)
	{
		AbstractAttributeInterface idAttribute = null;
		for (AbstractAttributeInterface attribute : attributeCollection)
		{

			if (attribute.getName().equalsIgnoreCase(ID_ATTRIBUTE_NAME))
			{
				idAttribute = attribute;
				break;
			}

		}
		attributeCollection.remove(idAttribute);
		return attributeCollection;
	}

	public static Collection<AbstractAttributeInterface> filterSystemAttributes(Collection<AbstractAttributeInterface> attributesCollection)
	{
		return filterSystemAttributes(new ArrayList(attributesCollection));
	}
}
