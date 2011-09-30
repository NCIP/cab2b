/*
 * Created on Nov 30, 2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package edu.wustl.common.util.global;

import java.util.HashMap;

/**
 * This classes is specific to common files. And contains all variables used by classes from
 * common package.
 * @author gautam_shetty
 * */
public class Constants 
{
	
	//	constants for TiTLi Search
	public static final String TITLI_SORTED_RESULT_MAP="sortedResultMap";
	public static final String TITLI_INSERT_OPERATION="insert";
	public static final String TITLI_UPDATE_OPERATION="update";
	public static final String TITLI_DELETE_OPERATION="delete";
	public static final String TITLI_SINGLE_RESULT="singleResult";
	public static final String TITLI_FETCH_ACTION="/TitliFetch.do";
	
	
	// constants for passwordManager
	
	public static final String MINIMUM_PASSWORD_LENGTH = "minimumPasswordLength";
		
	public static final String SELECT_OPTION = "-- Select --";
	public static final int SELECT_OPTION_VALUE = -1;
	public static final String CDE_CONF_FILE = "CDEConfig.xml";
	public static final String ANY = "Any";
	public static final String DELIMETER = ",";
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	public static final String TAB_DELIMETER = "\t";
			
	//	Misc
	public static final String SEPARATOR = " : ";
	
	public static final String TIMESTAMP_PATTERN = "yyyy-MM-dd-HH24.mm.ss.SSS";
	
	public static final  HashMap<String, String[]> 
			STATIC_PROTECTION_GROUPS_FOR_OBJECT_TYPES = new HashMap<String, String[]>();
	
	// Mandar: Used for Date Validations in Validator Class
	public static final String DATE_SEPARATOR = "-";
	public static final String DATE_SEPARATOR_SLASH = "/";
	public static final String MIN_YEAR = "1900";
	public static final String MAX_YEAR = "9999";
	
	//Activity Status values
	public static final String ACTIVITY_STATUS_ACTIVE = "Active";
	
	public static final String ADD = "add";
	
	public static final String getCollectionProtocolPGName(Long identifier)
	{
	    if(identifier == null)
	    {
	        return "COLLECTION_PROTOCOL_";
	    }
	    return "COLLECTION_PROTOCOL_"+identifier;
	}
	
	public static final String getCollectionProtocolPIGroupName(Long identifier)
	{
	    if(identifier == null)
	    {
	        return "PI_COLLECTION_PROTOCOL_";
	    }
	    return "PI_COLLECTION_PROTOCOL_"+identifier;
	}
	
	public static final String getCollectionProtocolCoordinatorGroupName(Long identifier)
	{
	    if(identifier == null)
	    {
	        return "COORDINATORS_COLLECTION_PROTOCOL_";
	    }
	    return "COORDINATORS_COLLECTION_PROTOCOL_"+identifier;
	}
	
	public static final String getDistributionProtocolPGName(Long identifier)
	{
	    if(identifier == null)
	    {
	        return "DISTRIBUTION_PROTOCOL_";
	    }
	    return "DISTRIBUTION_PROTOCOL_"+identifier;
	}
	 
	public static final String getDistributionProtocolPIGroupName(Long identifier)
	{
	    if(identifier == null)
	    {
	        return "PI_DISTRIBUTION_PROTOCOL_";
	    }
	    return "PI_DISTRIBUTION_PROTOCOL_"+identifier;
	}
	
	public static final String getStorageContainerPGName()
	{
	    return "USER_";
	}
	
	public static final String getSitePGName(Long identifier)
	{
	    if(identifier == null)
	    {
	        return "SITE_";
	    }
	    return "SITE_"+identifier;
	}
	
	
	public static final String COLLECTION_PROTOCOL_CLASS_NAME = "edu.wustl.catissuecore.domain.CollectionProtocol";//CollectionProtocol.class.getName();
	public static final String DISTRIBUTION_PROTOCOL_CLASS_NAME = "edu.wustl.catissuecore.domain.DistributionProtocol";//DistributionProtocol.class.getName();
	// Aarti: Constants for security parameter required 
	// while retrieving data from DAOs
	public static final int INSECURE_RETRIEVE = 0;
	public static final int CLASS_LEVEL_SECURE_RETRIEVE = 1; 
	public static final int OBJECT_LEVEL_SECURE_RETRIEVE = 2; 
	
