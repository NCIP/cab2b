package edu.wustl.cab2b.client.ui.viewresults;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.table.TableModel;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTable;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.util.Utility;

/**
 * This is the default panel to show in cases
 * 1. The title node is selected (displays IRecord of each childeren)
 * 2. The selected non-title node does not have any special viewer
 * @author rahul_ner
 *
 */
public class DefaultDataListDetailedPanel extends Cab2bPanel implements DataListDetailedPanelInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Cab2bTable table;

    /**
     * List of records to be displayed
     */
    private List<IRecord> records;

    private Vector<Vector> tableData = new Vector<Vector>();

    private Vector<String> tableHeader = new Vector<String>();

    public DefaultDataListDetailedPanel(List<IRecord> records) {
        this.records = records;
    }

    public DefaultDataListDetailedPanel(IRecord record) {
        this.records = Collections.singletonList(record);
    }

    /**
     * @see edu.wustl.cab2b.client.ui.controls.Cab2bPanel#doInitialization()
     */
    public void doInitialization() {
        initData();
        initGUI();
    }

    /**
     * Initailizes the UI components 
     */
    private void initGUI() {
        this.setLayout(new RiverLayout());
        table = new Cab2bTable(true, tableData, tableHeader);
        this.add("br hfill vfill", new JScrollPane(table));
    }

    /**
     * Initailizes the data to be viewed. 
     */
    private void initData() {
        List<AttributeInterface> attributeList = Utility.getAttributeList(records.get(0).getAttributes());

        //Add Headers
        for (AttributeInterface attribute : attributeList) {
            String formattedString = CommonUtils.getFormattedString(attribute.getName());
            tableHeader.add(formattedString);
        }

        //Add Data
        for (IRecord record : records) {
            Vector<Object> row = new Vector<Object>();
            for (AttributeInterface attribute : attributeList) {
                row.add(record.getValueForAttribute(attribute));

            }
            tableData.add(row);
        }
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
            if (j != 1) {
                sb.append(",");
            }
            // If special character in the column name
            // put it into double quotes
            String text = model.getColumnName(j);
            text = escapeString(text);
            sb.append(text);
        }
        sb.append("\n");
        /**
         * Write the actual column values to file
         */
        for (int i = 0; i < table.getSelectedRows().length; i++) {
            for (int j = 1; j < totalColumns; j++) {
                Object object = table.getValueAt(table.getSelectedRows()[i], j);
                if (j != 1) {
                    sb.append(",");
                }
                if (object == null) {
                    sb.append("");
                } else {
                    // If special character in the column value
                    // put it into double quotes
                    String text = object.toString();
                    text = escapeString(text);
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

    private String escapeString(String input) {
        if (input.indexOf(",") != -1) {
            input = "\"" + input + "\"";
        }

        return input;
    }

}
