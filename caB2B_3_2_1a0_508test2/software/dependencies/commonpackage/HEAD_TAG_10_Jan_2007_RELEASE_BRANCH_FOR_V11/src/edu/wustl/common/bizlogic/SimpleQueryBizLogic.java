/**
 * <p>Title: SimpleQueryBizLogic Class>
 * <p>Description:	SimpleQueryBizLogic contains the bizlogic required for simple query interface.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.common.bizlogic;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import edu.wustl.common.beans.QueryResultObjectData;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.query.DataElement;
import edu.wustl.common.query.Query;
import edu.wustl.common.query.SimpleConditionsNode;
import edu.wustl.common.query.Table;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.dbManager.HibernateMetaData;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * SimpleQueryBizLogic contains the bizlogic required for simple query interface.
 * @author gautam_shetty
 */
public class SimpleQueryBizLogic extends DefaultBizLogic
{

	/**
	 * Adds single quotes (') for string and date type attributes in the condition collecion 
	 * and the returns the Set of objects to which the condition attributes belong.
	 * @param simpleConditionNodeCollection The condition collection.
	 * @return the Set of objects to which the condition attributes belong.
	 * @throws DAOException
	 */
	public List handleStringAndDateConditions(Collection simpleConditionNodeCollection,
			List fromTables) throws DAOException
	{
		//Adding single quotes to strings and date values.
		Iterator iterator = simpleConditionNodeCollection.iterator();
		while (iterator.hasNext())
		{
			SimpleConditionsNode simpleConditionsNode = (SimpleConditionsNode) iterator.next();

			// Add all the objects selected in UI to the fromtables Set. 
			addInListIfNotPresent(fromTables, simpleConditionsNode.getCondition().getDataElement()
					.getTableAliasName());

			// Adds single quotes to the value of attributes whose type is string or date.
			String tableInPath = addSingleQuotes(simpleConditionsNode);

			//Get the tables in path for this field and add it in the fromTables Set. 
			if (tableInPath != null)
			{
				addTablesInPathToFromSet(fromTables, tableInPath);
			}

			addInListIfNotPresent(fromTables, simpleConditionsNode.getCondition().getDataElement()
					.getTableAliasName());
		}
		return fromTables;
	}

	/**
	 * Gets the alias names of the tables in path and adds them in the fromTables Set passed.
	 * @param fromTables The Set to which the alias names are to be added.
	 * @param tableInPath The ids of tables in path separated by :
	 * @throws DAOException
	 */
	private void addTablesInPathToFromSet(List fromTables, String tableInPath) throws DAOException
	{
		StringTokenizer tableInPathTokenizer = new StringTokenizer(tableInPath, ":");
		while (tableInPathTokenizer.hasMoreTokens())
		{
			Long tableId = Long.valueOf(tableInPathTokenizer.nextToken());
			QueryBizLogic bizLogic = new QueryBizLogic();//(QueryBizLogic)BizLogicFactory.getBizLogic(Constants.SIMPLE_QUERY_INTERFACE_ID);
			String aliasName = bizLogic.getAliasName(Constants.TABLE_ID_COLUMN, tableId);

			if (aliasName != null)
			{
				addInListIfNotPresent(fromTables, aliasName);
			}
		}
	}

	/**
	 * Adds quotes to the value of the attribute whose type is string or date 
	 * and returns the tables in path for that object.
	 * @param simpleConditionsNode The conditio node to be checked. 
	 * @return The tables in path for that object.
	 */
	private String addSingleQuotes(SimpleConditionsNode simpleConditionsNode)
	{
		String columnName = simpleConditionsNode.getCondition().getDataElement().getField();
		Logger.out.debug(" columnName:" + columnName);
		StringTokenizer stringToken = new StringTokenizer(columnName, ".");
		simpleConditionsNode.getCondition().getDataElement().setTable(stringToken.nextToken());
		Logger.out.debug("tablename:"
				+ simpleConditionsNode.getCondition().getDataElement().getTableAliasName());
		simpleConditionsNode.getCondition().getDataElement().setField(stringToken.nextToken());
		simpleConditionsNode.getCondition().getDataElement().setFieldType(stringToken.nextToken());
		String tableInPath = null;

		if (stringToken.hasMoreTokens())
		{
			tableInPath = stringToken.nextToken();
		}

		Logger.out.debug("^^^^^^^^^^^Condition:" + simpleConditionsNode.getCondition());

		return tableInPath;
	}

