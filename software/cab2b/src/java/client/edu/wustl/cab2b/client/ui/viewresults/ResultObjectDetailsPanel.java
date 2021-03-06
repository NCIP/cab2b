/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.viewresults;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.GridLayout;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JScrollPane;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.painter.gradient.BasicGradientPainter;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTitledPanel;
import edu.wustl.cab2b.client.ui.controls.RiverLayout;
import edu.wustl.cab2b.common.datalist.IDataRow;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.queryengine.result.IRecordWithAssociatedIds;
import edu.wustl.common.querysuite.metadata.associations.IInterModelAssociation;

/**
 * A Panel to show any entity's details.
 * @author chetan_bh
 */
public class ResultObjectDetailsPanel extends ResultPanel {
    private static final long serialVersionUID = 1L;

    protected IRecord record;

    protected IDataRow dataRow;

    protected JXPanel tablePanel;

    private DefaultDetailedPanel defaultDetailedPanel;

    private JXTitledPanel relatedDataTitledPanel;

    private JXPanel relatedDataPanel;

    private String currentBreadCrumbName;

    private Cab2bPanel m_sidePanel;

    public ResultObjectDetailsPanel(
            SimpleSearchResultBreadCrumbPanel searchPanel,
            IDataRow dataRow,
            IRecord record,
            Collection<AssociationInterface> incomingAssociationCollection,
            List<IInterModelAssociation> intraModelAssociationCollection,
            DefaultDetailedPanel defaultDetailedPanel) {
        /* Set the parent entity interface. Note : This can never be null. */
        super(searchPanel, incomingAssociationCollection, intraModelAssociationCollection);

        currentBreadCrumbName = searchPanel.getCurrentBreadCrumbName();
        this.defaultDetailedPanel = defaultDetailedPanel;
        this.record = record;
        this.dataRow = dataRow;
    }

    /**
     * @see edu.wustl.cab2b.client.ui.viewresults.ResultPanel#doInitialization()
     */
    public void doInitialization() {
        initData();
        initRelatedDataPanel();
        initGUI();
    }

    public void addDataSummaryPanel() {
        if (myDataListPanel != null && myDataListPanel.getComponentCount() > 1) {
            m_applyAllButton.setEnabled(true);
        }
        m_sidePanel.add(myDataListParentPanel, BorderLayout.CENTER);
    }

    protected void initData() {
    }

    protected void initGUI() {
        this.setLayout(new BorderLayout());
        //initTableGUI();        
        this.add(defaultDetailedPanel, BorderLayout.CENTER);

        initDataListButtons();
        Cab2bPanel buttonPanel = new Cab2bPanel(new RiverLayout(5, 5));
        buttonPanel.add(addToDataListButton);
        buttonPanel.add(m_applyAllButton);

        this.add(buttonPanel, BorderLayout.SOUTH);

        m_sidePanel = new Cab2bPanel(new GridLayout(2, 1, 5, 5));
        initDataListSummaryPanel();
        //fix for matching the sizes of "my data summrary" and "related data"
        Cab2bPanel relatedDataParentPanel = new Cab2bPanel(new RiverLayout(0, 5));
        relatedDataParentPanel.setBorder(null);
        relatedDataParentPanel.add("br hfill vfill", relatedDataTitledPanel);

        m_sidePanel.add(relatedDataParentPanel);
        m_sidePanel.add(myDataListParentPanel);

        this.add(m_sidePanel, BorderLayout.EAST);

    }

    private void initRelatedDataPanel() {
        relatedDataTitledPanel = new Cab2bTitledPanel("Related Data: " + currentBreadCrumbName);
        GradientPaint gp1 = new GradientPaint(new Point2D.Double(.05d, 0), new Color(185, 211, 238),
                new Point2D.Double(.95d, 0), Color.WHITE);
        relatedDataTitledPanel.setTitleFont(new Font("SansSerif", Font.BOLD, 11));
        relatedDataTitledPanel.setTitlePainter(new BasicGradientPainter(gp1));
        relatedDataTitledPanel.setTitleForeground(Color.BLACK);
        relatedDataTitledPanel.setBorder(null);

        relatedDataPanel = new Cab2bPanel(new RiverLayout(0, 10));
        relatedDataPanel.setBackground(Color.WHITE);
        relatedDataPanel.setBorder(null);

        if (incomingAssociationCollection != null && !incomingAssociationCollection.isEmpty()) {
            AbstractAssociatedDataPanel incomingPanel = new IncomingAssociationDataPanel(
                    incomingAssociationCollection, searchPanel.getAssociatedDataAL(), dataRow, record);
            relatedDataPanel.add(incomingPanel);
        }

        /* Add outgoing association if supported
         * 
         */
        if (record instanceof IRecordWithAssociatedIds) {
            Collection outgoingIntraModelAssociationCollection = dataRow.getEntity().getAssociationCollection();
            AbstractAssociatedDataPanel outgoingPanel = new OutgoingAssociationDataPanel(
                    outgoingIntraModelAssociationCollection, searchPanel.getAssociatedDataAL(), dataRow, record);
            relatedDataPanel.add("br ", outgoingPanel);
        }

        /*
         * We also must be able to add Intermodel association. Call the Path
         * finder code to add the intermodel association.
         */
        if (intraModelAssociationCollection != null && !intraModelAssociationCollection.isEmpty()) {
            AbstractAssociatedDataPanel interModelPanel = new InterModelAssociationDataPanel(
                    intraModelAssociationCollection, searchPanel.getAssociatedDataAL(), dataRow, record);
            relatedDataPanel.add("br ", interModelPanel);
        }

        relatedDataPanel.add("br", new Cab2bLabel("       "));
        JScrollPane relatedDataPane = new JScrollPane();
        relatedDataPane.getViewport().setBackground(Color.WHITE);
        relatedDataPane.getViewport().add(relatedDataPanel);
        relatedDataTitledPanel.add(relatedDataPane);
    }

    /**
     * @see edu.wustl.cab2b.client.ui.viewresults.ResultPanel#getSelectedDataRows()
     */
    List<IDataRow> getSelectedDataRows() {
        List<IDataRow> selectedDataRows = new ArrayList<IDataRow>();
        selectedDataRows.add(dataRow);
        return selectedDataRows;
    }

}