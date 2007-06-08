package edu.wustl.cab2b.client.ui.viewresults;

import java.util.Vector;

import javax.swing.JScrollPane;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTable;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.datalist.IDataRow;
import edu.wustl.cab2b.common.queryengine.result.IRecord;

public class DefaultDetailedPanel<R extends IRecord> extends Cab2bPanel {

    /**
     * 
     */
    protected IDataRow dataRow;

    protected R record;

    protected Vector<Vector> tableData = new Vector<Vector>();

    private Vector<String> tableHeader = new Vector<String>();

    private Cab2bTable objDetailsTable;
    
    protected boolean isVFill = true;

    public DefaultDetailedPanel(IDataRow dataRow, R record) {
        this.dataRow = dataRow;
        this.record = record;
    }

    /**
     */
    public void doInitialization() {
        initData();
        initGUI();
    }

    protected void initData() {
        for (int i = 0; i < dataRow.getAttributes().size(); i++) {
            Vector<Object> row = new Vector<Object>();
            AttributeInterface attribute = dataRow.getAttributes().get(i);
            String formattedString = CommonUtils.getFormattedString(attribute.getName());
            row.add(formattedString);
            row.add(dataRow.getRow()[i]);
            tableData.add(row);
        }
        tableHeader.add("Attribute");
        tableHeader.add("Value");
    }

    protected void initGUI() {
        this.setLayout(new RiverLayout());
        
        objDetailsTable = new Cab2bTable(false, tableData, tableHeader);
        objDetailsTable.setEditable(false);
        JScrollPane tableSP = new JScrollPane(objDetailsTable);
        if (isVFill) {
            this.add("br hfill vfill",tableSP);
        } else {
            this.add("br hfill",tableSP);
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
     * @return Returns the dataRow.
     */
    public IDataRow getDataRow() {
        return dataRow;
    }
}
