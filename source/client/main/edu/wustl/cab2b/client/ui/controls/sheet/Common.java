/*
 * Common.java
 *
 * Created on October 22, 2007, 7:27 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package edu.wustl.cab2b.client.ui.controls.sheet;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JSeparator;

import edu.wustl.cab2b.client.ui.controls.Cab2bStandardFonts;

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

    /** Name of Event signifying Column Addition Request */
    public static final String USER_COLUMN_ADDITION_REQUESTED = "USER_COLUMN_ADDITION_REQUESTED";

    /** Name of Event signifying Column Added */
    public static final String USER_COLUMN_ADDED = "USER_COLUMN_ADDED";

    /** Name of Event signifying Request for Showing Customization Console */
    public static final String REQUEST_SHOW_CUSTOMIZATION_CONSOLE = "REQUEST_SHOW_CUSTOMIZATION_CONSOLE";

    /** Name of Event signifying Reset All */
    public static final String REQUEST_RESET_ALL = "REQUEST_RESET_ALL";

    /** Name of Event signifying Show Row Details */
    public static final String REQUESTED_SHOW_ROW_DETAILS = "REQUESTED_SHOW_ROW_DETAILS";

    /** Name of Event signifying Data Row Double Clicked */
    public static final String EVENT_DATA_ROW_DOUBLE_CLICKED = "EVENT_DATA_ROW_DOUBLE_CLICKED";

    /** Name of Event signifying Header Row Double Clicked */
    public static final String EVENT_HEADER_ROW_DOUBLE_CLICKED = "EVENT_HEADER_ROW_DOUBLE_CLICKED";

    //Added by deepak
    /** Name of Event signifying Single Data Clicked */
    public static final String EVENT_DATA_SINGLE_CLICKED = "EVENT_DATA_SINGLE_CLICKED";

    /**
     * To set Size and Fonts of Cab2b Buttons
     * @param button
     */
    public static void cab2bButtonSetting(JButton button) {
        button.setFont(Cab2bStandardFonts.ARIAL_PLAIN_12);
        button.setPreferredSize(new Dimension(button.getPreferredSize().width, 22));
    }

    /** Recursively apply WHITE background to all child components, except for JButtons... 
     * @param comp
     */
    public static void setBackgroundWhite(JComponent comp) {
        if ((comp instanceof JButton) || (comp instanceof JSeparator)) {
            //  We do NOT want to change color of Either JButton, or JSparator...
            //  so exit recursion chain...
            return;
        } else
            comp.setBackground(java.awt.Color.WHITE);

        java.awt.Component[] childComp = comp.getComponents();
        for (int idx = 0; idx < childComp.length; idx++) {
            if (childComp[idx] instanceof JComponent) {
                setBackgroundWhite((JComponent) childComp[idx]);
            }
        }
    }

    /**
     * Recursively sets the Look and Feel of buttons
     * @param comp
     */
    public static void setButtonsLooks(JComponent comp) {
        if ((comp instanceof JButton)) {
            cab2bButtonSetting((JButton) comp);
        }
        java.awt.Component[] childComp = comp.getComponents();
        for (int idx = 0; idx < childComp.length; idx++) {
            if (childComp[idx] instanceof JComponent) {
                setButtonsLooks((JComponent) childComp[idx]);
            }
        }
    }
}