	/**
	 * Adds the activity status conditions for all the objects in the from clause.
	 * @param simpleConditionNodeCollection The SimpleConditionsNode Collection.
	 * @param fromTables Set of tables in the from clause of the query.
	 * @param simpleConditionsNode The last condition in the simpleConditionNode's Collection.
	 */
	public void addActivityStatusConditions(Collection simpleConditionNodeCollection, Set fromTables)
			throws DAOException, ClassNotFoundException
	{
		// Creating aliasName set with full package names.
		// Required for checking the activityStatus.
		Iterator fromTableSetIterator = fromTables.iterator();
		// Check and get the activity status conditions for all the objects in the conditions.
		List activityStatusConditionList = new ArrayList();

		while (fromTableSetIterator.hasNext())
		{
			String alias = (String) fromTableSetIterator.next();
			String className = getClassName(alias);
			SimpleConditionsNode activityStatusCondition = getActivityStatusCondition(alias,className);

			if (activityStatusCondition != null)
			{
				activityStatusCondition.getOperator().setOperator(Constants.AND_JOIN_CONDITION);
				activityStatusConditionList.add(activityStatusCondition);
			}
		}

		if (activityStatusConditionList.isEmpty() == false)
		{
			// Set the next operator of the last simple condition nodes as AND.
			Iterator iterator = simpleConditionNodeCollection.iterator();
			SimpleConditionsNode simpleConditionsNode = null;
			while (iterator.hasNext())
			{
				simpleConditionsNode = (SimpleConditionsNode) iterator.next();
			}
			simpleConditionsNode.getOperator().setOperator(Constants.AND_JOIN_CONDITION);

			// Add the activity status conditions in the simple conditions node collection.
			simpleConditionNodeCollection.addAll(activityStatusConditionList);
		}
	}

	/**
	 * TO create The Order by Attribute list & set it into the Query object.
	 * @param fromTables The From tables present in the from part of the Query.
	 * @param query The reference to the Query object.
	 * @throws DAOException 
	 */
	public void createOrderByListInQuery(Set fromTables, Query query) throws DAOException
	{
		// getting main Query object present in the fromTables set & storing it in mainObjectsOfQuery.
		Vector<String> mainObjectsOfQuery = QueryBizLogic.getMainObjectsOfQuery();
		mainObjectsOfQuery.retainAll(fromTables);
		
		//getting related object aliases present in query, & removing mainObjectsOfQuery aliase from it, so that this set will contain only related object & not any main Query object.
		Set<String> relatedObjectAliases = new HashSet<String>(fromTables);
		relatedObjectAliases.removeAll(mainObjectsOfQuery);
		
		for (String aliasName: mainObjectsOfQuery)
		{
			query.addToOrderByAttributeList(new DataElement(aliasName,Constants.IDENTIFIER));
			Vector<String> relatedTableAliases = QueryBizLogic.getRelatedTableAliases(aliasName);
			relatedTableAliases.retainAll(relatedObjectAliases);
			for (String relatedTableAlias :relatedTableAliases)
			{
				query.addToOrderByAttributeList(new DataElement(relatedTableAlias,Constants.IDENTIFIER));
			}
			relatedObjectAliases.removeAll(relatedTableAliases);
		}
		
		if (!relatedObjectAliases.isEmpty())
			throw new RuntimeException("Problem in creating Order by Attributes !!!!!");
	}
	
	/**
	 * Returns SimpleConditionsNode if the object named aliasName contains the activityStatus 
	 * data member, else returns null.
	 * @param aliasName The alias name of the object.
	 * @param className The fully qualified name of the class in which 
	 * activity status field is to be searched.
	 * @return SimpleConditionsNode if the object named aliasName contains the activityStatus 
	 * data member, else returns null.
	 */
	private SimpleConditionsNode getActivityStatusCondition(String aliasName, String className) throws DAOException,
			ClassNotFoundException
	{
		SimpleConditionsNode activityStatusCondition = null;

		if (className.equals(Constants.REPORTED_PROBLEM_CLASS_NAME))
		{
			return null;
		}
		//Returns the Class object if it is a valid class else returns null.
		Class classObject = edu.wustl.common.util.Utility.getClassObject(className);
		if (classObject != null)
		{
			Field[] objectFields = classObject.getDeclaredFields();

			for (int i = 0; i < objectFields.length; i++)
			{
				if (objectFields[i].getName().equals(Constants.ACTIVITY_STATUS))
				{
					activityStatusCondition = new SimpleConditionsNode();
					activityStatusCondition.getCondition().getDataElement().setTableName(aliasName);
					activityStatusCondition.getCondition().getDataElement().setField(
							Constants.ACTIVITY_STATUS_COLUMN);
					activityStatusCondition.getCondition().getOperator().setOperator("!=");
					activityStatusCondition.getCondition().setValue(
							Constants.ACTIVITY_STATUS_DISABLED);
					activityStatusCondition.getCondition().getDataElement().setFieldType(
							Constants.FIELD_TYPE_VARCHAR);
					return activityStatusCondition;
				}
			}

			Class superClass = classObject.getSuperclass();
			if ((activityStatusCondition == null)
					&& (superClass.getName().equals(
							"edu.wustl.common.domain.AbstractDomainObject") == false))
			{
				String superClassAliasName = getAliasName(superClass);
				activityStatusCondition = getActivityStatusCondition(superClassAliasName,superClass.getName());
			}
		}

		return activityStatusCondition;
	}

