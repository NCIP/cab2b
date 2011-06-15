/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2004</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */

package edu.wustl.common.bizlogic;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.Vector;

import oracle.sql.CLOB;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.dao.QuerySessionData;
import edu.wustl.common.dao.queryExecutor.PagenatedResultData;
import edu.wustl.common.query.Client;
import edu.wustl.common.query.DataElement;
import edu.wustl.common.query.Operator;
import edu.wustl.common.query.Query;
import edu.wustl.common.query.Relation;
import edu.wustl.common.query.RelationCondition;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.global.Variables;
import edu.wustl.common.util.logger.Logger;

/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2005</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */

public class QueryBizLogic extends DefaultBizLogic
{

	private static final String ALIAS_NAME_TABLE_NAME_MAP_QUERY = "select ALIAS_NAME ,TABLE_NAME from "
			+ "CATISSUE_QUERY_TABLE_DATA";

	private static final String ALIAS_NAME_PRIVILEGE_TYPE_MAP_QUERY = "select ALIAS_NAME ,PRIVILEGE_ID from "
			+ "CATISSUE_QUERY_TABLE_DATA";

	private static final String GET_RELATION_DATA = "select FIRST_TABLE_ID, SECOND_TABLE_ID,"
			+ "FIRST_TABLE_JOIN_COLUMN, SECOND_TABLE_JOIN_COLUMN "
			+ "from CATISSUE_RELATED_TABLES_MAP";

// Commenting this variable as its not used anywhere	
//	private static final String GET_COLUMN_DATA = "select ALIAS_NAME,COLUMN_NAME from CATISSUE_INTERFACE_COLUMN_DATA columnData, "
//			+ "CATISSUE_QUERY_TABLE_DATA tableData where columnData.TABLE_ID = tableData.TABLE_ID  "
//			+ "and columnData.IDENTIFIER=";

	private static final String GET_TABLE_ALIAS = "select ALIAS_NAME from CATISSUE_QUERY_TABLE_DATA "
			+ "where TABLE_ID=";

	private static final String GET_RELATED_TABLE_ALIAS_PART1 = "SELECT table2.alias_name "
			+ " from catissue_table_relation relation, CATISSUE_QUERY_TABLE_DATA table1, "
			+ " CATISSUE_QUERY_TABLE_DATA table2 "
			+ " where relation.parent_table_id = table1.table_id and relation.child_table_id = table2.table_id "
			+ " and table1.alias_name = ";

	private static final String GET_RELATED_TABLE_ALIAS_PART2 = " and exists "
			+ "(select * from catissue_search_display_data displayData "
			+ " where relation.RELATIONSHIP_ID = displayData.RELATIONSHIP_ID)";

	public static HashMap getQueryObjectNameTableNameMap()
	{
		List list = null;
		HashMap queryObjectNameTableNameMap = new HashMap();
		JDBCDAO dao = null;
		try
		{
			dao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
			dao.openSession(null);
			list = dao.executeQuery(ALIAS_NAME_TABLE_NAME_MAP_QUERY, null, false, null);

			Iterator iterator = list.iterator();
			while (iterator.hasNext())
			{
				List row = (List) iterator.next();
				queryObjectNameTableNameMap.put(row.get(0), row.get(1));
			}

		}
		catch (DAOException daoExp)
		{
			Logger.out.debug("Could not obtain table object relation. Exception:"
					+ daoExp.getMessage(), daoExp);
		}
		catch (ClassNotFoundException classExp)
		{
			Logger.out.debug("Could not obtain table object relation. Exception:"
					+ classExp.getMessage(), classExp);
		}
		finally
		{
			try
			{
				dao.closeSession();
			}
			catch (DAOException e)
			{
				Logger.out.debug(e.getMessage(), e);
			}
		}
		return queryObjectNameTableNameMap;
	}

	/**
	 * This returns the map containing table alias as key
	 * and type of privilege on that table as value
	 * @return
	 */
	public static HashMap getPivilegeTypeMap()
	{
		List list = null;
		HashMap pivilegeTypeMap = new HashMap();
		JDBCDAO dao = null;
		try
		{
			dao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
			dao.openSession(null);
			list = dao.executeQuery(ALIAS_NAME_PRIVILEGE_TYPE_MAP_QUERY, null, false, null);

			Iterator iterator = list.iterator();
			while (iterator.hasNext())
			{
				List row = (List) iterator.next();
				pivilegeTypeMap.put(row.get(0), row.get(1));
				
			}

		}
		catch (DAOException daoExp)
		{
			Logger.out.debug("Could not obtain table privilege map. Exception:"
					+ daoExp.getMessage(), daoExp);
		}
		catch (ClassNotFoundException classExp)
		{
			Logger.out.debug("Could not obtain table privilege map. Exception:"
					+ classExp.getMessage(), classExp);
		}
		finally
		{
			try
			{
				dao.closeSession();
			}
			catch (DAOException e)
			{
				Logger.out.debug(e.getMessage(), e);
			}
		}
		return pivilegeTypeMap;
	}

	public static HashMap getRelationData()
	{
		List list = null;
		JDBCDAO dao = null;
		String tableAlias1;
		String columnName1;
		String tableAlias2;
		String columnName2;
		List columnDataList;
		List row;
		String parentTableColumnID;
		String childTableColumnID;
		HashMap relationConditionsForRelatedTables = new HashMap();
		try
		{
			dao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
			dao.openSession(null);
			list = dao.executeQuery(GET_RELATION_DATA, null, false, null);

			Iterator iterator = list.iterator();

			while (iterator.hasNext())
			{
				row = (List) iterator.next();
				parentTableColumnID = (String) row.get(0);
				childTableColumnID = (String) row.get(1);
				columnName1 = (String) row.get(2);
				columnName2 = (String) row.get(3);
				if (Integer.parseInt(parentTableColumnID) == 0
						|| Integer.parseInt(childTableColumnID) == 0)
				{
					continue;
				}

				columnDataList = dao.executeQuery(GET_TABLE_ALIAS + parentTableColumnID, null,
						false, null);
				if (columnDataList.size() <= 0)
				{
					continue;
				}
				row = (List) columnDataList.get(0);
				if (row.size() <= 0)
				{
					continue;
				}
				tableAlias1 = (String) row.get(0);

				columnDataList = dao.executeQuery(GET_TABLE_ALIAS + childTableColumnID, null,
						false, null);
				if (columnDataList.size() <= 0)
				{
					continue;
				}
				row = (List) columnDataList.get(0);
				if (row.size() <= 0)
				{
					continue;
				}
				tableAlias2 = (String) row.get(0);

				relationConditionsForRelatedTables.put(new Relation(tableAlias1, tableAlias2),
						new RelationCondition(new DataElement(tableAlias1, columnName1),
								new Operator(Operator.EQUAL), new DataElement(tableAlias2,
										columnName2)));
			}

		}
		catch (DAOException daoExp)
		{
			Logger.out.debug("Could not obtain table object relation. Exception:"
					+ daoExp.getMessage(), daoExp);
		}
		catch (ClassNotFoundException classExp)
		{
			Logger.out.debug("Could not obtain table object relation. Exception:"
					+ classExp.getMessage(), classExp);
		}
		finally
		{
			try
			{
				dao.closeSession();
			}
			catch (DAOException e)
			{
				Logger.out.debug(e.getMessage(), e);
			}
		}
		return relationConditionsForRelatedTables;
	}

