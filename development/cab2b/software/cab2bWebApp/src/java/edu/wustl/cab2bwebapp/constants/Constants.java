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
    /** Constant for Saved Query Provider */
    public static final String SAVED_QUERY_BIZ_LOGIC = "savedQueryBizLogic";

    /** Constant for model groups */
    public static final String MODEL_GROUPS = "modelGroups";
    
    /** Constant for model group DVO list */
    public static final String MODEL_GROUP_DVO_LIST = "modelGroupDVOList";    

    /** Constant for saved searches */
    public static final String SAVED_SEARCHES = "savedSearches";
    
    /** Constant for saved searches */
    public static final String SAVED_QUERIES = "savedQueries";

    /** Constant for forward to home page */
    public static final String FORWARD_HOME = "home";

    /** Constant for collection of model group */
    public static final String FORWARD_MODEL_GROUP = "modelgroup";

    /** Constant for model group name */
    public static final String MODEL_GROUP_NAME = "modelGroupName";
    
    /** Constant for model group names */
    public static final String MODEL_GROUP_NAMES = "modelGroupNames";    

    /** Constant for fatal failure on home page access */
    public static final String FATAL_HOME_FAILURE = "fatalHomeFailure";

    /** Constant for model id */
    public static final String MODEL_ID = "modelId";

    /** Constant for page forward to saved searches page */
    public static final String FORWARD_SAVED_SEARCHES = "savedsearches";

    /** Constant for load model associated data failure message */
    public static final String FATAL_DISPLAY_SAVED_SEARCHES_FAILURE = "fatalDisplaySavedSearchesFailure";

    /** Constant for login page */
    public static final String LOGIN_PAGE = "loginPage";

    /** Constant for globus credential */
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

    /** Constant for entity group collection */
    public static final String ENTITY_GROUP_COLLECTION = "entityGroupCollection";

    /** Constant for query id */
    public static final String QUERY_ID = "queryId";

    /** Constant for forward to add limit page */
    public static final String FORWARD_ADD_LIMIT = "addlimit";

    /** Constant for fatal add limit failure */
    public static final String FATAL_ADD_LIMIT_FAILURE = "fatalAddLimitFailure";

    /** Constant for add limit xml file path */
    public static final String ADD_LIMIT_XML_FILE_PATH = "WEB-INF" + java.io.File.separator + "classes"
            + java.io.File.separator + "add-limit.xml";

    /** Constant for error in query execution */
    public static final String ERROR_QUERY_EXECUTE = "errorQueryExecute";

    /** Constant for attribute order */
    public static final String ATTRIBUTE_ORDER = "attributeOrder";

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

    /** Constant for forward to failure page */
    public static final String FORWARD_FAILURE = "failure";
    
    /** Constants for Failed Services */
    public static final String FAILED_SERVICES = "failedServices"; 
    
    /** Constants for Failed Services count */
    public static final String FAILED_SERVICES_COUNT = "failedServicesCount";
}