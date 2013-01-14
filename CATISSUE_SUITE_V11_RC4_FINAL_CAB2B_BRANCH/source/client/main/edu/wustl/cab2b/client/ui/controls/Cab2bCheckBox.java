/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.controls;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JCheckBox;

public class Cab2bCheckBox extends JCheckBox {

    static Color defaultBgColor = Color.WHITE;

    /**
     * Creates checkbox with no text 
     */
    public Cab2bCheckBox() {
        this("");
    }

    /**
     * Creates checkbox with text 
     * @param title
     */
    public Cab2bCheckBox(String title) {
        this(title, Cab2bStandardFonts.DEFAULT_FONT);
    }

    /**
     * Creates checkbox with text and font 
     * @param title
     * @param textFont
     */
    public Cab2bCheckBox(String title, Font textFont) {
        this(title, textFont, defaultBgColor);
    }

    /**
     * Creates checkbox with text, font and color 
     * @param title
     * @param textFont
     * @param bgColor
     */
    public Cab2bCheckBox(String title, Font textFont, Color bgColor) {
        super(title);
        this.setFont(textFont);
        this.setBackground(defaultBgColor);
    }

}
