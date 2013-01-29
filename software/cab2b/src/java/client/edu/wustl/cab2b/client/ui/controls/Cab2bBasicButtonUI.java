/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.controls;

import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.AbstractButton;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * Basic button UI
 * @author chetan_BH
 *
 */
public class Cab2bBasicButtonUI extends BasicButtonUI {

    /**
     * Constructor
     * 
     */
    public Cab2bBasicButtonUI() {

    }

    /**
     * @see javax.swing.plaf.basic.BasicButtonUI#paintFocus(java.awt.Graphics, javax.swing.AbstractButton, java.awt.Rectangle, java.awt.Rectangle, java.awt.Rectangle)
     * @param g
     * @param b
     * @param viewRect
     * @param textRect
     * @param iocnRect
     */
    protected void paintFocus(Graphics g, AbstractButton b, Rectangle viewRect, Rectangle textRect,
                              Rectangle iconRect) {
        // Do Nothing.
    }
}
