package edu.wustl.cab2b.client.ui.viewresults;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.painter.gradient.BasicGradientPainter;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.client.ui.MainSearchPanel;
import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.SaveDatalistPanel;
import edu.wustl.cab2b.client.ui.SearchNavigationPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTitledPanel;
import edu.wustl.cab2b.client.ui.controls.CustomizableBorder;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.datalist.DataRow;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.queryengine.result.IRecordWithAssociatedIds;
import edu.wustl.common.querysuite.metadata.associations.IInterModelAssociation;
import edu.wustl.common.util.logger.Logger;

/**
 * A Panel to show any entity's details.
 * @author chetan_bh
 */
public class ResultObjectDetailsPanel extends Cab2bPanel {
    Object[] objectDetails;

    List<AttributeInterface> attributes; // List<IAttribute> attributes;

    JXTable objDetailsTable;

    Vector<Vector<Object>> tableData;

    Vector<String> tableHeader;

    JXTitledPanel relatedDataTitledPanel;

    JXPanel relatedDataPanel;

    JButton addToDataListButton;

    ActionListener associatedDataActionListener;

    ActionListener breadCrumbActionListener;

    JXPanel breadCrumbPanel;

    EntityInterface parentEntityInterface = null;

    ViewSearchResultsPanel viewPanel;

    Object id = null;

    DataRow dataRow;

    private Cab2bButton m_applyAllButton;

    private Cab2bPanel m_sidePanel;
    
    /**
     * 
     */
    private IRecord record;
    
    SimpleSearchResultBreadCrumbPanel searchPanel;



    public ResultObjectDetailsPanel(SimpleSearchResultBreadCrumbPanel searchPanel,
            DataRow dataRow,
                      
            IRecord record) {
        /* Set the parent entity interface. Note : This can never be null. */
        this.dataRow = dataRow;
        this.parentEntityInterface = dataRow.getEntityInterface();
        this.id = dataRow.getId();
        this.objectDetails = dataRow.getRow();
        this.attributes = searchPanel.getAttributes();

        //this.interModelAssociationCollection = (Collection<IInterModelAssociation>) interIntraAssoClass.get(0);
        //this.incomingIntraModelAssociationCollection = (Collection<AssociationInterface>) interIntraAssoClass.get(1);
        this.record = record;
        this.searchPanel = searchPanel;

        breadCrumbActionListener = searchPanel.getBreadCrumbsAL();
        associatedDataActionListener = searchPanel.getAssociatedDataAL();
        this.viewPanel = searchPanel.viewPanel;

        initData();
        initRelatedDataPanel();
        initGUI();
    }

    public void addDataSummaryPanel() {
        m_sidePanel.add(ViewSearchResultsSimplePanel.myDataListTitledPanel, BorderLayout.CENTER);
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

        objDetailsTable = new JXTable(tableData, tableHeader);
        objDetailsTable.setEditable(false);

        JScrollPane tableSP = new JScrollPane(objDetailsTable);
        // tableSP.setPreferredSize(new Dimension(700,600));

        detailsTablePanel.add("br hfill vfill", tableSP);

        addToDataListButton = new Cab2bButton("Add To Data List");
        addToDataListButton.setPreferredSize(new Dimension(140, 22));
        addToDataListButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Logger.out.info("add to data list action");
                Vector<Object> selectedUserObjects = new Vector<Object>();
                selectedUserObjects.add(dataRow);
                MainSearchPanel.getDataList().addDataRows(selectedUserObjects);

                ViewSearchResultsSimplePanel.updateMyDataListPanel();
                SaveDatalistPanel.isDataListSaved = false;
                SearchNavigationPanel.messageLabel.setText(" *Added " + selectedUserObjects.size()
                        + " elements to data list");
                updateUI();
            }
        });

        // Add Apply All button to apply currently added datalist options
        // to the currently selected objects.
        m_applyAllButton = new Cab2bButton("Apply Data List");
        m_applyAllButton.setPreferredSize(new Dimension(130, 22));
        m_applyAllButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                // Perform apply all action
                Vector<Object> selectedUserObjects = new Vector<Object>();
                selectedUserObjects.add(dataRow);
                ViewSearchResultsSimplePanel.performApplyAllAction(selectedUserObjects,
                                                                   (JComponent) detailsTablePanel);
            }
        });
        detailsTablePanel.add("br", addToDataListButton);
        detailsTablePanel.add("tab tab", m_applyAllButton);
        // this.add("br", addToDataListButton);

        this.add(detailsTablePanel, BorderLayout.CENTER);

        m_sidePanel = new Cab2bPanel(new GridLayout(2, 1, 5, 5));

        ViewSearchResultsSimplePanel.initDataListSummaryPanel();

        m_sidePanel.add(relatedDataTitledPanel);
        m_sidePanel.add(ViewSearchResultsSimplePanel.myDataListTitledPanel);
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
        Collection incomingIntraModelAssociationCollection = searchPanel.getIncomingAssociationCollection();
        
        if ( incomingIntraModelAssociationCollection != null && !incomingIntraModelAssociationCollection.isEmpty()) {
            AbstractAssociatedDataPanel incomingPanel = new IncomingAssociationDataPanel(
                    incomingIntraModelAssociationCollection, associatedDataActionListener, id, dataRow,record);
            relatedDataPanel.add(incomingPanel);
        }

        /* Add outgoing association if supported
         * 
         */
        if (record instanceof IRecordWithAssociatedIds) {
            Collection outgoingIntraModelAssociationCollection = dataRow.getEntityInterface().getAssociationCollection();
            AbstractAssociatedDataPanel outgoingPanel = new OutgoingAssociationDataPanel(
                    outgoingIntraModelAssociationCollection, associatedDataActionListener, id, dataRow,record);
            relatedDataPanel.add("br",outgoingPanel);
        }

        /*
         * We also must be able to add Intermodel association. Call the Path
         * finder code to add the intermodel association.
         */
        Collection interModelAssociationCollection = searchPanel.getIntraModelAssociationCollection();
        if (interModelAssociationCollection != null && !interModelAssociationCollection.isEmpty()) {
            AbstractAssociatedDataPanel interModelPanel = new InterModelAssociationDataPanel(
                    interModelAssociationCollection, associatedDataActionListener, id, dataRow,record);
            relatedDataPanel.add("br",interModelPanel);
        }

        relatedDataPanel.add("br", new Cab2bLabel("       "));
        JScrollPane relatedDataPane = new JScrollPane(relatedDataPanel);
        relatedDataPane.getViewport().setBackground(Color.WHITE);
        relatedDataTitledPanel.add(relatedDataPane);

        relatedDataTitledPanel.setBorder(new CustomizableBorder(new Insets(1, 1, 1, 1), true, true));
    }
}