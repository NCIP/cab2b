package edu.wustl.cab2b.client.ui.viewresults;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.painter.gradient.BasicGradientPainter;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.client.ui.controls.Cab2bComboBox;
import edu.wustl.cab2b.client.ui.controls.Cab2bHyperlink;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bStandardFonts;
import edu.wustl.cab2b.client.ui.controls.Cab2bTitledPanel;
import edu.wustl.cab2b.client.ui.controls.RiverLayout;
import edu.wustl.cab2b.client.ui.pagination.JPagination;
import edu.wustl.cab2b.client.ui.pagination.NumericPager;
import edu.wustl.cab2b.client.ui.pagination.PageElement;
import edu.wustl.cab2b.client.ui.pagination.PageElementImpl;
import edu.wustl.cab2b.client.ui.pagination.Pager;
import edu.wustl.cab2b.common.datalist.DataRow;
import edu.wustl.cab2b.common.datalist.IDataRow;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.querysuite.metadata.associations.IInterModelAssociation;

/**
 * A Panel to show the results of the query operation. Provides 
 * functionality to make selection of the displayed results and
 * add it to the data list.
 *  
 * @author chetan_bh
 */
public class ViewSearchResultsSimplePanel extends ResultPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private JXPanel searchResultsPanel;

    /**
     * Query result in a format required by JPagination component.
     */
    private Vector<PageElement> allElements = null;

    private IDataRow parentDataRow;

    private IAssociation queryAssociation = null;

    private JXPanel m_addSummaryParentPanel;

    private JPagination pagination;

    private Vector<String> serviceURLComboContents = new Vector<String>();

    private Map<String, Vector<PageElement>> URLSToResultRowMap = new HashMap<String, Vector<PageElement>>();

    private JXTitledPanel titledSearchResultsPanel;

    public ViewSearchResultsSimplePanel(
            SimpleSearchResultBreadCrumbPanel searchPanel,
            IQueryResult queryResult,
            IAssociation association,
            IDataRow parentDataRow,
            /*EntityInterface presentEntityInterface,*/Collection<AssociationInterface> incomingAssociationCollection,
            List<IInterModelAssociation> intraModelAssociationCollection) {

        super(searchPanel, incomingAssociationCollection, intraModelAssociationCollection);

        queryAssociation = association;
        this.queryResult = queryResult;
        // Parent data row will be null for the first query's results, but will be non-null for associated class query's results. 
        this.parentDataRow = parentDataRow;
    }

    /**
     * @see edu.wustl.cab2b.client.ui.viewresults.ResultPanel#doInitialization()
     */
    public void doInitialization() {
        initData();
        initGUI();
    }

    /**
     * Method to add My data list summary panel to    
     *
     */
    public void addDataSummaryPanel() {
        if (myDataListPanel != null && myDataListPanel.getComponentCount() > 1) {
            m_applyAllButton.setEnabled(true);
        }
        m_addSummaryParentPanel.add(myDataListParentPanel, BorderLayout.EAST);
    }

    /**
     * Initializes the data needed for <code>JPagination</code> component.
     */
    private void initData() {
        allElements = new Vector<PageElement>();

        String className = Utility.getDisplayName(queryResult.getOutputEntity());
        List<AttributeInterface> attributes = Utility.getAttributeList(queryResult);
        int attributeSize = attributes.size();
        //int attributeLimitInDescStr = (attributeSize < 10) ? attributeSize : 10;

        Map<String, List<IRecord>> allRecords = queryResult.getRecords();
        serviceURLComboContents.add(" All service URLs ");
        for (String url : allRecords.keySet()) {

            List<IRecord> recordList = allRecords.get(url);
            
            StringBuilder urlNameSize = new StringBuilder( url );
            urlNameSize = new StringBuilder( urlNameSize.substring(urlNameSize.indexOf("//")+2));
            urlNameSize = new StringBuilder(urlNameSize.substring(0,urlNameSize.indexOf("/")));
            urlNameSize.append(" ( " + recordList.size() + " )");
            serviceURLComboContents.add(urlNameSize.toString());
            Vector<PageElement> elements = new Vector<PageElement>();
            for (IRecord record : recordList) {
                StringBuffer descBuffer = new StringBuffer();
                for (int i = 0; i < attributeSize; i++) {
                    Object value = record.getValueForAttribute(attributes.get(i));
                    if (value != null) {
                        if (i != 0) {
                            descBuffer.append(',');
                        }
                        descBuffer.append(value);
                    }
                }
                String description = descBuffer.toString();
                //                if (description.length() > 150) {
                //                    //150 is allowable chars at 1024 resolution
                //                    description = description.substring(0, 150);
                //                    //To avoid clipping of attribute value in-between
                //                    int index = description.lastIndexOf(",");
                //                   description = description.substring(0, index);
                //                }
                PageElement element = new PageElementImpl();
                element.setDisplayName(className + "_" + record.getRecordId().getId());
                element.setDescription(description);

                DataRow dataRow = new DataRow(record, queryResult.getOutputEntity());
                dataRow.setParent(parentDataRow);
                dataRow.setAssociation(queryAssociation);

                Vector recordListUserObject = new Vector();
                recordListUserObject.add(dataRow);
                recordListUserObject.add(record);

                element.setUserObject(recordListUserObject);

                allElements.add(element);
                elements.add(element);
            }
            URLSToResultRowMap.put(urlNameSize.toString(), elements);
        }
    }

    /**
     * Initializes the GUI for showing query results.
     */
    private void initGUI() {
        this.setLayout(new RiverLayout());

        /**
         * Add the following selectively
         */
        titledSearchResultsPanel = new Cab2bTitledPanel("Search Results :- " + "Total results ( "
                + allElements.size() + " )");
        GradientPaint gp = new GradientPaint(new Point2D.Double(.05d, 0), new Color(185, 211, 238),
                new Point2D.Double(.95d, 0), Color.WHITE);
        titledSearchResultsPanel.setTitlePainter(new BasicGradientPainter(gp));
        titledSearchResultsPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        titledSearchResultsPanel.setTitleFont(new Font("SansSerif", Font.BOLD, 11));
        titledSearchResultsPanel.setTitleForeground(Color.BLACK);

        pagination = initPagination(allElements);

        searchResultsPanel = new Cab2bPanel(new BorderLayout(0, 5));

        Cab2bComboBox serviceURLCombo = new Cab2bComboBox(serviceURLComboContents);
        serviceURLCombo.setPreferredSize(new Dimension(250, 20));
        serviceURLCombo.addActionListener(new ServiceURLSelectionListener());
        Cab2bPanel comboContainer = new Cab2bPanel(new RiverLayout(5, 5));
        
        JLabel jLabel = new JLabel("Results From");
        jLabel.setForeground(new Cab2bHyperlink().getUnclickedColor());
        
        
        comboContainer.add("left", jLabel);
        comboContainer.add("tab", serviceURLCombo);

        searchResultsPanel.add(BorderLayout.NORTH, comboContainer);

        searchResultsPanel.add(BorderLayout.CENTER, pagination);
        initDataListSummaryPanel();
        initDataListButtons();

        Cab2bPanel buttonPanel = new Cab2bPanel(new RiverLayout(8, 0));
        buttonPanel.add(addToDataListButton);
        buttonPanel.add(m_applyAllButton);
        searchResultsPanel.add(BorderLayout.SOUTH, buttonPanel);

        m_addSummaryParentPanel = new Cab2bPanel(new BorderLayout());
        m_addSummaryParentPanel.add(searchResultsPanel, BorderLayout.CENTER);
        m_addSummaryParentPanel.add(myDataListParentPanel, BorderLayout.EAST);
        titledSearchResultsPanel.setContentContainer(m_addSummaryParentPanel);
        this.add("p vfill hfill", titledSearchResultsPanel);
    }

    /**
     * 
     */
    private JPagination initPagination(Vector<PageElement> elements) {
        Pager pager = new NumericPager(elements, 3);
        JPagination pagination = new JPagination(elements, pager, this, true);
        pagination.addPageElementActionListener(searchPanel.getHyperlinkAL());
        return pagination;
    }

    /**
     * @see edu.wustl.cab2b.client.ui.viewresults.ResultPanel#getSelectedDataRows()
     */
    List<IDataRow> getSelectedDataRows() {
        List<Vector> selectedUserObjects = pagination.getSelectedPageElementsUserObjects();
        List<IDataRow> selectedDataRows = new ArrayList<IDataRow>();
        for (Vector recordListUserObject : selectedUserObjects) {
            selectedDataRows.add((IDataRow) recordListUserObject.get(0));
        }
        return selectedDataRows;
    }
