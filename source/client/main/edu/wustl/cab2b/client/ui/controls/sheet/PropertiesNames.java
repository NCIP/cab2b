/*
 * PropertiesNames.java
 *
 * Created on October 22, 2007, 7:27 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.wustl.cab2b.client.ui.controls.sheet;

/**
 *
 *  JASSI IMP: ADD COMMENT ON  ALL THE EVENTS.
 *  CArefully segregate: COLUMN_ADDED and NEW_COLUMN_CREATED, in it usage.
 *
 * @author jasbir_sachdeva
 */
interface PropertiesNames {
    /**     Bounded PRoperty Event mgmt Support...*/
//    public static final String COLUMN_ = "COLUMN_";
    public static final String COLUMN_READ_ONLY_ADDED = "COLUMN_READ_ONLY_ADDED";   
    public static final String COLUMN_MUTABLE_ADDING = "COLUMN_MUTABLE_ADDING"; 
    public static final String COLUMN_MUTABLE_ADDED = "COLUMN_MUTABLE_ADDED";   
    public static final String COLUMN_MUTABLE_REMOVED = "COLUMN_MUTABLE_REMOVED";   
//    public static final String COLUMNS_MUTABLE_REMOVED_ALL = "COLUMNS_MUTABLE_REMOVED_ALL";
    public static final String COLUMNS_REMOVED_ALL = "COLUMNS_REMOVED_ALL";
    
    public static final String COLUMN_VISIBLITY_CHANGING = "COLUMN_VISIBLITY_CHANGING";
    public static final String COLUMN_VISIBLITY_CHANGED = "COLUMN_VISIBLITY_CHANGED";

    
    /**     Makes all the colums visible to the user    */
    static final String SHOW_ALL_COLUMNS = "SHOW_ALL_COLUMNS";

    /**     Makes all Read-only column visible, rest alll are hidden.    */
    static final String SHOW_READ_ONLY_COLUMNS_ONLY = "SHOW_READ_ONLY_COLUMNS_ONLY";

    /**     Makes all Editable/Mutable column visible, rest alll are hidden.    */
    static final String SHOW_MUTABLE_COLUMNS_ONLY = "SHOW_MUTABLE_COLUMNS_ONLY";
    
    /**     Notification for User on Status Bar...*/
    static final String USER_WARNING = "USER_WARNING";
    
    /**     Notification for User that Magnifying glass button clicked...   */
    static final String EVENT_MAGNIFYING_GLASS_CLICKED = "EVENT_MAGNIFYING_GLASS_CLICKED";
    
}
