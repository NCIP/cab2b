package edu.wustl.cab2b.client.ui.viewresults;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JScrollPane;
import javax.swing.table.AbstractTableModel;

import org.jdesktop.swingx.decorator.AlternateRowHighlighter;

import edu.wustl.cab2b.client.ui.controls.Cab2bTable;
import edu.wustl.cab2b.client.ui.controls.LazyTable.DefaultLazyTableModel;
import edu.wustl.cab2b.client.ui.controls.LazyTable.LazyTableModelInterface;
import edu.wustl.cab2b.client.ui.controls.LazyTable.MatrixCache;
import edu.wustl.cab2b.client.ui.controls.LazyTable.PageDimension;
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
     * @see edu.wustl.cab2b.client.ui.viewresults.ResultObjectDetailsPanel#initTableGUI()
     */
    protected void initGUI() {
        super.initGUI();

        adjustRows();

        threeDTable = new Cab2bTable();

        tableSource = new BDQDataSource((IPartiallyInitialized3DRecord)record,new PageDimension(100,25),new MatrixCache(3));
        
        LazyTableModelInterface model = new BDQTableModel(tableSource);
        model.getValueAt(0,0);

        threeDTable.setModel(model);

        threeDTable.setColumnSelectionAllowed(true);

        threeDTable.setEditable(false);
        final JScrollPane tableSP = new JScrollPane(threeDTable);

        addRowHeader(tableSP);

        this.add("br hfill vfill", tableSP);
    }

    private void addRowHeader(JScrollPane tableSP) {
        Cab2bTable rowHeaderTable = new Cab2bTable(new AbstractTableModel() {

            public int getRowCount() {
                return tableSource.getCurrentPage().getData().getDim3Labels().length;
            }

            public int getColumnCount() {
                return 1;
            }

            @Override
            public String getColumnName(int arg0) {
                return "Sequence";
            }

            public Object getValueAt(int row, int column) {
                return tableSource.getCurrentPage().getData().getDim3Labels()[row];
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

}
