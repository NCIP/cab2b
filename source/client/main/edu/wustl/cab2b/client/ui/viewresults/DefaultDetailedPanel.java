package edu.wustl.cab2b.client.ui.viewresults;

import java.util.List;
import java.util.Vector;

import javax.swing.JScrollPane;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTable;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.queryengine.result.IRecord;

public class DefaultDetailedPanel<R extends IRecord> extends Cab2bPanel implements DataListDetailedPanelInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    protected R record;

    protected Vector<Vector> tableData = new Vector<Vector>();

    private Vector<String> tableHeader = new Vector<String>();

    private Cab2bTable objDetailsTable;

    protected boolean isVFill = true;

    public DefaultDetailedPanel(R record) {
        this.record = record;
    }

    /**
     */
    public void doInitialization() {
        initData();
        initGUI();
    }

    protected void initData() {
        this.setName("DataListDetailedPanel");
        this.setLayout(new RiverLayout(0, 5));
        List<AttributeInterface> attributeList = edu.wustl.cab2b.common.util.Utility.getAttributeList(record.getAttributes());
        for (AttributeInterface attribute : attributeList) {
            String formattedString = CommonUtils.getFormattedString(attribute.getName());
            Vector<Object> row = new Vector<Object>();
            row.add(formattedString);
            row.add(record.getValueForAttribute(attribute));
            tableData.add(row);
        }

        tableHeader.add("Attribute");
        tableHeader.add("Value");
    }

    protected void initGUI() {
        objDetailsTable = new Cab2bTable(false, tableData, tableHeader);
        objDetailsTable.setColumnSelectionAllowed(true);
        objDetailsTable.setEditable(false);
        JScrollPane tableSP = new JScrollPane(objDetailsTable);
        if (isVFill) {
            this.add("br hfill vfill", tableSP);
        } else {
            this.add("br hfill", tableSP);
        }
    }

    /**
     * 
     */
    protected void adjustRows() {
        int rowCount = objDetailsTable.getRowCount();
        if (rowCount < 10) {
            objDetailsTable.setVisibleRowCount(rowCount);
        } else {
            objDetailsTable.setVisibleRowCount(10);
        }
    }

    /**
     * @see edu.wustl.cab2b.client.ui.viewresults.DataListDetailedPanelInterface#getCSVData()
     */
    public String getCSVData() {
        StringBuffer sb = new StringBuffer();

        // Write the actual column values to file
        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < objDetailsTable.getRowCount(); i++) {
                Object object = objDetailsTable.getValueAt(i, j);

                if (i != 0)
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

    /**
     * Method to table data along with get column header and row header values 
     * @param tableSP
     * @return
     */
    protected String getCSVTableHeaderAndData(JScrollPane tableSP) {

        //setting row header
        Cab2bTable rowHeaderTable = (Cab2bTable) tableSP.getRowHeader().getView();
        Cab2bTable table = (Cab2bTable) tableSP.getViewport().getView();
        StringBuffer sb = new StringBuffer();

        //setting column header
        sb.append(getCSVTableHeader(rowHeaderTable));
        sb.append(",");
        sb.append(getCSVTableHeader(table));
        sb.append("\n");

        // Write the actual column values to file
        for (int i = 0; i < table.getRowCount(); i++) {
            for (int j = 0; j < table.getColumnCount(); j++) {
                Object object = table.getValueAt(i, j);

                //adding row header value 
                if (j == 0)
                    sb.append(CommonUtils.escapeString(rowHeaderTable.getValueAt(i, 0).toString()));

                if (i != 0)
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

    protected String getCSVTableHeader(Cab2bTable table) {

        StringBuffer sb = new StringBuffer();

        for (int j = 0; j < table.getColumnCount(); j++) {
            if (j != 0)
                sb.append(",");
            //If special character in the column name put it into double quotes
            String text = table.getModel().getColumnName(j);
            text = CommonUtils.escapeString(text);
            sb.append(text);
        }

        return sb.toString();

    }

    protected String getCSVData(Cab2bTable table) {
        StringBuffer sb = new StringBuffer();

        // Write the actual column values to file
        for (int i = 0; i < table.getRowCount(); i++) {
            for (int j = 0; j < table.getColumnCount(); j++) {
                Object object = table.getValueAt(i, j);

                if (i != 0)
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

    /**
     * @see edu.wustl.cab2b.client.ui.viewresults.DataListDetailedPanelInterface#getNoOfSelectedRows()
     */
    public final int getNoOfSelectedRows() {
        return 1;
    }

    public Cab2bTable getDataTable() {
        return objDetailsTable;
    }

}