	public static void initializeQueryData()
	{
		//        setObjectTableNames();
		//        setRelationConditionsForRelatedTables();
		//        setRelations();

		Client.objectTableNames = QueryBizLogic.getQueryObjectNameTableNameMap();
		Client.relationConditionsForRelatedTables = QueryBizLogic.getRelationData();
		Client.privilegeTypeMap = QueryBizLogic.getPivilegeTypeMap();
		Vector identifiedData = new Vector();

		//        identifiedData.add("firstName");
		//        identifiedData.add("lastName");
		//        identifiedData.add("middleName");
		//        identifiedData.add("birthDate");
		//        identifiedData.add("socialSecurityNumber");
		//        Client.identifiedFieldsMap.put(Participant.class.getName(), identifiedData);
		//        
		//        identifiedData = new Vector();
		//        identifiedData.add("registrationDate");
		//        Client.identifiedFieldsMap.put(CollectionProtocolRegistration.class.getName(), identifiedData);
		//        
		//        identifiedData = new Vector();
		//        identifiedData.add("medicalRecordNumber");
		//        Client.identifiedFieldsMap.put(ParticipantMedicalIdentifier.class.getName(), identifiedData);
		//        
		//        identifiedData = new Vector();
		//        identifiedData.add("surgicalPathologyNumber");
		//        Client.identifiedFieldsMap.put(ClinicalReport.class.getName(), identifiedData);

		//For Participant
		//identifiedData = new Vector();
		identifiedData.add("FIRST_NAME");
		identifiedData.add("LAST_NAME");
		identifiedData.add("MIDDLE_NAME");
		identifiedData.add("BIRTH_DATE");
		identifiedData.add("SOCIAL_SECURITY_NUMBER");
		identifiedData.add("DEATH_DATE");
		Client.identifiedDataMap.put(Query.PARTICIPANT, identifiedData);

		//For CollectionProtocolRegistration
		identifiedData = new Vector();
		identifiedData.add("REGISTRATION_DATE");
		Client.identifiedDataMap.put(Query.COLLECTION_PROTOCOL_REGISTRATION, identifiedData);

		//For CollectionProtocolRegistration
		identifiedData = new Vector();
		identifiedData.add("MEDICAL_RECORD_NUMBER");
		Client.identifiedDataMap.put(Query.PARTICIPANT_MEDICAL_IDENTIFIER, identifiedData);

		//For CollectionProtocolRegistration
		identifiedData = new Vector();
		identifiedData.add("SURGICAL_PATHOLOGICAL_NUMBER");
		Client.identifiedDataMap.put(Query.CLINICAL_REPORT, identifiedData);
		
		
		identifiedData = new Vector();
		identifiedData.add("CREATED_ON_DATE");
		Client.identifiedDataMap.put(Query.SPECIMEN, identifiedData);
		
		identifiedData = new Vector();
		identifiedData.add("START_DATE");
		Client.identifiedDataMap.put(Query.USER, identifiedData);
		
		identifiedData = new Vector();
		identifiedData.add("END_DATE");
		identifiedData.add("START_DATE");
		Client.identifiedDataMap.put(Query.SPECIMEN_PROTOCOL, identifiedData);
		
		identifiedData = new Vector();
		identifiedData.add("EVENT_TIMESTAMP");
		Client.identifiedDataMap.put(Query.DISTRIBUTION, identifiedData);
		
		identifiedData = new Vector();
		identifiedData.add("EVENT_TIMESTAMP");
		Client.identifiedDataMap.put(Query.DISTRIBUTION_ARRAY, identifiedData);
		
		identifiedData = new Vector();
		identifiedData.add("REPORTED_DATE");
		Client.identifiedDataMap.put(Query.REPORTED_PROBLEM, identifiedData);

		//        Client.identifiedClassNames.add(Participant.class.getName());
		//        Client.identifiedClassNames.add(CollectionProtocolRegistration.class.getName());
		//        Client.identifiedClassNames.add(SpecimenCollectionGroup.class.getName());
		//        Client.identifiedClassNames.add(Specimen.class.getName());
	}

	/**
	 * Returns the aliasName of the table from the table id.
	 * @param tableId
	 * @return
	 * @throws DAOException
	 */
	public String getAliasName(String columnName, Object columnValue) throws DAOException
	{
		JDBCDAO jdbcDAO = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		jdbcDAO.openSession(null);
		String[] selectColumnNames = {Constants.TABLE_ALIAS_NAME_COLUMN};
		String[] whereColumnNames = {columnName};
		String[] whereColumnConditions = {"="};
		Object[] whereColumnValues = {columnValue};
		List list = jdbcDAO.retrieve(Constants.TABLE_DATA_TABLE_NAME, selectColumnNames,
				whereColumnNames, whereColumnConditions, whereColumnValues, null);
		jdbcDAO.closeSession();

		String aliasName = null;
		if (list.isEmpty() == false)
		{
			List row = (List) list.get(0);
			aliasName = (String) row.get(0);
		}

		return aliasName;
	}

