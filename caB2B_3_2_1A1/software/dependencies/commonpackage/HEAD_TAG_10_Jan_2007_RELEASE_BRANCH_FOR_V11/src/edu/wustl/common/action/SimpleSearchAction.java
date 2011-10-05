/**
 * <p>Description:	SimpleSearchAction takes the conditions from the user, prepares, 
 * executes the query and shows the result in a spreadsheet view.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.common.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.common.actionForm.SimpleQueryInterfaceForm;
import edu.wustl.common.beans.QueryResultObjectData;
import edu.wustl.common.bizlogic.QueryBizLogic;
import edu.wustl.common.bizlogic.SimpleQueryBizLogic;
import edu.wustl.common.dao.QuerySessionData;
import edu.wustl.common.dao.queryExecutor.PagenatedResultData;
import edu.wustl.common.factory.AbstractBizLogicFactory;
import edu.wustl.common.query.DataElement;
import edu.wustl.common.query.Query;
import edu.wustl.common.query.QueryFactory;
import edu.wustl.common.query.SimpleConditionsNode;
import edu.wustl.common.query.SimpleQuery;
import edu.wustl.common.query.Table;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * SimpleSearchAction takes the conditions from the user, prepares, 
 * executes the query and shows the result in a spreadsheet view.      
 * @author gautam_shetty
 */
public class SimpleSearchAction extends BaseAction
{

	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		SimpleQueryInterfaceForm simpleQueryInterfaceForm = (SimpleQueryInterfaceForm) form;
		// -------- set the selected menu ------- start
		String strMenu = simpleQueryInterfaceForm.getMenuSelected();
		request.setAttribute(Constants.MENU_SELECTED, strMenu);
		Logger.out.debug(Constants.MENU_SELECTED + " set in SimpleSearch Action : -- " + strMenu);
		// -------- set the selected menu ------- end
		HttpSession session = request.getSession();
 
		String target = Constants.SUCCESS;
		Map map = simpleQueryInterfaceForm.getValuesMap();
		String counter = simpleQueryInterfaceForm.getCounter();
		//Bug 1096:Creating a copy of the orginal query object for redefining the query object
		Map originalQueryObject = (Map) session
				.getAttribute(Constants.ORIGINAL_SIMPLE_QUERY_OBJECT);
		String originalCounter = (String) session
				.getAttribute(Constants.ORIGINAL_SIMPLE_QUERY_COUNTER);
		//If map from form is null get the map values from session.
		if (map.size() == 0)
		{
			map = (Map) session.getAttribute(Constants.SIMPLE_QUERY_MAP);
			//Get the counter from the simple query map if set during configure action in the session object
			counter = (String) map.get("counter");
			//After retrieving the value of counter, remove from the map
			map.remove("counter");
		}
		//Bug 1096:Set the simple query map attributes in a temp session object for redefining the query
		//Set the counter(number of rows in the UI of simple query formed) also in session, 
		//used for redefining the query
		if (originalQueryObject == null && originalCounter == null)
		{
			if (map != null && counter != null)
			{
				session.setAttribute(Constants.ORIGINAL_SIMPLE_QUERY_OBJECT, new HashMap(map));
				session.setAttribute(Constants.ORIGINAL_SIMPLE_QUERY_COUNTER, new String(counter));
			}
		}

		session.setAttribute(Constants.SIMPLE_QUERY_MAP, map);
		Logger.out.debug("map after setting in session" + map);

		MapDataParser parser = new MapDataParser("edu.wustl.common.query");
		Collection simpleConditionNodeCollection = parser.generateData(map, true);
		
		/**
		 * Bug-2778: Results should return at least the attribute that was queried.
		 * 
		 */
		List fieldList = new ArrayList();
		if(simpleConditionNodeCollection != null && !simpleConditionNodeCollection.isEmpty() ) 
		{
			Iterator itr = simpleConditionNodeCollection.iterator();
			while(itr.hasNext())
			{
				SimpleConditionsNode simpleConditionsNode = (SimpleConditionsNode) itr.next();
				Table table = simpleConditionsNode.getCondition().getDataElement().getTable();	
				DataElement dataElement = simpleConditionsNode.getCondition().getDataElement();
				String field = table.getTableName() + "." + dataElement.getField();				
				fieldList.add(field);
			}
		}

		Map queryResultObjectDataMap = new HashMap();

		SimpleQueryBizLogic simpleQueryBizLogic = new SimpleQueryBizLogic();

		// Get the alias name of the first object in the condition.
		String viewAliasName = (String) map
				.get("SimpleConditionsNode:1_Condition_DataElement_table");

		// Instantiating the query object.
		Query query = QueryFactory.getInstance().newQuery(Query.SIMPLE_QUERY, viewAliasName);
		List aliasList = new ArrayList(); // List containing the Alias name of the Table name of the first condition element.
		aliasList.add(viewAliasName);

		// Puts the single quotes for attributes of type string and date and
		// returns the Set of objects to which the attributes belong.
		List fromTablesList = new ArrayList();
		simpleQueryBizLogic.handleStringAndDateConditions(simpleConditionNodeCollection,
				fromTablesList);

		// Get the configured result view columns else is null.
		String[] selectedColumns = simpleQueryInterfaceForm.getSelectedColumnNames();
		if (selectedColumns != null)
			session.setAttribute(Constants.CONFIGURED_SELECT_COLUMN_LIST, selectedColumns);

		// Set the result view for the query.
		List columnNames = new ArrayList();
		Vector selectDataElements =null;
		