/**
 * This class is the item action listener for the combo box when user selects 
 * particular url it will refresh the result panel and will show only the results 
 * from the selected url.   
 * @author atul_jawale
 *
 */
    class ServiceURLSelectionListener implements ActionListener {

        public void actionPerformed(ActionEvent event) {

            JComboBox comboBox = (JComboBox) event.getSource();
            Vector<PageElement> elements = null;
            int index = comboBox.getSelectedIndex();
            //check if the user selected to view all the results
            if (index == 0) {
                elements = allElements;
            } else { 
                String url = (String) comboBox.getSelectedItem();
                elements = URLSToResultRowMap.get(url);
            }
            searchResultsPanel.remove(pagination);
            pagination = initPagination(elements);

            searchResultsPanel.revalidate();
            searchResultsPanel.add(BorderLayout.CENTER, pagination);
            searchResultsPanel.revalidate();

            m_addSummaryParentPanel = new Cab2bPanel(new BorderLayout());
            m_addSummaryParentPanel.add(searchResultsPanel, BorderLayout.CENTER);
            m_addSummaryParentPanel.add(myDataListParentPanel, BorderLayout.EAST);
            titledSearchResultsPanel.setContentContainer(m_addSummaryParentPanel);

            titledSearchResultsPanel.revalidate();
            validate();

        }

    }
}