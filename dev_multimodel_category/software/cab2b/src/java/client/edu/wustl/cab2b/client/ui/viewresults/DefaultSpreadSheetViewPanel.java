/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.viewresults;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import edu.common.dynamicextensions.domain.Attribute;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.sheet.JSheet;
import edu.wustl.cab2b.client.ui.controls.sheet.JSheetViewDataModel;
import edu.wustl.cab2b.client.ui.experiment.ExperimentDataCategoryGridPanel;
import edu.wustl.cab2b.client.ui.experiment.SaveDataCategoryPanel;
import edu.wustl.cab2b.client.ui.query.Utility;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.queryengine.result.QueryResultFactory;
import edu.wustl.cab2b.common.queryengine.result.RecordId;

/**
 * This is the default panel to show in multiple records of entity,
 * 
 * @author rahul_ner/Deepak_Shingan
 * 
 */
public class DefaultSpreadSheetViewPanel extends Cab2bPanel implements DataListDetailedPanelInterface {
    private static final long serialVersionUID = 1L;

    /**
     * JSheet component 
     */
    private JSheet spreadsheet = new JSheet();

    /**
     * Property name for SPREADSHEET_MODEL_INSTALLED event 
     */
    public static final String SPREADSHEET_MODEL_INSTALLED = "SPREADSHEET_MODEL_INSTALLED";

    /**
     * Property name for SPREADSHEET_MODEL_UNINSTALLED event
     */
    public static final String SPREADSHEET_MODEL_UNINSTALLED = "SPREADSHEET_MODEL_UNINSTALLED";

    /**
     * Property name for DISABLE_CHART_LINK event
     */
    public static final String DISABLE_CHART_LINK = "DISABLE_CHART_LINK";

    /**
     * Property name for ENABLE_CHART_LINK event
     */
    public static final String ENABLE_CHART_LINK = "ENABLE_CHART_LINK";    

    /**
     * Property name for DISABLE_ANALYSIS_LINK event
     */
    public static final String DISABLE_ANALYSIS_LINK = "DISABLE_ANALYSIS_LINK";

    /**
     * List of user defined attributes
     */
    private List<AttributeInterface> userDefinedAttributes = new ArrayList<AttributeInterface>();

    /**
     * List of records to be displayed
     */
    private List<IRecord> records;

    /**
     * Map of attribute name and AttributeInterface object
     */
    private Map<String, AttributeInterface> attributeMap = new HashMap<String, AttributeInterface>();

    /**
     * ExperimentDataCategoryGridPanel reference
     */
    private ExperimentDataCategoryGridPanel expGridPanel = null;

    /**
     * Constructor
     * Initilizes basic UI components 
     */
    DefaultSpreadSheetViewPanel() {
        initializeGUI();
    }

    /**
     * Constructor
     * Initilizes basic UI components
     * @param records
     * @param expGridPanel
     */
    public DefaultSpreadSheetViewPanel(List<IRecord> records, ExperimentDataCategoryGridPanel expGridPanel) {
        this.setName("DataListDetailedPanel");
        this.records = records;
        this.expGridPanel = expGridPanel;
    }

    /**
     * Constructor
     * Initilizes basic UI components with g
     * @param records
     */
    public DefaultSpreadSheetViewPanel(List<IRecord> records) {
        this(records, null);
    }

    /**
     * @see edu.wustl.cab2b.client.ui.controls.Cab2bPanel#doInitialization()
     */
    public void doInitialization() {
        initializeGUI();
    }

    /** onClicking show details of the selected row. */
    class SaveCategoryActionListener extends AbstractAction {
        ExperimentDataCategoryGridPanel gridPanel;

        public SaveCategoryActionListener(ExperimentDataCategoryGridPanel expGridPanel) {
            super("Save", createImageIcon("images/saveDataCategory.png"));
            gridPanel = expGridPanel;
            setToolTipText("Save as data category ");
            // JSheet will display text in ToolBar only if property SHOW_TEXT is
            // set..
            putValue("SHOW_TEXT", "SHOW_TEXT");
            // Tooltips...
            putValue(SHORT_DESCRIPTION, "Save as data category");
            putValue(LONG_DESCRIPTION, "Save as data category");
        }

        SaveCategoryActionListener() {
            this(null);
        }

        public void actionPerformed(ActionEvent event) {
            SaveDataCategoryPanel saveDialogPanel = new SaveDataCategoryPanel(gridPanel);
        }
    }

