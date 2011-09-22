package edu.wustl.common.query;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.dao.queryExecutor.PagenatedResultData;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: (c) Washington University, School of Medicine 2005
 * </p>
 * <p>
 * Company: Washington University, School of Medicine, St. Louis.
 * </p>
 * 
 * @author Aarti Sharma
 * @version 1.0
 */
public abstract class Query {
	/**
	 * Advanced query type constant
	 */
	public static final String ADVANCED_QUERY = "AdvancedQuery";

	/**
	 * Simple query type constant
	 */
	public static final String SIMPLE_QUERY = "SimpleQuery";

	/**
	 * Vector of DataElement objects that need to be selected in the output
	 */
	private Vector resultView = new Vector();

	/**
	 * Starting object from which all related objects can be part of the query
	 */
	protected String queryStartObject;

	/**
	 * Parent object of the queryStartObject i.e. the object from which the
	 * queryStartObject is derived
	 */
	private String parentOfQueryStartObject = new String();

	/**
	 * Set of tables that should be included in FROM part of query
	 */
	private Set tableSet = new HashSet();

	/**
	 * Object that forms the where part of query. This is SimpleConditionsImpl
	 * object in case of Simple query and AdvancedConditionsImpl object in case
	 * of Advanced query
	 */
	protected ConditionsImpl whereConditions;

	/**
	 * Suffix that is appended to all table aliases which is helpful in case of
	 * nested queries to differentiate between super query object from the
	 * subquery object of same type
	 */
	protected int tableSufix = 1;
	

	protected int levelOfParent = 0;
	
	//Aarti: Commented the following code since its never being called
//	protected boolean isParentDerivedSpecimen = false;
	
	private String activityStatusConditions = new String();
	
	/**
	 * Operation between all children under the parent of this query
	 */
	private Operator parentsOperationWithChildren = new Operator(Operator.OR);
	
	/**
	 * Where query is on child or parent
	 * True when on child
	 */
	private boolean isQueryOnChild = true;

	/**
	 * Order by Attribute List
	 */
	private List<DataElement> orderByAttributeList = new ArrayList<DataElement>();
	
	/**
	 * Participant object constant
	 */
	public static final String PARTICIPANT = "Participant";

	public static final String COLLECTION_PROTOCOL_REGISTRATION = "CollectionProtReg";

	public static final String COLLECTION_PROTOCOL = "CollectionProtocol";

	public static final String COLLECTION_PROTOCOL_EVENT = "CollectionProtocolEvent";

	public static final String SPECIMEN_COLLECTION_GROUP = "SpecimenCollectionGroup";

	public static final String SPECIMEN = "Specimen";

	public static final String PARTICIPANT_MEDICAL_IDENTIFIER = "ParticipantMedicalId";

	public static final String INSTITUTION = "Institution";

	public static final String DEPARTMENT = "Department";

	public static final String CANCER_RESEARCH_GROUP = "CancerResearchGroup";

	public static final String USER = "User";

	public static final String ADDRESS = "Address";

	public static final String CSM_USER = "User";

	public static final String SITE = "Site";

	public static final String STORAGE_TYPE = "StorageType";

	public static final String SPECIMEN_CHARACTERISTICS = "SpecimenCharacteristics";

	public static final String STORAGE_CONTAINER_CAPACITY = "StorageContainerCapacity";

	public static final String BIO_HAZARD = "Biohazard";

	public static final String SPECIMEN_PROTOCOL = "SpecimenProtocol";

	public static final String COLLECTION_COORDINATORS = "CollectionCoordinators";

	public static final String SPECIMEN_REQUIREMENT = "SpecimenRequirement";

	public static final String COLLECTION_SPECIMEN_REQUIREMENT = "CollectionSpecimenRequirement";

	public static final String DISTRIBUTION_SPECIMEN_REQUIREMENT = "DistributionSpecReq";

	public static final String DISTRIBUTION_PROTOCOL = "DistributionProtocol";
	
	public static final String DISTRIBUTION = "Distribution";
	
	public static final String DISTRIBUTION_ARRAY = "Distribution_array";

	public static final String REPORTED_PROBLEM = "ReportedProblem";

	public static final String CELL_SPECIMEN_REQUIREMENT = "CellSpecimenRequirement";

	public static final String MOLECULAR_SPECIMEN_REQUIREMENT = "MolecularSpecimenRequirement";

	public static final String TISSUE_SPECIMEN_REQUIREMENT = "TissueSpecimenRequirement";

	public static final String SPECIMEN_EVENT_PARAMETERS = "SpecimenEventParameters";
	
	public static final String SPECIMEN_EVENT_PARAMETERS_APPEND = "S";

	public static final String FLUID_SPECIMEN_REQUIREMENT = "FluidSpecimenRequirement";

	public static final String STORAGE_CONTAINER = "StorageContainer";

	public static final String CHECKIN_CHECKOUT_EVENT_PARAMETER = "CheckinoutEventParam";

	public static final String CLINICAL_REPORT = "ClinicalReport";
	
