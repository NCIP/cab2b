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
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.painter.gradient.BasicGradientPainter;

import edu.common.dynamicextensions.domain.Association;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.client.ui.MainSearchPanel;
import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.SaveDatalistPanel;
import edu.wustl.cab2b.client.ui.SearchNavigationPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bHyperlink;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTitledPanel;
import edu.wustl.cab2b.client.ui.controls.CustomizableBorder;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.datalist.DataRow;
import edu.wustl.cab2b.common.ejb.path.PathFinderBusinessInterface;
import edu.wustl.cab2b.common.ejb.path.PathFinderHomeInterface;
import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.querysuite.metadata.associations.IInterModelAssociation;
import edu.wustl.common.querysuite.metadata.associations.IIntraModelAssociation;
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

    Collection<AssociationInterface> incomingIntraModelAssociationCollection;

    Collection<IInterModelAssociation> interModelAssociationCollection;

    public ResultObjectDetailsPanel(
            DataRow dataRow,
            List<AttributeInterface> attributes,
            ActionListener bcAL,
            ActionListener assoDataAL,
            ViewSearchResultsPanel viewPanel,
            Vector interIntraAssoClass) {
        /* Set the parent entity interface. Note : This can never be null. */
        this.dataRow = dataRow;
        this.parentEntityInterface = dataRow.getEntityInterface();
        this.id = dataRow.getId();
        this.viewPanel = viewPanel;
        this.objectDetails = dataRow.getRow();
        this.attributes = attributes;

        this.interModelAssociationCollection = (Collection<IInterModelAssociation>) interIntraAssoClass.get(0);
        this.incomingIntraModelAssociationCollection = (Collection<AssociationInterface>) interIntraAssoClass.get(1);

        breadCrumbActionListener = bcAL;
        associatedDataActionListener = assoDataAL;
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

        /* Get all the incoming intramodel associations. */
        PathFinderBusinessInterface busInt = (PathFinderBusinessInterface) CommonUtils.getBusinessInterface(
                                                                                                            "edu.wustl.cab2b.server.ejb.path.PathFinderBean",
                                                                                                            PathFinderHomeInterface.class,
                                                                                                            null);

        if (incomingIntraModelAssociationCollection != null) {
            Iterator assoIter = incomingIntraModelAssociationCollection.iterator();
            while (assoIter.hasNext()) {
                /*
                 * Get the Association and from that get the actual associated
                 * class in the form of EntityInterface.
                 */
                Association deAssociation = (Association) assoIter.next();
                /* Get the target entity. */
                EntityInterface associatedEntityInterface = deAssociation.getEntity();

                /*
                 * Map the DE association to an instance of Intramodel
                 * association.
                 */
                IIntraModelAssociation intraModelAssociation = (IIntraModelAssociation) QueryObjectFactory.createIntraModelAssociation(deAssociation);
                relatedDataPanel.add("br", addHyperlink(associatedEntityInterface, intraModelAssociation));
            }
        }

        /*
         * We also must be able to add Intermodel association. Call the Path
         * finder code to add the intermodel association.
         */

        JLabel label = new JLabel(" Inter Model Associations : ");
        label.setForeground(Color.red);
        relatedDataPanel.add("br", label);

        if (interModelAssociationCollection != null) {
            Iterator interAssocIttr = interModelAssociationCollection.iterator();
            while (interAssocIttr.hasNext()) {
                IInterModelAssociation interModelAssociation = (IInterModelAssociation) interAssocIttr.next();
                if (!interModelAssociation.isBidirectional()) {
                    continue;
                }
                EntityInterface associatedEntityInterface = interModelAssociation.getTargetEntity();
                relatedDataPanel.add("br", addHyperlink(associatedEntityInterface, interModelAssociation));
            }
        }

        relatedDataPanel.add("br", new Cab2bLabel("       "));
        JScrollPane relatedDataPane = new JScrollPane(relatedDataPanel);
        relatedDataPane.getViewport().setBackground(Color.WHITE);
        relatedDataTitledPanel.add(relatedDataPane);

        relatedDataTitledPanel.setBorder(new CustomizableBorder(new Insets(1, 1, 1, 1), true, true));
    }

    /*
     * The method initializes the user object for the hyperlink, so that both
     * target and source entities for the association, as well as the
     * association itself are remembered by the link
     */
    private Cab2bHyperlink addHyperlink(EntityInterface targetEntity, IAssociation association) {
        /* Trim off the assoc class name */
        Cab2bHyperlink hyperlink = new Cab2bHyperlink();

        /* Set the hyperlink text */
        hyperlink.setText(edu.wustl.cab2b.common.util.Utility.getDisplayName(targetEntity));

        if (association instanceof IIntraModelAssociation) {
            /* Get the target role name for the intramodel association. */
            IIntraModelAssociation intraModelAssociation = (IIntraModelAssociation) (association);
            String strRoleName = intraModelAssociation.getDynamicExtensionsAssociation().getSourceRole().getName();
            /* Set a tool tip for showing role names */
            hyperlink.setToolTipText("Target role name : " + strRoleName);
        } else if (association instanceof IInterModelAssociation) {
            /* Get the target role name for the intramodel association. */
            IInterModelAssociation interModelAssociation = (IInterModelAssociation) (association);

            String strRoleName = interModelAssociation.getSourceAttribute().getName();
            /* Set a tool tip for showing role names */
            hyperlink.setToolTipText("Target attribute name : " + strRoleName);
            // TODO : Where do i get the target role name for inter model
            // associations.
        }

        hyperlink.addActionListener(associatedDataActionListener);
        /* TODO : Please replace this with an appropriate data structure. */
        Vector hyperLinkUserObject = new Vector(4);
        /* Add the source and target entities. */

        /* 1st ELEMENT. */
        hyperLinkUserObject.add(parentEntityInterface);
        /* 2nd ELEMENT. */
        hyperLinkUserObject.add(targetEntity);

        /* Get the attributes for parent entity */
        Collection attributeInterfacesCollection = parentEntityInterface.getAttributeCollection();
        Iterator attriIntCollecIter = attributeInterfacesCollection.iterator();

        /*
         * 3rd ELEMENT : Get the conditions for the target class of the
         * association.
         */
        hyperLinkUserObject.add(getConditionForTarget(attriIntCollecIter));
        /* 4th ELEMENT is the data row itself */
        hyperLinkUserObject.add(dataRow);
        /* 5th ELEMENT: Finally add the intra model association. */
        hyperLinkUserObject.add(association);
        hyperlink.setUserObject(hyperLinkUserObject);
        return hyperlink;
    }

    /*
     * Given a collection of attributes for a class/emtity, the method
     * identifies the unique identifier, and sets the value.
     */
    private Vector getConditionForTarget(Iterator attriIntCollecIter) {
        Vector targetIdentifierData = new Vector();

        while (attriIntCollecIter.hasNext()) {
            AttributeInterface attrib = (AttributeInterface) attriIntCollecIter.next();
            if (attrib.getName().equalsIgnoreCase("id") || attrib.getName().equalsIgnoreCase("identifier")) {
                targetIdentifierData.add(attrib); // attribute
                targetIdentifierData.add("Equals"); // predicate

                /*
                 * Mahesh : Changed this to contain the actual identifier for
                 * this parent entity instance.
                 */
                targetIdentifierData.add(id); // value
                break;
            }
        }
        return targetIdentifierData;
    }

}