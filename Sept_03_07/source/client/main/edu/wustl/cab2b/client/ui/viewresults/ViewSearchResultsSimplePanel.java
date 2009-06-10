package edu.wustl.cab2b.client.ui.viewresults;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.painter.gradient.BasicGradientPainter;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTitledPanel;
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
    private Vector<PageElement> elements = null;

    private IDataRow parentDataRow;

    private IAssociation queryAssociation = null;

    private JXPanel m_addSummaryParentPanel;

    private JPagination pagination;

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
        elements = new Vector<PageElement>();

        String className = Utility.getDisplayName(queryResult.getOutputEntity());
        List<AttributeInterface> attributes = Utility.getAttributeList(queryResult);
        int attributeSize = attributes.size();
        //int attributeLimitInDescStr = (attributeSize < 10) ? attributeSize : 10;

        Map<String, List<IRecord>> allRecords = queryResult.getRecords();
        for (String url : allRecords.keySet()) {
            List<IRecord> recordList = allRecords.get(url);

            for (IRecord record : recordList) {
                StringBuffer descBuffer = new StringBuffer();
                for (int i = 0; i < attributeSize; i++) {
                    String value = record.getValueForAttribute(attributes.get(i));
                    if (value != null && !value.equals("")) {
                        if (i != 0) {
                            descBuffer.append(",");
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

                elements.add(element);
            }
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
        final JXTitledPanel titledSearchResultsPanel = new Cab2bTitledPanel("Search Results :- "
                + "Total results ( " + elements.size() + " )");
        GradientPaint gp = new GradientPaint(new Point2D.Double(.05d, 0), new Color(185, 211, 238),
                new Point2D.Double(.95d, 0), Color.WHITE);
        titledSearchResultsPanel.setTitlePainter(new BasicGradientPainter(gp));
        titledSearchResultsPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        titledSearchResultsPanel.setTitleFont(new Font("SansSerif", Font.BOLD, 11));
        titledSearchResultsPanel.setTitleForeground(Color.BLACK);
        
        Pager pager = new NumericPager(elements, 3);
        pagination = new JPagination(elements, pager, this, true);
        pagination.addPageElementActionListener(searchPanel.getHyperlinkAL());
       
        searchResultsPanel = new Cab2bPanel(new RiverLayout(0,5));
        searchResultsPanel.add("vfill hfill", pagination);
        initDataListSummaryPanel();
        initDataListButtons();
        
        Cab2bPanel buttonPanel= new Cab2bPanel(new RiverLayout(8,0));
        buttonPanel.add(addToDataListButton);
        buttonPanel.add(m_applyAllButton);
        searchResultsPanel.add("br", buttonPanel);

        m_addSummaryParentPanel = new Cab2bPanel(new BorderLayout());
        m_addSummaryParentPanel.add(searchResultsPanel, BorderLayout.CENTER);
        m_addSummaryParentPanel.add(myDataListParentPanel, BorderLayout.EAST);
        titledSearchResultsPanel.setContentContainer(m_addSummaryParentPanel);
        this.add("p vfill hfill", titledSearchResultsPanel);
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
}