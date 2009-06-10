/**
 * 
 */
package edu.wustl.cab2b.admin.util;

/**
 * @author atul_jawale
 *
 */
public class Cab2bConstants {

    public final static String MODEL_NULL_ERROR = "ERROR FETCHING DOMAIN MODEL FROM INDEX SERVICE.";

    public final static String LOAD_MODEL_STATUS_ACTION = "status";

    public final static String LOAD_MODEL_DETAILS_ACTION = "details";

    public static final String SUCCESS = "success";

    public static final String FAILURE = "failure";

    public final static String caDSR_SERVICE_URL = "http://cagrid-service.nci.nih.gov:8080/wsrf/services/cagrid/CaDSRService";

    public static String PROPERTY_FILENAME = null;

    public final static String PROPERTY_FILE = "Property_File";

    public final static String INDEX_URL = "indexurls";

    public final static String OFFSET_PARAMETER = "pager.offset";

    public final static String IN_PROGRESS = "Loading";

    public final static String FAILED = "Failed";

    public final static String PASS = "Pass";

    public final static String WAIT = "wait";

    public final static String ENTITY_ID_SEPARATOR = "#entity_id#";
    
    public final static String ENTITY_NAME_SEPARATOR = "#entity_name#";

    public final static String OPERATION = "#edit#";

    public final static String ADD_EDIT = "#add_edit#";
    
    public final static String USER_NAME ="UserName";
    
    public final static String USER_OBJECT = "User";
    
    public final static String USER_ID = "User_ID";
    
    public final static int MAX_PAGE_ITEMS = 5;
    
    public final static int MAX_INDEX_PAGES = 5;
    
    public final static String ALL_SELECTED_MODELS_LIST = "allSelectedModelsList";
    
    public final static String ALL_AVAILABLE_MODELS_TO_LOAD = "allAvailableModels";
    
    public final static String FILTERED_AVAILABLE_MODELS_TO_LOAD = "filteredAvailableModels";
    
    public final static String ALL_LOADED_MODELS = "allLoadedModels";
    
    public final static String FILTERED_LOADED_MODELS = "filteredLoadedModels";
    
    public final static String ALL_SERVICE_INSTANCES = "allServiceInstances"; 
    
    public final static String FILTERED_SERVICE_INSTANCES = "filteredServiceInstances";
}
