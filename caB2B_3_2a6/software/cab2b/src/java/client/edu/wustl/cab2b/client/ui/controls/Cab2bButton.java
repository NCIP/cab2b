package edu.wustl.cab2b.client.ui.controls;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.JButton;

/**
 * Cab2b button class
 * @author Chetan_BH
 *
 */
public class Cab2bButton extends JButton {

    /**
     * Creates a button with no set text or icon.
     */
    public Cab2bButton() {
        this("");
    }

    /**
     * Creates a button with text
     * @param strLabel
     */
    public Cab2bButton(String strLabel) {
        this(strLabel, false);
    }

    /**
     * Creates a button with text AND font.
     * @param strLabel
     * @param textFont
     */
    public Cab2bButton(String strLabel, Font textFont) {
        this(strLabel, false, textFont);
    }

    /**
     * Creates a button with text.
     * If isSimpleButtonUI is true set simple UI look  
     * @param strLabel
     * @param isSimpleButtonUI
     */
    public Cab2bButton(String strLabel, boolean isSimpleButtonUI) {
        this(strLabel, isSimpleButtonUI, Cab2bStandardFonts.ARIAL_PLAIN_12);
    }

    /**
     * Creates a button with text.
     * If isSimpleButtonUI is true set simple UI look and feel.
     * Sets button font
     * @param strLabel
     * @param isSimpleButtonUI
     * @param textFont
     */
    public Cab2bButton(String strLabel, boolean isSimpleButtonUI, Font textFont) {
        super(strLabel);
        this.setPreferredSize(new Dimension(85, 22));
        /*Arial font in windows is mapped to the SansSerif logical name.*/
        this.setFont(textFont);
        if (isSimpleButtonUI) {
            this.setUI(new Cab2bBasicButtonUI());
        }
    }

    /**
     *  Creates a button with icon.
     * @param imageIcon
     */
    public Cab2bButton(Icon imageIcon) {
        super(imageIcon);
        this.setPreferredSize(new Dimension(85, 22));
    }
}
