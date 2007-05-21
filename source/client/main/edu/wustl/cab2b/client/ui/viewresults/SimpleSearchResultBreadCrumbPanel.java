package edu.wustl.cab2b.client.ui.viewresults;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JComponent;

import org.jdesktop.swingx.JXPanel;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.controls.Cab2bHyperlink;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.pagination.PageElement;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.client.ui.util.CustomSwingWorker;
import edu.wustl.cab2b.common.datalist.DataRow;
import edu.wustl.cab2b.common.ejb.path.PathFinderBusinessInterface;
import edu.wustl.cab2b.common.ejb.path.PathFinderHomeInterface;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.querysuite.metadata.associations.IInterModelAssociation;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.util.logger.Logger;

/**
 * This panel is a container panel for all different panel in simple view of search results.
 * This panel has a bread crumb panel for easy navigation. 
 * 
 * @author chetan_bh
 */
public class SimpleSearchResultBreadCrumbPanel extends Cab2bPanel {

    private static final long serialVersionUID = 1L;

    HashMap<String, List<AttributeInterface>> mapResultLabel = new HashMap<String, List<AttributeInterface>>();

    public int panelCount = 0;

    /** A vector of display strings for bread crumbs hyperlink. */
    Vector<String> m_vBreadCrumbs = new Vector<String>();

    int m_iBreadCrumbCount = 0;

    /**
     * The breadcrumPanel common to all cards.
     */
    JXPanel m_breadCrumbPanel = null;

    /**
     * The panel will house cards, where each card is a panel depicting some details on the results.
     */
    JXPanel m_resultsPanel = null;

    /** Query result object. */
    IQueryResult queryResult;

    String currentBreadCrumb = "Patients";

    /**
     * Action listener for bread crumbs hyperlink.
     */
    ActionListener breadCrumbsAL;

    /**
     * Pagination components hyperlinks action listener.
     */
    ActionListener hyperlinkAL;

    /**
     * Associated Data hyperlinks action listener;
     */
    ActionListener associatedDataAL;

    ViewSearchResultsPanel viewPanel;

    List<AttributeInterface> attributeList = null;

    private Collection<AssociationInterface> incomingAssociationCollection;

    private List<IInterModelAssociation> intraModelAssociationCollection;

    public SimpleSearchResultBreadCrumbPanel(IQueryResult queryResult, ViewSearchResultsPanel viewPanel) {
        this.viewPanel = viewPanel;
        this.queryResult = queryResult;
        initData();
        initGUI(viewPanel);
    }

    /** Initialize data. */
    private void initData() {

    }

    public void addPanel(JXPanel panel, String panelName) {
        this.m_resultsPanel.add(panel, panelName);
    }

    /** Shows a panel represented by a name, by bringing that panel to the top. */
    public void showPanel(String panelName) {
        Logger.out.info("panel name : " + panelName);
        CardLayout layout = (CardLayout) this.m_resultsPanel.getLayout();
        layout.show(this.m_resultsPanel, panelName);
        int totalCardPanels = m_resultsPanel.getComponentCount();
        /**
         * have to add My Data list summary panel
         */
        for (int i = 0; i < totalCardPanels; i++) {
            Component comp = m_resultsPanel.getComponent(i);
            if (true == comp.isVisible()) {
                if (comp instanceof ViewSearchResultsSimplePanel) {
                    ViewSearchResultsSimplePanel showingPanel = (ViewSearchResultsSimplePanel) m_resultsPanel.getComponent(i);
                    showingPanel.addDataSummaryPanel();
                    break;
                } else if (comp instanceof ResultObjectDetailsPanel) {
                    ResultObjectDetailsPanel showingPanel = (ResultObjectDetailsPanel) m_resultsPanel.getComponent(i);
                    showingPanel.addDataSummaryPanel();
                    break;
                }
            }
        }
    }

    /** Addes a new panel to the top of the stack of existing breadcrumbs panels. */
    public void addBreadCrumbPanel(JXPanel panel, String panelName) {
        this.m_breadCrumbPanel.add(panel, panelName);
    }

    /** @see showPanel(String panelName) */
    public void showBreadcrumbPanel(String panelName) {
        CardLayout layout = (CardLayout) this.m_breadCrumbPanel.getLayout();
        layout.show(this.m_breadCrumbPanel, panelName);
    }

