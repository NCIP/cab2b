package edu.wustl.cab2b.client.ui.util;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

/**
 * All the constants used at UI side
 * 
 * @author Chandrakant Talele
 */
public interface ClientConstants {
    public static final String OPERATOR_AND = "AND";

    public static final String OPERATOR_OR = "OR";

    /** Resource bundle name for getting error codes and its description. */
    public static String ERROR_CODE_FILE_NAME = "errorcodes";

    /** Resource bundle name for getting externalized strings. */
    public static String APPLICATION_RESOURCES_FILE_NAME = "Cab2bApplicationResources";

    /**
     * caB2B image logo
     */
    public static String CAB2B_LOGO_IMAGE = "CaB2B_16_16_bg.gif";

    /**
     * Popular category image
     */
    public static String POPULAR_CATEGORIES_IMAGE = "popular_categories.gif";

    /**
     * My Search query image
     */
    public static String MY_SEARCH_QUERIES_IMAGE = "my_search.gif";

    /**
     * My Experiment image 
     */
    public static String MY_EXPERIMENT_IMAGE = "my_experiments.gif";

    /**
     * My category Image
     */
    public static String MY_CATEGORIES_IMAGE = "my_categories.gif";

    /**
     * Datails column image
     */
    public static String DETAILS_COLUMN_IMAGE = "popularcategories_icon.gif";

    /**
     * Add limit panel connect node selected image
     */
    public static String LIMIT_CONNECT_SELECTED = "limit_set_rollover.gif";

    /**
     *  Add limit panel connect node unselected image
     */
    public static String LIMIT_CONNECT_DESELECTED = "limit_set.gif";

    /**
     * Add limit panel select cursor icon image
     */
    public static String SELECT_ICON_ADD_LIMIT = "select_icon.gif";

    /**
     * Add limit panel cursor icon mouseover image
     */
    public static String SELECT_ICON_ADD_LIMIT_MOUSEOVER = "select_icon_mo.gif";

    /**
     * Add limit panel paranthesis icon image
     */
    public static String PARENTHISIS_ICON_ADD_LIMIT = "parenthesis_icon.gif";

    /**
     * Add limit panel paranthesis icon mouseover image
     */
    public static String PARENTHISIS_ICON_ADD_LIMIT_MOUSEOVER = "parenthesis_icon_mo.gif";

    /**
     * Add limit panel papergrid image
     */
    public static String PAPER_GRID_ADD_LIMIT = "paper_grid.png";

    /**
     * Add limit panel port image
     */
    public static String PORT_IMAGE_ADD_LIMIT = "port.gif";

    /**
     * Experiment / DataList tree open folder image
     */
    public static String TREE_OPEN_FOLDER = "open_folder.gif";

    /**
     * Experiment / DataList tree close folder image
     */
    public static String TREE_CLOSE_FOLDER = "close_folder.gif";

    /**
     * Visualise data image
     */
    public static String VISUALIZE_DATA = "visualize_data.gif";

    /**
     * Analyze data image
     */
    public static String ANALYZE_DATA = "analyze_data.gif";

    /**
     * Filter data image
     */
    public static String FILTER_DATA = "filter_data.gif";

    /**
     *  Charts image icon
     */
    public static String CHARTS = "charts.gif";

    /**
     * Bar graph image icon
     */
    public static String BAR_GRAPH = "graph_bar.gif";

    /**
     * Line Graph image icon
     */
    public static String LINE_GRAPH = "graph_line.gif";

    /**
     * Scatter Graph image icon
     */
    public static String SCATTER_GRAPH = "graph_scatter.gif";

   

    /**
     * Gene Annotation image icon
     */
    public static String GENE_ANNOTATION = "Gene_Annotation.gif";

    /**
     * Home tab unpressed icon
     */
    public static String HOME_TAB_UNPRESSED = "home_tab.gif";

    /**
     * Home tab unpressed icon
     */
    public static String HOME_TAB_PRESSED = "home_MO_tab.gif";

    /**
     * Search tab unpressed icon
     */
    public static String SEARCH_TAB_UNPRESSED = "searchdata_tab.gif";

    /**
     * Search tab pressed icon
     */
    public static String SEARCH_TAB_PRESSED = "searchdata_MO_tab.gif";

    /**
     * Experiment tab unpressed icon
     */
    public static String EXPT_TAB_UNPRESSED = "experiment_tab.gif";

    /**
     * Experiment tab pressed icon
     */
    public static String EXPT_TAB_PRESSED = "experiment_MO_tab.gif";

    /**
     * Tree leaf node icon
     */
    public static String TREE_LEAF_NODE = "data_14_14.gif";

    /**
     * Experiment leaf node icon
     */
    public static String EXPT_LEAF_NODE = "my_experiments_data.gif";

    /**
     * Select data category icon
     */
    public static String SELECT_DATA_CATEGORY = "select_data_category.gif";

    /**
     * Default border for client
     */
    public static final Border border = BorderFactory.createLineBorder(Color.BLACK);

    /**
     * Model map property file name
     */
    public static final String MODEL_MAP_PROPERTY_FILE = "modelMap";

    /**
     * Bar chart string name
     */
    public static final String BAR_CHART = "Bar Chart";

    /**
     * Line chart string name
     */
    public static final String LINE_CHART = "Line Chart";

    /**
     * Scatter chart string name
     */
    public static final String SCATTER_PLOT = "Scatter Plot";

    /**
     * chart string name
     */
    public static final String CHART = "Chart";   

    /**
     * Logout button icon 
     */
    public static final String LOGOUT_ICON = "logout_icon.gif";

    /**
     * MySettings Page Service URL Link
     */
    public static final String MENU_CLICK_EVENT = "menuClicked";

    public static final String SERVICE_URL = "Service URLs";

    public static final String SEARCH_EVENT = "searchEvent";

    public static final String BACK_EVENT = "backEvent";

    public static final String UPDATE_EVENT = "updateEvent";

    public static final String SERVICE_SELECT_EVENT = "serviceSelected";
    
    public static final String UPDATE_QUERYURLS_EVENT = "updateQueryURLS";
    
    public static final String DIALOG_CLOSE_EVENT = "dialogCloseEvent";
}
