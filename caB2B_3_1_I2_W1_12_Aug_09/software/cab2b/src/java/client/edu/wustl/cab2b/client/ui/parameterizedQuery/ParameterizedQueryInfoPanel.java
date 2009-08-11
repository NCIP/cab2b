package edu.wustl.cab2b.client.ui.parameterizedQuery;

import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import edu.wustl.cab2b.client.ui.controls.Cab2bCheckBox;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
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
    private Cab2bCheckBox keywordSearch;

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

        keywordSearch = new Cab2bCheckBox();
        keywordSearch.setText("Query for keyword search");

        Cab2bPanel containerPanel = new Cab2bPanel(new RiverLayout(5, 5));
        containerPanel.add(titleLabel);
        containerPanel.add("tab ", titleTextField);
        containerPanel.add("br ", descriptionLabel);
        containerPanel.add("tab ", scrollPane);
        containerPanel.add("br ", new Cab2bLabel());
        containerPanel.add("tab ", keywordSearch);

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
     * @return
     */
    public Boolean isKeywordSearch() {
        return keywordSearch.isSelected();
    }

    public void setKeywordSearchDisabled() {
        keywordSearch.setVisible(false);
    }
}