	public static final String PARAM = "Param";

	/**
	 * This method executes the query string formed from getString method and
	 * creates a temporary table.
	 * @param isSecureExecute
	 *            TODO
	 * @param hasConditionOnIdentifiedField TODO
	 * @param columnIdsMap
	 * 
	 * @return Returns true in case everything is successful else false
	 * @throws SQLException
	 */
	public List execute(SessionDataBean sessionDataBean,
			boolean isSecureExecute, Map queryResultObjectDataMap, boolean hasConditionOnIdentifiedField)
			throws DAOException, SQLException {
		return execute(sessionDataBean,isSecureExecute,queryResultObjectDataMap,hasConditionOnIdentifiedField,-1,-1).getResult();
	}

	/**
	 * This method executes the query string formed from getString method and
	 * creates a temporary table.
	 * @param isSecureExecute
	 * @param queryResultObjectDataMap TODO
	 * @param hasConditionOnIdentifiedField
	 * @param startIndex 
	 * @param totoalRecords
	 * @param columnIdsMap
	 * 
	 * @return Returns PagenatedResultData which contains following information:
	 * - sublist of the resultset depending upon startIndex & totoalRecords
	 * - total number of records resulting from the query.
	 * @throws SQLException
	 */
	public PagenatedResultData execute(SessionDataBean sessionDataBean,
			boolean isSecureExecute, Map queryResultObjectDataMap, boolean hasConditionOnIdentifiedField, int startIndex, int totoalRecords)
			throws DAOException, SQLException {
		try {
			JDBCDAO dao = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
			dao.openSession(null);
			String sql =  getString();
			Logger.out.debug("SQL************" + sql);
			PagenatedResultData pagenatedResultData = dao.executeQuery(sql, sessionDataBean,
					isSecureExecute,hasConditionOnIdentifiedField, queryResultObjectDataMap,startIndex,totoalRecords);
			dao.closeSession();
			return pagenatedResultData;
		} catch (DAOException daoExp) {
			throw new DAOException(daoExp.getMessage(), daoExp);
		} catch (ClassNotFoundException classExp) {
			throw new DAOException(classExp.getMessage(), classExp);
		}
	}

	/**
	 * Adds the dataElement to result view.
	 * 
	 * @param dataElement -
	 *            Data Element to be added.
	 * @return - true (as per the general contract of Collection.add).
	 */
	public boolean addElementToView(DataElement dataElement) throws SQLException{
		if(dataElement == null)
		{
			throw new NullPointerException("Data element added to view cannot be null");
		}
		return resultView.add(dataElement);
	}

	/**
	 * Adds the dataElement to result view
	 * 
	 * @param dataElement -
	 *            Data Element to be added
	 * @return - true (as per the general contract of Collection.add).
	 */
	public void addElementToView(int position, DataElement dataElement) {
		resultView.add(position, dataElement);
	}

	/**
	 * Returns the SQL representation of this query object
	 * 
	 * @return
	 * @throws SQLException
	 */
	public String getString() throws SQLException {
		
		//Formatting is required only the first time
		if(this.queryStartObject.equals(Query.PARTICIPANT))
		{
			whereConditions.formatTree();
		}
		StringBuffer query = new StringBuffer();
		HashSet set = new HashSet();

		/**
		 * Forming SELECT part of the query.
		 */
		query.append(this.getQuerySelectString());

		/**
		 * Forming FROM part of query
		 */
		set = (HashSet) getTableSet();
		set.addAll(getLinkingTables(set));
		//START: Fix for Bug#1992
		set.addAll(getChildrenTables(set));
		//END: Fix for Bug#1992		
		Logger.out.debug("Set : " + set.toString());
//		query.append("\nFROM ");
		String joinConditionString = this.getJoinConditionString(set);
		Logger.out.debug("Set After forming join string: " + set.toString());
		query.append(this.formFromString(set));

		/**
		 * Forming WHERE part of the query
		 */
		query.append(this.getWhereQueryString(joinConditionString));
		
		/**
		 * Name: Prafull
		 * Description: Query performance issue. Instead of saving complete query results in session, resultd will be fetched for each result page navigation.
		 * object of class QuerySessionData will be saved session, which will contain the required information for query execution while navigating through query result pages.
		 * 
		 *  Appending Order by clause to the SQL.  
		 */
		query.append(getOrderByClauseString());
		
		return query.toString();
	}

	/**
	 * To form the Order By clause of the SQL.
	 * @return The String representing Order By SQL for the query.
	 * @throws SQLException
	 */
	private String getOrderByClauseString() throws SQLException
	{
		StringBuffer buffer = new StringBuffer();
		if (orderByAttributeList.size()!=0)
		{
			buffer.append("\nORDER BY ");
			buffer.append(orderByAttributeList.get(0).toSQLString(this.tableSufix));
			for (int i = 1; i < orderByAttributeList.size(); i++)
			{
				buffer.append(","+orderByAttributeList.get(i).toSQLString(this.tableSufix));
			}	
		}
		return buffer.toString();
	}

