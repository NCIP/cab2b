package edu.wustl.cab2b.client.ui.viewresults;

import javax.swing.event.TableModelListener;

import edu.wustl.cab2b.client.ui.controls.temp.BDQDataSource;
import edu.wustl.cab2b.client.ui.controls.temp.LazyTableModel;

/**
 * @author rahul_ner
 *
 */
public class BDQTableModel extends LazyTableModel<BDQDataSource> {

    public BDQTableModel(BDQDataSource dataSource) {
        super(dataSource);
    }

    public int getRowCount() {
        return dataSource.getCurrentData().getDim3Labels().length;
    }

    public int getColumnCount() {
        return dataSource.getCurrentData().getDim1Labels().length
                * dataSource.getCurrentData().getDim2Labels().length;

    }

    public String getColumnName(int columnIndex) {

        int dim2Size = dataSource.getCurrentData().getDim2Labels().length;

        int dim1Index = columnIndex / dim2Size;
        int dim2Index = columnIndex - dim1Index;

        return dataSource.getCurrentData().getDim2Labels()[dim2Index] + "_"
                + dataSource.getCurrentData().getDim1Labels()[dim1Index];

    }

    public Class<?> getColumnClass(int arg0) {
        return String.class;
    }

    public boolean isCellEditable(int arg0, int arg1) {
        return false;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {

        int dim2Size = dataSource.getData(rowIndex,columnIndex).getDim2Labels().length;

        int dim1Index = columnIndex / dim2Size;
        int dim2Index = columnIndex - dim1Index;
        int dim3Index = rowIndex;

        return dataSource.getCurrentData().getCube()[dim1Index][dim2Index][dim3Index];

    }

    public void setValueAt(Object arg0, int arg1, int arg2) {

    }

    public void addTableModelListener(TableModelListener arg0) {

    }

    public void removeTableModelListener(TableModelListener arg0) {

    }
}
