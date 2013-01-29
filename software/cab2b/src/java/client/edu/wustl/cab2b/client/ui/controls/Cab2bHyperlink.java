/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

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
 * @param <T>
 */
public class Cab2bHyperlink<T> extends JXHyperlink {

    private static final long serialVersionUID = 8338180418321272388L;

    /**
     * Hyperlink default color clicked
     */
    private static final Color DEFAULT_CLICKED_COLOR = new Color(76,41,157);

    /**
     * Hyperlink default color unclicked
     */
    private static final Color DEFAULT_UNCLICKED_COLOR = new Color(0x034E74);

    /**
     * User object associated with this hyperlink.
     */
    private T userObject;

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
        this.setClickedColor(DEFAULT_CLICKED_COLOR);
        this.setUnclickedColor(DEFAULT_UNCLICKED_COLOR);
        this.setUI(new Cab2bHyperlinkUI(usePlainFont));
    }

    /**
     * Returns user object associated with this hyperlink.
     * @return UserObject
     */
    public T getUserObject() {
        return userObject;
    }

    /**
     * Sets the user object.
     * @param userObject
     */
    public void setUserObject(T userObject) {
        this.userObject = userObject;
    }

}