	/**
	 * @param query
	 * @param joinConditionString
	 * @throws SQLException
	 */
	protected String getWhereQueryString(String joinConditionString) throws SQLException {
		StringBuffer whereQueryString = new StringBuffer();
		whereQueryString.append("\nWHERE ");
		
		if (whereConditions.hasConditions()) {
			if (joinConditionString != null
					&& joinConditionString.length() != 0) {
			    whereQueryString.append(joinConditionString);
			}
		}
		
		activityStatusConditions = this.getActivityStatusConditions();
		if (activityStatusConditions != null)
		{
			whereQueryString.append(" " + activityStatusConditions);
		}
		
		if (whereConditions.hasConditions()) {
			if (joinConditionString != null
					&& joinConditionString.length() != 0) {
				whereQueryString.append(" " + Operator.AND + " (");
			}
			whereQueryString.append(whereConditions.getString(tableSufix));
			if (joinConditionString != null
					&& joinConditionString.length() != 0) {
				whereQueryString.append(" )");
			}
		}
		return whereQueryString.toString();
	}

	/**
	 * @param query
	 * @throws SQLException
	 */
	protected String getQuerySelectString() throws SQLException {
		StringBuffer selectStringBuffer = new StringBuffer();
		selectStringBuffer.append("Select ");
		if (resultView.size() == 0) {
			selectStringBuffer.append(" * ");
		} else {
			DataElement dataElement;
			String dataElementString;
			for (int i = 0; i < resultView.size(); i++) {
				dataElement = (DataElement) resultView.get(i);
				
				dataElementString = dataElement.toSQLString(tableSufix) + " "
						+ dataElement.getColumnNameString(tableSufix) + i;
				if (dataElementString.length() >= 20) {
					dataElementString = dataElement.toSQLString(tableSufix)
							+ " Column" + i;
				}
			
				//                set.add(dataElement.getTable());
				if (i != resultView.size() - 1) {

				    selectStringBuffer.append(dataElementString + " , ");
				} else {
				    selectStringBuffer.append(dataElementString + " ");
				}
			
				
			}
			
		}
		return selectStringBuffer.toString();
	}

	/**
	 * @param set
	 * @return
	 */
	protected Set getLinkingTables(HashSet set) {
		Set linkingTables = new HashSet();
		Iterator it = set.iterator();
		Table table;
		while(it.hasNext())
		{
			table = (Table) it.next();
			while(table.hasDifferentAlias())
			{
				Logger.out.debug("Linking table: "+table+" ---> "+table.getLinkingTable());
				table = table.getLinkingTable();
				linkingTables.add(table);
			}
			
		}
		Logger.out.debug("linking tables:"+linkingTables);
		return linkingTables;
	}
	
	/**
	 * This method returns set of all the tables that are children
	 * of the queryStartObject whenever the query is a subquery
	 * @param set
	 * @return
	 */
	protected Set getChildrenTables(HashSet set) {
		Set childrenTables = new HashSet();
		/*
		 * If query is a subquery then depending on the queryStartObject add the
		 * corresponding children to the childrenTables set
		 */
		if (tableSufix > 1) {
			if (this.queryStartObject.equals(Query.SPECIMEN_COLLECTION_GROUP)) {
				childrenTables.add(new Table(Query.SPECIMEN));
			} else if (this.queryStartObject.equals(Query.COLLECTION_PROTOCOL)) {
				childrenTables.add(new Table(
						Query.COLLECTION_PROTOCOL_REGISTRATION));
				childrenTables.add(new Table(Query.COLLECTION_PROTOCOL_EVENT));
			}
		}

		Logger.out.debug("children tables:" + childrenTables);
		return childrenTables;
	}
	
	/**
	 * This method returns set of all objects related to queryStartObject
	 * transitively
	 * 
	 * @param string -
	 *            Starting object to which all related objects should be found
	 * @return set of all objects related to queryStartObject transitively
	 */
	protected HashSet getQueryObjects(String queryStartObject) {
		HashSet set = new HashSet();
		set.add(queryStartObject);
		Vector relatedObjectsCollection = (Vector) Client.relations
				.get(queryStartObject);
		if (relatedObjectsCollection == null) {
			return set;
		}

		set.addAll(relatedObjectsCollection);
		for (int i = 0; i < relatedObjectsCollection.size(); i++) {
			set
					.addAll(getQueryObjects((String) relatedObjectsCollection
							.get(i)));
		}

		//        while(queryStartObject != null)
		//        {
		//            set.add(queryStartObject);
		//            queryStartObject = (String) Client.relations.get(queryStartObject);
		//        }
		return set;
	}

