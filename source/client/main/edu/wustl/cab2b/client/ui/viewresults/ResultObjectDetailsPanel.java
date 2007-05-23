package edu.wustl.cab2b.client.ui.viewresults;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import javax.swing.JScrollPane;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.painter.gradient.BasicGradientPainter;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTable;
import edu.wustl.cab2b.client.ui.controls.Cab2bTitledPanel;
import edu.wustl.cab2b.client.ui.controls.CustomizableBorder;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.datalist.DataRow;
import edu.wustl.cab2b.common.datalist.IDataRow;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.queryengine.result.IRecordWithAssociatedIds;
import edu.wustl.common.querysuite.metadata.associations.IInterModelAssociation;

/**
 * A Panel to show any entity's details.
 * @author chetan_bh
 */
public class ResultObjectDetailsPanel extends ResultPanel {
    Object[] objectDetails;

    List<AttributeInterface> attributes; // List<IAttribute> attributes;

    Cab2bTable objDetailsTable;

    Vector<Vector<Object>> tableData;

    Vector<String> tableHeader;

    JXTitledPanel relatedDataTitledPanel;

    JXPanel relatedDataPanel;

   
    ActionListener associatedDataActionListener;

    ActionListener breadCrumbActionListener;

    JXPanel breadCrumbPanel;

    EntityInterface parentEntityInterface = null;

    ViewSearchResultsPanel viewPanel;

    Object id = null;

    DataRow dataRow;


    private Cab2bPanel m_sidePanel;

    /**
     * 
     */
    private IRecord record;

    

    public ResultObjectDetailsPanel(SimpleSearchResultBreadCrumbPanel searchPanel, DataRow dataRow,

    IRecord record, Collection<AssociationInterface> incomingAssociationCollection, List<IInterModelAssociation> intraModelAssociationCollection) {
        /* Set the parent entity interface. Note : This can never be null. */
        super(searchPanel,incomingAssociationCollection,intraModelAssociationCollection);
        
        this.dataRow = dataRow;
        this.parentEntityInterface = dataRow.getEntityInterface();
        this.id = dataRow.getId();
        this.objectDetails = dataRow.getRow();
        this.attributes = searchPanel.getAttributes();

        this.record = record;
        

        breadCrumbActionListener = searchPanel.getBreadCrumbsAL();
        associatedDataActionListener = searchPanel.getAssociatedDataAL();
        this.viewPanel = searchPanel.viewPanel;

        initData();
        initRelatedDataPanel();
        initGUI();
    }

    public void addDataSummaryPanel() {
        m_sidePanel.add(myDataListTitledPanel, BorderLayout.CENTER);
    }

    private void initData() {
        tableData = new Vector<Vector<Object>>();
        Vector<Object> row = new Vector<Object>();
        for (int i = 0; i < objectDetails.length; i++) {
            AttributeInterface attribute = attributes.get(i);
            String formattedString = CommonUtils.getFormattedString(attribute.getName());
            row.add(formattedString);
            row.add(objectDetails[i]);
            tableData.add(row);
            row = new Vector<Object>();
        }

        tableHeader = new Vector<String>();
        // TODO remove this hard coding, or externalize this strings.
        tableHeader.add("Attribute");
        tableHeader.add("Value");
    }

    private void initGUI() {
        this.setLayout(new BorderLayout());

        final JXPanel detailsTablePanel = new Cab2bPanel(new RiverLayout());

        objDetailsTable = new Cab2bTable(false,tableData, tableHeader);
        objDetailsTable.setEditable(false);

        JScrollPane tableSP = new JScrollPane(objDetailsTable);
        // tableSP.setPreferredSize(new Dimension(700,600));

        detailsTablePanel.add("br hfill vfill", tableSP);

        initDataListButtons();

        detailsTablePanel.add("br", addToDataListButton);
        detailsTablePanel.add("tab tab", m_applyAllButton);

        this.add(detailsTablePanel, BorderLayout.CENTER);

        m_sidePanel = new Cab2bPanel(new GridLayout(2, 1, 5, 5));

        initDataListSummaryPanel();

        m_sidePanel.add(relatedDataTitledPanel);
        m_sidePanel.add(myDataListTitledPanel);
        this.add(m_sidePanel, BorderLayout.EAST);
    }

    private void initRelatedDataPanel() {
        relatedDataTitledPanel = new Cab2bTitledPanel("Related Data");
        GradientPaint gp1 = new GradientPaint(new Point2D.Double(.05d, 0), new Color(185, 211, 238),
                new Point2D.Double(.95d, 0), Color.WHITE);
        relatedDataTitledPanel.setTitlePainter(new BasicGradientPainter(gp1));
        relatedDataTitledPanel.setTitleForeground(Color.BLACK);

        relatedDataPanel = new Cab2bPanel();
        relatedDataPanel.setBackground(Color.WHITE);
        relatedDataPanel.setLayout(new RiverLayout(5, 10));
        
        if (incomingAssociationCollection != null && !incomingAssociationCollection.isEmpty()) {
            AbstractAssociatedDataPanel incomingPanel = new IncomingAssociationDataPanel(
                    incomingAssociationCollection, associatedDataActionListener, id, dataRow, record);
            relatedDataPanel.add(incomingPanel);
        }

        /* Add outgoing association if supported
         * 
         */
        if (record instanceof IRecordWithAssociatedIds) {
            Collection outgoingIntraModelAssociationCollection = dataRow.getEntityInterface().getAssociationCollection();
            AbstractAssociatedDataPanel outgoingPanel = new OutgoingAssociationDataPanel(
                    outgoingIntraModelAssociationCollection, associatedDataActionListener, id, dataRow, record);
            relatedDataPanel.add("br", outgoingPanel);
        }

        /*
         * We also must be able to add Intermodel association. Call the Path
         * finder code to add the intermodel association.
         */
        if (intraModelAssociationCollection != null && !intraModelAssociationCollection.isEmpty()) {
            AbstractAssociatedDataPanel interModelPanel = new InterModelAssociationDataPanel(
                    intraModelAssociationCollection, associatedDataActionListener, id, dataRow, record);
            relatedDataPanel.add("br", interModelPanel);
        }

        relatedDataPanel.add("br", new Cab2bLabel("       "));
        JScrollPane relatedDataPane = new JScrollPane(relatedDataPanel);
        relatedDataPane.getViewport().setBackground(Color.WHITE);
        relatedDataTitledPanel.add(relatedDataPane);

        relatedDataTitledPanel.setBorder(new CustomizableBorder(new Insets(1, 1, 1, 1), true, true));
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