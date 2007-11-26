package edu.wustl.cab2b.client.ui.viewresults;

import static edu.wustl.cab2b.client.ui.util.ClientConstants.DETAILS_COLUMN_IMAGE;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.table.TableModel;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTable;
import edu.wustl.cab2b.client.ui.controls.sheet.JSheet;
import edu.wustl.cab2b.client.ui.experiment.ApplyFilterPanel;
import edu.wustl.cab2b.client.ui.experiment.ExperimentDataCategoryGridPanel;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.util.Utility;

/**
 * This is the default panel to show in multiple records of entity,
 * 
 * @author rahul_ner
 * 
 */
public class DefaultSpreadSheetViewPanel extends Cab2bPanel implements DataListDetailedPanelInterface {
    private static final long serialVersionUID = 1L;

    private Cab2bTable table;

    private JSheet spreadsheet = new JSheet();

    private ImageIcon defaultCellImage = new ImageIcon(
            this.getClass().getClassLoader().getResource(DETAILS_COLUMN_IMAGE));

    /**
     * List of records to be displayed
     */
    private List<IRecord> records;

    private Vector<Vector<Object>> tableData;

    private Vector<String> tableHeader;

    private Boolean showCheckBox;

    private Boolean showFilterPanel;

    private Boolean showDefaultTable = false;

    private ApplyFilterPanel applyFilterPanel;

    private Map<String, AttributeInterface> attributeMap = new HashMap<String, AttributeInterface>();

    private ExperimentDataCategoryGridPanel expGridPanel = null;

    public DefaultSpreadSheetViewPanel(
            Boolean showFilterPanel,
            Boolean showCheckBox,
            List<IRecord> records,
            Boolean showDefaultTable,
            ExperimentDataCategoryGridPanel expGridPanel) {
        this.showFilterPanel = showFilterPanel;
        this.showCheckBox = showCheckBox;
        this.showDefaultTable = showDefaultTable;
        this.setName("DataListDetailedPanel");
        this.records = records;
        this.expGridPanel = expGridPanel;
    }

    /* public DefaultSpreadSheetViewPanel(
     Boolean showFilterPanel,
     Boolean showCheckBox,
     IRecord record,
     Boolean showDefaultTable) {
     this.showFilterPanel = showFilterPanel;
     this.showCheckBox = showCheckBox;
     this.showDefaultTable = showDefaultTable;
     this.setName("DefaultSpreadSheetViewPanel");
     this.records = Collections.singletonList(record);
     }*/

    public DefaultSpreadSheetViewPanel(List<IRecord> records) {
        this(false, true, records, false, null);
    }

    public DefaultSpreadSheetViewPanel(Boolean showCheckBox, List<IRecord> records) {
        this(false, showCheckBox, records, false, null);
    }

    /*    public DefaultSpreadSheetViewPanel(IRecord record) {
     this(false, true, record, false);
     }

     public DefaultSpreadSheetViewPanel(Boolean showCheckBox, IRecord record) {
     this(false, showCheckBox, record, false);
     }*/

    /**
     * @see edu.wustl.cab2b.client.ui.controls.Cab2bPanel#doInitialization()
     */
    public void doInitialization() {
        //initializeTableData();
        initializeGUI();
    }

    /**
     * Initailizes the UI components
     */
    private void initializeGUI() {
        this.setBorder(null);
        this.removeAll();

        spreadsheet.setReadOnlytDataModel(new RecordsTableModel(records));
        spreadsheet.addPropertyChangeListener(new MagnifierGlassListener());
        this.add("br hfill vfill", spreadsheet);

    }

