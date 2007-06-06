package edu.wustl.cab2b.client.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Keymap;

import org.jdesktop.swingx.JXPanel;

import edu.wustl.cab2b.client.cache.ClientSideCache;
import edu.wustl.cab2b.client.metadatasearch.MetadataSearch;
import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.client.ui.util.CustomSwingWorker;
import edu.wustl.cab2b.common.beans.MatchedClass;
import edu.wustl.cab2b.common.cache.IEntityCache;
import edu.wustl.cab2b.common.exception.CheckedException;

/**
 * The abstract class that contains commonalities between the advanced/category
 * search panels for the main as well as 'AddLimit' section from the main search
 * dialog. Concrete classes must over ride methods to effect custom layout.
 * 
 * @author mahesh_iyer
 * 
 */

public abstract class AbstractSearchPanel extends Cab2bPanel {

    /**
     * The reference to the parent content panel required to be refreshed for
     * the appropritate event.
     */
    private ContentPanel m_addLimitPanel;

    /**
     * A generic reference to the specific implementation of the advanced search
     * panel.
     */
    private AbstractAdvancedSearchPanel m_advSearchPanel;

    private boolean isErrorDispayed = false;

    /** A specific implementation of the results panel. */
    private AbstractSearchResultPanel m_srhResultPanel;

    /** Text field to specify the search term. */
    protected JTextField m_srhTextField;

    /** search button.*/
    protected JButton m_srhButton;

    /**Error message panel**/
    Cab2bPanel errorMsgPanel;

    /**
     * constructor
     * 
     * @param addLimitPanel
     *            The reference to the parent content panel that is saved, so
     *            that it can be made available to child components, which can
     *            then cause the parent to refresh for appropriate events.
     */

    public AbstractSearchPanel(ContentPanel addLimitPanel) {
        this.m_addLimitPanel = addLimitPanel;
        initGUI();
    }

    /**
     * Method initializes the panel by appropriately laying out child components.
     * 
     */

    private void initGUI() {
        /* Set the layout.*/
        this.setLayout(new RiverLayout());

        /* Invoke the method to get the specific type of Advanced search panel to be added*/
        m_advSearchPanel = this.getAdvancedSearchPanel();

        /* Intialize the Search button.*/
        m_srhButton = new Cab2bButton("Search");
        m_srhButton.setEnabled(false);
        m_srhButton.addActionListener(new SearchActionListener(this.m_addLimitPanel));

        /* Intializa the text field.*/
        m_srhTextField = new JTextField();
        Keymap keyMap = m_srhTextField.addKeymap("enter", m_srhTextField.getKeymap());
        KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        keyMap.addActionForKeyStroke(key, new SearchActionListener(this.m_addLimitPanel));
        m_srhTextField.setKeymap(keyMap);

        /* Add a listener to the text-field.*/
        m_srhTextField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent arg0) {
                m_srhButton.setEnabled(true);
            }

            public void removeUpdate(DocumentEvent arg0) {
                if (arg0.getDocument().getLength() == 0) {
                    m_srhButton.setEnabled(false);
                }
            }