	public static final String CATISSUE_SPECIMEN = "CATISSUE_SPECIMEN";
	
	// Constants used for authentication module.
    public static final String LOGIN = "login";
    public static final String LOGOUT = "logout";
    
    public static final String OPERATION = "operation";
    
    // Constants for HTTP-API
    public static final String HTTP_API = "HTTPAPI";
	
    public static final String SUCCESS = "success";
	public static final String FAILURE = "failure";
	
	public static final String SYSTEM_IDENTIFIER = "id";
	
	// User Roles
	public static final String ADMINISTRATOR = "Administrator";
	
	//Assign Privilege Constants.
	public static final boolean PRIVILEGE_ASSIGN = true;
	
	//DAO Constants.
	public static final int HIBERNATE_DAO = 1;
	public static final int JDBC_DAO = 2;
	
	public static final String  ORACLE_DATABASE = "ORACLE";
	public static final String  MYSQL_DATABASE = "MYSQL";
	public static final String  POSTGRESQL_DATABASE = "POSTGRESQL";
	public static final String  DB2_DATABASE = "DB2";
	
	// The unique key voilation message is "Duplicate entry %s for key %d"
	// This string is used for searching " for key " string in the above error message
	public static final String MYSQL_DUPL_KEY_MSG = " for key ";
	
	public static final String GENERIC_DATABASE_ERROR = "An error occurred during a database operation. Please report this problem to the administrator";
	public static final String CONSTRAINT_VOILATION_ERROR = "Submission failed since a {0} with the same {1} already exists";
	public static final String OBJECT_NOT_FOUND_ERROR = "Submission failed since a {0} with given {1}: \"{2}\" does not exists";
	
	public static final String ACTIVITY_STATUS_DISABLED = "Disabled";
	public static final String ACTIVITY_STATUS_CLOSED = "Closed";
	
	public static final String AND_JOIN_CONDITION = "AND";
	public static final String OR_JOIN_CONDITION = "OR";
	public static final String ACTIVITY_STATUS = "activityStatus";
	
	public static final boolean switchSecurity = true;
	
	public static final String EDIT = "edit";
	
	public static final String DATE_PATTERN_MM_DD_YYYY = "MM-dd-yyyy";
	
	//Constants for audit of disabled objects.
	public static final String UPDATE_OPERATION = "UPDATE";
	public static final String ACTIVITY_STATUS_COLUMN = "ACTIVITY_STATUS";
	
	//Constants for Summary Page
	public static final String TISSUE = "Tissue";
	public static final String MOLECULE = "Molecular";
	public static final String CELL = "Cell";
	public static final String FLUID = "Fluid";
	
	//Tree View constants.
	public static final String TISSUE_SITE = "Tissue Site";
	public static final String CLINICAL_DIAGNOSIS="Clinical Diagnosis";
	public static final int TISSUE_SITE_TREE_ID = 1;
	public static final int STORAGE_CONTAINER_TREE_ID = 2;
	public static final int QUERY_RESULTS_TREE_ID = 3;
	public static final String ROOT = "Root";
	public static final String CATISSUE_CORE = "caTissue Core";
	
	//Mandar : CDE xml package path.
	public static final String CDE_XML_PACKAGE_PATH = "edu.wustl.common.cde.xml";
	public static final String BOOLEAN_YES = "Yes";
	public static final String BOOLEAN_NO = "No";
	
	public static final String SESSION_DATA = "sessionData";
	public static final String TEMP_SESSION_DATA = "tempSessionData";
	public static final String ACCESS = "access";
	public static final String PASSWORD_CHANGE_IN_SESSION = "changepassword";
	
	public static final String USER_CLASS_NAME = "edu.wustl.common.domain.User";
	
	public static final String IDENTIFIER = "IDENTIFIER";
	
	public static final String FIELD_TYPE_BIGINT = "bigint";
	public static final String FIELD_TYPE_VARCHAR = "varchar";
	public static final String FIELD_TYPE_TEXT = "text";
	public static final String FIELD_TYPE_TINY_INT = "tinyint";
	public static final String FIELD_TYPE_DATE = "date";
	public static final String FIELD_TYPE_TIMESTAMP_DATE = "timestampdate";
	
	public static final String TABLE_ALIAS_NAME_COLUMN = "ALIAS_NAME";
	public static final String TABLE_DATA_TABLE_NAME = "CATISSUE_QUERY_TABLE_DATA";
	public static final String TABLE_DISPLAY_NAME_COLUMN = "DISPLAY_NAME";
	