		/**
		 * Bug-2778: Results should return at least the attribute that was queried.
		 * If selectedColumns is null, then we are showing all the default view attribute
		 * plus the attibute that was queried. And if selectedColumns is not null, then we
		 * are showing only those attribute that are selected in Define View.
		 */
		if (selectedColumns != null)
		{
			selectDataElements = simpleQueryBizLogic.getSelectDataElements(selectedColumns,
					aliasList, columnNames, true, null);
		}
		else
		{
			selectDataElements = simpleQueryBizLogic.getSelectDataElements(selectedColumns,
				aliasList, columnNames, true, fieldList);
		}
		query.setResultView(selectDataElements);
       /**
        *  To fix bug 3449, scientist seeing PHI data
        */
		Set fromTables = new HashSet(); 
		
	
		Set tableSet = query.getTableSet();
		Iterator itr = tableSet.iterator();
		while(itr.hasNext()) 
		{
			Table table = (Table )itr.next(); 
			fromTables.add(table.getTableName());
		}
		
		//fromTables = new HashSet(); 
		fromTables.addAll(fromTablesList);
		fromTables.addAll(aliasList); 


		// Set the from tables in the query.
 		query.setTableSet(fromTables);

		// Checks and gets the activity status conditions for all the objects in
		// the from clause
		// and adds it in the simple conditions node collection.
		simpleQueryBizLogic.addActivityStatusConditions(simpleConditionNodeCollection, fromTables);

		simpleQueryBizLogic.createOrderByListInQuery(fromTables, query);
		
		// Sets the condition objects from user in the query object.
		((SimpleQuery) query).addConditions(simpleConditionNodeCollection);

		QueryBizLogic queryBizLogic = (QueryBizLogic)AbstractBizLogicFactory.getBizLogic(
            	ApplicationProperties.getValue("app.bizLogicFactory"),
				"getBizLogic", Constants.QUERY_INTERFACE_ID);
    
		int identifierIndex = 0;
		
		/**
		 * Name: Prafull
		 * Description: Query performance issue. Instead of saving complete query results in session, resultd will be fetched for each result page navigation.
		 * object of class QuerySessionData will be saved in session, which will contain the required information for query execution while navigating through query result pages.
		 * Added PagenatedResultData object, to transfer the results from the query/jdbcDao execute method.
		 */

		int recordsPerPage; 
		String recordsPerPageSessionValue = (String)session.getAttribute(Constants.RESULTS_PER_PAGE);
		if (recordsPerPageSessionValue==null)
		{
				recordsPerPage = Integer.parseInt(XMLPropertyHandler.getValue(Constants.RECORDS_PER_PAGE_PROPERTY_NAME));
				session.setAttribute(Constants.RESULTS_PER_PAGE, recordsPerPage+"");
		}
		else
			recordsPerPage = new Integer(recordsPerPageSessionValue).intValue();
		
		PagenatedResultData pagenatedResultData=null;
		boolean isSecureExecute = getSessionData(request).isSecurityRequired();
		boolean hasConditionOnIdentifiedField;
		/**
		 * Constants.SWITCHSECURIRY is removed from if statement
		 */
		
		simpleQueryBizLogic.createQueryResultObjectData(fromTables, queryResultObjectDataMap,
				query);

		List identifierColumnNames = new ArrayList();
		identifierColumnNames = simpleQueryBizLogic.addObjectIdentifierColumnsToQuery(
				queryResultObjectDataMap, query);
		simpleQueryBizLogic.setDependentIdentifiedColumnIds(queryResultObjectDataMap, query);

		//Aarti: adding other columns to the result view
		//			for (int i = 0; i < columnNames.size(); i++) {
		//				identifierColumnNames.add((String) columnNames.get(i));
		//			}

		for (int i = 0; i < identifierColumnNames.size(); i++)
		{
			columnNames.add((String) identifierColumnNames.get(i));
		}
		if(isSecureExecute)
		{
			
			queryBizLogic.insertQuery(query.getString(),getSessionData(request));
			hasConditionOnIdentifiedField = query.hasConditionOnIdentifiedField();
			
			/**
			 * Name: Prafull
			 * Description: Query performance issue. Instead of saving complete query results in session, resultd will be fetched for each result page navigation.
			 * object of class QuerySessionData will be saved session, which will contain the required information for query execution while navigating through query result pages.
			 * 
			 *  PagenatedResultData object will contain the query results  
			 */
			pagenatedResultData = query.execute(getSessionData(request), isSecureExecute, queryResultObjectDataMap, hasConditionOnIdentifiedField,0 ,recordsPerPage);
		}
		else
		{
			isSecureExecute = false;
			hasConditionOnIdentifiedField = false;
			pagenatedResultData = query.execute(getSessionData(request), false, null, false,0,recordsPerPage);
		}
	 	if (simpleQueryInterfaceForm.getPageOf().equals(Constants.PAGEOF_SIMPLE_QUERY_INTERFACE))
		{
			/**
			 * Added by Vijay. Check is added to decide hyperlink should be displayed or not, based on the variable isSecurityRequired of session dataBean
			 */
			//if(!isSecureExecute)
			{
				/**
				 * Name : Prafull_kadam
				 * Reviewer: Aarti_Sharma
				 * Patch ID: SimpleSearchEdit_1 
				 * Description: User should be able to Edit the Objects searched from Simple search. 
				 * For this Selected Colunms in the Query Results are shown as Heypelink, on clicking it user can edit that object.
				 */
				 // Creating & setting Hyperlink column map in request, which contains the information required for the Columns to be hyperlinked.
				Map<Integer, QueryResultObjectData> hyperlinkColumnMap = simpleQueryBizLogic.getHyperlinkMap(queryResultObjectDataMap, query.getResultView());
				/**
				 * Name : Prafull_kadam
				 * Patch ID: 4270_1
				 * Description: edit mode through simple search fails if records per page dropdown is changed. 
				 * 		Setting hyperlinkColumnMap in session instead of request, so that it will persiste when the records per page drop down changed or page number changed. In jsp same is retrived from session.
				 */
				session.setAttribute(Constants.HYPERLINK_COLUMN_MAP, hyperlinkColumnMap);
			}
		}
		else
		{
			// Get the index of Identifier field of main object.
			Vector tableAliasNames = new Vector();
			tableAliasNames.add(viewAliasName);
			Map tableMap = query.getIdentifierColumnIds(tableAliasNames);
			if (tableMap != null)
			{
				identifierIndex = Integer.parseInt(tableMap.get(viewAliasName).toString()) - 1;
				request
						.setAttribute(Constants.IDENTIFIER_FIELD_INDEX,
								new Integer(identifierIndex));
			}
			queryBizLogic.insertQuery(query.getString(),getSessionData(request));
			/**
			 * Name: Prafull
			 * Description: Query performance issue. Instead of saving complete query results in session, resultd will be fetched for each result page navigation.
			 * object of class QuerySessionData will be saved session, which will contain the required information for query execution while navigating through query result pages.
			 * 
			 *  PagenatedResultData object will contain the query results  
			 */

			
		}