	private Vector getViewElements(String[] selectedColumnsList)
	{
		/*Split the string which is in the form TableAlias.columnNames.columnDisplayNames 
		 * and set the dataelement object.
		 */
		Vector vector = new Vector();
		for (int i = 0; i < selectedColumnsList.length; i++)
		{
			StringTokenizer st = new StringTokenizer(selectedColumnsList[i], ".");
			DataElement dataElement = new DataElement();
			while (st.hasMoreTokens())
			{
				dataElement.setTableName(st.nextToken());
				String field = st.nextToken();
				Logger.out.debug(st.nextToken());
				String tableInPath = null;
				if (st.hasMoreTokens())
				{
					tableInPath = st.nextToken();
					field = field + "." + tableInPath;
					Logger.out.debug("Field with the table id......." + field);
				}
				dataElement.setField(field);
			}
			vector.add(dataElement);
		}

		return vector;
	}

	private List getColumnDisplayNames(String[] selectedColumnsList)
	{
		/*Split the string which is in the form TableAlias.columnNames.columnDisplayNames 
		 * and set the dataelement object.
		 */
		List columnDisplayNames = new ArrayList();
		for (int i = 0; i < selectedColumnsList.length; i++)
		{
			StringTokenizer st = new StringTokenizer(selectedColumnsList[i], ".");
			while (st.hasMoreTokens())
			{
				st.nextToken();
				st.nextToken();
				String displayName = st.nextToken();
				columnDisplayNames.add(displayName);
				Logger.out.debug("columnDisplayNames" + displayName);
				if (st.hasMoreTokens())
					st.nextToken();
			}
		}
		return columnDisplayNames;
	}

	/**
	 * To get the Vector of the DataElement objects for the given table aliasName list. Also sets the result view for the query.
	 * @param selectedColumns The Array of String of selectedColumns.
	 * @param tableList The list of Table AliasName.
	 * @param columnNames The list of Columns to be shown in the result view.
	 * @return The Vector of data elements objects for given table alias.
	 * @throws DAOException
	 */
	public Vector getSelectDataElements(String[] selectedColumns, List tableList, List columnNames)
			throws DAOException
	{
		return getSelectDataElements(selectedColumns, tableList, columnNames, false);
	}

	/**
	 * To get the Vector of the DataElement objects for the given table aliasName list. Also sets the result view for the query.
	 * @param selectedColumns The Array of String of selectedColumns.
	 * @param tableList The list of Table AliasName.
	 * @param columnNames The list of Columns to be shown in the result view.
	 * @param onlyDefaultAttributes The boolean value, which will decide the list of columns in returned list. 
	 * 			If true, it will return only default view attributes corresponding to a table aliasName, else return all attributes.
	 * @return The Vector of data elements objects for given table alias.
	 * @throws DAOException
	 */
	public Vector getSelectDataElements(String[] selectedColumns, List tableList, List columnNames,
			boolean onlyDefaultAttributes) throws DAOException
	{
		Vector selectDataElements = null;

		//If columns not conigured, set to default.
		if (selectedColumns == null)
		{
			selectDataElements = getViewElements(tableList, columnNames, onlyDefaultAttributes);
		}
		//else set to the configured columns.
		else
		{
			selectDataElements = getViewElements(selectedColumns);
			List columnNamesList = getColumnDisplayNames(selectedColumns);
			columnNames.addAll(columnNamesList);
		}

		// Getting the aliasNames of the table ids in the tables in path.
		List forFromSet = configureSelectDataElements(selectDataElements);

		forFromSet.removeAll(tableList);
		tableList.addAll(forFromSet);

		return selectDataElements;
	}