    public List<AttributeInterface> getAttributes() {
        return this.attributeList;
    }

    public IQueryResult getQueryResult() {
        return queryResult;
    }

    private JXPanel updateGuiIfResultOneOrZero(IAssociation association, final DataRow parentDataRow) {
        //Check if you got only one record
        JXPanel simpleSearchResultPanel = null;
        Map<String, List<IRecord>> allRecords = queryResult.getRecords();
        int recordNo = edu.wustl.cab2b.client.ui.query.Utility.getRecordNum(queryResult);
        String className = edu.wustl.cab2b.common.util.Utility.getDisplayName(queryResult.getOutputEntity());

        if (recordNo == 0) {
            simpleSearchResultPanel = getNoResultFoundPopup(className);
        } else {
            setInterIntraAssociatedObjectsCollection(queryResult.getOutputEntity());

            if (recordNo == 1) {
                List<IRecord> recordList = null;
                String urlKey = null;

                for (String key : allRecords.keySet()) {
                    recordList = allRecords.get(key);
                    if (recordList.size() == 1) {
                        urlKey = key;
                        break;
                    }
                }
                /* Initialize the count for number of attributes to be shown in the */
                IRecord record = recordList.get(0);

                Object[] valueArray = new Object[this.attributeList.size()];
                for (int i = 0; i < this.attributeList.size(); i++) {
                    valueArray[i] = record.getValueForAttribute(this.attributeList.get(i));
                }
                DataRow dataRow = new DataRow();
                AttributeInterface attrib = this.attributeList.get(0);
                /*
                 * Get the EntityInterface from the map only if the last parameter is null. 
                 * This should ideally happen only the first time
                 */
                EntityInterface presentEntityInterface = null;
                presentEntityInterface = attrib.getEntity();
                String strclassName = edu.wustl.cab2b.common.util.Utility.getDisplayName(presentEntityInterface);

                AttributeInterface idAttribute = Utility.getIdAttribute(presentEntityInterface);
                Object id = record.getValueForAttribute(idAttribute);
                dataRow.setRow(valueArray);
                dataRow.setAttributes(this.attributeList);
                Logger.out.info("Class Name if result one or zero:" + strclassName);
                dataRow.setClassName(strclassName);
                dataRow.setParent(parentDataRow);
                dataRow.setId(id);
                dataRow.setAssociation(association);
                dataRow.setEntityInterface(presentEntityInterface);
                dataRow.setURL(urlKey);
                simpleSearchResultPanel = new ResultObjectDetailsPanel(this, dataRow, record);
            }
        }

        return simpleSearchResultPanel;
    }

    private JXPanel getNoResultFoundPopup(String className) {
        JXPanel noResultFoundPopup = new Cab2bPanel();
        Cab2bLabel noResultFoundErroLabel = new Cab2bLabel("No results found for Class " + className);
        noResultFoundPopup.add(noResultFoundErroLabel);
        return noResultFoundPopup;
    }