	/**
	 * This method returns the Join Conditions string that joins all the
	 * tables/objects in the set. In case its a subquery relation with the
	 * superquery is also appended in this string
	 * 
	 * @param set -
	 *            objects in the query
	 * @return - string containing all join conditions
	 * @throws SQLException
	 */
	protected String getJoinConditionString(final HashSet set) throws SQLException {
		StringBuffer joinConditionString = new StringBuffer();
		Object[] tablesArray = set.toArray();
		Logger.out.debug(" tablesArray:"+Utility.getArrayString(tablesArray));
		RelationCondition relationCondition = null;
		
		joinConditionString.append(getJoinConditionWithParent(set));
		
//		if (joinConditionString.length() != 0) {
//    		joinConditionString.append(Operator.AND + " ");
//    	}

		//For all permutations of tables find the joining conditions
		Table table1;
		Table table2;
		String relationString;
		for (int i = 0; i < tablesArray.length; i++) 
		{
			table1 = (Table) tablesArray[i];
			if(table1.hasDifferentAlias())
			{
			    relationString = getRelationStringWithLinkingTable(set, table1, table1.getLinkingTable());
			    if (joinConditionString.length() != 0 && relationString!=null && relationString.length()!=0) {
		    		joinConditionString.append(Operator.AND + " ");
		    	}
			    joinConditionString.append(relationString);
				continue;
			}
			for (int j = i + 1; j < tablesArray.length; j++) {
				
				table2 = (Table) tablesArray[j];
				
				if(table2.hasDifferentAlias())
				{
					continue;
				}
				
				relationCondition = (RelationCondition) Client.relationConditionsForRelatedTables
						.get(new Relation((String) table1.getTableName(),
								(String) table2.getTableName()));
				
				
				if (relationCondition == null) 
				{
				    relationCondition = (RelationCondition) Client.relationConditionsForRelatedTables
					.get(new Relation((String) table2.getTableName(),
							(String) table1.getTableName()));
				}
				if(relationCondition != null)
				{
					Logger.out.debug(table1.getTableName() + " " + table2.getTableName()
							+ " " + relationCondition.toSQLString(tableSufix));
					if (joinConditionString.length() != 0) {
						joinConditionString.append(Operator.AND + " ");
					}
					joinConditionString.append(relationCondition
							.toSQLString(tableSufix));
				}
				
				
			}
		}
		return joinConditionString.toString();
	}

	/**
	 * This method returns the relation string of a table with different Alias
	 * with its linking table
     * @param set
     * @param joinConditionString
     * @param tableWithDiffAlias
     * @throws SQLException
     */
    private String getRelationStringWithLinkingTable(final HashSet set, Table tableWithDiffAlias, Table linkingTable) throws SQLException {
        StringBuffer relationString = new StringBuffer();
        RelationCondition relationCondition;
        relationCondition = (RelationCondition) Client.relationConditionsForRelatedTables
        .get(new Relation((String) linkingTable.getTableName(),
        		(String) tableWithDiffAlias.getTableName()));
        Logger.out.debug("***************relationCondition:"+relationCondition);
        if (relationCondition != null) {
        	relationCondition = new RelationCondition(relationCondition);
        	relationCondition.getLeftDataElement().setTable(linkingTable);
        	relationCondition.getRightDataElement().setTable(tableWithDiffAlias);
        	Logger.out.debug(tableWithDiffAlias.getTableName() + " " +linkingTable.getTableName()
        			+ " " + relationCondition.toSQLString(tableSufix));
        	if (relationString.length() != 0) {
        	    relationString.append(Operator.AND + " ");
        	}
        	relationString.append(relationCondition
        			.toSQLString(tableSufix));
        	set.add(linkingTable);
        }
        else
        {
        	relationCondition = (RelationCondition) Client.relationConditionsForRelatedTables
        	.get(new Relation((String) tableWithDiffAlias.getTableName(),
        			(String) linkingTable.getTableName()));
        	Logger.out.debug("***************relationCondition:"+relationCondition);
        	if (relationCondition != null) {
        		relationCondition = new RelationCondition(relationCondition);
        		relationCondition.getLeftDataElement().setTable(tableWithDiffAlias);
        		relationCondition.getRightDataElement().setTable(linkingTable);
        		Logger.out.debug(tableWithDiffAlias.getTableName() + " " +linkingTable.getTableName()
        				+ " " + relationCondition.toSQLString(tableSufix));
        		if (relationString.length() != 0) {
        		    relationString.append(Operator.AND + " ");
        		}
        		relationString.append(relationCondition
        				.toSQLString(tableSufix));
        		set.add(linkingTable);
        	}
        }
        return relationString.toString();
    }

