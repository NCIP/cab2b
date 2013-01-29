/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.searchDataWizard.dag.ambiguityresolver;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;

import edu.wustl.cab2b.client.ui.controls.Cab2bHyperlink;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bStandardFonts;
import edu.wustl.cab2b.client.ui.controls.CheckBoxTableModel;
import edu.wustl.cab2b.client.ui.controls.RiverLayout;
import edu.wustl.cab2b.client.ui.controls.TextAreaRenderer;
import edu.wustl.cab2b.common.util.Constants;
import edu.wustl.common.querysuite.metadata.path.IPath;

/**
 * A Panel which lists all the available paths between the source target
 * entities, a filter is provided to filter the paths based on some criteria.
 * 
 * @author chetan_bh
 */
public class AvailablePathsPanel extends AbstractAmibuityResolver {
    private static final long serialVersionUID = 1L;

    /**
     * Map of all paths
     */
    private Map<String, List<IPath>> allPathMap;

    /**
     * Hyperlink panel
     */
    private Cab2bPanel hyperlinkPanel;

    /**
     * Hyperlink for displaying general paths 
     */
    private Cab2bHyperlink generalPathLink;

    /**
     * Hyperlink for displaying curated paths
     */
    private Cab2bHyperlink curatedPathLink;

    /**
     * Parameterized constructor
     * @param allPathMap Map of all paths
     */
    public AvailablePathsPanel(Map<String, List<IPath>> allPathMap) {
        this.allPathMap = allPathMap;
        this.userSelectedPaths = new ArrayList<IPath>();
        initializeGUI();
    }