    /** Initialize GUI. */
    private void initGUI(ViewSearchResultsPanel viewPanel) {
        this.setLayout(new RiverLayout());

        breadCrumbsAL = new BreadCrumbActionListener(this);
        hyperlinkAL = new HyperlinlActionListener(this);
        associatedDataAL = new AssociatedDataActionListener(this);

        this.attributeList = Utility.getAttributeList(queryResult);

        //Check if you got only one record
        JXPanel simpleSearchResultPanel;
        simpleSearchResultPanel = updateGuiIfResultOneOrZero(null, null);

        if (simpleSearchResultPanel == null) {
            simpleSearchResultPanel = new ViewSearchResultsSimplePanel(this,null, null, viewPanel, null);
        }

        /*
         * The breadcrumb panel should be common to all cards, and hence should not be part of any card.
         */

        String key = this.panelCount + "#"
                + edu.wustl.cab2b.common.util.Utility.getDisplayName(queryResult.getOutputEntity());
        this.m_vBreadCrumbs.add(key);
        Logger.out.info("key   :" + key);
        /*
         * Put the list of attributes into the map and associate a key with it.
         * The key is the userObject that sits behind the hyperlinks in the
         * bread-crumbs.
         */
        /**/

        this.mapResultLabel.put(key, this.attributeList);
        /*Set also directly the attribute list*/
        // this.attributeList = classRecords.getAttributes();
        this.m_breadCrumbPanel = new Cab2bPanel();
        this.m_breadCrumbPanel.setLayout(new CardLayout());
        BreadcrumbPanel breadCrumbPanel = new BreadcrumbPanel(breadCrumbsAL, m_vBreadCrumbs);
        this.m_breadCrumbPanel.add(breadCrumbPanel, "" + this.panelCount);
        //this.m_breadCrumbPanel.setPreferredSize(new Dimension(1150, 20));

        /*
         * Initialize the panel that will house all cards, and add the first card.
         */
        this.m_resultsPanel = new Cab2bPanel();
        this.m_resultsPanel.setLayout(new CardLayout());
        this.m_resultsPanel.add(simpleSearchResultPanel, "" + this.panelCount);

        /* Add the main result panel to the current panel.*/
        this.add("hfill", this.m_breadCrumbPanel);
        this.add("br vfill hfill", this.m_resultsPanel);
    }

    /** Gets bread-crumbs hyperlink action listener. */
    public ActionListener getBreadCrumbsAL() {
        return breadCrumbsAL;
    }

    /** Sets bread-crumbs hyperlink action listener. */
    public void setBreadCrumbsAL(ActionListener breadCrumbsAL) {
        this.breadCrumbsAL = breadCrumbsAL;
    }

    /** Gets pagination elements hyperlink action listener. */
    public ActionListener getHyperlinkAL() {
        return hyperlinkAL;
    }

    /** Sets pagination elements hyperlink action listener. */
    public void setHyperlinkAL(ActionListener hyperlinkAL) {
        this.hyperlinkAL = hyperlinkAL;
    }

    /** Gets associated data hyperlink action listener. */
    public ActionListener getAssociatedDataAL() {
        return associatedDataAL;
    }

    /** Sets associated data hyperlinks action listner. */
    public void setAssociatedDataAL(ActionListener associatedDataAL) {
        this.associatedDataAL = associatedDataAL;
    }

    /**
     * @return Returns the incomingAssociationCollection.
     */
    public Collection<AssociationInterface> getIncomingAssociationCollection() {
        return incomingAssociationCollection;
    }

    /**
     * @return Returns the intraModelAssociationCollection.
     */
    public List<IInterModelAssociation> getIntraModelAssociationCollection() {
        return intraModelAssociationCollection;
    }

    private void setInterIntraAssociatedObjectsCollection(EntityInterface entityInterface) {
        /* Get all the incoming intramodel associations. */
        PathFinderBusinessInterface busInt = (PathFinderBusinessInterface) CommonUtils.getBusinessInterface(
                                                                                                            "edu.wustl.cab2b.server.ejb.path.PathFinderBean",
                                                                                                            PathFinderHomeInterface.class,
                                                                                                            null);

        try {

            incomingAssociationCollection = busInt.getIncomingIntramodelAssociations(entityInterface.getId());

            intraModelAssociationCollection = busInt.getInterModelAssociations(entityInterface.getId());

        } catch (RemoteException re) {
            CommonUtils.handleException(re, viewPanel, true, true, false, false);
        }

    }

    /**
     * This action listener class should take care of creating new/existinf panels in the 
     * Card layout.
     * 
     * @author chetan_bh
     */
    private class BreadCrumbActionListener implements ActionListener {
        SimpleSearchResultBreadCrumbPanel breadCrumbPanel;

        public BreadCrumbActionListener(SimpleSearchResultBreadCrumbPanel panel) {
            this.breadCrumbPanel = panel;
        }