	/**
	 * Sets column names depending on the table name selected for that condition.
	 * @param request HttpServletRequest
	 * @param i number of row.
	 * @param value table name.
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
    
    /**
     * Bug#3549
     * Patch 1_1
     * Description: modified query to order the result  by ATTRIBUTE_ORDER column.
     */
	public List getColumnNames(String value) throws DAOException, ClassNotFoundException
	{
		String sql = " SELECT tableData2.ALIAS_NAME, temp.COLUMN_NAME, temp.ATTRIBUTE_TYPE, temp.TABLES_IN_PATH, temp.DISPLAY_NAME,temp.ATTRIBUTE_ORDER "
				+ " from CATISSUE_QUERY_TABLE_DATA tableData2 join "
				+ " ( SELECT  columnData.COLUMN_NAME, columnData.TABLE_ID, columnData.ATTRIBUTE_TYPE, "
				+ " displayData.DISPLAY_NAME, displayData.ATTRIBUTE_ORDER , relationData.TABLES_IN_PATH "
				+ " FROM CATISSUE_INTERFACE_COLUMN_DATA columnData, "
				+ " CATISSUE_TABLE_RELATION relationData, "
				+ " CATISSUE_QUERY_TABLE_DATA tableData, "
				+ " CATISSUE_SEARCH_DISPLAY_DATA displayData "
				+ " where relationData.CHILD_TABLE_ID = columnData.TABLE_ID and "
				+ " relationData.PARENT_TABLE_ID = tableData.TABLE_ID and "
				+ " relationData.RELATIONSHIP_ID = displayData.RELATIONSHIP_ID and "
				+ " columnData.IDENTIFIER = displayData.COL_ID and "
				+ " tableData.ALIAS_NAME = '"
				+ value + "') temp " + " on temp.TABLE_ID = tableData2.TABLE_ID ORDER BY temp.ATTRIBUTE_ORDER";

		Logger.out.debug("SQL*****************************" + sql);

		JDBCDAO jdbcDao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		jdbcDao.openSession(null);
		List list = jdbcDao.executeQuery(sql, null, false, null);
		jdbcDao.closeSession();

		List columnNameValueBeanList = new ArrayList();

		Iterator iterator = list.iterator();
		int j = 0;
		while (iterator.hasNext())
		{
			List rowList = (List) iterator.next();
			String columnValue = (String) rowList.get(j++) + "." + (String) rowList.get(j++) + "."
					+ (String) rowList.get(j++);   
            
			String tablesInPath = (String) rowList.get(j++);
			if ((tablesInPath != null) && ("".equals(tablesInPath) == false))
			{
				columnValue = columnValue + "." + tablesInPath;
			}
			String columnName = (String) rowList.get(j++);
			NameValueBean nameValueBean = new NameValueBean();
			nameValueBean.setName(columnName);
			nameValueBean.setValue(columnValue);
			columnNameValueBeanList.add(nameValueBean);
			j = 0;
		}

		return columnNameValueBeanList;
	}

