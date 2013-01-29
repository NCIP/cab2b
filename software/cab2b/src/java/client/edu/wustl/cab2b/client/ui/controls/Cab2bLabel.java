/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.controls;

import java.awt.Color;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * A display area for a short text string or an image, or both. A label does not react to input events. 
 * As a result, it cannot get the keyboard focus. A label can, however, display a keyboard alternative 
 * as a convenience for a nearby component that has a keyboard alternative but can't display it.
 * A JLabel object can display either text, an image, or both. You can specify where in the label's 
 * display area the label's contents are aligned by setting the vertical and horizontal alignment. 
 * By default, labels are vertically centered in their display area. Text-only labels are leading edge 
 * aligned, by default; image-only labels are horizontally centered, by default. You can also specify the
 * position of the text relative to the image. By default, text is on the trailing edge of the image, 
 * with the text and image vertically aligned.
 * @author Chetan_BH
 *
 */
public class Cab2bLabel extends JLabel {
    private static final long serialVersionUID = 1L;

    /**
     * Label background color
     */
    static Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;

    /**
     * Creates a Cab2bLabel instance with no image and with an empty string for the title.
     */
    public Cab2bLabel() {
        this("");
    }

    /**
     * Creates a Cab2bLabel instance with no image and with a string for the title.
     * @param title
     */
    public Cab2bLabel(String title) {
        this(title, Cab2bStandardFonts.ARIAL_PLAIN_12);
    }

    /**
     * Creates a Cab2bLabel instance with a string for the title and specified alignment
     * @param title
     * @param alignment
     */
    public Cab2bLabel(String title, int alignment) {
        this(title, Cab2bStandardFonts.ARIAL_PLAIN_12, alignment);
    }

    /**
     * Creates a Cab2bLabel instance with the specified text and font.
     * @param title
     * @param textFont
     */
    public Cab2bLabel(String title, Font textFont) {
        this(title, textFont, SwingConstants.LEFT);
    }

    /**
     * Creates a Cab2bLabel instance with the specified text, font and alignment.
     * @param title
     * @param textFont
     * @param alignment
     */
    public Cab2bLabel(String title, Font textFont, int alignment) {
        super(title, alignment);
        this.setFont(textFont);
        this.setBackground(DEFAULT_BACKGROUND_COLOR);
    }

    /**
     * Creates a Cab2bLabel instance with the specified image.
     * @param icon
     */
    public Cab2bLabel(Icon icon) {
        super(icon);
    }
}