    /**
     * @param set
     * @param joinConditionString
     * @throws SQLException
     */
    protected String getJoinConditionWithParent(final HashSet set) throws SQLException {
        StringBuffer joinConditionString = new StringBuffer();
        RelationCondition relationCondition;
        //If subquery then join with the superquery
		if (tableSufix > 1) {
			
			Logger.out.debug("Parent:"+getParentOfQueryStartObject()+" parentsOperationWithChildren:"+parentsOperationWithChildren);
			//this maps the specimen to the one in the upper query
			//this is to support queries with 'OR' condition
			//START: Fix for Bug#1992
			if(this.getParentOfQueryStartObject().equals(Query.SPECIMEN_COLLECTION_GROUP) && 
					!this.parentsOperationWithChildren.getOperatorParams()[0].equals(Operator.AND))
			{
					relationCondition = new RelationCondition(new DataElement(Query.SPECIMEN,Constants.IDENTIFIER),
							new Operator(Operator.EQUAL),new DataElement(Query.SPECIMEN,Constants.IDENTIFIER));
				
			}
			//END: Fix for Bug#1992
			else
			{
			    Relation joinRelation = getJoinRelationWithParent();
			    //If there is no joinin relationship between this query and parent query
			    //return empty string
			    if(joinRelation == null)
			    {
			        return joinConditionString.toString();
			    }
				relationCondition = (RelationCondition) Client.relationConditionsForRelatedTables
					.get(joinRelation);
				
				//If exists a join relation with parent but
				//there is no relation condition in relationship table throw exception
				if(relationCondition == null)
				{
				    throw new SQLException("Relation condition with parent is essential " +
				    		"when Join relation with parent exists");
				}
				Logger.out.debug("*********relationCondition:"+relationCondition+"  "+joinRelation);
				//START: Fix for Bug#1992
				//Add the 
//				set.add(new Table(relationCondition.getRightDataElement().getTableAliasName()));
				//END: Fix for Bug#1992
			}
			
			
			
			if (relationCondition != null) {
				DataElement rightDataElement;
				DataElement leftDataElement;
				
				//Start: Fix for Bug#2076
				Logger.out.debug("isQueryOnChild:"+isQueryOnChild);
				if(this.isQueryOnChild)
				{
					 rightDataElement = new DataElement(relationCondition.getRightDataElement());
					 leftDataElement = new DataElement(relationCondition.getLeftDataElement());
				}//END: Fix for Bug#2076
				else
				{
					leftDataElement = new DataElement(relationCondition.getRightDataElement());
					rightDataElement = new DataElement(relationCondition.getLeftDataElement());
				}
				
				
				//Aarti: Commented the following code since its never being called
				//If its a relation between two specimen
//				if(rightDataElement.getTable().getTableName().equals(Query.SPECIMEN)
//						&& leftDataElement.getTable().getTableName().equals(Query.SPECIMEN))
//				{
//					if(isParentDerivedSpecimen)
//					{
//						Logger.out.debug("Parent is derived specimen");
//						leftDataElement.setTable(new Table(Query.SPECIMEN,Query.SPECIMEN+levelOfParent+"L"));
//						leftDataElement.setTable(new Table(Query.SPECIMEN,Query.SPECIMEN+levelOfParent+"L"));
//						
//					}
//					else
//					{
//						Logger.out.debug("Parent is not derived specimen");
//					}
//				}
				
				tableSet.add(rightDataElement.getTableAliasName());
				joinConditionString.append(" "
						+ rightDataElement.toSQLString(
								tableSufix));
				joinConditionString.append(Operator.EQUAL);
				joinConditionString.append(" "+leftDataElement.toSQLString(tableSufix - 1)
						+ " ");
				Logger.out.debug(rightDataElement.toSQLString(tableSufix)+Operator.EQUAL+leftDataElement.toSQLString(tableSufix - 1));
				
			}
			
		}
		return joinConditionString.toString();
    }

    /**
	 * @return
	 */
	protected Relation getJoinRelationWithParent() {
		Map JOIN_RELATION_MAP = new HashMap();
		JOIN_RELATION_MAP.put(new Relation(Query.PARTICIPANT,Query.COLLECTION_PROTOCOL),new Relation(Query.PARTICIPANT,Query.COLLECTION_PROTOCOL_REGISTRATION));
		/** Name : Aarti Sharma
		 * Bug ID: 4366
		 * Desciption: Specimen collection group should be joined with parent query's registartion and not collection
		 * protocol event else it might lead to wrong results when there are more than one participants registered for
		 * the same protocol
		 */
		JOIN_RELATION_MAP.put(new Relation(Query.COLLECTION_PROTOCOL,Query.SPECIMEN_COLLECTION_GROUP),new Relation(Query.COLLECTION_PROTOCOL_REGISTRATION,Query.SPECIMEN_COLLECTION_GROUP));
		//START: Fix for Bug#1992
		JOIN_RELATION_MAP.put(new Relation(Query.SPECIMEN_COLLECTION_GROUP,Query.SPECIMEN),new Relation(Query.SPECIMEN_COLLECTION_GROUP,Query.SPECIMEN));
		//END: Fix for Bug#1992
		JOIN_RELATION_MAP.put(new Relation(Query.SPECIMEN,Query.SPECIMEN),new Relation(Query.SPECIMEN,Query.SPECIMEN));
		Logger.out.debug(this.getParentOfQueryStartObject()+" "+this.queryStartObject);
		return (Relation) JOIN_RELATION_MAP.get(new Relation(this.getParentOfQueryStartObject(),this.queryStartObject));
	}