	/**
	 * Sets the next table names depending on the table in the previous row. 
	 * @param request
	 * @param i
	 * @param prevValue previous table name.
	 * @param nextOperatorValue
	 * @param checkList
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	public Set getNextTableNames(String prevValue) throws DAOException, ClassNotFoundException
	{
		Set objectList = new TreeSet();
		String sql = " (select temp.ALIAS_NAME, temp.DISPLAY_NAME "
				+ " from "
				+ " (select relationData.FIRST_TABLE_ID, tableData.ALIAS_NAME, tableData.DISPLAY_NAME "
				+ " from CATISSUE_QUERY_TABLE_DATA tableData join "
				+ " CATISSUE_RELATED_TABLES_MAP relationData "
				+ " on tableData.TABLE_ID = relationData.SECOND_TABLE_ID) temp join CATISSUE_QUERY_TABLE_DATA tableData2 "
				+ " on temp.FIRST_TABLE_ID = tableData2.TABLE_ID "
				+ " where tableData2.ALIAS_NAME = '"
				+ prevValue
				+ "') "
				+ " union "
				+ " (select temp1.ALIAS_NAME, temp1.DISPLAY_NAME "
				+ " from "
				+ " (select relationData1.SECOND_TABLE_ID, tableData4.ALIAS_NAME, tableData4.DISPLAY_NAME "
				+ " from CATISSUE_QUERY_TABLE_DATA tableData4 join "
				+ " CATISSUE_RELATED_TABLES_MAP relationData1 "
				+ " on tableData4.TABLE_ID = relationData1.FIRST_TABLE_ID) temp1 join CATISSUE_QUERY_TABLE_DATA tableData3 "
				+ " on temp1.SECOND_TABLE_ID = tableData3.TABLE_ID "
				+ " where tableData3.ALIAS_NAME = '" + prevValue + "')";

		Logger.out.debug("TABLE SQL*****************************" + sql);

		JDBCDAO jdbcDao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		jdbcDao.openSession(null);
		List list = jdbcDao.executeQuery(sql, null, false, null);
		jdbcDao.closeSession();

		//Adding NameValueBean of select option.
		NameValueBean nameValueBean = new NameValueBean();
		nameValueBean.setName(Constants.SELECT_OPTION);
		nameValueBean.setValue("-1");
		objectList.add(nameValueBean);

		//Adding the NameValueBean of previous selected object.
		JDBCDAO jdbcDAO = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		jdbcDAO.openSession(null);

		sql = "select DISPLAY_NAME from CATISSUE_QUERY_TABLE_DATA where ALIAS_NAME='" + prevValue
				+ "'";
		List prevValueDisplayNameList = jdbcDAO.executeQuery(sql, null, false, null);
		jdbcDAO.closeSession();

		if (!prevValueDisplayNameList.isEmpty())
		{
			List rowList = (List) prevValueDisplayNameList.get(0);
			nameValueBean = new NameValueBean();
			nameValueBean.setName((String) rowList.get(0));
			nameValueBean.setValue(prevValue);
			objectList.add(nameValueBean);
		}

		List checkList = getCheckList();
		Iterator iterator = list.iterator();
		while (iterator.hasNext())
		{
			int j = 0;
			List rowList = (List) iterator.next();
			if (checkForTable(rowList, checkList))
			{
				nameValueBean = new NameValueBean();
				nameValueBean.setValue((String) rowList.get(j++));
				nameValueBean.setName((String) rowList.get(j));
				objectList.add(nameValueBean);
			}
		}

		return objectList;
	}

	private List getCheckList() throws DAOException, ClassNotFoundException
	{
		String sql = " select TABLE_A.ALIAS_NAME, TABLE_A.DISPLAY_NAME "
				+ " from catissue_table_relation TABLE_R, "
				+ " CATISSUE_QUERY_TABLE_DATA TABLE_A, " + " CATISSUE_QUERY_TABLE_DATA TABLE_B "
				+ " where TABLE_R.PARENT_TABLE_ID = TABLE_A.TABLE_ID and "
				+ " TABLE_R.CHILD_TABLE_ID = TABLE_B.TABLE_ID ";

		JDBCDAO jdbcDao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		jdbcDao.openSession(null);
		List checkList = jdbcDao.executeQuery(sql, null, false, null);
		jdbcDao.closeSession();

		return checkList;
	}

	private boolean checkForTable(List rowList, List checkList)
	{
		String aliasName = (String) rowList.get(0), displayName = (String) rowList.get(1);
		Iterator iterator = checkList.iterator();

		while (iterator.hasNext())
		{
			List row = (List) iterator.next();
			if (aliasName.equals((String) row.get(0)) && displayName.equals((String) row.get(1)))
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns the display name of the aliasName passed.
	 * @param value
	 * @return
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	public String getDisplayName(String aliasName) throws DAOException, ClassNotFoundException
	{
		String prevValueDisplayName = null;

		JDBCDAO jdbcDAO = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		jdbcDAO.openSession(null);
		String sql = "select DISPLAY_NAME from CATISSUE_QUERY_TABLE_DATA where ALIAS_NAME='"
				+ aliasName + "'";
		List list = jdbcDAO.executeQuery(sql, null, false, null);
		jdbcDAO.closeSession();

		if (!list.isEmpty())
		{
			List rowList = (List) list.get(0);
			prevValueDisplayName = (String) rowList.get(0);
		}
		return prevValueDisplayName;
	}

	public String getDisplayNamebyTableName(String tableName) throws DAOException,
			ClassNotFoundException
	{
		String prevValueDisplayName = null;

		JDBCDAO jdbcDAO = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		jdbcDAO.openSession(null);
		String sql = "select DISPLAY_NAME from CATISSUE_QUERY_TABLE_DATA where TABLE_NAME='"
				+ tableName + "'";
		List list = jdbcDAO.executeQuery(sql, null, false, null);
		jdbcDAO.closeSession();

		if (!list.isEmpty())
		{
			List rowList = (List) list.get(0);
			prevValueDisplayName = (String) rowList.get(0);
		}
		return prevValueDisplayName;
	}

	/**
	 * Returns all the tables in the simple query interface.
	 * @param request
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	public Set getAllTableNames(String aliasName, int forQI) throws DAOException,
			ClassNotFoundException
	{
		String[] selectColumnNames = {Constants.TABLE_DISPLAY_NAME_COLUMN,
				Constants.TABLE_ALIAS_NAME_COLUMN};
		String[] whereColumnNames = {Constants.TABLE_FOR_SQI_COLUMN};
		String[] whereColumnConditions = {"="};
		String[] whereColumnValues = {String.valueOf(forQI)};

		if ((aliasName != null) && (!"".equals(aliasName)))
		{
			whereColumnNames = new String[2];
			whereColumnNames[0] = Constants.TABLE_FOR_SQI_COLUMN;
			whereColumnNames[1] = Constants.TABLE_ALIAS_NAME_COLUMN;
			whereColumnConditions = new String[2];
			whereColumnConditions[0] = "=";
			whereColumnConditions[1] = "=";
			whereColumnValues = new String[2];
			whereColumnValues[0] = String.valueOf(forQI);
			aliasName = "'" + aliasName + "'";
			whereColumnValues[1] = aliasName;
		}

		JDBCDAO jdbcDAO = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		jdbcDAO.openSession(null);
		List tableList = jdbcDAO.retrieve(Constants.TABLE_DATA_TABLE_NAME, selectColumnNames,
				whereColumnNames, whereColumnConditions, whereColumnValues, null);
		jdbcDAO.closeSession();

		Set objectNameValueBeanList = new TreeSet();
		if (aliasName == null || "".equals(aliasName))
		{
			NameValueBean nameValueBean = new NameValueBean();
			nameValueBean.setValue("-1");
			nameValueBean.setName(Constants.SELECT_OPTION);
			objectNameValueBeanList.add(nameValueBean);
		}

		Iterator objIterator = tableList.iterator();
		while (objIterator.hasNext())
		{
			List row = (List) objIterator.next();
			NameValueBean nameValueBean = new NameValueBean();
			nameValueBean.setName((String) row.get(0));
			nameValueBean.setValue((String) row.get(1));
			objectNameValueBeanList.add(nameValueBean);
		}

		return objectNameValueBeanList;
	}

	/**
	 * Returns all the tables in the simple query interface.
	 * @param request
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	public static Vector getMainObjectsOfQuery() throws DAOException
	{
		String sql = " select alias_name from CATISSUE_QUERY_TABLE_DATA where FOR_SQI=1";

		List list = null;
		java.util.Vector mainObjects = new java.util.Vector();
		JDBCDAO dao = null;
		try
		{
			dao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
			dao.openSession(null);
			list = dao.executeQuery(sql, null, false, null);

			Iterator iterator = list.iterator();
			while (iterator.hasNext())
			{
				List row = (List) iterator.next();
				mainObjects.add(row.get(0));
				Logger.out.info("Main Objects:" + row.get(0));
			}

		}
		catch (DAOException daoExp)
		{
			Logger.out.debug("Could not obtain main objects. Exception:" + daoExp.getMessage(),
					daoExp);
		}
		catch (ClassNotFoundException classExp)
		{
			Logger.out.debug("Could not obtain main objects. Exception:" + classExp.getMessage(),
					classExp);
		}
		finally
		{
			try
			{
				dao.closeSession();
			}
			catch (DAOException e)
			{
				Logger.out.debug(e.getMessage(), e);
			}
		}
		return mainObjects;
	}

	/**
	 * This method returns all tables that are related to 
	 * the aliasname passed as parameter
	 * @author aarti_sharma
	 * @param aliasName
	 * @return
	 * @throws DAOException
	 */
	public static Vector getRelatedTableAliases(String aliasName) throws DAOException
	{
		List list = null;
		java.util.Vector relatedTableAliases = new java.util.Vector();
		JDBCDAO dao = null;
		try
		{
			dao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
			dao.openSession(null);
			list = dao.executeQuery(GET_RELATED_TABLE_ALIAS_PART1 + "'" + aliasName + "'"
					+ GET_RELATED_TABLE_ALIAS_PART2, null, false, null);

			Iterator iterator = list.iterator();
			while (iterator.hasNext())
			{
				List row = (List) iterator.next();
				relatedTableAliases.add(row.get(0));
				Logger.out.info("aliasName:" + aliasName + " Related Table: " + row.get(0));
			}

		}
		catch (DAOException daoExp)
		{
			Logger.out.debug("Could not obtain related tables. Exception:" + daoExp.getMessage(),
					daoExp);
		}
		catch (ClassNotFoundException classExp)
		{
			Logger.out.debug("Could not obtain related tables. Exception:" + classExp.getMessage(),
					classExp);
		}
		finally
		{
			try
			{
				dao.closeSession();
			}
			catch (DAOException e)
			{
				Logger.out.debug(e.getMessage(), e);
			}
		}
		return relatedTableAliases;
	}