	public static final String TABLE_FOR_SQI_COLUMN = "FOR_SQI";
	
	public static final String TABLE_ID_COLUMN = "TABLE_ID";
	
	public static final String NULL = "NULL";
	
	public static final String CONDITION_VALUE_YES = "yes";
	
	public static final String TINY_INT_VALUE_ONE = "1";
	public static final String TINY_INT_VALUE_ZERO = "0";
	
	public static final String FIELD_TYPE_TIMESTAMP_TIME = "timestamptime";
	
	public static final String CDE_NAME_TISSUE_SITE = "Tissue Site";
	
	public static final String UPPER = "UPPER";
	
	public static final String PARENT_SPECIMEN_ID_COLUMN = "PARENT_SPECIMEN_ID";
	
	// Query results view temporary table name.
	public static final String QUERY_RESULTS_TABLE = "CATISSUE_QUERY_RESULTS";
	
	public static final String TIME_PATTERN_HH_MM_SS = "HH:mm:ss";
	
	public static final int SIMPLE_QUERY_INTERFACE_ID = 40;
	
	// -- menu selection related
	public static final String MENU_SELECTED = "menuSelected";
	
	public static final String SIMPLE_QUERY_MAP = "simpleQueryMap";
	
	public static final String IDENTIFIER_FIELD_INDEX = "identifierFieldIndex";
	/*
	 * Patch ID: SimpleSearchEdit_3 
	 * Description: Constants required for this feature plus Delimeter used in the DHTML grid.
	*/
	public static final String HYPERLINK_COLUMN_MAP = "hyperlinkColumnMap";
	public static final String DHTMLXGRID_DELIMETER = "|@|"; 
	public static final String PAGEOF_SIMPLE_QUERY_INTERFACE = "pageOfSimpleQueryInterface";
	public static final String SIMPLE_QUERY_ALIAS_NAME = "simpleQueryAliasName";
	
	public static final String SIMPLE_QUERY_INTERFACE_ACTION = "/SimpleQueryInterface.do";
	
	public static final String PAGEOF = "pageOf";
	public static final String STORAGE_CONTAINER = "storageContainerName";
	public static final String TABLE_ALIAS_NAME = "aliasName";
	public static final String SIMPLE_QUERY_NO_RESULTS = "noResults";
	public static final String SEARCH_OBJECT_ACTION = "/SearchObject.do";
	
	public static final String SEARCH = "search";
	public static final String IS_SIMPLE_SEARCH = "isSimpleSearch";
	// SimpleSearchAction
	public static final String SIMPLE_QUERY_SINGLE_RESULT = "singleResult";
	
	public static final String SPREADSHEET_DATA_LIST = "spreadsheetDataList";
	public static final String SPREADSHEET_COLUMN_LIST = "spreadsheetColumnList";
	public static final String QUERY_SESSION_DATA = "querySessionData";
	
	public static final String ACCESS_DENIED = "access_denied";
	
	public static final String ADVANCED_CONDITION_NODES_MAP = "advancedConditionNodesMap";
	public static final String ADVANCED_CONDITIONS_ROOT = "advancedCondtionsRoot";
	
	public static final String TREE_VECTOR = "treeVector";
	public static final String SELECT_COLUMN_LIST = "selectColumnList";
	public static final String SELECTED_NODE = "selectedNode";
	
	//Individual view Constants in DataViewAction.
	public static final String CONFIGURED_COLUMN_DISPLAY_NAMES = "configuredColumnDisplayNames";
	public static final String CONFIGURED_COLUMN_NAMES = "configuredColumnNames";
	public static final String CONFIGURED_SELECT_COLUMN_LIST = "configuredSelectColumnList";
	
	public static final String COLUMN_DISPLAY_NAMES = "columnDisplayNames";
	
	public static final String COLUMN_ID_MAP = "columnIdsMap";
	public static final String PAGEOF_ADVANCE_QUERY_INTERFACE = "pageOfAdvanceQueryInterface";
	public static final String PAGEOF_QUERY_RESULTS = "pageOfQueryResults";
	
	public static final String COLUMN = "Column";
	
	public static final String ATTRIBUTE_NAME_LIST = "attributeNameList";
	public static final String ATTRIBUTE_CONDITION_LIST = "attributeConditionList";
	
