package edu.wustl.cab2b.client.ui.viewresults;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import edu.wustl.cab2b.common.queryengine.result.I3DDataRecord;

/**
 * @author rahul_ner
 *
 */
public class BDQTableModel implements TableModel {

    I3DDataRecord record;

    public BDQTableModel(I3DDataRecord record) {
        this.record = record;
    }

    public int getRowCount() {
        return record.getDim3Labels().length;
    }

    public int getColumnCount() {
        return record.getDim1Labels().length * record.getDim2Labels().length;

    }

    public String getColumnName(int columnIndex) {

        int dim2Size = record.getDim2Labels().length;

        int dim1Index = columnIndex / dim2Size;
        int dim2Index = columnIndex - dim1Index;

        return record.getDim2Labels()[dim2Index] + "_" + record.getDim1Labels()[dim1Index];

    }

    public Class<?> getColumnClass(int arg0) {
        return String.class;
    }

    public boolean isCellEditable(int arg0, int arg1) {
        return false;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {

        int dim2Size = record.getDim2Labels().length;

        int dim1Index = columnIndex / dim2Size;
        int dim2Index = columnIndex - dim1Index;
        int dim3Index = rowIndex;

        return record.getCube()[dim1Index][dim2Index][dim3Index];

    }

    public void setValueAt(Object arg0, int arg1, int arg2) {

    }

    public void addTableModelListener(TableModelListener arg0) {

    }

    public void removeTableModelListener(TableModelListener arg0) {

    }
}