	/**
	 * Sets column display name depending on the table name and column name.
	 * @param request HttpServletRequest
	 * @param i number of row.
	 * @param value table name.
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	//    public String getColumnDisplayNames(String aliasName,String columnName) throws DAOException, ClassNotFoundException
	//    {
	//        /*String sql = 	"SELECT displayData.DISPLAY_NAME FROM  "+
	//						"CATISSUE_SEARCH_DISPLAY_DATA displayData ,"+
	//						"CATISSUE_INTERFACE_COLUMN_DATA columnData,"+
	//						"CATISSUE_QUERY_TABLE_DATA tableData where "+
	//						"tableData.TABLE_ID = columnData.TABLE_ID AND" +
	//						" columnData.IDENTIFIER = displayData.COL_ID AND " +
	//						"tableData.ALIAS_NAME = '"+aliasName+"' AND" +
	//						" columnData.COLUMN_NAME = '"+columnName+"'";*/
	//        String sql = 	" SELECT temp.DISPLAY_NAME " +
	//        " from CATISSUE_QUERY_TABLE_DATA tableData2 join " +
	//        " ( SELECT  columnData.COLUMN_NAME, columnData.TABLE_ID, columnData.ATTRIBUTE_TYPE, " +
	//        " displayData.DISPLAY_NAME, relationData.TABLES_IN_PATH " +
	//        " FROM CATISSUE_INTERFACE_COLUMN_DATA columnData, " +
	//        " CATISSUE_TABLE_RELATION relationData, " +
	//        " CATISSUE_QUERY_TABLE_DATA tableData, " +
	//        " CATISSUE_SEARCH_DISPLAY_DATA displayData " +
	//        " where relationData.CHILD_TABLE_ID = columnData.TABLE_ID and " +
	//        " relationData.PARENT_TABLE_ID = tableData.TABLE_ID and " +
	//        " relationData.RELATIONSHIP_ID = displayData.RELATIONSHIP_ID and " +
	//        " columnData.IDENTIFIER = displayData.COL_ID and " +
	//        " tableData.ALIAS_NAME = '"+aliasName+"' AND columnData.COLUMN_NAME= '"+columnName+"' ) temp " +
	//        " on temp.TABLE_ID = tableData2.TABLE_ID ";
	//        
	//        Logger.out.debug("SQL*****************************"+sql);
	//        
	//        JDBCDAO jdbcDao = new JDBCDAO();
	//        jdbcDao.openSession(null);
	//        List list = jdbcDao.executeQuery(sql, null, false, null);
	//        jdbcDao.closeSession();
	//        String columnDisplayName  = new String();
	//        Iterator iterator = list.iterator();
	//        while (iterator.hasNext())
	//        {
	//            List rowList = (List)iterator.next();
	//            columnDisplayName = (String)rowList.get(0);
	//        }
	//        return columnDisplayName;
	//    }
	/**
	 * Returns the tables in path depending on the parent table Id and child table Id 
	 * @param tableId Table Id.
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	public Set setTablesInPath(Long parentTableId, Long childTableId) throws DAOException,
			ClassNotFoundException
	{
		String sql = " SELECT TABLES_IN_PATH FROM CATISSUE_TABLE_RELATION WHERE "
				+ " PARENT_TABLE_ID = '" + parentTableId + "' and " + " CHILD_TABLE_ID = '"
				+ childTableId + "'";

		Logger.out.debug("SQL*****************************" + sql);

		JDBCDAO jdbcDao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		jdbcDao.openSession(null);
		List list = jdbcDao.executeQuery(sql, null, false, null);
		jdbcDao.closeSession();

		Set tablePathSet = new HashSet();
		QueryBizLogic bizLogic = new QueryBizLogic();//(QueryBizLogic)BizLogicFactory.getBizLogic(Constants.SIMPLE_QUERY_INTERFACE_ID);
		Iterator iterator = list.iterator();
		while (iterator.hasNext())
		{
			List rowList = (List) iterator.next();
			String tablePath = (String) rowList.get(0);
			Logger.out.debug("tableinpath with ids as from database:" + tablePath);
			StringTokenizer pathIdToken = new StringTokenizer(tablePath, ":");
			Logger.out.debug("no. of tables in path:" + pathIdToken.countTokens());
			while (pathIdToken.hasMoreTokens())
			{
				Long tableIdinPath = Long.valueOf(pathIdToken.nextToken());
				String tableName = bizLogic.getAliasName(Constants.TABLE_ID_COLUMN, tableIdinPath);
				Logger.out.debug("table in path:" + tableName);
				tablePathSet.add(tableName);
			}
		}
		return tablePathSet;
	}

	/**
	 * Returns the attribute type for the given column name and table alias name
	 * @param columnName Column Name.
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	public String getAttributeType(String columnName, String aliasName) throws DAOException,
			ClassNotFoundException
	{
		String sql = " select columnData.ATTRIBUTE_TYPE from "
				+ " CATISSUE_INTERFACE_COLUMN_DATA columnData, "
				+ " CATISSUE_QUERY_TABLE_DATA tableData "
				+ " where  columnData.TABLE_ID = tableData.TABLE_ID and "
				+ "  columnData.COLUMN_NAME = '" + columnName + "' and tableData.ALIAS_NAME = '"
				+ aliasName + "' ";
		Logger.out.debug("SQL*****************************" + sql);

		JDBCDAO jdbcDao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		jdbcDao.openSession(null);
		List list = jdbcDao.executeQuery(sql, null, false, null);
		jdbcDao.closeSession();
		String atrributeType = new String();
		Iterator iterator = list.iterator();
		while (iterator.hasNext())
		{
			List rowList = (List) iterator.next();
			atrributeType = (String) rowList.get(0);

		}
		return atrributeType;
	}

	/**
	 * Returns the table Id of the table given the table alias name.
	 * @param tableId
	 * @return
	 * @throws DAOException
	 */
	public String getTableIdFromAliasName(String aliasName) throws DAOException
	{

		JDBCDAO jdbcDAO = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		jdbcDAO.openSession(null);
		String[] selectColumnNames = {"TABLE_ID"};
		String[] whereColumnNames = {"ALIAS_NAME"};
		String[] whereColumnConditions = {"="};
		String[] whereColumnValues = {"'" + aliasName + "'"};
		List list = jdbcDAO.retrieve("CATISSUE_QUERY_TABLE_DATA", selectColumnNames,
				whereColumnNames, whereColumnConditions, whereColumnValues, null);
		jdbcDAO.closeSession();
		Logger.out.debug("List of Ids size: " + list.size() + " list " + list);
		String tableIdString = new String();
		Iterator iterator = list.iterator();
		while (iterator.hasNext())
		{
			List rowList = (List) iterator.next();
			Logger.out.debug("RowList of Ids size: " + rowList.size() + " Rowlist " + rowList);
			//Logger.out.debug("RowList element "+rowList.get(0));
			tableIdString = (String) rowList.get(0);
		}
		return tableIdString;
	}