    /** Returns an ImageIcon, or null if the path was invalid. */
    protected ImageIcon createImageIcon(String pathImage) {

        pathImage = "/" + this.getClass().getPackage().getName().replace('.', '/') + '/' + pathImage;
        // pathImage = '/'+this.getClass().getPackage().getName().replace('.',
        // '/') + '/'+pathImage;
        java.net.URL imgURL = getClass().getResource(pathImage);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("[DefaultSpreadSheetViewPanel]: Unable to locate Toolbar icon images: " + pathImage);
            return null;
        }
    }

    public EntityInterface getOldRecordsEntity() {
        return Utility.getEntity(records);
    }

    /**
     * Initailizes the UI components
     */
    private void initializeGUI() {
        this.setBorder(null);
        this.removeAll();

        // show save category tool bar button only when this panel is opened in
        // Experiment context
        if (expGridPanel != null) {
            Action saveCategoryAction = new SaveCategoryActionListener(expGridPanel);
            List<Action> arrayList = new ArrayList<Action>();
            arrayList.add(saveCategoryAction);
            spreadsheet.setAdditionalToolbarActions(arrayList);
        } else {
            spreadsheet.removeComponentFromToolBar(JSheet.MENU_BUTTON_PROPERTIES);
            spreadsheet.removeComponentFromToolBar(JSheet.MENU_BUTTON_COPY);
            spreadsheet.removeComponentFromToolBar(JSheet.MENU_BUTTON_PASTE);
            spreadsheet.removeComponentFromToolBar(JSheet.MENU_BUTTON_ADD_COLUMN);
            spreadsheet.removeComponentFromToolBar(JSheet.MENU_BUTTON_RESET);
            spreadsheet.removeComponentFromToolBar(JSheet.MENU_BUTTON_CLEAR);
        }

        spreadsheet.setReadOnlyDataModel(new RecordsTableModel(records));
        spreadsheet.addPropertyChangeListener(new ShowRecordDetailsListener());
        this.add("br hfill vfill", spreadsheet);
        spreadsheet.addAncestorListener(new javax.swing.event.AncestorListener() {

            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {

            }

            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                firePropertyChange(SPREADSHEET_MODEL_INSTALLED, null, spreadsheet);
            }

            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
                firePropertyChange(SPREADSHEET_MODEL_UNINSTALLED, null, spreadsheet);
            }

        });
    }

    private AbstractTableModel getSpreadSheetDataModel() {
        return spreadsheet.getViewTableModel();
    }

    public int[] getSelectedRows() {
        return spreadsheet.getSelectedRows();
    }

    public int[] getSelectedColumns() {
        return spreadsheet.getSelectedColumns();
    }

    /**
     * @param columnName
     * @return
     */
    public AttributeInterface getColumnAttribute(String columnName) {
        return attributeMap.get(columnName);
    }

    /**
     * @param records
     */
    public void refreshView(List<IRecord> records) {
        this.records = records;
        doInitialization();
        updateUI();
    }

    /**
     * This method returns modified list of IRecords visible in Jsheet. All
     * extra added columns also gets added in IRecords as a new attributes along
     * with all original record attributes.
     * 
     * @return
     */
    public List<IRecord> getAllVisibleRecords() {
        JSheetViewDataModel tableModel = spreadsheet.getViewTableModel();

        // spreadsheet.firePropertyChange(, oldValue, newValue);
        int rowCount = tableModel.getRowCount();
        if (rowCount <= 0) {
            return null;
        }

        List<IRecord> selectedRecords = new ArrayList<IRecord>(rowCount);
        // check if extra column added
        if (isExtraColumnAdded()) {
            // Add all attributes in a collection and pass it as an argument for
            // new record
            Map<AttributeInterface, Object> mapAttributeValue = null;
            new HashMap<AttributeInterface, Object>();

            for (int rowIndex = 0; rowIndex < rowCount && rowIndex < records.size(); rowIndex++) {
                int modelRowNumber = tableModel.convertRowIndexToModel(rowIndex);
                IRecord record = records.get(modelRowNumber);
                mapAttributeValue = new HashMap<AttributeInterface, Object>();
                for (AttributeInterface attribute : record.getAttributes()) {
                    AttributeInterface newAttribute = new Attribute();
                    newAttribute.setName(attribute.getName());
                    newAttribute.setAttributeTypeInformation(attribute.getAttributeTypeInformation());
                    mapAttributeValue.put(newAttribute, record.getValueForAttribute(attribute));
                }

                TableModel tblModel = spreadsheet.getJSheetTableModel();

                // Now add user defined attributes
                int dataModelColCount = tblModel.getColumnCount();
                for (int columnIndex = record.getAttributes().size(); columnIndex < dataModelColCount; columnIndex++) {
                    if (isUserColumnDefined(columnIndex)) {
                        AttributeInterface newAttribute = DomainObjectFactory.getInstance().createStringAttribute();
                        newAttribute.setName(tblModel.getColumnName(columnIndex));
                        mapAttributeValue.put(newAttribute, tblModel.getValueAt(modelRowNumber, columnIndex));
                    }
                }

                // Fill data in the records
                IRecord newRecord = QueryResultFactory.createRecord(mapAttributeValue.keySet(), new RecordId("",
                        ""));
                Set<AttributeInterface> newAttributeSet = mapAttributeValue.keySet();
                for (AttributeInterface newAttribute : newAttributeSet) {
                    newRecord.putValueForAttribute(newAttribute, mapAttributeValue.get(newAttribute));
                }
                selectedRecords.add(newRecord);
            }
        } else {
            for (int i = 0; i < rowCount && i < records.size(); i++) {
                int recordNumber = tableModel.convertRowIndexToModel(i);
                selectedRecords.add(records.get(recordNumber));
            }
        }
        return selectedRecords;
    }

    private boolean isExtraColumnAdded() {
        for (int i = 0; i < spreadsheet.getJSheetTableModel().getColumnCount(); i++) {
            if (isUserColumnDefined(i)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method to check whether the column of selected index is userdefined or
     * not
     * 
     * @param viewColumnIndex
     * @return
     */
    private boolean isUserColumnDefined(int viewColumnIndex) {
        return spreadsheet.getJSheetTableModel().isCellEditable(0, viewColumnIndex);
    }

    /**
     * This method returns the current records present in the table after
     * applying any filtering.
     * 
     * @return
     */
    public List<IRecord> getSelectedRecords() {
        int selectedRowCount = spreadsheet.getSelectedRows().length;
        List<IRecord> selectedRecords = new ArrayList<IRecord>(selectedRowCount);
        for (int i = 0; i < selectedRowCount && i < records.size(); i++) {
            int recordNumber = spreadsheet.getViewTableModel().convertRowIndexToModel(
                                                                                      spreadsheet.getSelectedRows()[i]);
            selectedRecords.add(records.get(recordNumber));
        }
        return selectedRecords;
    }

    public JComponent getFilterPanel() {
        return spreadsheet.getContextFilterConsole();
    }

    class ShowRecordDetailsListener implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals(spreadsheet.REQUESTED_SHOW_ROW_DETAILS)) {
                Integer rowNumber = (Integer) evt.getNewValue();
                DefaultDetailedPanel defaultDetailedPanel = ResultPanelFactory.getResultDetailedPanel(records.get(rowNumber.intValue()));
                defaultDetailedPanel.addPropertyChangeListener(new PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent evt) {
                        firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
                    }
                });
                if (expGridPanel != null) {
                    expGridPanel.addDetailTabPanel("Details_" + (rowNumber.intValue() + 1), defaultDetailedPanel);
                    firePropertyChange(DISABLE_CHART_LINK, evt.getOldValue(), evt.getNewValue());
                } else {
                    Container container = spreadsheet.getParent();
                    container.remove(spreadsheet);
                    container.add("hfill vfill", defaultDetailedPanel);
                    container.validate();
                }
            }
            firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
        }
    }

    public List<String> getSelectedColumnNames() {
        List<String> selectedColumnNames = new ArrayList<String>(getSelectedColumns().length);
        for (int i = 0; i < getSelectedColumns().length; i++) {
            selectedColumnNames.add(getSpreadSheetDataModel().getColumnName(getSelectedColumns()[i]));
        }
        return selectedColumnNames;
    }

    public Object[][] getSelectedData() {
        Object data[][] = new Object[getSelectedRows().length][getSelectedColumns().length];
        for (int rowIndex = 0; rowIndex < getSelectedRows().length; rowIndex++) {
            for (int columnIndex = 0; columnIndex < getSelectedColumns().length; columnIndex++) {
                data[rowIndex][columnIndex] = getSpreadSheetDataModel().getValueAt(
                                                                                   getSelectedRows()[rowIndex],
                                                                                   getSelectedColumns()[columnIndex]);
            }
        }
        return data;
    }

    public List<String> getSelectedRowNames() {
        List<String> selectedRowNames = new ArrayList<String>(getSelectedRows().length);
        for (int i = 0; i < getSelectedRows().length; i++) {
            selectedRowNames.add("" + getSelectedRows()[i]);
        }
        return selectedRowNames;
    }

    /**
     * @return the userDefinedAttributes
     */
    public List<AttributeInterface> getUserDefinedAttributes() {
        userDefinedAttributes.clear();
        for (int i = 0; i < spreadsheet.getJSheetTableModel().getColumnCount(); i++) {
            if (isUserColumnDefined(i)) {
                AttributeInterface newAttribute = DomainObjectFactory.getInstance().createStringAttribute();
                newAttribute.setName(spreadsheet.getJSheetTableModel().getColumnName(i));
                userDefinedAttributes.add(newAttribute);
            }
        }
        return userDefinedAttributes;
    }
}
