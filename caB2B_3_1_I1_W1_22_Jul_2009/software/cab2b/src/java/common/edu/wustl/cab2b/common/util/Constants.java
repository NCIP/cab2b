package edu.wustl.cab2b.common.util;

import java.awt.Dimension;

/**
 * Constants class for the common
 * @author gautam_shetty
 */
public interface Constants {
    
    /** Metadata search configuration parameter : Used when a class name is to be included in search*/
    public static final int CLASS = 1;
    
    /** Metadata search configuration parameter : Used when a attribute name is to be included in search*/
    public static final int ATTRIBUTE = 2;
    
    /** Metadata search configuration parameter : Used when a class's description is to be included in search*/
    public static final int CLASS_WITH_DESCRIPTION = 4;
    
    /** Metadata search configuration parameter : Used when a attribute's description is to be included in search*/
    public static final int ATTRIBUTE_WITH_DESCRIPTION = 5;
    
    /** Metadata search configuration parameter : Used when a permissible value is to be included in search*/
    public static final int PV = 3;
    
    /** Metadata search configuration parameter : Used when a text based search is desired*/
    public static final int BASED_ON_TEXT = 0;
    
    /** Metadata search configuration parameter : Used when a code based search is desired*/
    public static final int BASED_ON_CONCEPT_CODE = 1;

    //public static final int INLUDE_CLASSES_CLASS = 6;

    //public static final int INLUDE_CLASSES_ATTRIBUTE = 7;

    public static final String OLD_ENTITY_ID_TAG_NAME = "original_entity_id";
    
    /** Tagged value key to store the name of original entity*/
    public static final String ENTITY_DISPLAY_NAME = "original_entity_display_name";
    
    /**Tagged value to indicate a data category is a filtered data category*/
    public static final String FILTERED = "filtered";

    /** Tagged value for non-search-able entities or attributes*/
    public static final String TAGGED_VALUE_NOT_SEARCHABLE = "NOT_SEARCHABLE";
    /**
     * Constant which represents String that will be used as URL KEY in tagged value
     */
    public static final String URL_KEY = "urlKey";

    /**
     * Name of Category Entity Group
     */
    public static final String CATEGORY_ENTITY_GROUP_NAME = "CategoryEntityGroup";
    
    /**
     * Name of Category Entity Group
     */
    public static final String MMC_ENTITY_GROUP_NAME = "MMCEntityGroup";

    /**
     * Name of Data List Entity Group to exclude from Metadata search.
     */
    public static final String DATALIST_ENTITY_GROUP_NAME = "DataListEntityGroup";

    /**
     * tagged key constant to identify a category entity.
     */
    public static final String TYPE_CATEGORY = "Category";
    
    /**
     * tagged constant to identify a multi-model category entity.
     */
    public static final String MULTIMODELCATEGORY = "MMCategory";

    /**
     * tagged key constant to identify wthether a attribute OR an association is a derived one
     */
    public static final String TYPE_DERIVED = "derived";

    /**
     * tagged key constant to identify wthether a attribute OR an association is a derived one
     */
    public static final String ORIGINAL_ASSOCIATION_POINTER = "actualAssociationPointer";

    /**
     * Represents the String used as field connector to connect intermediate path Ids, while generating the file.
     */
    public static final String CONNECTOR = "_";

    /**
     * tagged key constant to identify cab2b entity group
     */
    public static final String CAB2B_ENTITY_GROUP = "caB2BEntityGroup";

    public static final String SELECT = "-- Select --";
    
    //public static final String SELECT_COLUMN = "-- Select Column --";

    public static final String SELECTED_PATH = "Selected Path";

    public static final String CURATED_PATH = "Curated Path";

    public static final String GENERAL_PATH = "General Path";

    public static final Dimension WIZARD_SIZE2_DIMENSION = new Dimension(752, 580);

    public static final Dimension WIZARD_NAVIGATION_PANEL_DIMENSION = new Dimension(752, 36);
    
    public static final String APPLY_FILTER_PANEL_NAME = "applyFilterPanel";
    
    public enum ChartOrientation {
        ROW_AS_CATEGORY, COLUMN_AS_CATEGORY
    }
}