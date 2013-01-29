/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.controls;

import java.awt.Color;
import java.awt.LayoutManager;

import org.jdesktop.swingx.JXPanel;


/**
 * Cab2bPanel is a generic lightweight container extended from JXPanel. 
 * @author Chetan_BH
 *
 */
public class Cab2bPanel extends JXPanel {

    /**
     * Default panel background color
     */
    static Color defaultBgColor = Color.WHITE;

    /**
     * Creates a new Cab2bPanel with a double buffer and a RiverLayout
     */
    public Cab2bPanel() {
        this(new RiverLayout());
        this.setBorder(null);
    }

    /**
     * Creates a new Cab2bPanel with  RiverLayout and the specified LayoutManager.
     * @param lm
     */
    public Cab2bPanel(LayoutManager lm) {
        this(lm, false);
        this.setBorder(null);
    }

    /**
     * Creates a new Cab2bPanel with specified LayoutManager and the specified buffering strategy.
     * @param lm
     * @param isDoubleBuffered
     */
    public Cab2bPanel(LayoutManager lm, boolean isDoubleBuffered) {
        this(lm, isDoubleBuffered, defaultBgColor);
        this.setBorder(null);
    }

    /**
     * Creates a new Cab2bPanel with specified LayoutManager , the specified buffering strategy and specified color.
     * @param lm
     * @param isDoubleBuffered
     * @param bgColor
     */
    public Cab2bPanel(LayoutManager lm, boolean isDoubleBuffered, Color bgColor) {
        super(lm, isDoubleBuffered);
        this.setBackground(bgColor);
        this.setBorder(null);
    }

    /**
     * Do initilization for panel
     */
    public void doInitialization() {

    }
}
