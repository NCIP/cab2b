/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.parameterizedQuery;

import java.awt.Dimension;

import javax.swing.ButtonGroup;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bRadioButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bTextField;
import edu.wustl.cab2b.client.ui.controls.Cab2bTitledPanel;
import edu.wustl.cab2b.client.ui.controls.RiverLayout;

/**
 * Class to generate Parameterized Query Information GUI
 * @author Deepak_Shingan
 *
 */
public class ParameterizedQueryInfoPanel extends Cab2bTitledPanel {
    private static final long serialVersionUID = 1L;

    /** Query name holder */
    private Cab2bTextField titleTextField;

    /** Query description holder */
    private JTextArea queryTextArea;

    /** Indicates if the query is for keyword search */
    private Cab2bRadioButton keywordSearch;

    /** Indicates if the query is for Form Based search */
    private Cab2bRadioButton formBasedSearch;

    /** Indicates if the query is for Both types of search */
    private Cab2bRadioButton formAndKeywordSearch;

    /** Default constructor */
    public ParameterizedQueryInfoPanel() {
        initGUI();
    }

    /**
     * This method initializes the information QueryPanel
     */
    private void initGUI() {
        Cab2bLabel titleLabel = new Cab2bLabel("Title");
        titleTextField = new Cab2bTextField("", new Dimension(225, 22));

        Cab2bLabel descriptionLabel = new Cab2bLabel("Description");
        queryTextArea = new JTextArea();
        queryTextArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(queryTextArea);
        scrollPane.setPreferredSize(new Dimension(225, 60));

        formBasedSearch = new Cab2bRadioButton("Saved search");

        keywordSearch = new Cab2bRadioButton("Keyword search");

        formAndKeywordSearch = new Cab2bRadioButton("Both");

        ButtonGroup typesOfQueries = new ButtonGroup();
        typesOfQueries.add(formBasedSearch);
        typesOfQueries.add(keywordSearch);
        typesOfQueries.add(formAndKeywordSearch);
        formBasedSearch.setSelected(Boolean.TRUE);

        Cab2bPanel containerPanel = new Cab2bPanel(new RiverLayout(5, 5));
        containerPanel.add(titleLabel);
        containerPanel.add("tab ", titleTextField);
        containerPanel.add("br ", descriptionLabel);
        containerPanel.add("tab ", scrollPane);
        containerPanel.add("br ", new Cab2bLabel());
        containerPanel.add("tab ", formBasedSearch);
        containerPanel.add("tab ", keywordSearch);
        containerPanel.add("tab ", formAndKeywordSearch);

        this.setTitle("Information");
        this.setContentContainer(containerPanel);
    }

    /**
     * This method returns the query name
     * @return query name written in "Title" TextBox
     */
    public String getQueryName() {
        return titleTextField.getText();
    }

    /**
     * This method returns the query description
     * @return query description written in "Description" TextBox
     */
    public String getQueryDecription() {
        return queryTextArea.getText();
    }

    /**
     * This method return true if query is marked for keyword search; false otherwise.
     * @return Boolean
     */
    public Boolean isKeywordSearch() {
        return keywordSearch.isSelected();
    }

    /**
     * This method returns true if query is to be saved for both Searches : saved and keyword  
     * @return Boolean
     */
    public Boolean isFormAndKeywordSearch() {
        return formAndKeywordSearch.isSelected();
    }

    /**
     * This method disables the keyword search and Both search option for MMC query 
     */
    public void setKeywordAndBothOptionDisabled() {
        keywordSearch.setVisible(false);
        formAndKeywordSearch.setVisible(false);
    }
}
