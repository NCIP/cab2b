package edu.wustl.cab2b.client.ui.viewresults;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import org.jdesktop.swingx.decorator.AlternateRowHighlighter;

import edu.wustl.cab2b.client.ui.controls.Cab2bTable;
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

    public static boolean isWholeColumnSelected = false;

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
        threeDTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        threeDTable.addMouseListener(new CellMouseListener());
        threeDTable.getTableHeader().addMouseListener(new HeaderMouseListener());

        tableSource = new BDQDataSource((IPartiallyInitialized3DRecord) record, new PageDimension(100, 25),
                new MatrixCache(3));

        LazyTableModelInterface model = new BDQTableModel(tableSource);
        model.getValueAt(0, 0);

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

    class HeaderMouseListener extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            int columnNumber = threeDTable.columnAtPoint(e.getPoint());
            threeDTable.setColumnSelectionAllowed(true);
            threeDTable.setRowSelectionAllowed(false);
            ListSelectionModel selection = threeDTable.getColumnModel().getSelectionModel();

            if (e.isShiftDown()) {
                int anchor = selection.getAnchorSelectionIndex();
                int lead = selection.getLeadSelectionIndex();

                if (anchor != -1) {
                    boolean old = selection.getValueIsAdjusting();
                    selection.setValueIsAdjusting(true);

                    boolean anchorSelected = selection.isSelectedIndex(anchor);

                    if (lead != -1) {
                        if (anchorSelected)
                            selection.removeSelectionInterval(anchor, lead);
                        else
                            selection.addSelectionInterval(anchor, lead);
                    }

                    if (anchorSelected)
                        selection.addSelectionInterval(anchor, columnNumber);
                    else
                        selection.removeSelectionInterval(anchor, columnNumber);

                    selection.setValueIsAdjusting(old);
                } else
                    selection.setSelectionInterval(columnNumber, columnNumber);
            } else if (e.isControlDown()) {
                if (selection.isSelectedIndex(columnNumber))
                    selection.removeSelectionInterval(columnNumber, columnNumber);
                else
                    selection.addSelectionInterval(columnNumber, columnNumber);
            } else {
                selection.setSelectionInterval(columnNumber, columnNumber);
                isWholeColumnSelected = true;

                //this is needed to select the cell and activate the focus listener 
                threeDTable.setRowSelectionInterval(1, 1);
            }
        }

    }

    class CellMouseListener extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            threeDTable.setCellSelectionEnabled(true);
            isWholeColumnSelected = false;
        }
    }

}