        public void actionPerformed(ActionEvent evt) {
            Logger.out.info("bread crumbs ActionListener : " + evt.getActionCommand());
            Cab2bHyperlink hyperlink = (Cab2bHyperlink) evt.getSource();
            /* Get the user object for the hyperlink. This contains the whole string.*/
            String hyperlinkText = (String) hyperlink.getUserObject();

            /*Set the attribute list if user clicked on a class.*/
            List<AttributeInterface> attrList = breadCrumbPanel.mapResultLabel.get(hyperlinkText);
            if (attrList != null) {
                breadCrumbPanel.attributeList = attrList;
            }
            int i;
            for (i = 0; i < m_vBreadCrumbs.size(); i++) {
                String strVectorValue = (String) m_vBreadCrumbs.get(i);
                boolean blnEval = (strVectorValue.trim().equals(hyperlinkText.trim()));
                if (blnEval) {
                    break;
                }
            }
            i++;
            Logger.out.info("i :> " + i);

            Logger.out.info("size :> " + m_vBreadCrumbs.size());
            for (int j = i; j < m_vBreadCrumbs.size(); j++) {
                m_vBreadCrumbs.remove(j);
                j = j - 1;
            }

            /*Get the number from the hyperlink text and use that to show the panel.*/
            String strIndex = hyperlinkText.substring(0, hyperlinkText.indexOf("#"));
            Logger.out.info("INDEX FOR PANEL : " + strIndex);

            breadCrumbPanel.showPanel(strIndex);
            BreadcrumbPanel breadcrumbPanel = new BreadcrumbPanel(breadCrumbPanel.getBreadCrumbsAL(),
                    m_vBreadCrumbs);
            this.breadCrumbPanel.addBreadCrumbPanel(breadcrumbPanel, strIndex);
            this.breadCrumbPanel.showBreadcrumbPanel(strIndex);
        }
    }

    /**
     * This action listener class should take care of creating new/existing panels in the 
     * Card layout.
     * 
     * @author chetan_bh
     */
    private class HyperlinlActionListener implements ActionListener {
        SimpleSearchResultBreadCrumbPanel breadCrumbPanel;

        //private boolean blnAddingFirstTime = true;

        public HyperlinlActionListener(SimpleSearchResultBreadCrumbPanel panel) {
            this.breadCrumbPanel = panel;
        }

        public void actionPerformed(ActionEvent evt) {
            Cab2bHyperlink hyperlink = (Cab2bHyperlink) evt.getSource();
            Object userObj = hyperlink.getUserObject();
            Logger.out.info("userObj " + userObj.getClass());

            PageElement element = (PageElement) userObj;

            /*Get the data row corresponding to the clicked element.*/
            final Vector recordListUserObject = (Vector) element.getUserObject();
            final DataRow dataRow = (DataRow) recordListUserObject.get(0);
            final IRecord record = (IRecord) recordListUserObject.get(1);

            /*Get the attributes for the last query fired.*/
            final List<AttributeInterface> attrList = breadCrumbPanel.getAttributes();
            Logger.out.info("attributes " + attrList);
            String hyperlinkText = hyperlink.getText();

            /*
             * Refresh the breadcrumb vector, and pass that instance onto a new
             * instance of the breadcrumb panel.
             */
            //SimpleSearchResultBreadCrumbPanel.m_vBreadCrumbs.add(hyperlinkText);
            (breadCrumbPanel.panelCount)++;
            final int currentCount = breadCrumbPanel.panelCount;

            Logger.out.info("CURRENT COUNT : " + currentCount);
            m_vBreadCrumbs.add(currentCount + "#" + hyperlinkText);
            Logger.out.info("VECT SIZE : " + m_vBreadCrumbs.size());
            BreadcrumbPanel breadcrumbPanel1 = new BreadcrumbPanel(breadCrumbPanel.getBreadCrumbsAL(),
                    m_vBreadCrumbs);

            //breadcrumbPanel1.setPreferredSize(new Dimension(900,20));           
            this.breadCrumbPanel.addBreadCrumbPanel(breadcrumbPanel1, "" + currentCount);
            this.breadCrumbPanel.showBreadcrumbPanel("" + currentCount);

            // ----------- Swing worker thread ------------
            CustomSwingWorker sw = new CustomSwingWorker(viewPanel) {

                // Vector interIntraAcssoClassColl;

                @Override
                protected void doNonUILogic() throws RuntimeException {
                    //interIntraAcssoClassColl = getInterIntraAssociatedObjectsCollection(dataRow);
                }

                @Override
                protected void doUIUpdateLogic() throws RuntimeException {
                    // TODO can also pass queryResult object instead of first two parameters.
                    ResultObjectDetailsPanel detailsPanel = new ResultObjectDetailsPanel(breadCrumbPanel, dataRow,
                            record);
                    breadCrumbPanel.addPanel(detailsPanel, "" + currentCount);
                    breadCrumbPanel.showPanel("" + currentCount);
                }
            };
            sw.start();

        }
    }

