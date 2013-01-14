/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2bwebapp.constants;

/**
 * Application Specific Constants
 * @author Chetan Pundhir
 */
public interface Constants {
    /**
     * Constant for FOUR_KILO_BYTES.
     */
    public static final int FOUR_KILO_BYTES = 4096;

    /** Constants for AJAX Call */
    public static final String AJAX_CALL = "Ajax-Call";

    /** Constants for invalidRequest */
    public static final String INVALID_REQUEST = "invalidRequest";

    /** Constant for forward to infeasibleURLs */
    public static final String INFEASIBLE_URL = "infeasibleURL";

    /** Constants for isFirstRequest */
    public static final String IS_FIRST_REQUEST = "isFirstRequest";

    /** Constants for exported_file_path */
    public static final String EXPORTED_FILE_PATH = "exported_file_path";

    /** Constants for conditionList */
    public static final String CONDITION_LIST = "conditionList";

    /** Constants for stopAjax */
    public static final String STOP_AJAX = "stopAjax";

    /** Constants for Finishing Query Execution */
    public static final String UI_POPULATION_FINISHED = "UI_population_finished";

    /** Constants for processing */
    public static final String PROCESSING = "processing";

    /** Constants for keyword */
    public static final String KEYWORD = "keyword";

    /** Constants for queryBizLogicObject */
    public static final String QUERY_BIZ_LOGIC_OBJECT = "queryBizLogicObject";

    /** Constants for User versus QueryBizLogicObject used for maintaining background query execution map*/
    public static final String USER_VS_QUERY_BIZ_LOGIC_OBJECT = "UserVsQueryBizLogicObject";

    /** Constant for queryStatusDVOSet object */
    public static final String QUERY_STATUS_DVO_SET = "queryStatusDVOSet";

    /** Constant for Saved Query Provider */
    public static final String SAVED_QUERY_BIZ_LOGIC = "savedQueryBizLogic";

    /** Constant for model groups */
    public static final String MODEL_GROUPS = "modelGroups";

    /** Constant for transformation max UI limit */
    public static final String TRANSFORMATION_MAX_LIMIT = "transformationMaxLimit";

    /** Constant for keywordQueryNotPresent */
    public static final String KEYWORD_QUERY_NOT_PRESENT = "keywordQueryNotPresent";

    /** Constant for model group DVO list */
    public static final String MODEL_GROUP_DVO_LIST = "modelGroupDVOList";

    /** Constant for saved searches */
    public static final String SAVED_SEARCHES = "savedSearches";

    /** Constant for saved searches */
    public static final String SAVED_QUERIES = "savedQueries";

    /** Constant for selectedQueryName */
    public static final String SELECTED_QUERY_NAME = "selectedQueryName";

    /** Constant for queryResultCount */
    public static final String QUERY_RESULT_COUNT = "queryResultCount";

    /** Constant for forward to home page */
    public static final String FORWARD_HOME = "home";

    /** Constant for model group name */
    public static final String MODEL_GROUP_NAME = "modelGroupName";

    /** Constant for fatal failure on home page access */
    public static final String FATAL_HOME_FAILURE = "fatalHomeFailure";

    /** Constant for page forward to saved searches page */
    public static final String FORWARD_SAVED_SEARCHES = "savedsearches";

    /** Constant for load model associated data failure message */
    public static final String FATAL_DISPLAY_SAVED_SEARCHES_FAILURE = "fatalDisplaySavedSearchesFailure";

    /** Constant for globus_credential */
    public static final String GLOBUS_CREDENTIAL = "globusCredential";

    /** Constant for anonymous user */
    public static final String ANONYMOUS = "Anonymous";

    /** Constant for user object */
    public static final String USER = "user";

    /** Constant for user name */
    public static final String USER_NAME = "userName";

    /** Constant for invalid login error */
    public static final String ERROR_LOGIN_INVALID = "errorLoginInvalid";

    /** Constant for session timeout error */
    public static final String ERROR_SESSION_TIMEOUT = "errorSessionTimeout";

    /** Constant for fatal login failure */
    public static final String FATAL_LOGIN_FAILURE = "fatalLoginFailure";

    /** Constant for forward to login page */
    public static final String FORWARD_LOGIN = "login";

    /** Constant for successful user logout message */
    public static final String SUCCESS_USER_LOGOUT = "successUserLogout";

    /** Constant for fatal logout failure */
    public static final String FATAL_LOGOUT_FAILURE = "fatalLogoutFailure";

    /** Constant for category entity group name */
    public static final String CATEGORY_ENTITY_GROUP_NAME = "CategoryEntityGroup";

    /** Constant for service instances */
    public static final String SERVICE_INSTANCES = "serviceInstances";