		// List of results the query will return on execution.
		List list = pagenatedResultData.getResult();
		// If the result contains no data, show error message.
		if (list.isEmpty())
		{
			ActionErrors errors = new ActionErrors();
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("simpleQuery.noRecordsFound"));
			saveErrors(request, errors);

			String alias = (String) session.getAttribute(Constants.SIMPLE_QUERY_ALIAS_NAME);
			if (alias == null)
				alias = simpleQueryInterfaceForm.getAliasName();
			simpleQueryInterfaceForm.setValues(map);
			//remove the original session attributes for the query if the list is empty
			session.setAttribute(Constants.ORIGINAL_SIMPLE_QUERY_OBJECT, null);
			session.setAttribute(Constants.ORIGINAL_SIMPLE_QUERY_COUNTER, null);

			String path = Constants.SIMPLE_QUERY_INTERFACE_ACTION + "?" + Constants.PAGEOF + "="
					+ simpleQueryInterfaceForm.getPageOf() + "&" ;
			if(alias != null)
			{
				path = path + Constants.TABLE_ALIAS_NAME + "="	+ alias;
			}

			return getActionForward(Constants.SIMPLE_QUERY_NO_RESULTS, path);
		}
		else
		{
			// If the result contains only one row and the page is of edit
			// then show the result in the edit page.
			if ((list.size() == 1)
					&& (Constants.PAGEOF_SIMPLE_QUERY_INTERFACE.equals(simpleQueryInterfaceForm
							.getPageOf()) == false))
			{
				List rowList = (List) list.get(0);

				String path = Constants.SEARCH_OBJECT_ACTION + "?" + Constants.PAGEOF + "="
						+ simpleQueryInterfaceForm.getPageOf() + "&" + Constants.OPERATION + "="
						+ Constants.SEARCH + "&" + Constants.SYSTEM_IDENTIFIER + "="
						+ rowList.get(identifierIndex);

				
				if(simpleQueryInterfaceForm.getPageOf().equals("pageOfCollectionProtocol"))
				{
					path = "/RetrieveCollectionProtocol.do?"+ Constants.SYSTEM_IDENTIFIER + "="
					+ rowList.get(identifierIndex);
				}
				return getActionForward(Constants.SIMPLE_QUERY_SINGLE_RESULT, path);
			}
			else
			{
				/**
				 * Name: Prafull
				 * Description: Query performance issue. Instead of saving complete query results in session, resultd will be fetched for each result page navigation.
				 * object of class QuerySessionData will be saved session, which will contain the required information for query execution while navigating through query result pages.
				 * 
				 *  saving required query data in Session so that can be used later on while navigating through result pages using pagination.  
				 */
				QuerySessionData querySessionData = new QuerySessionData();
				querySessionData.setSql(query.getString());
				querySessionData.setQueryResultObjectDataMap(queryResultObjectDataMap);
				querySessionData.setSecureExecute(isSecureExecute);
				querySessionData.setHasConditionOnIdentifiedField(hasConditionOnIdentifiedField);
				querySessionData.setRecordsPerPage(recordsPerPage);
				querySessionData.setTotalNumberOfRecords(pagenatedResultData.getTotalRecords());
				
				session.setAttribute(Constants.QUERY_SESSION_DATA, querySessionData);
				
				// If results contain more than one result, show the spreadsheet
				// view.
				request.setAttribute(Constants.PAGEOF, simpleQueryInterfaceForm.getPageOf());
				request.setAttribute(Constants.SPREADSHEET_DATA_LIST, list);
				request.setAttribute(Constants.SPREADSHEET_COLUMN_LIST, columnNames);
			}
		}
		session.setAttribute(Constants.IS_SIMPLE_SEARCH, Constants.TRUE);
		return mapping.findForward(target);
	}

	private ActionForward getActionForward(String name, String path)
	{
		ActionForward actionForward = new ActionForward();
		actionForward.setName(name);
		actionForward.setPath(path);

		return actionForward;
	}

	//	public ActionForward executeAction(ActionMapping mapping, ActionForm
	// form,
	//			HttpServletRequest request, HttpServletResponse response)
	//			throws Exception {
	//		Logger.out.debug("SimpleSearchAction");
	//		SimpleQueryInterfaceForm simpleQueryInterfaceForm =
	// (SimpleQueryInterfaceForm) form;
	//		// String counter = simpleQueryInterfaceForm.getCounter();
	//		HttpSession session = request.getSession();
	//		// if(counter==null)
	//		// counter =
	//		// (String)session.getAttribute(Constants.SIMPLE_QUERY_COUNTER);
	//		// //Set form attributes in session for configuration after Search
	//		// else
	//		//
	// session.setAttribute(Constants.SIMPLE_QUERY_COUNTER,simpleQueryInterfaceForm.getCounter());
	//		Map map = null;
	//
	//		// Get the aliasName.
	//		String viewAliasName = (String) simpleQueryInterfaceForm
	//				.getValue("SimpleConditionsNode:1_Condition_DataElement_table");
	//
	//		if (viewAliasName == null) {
	//			map = (Map) session.getAttribute(Constants.SIMPLE_QUERY_MAP);
	//			viewAliasName = (String) map
	//					.get("SimpleConditionsNode:1_Condition_DataElement_table");
	//		} else
	//			session.setAttribute(Constants.SIMPLE_QUERY_ALIAS_NAME,
	//					viewAliasName);
	//
	//		Logger.out.debug("viewAliasName" + viewAliasName);
	//		String target = Constants.SUCCESS;
	//
	//		try {
	//			//If map from session is null get the map values from form
	//			if (map == null) {
	//				map = simpleQueryInterfaceForm.getValuesMap();
	//				session.setAttribute(Constants.SIMPLE_QUERY_MAP, map);
	//			}
	//			Logger.out.debug("map size" + map.size());
	//			MapDataParser parser = new MapDataParser(
	//					"edu.wustl.catissuecore.query");
	//
	//			Collection simpleConditionNodeCollection = parser.generateData(map,
	//					true);
	//			Iterator iterator = simpleConditionNodeCollection.iterator();
	//
	//			Set fromTables = new HashSet();
	//			Set aliasNameSet = new HashSet();
	//
	//			//Aarti: identifiers of the objects that need to be checked for
	//			// object level privileges.
	//// String[][] objectIdentifiers;
	//			
	//// //Aarti: Set of objects on which authorization needs to be checked
	//// Set tableSet = new HashSet();
	////
	//			Map queryResultObjectDataMap = new HashMap();
	//			
	//			
	//// //Aarti: Related tables map
	//// Map relatedTablesMap = new HashMap();
	//
	//			//Adding single quotes to strings and date values.
	//			SimpleConditionsNode simpleConditionsNode = null;
	//			int counter = 1;
	//
	//			QueryResultObjectData queryResultObjectData ;
	//			while (iterator.hasNext()) {
	//				
	//				// Get the tables in path for this field and add it in the
	//				// fromTables Set.
	//				
	//
	//				//Aarti: Getting the main object of condition and adding it to
	//				// fromTables
	//				String fromAliasNameKey = "SimpleConditionsNode:" + counter
	//						+ "_Condition_DataElement_table";
	//				String fromAliasNameValue = (String) map.get(fromAliasNameKey);
	//
	//				//Prepare a Set of table names.
	//				fromTables.add(fromAliasNameValue);
	//
	//// //Aarti: Also add it to TableSet
	//// tableSet.add(fromAliasNameValue);
	//				
	//				
	//				
	//
	//				simpleConditionsNode = (SimpleConditionsNode) iterator.next();
	//				String columnName = simpleConditionsNode.getCondition()
	//						.getDataElement().getField();
	//				StringTokenizer stringToken = new StringTokenizer(columnName,
	//						".");
	//				String conditionTableName = stringToken.nextToken();
	//				simpleConditionsNode.getCondition().getDataElement().setTable(
	//						conditionTableName);
	//
	//				simpleConditionsNode.getCondition().getDataElement().setField(
	//						stringToken.nextToken());
	//				String fieldType = stringToken.nextToken();
	//				String value = simpleConditionsNode.getCondition().getValue();
	//				String tableInPath = null;
	//				if (stringToken.hasMoreTokens()) {
	//					tableInPath = stringToken.nextToken();
	//				}
	//
	//				if (fieldType.equalsIgnoreCase(Constants.FIELD_TYPE_VARCHAR)
	//						|| fieldType
	//								.equalsIgnoreCase(Constants.FIELD_TYPE_DATE)
	//						|| fieldType
	//								.equalsIgnoreCase(Constants.FIELD_TYPE_TEXT)) {
	//					if (fieldType
	//							.equalsIgnoreCase(Constants.FIELD_TYPE_VARCHAR)) {
	//						value = "'" + value + "'";
	//					} else {
	//						value = "STR_TO_DATE('" + value + "','"
	//								+ Constants.MYSQL_DATE_PATTERN + "')";
	//					}
	//
	//					simpleConditionsNode.getCondition().setValue(value);
	//				}
	//
	//				if (tableInPath != null) {
	//					StringTokenizer tableInPathTokenizer = new StringTokenizer(
	//							tableInPath, ":");
	//					String aliasName = null;
	//
	//					while (tableInPathTokenizer.hasMoreTokens()) {
	//						Long tableId = Long.valueOf(tableInPathTokenizer
	//								.nextToken());
	//						QueryBizLogic bizLogic = (QueryBizLogic) BizLogicFactory
	//								.getBizLogic(Constants.SIMPLE_QUERY_INTERFACE_ID);
	//						aliasName = bizLogic.getAliasNameFromTableId(tableId);
	//						if (aliasName != null) {
	//							fromTables.add(aliasName);
	//						}
	//
	//						Logger.out
	//								.debug("aliasName for from Set**************************"
	//										+ aliasName);
	//					}
	//
	//				}
	//
	//				//Creating aliasName set with full package names.
	//				//Required for checking the activityStatus.
	//				aliasNameSet.add("edu.wustl.catissuecore.domain."
	//						+ simpleConditionsNode.getCondition().getDataElement()
	//								.getTable());
	//
	//				
	//				queryResultObjectData = createQueryResultObjectData(fromAliasNameValue);
	//				
	//				queryResultObjectDataMap.put(fromAliasNameValue,queryResultObjectData);
	//				counter++;
	//			}
	//
	//			//Add all the objects selected in UI to the fromtables Set.
	//			// for (int i=1;i<=Integer.parseInt(counter);i++)
	//			// {
	//			// String fromAliasNameKey =
	//			// "SimpleConditionsNode:"+i+"_Condition_DataElement_table";
	//			// String fromAliasNameValue = (String)map.get(fromAliasNameKey);
	//			//
	//			// //Prepare a Set of table names.
	//			// fromTables.add(fromAliasNameValue);
	//			// }
	//
	//			//Activity Status condition nodes.
	//			List activityStatusConditionList = new ArrayList();
	//			Iterator aliasNameIterator = aliasNameSet.iterator();
	//			while (aliasNameIterator.hasNext()) {
	//				String fullyQualifiedClassName = (String) aliasNameIterator
	//						.next();
	//				SimpleConditionsNode activityStatusCondition =
	// getActivityStatusCondition(fullyQualifiedClassName);
	//				if (activityStatusCondition != null) {
	//					activityStatusCondition.getOperator().setOperator(
	//							Constants.AND_JOIN_CONDITION);
	//					activityStatusConditionList.add(activityStatusCondition);
	//				}
	//			}
	//
	//			if (activityStatusConditionList.isEmpty() == false) {
	//				simpleConditionsNode.getOperator().setOperator(
	//						Constants.AND_JOIN_CONDITION);
	//				simpleConditionNodeCollection
	//						.addAll(activityStatusConditionList);
	//			}
	//
	//			// Forming the query.
	//			Query query = QueryFactory.getInstance().newQuery(
	//					Query.SIMPLE_QUERY, viewAliasName);
	//
	//			// Adding the condition objects from user.
	//			((SimpleQuery) query).addConditions(simpleConditionNodeCollection);
	//			List list = null;
	//
	//			// Get the view columns.
	//			List columnNames = null;
	//			
	//			
	//
	//			Iterator iterator1 = fromTables.iterator();
	//			while (iterator1.hasNext()) {
	//				Logger.out
	//						.debug("From tABLES before............................."
	//								+ iterator1.next());
	//			}
	//
	//			if (simpleQueryInterfaceForm.getPageOf().equals(
	//					Constants.PAGEOF_SIMPLE_QUERY_INTERFACE)) {
	//				String[] selectedColumns = simpleQueryInterfaceForm
	//						.getSelectedColumnNames();
	//
	//				//Set the result view for the query
	//				List tempColumnNames = getColumnNames(selectedColumns, query,
	//						fromTables);
	//
	//				//Getting the DataElement for select columns of the query.
	//				Vector selectDataElements = query.getResultView();
	//
	//				//Getting the aliasNames of the table ids in the tables in
	//				// path.
	//				Set forFromSet = configureSelectDataElements(
	//						selectDataElements, query);
	//				fromTables.addAll(forFromSet);
	//
	//				// iterator1 = fromTables.iterator();
	//				// while (iterator1.hasNext())
	//				// {
	//				// Logger.out.debug("From tABLES.
	//				// after............................"+iterator1.next());
	//				// }
	//
	//				// Set the from tables in the query.
	//				query.setTableSet(fromTables);
	//
	//				Logger.out.debug("Show tables Set..............");
	//				query.showTableSet();
	//
	//// if (!Constants.switchSecurity) {
	//// tableSet = new HashSet();
	//// }
	//
	//				//Aarti: Adding data elements corresponding to all
	//				// identifiers of all tables in tableSet to query
	//				//and setting their columnIds in object Identifiers array
	//				
	//// objectIdentifiers = new String[tableSet.size()][2];
	//// Iterator fromTablesIterator = tableSet.iterator();
	//// DataElement identifierDataElement;
	//// String tableName;
	//// for (int i = 0; i < tableSet.size(); i++) {
	//// tableName = (String) fromTablesIterator.next();
	//// objectIdentifiers[i][0] = tableName;
	//// objectIdentifiers[i][1] = String.valueOf(i);
	//// identifierDataElement = new DataElement(tableName,
	//// Constants.IDENTIFIER);
	//// query.addElementToView(i, identifierDataElement);
	//// columnNames.add(tableName + " ID");
	//// }
	//				
	//				columnNames = new ArrayList();
	//				columnNames = addObjectIdentifierColumnsToQuery(queryResultObjectDataMap,
	// query);
	//				setDependentIdentifiedColumnIds(queryResultObjectDataMap, query);
	//
	//				//Aarti: adding other columns to the result view
	//				for (int i = 0; i < tempColumnNames.size(); i++) {
	//					columnNames.add((String) tempColumnNames.get(i));
	//				}
	//
	//				//Setting column ids for the corresponding table Aliases.
	//// fromTablesIterator = tableSet.iterator();
	////// Map columnIdsMap = new HashMap();
	//// //Aarti: Get ColumnIds that map to the tables in tableSet
	//// // and map them in columnIdsMap
	//// for (int i = 0; i < tableSet.size(); i++) {
	//// tableName = (String) fromTablesIterator.next();
	//// columnIdsMap.put(tableName, query.getColumnIds(tableName,
	//// (Vector) (relatedTablesMap.get(tableName))));
	//// }
	//
	//				list = query.execute(getSessionData(request),
	//						queryResultObjectDataMap);
	//			} else {
	//				columnNames = query.setViewElements(fromTables);
	//
	//				//Getting the DataElement for select columns of the query.
	//				Vector selectDataElements = query.getResultView();
	//
	//				//Getting the aliasNames of the table ids in the tables in
	//				// path.
	//				Set forFromSet = configureSelectDataElements(
	//						selectDataElements, query);
	//				fromTables.addAll(forFromSet);
	//
	//				//Set the from tables in the query.
	//				query.setTableSet(fromTables);
	//
	//				// Logger.out.debug("Show tables Set..............");
	//				// query.showTableSet();
	//				//
	//				// iterator1 = fromTables.iterator();
	//				// while (iterator1.hasNext())
	//				// {
	//				// Logger.out.debug("From tABLES.
	//				// after............................"+iterator1.next());
	//				// }
	//
	//				list = query.execute(getSessionData(request),
	//						null);
	//			}
	//
	//			if (list.isEmpty()) {
	//				ActionErrors errors = new ActionErrors();
	//				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
	//						"simpleQuery.noRecordsFound"));
	//				saveErrors(request, errors);
	//				String alias = (String) session
	//						.getAttribute(Constants.SIMPLE_QUERY_ALIAS_NAME);
	//				if (alias == null)
	//					alias = simpleQueryInterfaceForm.getAliasName();
	//				String path = "SimpleQueryInterface.do?pageOf="
	//						+ simpleQueryInterfaceForm.getPageOf() + "&aliasName="
	//						+ alias;
	//				simpleQueryInterfaceForm.setValues(map);
	//
	//				RequestDispatcher requestDispatcher = request
	//						.getRequestDispatcher(path);
	//				requestDispatcher.forward(request, response);
	//			} else {
	//				if ((list.size() == 1)
	//						&& (Constants.PAGEOF_SIMPLE_QUERY_INTERFACE
	//								.equals(simpleQueryInterfaceForm.getPageOf()) == false)) {
	//					List rowList = (List) list.get(0);
	//					String action = "SearchObject.do?pageOf="
	//							+ simpleQueryInterfaceForm.getPageOf()
	//							+ "&operation=search&id="
	//							+ rowList.get(0);
	//
	//					RequestDispatcher requestDispatcher = request
	//							.getRequestDispatcher(action);
	//					requestDispatcher.forward(request, response);
	//				} else {
	//					request.setAttribute(Constants.PAGEOF,
	//							simpleQueryInterfaceForm.getPageOf());
	//					request.setAttribute(Constants.SPREADSHEET_DATA_LIST, list);
	//					request.setAttribute(Constants.SPREADSHEET_COLUMN_LIST,
	//							columnNames);
	//				}
	//			}
	//		} catch (DAOException daoExp) {
	//			Logger.out.debug(daoExp.getMessage(), daoExp);
	//			target = Constants.FAILURE;
	//		}
	//
	//		return mapping.findForward(target);
	//	}
	//
	//	// /**
	//	// * Returns true if the object named aliasName contains the
	// activityStatus
	//	// * data member, else returns false.
	//	// * @param aliasName
	//	// * @return
	//	// */
	//	// private boolean hasActivityStatus(String aliasName)
	//	// {
	//	// try
	//	// {
	//	// Class className = Class.forName("edu.wustl.catissuecore.domain." +
	//	// aliasName);
	//	//
	//	// if (className.equals(CollectionProtocol.class)
	//	// || className.equals(DistributionProtocol.class)
	//	// || className.equals(Specimen.class))
	//	// return true;
	//	//
	//	// Logger.out.debug("Class.................." + className.getName());
	//	// Field[] objectFields = className.getDeclaredFields();
	//	// Logger.out.debug("Field Size..........................." +
	//	// objectFields.length);
	//	// for (int i = 0; i < objectFields.length; i++)
	//	// {
	//	//
	// Logger.out.debug("objectFields[i].getName().............................."
	//	// + objectFields[i].getName());
	//	// if (objectFields[i].getName().equals(Constants.ACTIVITY_STATUS))
	//	// {
	//	// return true;
	//	// }
	//	// }
	//	// }
	//	// catch (ClassNotFoundException classNotExcp)
	//	// {
	//	// Logger.out.debug(classNotExcp.getMessage(), classNotExcp);
	//	// }
	//	//
	//	// return false;
	//	// }
	//
	//	// /**
	//	// * Returns SimpleConditionsNode if the object named aliasName contains
	// the
	//	// activityStatus
	//	// * data member, else returns false.
	//	// * @param aliasName
	//	// * @return
	//	// */
	//	// private List getActivityStatusCondition(String
	// fullyQualifiedClassName)
	//	// {
	//	// SimpleConditionsNode activityStatusCondition = null;
	//	// List activityStatusList = new ArrayList();
	//	//
	//	// try
	//	// {
	//	// Class className = Class.forName(fullyQualifiedClassName);
	//	// Field[] objectFields = className.getDeclaredFields();
	//	//
	//	// for (int i = 0; i < objectFields.length; i++)
	//	// {
	//	// if (objectFields[i].getName().equals(Constants.ACTIVITY_STATUS))
	//	// {
	//	//
	//	// activityStatusCondition = new SimpleConditionsNode();
	//	//
	// activityStatusCondition.getCondition().getDataElement().setTable(Utility.parseClassName(fullyQualifiedClassName));
	//	//
	// activityStatusCondition.getCondition().getDataElement().setField("ACTIVITY_STATUS");
	//	// activityStatusCondition.getCondition().getOperator().setOperator("=");
	//	// activityStatusCondition.getCondition().setValue("'" +
	//	// Constants.ACTIVITY_STATUS_ACTIVE + "'");
	//	//
	// activityStatusCondition.getOperator().setOperator(Constants.OR_JOIN_CONDITION);
	//	//
	//	// activityStatusList.add(activityStatusCondition);
	//	//
	//	// activityStatusCondition = new SimpleConditionsNode();
	//	//
	// activityStatusCondition.getCondition().getDataElement().setTable(Utility.parseClassName(fullyQualifiedClassName));
	//	//
	// activityStatusCondition.getCondition().getDataElement().setField("ACTIVITY_STATUS");
	//	// activityStatusCondition.getCondition().getOperator().setOperator("=");
	//	// activityStatusCondition.getCondition().setValue("'" +
	//	// Constants.ACTIVITY_STATUS_CLOSED + "'");
	//	//
	//	// activityStatusList.add(activityStatusCondition);
	//	// }
	//	// }
	//	//
	//	// if ((activityStatusCondition == null)&&
	//	// (className.getSuperclass().getName().equals(
	//	// "edu.wustl.catissuecore.domain.AbstractDomainObject") == false))
	//	// {
	//	// activityStatusList =
	// getActivityStatusCondition(className.getSuperclass()
	//	// .getName());
	//	// }
	//	// }
	//	//
	//	// catch (ClassNotFoundException classNotExcp)
	//	// {
	//	// Logger.out.debug(classNotExcp.getMessage(), classNotExcp);
	//	//
	//	// }
	//	//
	//	// return activityStatusList;
	//	//
	//	// }
	//
	//	/**
	//	 * @param fromAliasNameValue
	//	 * @return
	//	 * @throws DAOException
	//	 */
	//	private QueryResultObjectData createQueryResultObjectData(String
	// fromAliasNameValue) throws DAOException {
	//		QueryResultObjectData queryResultObjectData;
	//		queryResultObjectData = new QueryResultObjectData();
	//		queryResultObjectData.setAliasName(fromAliasNameValue);
	//		//Aarti: getting related tables that should be dependent
	//		//on main object for authorization
	//		Vector relatedTables = new Vector();
	//		relatedTables = QueryBizLogic
	//				.getRelatedTableAliases(fromAliasNameValue);
	//// Aarti: Get main query objects which should have individual checks
	//		//for authorization and should not be dependent on others
	//		Vector mainQueryObjects = QueryBizLogic.getMainObjectsOfQuery();
	//		
	//		String queryObject;
	//		//Aarti: remove independent query objects from related objects
	//		//vector and add them to tableSet so that their authorization
	//		//is checked individually
	//		for (int i = 0; i < mainQueryObjects.size(); i++) {
	//			queryObject = (String) mainQueryObjects.get(i);
	//			if (relatedTables.contains(queryObject)) {
	//				relatedTables.remove(queryObject);
	//// tableSet.add(queryObject);
	//				if(!queryObject.equals(fromAliasNameValue))
	//				{
	//					queryResultObjectData.addRelatedQueryResultObject(new
	// QueryResultObjectData(queryObject));
	//				}
	//			}
	//		}
	//		
	//		//Aarti: Map all related tables to the main table
	//// relatedTablesMap.put(fromAliasNameValue, relatedTables);
	//		queryResultObjectData.setDependentObjectAliases(relatedTables);
	//		return queryResultObjectData;
	//	}
	//
	//	/**
	//	 * @param queryResultObjectDataMap
	//	 * @param query
	//	 */
	//	private List addObjectIdentifierColumnsToQuery(Map
	// queryResultObjectDataMap, Query query) {
	//		DataElement identifierDataElement;
	//		
	//		List columnNames = new ArrayList();
	//		Set keySet = queryResultObjectDataMap.keySet();
	//		Iterator keyIterator = keySet.iterator();
	//		QueryResultObjectData queryResultObjectData2;
	//		QueryResultObjectData queryResultObjectData3;
	//		Vector queryObjects;
	//		Map columnIdsMap = new HashMap();
	//		int columnId =0;
	//		for(int i=0;keyIterator.hasNext();i++)
	//		{
	//			queryResultObjectData2 = (QueryResultObjectData)
	// queryResultObjectDataMap.get(keyIterator.next());
	//			queryObjects = queryResultObjectData2.getIndependentQueryObjects();
	//			for(int j = 0 ; j<queryObjects.size();j++)
	//			{
	//				queryResultObjectData3 = (QueryResultObjectData) queryObjects.get(j);
	//				identifierDataElement = new
	// DataElement(queryResultObjectData3.getAliasName(),
	//					Constants.IDENTIFIER);
	//				query.addElementToView(columnId, identifierDataElement);
	//				queryResultObjectData3.setIdentifierColumnId(columnId++);
	//				columnNames.add(queryResultObjectData3.getAliasName() + " ID");
	//			}
	//			
	//		}
	//		return columnNames;
	//	}
	//
	//	/**
	//	 * @param queryResultObjectDataMap
	//	 * @param query
	//	 */
	//	private void setDependentIdentifiedColumnIds(Map
	// queryResultObjectDataMap, Query query) {
	//		Iterator keyIterator;
	//		QueryResultObjectData queryResultObjectData2;
	//		QueryResultObjectData queryResultObjectData3;
	//		Vector queryObjects;
	//		Set keySet2 = queryResultObjectDataMap.keySet();
	//		keyIterator = keySet2.iterator();
	//		for(int i=0;keyIterator.hasNext();i++)
	//		{
	//			queryResultObjectData2 = (QueryResultObjectData)
	// queryResultObjectDataMap.get(keyIterator.next());
	//			queryObjects = queryResultObjectData2.getIndependentQueryObjects();
	//			for(int j = 0 ; j<queryObjects.size();j++)
	//			{
	//				queryResultObjectData3 = (QueryResultObjectData) queryObjects.get(j);
	//				queryResultObjectData3.setDependentColumnIds(query.getColumnIds(queryResultObjectData3.getAliasName(),queryResultObjectData3.getDependentObjectAliases()));
	//				queryResultObjectData3.setIdentifiedDataColumnIds(query.getIdentifiedColumnIds(queryResultObjectData3.getAliasName(),queryResultObjectData3.getDependentObjectAliases()));
	//				Logger.out.debug(" table:"+queryResultObjectData3.getAliasName()+"
	// columnIds:"+queryResultObjectData3.getDependentColumnIds());
	//			}
	//			
	//		}
	//	}
	//
	//	/**
	//	 * Returns SimpleConditionsNode if the object named aliasName contains the
	//	 * activityStatus data member, else returns false.
	//	 *
	//	 * @param aliasName
	//	 * @return
	//	 */
	//	private SimpleConditionsNode getActivityStatusCondition(
	//			String fullyQualifiedClassName) {
	//		SimpleConditionsNode activityStatusCondition = null;
	//
	//		//Returns the Class objet if it is a valid class else returns null.
	//		Class className = edu.wustl.common.util.Utility
	//				.getClassObject(fullyQualifiedClassName);
	//		if (className != null) {
	//			Field[] objectFields = className.getDeclaredFields();
	//
	//			for (int i = 0; i < objectFields.length; i++) {
	//				if (objectFields[i].getName().equals(Constants.ACTIVITY_STATUS)) {
	//					activityStatusCondition = new SimpleConditionsNode();
	//					activityStatusCondition
	//							.getCondition()
	//							.getDataElement()
	//							.setTable(
	//									Utility
	//											.parseClassName(fullyQualifiedClassName));
	//					activityStatusCondition.getCondition().getDataElement()
	//							.setField("ACTIVITY_STATUS");
	//					activityStatusCondition.getCondition().getOperator()
	//							.setOperator("!=");
	//					activityStatusCondition.getCondition().setValue(
	//							"'" + Constants.ACTIVITY_STATUS_DISABLED + "'");
	//				}
	//			}
	//
	//			if ((activityStatusCondition == null)
	//					&& (className
	//							.getSuperclass()
	//							.getName()
	//							.equals(
	//									"edu.wustl.catissuecore.domain.AbstractDomainObject") == false)) {
	//				activityStatusCondition = getActivityStatusCondition(className
	//						.getSuperclass().getName());
	//			}
	//		}
	//
	//		return activityStatusCondition;
	//	}
	//
	//	private Vector setViewElements(String[] selectedColumnsList) {
	//		/*
	//		 * Split the string which is in the form
	//		 * TableAlias.columnNames.columnDisplayNames and set the dataelement
	//		 * object.
	//		 */
	//		Vector vector = new Vector();
	//		for (int i = 0; i < selectedColumnsList.length; i++) {
	//			StringTokenizer st = new StringTokenizer(selectedColumnsList[i],
	//					".");
	//			DataElement dataElement = new DataElement();
	//			while (st.hasMoreTokens()) {
	//				dataElement.setTable(st.nextToken());
	//				String field = st.nextToken();
	//				Logger.out.debug(st.nextToken());
	//				String tableInPath = null;
	//				if (st.hasMoreTokens()) {
	//					tableInPath = st.nextToken();
	//					field = field + "." + tableInPath;
	//					Logger.out.debug("Field with the table id......." + field);
	//				}
	//				dataElement.setField(field);
	//			}
	//			vector.add(dataElement);
	//		}
	//		return vector;
	//	}
	//
	//	private List getColumnDisplayNames(String[] selectedColumnsList) {
	//		/*
	//		 * Split the string which is in the form
	//		 * TableAlias.columnNames.columnDisplayNames and set the dataelement
	//		 * object.
	//		 */
	//		List columnDisplayNames = new ArrayList();
	//		for (int i = 0; i < selectedColumnsList.length; i++) {
	//			StringTokenizer st = new StringTokenizer(selectedColumnsList[i],
	//					".");
	//			DataElement dataElement = new DataElement();
	//			while (st.hasMoreTokens()) {
	//				st.nextToken();
	//				st.nextToken();
	//				String displayName = st.nextToken();
	//				columnDisplayNames.add(displayName);
	//				Logger.out.debug("columnDisplayNames" + displayName);
	//				if (st.hasMoreTokens())
	//					st.nextToken();
	//			}
	//		}
	//		return columnDisplayNames;
	//	}
	//
	//	private List getColumnNames(String[] selectedColumns, Query query,
	//			Set tableSet) throws DAOException {
	//		//set the result view for the query
	//		List tempColumnNames;
	//
	//		//If columns not conigured, set to default
	//		if (selectedColumns == null)
	//			tempColumnNames = query.setViewElements(tableSet);
	//		//else set to the configured columns
	//		else {
	//			Vector resultViewVector = setViewElements(selectedColumns);
	//			query.setResultView(resultViewVector);
	//			tempColumnNames = getColumnDisplayNames(selectedColumns);
	//		}
	//		return tempColumnNames;
	//	}
	//
	//	private Set configureSelectDataElements(Vector selectDataElements,
	//			Query query) throws DAOException {
	//		Set forFromSet = new HashSet();
	//
	//		Iterator iterator = selectDataElements.iterator();
	//		QueryBizLogic bizLogic = (QueryBizLogic) BizLogicFactory
	//				.getBizLogic(Constants.SIMPLE_QUERY_INTERFACE_ID);
	//		while (iterator.hasNext()) {
	//			DataElement dataElement = (DataElement) iterator.next();
	//			String fieldName = dataElement.getField();
	//			Logger.out
	//					.debug("Field with the table id in configureSelectDataElements......."
	//							+ fieldName);
	//			StringTokenizer stringToken = new StringTokenizer(dataElement
	//					.getField(), ".");
	//			dataElement.setField(stringToken.nextToken());
	//			//
	// Logger.out.debug("..........................."+stringToken.nextToken());
	//
	//			if (stringToken.hasMoreElements()) {
	//				StringTokenizer pathIdToken = new StringTokenizer(stringToken
	//						.nextToken(), ":");
	//				while (pathIdToken.hasMoreTokens()) {
	//					Long tableId = Long.valueOf(pathIdToken.nextToken());
	//					String aliasName = bizLogic
	//							.getAliasNameFromTableId(tableId);
	//					Logger.out
	//							.debug("configureSelectDataElements..............aliasName.."
	//									+ aliasName);
	//					forFromSet.add(aliasName);
	//				}
	//			}
	//		}
	//		query.setResultView(selectDataElements);
	//
	//		return forFromSet;
	//	}*/
}