	public static final String[] ATTRIBUTE_NAME_ARRAY = {
	        SELECT_OPTION
	};
	
	public static final String[] ATTRIBUTE_CONDITION_ARRAY = {
	        "=","<",">"
	};
	
	//For Simple Query Interface
	public static final int SIMPLE_QUERY_TABLES = 1;
	public static final String OBJECT_NAME_LIST = "objectNameList";
	
	public static final String ACCESS_DENIED_ADMIN = "access_denied_admin";
	public static final String ACCESS_DENIED_BIOSPECIMEN = "access_denied_biospecimen";
	
	// Constants for type of query results view.
	public static final String SPREADSHEET_VIEW = "Spreadsheet View";
	public static final String OBJECT_VIEW = "Edit View";
	
	public static final String COLLECTION_PROTOCOL ="CollectionProtocol";
	
	// Frame names in Query Results page.
	public static final String DATA_VIEW_FRAME = "myframe1";
	public static final String APPLET_VIEW_FRAME = "appletViewFrame";
	
	// NodeSelectionlistener - Query Results Tree node selection (For spreadsheet or individual view).
	public static final String DATA_VIEW_ACTION = "DataView.do?nodeName=";
	public static final String VIEW_TYPE = "viewType";
	
	// TissueSite Tree View Constants.
	public static final String PROPERTY_NAME = "propertyName";
	
	// For Tree Applet
	public static final String PAGEOF_STORAGE_LOCATION = "pageOfStorageLocation";
	public static final String PAGEOF_SPECIMEN = "pageOfSpecimen";
	public static final String PAGEOF_STORAGECONTAINER="pageOfStorageContainer";
	public static final String PAGEOF_TISSUE_SITE = "pageOfTissueSite";
	public static final String PAGEOF_MULTIPLE_SPECIMEN = "pageOfMultipleSpecimen";
	//Added By Ramya
	public static final String PAGEOF_SPECIMEN_TREE = "pageOfSpecimenTree";
	
	//Added By Ramya
	//Constants to display Specimen Tree in RequestDetails.jsp
	public static final int SPECIMEN_TREE_ID = 4;
	public static final String SPECIMEN_TYPE = "type";	
	public static final String SPECIMEN_CLASS= "specimenClass";
	public static final String SPECIMEN_TREE_ROOT_NAME = "Specimens";
	
	// Experiment Module
	public static final int EXPERIMETN_TREE_ID = 5;
	
	// Constants for Storage Container.
	public static final String STORAGE_CONTAINER_TYPE = "storageType";
	public static final String STORAGE_CONTAINER_TO_BE_SELECTED = "storageToBeSelected";
	public static final String STORAGE_CONTAINER_POSITION = "position";
	
	public static final String CDE_NAME = "cdeName";
	
	// Tree Data Action
	public static final String TREE_DATA_ACTION = "Data.do";
	public static final String SHOW_STORAGE_CONTAINER_GRID_VIEW_ACTION = "ShowStorageGridView.do";
	
	public static final String TREE_APPLET_NAME = "treeApplet";
	
	//	 for Add New
	public static final String ADD_NEW_STORAGE_TYPE_ID ="addNewStorageTypeId";
	public static final String ADD_NEW_COLLECTION_PROTOCOL_ID ="addNewCollectionProtocolId";
	public static final String ADD_NEW_SITE_ID ="addNewSiteId";
	public static final String ADD_NEW_USER_ID ="addNewUserId";
	public static final String ADD_NEW_USER_TO ="addNewUserTo";
	public static final String SUBMITTED_FOR = "submittedFor";
	public static final String SUBMITTED_FOR_ADD_NEW = "AddNew";
	public static final String SUBMITTED_FOR_FORWARD_TO = "ForwardTo";
	public static final String SUBMITTED_FOR_DEFAULT = "Default";
	public static final String FORM_BEAN_STACK= "formBeanStack";
	public static final String ADD_NEW_FORWARD_TO ="addNewForwardTo";
	public static final String FORWARD_TO = "forwardTo";
	public static final String ADD_NEW_FOR = "addNewFor";

	public static final String ERROR_DETAIL = "Error Detail";
	
	//Identifiers for various Form beans
	public static final int QUERY_INTERFACE_ID = 43;
	
	//Status message key Constants
	public static final String STATUS_MESSAGE_KEY = "statusMessageKey";
		
