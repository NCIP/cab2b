package edu.wustl.cab2b.client.ui.controls;

import java.awt.Color;

import javax.swing.Action;

import org.jdesktop.swingx.JXHyperlink;

/**
 * By Default Cab2bHyperlink is underlined, unlike JXHyperlink,
 * where hyperlinks are underlined only on mouse over.
 * 
 * To get JXHyperlink behaviour in Cab2bHyperlink call Cab2bHyperlink constructors
 * with isHyperlinkUnderlined boolean set to false. 
 * 
 * @author chetan_bh
 *
 */
public class Cab2bHyperlink extends JXHyperlink {

    private static final long serialVersionUID = 8338180418321272388L;

    /**
     * User object associated with this hyperlink.
     */
    private Object userObject;

    private static Color clickedHyperlinkColor = new Color(0x006699);

    private static Color unclickedHyperlinkColor = new Color(0x034E74);

    private static boolean hyperlinkUnderlined = true;

    /**
     * default constructor
     */
    public Cab2bHyperlink() {
        this(null, hyperlinkUnderlined, clickedHyperlinkColor, unclickedHyperlinkColor);
    }

    /**
     * @param isHyperlinkUnderlined True if Hyperlink is Underlined else false
     */
    public Cab2bHyperlink(boolean isHyperlinkUnderlined) {
        this(null, isHyperlinkUnderlined, clickedHyperlinkColor, unclickedHyperlinkColor);
    }

    /**
     * @param clickedColor colour to use when hyperlink is clicked
     * @param unclickedColor colour to use when hyperlink is not clicked
     */
    public Cab2bHyperlink(Color clickedColor, Color unclickedColor) {
        this(null, hyperlinkUnderlined, clickedColor, unclickedColor);
    }

    /**
     * @param action Action
     */
    public Cab2bHyperlink(Action action) {
        this(action, hyperlinkUnderlined, clickedHyperlinkColor, unclickedHyperlinkColor);
    }

    /**
     * @param action Action 
     * @param isHyperlinkUnderlined True if Hyperlink is Underlined else false
     */
    public Cab2bHyperlink(Action action, boolean isHyperlinkUnderlined) {

        this(action, isHyperlinkUnderlined, clickedHyperlinkColor, unclickedHyperlinkColor);
    }

    /**
     * @param action Action
     * @param isHyperlinkUnderlined True if Hyperlink is Underlined else false
     * @param clickedColor colour to use when hyperlink is clicked
     * @param unclickedColor colour to use when hyperlink is not clicked
     */
    public Cab2bHyperlink(Action action, boolean isHyperlinkUnderlined, Color clickedColor, Color unclickedColor) {
        super(action);

        hyperlinkUnderlined = isHyperlinkUnderlined;
        this.setClickedColor(clickedColor);
        this.setUnclickedColor(unclickedColor);
        if (hyperlinkUnderlined)
            this.setUI(new Cab2bHyperlinkUI());
    }

    /**
     * Returns user object associated with this hyperlink.
     */
    public Object getUserObject() {
        return userObject;
    }

    /**
     * Sets the user object.
     */
    public void setUserObject(Object userObject) {
        this.userObject = userObject;
    }

    /**
     * @return Returns the clickedHyperlinkColor.
     */
    public static Color getClickedHyperlinkColor() {
        return clickedHyperlinkColor;
    }

    /**
     * @return Returns the unclickedHyperlinkColor.
     */
    public static Color getUnclickedHyperlinkColor() {
        return unclickedHyperlinkColor;
    }
}