    /** Constant for forward to service instances page */
    public static final String FORWARD_SERVICE_INSTANCES = "serviceinstances";

    /** Constant for display service instances failure message */
    public static final String FATAL_DISPLAY_SERVICE_INSTANCES_FAILURE = "fatalDisplayServiceInstancesFailure";

    public static final String SERVICE_INSTANCES_NOT_CONFIGURED = "serviceInstancesNotConfigured";

    /** Constant for query id */
    public static final String QUERY_ID = "queryId";

    /** Constant for query conditions */
    public static final String QUERY_CONDITIONS = "queryConditions";

    /** Constant for keyWordQueryId */
    public static final String KEYWORD_QUERY_ID = "keyWordQueryId";

    /** Constant for forward to add limit page */
    public static final String FORWARD_ADD_LIMIT = "addlimit";

    /** Constant for fatal add limit failure */
    public static final String FATAL_ADD_LIMIT_FAILURE = "fatalAddLimitFailure";

    /** Constant for add limit XML file path */
    public static final String ADD_LIMIT_XML_FILE_PATH = "WEB-INF" + java.io.File.separator + "classes"
            + java.io.File.separator + "add-limit.xml";

    /** Constant for all hosting institutions */
    public static final String ALL_HOSTING_INSTITUTIONS = "All Hosting Institutions";

    /** Constant for Hosting Cancer Research Center */
    public static final String HOSTING_CANCER_RESEARCH_CENTER = "Hosting Cancer Research Center";

    /** Constant for Point of Contact */
    public static final String POINT_OF_CONTACT = "Point of Contact";

    /** Constant for Contact eMail */
    public static final String CONTACT_EMAIL = "Contact eMail";

    /** Constant for Hosting Institution */
    public static final String HOSTING_INSTITUTION = "Hosting Institution";

    /** Constant for Hosting Institution */
    public static final String MODEL_NAME = "Model Name";

    /** Constant for query result */
    public static final String SEARCH_RESULTS = "searchResults";

    /** Constant for query result view */
    public static final String SEARCH_RESULTS_VIEW = "searchResultsView";

    /** Constant for forward to search results page */
    public static final String FORWARD_SEARCH_RESULTS = "searchresults";

    /** Constant for forward to search results panel page */
    public static final String FORWARD_SEARCH_RESULTS_PANEL = "searchresultspanel";

    /** Constant for fatal keyword search failure */
    public static final String FATAL_KYEWORD_SEARCH_FAILURE = "fatalKeywordSearchFailure";

    /** Constant for fatal export failure */
    public static final String FATAL_EXPORT_FAILURE = "fatalExportFailure";

    /** Constants for Failed Services */
    public static final String FAILED_SERVICES = "failedServices";

    /** Constants for Failed Services count */
    public static final String FAILED_SERVICES_COUNT = "failedServicesCount";

    /** Constant for forward to failure page */
    public static final String FORWARD_FAILURE = "failure";

    /** Constant for forward to displaylimitedannotation page */
    public static final String DISPLAY_LIMITED_ANNOTATION = "displaylimitedannotation";

    /** constant for saving all annotation dvo in session ,it is used only in the view */
    public static final String SAVE_ANNOTATION = "savedannotation";

    /** Constant for forward to displayallannotation page */
    public static final String DISPLAY_All_ANNOTATION = "displayallannotation";

    /** Constant used  for key used in saving annotation in Map*/
    public static final String RESOURCE_ANNOTATIONMAPS = "annotationMap";

    /** Constant for forward to dash board page */
    public static final String FORWARD_DASHBOARD = "dashboard";

    /** Constant for forward to dash board panel page */
    public static final String FORWARD_DASHBOARD_PANEL = "dashboardpanel";

    /** Constant for forward to dash board action class */
    public static final String FORWARD_DASHBOARD_ACTION = "dashboardaction";

    /** Constant for fatal failure for displaying dash board */
    public static final String FATAL_DISPLAY_DASHBOARD_FAILURE = "fatalDisplayDashboardFailure";

    /** Constant for resourceId in session*/
    public static final String RESOURCE_ID = "resourceId";

    /** Constant for user selected token */
    public static final String TOKEN = "token";

    /** Constant for all resources in session */
    public static final String RESOURCES = "resources";

    /** Constant for all resources in session */
    public static final String ONTOLOGIES = "ontologies";

    /** Constant for parameter "index" */
    public static final String INDEX = "index";

    /** Constant for index separator in session */
    public static final String INDEX_SEPEARATOR = "sep";
    
    /** Constant for word indicator  */
    public static final String WORD_INDICATOR = "##WORD##";
    
}
