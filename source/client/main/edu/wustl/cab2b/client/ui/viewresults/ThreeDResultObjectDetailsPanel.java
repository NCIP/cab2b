package edu.wustl.cab2b.client.ui.viewresults;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JScrollPane;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import org.jdesktop.swingx.decorator.AlternateRowHighlighter;

import edu.wustl.cab2b.client.ui.controls.Cab2bTable;
import edu.wustl.cab2b.client.ui.controls.temp.BDQDataSource;
import edu.wustl.cab2b.common.queryengine.result.I3DDataRecord;
import edu.wustl.cab2b.common.queryengine.result.IPartiallyInitialized3DRecord;
import edu.wustl.cab2b.common.queryengine.result.IRecord;

/**
 * @author rahul_ner
 * 
 */
public class ThreeDResultObjectDetailsPanel extends DefaultDetailedPanel<I3DDataRecord> {

    private Cab2bTable threeDTable;

    BDQDataSource tableSource;

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param searchPanel
     * @param dataRow
     * @param record
     * @param incomingAssociationCollection
     * @param intraModelAssociationCollection
     * @throws ClassCastException if record is not of type {@link I3DDataRecord}.
     */
    public ThreeDResultObjectDetailsPanel(IRecord record) {
        super((I3DDataRecord) record);
        isVFill = false;
    }

    /**
     * @see edu.wustl.cab2b.client.ui.viewresults.ResultObjectDetailsPanel#initData()
     */
    //    protected void initData() {
    //        super.initData();
    //
    //        Object[][][] inputData = record.getCube();
    //
    //        for (int i = 0; i < inputData.length; i++) {
    //            for (int j = 0; j < inputData[i].length; j++) {
    //                String columnHeader = record.getDim2Labels()[j] + "_" + record.getDim1Labels()[i];
    //                threeDTableHeader.add(columnHeader);
    //                for (int k = 0; k < inputData[i][j].length; k++) {
    //                    Object value = inputData[i][j][k];
    //
    //                    if (threeDTableData.size() == k || threeDTableData.get(k) == null) {
    //                        threeDTableData.add(new Vector<Object>());
    //                    }
    //
    //                    Vector<Object> row = threeDTableData.get(k);
    //                    row.add(value);
    //                }
    //            }
    //        }
    //    }
    /**
     * @see edu.wustl.cab2b.client.ui.viewresults.ResultObjectDetailsPanel#initTableGUI()
     */
    protected void initGUI() {
        super.initGUI();

        adjustRows();

        threeDTable = new Cab2bTable();
        tableSource = new BDQDataSource((IPartiallyInitialized3DRecord<?, ?>) record);
        TableModel model = new BDQTableModel(tableSource);
        threeDTable.setModel(model);

        threeDTable.setColumnSelectionAllowed(true);

        threeDTable.setEditable(false);
        final JScrollPane tableSP = new JScrollPane(threeDTable);
        //        final AdjustmentListener Listener =  tableSP.getVerticalScrollBar().getAdjustmentListeners()[0];
        //        
        //        
        //         tableSP.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
        //            public void adjustmentValueChanged(AdjustmentEvent e) {
        //                tableSP.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        //                Listener.adjustmentValueChanged(e);
        //                tableSP.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        //            }
        //        });

        addRowHeader(tableSP);

        this.add("br hfill vfill", tableSP);
    }

    private void addRowHeader(JScrollPane tableSP) {
        I3DDataRecord threeDRecord = (I3DDataRecord) record;

        Object[] dim3RowHeaderHeader = { "Sequence" };
        Object[] dim3RowHeaderList = threeDRecord.getDim3Labels();
        Object[][] dim2RowHeader = new Object[dim3RowHeaderList.length][1];

        for (int i = 0; i < dim3RowHeaderList.length; i++) {
            dim2RowHeader[i][0] = dim3RowHeaderList[i];
        }
        Cab2bTable rowHeaderTable = new Cab2bTable(new AbstractTableModel() {

            public int getRowCount() {
                return tableSource.getCurrentData().getDim3Labels().length;
            }

            public int getColumnCount() {
                return 1;
            }

            @Override
            public String getColumnName(int arg0) {
                return "Sequence";
            }

            public Object getValueAt(int row, int column) {
                return tableSource.getCurrentData().getDim3Labels()[row];
            }

        });

        rowHeaderTable.setSize(new Dimension(0, 28));
        rowHeaderTable.setFont(new Font("Arial", Font.BOLD, 14));
        rowHeaderTable.setHighlighters();
        rowHeaderTable.setBackground(AlternateRowHighlighter.genericGrey.getForeground());

        tableSP.setRowHeaderView(rowHeaderTable);
    }

    public Cab2bTable getDataTable() {
        return threeDTable;
    }

    static final int NO_OF_CLOUMNS = 10;

    static final int NO_OF_ROWS = 5;
}
