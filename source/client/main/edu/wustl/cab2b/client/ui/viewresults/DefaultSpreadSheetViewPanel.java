package edu.wustl.cab2b.client.ui.viewresults;

import static edu.wustl.cab2b.client.ui.util.ClientConstants.DETAILS_COLUMN_IMAGE;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.table.AbstractTableModel;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.sheet.JSheet;
import edu.wustl.cab2b.client.ui.experiment.ExperimentDataCategoryGridPanel;
import edu.wustl.cab2b.common.queryengine.result.IRecord;

/**
 * This is the default panel to show in multiple records of entity,
 * 
 * @author rahul_ner
 * 
 */
public class DefaultSpreadSheetViewPanel extends Cab2bPanel implements DataListDetailedPanelInterface {
    private static final long serialVersionUID = 1L;

    private JSheet spreadsheet = new JSheet();

    public static final String SPREADSHEET_MODEL_INSTALLED = "SPREADSHEET_MODEL_INSTALLED";

    private ImageIcon defaultCellImage = new ImageIcon(
            this.getClass().getClassLoader().getResource(DETAILS_COLUMN_IMAGE));

    /**
     * List of records to be displayed
     */
    private List<IRecord> records;

    private Map<String, AttributeInterface> attributeMap = new HashMap<String, AttributeInterface>();

    private ExperimentDataCategoryGridPanel expGridPanel = null;

    public DefaultSpreadSheetViewPanel(List<IRecord> records, ExperimentDataCategoryGridPanel expGridPanel) {
        this.setName("DataListDetailedPanel");
        this.records = records;
        this.expGridPanel = expGridPanel;
    }

    public DefaultSpreadSheetViewPanel(List<IRecord> records) {
        this(records, null);
    }

    /**
     * @see edu.wustl.cab2b.client.ui.controls.Cab2bPanel#doInitialization()
     */
    public void doInitialization() {
        initializeGUI();
    }

    /**
     * Initailizes the UI components
     */
    private void initializeGUI() {
        this.setBorder(null);
        this.removeAll();

        spreadsheet.setReadOnlyDataModel(new RecordsTableModel(records));
        spreadsheet.addPropertyChangeListener(new ShowRecordDetailsListener());
        this.add("br hfill vfill", spreadsheet);
        firePropertyChange(SPREADSHEET_MODEL_INSTALLED, null, spreadsheet);
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
     * This method returns the current records present in the table after
     * applying any filtering.
     * 
     * @return
     */
    public List<IRecord> getSelectedRecords() {
        int selectedRowCount = spreadsheet.getSelectedRows().length;
        List<IRecord> selectedRecords = new ArrayList<IRecord>(selectedRowCount);
        for (int i = 0; i < selectedRowCount; i++) {
            selectedRecords.add(records.get(spreadsheet.getSelectedRows()[i]));
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
                expGridPanel.addDetailTabPanel("Details_" + (rowNumber.intValue() + 1), defaultDetailedPanel);
            } else if (evt.getPropertyName().equals(spreadsheet.EVENT_DATA_SINGLE_CLICKED)) {
                firePropertyChange(spreadsheet.EVENT_DATA_SINGLE_CLICKED, evt.getOldValue(), evt.getNewValue());
            }
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
        for (int rowIndex = 0; rowIndex < getSelectedRows().length; rowIndex++)
            for (int columnIndex = 0; columnIndex < getSelectedColumns().length; columnIndex++) {
                data[rowIndex][columnIndex] = getSpreadSheetDataModel().getValueAt(
                                                                                   getSelectedRows()[rowIndex],
                                                                                   getSelectedColumns()[columnIndex]);
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
}