    /**
     * Initializes the GUI.
     */
    protected void initializeGUI() {
        hyperlinkPanel = createHyperLinkPanel();
        this.add(hyperlinkPanel, BorderLayout.NORTH);
        addTablePanel();
        buttonPanel = getButtonPanel();
        this.add(buttonPanel, BorderLayout.SOUTH);

        revalidate();
        updateUI();
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.client.ui.searchDataWizard.dag.ambiguityresolver.AbstractAmibuityResolver#addTablePanel()
     */
    protected void addTablePanel() {
        AbstractTableModel abstractTableModel = getAmbiguityTableModel();
        ambiguityPathTable = createAmbiguityPathTable(abstractTableModel);
        TableColumnModel tableColumnModel = ambiguityPathTable.getColumnModel();
        tableColumnModel.getColumn(1).setCellRenderer(new TextAreaRenderer());
        Cab2bPanel tablePanel = createTablePanel(ambiguityPathTable);
        this.add(tablePanel, BorderLayout.CENTER);
    }

    /**
     * Method returning panel with "Submit" and "Cancel" buttons 
     * @return
     */
    private Cab2bPanel getButtonPanel() {
        ActionListener submitButtonListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                userSelectedPaths.clear();
                CheckBoxTableModel checkBoxTableModel = (CheckBoxTableModel) ambiguityPathTable.getModel();
                int[] selectedPathsIndexes = checkBoxTableModel.getCheckedRowIndexes();
                for (int i = 0; i < selectedPathsIndexes.length; i++) {
                    userSelectedPaths.add(selectedPathList.get(selectedPathsIndexes[i]));
                }
                parentWindow.dispose();
            }
        };
        return createButtonPanel(submitButtonListener);
    }

    /**
     * Returns the data for the Ambiguity Resolver table
     * @return Array of Object array containing the table data
     */
    protected AbstractTableModel getAmbiguityTableModel() {
        int rowIndex = 0;
        Object[][] ambiguityTableData = new Object[selectedPathList.size()][3];
        int pathPopularity = (int) (1 / (double) selectedPathList.size() * 100.00);
        for (IPath path : selectedPathList) {
            ambiguityTableData[rowIndex][0] = new Boolean(false);
            ambiguityTableData[rowIndex][1] = getFullPathNames(path);
            ambiguityTableData[rowIndex][2] = pathPopularity + " %";
            rowIndex++;
        }

        CheckBoxTableModel checkBoxTableModel = new CheckBoxTableModel(AMBIGUITY_PATH_TABLE_HEADERS,
                ambiguityTableData);
        return checkBoxTableModel;
    }

    /**
     * This method returns the searchPanel of hyperlinks containing link of General Path and Currated Path respectively.
     * @return searchPanel of hyperlinks
     */
    private Cab2bPanel createHyperLinkPanel() {
        Cab2bPanel hyperlinkPanel = new Cab2bPanel(new RiverLayout());
        hyperlinkPanel.setSize(Constants.WIZARD_NAVIGATION_PANEL_DIMENSION);

        List<IPath> generalPathList = allPathMap.get(Constants.GENERAL_PATH);
        generalPathLink = createPathHyperLink(Constants.GENERAL_PATH, generalPathList);
        hyperlinkPanel.add("tab ", generalPathLink);

        List<IPath> curatedPathList = allPathMap.get(Constants.CURATED_PATH);
        if (!curatedPathList.isEmpty()) {
            selectedPathList = curatedPathList;
            curatedPathLink = createPathHyperLink(Constants.CURATED_PATH, curatedPathList);
            Cab2bLabel curatedPathLabel = new Cab2bLabel(Constants.CURATED_PATH, Cab2bStandardFonts.ARIAL_BOLD_12);
            hyperlinkPanel.add("tab ", curatedPathLabel);
        } else {
            selectedPathList = generalPathList;
        }

        return hyperlinkPanel;
    }

    /**
     * Update hyperlink panel
     * @param component1
     * @param component2
     */
    private void refreshHyperlinkPanel(Component component1, Component component2) {
        hyperlinkPanel.removeAll();
        hyperlinkPanel.add("tab ", component1);
        hyperlinkPanel.add("tab ", component2);
    }

    /**
     * This method creates a hyperlink given the name of the link and the object to be embedded
     * @param linkName name of the link
     * @param pathList object to be embedded
     * @return a hyperlink
     */
    private Cab2bHyperlink createPathHyperLink(final String linkName, final List<IPath> pathList) {
        Cab2bHyperlink<List<IPath>> pathHyperLink = new Cab2bHyperlink<List<IPath>>();
        pathHyperLink.setBounds(new Rectangle(5, 5, 5, 5));
        pathHyperLink.setText(linkName);
        pathHyperLink.setActionCommand(linkName);
        pathHyperLink.setUserObject(pathList);

        pathHyperLink.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                Cab2bHyperlink<List<IPath>> pathHyperLink = (Cab2bHyperlink<List<IPath>>) actionEvent.getSource();
                String linkClicked = actionEvent.getActionCommand();
                if (linkClicked.equals(Constants.CURATED_PATH)) {
                    Cab2bLabel curatedPathLabel = new Cab2bLabel(Constants.CURATED_PATH,
                            Cab2bStandardFonts.ARIAL_BOLD_12);
                    refreshHyperlinkPanel(generalPathLink, curatedPathLabel);
                } else if (curatedPathLink != null) {
                    Cab2bLabel generalPathLabel = new Cab2bLabel(Constants.GENERAL_PATH,
                            Cab2bStandardFonts.ARIAL_BOLD_12);
                    refreshHyperlinkPanel(generalPathLabel, curatedPathLink);
                }
                selectedPathList = pathHyperLink.getUserObject();
                refreshAmbiguityTable();
            }
        });

        return pathHyperLink;
    }

    /**
     * This method refreshes the table data
     */
    protected void refreshAmbiguityTable() {
        this.removeAll();
        this.add(hyperlinkPanel, BorderLayout.NORTH);
        addTablePanel();
        this.add(buttonPanel, BorderLayout.SOUTH);

        revalidate();
        updateUI();
    }

    /**
     * Returns the users path selection in a map.
     * 
     * @return
     */
    public List<IPath> getUserSelectedPaths() {
        return userSelectedPaths;
    }

}