	//Constant for redefine operation for Advance and Simple Query
	public static final String REDEFINE = "redefine";	
	public static final String ORIGINAL_SIMPLE_QUERY_OBJECT = "originalSimpleQueryObject";
	public static final String ORIGINAL_SIMPLE_QUERY_COUNTER = "counter";
	public static final String SIMPLE_QUERY_COUNTER = "counter";
/***  Added New Constansts  ***/
	
//	Activity Status values
	public static final String ACTIVITY_STATUS_APPROVE = "Approve";
	public static final String ACTIVITY_STATUS_REJECT = "Reject";
	public static final String ACTIVITY_STATUS_NEW = "New";
	public static final String ACTIVITY_STATUS_PENDING = "Pending";
	
	//Approve User status values.
	public static final String APPROVE_USER_APPROVE_STATUS = "Approve";
	public static final String APPROVE_USER_REJECT_STATUS = "Reject";
	public static final String APPROVE_USER_PENDING_STATUS = "Pending";

//	Identifiers for various Form beans
	public static final int USER_FORM_ID = 1;
	public static final int ACCESSION_FORM_ID = 3;
	public static final int REPORTED_PROBLEM_FORM_ID = 4;
	public static final int INSTITUTION_FORM_ID = 5;
	public static final int APPROVE_USER_FORM_ID = 6;
	public static final int ACTIVITY_STATUS_FORM_ID = 7;
	public static final int DEPARTMENT_FORM_ID = 8;
	public static final int CANCER_RESEARCH_GROUP_FORM_ID = 14;
	public static final int FORGOT_PASSWORD_FORM_ID = 35;
	public static final int SIGNUP_FORM_ID = 36;
	public static final int DISTRIBUTION_FORM_ID = 37;
	
//	Query Interface Results View Constants
	public static final String QUERY = "query";
	public static final String PAGEOF_APPROVE_USER = "pageOfApproveUser";
	public static final String PAGEOF_SIGNUP = "pageOfSignUp";
	public static final String PAGEOF_USERADD = "pageOfUserAdd";
	public static final String PAGEOF_USER_ADMIN = "pageOfUserAdmin";
	public static final String PAGEOF_USER_PROFILE = "pageOfUserProfile";
	public static final String PAGEOF_CHANGE_PASSWORD = "pageOfChangePassword";
	
//	Approve User Constants
	public static final int ZERO = 0;
	public static final int START_PAGE = 1;
	public static final int NUMBER_RESULTS_PER_PAGE = 5;
	public static final String PAGE_NUMBER = "pageNum";
	public static final String RESULTS_PER_PAGE = "numResultsPerPage";
	public static final String RECORDS_PER_PAGE_PROPERTY_NAME="resultView.noOfRecordsPerPage";
	public static final String TOTAL_RESULTS = "totalResults";
	public static final String PREVIOUS_PAGE = "prevpage";
	public static final String NEXT_PAGE = "nextPage";
	public static final String ORIGINAL_DOMAIN_OBJECT_LIST = "originalDomainObjectList";
	public static final String SHOW_DOMAIN_OBJECT_LIST = "showDomainObjectList";
	public static final String USER_DETAILS = "details";
	public static final String CURRENT_RECORD = "currentRecord";
	public static final String APPROVE_USER_EMAIL_SUBJECT = "Your membership status in caTISSUE Core.";
	
	// Constants required in UserBizLogic
	public static final int DEFAULT_BIZ_LOGIC = 0;
	
	public static final String [] USER_ACTIVITY_STATUS_VALUES = {
        SELECT_OPTION,
        "Active",
        "Closed"
	};
	
	public static final String CDE_NAME_COUNTRY_LIST = "Countries";
	public static final String CDE_NAME_STATE_LIST = "States";

    /**
     * @param id
     * @return
     */
    public static String getUserPGName(Long identifier)
    {
        if(identifier == null)
	    {
	        return "USER_";
	    }
	    return "USER_"+identifier;
    }

    /**
     * @param id
     * @return
     */
    public static String getUserGroupName(Long identifier)
    {
        if(identifier == null)
	    {
	        return "USER_";
	    }
	    return "USER_"+identifier;
    }
	
//  Constants required for Forgot Password
	public static final String FORGOT_PASSWORD = "forgotpassword";
	