            public void changedUpdate(DocumentEvent arg0) {
                /* No implementation for this method is required.*/
            }
        });

        //m_srhTextField.setBorder(BorderFactory.createLoweredBevelBorder());
        m_srhTextField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 220)));
        /* Invoke the method based on concrete implementations from sub-class*/
        addTextField();

        /* Add the components to the panel.*/
        this.add(m_srhButton);
        this.add("br", m_advSearchPanel);
    }

    /**
     * Getter method for returning a reference to the text field.
     * 
     */
    JTextField getTextField() {
        return m_srhTextField;
    }

    /**
     * The method clears any previously searched results, by removing the
     * corresponding panel.
     * 
     * @param resultPanel
     *            The results panel to be removed
     * 
     */
    private void removeResultPanel(AbstractSearchResultPanel resultPanel) {
        this.remove(this.m_srhResultPanel);
        this.updateUI();
    }

    private void removeErrorPanel(Cab2bPanel errorPanel) {
        this.remove(this.errorMsgPanel);
        this.updateUI();
    }

    /**
     * The method adds the {@link AddLimitSearchResultPanel}dynamically to this panel. 
     * 
     * @param resultPanel
     *            The results panel to be added.
     */
    private void addResultsPanel(JXPanel resultPanel) {
        this.add("p vfill", resultPanel);
        this.updateUI();
    }

    /**
     * Action listener for the text field as well as the search button.
     * 
     */

    private class SearchActionListener extends AbstractAction {
        /** Component reference to pass to Error dialog boxes, for centering dialogs. */
        Component comp;

        public SearchActionListener(Component comp) {
            this.comp = comp;
        }

        public void actionPerformed(ActionEvent ae) {
            /* Read the value from the text field.*/
            String value = m_srhTextField.getText();
            value = CommonUtils.removeContinuousSpaceCharsAndTrim(value);
            final String[] values = value.split("\\s");

            /* Invoke the method to determing the combination of search.*/
            final int[] searchTargetStatus = m_advSearchPanel.getSearchTargetStatus();
            final int searchOn = m_advSearchPanel.getSearchOnStatus();

            CustomSwingWorker swingWorker = new CustomSwingWorker(comp) {
                Set dummySrhResult = null;

                protected void doNonUILogic() throws RuntimeException {

                    try {
                        IEntityCache cache = ClientSideCache.getInstance();
                        MetadataSearch metadataSearch = new MetadataSearch(cache);
                        MatchedClass matchedClass = metadataSearch.search(searchTargetStatus, values, searchOn);
                        /* The results that is the collection of entities. */
                        dummySrhResult = matchedClass.getEntityCollection();

                        //                      }
                    } catch (CheckedException e1) {
                        CommonUtils.handleException(e1, comp, true, true, true, false);
                    }
                }

                @Override
                protected void doUIUpdateLogic() throws RuntimeException {
                    if (m_srhResultPanel != null) {
                        /* Remove any previously added results panel.*/
                        removeResultPanel(m_srhResultPanel);
                    }

                    //replace previous panel only when we got some results 
                    if (dummySrhResult.size() > 0 && dummySrhResult != null) {
                        /* Add an appropriate instance of the search results panel to this panel */
                        m_srhResultPanel = getSearchResultPanel(m_addLimitPanel, dummySrhResult);
                        //System.out.println("CONTAINER : "+m_srhResultPanel);					
                        m_addLimitPanel.setSearchResultPanel(m_srhResultPanel);

                        addResultsPanel(m_srhResultPanel);
                        if (isErrorDispayed) {
                            removeErrorPanel(errorMsgPanel);
                        }
                        isErrorDispayed = false;
                    } else {
                        if (isErrorDispayed == false) {
                            //show error msg
                            Cab2bLabel errorMsg = new Cab2bLabel("No result found.");
                            errorMsgPanel = new Cab2bPanel();
                            errorMsgPanel.add(errorMsg);
                            addResultsPanel(errorMsgPanel);
                            isErrorDispayed = true;
                        }
                    }
                }
            };
            swingWorker.start();
        }
    }

    /**
     * The abstract method returns the appropriate type of
     * {@link AbstractSearchResultPanel} to be added to this panel. Sub-classes
     * are required to over-ride this method.
     * 
     * @param addLimitPanel
     *            The reference to the parent content panel required by the a
     *            specific instance of {@link AbstractSearchResultPanel} to be
     *            refreshed for the appropritate events it can generate.
     * 
     * @param searchResult
     *            The collection of Entities
     */
    public abstract AbstractSearchResultPanel getSearchResultPanel(ContentPanel addLimitPanel, Set searchResult);

    /**
     * The abstract method returns the appropriate type of
     * {@link AbstractAdvancedSearchPanel} to be added to this panel. Sub-classes
     * are required to over-ride this method.
     * 
     * @return AbstractAdvancedSearchPanel 
     */

    public abstract AbstractAdvancedSearchPanel getAdvancedSearchPanel();

    /**
     * The abstract method to add the text field in a manner required by the
     * specific instance of {@link AbstractSearchPanel}
     * 
     */
    public abstract void addTextField();

}
