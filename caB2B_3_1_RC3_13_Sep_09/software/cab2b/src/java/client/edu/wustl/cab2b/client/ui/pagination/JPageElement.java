package edu.wustl.cab2b.client.ui.pagination;

/*
 > I have a JFrame Window and a JLabel on it with a long text. The problem
 > is that the text of the JLabel does not fit the size of the window -
 > the value for the width of the window is to less. But I want not to
 > make the window wider. How can I make it that the text of the JLabel is
 > automatically divided when the end of the window width is reached and
 > the remaining text will be placed in the next line?

 That will not be easy with a JLabel. You an easily create a JLabel that
 spans multiple lines (using HTML tags), but getting it to wrap is
 something else.

 Consider using one of the multi-line JTextComponent subclasses. You can
 change the opacity, colors, font, and other properties to make it look the
 same as the the JLabel.
 */

/*
 JLabel for output
 Why using JLabel for output is usually bad

 It's possible to change the text of a JLabel, although this is not generally a good idea after
 the user interface is already displayed. For output JTextField is often a better choice.
 The use of JLabel for output is mentioned because some textbooks display output this way.
 Here are some reasons not to use it.

 * Can't copy to clipboard. The user can not copy text from a JLabel, but can from a JTextField.
 * Can't set background. Changing the background of individual components probably isn't a good
 idea, so this restriction on JLabels is not serious. You can change the background of a
 JTextField, for better or worse.
 * Text length. This is where there are some serious issues. You can always see the entire text
 in a JTextField, although you might have to scroll it it's long. There are several
 possibilities with a JLabel. You may either not see all of the long text in a JLabel,
 or putting long text into a JLabel may cause the layout to be recomputed, resulting
 in a truly weird user experience.
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Vector;

import javax.swing.JCheckBox;

import edu.wustl.cab2b.client.ui.controls.Cab2bCheckBox;
import edu.wustl.cab2b.client.ui.controls.Cab2bHyperlink;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bStandardFonts;
import edu.wustl.cab2b.client.ui.controls.RiverLayout;
import edu.wustl.cab2b.client.ui.mainframe.MainFrame;
import edu.wustl.cab2b.client.ui.query.Utility;

/**
 * Each JPageElement should know its pageIndex(String) and index in that page.
 * This two index is needed for Creating an PageSelectionEvent and throwing it to JPagination.
 *
 *  JPagination can then maintain its SelectionModel properly.
 *
 * @author chetan_bh
 */
public class JPageElement extends Cab2bPanel implements ActionListener, PropertyChangeListener {

    private static final long serialVersionUID = 1L;

    /**
     * An <code>Cab2bHyperlink</code> for this composite component. 
     */
    private Cab2bHyperlink<JPageElement> hyperlink;

    private Cab2bLabel label;

    /**
     * A <code>JCheckBox</code> for enabling selections on the page elements.
     * displayed in a page panel. 
     */
    private JCheckBox checkBox;

    /**
     * A <code>JLabel</code> to display the description associated with each page elements.
     */
    private Cab2bLabel descriptionLabel;

    private PageElement pageElement;

    /**
     * A boolean to indicate whether this component is selectable or not.
     */
    private boolean isSelectable;

    private PageElementIndex elementIndex;

    /**
     * Pagination reference.
     */
    private JPagination pagination;

    private static int descLength = (int) ((MainFrame.getScreenDimension().getWidth() / 1024f) * 110f);

    /**
     * @param pagination
     * @param pageElement
     * @param isSelectable
     * @param pageElementIndex
     */
    public JPageElement(
            JPagination pagination,
            PageElement pageElement,
            boolean isSelectable,
            PageElementIndex pageElementIndex) {
        this.pagination = pagination;
        this.pageElement = pageElement;
        this.isSelectable = isSelectable;
        this.elementIndex = pageElementIndex;
        initGUI();
        initListeners();
        this.setMinimumSize(new Dimension(0, (int) this.getPreferredSize().getHeight() + 5));
    }

    private void initListeners() {
        if (pagination != null) {
            pagination.addPropertyChangeListener("isSelectable", this);
        }
    }

    /**
     * @return Pagination instance
     */
    public JPagination getPagination() {
        return pagination;
    }

    /**
     * @return Page Element
     */
    public PageElement getPageElement() {
        return pageElement;
    }