	public static final String LOGINNAME = "loginName";
	public static final String LASTNAME = "lastName";
	public static final String FIRSTNAME = "firstName";
	public static final String INSTITUTION = "institution";
	public static final String EMAIL = "email";
	public static final String DEPARTMENT = "department";
	public static final String ADDRESS = "address";
	public static final String CITY = "city";
	public static final String STATE = "state";
	public static final String COUNTRY = "country";
	public static final String NEXT_CONTAINER_NO = "startNumber";
	public static final String CSM_USER_ID = "csmUserId";
	
	public static final String REPORTED_PROBLEM_CLASS_NAME = "edu.wustl.catissuecore.domain.ReportedProblem";
	public static final String PARTICIPANT = "Participant";
	public static final String SPECIMEN = "Specimen";
	public static final String SPECIMEN_COLLECTION_GROUP ="SpecimenCollectionGroup";
	public static final String ALL = "All";
	public static final int[] RESULT_PERPAGE_OPTIONS = {10,50,100,500,Integer.MAX_VALUE};
	public static final String PAGEOF_ALIQUOT = "pageOfAliquot";
	public static final String ISCHECKPERMISSION="isToCheckCSMPermission";
	 
	public static final int HASH_PRIME = 7;
	public static final String QUERY_SQL = "querySQL";

	public static final String PRIVILEGE_TAG_NAME = "PRIVILEGE_ID";
	public static final String BIRTH_DATE_TAG_NAME = "IS_BIRTH_DATE";
	
	//changes for titli
	public static final String TABLE_TABLE_NAME_COLUMN = "TABLE_NAME";
	
	public static final String EXPRESSION_ID_SEPARATOR = ":";
	
	public static final String 	CP_CLASS_NAME = "edu.wustl.catissuecore.domain.CollectionProtocol";
	public static final String EXPORT_FILE_NAME_START = "Report_Content_";
       
    public static final String SECURITY_MANAGER_PROP_FILE = "SecurityManager.properties";
    public static final String APPLN_CONTEXT_NAME = "application.context.name";
    public static final String SECURITY_MANAGER_CLASSNAME = "class.name";
    
    public static final String MAIN_PROTOCOL_OBJECT = "mainprotocolobject.classname";
    public static final String READ_DENIED_OBJECTS = "readdeniedobjects";
    public static final String CSM_PROPERTY_FILE = "csm.properties";
    public static final String ROLE_ADMINISTRATOR="Administrator";
    public static final String TECHNICIAN = "Technician";
	public static final String SUPERVISOR = "Supervisor";
	public static final String SCIENTIST = "Scientist";
	public static final String ROLE_SUPER_ADMINISTRATOR="SUPERADMINISTRATOR";
	
	public static final String SUPER_ADMINISTRATOR_ROLE = "SUPER_ADMINISTRATOR_ROLE";
	public static final String ADMINISTRATOR_ROLE = "ADMINISTRATOR_ROLE";
	public static final String SUPERVISOR_ROLE = "SUPERVISOR_ROLE";
	public static final String TECHNICIAN_ROLE = "TECHNICIAN_ROLE";
	public static final String PUBLIC_ROLE = "PUBLIC_ROLE";
	public static final String ADMINISTRATOR_GROUP_ID = "ADMINISTRATOR_GROUP_ID";
	public static final String SUPERVISOR_GROUP_ID = "SUPERVISOR_GROUP_ID";
	public static final String TECHNICIAN_GROUP_ID = "TECHNICIAN_GROUP_ID";
	public static final String PUBLIC_GROUP_ID = "PUBLIC_GROUP_ID";
	public static final String SUPER_ADMINISTRATOR_GROUP_ID = "SUPER_ADMINISTRATOR_GROUP_ID";
	public static final String APPLICATION_CLINPORTAL = "clinportal";
	public static final String allowOperation = "allowOperation";
	public static final String REGISTRATION = "REGISTRATION";
	public static final String PHI_ACCESS = "PHI_ACCESS";
	public static final String READ_DENIED = "READ_DENIED";
	public static final String VALIDATOR_CLASSNAME = "validator.classname";
	public static final String hashedOut = "##";
	public static final int ONE = 1;
	public static final int INITIALIZE = -1;
	
	// changes for ACORN
	public static final String MSSQLSERVER_DATABASE = "MSSQLSERVER";
	public static final String FORMAT_FILE_EXTENTION = "_FormatFile.txt";
}