	/**
	 * Bug-2778: Results should return at least the attribute that was queried.
	 * @param selectedColumns The Array of String of selectedColumns.
	 * @param tableList The list of Table AliasName.
	 * @param columnNames The list of Columns to be shown in the result view.
	 * @param onlyDefaultAttributes The boolean value, which will decide the list of columns in returned list. 
	 * 			If true, it will return only default view attributes corresponding to a table aliasName, else return all attributes.
	 * @param fieldList List.
	 * 		  If not null or not empty, then add the attibutes from the List to selectDataElements and columnNames
	 * @return The Vector of data elements objects for given table alias.
	 * @throws DAOException
	 */
	public Vector getSelectDataElements(String[] selectedColumns, List tableList, List columnNames,
			boolean onlyDefaultAttributes, List fieldList) throws DAOException
	{
		Vector selectDataElements = getSelectDataElements(selectedColumns,tableList,columnNames,onlyDefaultAttributes);	
		
		/**
		 * Bug-2778: Results should return at least the attribute that was queried.
		 * If not null or not empty, then add the attibutes from the List to selectDataElements and columnNames
		 */
		if(fieldList != null && !fieldList.isEmpty())
		{
			Iterator itr = fieldList.iterator();
			while (itr.hasNext()) 
			{
				String field = (String) itr.next();
				
				StringTokenizer st = new StringTokenizer(field, ".");
				DataElement dataElement = new DataElement();
				Table table = new Table();
			
				String parentTableAliasName = st.nextToken();
				String childTableAliasName =  st.nextToken();
				String columnName = st.nextToken();
				String fieldType = st.nextToken();
				
				table.setTableName(childTableAliasName);				
				dataElement.setTable(table);
				dataElement.setField(columnName);
				dataElement.setFieldType(fieldType);
				if (!selectDataElements.contains(dataElement))
				{
					selectDataElements.add(dataElement);
					
					//Getting the column Display name from actual column name ex. LAST_NAME to Participant Last Name.
					String displayColumnName = getColumnDisplayName(childTableAliasName,columnName,parentTableAliasName);
					if(displayColumnName != null )
					{
						columnNames.add(displayColumnName);
					}					
				}			
			}				
		}
		return selectDataElements; 
	}
	
	/**
	 * returns display column name from the actual column name.
	 * @param aliasName String
	 * @param columnName String
	 * @return String
	 * @throws DAOException
	 */
	private String getColumnDisplayName(String childTableAliasName, String columnName,String parentTableAliasName) throws DAOException
	{
		String displayColumnName = null;
		try
		{
			JDBCDAO jdbcDao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
			jdbcDao.openSession(null);			
			Logger.out.debug("Alias Name : ............." + childTableAliasName);

			String sql = "SELECT sc.display_name, tab.alias_name FROM   " +
					"catissue_search_display_data sc, catissue_table_relation tr, " +
					"catissue_interface_column_data col, catissue_query_table_data tab, catissue_query_table_data maintab " +
					"WHERE  tr.relationship_id = sc.relationship_id AND " +
					"col.identifier = sc.col_id AND col.table_id = tab.table_id AND " +
					"maintab.table_id = tr.parent_table_id " +
					"AND maintab.alias_name = '" +
					parentTableAliasName +
					"' AND tab.alias_name='" +
					childTableAliasName +
					"' AND col.COLUMN_NAME='" +
					columnName+
					"'";       

			Logger.out.debug("DATA ELEMENT SQL : " + sql);

			List list = jdbcDao.executeQuery(sql, null, false, null);
			String nameSql = "select DISPLAY_NAME from CATISSUE_QUERY_TABLE_DATA where ALIAS_NAME='"
					+ parentTableAliasName + "'";
			List nameList = jdbcDao.executeQuery(nameSql, null, false, null);
			String tableDisplayName = new String();
			if (!nameList.isEmpty())
			{
				List rowNameList = (List) nameList.get(0);
				tableDisplayName = (String) rowNameList.get(0);
			}
			Logger.out.debug("tableDisplayName in getviewelements:" + tableDisplayName);
			Logger.out.debug("list.size()************************" + list.size());
			Iterator iterator = list.iterator();
			while (iterator.hasNext())
			{
				List rowList = (List) iterator.next();								
				displayColumnName = (String) rowList.get(0) + " : " + tableDisplayName;
			}		
			jdbcDao.closeSession();
		}
		catch (ClassNotFoundException classExp)
		{
			throw new DAOException(classExp.getMessage(), classExp);
		}	
		return displayColumnName;
	}
	