	/**
	 * This method returns the string of table names in set that forms FROM part
	 * of query which forms the FROM part of the query
	 * 
	 * @param set -
	 *            set of tables
	 * @return A comma separated list of the tables in the set
	 * @throws SQLException
	 */
	protected String formFromString(final HashSet set) throws SQLException {
	    if(set == null)
	    {
	        throw new SQLException("Table set is null");
	    }
		StringBuffer fromString = new StringBuffer();
		
		fromString.append("\nFROM ");
		Iterator it = set.iterator();
		
		Table table;
		String tableName;
		while (it.hasNext()) {
			fromString.append(" ");
			table = (Table) it.next();
			tableName = (String) Client.objectTableNames.get(table.getTableName());
			Logger.out.debug(" Table name:"+table.getTableName());
			if(tableName == null)
			{
			    throw new SQLException("Unknown Object:"+table.getTableName());
			}
			fromString
					.append((tableName)
							.toUpperCase()
							+ " " + table.toSQLString() + tableSufix + " ");
			if (it.hasNext()) {
				fromString.append(",");
			}
		}
		fromString.append(" ");
		return fromString.toString();
	}

	public int getTableSufix() {
		return tableSufix;
	}

	public void setTableSufix(int tableSufix) {
		this.tableSufix = tableSufix;
	}

	public String getParentOfQueryStartObject() {
	    Logger.out.info("parent of query start object:"+parentOfQueryStartObject);
		return parentOfQueryStartObject;
	}

	public void setParentOfQueryStartObject(String parentOfQueryStartObject) {
		this.parentOfQueryStartObject = parentOfQueryStartObject;
	}

	/**
	 * @param resultView
	 *            The resultView to set.
	 */
	public void setResultView(Vector resultView) {
		this.resultView = resultView;
	}

	/**
	 * 
	 * @return
	 */
	public Vector getResultView() {
		return this.resultView;
	}

	public Set getRelatedTables(String aliasName) throws SQLException {
		List list = null;
		Set relatedTableNames = new HashSet();
		try {
			JDBCDAO dao = (JDBCDAO)DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
			dao.openSession(null);
// Commenting this variable as its not used in code.			
//			String sqlString = "SELECT tableData2.ALIAS_NAME from CATISSUE_QUERY_TABLE_DATA tableData2 "
//					+ "join (SELECT CHILD_TABLE_ID FROM CATISSUE_TABLE_RELATION relationData,"
//					+ "CATISSUE_QUERY_TABLE_DATA tableData "
//					+ "where relationData.PARENT_TABLE_ID = tableData.TABLE_ID and tableData.ALIAS_NAME = '"
//					+ aliasName
//					+ "') as relatedTables  on relatedTables.CHILD_TABLE_ID = tableData2.TABLE_ID";
			list = dao.executeQuery(getString(), null, false, null);

			Iterator iterator = list.iterator();
			while (iterator.hasNext()) {
				List row = (List) iterator.next();
				relatedTableNames.add(row.get(0));
			}

			dao.closeSession();
		} catch (DAOException daoExp) {
			Logger.out.debug("Could not obtain related tables. Exception:"
					+ daoExp.getMessage(), daoExp);
		} catch (ClassNotFoundException classExp) {
			Logger.out.debug("Could not obtain related tables. Exception:"
					+ classExp.getMessage(), classExp);
		}
		Logger.out.debug("Tables related to " + aliasName + " "
				+ relatedTableNames.toString());
		return relatedTableNames;
	}

	/**
	 * @return Returns the tableSet.
	 */
	public Set getTableNamesSet() {
		Set set = new HashSet();
		DataElement dataElement;
		if (resultView != null) {
			for (int i = 0; i < resultView.size(); i++) {
				dataElement = (DataElement) resultView.get(i);
				set.add(dataElement.getTableAliasName());

			}
		}
		if (whereConditions != null) {
			Set queryObjects = whereConditions.getQueryObjects();
			if (queryObjects != null) {
				Iterator it = queryObjects.iterator();
				Table table;
				while(it.hasNext())
				{
					table = (Table) it.next();
					set.add(table.getTableName());
				}
			}
		}
		if (this.queryStartObject != null) {
			set.add(this.queryStartObject);
		}
		if (tableSet != null) {
			
			set.addAll(tableSet);
		}
		Logger.out.debug("TableNamesSet:" + set);
		
		//REmoving all event parameter tables
		Iterator it = set.iterator();
		String tableName;
		Set newTableSet = new HashSet();
		while(it.hasNext())
		{
			tableName = (String) it.next();
			if(tableName.indexOf(Query.PARAM) == -1)
			{
				newTableSet.add(tableName);			
			}
		}
		
		Logger.out.debug("TableNamesSet after removing event parameter tables:" + newTableSet);
		
		return newTableSet;
	}
	