    /**
     * This action listener is to handle actions on associated data hyperlinks.
     * 
     * @author chetan_bh
     */
    private class AssociatedDataActionListener implements ActionListener {
        SimpleSearchResultBreadCrumbPanel breadCrumbPanel;

        String breadCrumbText;

        public AssociatedDataActionListener(SimpleSearchResultBreadCrumbPanel panel) {
            this.breadCrumbPanel = panel;
        }

        // TODO This action listener should take care of Creating QueryObject
        // and firing the query using QueryExecutor.
        public void actionPerformed(ActionEvent ae) {
            /* Get the source for the event.*/
            Cab2bHyperlink hyperlink = (Cab2bHyperlink) ae.getSource();
            AbstractAssociatedDataPanel linkContainer = (AbstractAssociatedDataPanel) hyperlink.getParent();
            /* Get the user object associated with that source.*/
            final HyperLinkUserObject hyperLinkUserObject = (HyperLinkUserObject) hyperlink.getUserObject();

            final IQuery query = linkContainer.getQuery((hyperLinkUserObject.getAssociation()));
            breadCrumbText = Utility.getDisplayName(hyperLinkUserObject.getTargetEntity());
            breadCrumbPanel.panelCount++;
            final int currentCount = breadCrumbPanel.panelCount;

            /* Get result by executing the Query in a worker thread. */
            CustomSwingWorker swingWorker = new CustomSwingWorker((JXPanel) breadCrumbPanel, queryResult) {

                @Override
                protected void doNonUILogic() throws RuntimeException {
                    queryResult = CommonUtils.executeQuery((ICab2bQuery) query, (JComponent) breadCrumbPanel);
                }

                @Override
                protected void doUIUpdateLogic() throws RuntimeException {
                    List<AttributeInterface> attributeList = Utility.getAttributeList(queryResult);

                    /*Get attributes and set in map, for later when the user is navigating, and for now directly set it.*/
                    breadCrumbPanel.mapResultLabel.put(currentCount + "#" + breadCrumbText, attributeList);
                    breadCrumbPanel.attributeList = attributeList;

                    /*
                     * Add a new instance of ViewSearchResultsSimplePanel.
                     */
                    //Check if you got only one record
                    IAssociation association = hyperLinkUserObject.getAssociation();
                    DataRow dataRow = hyperLinkUserObject.getParentDataRow();
                    EntityInterface targetEntity = hyperLinkUserObject.getTargetEntity();

                    JXPanel simpleSearchResultPanelNew = updateGuiIfResultOneOrZero(association, dataRow);
                    if (simpleSearchResultPanelNew == null) {
                        simpleSearchResultPanelNew = new ViewSearchResultsSimplePanel(breadCrumbPanel,association,
                                 dataRow, viewPanel, targetEntity);
                    }

                    addToPanel(simpleSearchResultPanelNew);
                }
            };
            if (query != null) {
                swingWorker.start();
            } else {
                addToPanel(getNoResultFoundPopup(breadCrumbText));
            }
        }

        private void addToPanel(JXPanel simpleSearchResultPanelNew) {
            int currentCount = breadCrumbPanel.panelCount;

            breadCrumbPanel.addPanel(simpleSearchResultPanelNew, "" + currentCount);
            breadCrumbPanel.showPanel("" + currentCount);

            /*
             * Also we need to refresh the breadcrumb panel,so do the same with the breadcrumb panel.
             */
            m_vBreadCrumbs.add(currentCount + "#" + breadCrumbText);
            BreadcrumbPanel breadcrumbPanel = new BreadcrumbPanel(breadCrumbPanel.getBreadCrumbsAL(),
                    m_vBreadCrumbs);

            breadCrumbPanel.addBreadCrumbPanel(breadcrumbPanel, "" + currentCount);
            breadCrumbPanel.showBreadcrumbPanel("" + currentCount);
        }
    }

}
