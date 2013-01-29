/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */
package edu.wustl.cab2b.client.ui.mysettings;

import static edu.wustl.cab2b.client.ui.util.ClientConstants.MENU_CLICK_EVENT;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.SERVICE_URL;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;

/**
 * @author atul_jawale
 * 
 * This class is the left menu panel in my settings page. Each label in this panel will represent a menu item
 * we are adding a mouse listener to each label where we fire the property change on this panel i.e. when user
 * clicks on SERVICE URLs label the mouseClicked event is captured and a property change event on the SERVICE_URL 
 * property is fired which is not handled in this class.   
 */
public class LeftPanel extends Cab2bPanel {

    /**
     * Default Constructor
     */
    public LeftPanel() {
        super();
        initGUI();
    }

    /**
     * This method will initialize the panel as a panel containing label on each line
     */
    public void initGUI() {
        Cab2bLabel blankLabel = new Cab2bLabel(" ");
        blankLabel.setPreferredSize(new Dimension(30, 30));
        add("p hfill", blankLabel);
        
        Cab2bLabel label = new Cab2bLabel(SERVICE_URL);
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        label.setBackground(Color.white);
        label.setOpaque(true);
        label.setPreferredSize(new Dimension(30, 25));
        add("p hfill", label);
        
        setPreferredSize(new Dimension(150, 400));
        setBackground(new Color(185, 211, 238));
        label.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent arg0) {
                firePropertyChange(MENU_CLICK_EVENT, null, SERVICE_URL);
            }
        });
    }
}
