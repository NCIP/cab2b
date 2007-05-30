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
 * @author Chandrakant Talele
 */
public class Cab2bHyperlink extends JXHyperlink {

    private static final long serialVersionUID = 8338180418321272388L;

    private static final Color defaultClickedHyperlinkColor = new Color(0x006699);

    private static final Color defaultUnclickedHyperlinkColor = new Color(0x034E74);

    /**
     * User object associated with this hyperlink.
     */
    private Object userObject;

    private Color clickedHyperlinkColor;

    private Color unclickedHyperlinkColor;

    /**
     * Default constructor. Creates hypelink object which will be painted with <b>Bold</b> text.
     */
    public Cab2bHyperlink() {
        this(false);
    }

    /**
     * @param usePlainFont TRUE if hyperlink is to be painted with plain text. 
     *                     FALSEpa hyperlink is to be painted with <b>Bold</b> text
     */
    public Cab2bHyperlink(boolean usePlainFont) {
        this(null, usePlainFont);
    }

    /**
     * @param action Action to associate
     * @param usePlainFont TRUE if hyperlink is to be painted with plain text. 
     *                     FALSEpa hyperlink is to be painted with <b>Bold</b> text
     */
    public Cab2bHyperlink(Action action, boolean usePlainFont) {
        super(action);
        this.setClickedHyperlinkColor(defaultClickedHyperlinkColor);
        this.setUnclickedHyperlinkColor(defaultUnclickedHyperlinkColor);
        this.setUI(new Cab2bHyperlinkUI(usePlainFont));
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
    public Color getClickedHyperlinkColor() {
        return clickedHyperlinkColor;
    }

    /**
     * @return Returns the unclickedHyperlinkColor.
     */
    public Color getUnclickedHyperlinkColor() {
        return unclickedHyperlinkColor;
    }

    /**
     * @param clickedHyperlinkColor The clickedHyperlinkColor to set.
     */
    public void setClickedHyperlinkColor(Color clickedHyperlinkColor) {
        this.clickedHyperlinkColor = clickedHyperlinkColor;
        this.setClickedColor(clickedHyperlinkColor);
    }

    /**
     * @param unclickedHyperlinkColor The unclickedHyperlinkColor to set.
     */
    public void setUnclickedHyperlinkColor(Color unclickedHyperlinkColor) {
        this.unclickedHyperlinkColor = unclickedHyperlinkColor;
        this.setUnclickedColor(unclickedHyperlinkColor);
    }

}