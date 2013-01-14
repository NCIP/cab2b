/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.controls;

import java.awt.Font;

/**
 * caB2B application standard font constants
 * @author Chetan_BH
 *
 */
public interface Cab2bStandardFonts {
    /**
     * Arial plain 12 Normal
     */
    public static Font ARIAL_PLAIN_12 = new Font("arial", Font.PLAIN, 12);

    /**
     * Arial plain 12 BOLD
     */
    public static Font ARIAL_BOLD_12 = new Font("arial", Font.BOLD, 12);

    /**
     * Arial plain 12 Italic
     */
    public static Font ARIAL_ITALIC_12 = new Font("arial", Font.ITALIC, 12);

    /**
     * Arial plain 16 Normal
     */
    public static Font ARIAL_PLAIN_16 = new Font("arial", Font.PLAIN, 16);
    
    /**
     * Arial plain 10 Bold
     */
    public static Font ARIAL_BOLD_10 = new Font("arial", Font.BOLD, 10);

    /**
     * Default caB2B font
     */
    public static Font DEFAULT_FONT = ARIAL_PLAIN_12;
}
