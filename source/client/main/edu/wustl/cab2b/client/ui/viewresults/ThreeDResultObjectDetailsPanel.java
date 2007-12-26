package edu.wustl.cab2b.client.ui.viewresults;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import org.jdesktop.swingx.decorator.AlternateRowHighlighter;

import edu.wustl.cab2b.client.ui.controls.Cab2bTable;
import edu.wustl.cab2b.client.ui.controls.LazyTable.LazyTableModelInterface;
import edu.wustl.cab2b.client.ui.controls.LazyTable.MatrixCache;
import edu.wustl.cab2b.client.ui.controls.LazyTable.PageDimension;
import edu.wustl.cab2b.client.ui.controls.sheet.JSheet;
import edu.wustl.cab2b.client.ui.util.CustomSwingWorker;
import edu.wustl.cab2b.common.queryengine.result.I3DDataRecord;
import edu.wustl.cab2b.common.queryengine.result.IPartiallyInitialized3DRecord;
import edu.wustl.cab2b.common.queryengine.result.IRecord;

/**
 * @author rahul_ner
 * 
 */
public class ThreeDResultObjectDetailsPanel extends DefaultDetailedPanel<I3DDataRecord> {

    private Cab2bTable threeDTable;

    private BDQDataSource tableSource;

    private boolean isWholeColumnSelected = false;

    private JScrollPane tableScrollPane;

    private int totalRowCount = 0;

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
        this.setName("DataListDetailedPanel");
    }

    /**
     * @see edu.wustl.cab2b.client.ui.viewresults.ResultObjectDetailsPanel#initTableGUI()
     */
    protected void initGUI() {

        CustomSwingWorker swingWorker = new CustomSwingWorker(this) {
            @Override
            protected void doNonUILogic() throws Exception {
                threeDTable = new Cab2bTable();
                tableSource = new BDQDataSource((IPartiallyInitialized3DRecord) record,
                        new PageDimension(100, 25), new MatrixCache(3));
                LazyTableModelInterface model = new BDQTableModel(tableSource);
                model.getValueAt(0, 0);
                threeDTable.setModel(model);
                threeDTable.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        firePropertyChange(DefaultSpreadSheetViewPanel.DISABLE_HEATMAP_LINK, -1, 0);
                        firePropertyChange(JSheet.EVENT_DATA_SINGLE_CLICKED, -1, 0);
                    }
                });
            }

            @Override
            protected void doUIUpdateLogic() throws Exception {
                ThreeDResultObjectDetailsPanel.super.initGUI();
                adjustRows();
                threeDTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                threeDTable.addMouseListener(new CellMouseListener());
                threeDTable.getTableHeader().addMouseListener(new HeaderMouseListener());
                threeDTable.setColumnSelectionAllowed(true);
                threeDTable.setEditable(false);
                tableScrollPane = new JScrollPane(threeDTable);
                addRowHeader();
                ThreeDResultObjectDetailsPanel.this.add("br hfill vfill", tableScrollPane);
            }
        };
        swingWorker.start();
    }

    public String getRowHeaderName(int rowNumber) {
        JViewport rowViewport = tableScrollPane.getRowHeader();
        Cab2bTable rowHeaderTable = (Cab2bTable) rowViewport.getComponent(0);
        Object value = rowHeaderTable.getValueAt(rowNumber, 0);

        String rowHeaderName = null;
        if (value != null) {
            rowHeaderName = value.toString() + "(" + rowNumber + ")";
        } else {
            rowHeaderName = "_(" + rowNumber + ")";
        }
        return rowHeaderName;
    }

    private void addRowHeader() {
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
                Object value = tableSource.getCurrentPage().getData().getDim3Labels()[row];
                return value;
            }
        });

        rowHeaderTable.setAutoscrolls(false);
        rowHeaderTable.setSize(new Dimension(0, 28));
        rowHeaderTable.setFont(new Font("Arial", Font.BOLD, 14));
        rowHeaderTable.setHighlighters();
        rowHeaderTable.setBackground(AlternateRowHighlighter.genericGrey.getForeground());
        tableScrollPane.setRowHeaderView(rowHeaderTable);
    }

    public boolean getIsWholeColumnSelected() {
        return isWholeColumnSelected;
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
                firePropertyChange(DefaultSpreadSheetViewPanel.ENABLE_CHART_LINK, -1, 0);
                firePropertyChange(DefaultSpreadSheetViewPanel.ENABLE_HEATMAP_LINK, -1, 0);
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

    @Override
    public String getCSVData() {
        StringBuffer sb = new StringBuffer();

        sb.append(super.getCSVData());
        sb.append("\n");

        sb.append(getCSVTableHeaderAndData(tableScrollPane));
        sb.append("\n");

        return sb.toString();
    }

    /**
     * Returns ThreeDResultTableModel
     * @return
     */
    public TableModel getThreeDResultTableModel() {
        return threeDTable.getModel();
    }

    public List<TreeSet<Comparable<?>>> getUniqueRecordValues() {
        return tableSource.getUniqueRecordValues();
    }

    public List<String> getSelectedColumnNames() {
        List<String> selectedColumnsList = new ArrayList<String>(threeDTable.getSelectedColumns().length);
        for (int i = 0; i < threeDTable.getSelectedColumns().length; i++) {
            selectedColumnsList.add(threeDTable.getColumnName(threeDTable.getSelectedColumns()[i]));
        }
        return selectedColumnsList;
    }

    public Object[][] getSelectedData() {
        Object data[][] = null;
        String selectedRowNames[] = null;
        if (isWholeColumnSelected) {
            BDQTableModel datCubeTableModel = (BDQTableModel) getThreeDResultTableModel();
            data = datCubeTableModel.getColumnValues(getSelectedColumns());

            //initilise this variable, which is used at the time of collecting row names 
            totalRowCount = data.length;
          } else {
            data = new Object[threeDTable.getSelectedRows().length][threeDTable.getSelectedColumns().length];
            for (int rowIndex = 0; rowIndex < threeDTable.getSelectedRows().length; rowIndex++)
                for (int columnIndex = 0; columnIndex < threeDTable.getSelectedColumns().length; columnIndex++) {
                    data[rowIndex][columnIndex] = threeDTable.getValueAt(
                                                                         threeDTable.getSelectedRows()[rowIndex],
                                                                         threeDTable.getSelectedColumns()[columnIndex]);
                }
        }
        return data;
    }

    public List<String> getSelectedRowNames() {

        List<String> selectedRowList = null;
        if (isWholeColumnSelected) {
            if (totalRowCount == 0) {
                BDQTableModel datCubeTableModel = (BDQTableModel) getThreeDResultTableModel();
                totalRowCount = datCubeTableModel.getColumnValues(getSelectedColumns()).length;
            }
            selectedRowList = new ArrayList<String>(totalRowCount);
            for (int i = 0; i < totalRowCount; i++) {
                selectedRowList.add(getRowHeaderName(i));
            }
        } else {
            selectedRowList = new ArrayList<String>(threeDTable.getSelectedRows().length);
            for (int i = 0; i < threeDTable.getSelectedRows().length; i++) {
                selectedRowList.add(getRowHeaderName(threeDTable.getSelectedRows()[i]));
            }
        }
        return selectedRowList;
    }

    public int[] getSelectedColumns() {
        return threeDTable.getSelectedColumns();
    }

}
