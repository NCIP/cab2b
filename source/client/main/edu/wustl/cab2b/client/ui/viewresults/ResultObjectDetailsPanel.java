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
import java.util.Vector;

import javax.swing.JScrollPane;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.painter.gradient.BasicGradientPainter;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTable;
import edu.wustl.cab2b.client.ui.controls.Cab2bTitledPanel;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.datalist.IDataRow;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.queryengine.result.IRecordWithAssociatedIds;
import edu.wustl.common.querysuite.metadata.associations.IInterModelAssociation;

/**
 * A Panel to show any entity's details.
 * @author chetan_bh
 */
public class ResultObjectDetailsPanel extends ResultPanel {

    /**
     * 
     */
    protected IRecord record;

    protected JXPanel detailsTablePanel;

    private Vector<Vector> tableData = new Vector<Vector>();

    private Vector<String> tableHeader = new Vector<String>();

    private Cab2bTable objDetailsTable;

    private JXTitledPanel relatedDataTitledPanel;

    private JXPanel relatedDataPanel;

    protected IDataRow dataRow;

    private Cab2bPanel m_sidePanel;

    public ResultObjectDetailsPanel(
            SimpleSearchResultBreadCrumbPanel searchPanel,
            IDataRow dataRow,

            IRecord record,
            Collection<AssociationInterface> incomingAssociationCollection,
            List<IInterModelAssociation> intraModelAssociationCollection) {
        /* Set the parent entity interface. Note : This can never be null. */
        super(searchPanel, incomingAssociationCollection, intraModelAssociationCollection);

        this.dataRow = dataRow;

        this.record = record;
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
        m_sidePanel.add(myDataListParentPanel, BorderLayout.CENTER);
    }

    protected void initData() {
        for (int i = 0; i < dataRow.getAttributes().size(); i++) {
            Vector<Object> row = new Vector<Object>();
            AttributeInterface attribute = dataRow.getAttributes().get(i);
            String formattedString = CommonUtils.getFormattedString(attribute.getName());
            row.add(formattedString);
            row.add(dataRow.getRow()[i]);
            tableData.add(row);
        }
        // TODO remove this hard coding, or externalize this strings.
        tableHeader.add("Attribute");
        tableHeader.add("Value");
    }

    protected void initGUI() {
        this.setLayout(new BorderLayout());

        initTableGUI();

        initDataListButtons();

        detailsTablePanel.add("br", addToDataListButton);
        detailsTablePanel.add("tab tab", m_applyAllButton);

        this.add(detailsTablePanel, BorderLayout.CENTER);

        m_sidePanel = new Cab2bPanel(new GridLayout(2, 1, 5, 5));

        initDataListSummaryPanel();

        //fix for matching the sizes of "my data summrary" and "related data"
        Cab2bPanel relatedDataParentPanel = new Cab2bPanel();
        relatedDataParentPanel.setBorder(null);
        relatedDataParentPanel.add("br hfill vfill", relatedDataTitledPanel);

        m_sidePanel.add(relatedDataParentPanel);
        m_sidePanel.add(myDataListParentPanel);
        this.add(m_sidePanel, BorderLayout.EAST);
    }

    protected void initTableGUI() {
        detailsTablePanel = new Cab2bPanel(new RiverLayout());

        objDetailsTable = new Cab2bTable(false, tableData, tableHeader);

        objDetailsTable.setEditable(false);

        JScrollPane tableSP = new JScrollPane(objDetailsTable);

        detailsTablePanel.add("br hfill vfill", tableSP);
    }

    private void initRelatedDataPanel() {
        relatedDataTitledPanel = new Cab2bTitledPanel("Related Data");
        GradientPaint gp1 = new GradientPaint(new Point2D.Double(.05d, 0), new Color(185, 211, 238),
                new Point2D.Double(.95d, 0), Color.WHITE);
        relatedDataTitledPanel.setTitleFont(new Font("SansSerif", Font.BOLD, 11));
        relatedDataTitledPanel.setTitlePainter(new BasicGradientPainter(gp1));
        relatedDataTitledPanel.setTitleForeground(Color.BLACK);
        relatedDataTitledPanel.setBorder(null);

        relatedDataPanel = new Cab2bPanel();
        relatedDataPanel.setBackground(Color.WHITE);
        relatedDataPanel.setLayout(new RiverLayout(5, 10));
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
            Collection outgoingIntraModelAssociationCollection = dataRow.getEntityInterface().getAssociationCollection();
            AbstractAssociatedDataPanel outgoingPanel = new OutgoingAssociationDataPanel(
                    outgoingIntraModelAssociationCollection, searchPanel.getAssociatedDataAL(), dataRow, record);
            relatedDataPanel.add("br", outgoingPanel);
        }

        /*
         * We also must be able to add Intermodel association. Call the Path
         * finder code to add the intermodel association.
         */
        if (intraModelAssociationCollection != null && !intraModelAssociationCollection.isEmpty()) {
            AbstractAssociatedDataPanel interModelPanel = new InterModelAssociationDataPanel(
                    intraModelAssociationCollection, searchPanel.getAssociatedDataAL(), dataRow, record);
            relatedDataPanel.add("br", interModelPanel);
        }

        relatedDataPanel.add("br", new Cab2bLabel("       "));
        JScrollPane relatedDataPane = new JScrollPane(relatedDataPanel);
        relatedDataPane.getViewport().setBackground(Color.WHITE);
        relatedDataTitledPanel.add(relatedDataPane);

        //relatedDataTitledPanel.setBorder(new CustomizableBorder(new Insets(1, 1, 1, 1), true, true));
    }

    /**
     * @see edu.wustl.cab2b.client.ui.viewresults.ResultPanel#getSelectedDataRows()
     */
    List<IDataRow> getSelectedDataRows() {
        List<IDataRow> selectedDataRows = new ArrayList<IDataRow>();
        selectedDataRows.add(dataRow);
        return selectedDataRows;
    }

    /**
     * 
     */
    protected void adjustRows() {
        int rowCount = objDetailsTable.getRowCount();
        if (rowCount < 7) {
            objDetailsTable.setVisibleRowCount(rowCount);
        } else {
            objDetailsTable.setVisibleRowCount(7);
        }
    }
}