	/**
	 * Gets the fields from select clause of the query and returns 
	 * Set of objects of that attributes to be added in the from clause.  
	 * @param query The query object whose select fields are to be get.
	 * @return Set of objects of that attributes to be added in the from clause.
	 * @throws DAOException
	 */
	private List configureSelectDataElements(Vector selectDataElements) throws DAOException
	{
		List forFromSet = new ArrayList();

		Iterator iterator = selectDataElements.iterator();
		while (iterator.hasNext())
		{
			DataElement dataElement = (DataElement) iterator.next();
			StringTokenizer stringToken = new StringTokenizer(dataElement.getField(), ".");
			dataElement.setField(stringToken.nextToken());

			addInListIfNotPresent(forFromSet, dataElement.getTableAliasName());

			if (stringToken.hasMoreElements())
			{
				String tableInPath = stringToken.nextToken();
				addTablesInPathToFromSet(forFromSet, tableInPath);
			}
		}

		return forFromSet;
	}

	/**
	 * Returns the Vector of DataElement objects for the select clause of the query.
	 * And also list the column names in the columnList list.
	 * @param aliasNameList The Set of the alias names for which the DataElements are to be created.
	 * @param columnList List of column names to be shown in the spreadsheet view.
	 * @return the Vector of DataElement objects for the select clause of the query with Default view attribute.
	 * @throws DAOException
	 */
	public Vector getViewElements(List aliasNameList, List columnList) throws DAOException
	{
		Vector vector = getViewElements(aliasNameList, columnList, true);
		return vector;
	}

	/**
	 * Returns the Vector of DataElement objects for the select clause of the query.
	 * And also list the column names in the columnList list.
	 * @param aliasNameList The Set of the alias names for which the DataElements are to be created.
	 * @param columnList List of column names to be shown in the spreadsheet view.
	 * @param OnlyDefaultAttribute true if the required column should be Default view columns.
	 * @return the Vector of DataElement objects for the select clause of the query.
	 * @throws DAOException
	 */
    
    /**
     * Bug#3549
     * Patch 1_2
     * Description:modified query to order the result by ATTRIBUTE_ORDER column.
     */
    
	public Vector getViewElements(List aliasNameList, List columnList, boolean OnlyDefaultAttribute)
			throws DAOException
	{
		Vector vector = new Vector();
		try
		{
			JDBCDAO jdbcDao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
			jdbcDao.openSession(null);

			Iterator aliasNameIterator = aliasNameList.iterator();
			while (aliasNameIterator.hasNext())
			{
				String aliasName = (String) aliasNameIterator.next();
				Logger.out.debug("Alias Name : ............." + aliasName);

				String sql = " SELECT tableData2.ALIAS_NAME, temp.COLUMN_NAME, temp.TABLES_IN_PATH, "
						+ " temp.DISPLAY_NAME, temp.ATTRIBUTE_TYPE "
						+ " from CATISSUE_QUERY_TABLE_DATA tableData2 join "
						+ " ( SELECT  columnData.COLUMN_NAME, columnData.TABLE_ID, displayData.DISPLAY_NAME,displayData.ATTRIBUTE_ORDER, "
						+ " relationData.TABLES_IN_PATH, columnData.ATTRIBUTE_TYPE "
						+ " FROM CATISSUE_INTERFACE_COLUMN_DATA columnData, "
						+ " CATISSUE_TABLE_RELATION relationData, "
						+ " CATISSUE_QUERY_TABLE_DATA tableData, "
						+ " CATISSUE_SEARCH_DISPLAY_DATA displayData "
						+ " where relationData.CHILD_TABLE_ID = columnData.TABLE_ID and "
						+ " relationData.PARENT_TABLE_ID = tableData.TABLE_ID and "
						+ " relationData.RELATIONSHIP_ID = displayData.RELATIONSHIP_ID and ";

				String defaultViewCondition = " displayData.DEFAULT_VIEW_ATTRIBUTE = 1 and ";

				String sql1 = " columnData.IDENTIFIER = displayData.COL_ID and "
						+ " tableData.ALIAS_NAME = '" + aliasName + "') temp "
						+ " on temp.TABLE_ID = tableData2.TABLE_ID ORDER BY temp.ATTRIBUTE_ORDER";

				if (OnlyDefaultAttribute)
					sql = sql + defaultViewCondition + sql1;
				else
					sql = sql + sql1;

				Logger.out.debug("DATA ELEMENT SQL : " + sql);

				List list = jdbcDao.executeQuery(sql, null, false, null);
				String nameSql = "select DISPLAY_NAME from CATISSUE_QUERY_TABLE_DATA where ALIAS_NAME='"
						+ aliasName + "'";
				List nameList = jdbcDao.executeQuery(nameSql, null, false, null);
				String tableDisplayName = new String();
				if (!nameList.isEmpty())
				{
					List rowNameList = (List) nameList.get(0);
					tableDisplayName = (String) rowNameList.get(0);
				}
				Logger.out.debug("tableDisplayName in getviewelements:" + tableDisplayName);
				Logger.out.debug("list.size()************************" + list.size());
				Iterator iterator = list.iterator();
				while (iterator.hasNext())
				{
					List rowList = (List) iterator.next();
					DataElement dataElement = new DataElement();
					dataElement.setTableName((String) rowList.get(0));
					String fieldName = (String) rowList.get(1);

					Logger.out.debug("fieldName*********************" + fieldName);
					dataElement.setField(fieldName + "." + (String) rowList.get(2));
					dataElement.setFieldType((String) rowList.get(4));
					vector.add(dataElement);
					columnList.add((String) rowList.get(3) + " : " + tableDisplayName);
				}
			}
			jdbcDao.closeSession();
		}
		catch (ClassNotFoundException classExp)
		{
			throw new DAOException(classExp.getMessage(), classExp);
		}
		return vector;
	}