    private void initializeGUIOld() {
        this.setBorder(null);
        this.removeAll();

        table = new Cab2bTable(showCheckBox, tableData, tableHeader);
        table.setDefaultRenderer(Boolean.class, table.getDefaultRenderer(Object.class));

        table.setColumnSelectionAllowed(true);

        if (showFilterPanel) {
            applyFilterPanel = new ApplyFilterPanel(this);
            this.add(applyFilterPanel);
            this.add("br ", new Cab2bLabel(" "));
        }

        if (showDefaultTable) {
            table.addMouseListener(new IconMouseListner());

            // setting first column width FIXED
            if (table.getColumnCount() > 0) {
                table.getColumn(0).setMaxWidth(50);
            }
            table.addMouseMotionListener(new MouseMotionAdapter() {
                // if mouse is moving under first column
                // set hand courser
                public void mouseMoved(MouseEvent evt) {
                    Cab2bTable table = (Cab2bTable) evt.getSource();
                    if (table.getColumnModel().getColumnIndexAtX(evt.getX()) == 0) {
                        table.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    } else {
                        table.setCursor(Cursor.getDefaultCursor());
                    }
                }
            });
        }

        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.getViewport().add(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                      JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.add("br hfill vfill", jScrollPane);

    }

    /**
     * Initailizes the data to be viewed.
     */
    private void initializeTableData() {
        tableData = new Vector<Vector<Object>>();
        tableHeader = new Vector<String>();
        attributeMap.clear();

        if (!records.isEmpty()) {
            List<AttributeInterface> attributeList = Utility.getAttributeList(records.get(0).getAttributes());

            if (showDefaultTable) {
                tableHeader.add("Details");
            }

            // Add Headers
            for (AttributeInterface attribute : attributeList) {
                String formattedString = CommonUtils.getFormattedString(attribute.getName());
                attributeMap.put(formattedString, attribute);
                tableHeader.add(formattedString);
            }

            // Add Data
            for (IRecord record : records) {
                Vector<Object> row = new Vector<Object>();

                if (showDefaultTable) {
                    row.add(defaultCellImage);
                }

                for (AttributeInterface attribute : attributeList) {
                    row.add(record.getValueForAttribute(attribute));
                }
                tableData.add(row);
            }
        }
    }

    public void setJSheetConsoleVisible(boolean value) {
        spreadsheet.setConsoleVisible(value);
    }

    public void setJSheetMagnifyingGlassVisible(boolean value) {
        spreadsheet.setMagnifyingGlassVisible(value);
    }
    /**
     * @return
     */
    public Cab2bTable getDataTable() {
        return table;
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
        List<IRecord> selectedRecords = new ArrayList<IRecord>(table.getRowCount());
        for (int i = 0; i < table.getRowCount(); i++) {
            int originalRowIndex = table.convertRowIndexToModel(i);
            selectedRecords.add(records.get(originalRowIndex));
        }
        return selectedRecords;
    }

    /**
     * @see edu.wustl.cab2b.client.ui.viewresults.DataListDetailedPanelInterface#getCSVData()
     */
    public String getCSVData() {

        StringBuffer sb = new StringBuffer();

        // Save data to selected file
        TableModel model = table.getModel();
        int totalColumns = table.getColumnCount();
        for (int j = 1; j < totalColumns; j++) {

            if (j != 1)
                sb.append(",");

            //If special character in the column name put it into double quotes
            String text = model.getColumnName(j);
            text = CommonUtils.escapeString(text);
            sb.append(text);
        }
        sb.append("\n");

        int[] selectedRows = table.getSelectedRows();

        // Write the actual column values to file
        for (int i = 0; i < selectedRows.length; i++) {
            for (int j = 1; j < totalColumns; j++) {
                Object object = table.getValueAt(selectedRows[i], j);

                if (j != 1)
                    sb.append(",");

                if (object == null) {
                    sb.append("");
                } else {
                    //If special character in the column name put it into double quotes
                    String text = object.toString();
                    text = CommonUtils.escapeString(text);
                    sb.append(text);
                }
            }
            sb.append("\n");
        }

        return sb.toString();

    }

    public int getNoOfSelectedRows() {
        return table.getSelectedRows().length;
    }

    public ApplyFilterPanel getFilterPanel() {
        return applyFilterPanel;
    }

    class MagnifierGlassListener implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals(spreadsheet.EVENT_MAGNIFYING_GLASS_CLICKED)) {
                Integer rowNumber = (Integer) evt.getNewValue();
                DefaultDetailedPanel defaultDetailedPanel = ResultPanelFactory.getResultDetailedPanel(records.get(rowNumber.intValue()));
                expGridPanel.addDetailTabPanel("Details_" + (rowNumber.intValue() + 1), defaultDetailedPanel);
            }
        }

    }

    class IconMouseListner extends MouseAdapter {

        public void mouseClicked(MouseEvent mouseEvent) {
            Cab2bTable table = (Cab2bTable) mouseEvent.getSource();
            if (table.getColumnModel().getColumnIndexAtX(mouseEvent.getX()) == 0) {
                int row = table.getSelectionModel().getLeadSelectionIndex();
                DefaultDetailedPanel defaultDetailedPanel = ResultPanelFactory.getResultDetailedPanel(records.get(table.convertRowIndexToModel(row)));
                //WindowUtilities.showInDialog(NewWelcomePanel.mainFrame, defaultDetailedPanel, "Details", new Dimension(750, 580), true, true);
                Container container = table.getParent().getParent().getParent().getParent().getParent().getParent();
                if (container != null && container instanceof ExperimentDataCategoryGridPanel) {
                    ExperimentDataCategoryGridPanel experimentDataCategoryGridPanel = (ExperimentDataCategoryGridPanel) container;
                    experimentDataCategoryGridPanel.addDetailTabPanel("Details_" + (row + 1), defaultDetailedPanel);
                }
            }
        }

    }
}