    /**
     * Initialize GUI for page element.
     */
    private void initGUI() {
        this.removeAll();
        this.setLayout(new RiverLayout(0, 0));

        hyperlink = new Cab2bHyperlink<JPageElement>(true);
        hyperlink.setText(pageElement.getDisplayName());
        hyperlink.setActionCommand(pageElement.getActionCommand());
        hyperlink.setUserObject(this);

        label = new Cab2bLabel(pageElement.getDisplayName());
        label.setFont(Cab2bStandardFonts.ARIAL_BOLD_12);

        String description = pageElement.getDescription();
        if (description == null) {
            description = "";
        }
        descriptionLabel = new Cab2bLabel(description);
        StringBuffer toolTipText = new StringBuffer(20);
        toolTipText.append("<HTML><P>" + Utility.getWrappedText(description, 75) + "</P></HTML>");
        descriptionLabel.setToolTipText(toolTipText.toString());

        if (description.length() > descLength) {
            String textDsc = description.substring(0, descLength) + "...";
            descriptionLabel.setText(textDsc);
        }

        if (isSelectable && pagination != null) {
            checkBox = new Cab2bCheckBox();
            checkBox.setOpaque(false);
            checkBox.addActionListener(this);
            checkBox.setSelected(pageElement.isSelected());

            this.add("br", checkBox);
            this.add("tab", hyperlink);
        } else {
            this.add("br tab", hyperlink);
        }
        String extraText = pageElement.getExtraDisplayText();
        if (extraText != null) {
            Cab2bLabel extraLabel = new Cab2bLabel(extraText);
            extraLabel.setFont(Cab2bStandardFonts.ARIAL_BOLD_12);
            extraLabel.setForeground(new Color(0,128,0));
            this.add("tab", extraLabel);
        }
        this.add("br tab", descriptionLabel);
    }

    /**
     * Resets the Hyperlink
     */
    public void resetHyperLink() {
        removeAll();
        if (isSelectable && pagination != null) {
            this.add("br", checkBox);
            this.add("tab", hyperlink);
        } else {
            this.add("br tab", hyperlink);
        }
        String extraText = pageElement.getExtraDisplayText();
        if (extraText != null) {
            Cab2bLabel extraLabel = new Cab2bLabel(extraText);
            extraLabel.setFont(Cab2bStandardFonts.ARIAL_BOLD_12);
            extraLabel.setForeground(new Color(0,128,0));
            this.add("tab", extraLabel);
        }
        this.add("br tab", descriptionLabel);
    }

    /**
     * Resets the Label
     */
    public void resetLabel() {
        removeAll();
        if (isSelectable && pagination != null) {
            this.add("br", checkBox);
            this.add("tab", label);
        } else {
            this.add("br tab", label);
        }
        String extraText = pageElement.getExtraDisplayText();
        if (extraText != null) {
            Cab2bLabel extraLabel = new Cab2bLabel(extraText);
            extraLabel.setFont(Cab2bStandardFonts.ARIAL_BOLD_12);
            extraLabel.setForeground(new Color(0,128,0));
            this.add("tab", extraLabel);
        }
        this.add("br tab", descriptionLabel);
    }

    /**
     * Returns true if this is selected, else false.
     * @return true if selected
     */
    public boolean isSelected() {
        return checkBox.isSelected();
    }

    /**
     * Capture events happening on JPageElements CheckBox 
     * Create a PageSelectionEvent and fire an selectionChangedEvent to JPagination.
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        if (pageElement.isSelected()) {
            pageElement.setSelected(false);
        } else {
            pageElement.setSelected(true);
        }

        PageSelectionEvent selectionEvent = new PageSelectionEvent(this, elementIndex);
        pagination.fireSelectionValueChanged(selectionEvent);
    }

    /**
     * @param ae
     */
    public void addHyperlinkActionListener(ActionListener ae) {
        hyperlink.addActionListener(ae);
    }

    /**
     * @param aes
     */
    public void addHyperlinkActionListeners(Vector<ActionListener> aes) {
        for (ActionListener ae : aes) {
            hyperlink.addActionListener(ae);
        }
    }

    /**
     * @param ae 
     */
    public void removeHyperlinkActionListener(ActionListener ae) {
        hyperlink.removeActionListener(ae);
    }

    /**
     * Property Change Listener, listens for specific changes in JPagination property change like "isSelectable" property.
     * @param propChangeEvent
     */
    public void propertyChange(PropertyChangeEvent propChangeEvent) {
        String propertyName = propChangeEvent.getPropertyName();
        Object newValue = propChangeEvent.getNewValue();
        if (propertyName.equals("isSelectable")) {
            Boolean newBoolValue = (Boolean) newValue;
            if (newBoolValue) {
                isSelectable = newBoolValue;
                initGUI();
                this.revalidate();
            } else {
                isSelectable = newBoolValue;
                this.remove(checkBox);
                this.revalidate();
            }
        }
    }
}