	/**
	 * @param fromAliasNameValue
	 * @param fromTables
	 * @return
	 * @throws DAOException
	 */
	public QueryResultObjectData createQueryResultObjectData(String fromAliasNameValue,
			Set fromTables) throws DAOException
	{
		QueryResultObjectData queryResultObjectData;
		queryResultObjectData = new QueryResultObjectData();
		queryResultObjectData.setAliasName(fromAliasNameValue);
		//Aarti: getting related tables that should be dependent 
		//on main object for authorization
		Vector relatedTables = new Vector();
		relatedTables = QueryBizLogic.getRelatedTableAliases(fromAliasNameValue);
		//remove all the related objects that are not part of the current query
		for (int i = 0; i < relatedTables.size(); i++)
		{
			if (!fromTables.contains(relatedTables.get(i)))
			{
				relatedTables.remove(i--);
			}
		}

		Logger.out.debug("After removing tables not in query relatedTable:" + relatedTables);
		//					Aarti: Get main query objects which should have individual checks
		//for authorization and should not be dependent on others
		Vector mainQueryObjects = QueryBizLogic.getMainObjectsOfQuery();

		String queryObject;
		//Aarti: remove independent query objects from related objects
		//vector and add them to tableSet so that their authorization
		//is checked individually
		for (int i = 0; i < mainQueryObjects.size(); i++)
		{
			queryObject = (String) mainQueryObjects.get(i);
			if (relatedTables.contains(queryObject))
			{
				relatedTables.remove(queryObject);
				//							tableSet.add(queryObject);
				if (!queryObject.equals(fromAliasNameValue))
				{
					queryResultObjectData.addRelatedQueryResultObject(new QueryResultObjectData(
							queryObject));
				}
			}
		}

		//Aarti: Map all related tables to the main table
		//					relatedTablesMap.put(fromAliasNameValue, relatedTables);
		queryResultObjectData.setDependentObjectAliases(relatedTables);
		return queryResultObjectData;
	}

	/**
	 * @param queryResultObjectDataMap
	 * @param query
	 */
	public List addObjectIdentifierColumnsToQuery(Map queryResultObjectDataMap, Query query)
	{
		List columnNames = new ArrayList();
		Set keySet = queryResultObjectDataMap.keySet();
		Iterator keyIterator = keySet.iterator();
		QueryResultObjectData queryResultObjectData2;
		QueryResultObjectData queryResultObjectData3;
		Vector queryObjects;
		Vector queryObjectNames;
		int initialColumnNumbers = query.getResultView().size();
		Map columnIdsMap;

		for (int i = 0; keyIterator.hasNext(); i++)
		{
			queryResultObjectData2 = (QueryResultObjectData) queryResultObjectDataMap
					.get(keyIterator.next());
			queryObjects = queryResultObjectData2.getIndependentQueryObjects();
			queryObjectNames = queryResultObjectData2.getIndependentObjectAliases();
			for (int j = 0; j < queryObjects.size(); j++)
			{
				columnIdsMap = query.getIdentifierColumnIds(queryObjectNames);
				queryResultObjectData3 = (QueryResultObjectData) queryObjects.get(j);
				queryResultObjectData3.setIdentifierColumnId(((Integer) columnIdsMap
						.get(queryResultObjectData3.getAliasName())).intValue() - 1);
			}

		}
		int columnsAdded = query.getResultView().size() - initialColumnNumbers;
		for (int i = 0; i < columnsAdded; i++)
		{
			columnNames.add(" ID");
		}
		return columnNames;
	}

