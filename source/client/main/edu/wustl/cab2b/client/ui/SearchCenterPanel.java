package edu.wustl.cab2b.client.ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.util.Vector;

import javax.swing.BorderFactory;

import org.jdesktop.swingx.JXPanel;

import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;

/**
 * The center searchPanel from the main search dialog. The searchPanel uses a card layout to
 * manage all the cards. Each card is in turn a searchPanel corresponding to each tab
 * in th main search dialog.
 * 
 * @author mahesh_iyer
 * 
 */

public class SearchCenterPanel extends Cab2bPanel {
    private static final long serialVersionUID = 1L;

    /** An array of all the cards to be added to this searchPanel.*/
    private JXPanel[] arrCards = new Cab2bPanel[5];

    /** Identifier to identify the card corresponding to the advanced search searchPanel.*/
    private static final String strIdentifierChooseCateglbl = "Choose Search Category";

    /** Identifier to identify the card corresponding to the Add Limit searchPanel.*/
    private static final String strIdentifierAddLimitlbl = "Add Limit";

    /** Identifier to identify the card corresponding to the Define Search Results searchPanel.*/
    private static final String strDefineSearchResultslbl = "Define Search Results View";

    /** Identifier to identify the card corresponding to the View Search Results searchPanel.*/
    private static final String strViewSearchResultslbl = "View Search Results";

    /** Identifier to identify the card corresponding to the Data List searchPanel.*/
    private static final String strDataListlbl = "Data List";

    /** Index to indicate the currently selected index. Initialized to default value of 0.*/
    private int iCurrentlySelectedCard = 0;

    private AddLimitPanel addLimitPanel;

    private ChooseCategoryPanel chooseCategPanel;

    /**
     * HashMap of identifiers. This would be used by the navigation searchPanel to
     * bring up the appropriate card.
     */
    private Vector<String> vIdentifiers = new Vector<String>();

    /**
     * The method returns the number of cards currently selected
     * 
     * @return int The current card count.
     */
    public int getIdentifierCount() {
        return this.vIdentifiers.size();
    }

    /**
     * The method returns the index for the currently selected card.
     * 
     * @return int The index of the currently selected card.
     */
    public int getSelectedCardIndex() {
        return this.iCurrentlySelectedCard;
    }

    /**
     * The method returns the currently selected card.
     * 
     * @return JXPanel The currently selected card.
     */
    public JXPanel getSelectedCard() {

        return this.arrCards[this.getSelectedCardIndex()];
    }

    /**
     * The method sets the index of the currently selected card.
     * 
     * @param  iSelectedCard index of the card to be selected.
     */
    public void setSelectedCardIndex(int iSelectedCard) {
        this.iCurrentlySelectedCard = iSelectedCard;
    }

    /**
     * Constructor.
     */
    SearchCenterPanel() {
        initGUI();
        this.setBorder(new SearchDialogBorder());
    }

    /**
     * The method initializes the tabbed pane.
     */
    private void initGUI() {
        /*Set the card layout for the center searchPanel.*/
        this.setLayout(new CardLayout());

        /* First card initialization.*/
        chooseCategPanel = new ChooseCategoryPanel();
        this.add(chooseCategPanel, strIdentifierChooseCateglbl);
        chooseCategPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 220)));

        /*
         * Add the identifier to the vector, where from it would be refrenced by
         * the navigation searchPanel.
         */
        this.vIdentifiers.add(strIdentifierChooseCateglbl);
        this.arrCards[0] = chooseCategPanel;

        /* Second card initialization.*/
        addLimitPanel = new AddLimitPanel();
        this.add(addLimitPanel, strIdentifierAddLimitlbl);
        this.vIdentifiers.add(strIdentifierAddLimitlbl);
        this.arrCards[1] = addLimitPanel;

        this.vIdentifiers.add(strDefineSearchResultslbl);
        this.vIdentifiers.add(strViewSearchResultslbl);
        this.vIdentifiers.add(strDataListlbl);
    }

    /**
     * The method returns the identifier associated with the given index.
     * 
     * @param index
     * @return
     */
    public String getIdentifier(int index) {
        return (String) this.vIdentifiers.elementAt(index);
    }

    public void reset() {
        addLimitPanel.resetPanel();
    }

    /**
     * get the choose category searchPanel instance 
     * @return the choose category searchPanel instance
     */
    public ChooseCategoryPanel getChooseCategoryPanel() {
        return chooseCategPanel;
    }

    /**
     * get the add limit searchPanel instance 
     * @return the add limit searchPanel instance
     */
    public AddLimitPanel getAddLimitPanel() {
        return addLimitPanel;
    }

    public void setChooseCategoryPanel(ChooseCategoryPanel chooseCategoryPanel) {
        this.chooseCategPanel = chooseCategoryPanel;
        this.add(chooseCategPanel, strIdentifierChooseCateglbl);
        this.vIdentifiers.add(strIdentifierChooseCateglbl);
        this.arrCards[0] = chooseCategPanel;
    }

    public void setAddLimitPanel(AddLimitPanel addLimitPanel) {
        this.addLimitPanel = addLimitPanel;
        this.add(addLimitPanel, strIdentifierAddLimitlbl);
        this.vIdentifiers.add(strIdentifierAddLimitlbl);
        this.arrCards[1] = addLimitPanel;
    }

    /**
     * @return the arrCards
     */
    public JXPanel[] getArrCards() {
        return arrCards;
    }

    /**
     * @return the arrCards
     */
    public JXPanel getArrCardElement(int index) {
        return arrCards[index];
    }

    /**
     * @return the arrCards
     */
    public void setArrCardElement(JXPanel panel, int index) {
        arrCards[index] = panel;
    }

    /**
     * @param arrCards the arrCards to set
     */
    public void setArrCards(JXPanel[] arrCards) {
        this.arrCards = arrCards;
    }

    /**
     * @return the strDataListlbl
     */
    public static String getStrDataListlbl() {
        return strDataListlbl;
    }

    /**
     * @return the strViewSearchResultslbl
     */
    public static String getStrViewSearchResultslbl() {
        return strViewSearchResultslbl;
    }

    /**
     * @return the strDefineSearchResultslbl
     */
    public static String getStrDefineSearchResultslbl() {
        return strDefineSearchResultslbl;
    }

    /**
     * @return the strIdentifierAddLimitlbl
     */
    public static String getStrIdentifierAddLimitlbl() {
        return strIdentifierAddLimitlbl;
    }

    /**
     * @return the strIdentifierChooseCateglbl
     */
    public static String getStrIdentifierChooseCateglbl() {
        return strIdentifierChooseCateglbl;
    }

}