	/**
	 * To get the List of NameValueBean objects of columns corresponding to the table aliasName. 
	 * @param aliasName The String representing aliasName of the table.
	 * @return The List of NameValueBean objects, representing Columns corresponding to a table aliasName.
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	public List setColumnNames(String aliasName) throws DAOException, ClassNotFoundException
	{
		return getColumnNames(aliasName, false);
	}

	/**
	 * To get the List of NameValueBean objects of columns corresponding to the table aliasName. 
	 * @param aliasName The String representing aliasName of the table.
	 * @param defaultViewAttributesOnly The boolean value, which will decide the list of columns in returned list. 
	 * 			If true, it will return only default view attributes corresponding to a table aliasName, else return all attributes. 
	 * @return The List of NameValueBean objects, representing Columns corresponding to a table aliasName.
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	public List getColumnNames(String aliasName, boolean defaultViewAttributesOnly)
			throws DAOException, ClassNotFoundException
	{
		String sql = getQueryFor(aliasName, defaultViewAttributesOnly);
		List columnList = getList(aliasName, sql);
		return columnList;
	}

	/**
	 * @param aliasName The String representing alias name of the table.
	 * @param sql The Sql Script to get List of records.
	 * @return The List of NameValueBean objects, obtained by specified sql.
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	private List getList(String aliasName, String sql) throws DAOException, ClassNotFoundException
	{
		Logger.out.debug("SQL*****************************" + sql);

		JDBCDAO jdbcDao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		jdbcDao.openSession(null);
		List list = jdbcDao.executeQuery(sql, null, false, null);
		jdbcDao.closeSession();
		String tableName = new String();
		String tableDisplayName = new String();
		String columnName = new String();
		String columnDisplayName = new String();
		List columnList = new ArrayList();
		Iterator iterator = list.iterator();
		int j = 0, k = 0;
		while (iterator.hasNext())
		{

			List rowList = (List) iterator.next();
			tableName = (String) rowList.get(j++);
			tableDisplayName = getDisplayName(aliasName);
			columnName = (String) rowList.get(j++);
			columnDisplayName = (String) rowList.get(j++);

			//Name ValueBean Value in the for of tableAlias.columnName.columnDisplayName.tablesInPath 
			String columnValue = tableName + "." + columnName + "." + columnDisplayName + " : "
					+ tableDisplayName;
			String tablesInPath = (String) rowList.get(j++);

			if ((tablesInPath != null) && ("".equals(tablesInPath) == false))
			{
				columnValue = columnValue + "." + tablesInPath;
			}

			NameValueBean columns = new NameValueBean(columnDisplayName, columnValue);
			columnList.add(columns);
			j = 0;
			k++;
		}
		return columnList;
	}

	/**
	 * To get The SQL Query for fetching column names of given aliasName. 
	 * @param aliasName The Table alias name.
	 * @param defaultViewAttributesOnly true if user wants only Default view attributes, else query created will return all column names for given aliasName.
	 * @return The sql query.
	 */
     
    /**
     * Bug#3549
     * Patch 1_2
     * Description:modified query to order the result  by ATTRIBUTE_ORDER column.
     */
    
	private String getQueryFor(String aliasName, boolean defaultViewAttributesOnly)
	{
		String sql = " SELECT tableData2.ALIAS_NAME, temp.COLUMN_NAME,  temp.DISPLAY_NAME, temp.TABLES_IN_PATH  "
				+ " from CATISSUE_QUERY_TABLE_DATA tableData2 join "
				+ " ( SELECT  columnData.COLUMN_NAME, columnData.TABLE_ID, columnData.ATTRIBUTE_TYPE, "
				+ " displayData.DISPLAY_NAME,displayData.ATTRIBUTE_ORDER , relationData.TABLES_IN_PATH "
				+ " FROM CATISSUE_INTERFACE_COLUMN_DATA columnData, "
				+ " CATISSUE_TABLE_RELATION relationData, "
				+ " CATISSUE_QUERY_TABLE_DATA tableData, "
				+ " CATISSUE_SEARCH_DISPLAY_DATA displayData "
				+ " where relationData.CHILD_TABLE_ID = columnData.TABLE_ID and "
				+ " relationData.PARENT_TABLE_ID = tableData.TABLE_ID and "
				+ " relationData.RELATIONSHIP_ID = displayData.RELATIONSHIP_ID and "
				+ " columnData.IDENTIFIER = displayData.COL_ID and ";

		String sqlConditionForDefaultView = " displayData.DEFAULT_VIEW_ATTRIBUTE = 1 and ";

		String sql1 = " tableData.ALIAS_NAME = '" + aliasName + "') temp "
				+ " on temp.TABLE_ID = tableData2.TABLE_ID ORDER BY temp.ATTRIBUTE_ORDER";

		if (defaultViewAttributesOnly)
			sql = sql + sqlConditionForDefaultView + sql1;
		else
			sql = sql + sql1;

		return sql;
	}

	// For Summary Report Page
	/**
	 * Returns the count of speciman of the type passed.
	 * @param String of Specimen Type, JDDCDAO object
	 * @return String
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	public String getSpecimenTypeCount(String specimanType, JDBCDAO jdbcDAO)
			throws DAOException, ClassNotFoundException 
	{
		String prevValueDisplayName = "0";
		String sql = "select count(*) from CATISSUE_SPECIMEN specimen join catissue_abstract_specimen absspec "
				+ " on specimen.identifier=absspec.identifier where absspec.SPECIMEN_CLASS = '"
				+ specimanType
				+ "' and specimen.COLLECTION_STATUS = 'Collected'";
		try 
		{
			List list = jdbcDAO.executeQuery(sql, null, false, null);

			if (!list.isEmpty()) 
			{
				List rowList = (List) list.get(0);
				prevValueDisplayName = (String) rowList.get(0);
			}			
		}
		catch (DAOException e) 
		{
			System.out.println(e);
		}
		return prevValueDisplayName;
	}
	
	/**
	 * Returns Map which has all the details of Summary Page
	 * @return Map<String, Object>
	 * @throws DAOException
	 * @throws ClasssNotFoundException 
	 */
	