	/**
	 * @param queryResultObjectDataMap
	 * @param query
	 */
	public void setDependentIdentifiedColumnIds(Map queryResultObjectDataMap, Query query)
	{
		Iterator keyIterator;
		QueryResultObjectData queryResultObjectData2;
		QueryResultObjectData queryResultObjectData3;
		Vector queryObjects;
		Set keySet2 = queryResultObjectDataMap.keySet();
		keyIterator = keySet2.iterator();
		for (int i = 0; keyIterator.hasNext(); i++)
		{
			queryResultObjectData2 = (QueryResultObjectData) queryResultObjectDataMap
					.get(keyIterator.next());
			queryObjects = queryResultObjectData2.getIndependentQueryObjects();
			for (int j = 0; j < queryObjects.size(); j++)
			{
				queryResultObjectData3 = (QueryResultObjectData) queryObjects.get(j);
				queryResultObjectData3.setDependentColumnIds(query.getColumnIds(
						queryResultObjectData3.getAliasName(), queryResultObjectData3
								.getDependentObjectAliases()));
				queryResultObjectData3.setIdentifiedDataColumnIds(query.getIdentifiedColumnIds(
						queryResultObjectData3.getAliasName(), queryResultObjectData3
								.getDependentObjectAliases()));
				Logger.out.debug(" table:" + queryResultObjectData3.getAliasName() + " columnIds:"
						+ queryResultObjectData3.getDependentColumnIds());
			}

		}
	}

	/**
	 * @param fromTables
	 * @param queryResultObjectDataMap
	 * @param query
	 */
	public void createQueryResultObjectData(Set fromTables, Map queryResultObjectDataMap,
			Query query) throws DAOException
	{
		Iterator iterator = fromTables.iterator();
		String tableAlias;
		QueryResultObjectData queryResultObjectData;
		Vector mainQueryObjects = QueryBizLogic.getMainObjectsOfQuery();
		Logger.out.debug(" tables in query:" + fromTables);
		while (iterator.hasNext())
		{
			tableAlias = (String) iterator.next();
			Logger.out.debug("*********** table obtained from fromTables set:" + tableAlias);
			if (mainQueryObjects.contains(tableAlias))
			{
				queryResultObjectData = createQueryResultObjectData(tableAlias, fromTables);
				if (query.getColumnIds(tableAlias,
						queryResultObjectData.getDependentObjectAliases()).size() != 0)
				{
					queryResultObjectDataMap.put(tableAlias, queryResultObjectData);
				}
			}
		}
	}

	
	/**
	 * To get the Map representing the Index of the column to be hyperlinked verses the QueryResultObjectData, which containes information about the Id column & alias name of the object associated with that column.
	 * @param queryResultsObjectDataMap The Map of alias name verses QueryResultObjectData, Contains information of the different objects present in Query like, alias Name, index of the id in the query results etc.
	 * @return the map of columns to be hyperlinked.
	 * @throws DAOException if any db related error occures.
	 * 
	 * Patch ID: SimpleSearchEdit_2 
	 * Description: Method to get the Map which contains information about which columns to be Hyperlinked.
	 */
	public Map<Integer, QueryResultObjectData> getHyperlinkMap(Map<String,QueryResultObjectData> queryResultsObjectDataMap, Vector<DataElement> resultView) throws DAOException
	{
		Map<Integer, QueryResultObjectData> map = new HashMap<Integer, QueryResultObjectData>();
		JDBCDAO dao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);