	/**
	 * @return Returns the tableSet.
	 */
	public Set getTableSet() {
		Set set = new HashSet();
		DataElement dataElement;
		if (resultView != null) {
			for (int i = 0; i < resultView.size(); i++) {
				dataElement = (DataElement) resultView.get(i);
				set.add(dataElement.getTable());

			}
		}
		Logger.out.debug("TableSet after adding resultview:" + set);
		
		if (whereConditions != null) {
			set.addAll(whereConditions.getQueryObjects());
		}
//		if (this.queryStartObject != null) {
//			set.add(new Table(this.queryStartObject,this.queryStartObject));
//		}
		if (tableSet != null) {
			
			Iterator it = tableSet.iterator();
			String tableName;
			while(it.hasNext())
			{
				tableName = (String) it.next();
				set.add(new Table(tableName,tableName));
			}
			
		}
		Logger.out.debug("Overall TableSet:" + set);
		return set;
	}

	/**
	 * @param tableSet
	 *            The tableSet to set.
	 */
	public void setTableSet(Set tableSet) {
		this.tableSet = tableSet;
	}

	public void showTableSet() {
		Iterator iterator1 = this.tableSet.iterator();
		while (iterator1.hasNext()) {
			Logger.out.debug("showTableSet............................"
					+ iterator1.next());
		}
	}

	/**
	 * This method returns all the column numbers in the query that belong to
	 * tableAlias and the related tables passed as parameter
	 * 
	 * @param tableAlias
	 * @param relatedTables
	 * @return
	 */
	public Vector getColumnIds(String tableAlias, Vector relatedTables) {
		Logger.out.debug(" tableAlias:" + tableAlias + " relatedTables:"
				+ relatedTables);
		if (relatedTables == null) {
			relatedTables = new Vector();
		}
		Vector columnIds = new Vector();
		DataElement dataElement;
		String dataElementTableName;
		String dataElementFieldName;
		for (int i = 0; i < resultView.size(); i++) {
			dataElement = (DataElement) resultView.get(i);
			dataElementTableName = dataElement.getTableAliasName();
			dataElementFieldName = dataElement.getField();

			//I
			if (dataElementTableName.equals(tableAlias)
					|| relatedTables.contains(dataElementTableName)) {
				if(dataElementTableName.equals(tableAlias) && (dataElementFieldName.equals(Constants.IDENTIFIER)|| 
						dataElementFieldName.equals(Constants.PARENT_SPECIMEN_ID_COLUMN)))
				{
					continue;
				}
				columnIds.add(new Integer(i + 1));
				Logger.out.debug("tableAlias:" + tableAlias + " columnId:"
						+ (i + 1));
			}
		}

		return columnIds;
	}

	/**
	 * This method returns all the column numbers in the query that belong to
	 * tableAlias and the related and are Identified Columns tables passed as
	 * parameter
	 * 
	 * @param tableAlias
	 * @param relatedTables
	 * @return
	 */
	public Vector getIdentifiedColumnIds(String tableAlias, Vector relatedTables) {
		Logger.out.debug(" tableAlias:" + tableAlias + " relatedTables:"
				+ relatedTables);
		if (relatedTables == null) {
			relatedTables = new Vector();
		}
		Vector columnIds = new Vector();
		DataElement dataElement;
		String dataElementTableName;
		String dataElementFieldName;
		Vector identifiedData;
		for (int i = 0; i < resultView.size(); i++) {
			dataElement = (DataElement) resultView.get(i);
			dataElementTableName = dataElement.getTableAliasName();
			dataElementFieldName = dataElement.getField();
			//I
			if (dataElementTableName.equals(tableAlias)
					|| relatedTables.contains(dataElementTableName)) {
				identifiedData = (Vector) Client.identifiedDataMap
						.get(dataElementTableName);
				Logger.out.debug("Table:" + dataElementTableName
						+ " Identified Data:" + identifiedData);
				if (identifiedData != null) {
					Logger.out.debug(" identifiedData not null..."
							+ identifiedData);
					Logger.out.debug(" dataElementFieldName:"
							+ dataElementFieldName);
					Logger.out
							.debug(" identifiedData.contains(dataElementFieldName)***** "
									+ identifiedData
											.contains(dataElementFieldName));
					if (identifiedData.contains(dataElementFieldName)) {
						Logger.out
								.debug(" identifiedData.contains(dataElementFieldName)***** "
										+ identifiedData
												.contains(dataElementFieldName));
						columnIds.add(new Integer(i + 1));
						Logger.out.debug("tableAlias:" + tableAlias
								+ " Identified column:" + dataElementFieldName
								+ " Identified columnId:" + (i + 1));
					}
				}

			}
		}

		return columnIds;
	}