	public Map<String, Object> getTotalSummaryDetails() throws DAOException
	{		
		JDBCDAO jdbcDAO = null;
		Map<String, Object> summaryDataMap = null;
		try
		{
			// Database connection established
			jdbcDAO = (JDBCDAO) DAOFactory.getInstance().getDAO(
					Constants.JDBC_DAO);
			jdbcDAO.openSession(null);

			summaryDataMap = new HashMap<String, Object>();
			summaryDataMap.put("TotalSpecimenCount",
					getTotalSpecimenCount(jdbcDAO));
			summaryDataMap.put("TissueCount", getSpecimenTypeCount(
					Constants.TISSUE, jdbcDAO));
			summaryDataMap.put("CellCount", getSpecimenTypeCount(
					Constants.CELL, jdbcDAO));
			summaryDataMap.put("MoleculeCount", getSpecimenTypeCount(
					Constants.MOLECULE, jdbcDAO));
			summaryDataMap.put("FluidCount", getSpecimenTypeCount(
					Constants.FLUID, jdbcDAO));
			summaryDataMap.put("TissueTypeDetails",
					getSpecimenTypeDetailsCount(Constants.TISSUE, jdbcDAO));
			summaryDataMap.put("CellTypeDetails", getSpecimenTypeDetailsCount(
					Constants.CELL, jdbcDAO));
			summaryDataMap.put("MoleculeTypeDetails",
					getSpecimenTypeDetailsCount(Constants.MOLECULE, jdbcDAO));
			summaryDataMap.put("FluidTypeDetails", getSpecimenTypeDetailsCount(
					Constants.FLUID, jdbcDAO));
			summaryDataMap.put("TissueQuantity", getSpecimenTypeQuantity(
					Constants.TISSUE, jdbcDAO));
			summaryDataMap.put("CellQuantity", getSpecimenTypeQuantity(
					Constants.CELL, jdbcDAO));
			summaryDataMap.put("MoleculeQuantity", getSpecimenTypeQuantity(
					Constants.MOLECULE, jdbcDAO));
			summaryDataMap.put("FluidQuantity", getSpecimenTypeQuantity(
					Constants.FLUID, jdbcDAO));
		}
		catch (ClassNotFoundException e)
		{
			System.out.println(e);
		}
		catch (DAOException e)
		{
			System.out.println(e);
		}
		finally
		{
			jdbcDAO.closeSession();
		}		
		return summaryDataMap;
	}
	

	/**
	 * Returns the count of speciman of the type passed.
	 * @param value
	 * @return 
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	private String getSpecimenTypeQuantity(String specimanType, JDBCDAO jdbcDAO) throws DAOException,
			ClassNotFoundException
	{
		String prevValueDisplayName = "0";
		String sql = "select sum(AVAILABLE_QUANTITY) from CATISSUE_SPECIMEN specimen join"
				+ " catissue_abstract_specimen absspec on specimen.identifier=absspec.identifier"
				+ " where absspec.SPECIMEN_CLASS='" + specimanType + "'";
		try 
		{
			List list = jdbcDAO.executeQuery(sql, null, false, null);

			if (!list.isEmpty()) 
			{
				List rowList = (List) list.get(0);
				prevValueDisplayName = (String) rowList.get(0);
			}			
		}
		catch (DAOException e) 
		{
			System.out.println(e);
		}		
		return prevValueDisplayName;
	}

	/**
	 * Returns the Specimen Sub-Type and its available Quantity
	 * @param specimenType Class whose details are to be retrieved
	 * @return Vector of type and count name value bean
	 * @throws DAOException
	 * @throws ClassNotFoundException, DAOException
	 */
	private Vector getSpecimenTypeDetailsCount(String specimenType, JDBCDAO jdbcDAO) throws DAOException,
				ClassNotFoundException
	{
		String sql = "select absspec.SPECIMEN_TYPE,COUNT(*) from CATISSUE_SPECIMEN specimen "+
				"join catissue_abstract_specimen absspec on specimen.identifier=absspec.identifier "+
				"and specimen.COLLECTION_STATUS='Collected' " +
				" and absspec.SPECIMEN_CLASS = '"+ specimenType + "'group by absspec.SPECIMEN_TYPE " +
						" order by absspec.SPECIMEN_TYPE";
		Vector nameValuePairs = null;
		try 
		{
			List list = jdbcDAO.executeQuery(sql, null, false, null);			
			nameValuePairs = new Vector();
			if (!list.isEmpty())
			{
				// Creating name value beans.
				for (int i = 0; i < list.size(); i++)
				{
					List detailList = (List) list.get(i);
					NameValueBean nameValueBean = new NameValueBean();
					nameValueBean.setName((String) detailList.get(0));
					nameValueBean.setValue((String) detailList.get(1));
					Logger.out.debug(i + " : " + nameValueBean.toString());
					nameValuePairs.add(nameValueBean);
				}
			}			
		}
		catch (DAOException e) 
		{
			System.out.println(e);
		}			
		return nameValuePairs;
	}
	
