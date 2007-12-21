/*
 * Common.java
 *
 * Created on October 22, 2007, 7:27 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package edu.wustl.cab2b.client.ui.controls.sheet;

import javax.swing.*;

/**
 *
 *  JASSI IMP: ADD COMMENT ON  ALL THE EVENTS.
 *  CArefully segregate: COLUMN_ADDED and NEW_COLUMN_CREATED, in it usage.
 *
 * @author jasbir_sachdeva
 */
class Common {

    /**     Bounded PRoperty Event mgmt Support...*/
    public static final String COLUMN_VISIBLITY_CHANGE_REQUESTED = "COLUMN_VISIBLITY_CHANGE_REQUESTED";
    public static final String USER_COLUMN_ADDITION_REQUESTED = "USER_COLUMN_ADDITION_REQUESTED";
    public static final String USER_COLUMN_ADDED = "USER_COLUMN_ADDED";
    public static final String REQUEST_SHOW_CUSTOMIZATION_CONSOLE = "REQUEST_SHOW_CUSTOMIZATION_CONSOLE";
    public static final String REQUEST_RESET_ALL = "REQUEST_RESET_ALL";
    public static final String REQUESTED_SHOW_ROW_DETAILS = "REQUESTED_SHOW_ROW_DETAILS";
    public static final String EVENT_DATA_ROW_DOUBLE_CLICKED = "EVENT_DATA_ROW_DOUBLE_CLICKED";
    public static final String EVENT_HEADER_ROW_DOUBLE_CLICKED = "EVENT_HEADER_ROW_DOUBLE_CLICKED";

    //Added by deepak
    public static final String EVENT_DATA_SINGLE_CLICKED = "EVENT_DATA_SINGLE_CLICKED";

    /** Recursively apply WHITE background to all child components, except for JButtons...    */
    public static void setBackgroundWhite(JComponent comp) {
        if (!(comp instanceof JButton)) {
            comp.setBackground(java.awt.Color.WHITE);
        }
        java.awt.Component[] childComp = comp.getComponents();
        for (int idx = 0; idx < childComp.length; idx++) {
            if (childComp[idx] instanceof JComponent) {
                setBackgroundWhite((JComponent) childComp[idx]);
            }
        }
    }
}