		try 
		{
			dao.openSession(null);
			
			for(String aliasName: queryResultsObjectDataMap.keySet())
			{
				//SQL to get information regarding which column to be hyperlined for the given table alias.
				String columnIdSQL = "Select col.COLUMN_NAME, coltable.alias_name From CATISSUE_INTERFACE_COLUMN_DATA col, CATISSUE_QUERY_TABLE_DATA maintable, CATISSUE_QUERY_TABLE_DATA coltable, CATISSUE_QUERY_EDITLINK_COLS linkcol "
					+ " where maintable.TABLE_ID = linkcol.TABLE_ID "
					+ " and linkcol.col_id = col.IDENTIFIER "
					+ " and coltable.TABLE_ID = col.TABLE_ID"
					+ " and maintable.ALIAS_NAME = '" + aliasName +"' ";
				
				List<List<String>> list = dao.executeQuery(columnIdSQL, null, false, null);
				for (List<String> row: list)
				{
					String columnName = row.get(0);
					String actualTableAliasName = row.get(1);
					int index = getIndex(resultView, actualTableAliasName, columnName);
					/**
					 * if index value is -1 then ignore that column, as that column is not present in the Result view.
					 */
					if (index!=-1) 
					{
						// This column is present in the view.
						boolean attributeFound = true; 
						QueryResultObjectData queryResultObjectData = queryResultsObjectDataMap.get(actualTableAliasName);
						if (queryResultObjectData==null) // this attribute belongs to the one of the dependent object.
						{
							attributeFound = false;
							for(String theAliasName: queryResultsObjectDataMap.keySet())
							{
								queryResultObjectData = queryResultsObjectDataMap.get(theAliasName);
								if (queryResultObjectData.getDependentObjectAliases().contains(actualTableAliasName))
								{
									//The dependent object containing the required attribute found!!!!!
									attributeFound = true;
									break; 
								}
								
							}
						}
						if (attributeFound)
						{
							map.put(index, queryResultObjectData);
						}
					}
				}
			}

			return map;
		} 
		catch (ClassNotFoundException classExp) 
		{
			throw new DAOException(classExp.getMessage(), classExp);
		}
		finally
		{
			dao.closeSession();
		}
	}
	/**
	 * TO get the index of the dataelement in the result view for given alias & columnName
	 * @param resultView the vector of data elements
	 * @param alias The alias name to search. 
	 * @param columnName the name of the column to search.
	 * @return -1 if no such data element exists in the resultview else return the index of it.
	 */
	private int getIndex(Vector<DataElement> resultView, String alias, String columnName)
	{
		for (int index = 0; index < resultView.size(); index++)
		{
			DataElement element = resultView.get(index);
			if (alias.equals(element.getTable().getTableName()) && columnName.equals(element.getField()))
			{
				return index;
			}
		}
		return -1;
	}
	
	private void addInListIfNotPresent(List list, String string)
	{
		if (list.contains(string) == false)
		{
			list.add(string);
		}
	}

	private String getClassName(String aliasName) throws DAOException, ClassNotFoundException
	{
		String tableName = getTableName(aliasName);
		String className = HibernateMetaData.getClassName(tableName);
		return className;
	}

	/**
	 * To get the tableName corresponding to the aliasName.
	 * @param aliasName The aliasName of the table.
	 * @return The tableName corresponding to the aliasName.
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	public String getTableName(String aliasName) throws DAOException, ClassNotFoundException
	{
		String tableName = new String();
		JDBCDAO jdbcDAO = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		jdbcDAO.openSession(null);
		String sql = "select TABLE_NAME from CATISSUE_QUERY_TABLE_DATA where ALIAS_NAME='"
				+ aliasName + "'";
		List list = jdbcDAO.executeQuery(sql, null, false, null);
		jdbcDAO.closeSession();

		if (!list.isEmpty())
		{
			List rowList = (List) list.get(0);
			tableName = (String) rowList.get(0);
		}
		return tableName;
	}

	/**
	 * To get the aliasName corresponding to the tableName.
	 * @param tableName The name of the table.
	 * @return The aliasName corresponding to the tableName.
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	public String getAliasName(String tableName) throws DAOException, ClassNotFoundException
	{
		String aliasName = new String();
		JDBCDAO jdbcDAO = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		jdbcDAO.openSession(null);
		String sql = "select ALIAS_NAME from CATISSUE_QUERY_TABLE_DATA where TABLE_NAME='"
				+ tableName + "'";
		List list = jdbcDAO.executeQuery(sql, null, false, null);
		jdbcDAO.closeSession();

		if (!list.isEmpty())
		{
			List rowList = (List) list.get(0);
			aliasName = (String) rowList.get(0);
		}
		return aliasName;
	}
	
	/**
	 * To get the AliasName for the Given Class.
	 * @param theClass The Class of the Domain Object.
	 * @return The AliasName corresponding to the Class.
	 * @throws ClassNotFoundException
	 * @throws DAOException
	 */
	public String getAliasName(Class theClass) throws DAOException, ClassNotFoundException
	{
		String tableName = HibernateMetaData.getTableName(theClass);
		String aliasName = getAliasName(tableName);
		return aliasName;
	}
}