	/***
	 * Returns the Total Specimen Count of caTissue
	 * @param jdbcDAO
	 * @return String
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	private String getTotalSpecimenCount(JDBCDAO jdbcDAO) throws DAOException, ClassNotFoundException
	{
		String prevValueDisplayName = "0";		
		String sql = "select count(*) from CATISSUE_SPECIMEN specimen join " +
				"catissue_abstract_specimen absspec on specimen.identifier=absspec.identifier " +
				"where specimen.COLLECTION_STATUS='Collected'";		
		try 
		{
			List list = jdbcDAO.executeQuery(sql, null, false, null);
			if (!list.isEmpty())
			{
				List rowList = (List) list.get(0);
				prevValueDisplayName = (String) rowList.get(0);
			}			
		}
		catch (DAOException e)
		{
			System.out.println(e);
		}		
		return prevValueDisplayName;
	}
	
	/***
	 * 
	 * @param sqlQuery
	 * @param sessionData
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	public void insertQuery(String sqlQuery, SessionDataBean sessionData) throws DAOException,
			ClassNotFoundException
	{

		JDBCDAO jdbcDAO = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		try
		{

			String sqlQuery1 = sqlQuery.replaceAll("'", "''");
			long no = 1;

			jdbcDAO.openSession(null);

			SimpleDateFormat fSDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String timeStamp = fSDateFormat.format(new Date());

			String ipAddr = sessionData.getIpAddress();

			String userId = sessionData.getUserId().toString();
			String comments = "QueryLog";

			if (Variables.databaseName.equals(Constants.MYSQL_DATABASE))
			{
				String sqlForAudiEvent = "insert into catissue_audit_event(IP_ADDRESS,EVENT_TIMESTAMP,USER_ID ,COMMENTS) values ('"
						+ ipAddr + "','" + timeStamp + "','" + userId + "','" + comments + "')";
				jdbcDAO.executeUpdate(sqlForAudiEvent);

				String sql = "select max(identifier) from catissue_audit_event where USER_ID='"
						+ userId + "'";

				List list = jdbcDAO.executeQuery(sql, null, false, null);

				if (!list.isEmpty())
				{

					List columnList = (List) list.get(0);
					if (!columnList.isEmpty())
					{
						String str = (String) columnList.get(0);
						if (!str.equals(""))
						{
							no = Long.parseLong(str);

						}
					}
				}

				String sqlForQueryLog = "insert into catissue_audit_event_query_log(QUERY_DETAILS,AUDIT_EVENT_ID) values ('"
						+ sqlQuery1 + "','" + no + "')";
				Logger.out.debug("sqlForQueryLog:" + sqlForQueryLog);
				jdbcDAO.executeUpdate(sqlForQueryLog);
			}
			else
			{
				String sql = "select CATISSUE_AUDIT_EVENT_PARAM_SEQ.nextVal from dual";

				List list = jdbcDAO.executeQuery(sql, null, false, null);

				if (!list.isEmpty())
				{

					List columnList = (List) list.get(0);
					if (!columnList.isEmpty())
					{
						String str = (String) columnList.get(0);
						if (!str.equals(""))
						{
							no = Long.parseLong(str);

						}
					}
				}
				String sqlForAudiEvent = "insert into catissue_audit_event(IDENTIFIER,IP_ADDRESS,EVENT_TIMESTAMP,USER_ID ,COMMENTS) values ('"
						+ no
						+ "','"
						+ ipAddr
						+ "',to_date('"
						+ timeStamp
						+ "','yyyy-mm-dd HH24:MI:SS'),'" + userId + "','" + comments + "')";
				Logger.out.info("sqlForAuditLog:" + sqlForAudiEvent);
				jdbcDAO.executeUpdate(sqlForAudiEvent);

				long queryNo = 1;
				sql = "select CATISSUE_AUDIT_EVENT_QUERY_SEQ.nextVal from dual";

				list = jdbcDAO.executeQuery(sql, null, false, null);

				if (!list.isEmpty())
				{

					List columnList = (List) list.get(0);
					if (!columnList.isEmpty())
					{
						String str = (String) columnList.get(0);
						if (!str.equals(""))
						{
							queryNo = Long.parseLong(str);

						}
					}
				}
				String sqlForQueryLog = "insert into catissue_audit_event_query_log(IDENTIFIER,QUERY_DETAILS,AUDIT_EVENT_ID) "
						+ "values (" + queryNo + ",EMPTY_CLOB(),'" + no + "')";
				jdbcDAO.executeUpdate(sqlForQueryLog);
				String sql1 = "select QUERY_DETAILS from catissue_audit_event_query_log where IDENTIFIER="+queryNo+" for update";
				list = jdbcDAO.executeQuery(sql1, null, false, null);

				CLOB clob=null;
				
				if (!list.isEmpty())
				{

					List columnList = (List) list.get(0);
					if (!columnList.isEmpty())
					{
						clob = (CLOB)columnList.get(0);
					}
				}
//				get output stream from the CLOB object
				OutputStream os = clob.getAsciiOutputStream();
				OutputStreamWriter osw = new OutputStreamWriter(os);
				
//			use that output stream to write character data to the Oracle data store
				osw.write(sqlQuery1.toCharArray());
				//write data and commit
				osw.flush();
				osw.close();
				os.close();
				jdbcDAO.commit();
				Logger.out.info("sqlForQueryLog:" + sqlForQueryLog);
				

			}
		}
		catch(IOException e)
		{
			throw new DAOException(e.getMessage());
		}
		catch(SQLException e)
		{
			throw new DAOException(e.getMessage());
		}
		catch (DAOException e)
		{
			throw (e);
		}
		finally
		{
			jdbcDAO.closeSession();
		}
		/*String sqlForQueryLog = "insert into catissue_audit_event_query_log(IDENTIFIER,QUERY_DETAILS) values ('"
		 + no + "','" + sqlQuery1 + "')";
		 jdbcDAO.executeUpdate(sqlForQueryLog);*/

	}
	
	/**
	 * Method to execute the given SQL to get the query result.
	 * @param sessionDataBean reference to SessionDataBean object
	 * @param querySessionData
	 * @param startIndex The Starting index of the result set.
	 * @return The reference to PagenatedResultData, which contains the Query result information.
	 * @throws DAOException
	 */
	public PagenatedResultData execute(SessionDataBean sessionDataBean, QuerySessionData querySessionData, int startIndex)
			throws DAOException
	{
		JDBCDAO dao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		try
		{
			dao.openSession(null);
			edu.wustl.common.dao.queryExecutor.PagenatedResultData pagenatedResultData = dao.executeQuery(querySessionData.getSql(), sessionDataBean,
					querySessionData.isSecureExecute(), querySessionData.isHasConditionOnIdentifiedField(), querySessionData.getQueryResultObjectDataMap(),
					startIndex, querySessionData.getRecordsPerPage());

			return pagenatedResultData;
		}
		catch (DAOException daoExp)
		{
			throw new DAOException(daoExp.getMessage(), daoExp);
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
	public static List executeSQL(String sql) throws DAOException, ClassNotFoundException
	{
		Logger.out.debug("SQL to get cardinality between 2 entities... "+ sql);
	
		JDBCDAO jdbcDao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		jdbcDao.openSession(null);
		List list = jdbcDao.executeQuery(sql, null, false, null);
		jdbcDao.closeSession();
		return list;
	}
}