	/**
	 * This method returns table alias -> identifier column id map of all the
	 * objects in tableAliasVector. In case there is no identifier column in the
	 * query for some table alias om the vector then that column is by default
	 * included in the resultant query
	 * 
	 * @param tableAliasVector
	 * @return
	 */
	public Map getIdentifierColumnIds(Vector tableAliasVector) {
		Logger.out.debug(" tableAliasVector:" + tableAliasVector);

		Map columnIdsMap = new HashMap();
		DataElement dataElement;
		String dataElementTableName;
		String dataElementFieldName;
		String tableAlias;
		DataElement identifierDataElement;

		for (int j = 0; j < tableAliasVector.size(); j++) {
			tableAlias = (String) tableAliasVector.get(j);
			for (int i = 0; i < resultView.size(); i++) {
				dataElement = (DataElement) resultView.get(i);
				dataElementTableName = dataElement.getTableAliasName();
				dataElementFieldName = dataElement.getField();

				if (dataElementTableName.equals(tableAlias)
						&& dataElementFieldName.equals(Constants.IDENTIFIER)) {
					columnIdsMap.put(tableAlias, new Integer(i + 1));
					Logger.out.debug("tableAlias:" + tableAlias
							+ " Identifier columnId:" + (i + 1));
					break;
				}
			}
			if (columnIdsMap.get(tableAlias) == null) {
				identifierDataElement = new DataElement(tableAlias,
						Constants.IDENTIFIER);
				this.resultView.add(identifierDataElement);
				columnIdsMap.put(tableAlias, new Integer(resultView.size()));
				Logger.out.debug("tableAlias:" + tableAlias
						+ " Added Identifier columnId:" + resultView.size());
			}
		}

		return columnIdsMap;
	}

	public Map getIdentifierColumnIds(Set tableAliasSet) {
		Vector tableAliasVector = new Vector();
		tableAliasVector.addAll(tableAliasSet);
		return getIdentifierColumnIds(tableAliasVector);
	}

	/**
	 * This method returns 'tableAlias.ColumnName' -> column id map of all the
	 * objects in select part of query. 
	 * 
	 * @return
	 */
	public Map getColumnIdsMap() {

		Map columnIdsMap = new HashMap();
		DataElement dataElement;
		String dataElementTableName;
		String dataElementFieldName;

		for (int i = 0; i < resultView.size(); i++) {
			dataElement = (DataElement) resultView.get(i);
			dataElementTableName = dataElement.getTableAliasName();
			dataElementFieldName = dataElement.getField();

			columnIdsMap.put(dataElementTableName + "." + dataElementFieldName,
					new Integer(i + 1));
			Logger.out.debug("tableAlias:" + dataElementTableName + " "
					+ dataElementFieldName + " columnId:" + (i + 1));

		}
		return columnIdsMap;
	}

	/**
	 * @return Returns the whereConditions.
	 */
	public ConditionsImpl getWhereConditions() {
		return whereConditions;
	}
	/**
	 * @return Returns the activityStatusConditions.
	 */
	public String getActivityStatusConditions() {
		return activityStatusConditions;
	}
	/**
	 * @param activityStatusConditions The activityStatusConditions to set.
	 */
	public void setActivityStatusConditions(String activityStatusConditions) {
		this.activityStatusConditions = activityStatusConditions;
	}
	
	/**
	 * This method returns true if the query made 
	 * is on any of the identified fields else false 
	 * @return
	 */
	public boolean hasConditionOnIdentifiedField()
	{
		return whereConditions.hasConditionOnIdentifiedField();
	}
	
//	/**
//	 * @return Returns the isParentDerivedSpecimen.
//	 */
//	public boolean isParentDerivedSpecimen() {
//		return isParentDerivedSpecimen;
//	}
//	/**
//	 * @param isParentDerivedSpecimen The isParentDerivedSpecimen to set.
//	 */
//	public void setParentDerivedSpecimen(boolean isParentDerivedSpecimen) {
//		this.isParentDerivedSpecimen = isParentDerivedSpecimen;
//	}
	/**
	 * @return Returns the levelOfParent.
	 */
	public int getLevelOfParent() {
		return levelOfParent;
	}
	/**
	 * @param levelOfParent The levelOfParent to set.
	 */
	public void setLevelOfParent(int levelOfParent) {
		this.levelOfParent = levelOfParent;
	}
	public Operator getParentsOperationWithChildren() {
		return parentsOperationWithChildren;
	}
	public void setParentsOperationWithChildren(
			Operator parentsOperationWithChildren) {
		this.parentsOperationWithChildren = parentsOperationWithChildren;
	}
	public boolean isQueryOnChild() {
		return isQueryOnChild;
	}
	public void setQueryOnChild(boolean isQueryOnChild) {
		this.isQueryOnChild = isQueryOnChild;
	}

	
	
	
	/**
	 * TO get the Order by Attribute list.
	 * @return the orderByAttributeList
	 */
	public List<DataElement> getOrderByAttributeList()
	{
		return orderByAttributeList;
	}

	
	/**
	 * @param orderByAttributeList the orderByAttributeList to set
	 */
	public void setOrderByAttributeList(List<DataElement> orderByAttributeList)
	{
		this.orderByAttributeList = orderByAttributeList;
	}
	
	/**
	 * To add the given attribute in the orderByAttributeList.
	 * @param attributeName The reference to the DataElement representing attribute to be added in the SQL.
	 */
	public void addToOrderByAttributeList(DataElement attributeName)
	{
		orderByAttributeList.add(attributeName);
	}
}