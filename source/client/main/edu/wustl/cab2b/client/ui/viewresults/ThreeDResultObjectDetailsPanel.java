package edu.wustl.cab2b.client.ui.viewresults;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JScrollPane;

import org.jdesktop.swingx.decorator.AlternateRowHighlighter;

import edu.wustl.cab2b.client.ui.controls.Cab2bTable;
import edu.wustl.cab2b.common.queryengine.result.I3DDataRecord;
import edu.wustl.cab2b.common.queryengine.result.IRecord;

/**
 * @author rahul_ner
 * 
 */
public class ThreeDResultObjectDetailsPanel extends DefaultDetailedPanel<I3DDataRecord> {

    private Cab2bTable threeDTable;

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

        threeDTable = new Cab2bTable(new BDQTableModel(record));
        threeDTable.setColumnSelectionAllowed(true);
        threeDTable.setEditable(false);
        JScrollPane tableSP = new JScrollPane(threeDTable);
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
        Cab2bTable rowHeaderTable = new Cab2bTable(false, dim2RowHeader, dim3RowHeaderHeader);

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

    /**
     * This method return the LazyParams containing the list of ranges that needs to 
     * be fetched for the bioDatacube. 
     * 
     * @param cube cube for which data 
     * @param startColumn
     * @param startRow
     * @return
     */
    //    private static ILazyParams getRanges(Object[][][] cube, int startColumn, int startRow) {
    //        int dim1Size = cube.length;
    //        int dim2Size = cube[0].length;
    //        int dim3Size = cube[0][0].length;
    //
    //        List<LazyParams.Range> ranges = new ArrayList<LazyParams.Range>();
    //
    //        int si = startColumn / dim2Size;
    //        int sj = startColumn % dim2Size;
    //        int sk = startRow;
    //
    //        int di = si;
    //
    //        int dj = dim2Size;
    //
    //        int dk = (startRow + NO_OF_ROWS) > dim3Size ? dim3Size : (startRow + NO_OF_ROWS);
    //
    //        int remaining = NO_OF_CLOUMNS;
    //
    //        while (remaining != 0 && si <= dim1Size) {
    //
    //            di = si;
    //            //            dj = ((remaining / dim2Size) == 0) ? (remaining % dim2Size) : dim2Size;
    //            dj = (remaining < dim2Size) ? remaining : dim2Size;
    //
    //            ranges.add(new LazyParams.Range(si, di, sj, dj, sk, dk));
    //
    //            remaining = remaining - (dj - sj);
    //            si++;
    //            sj = 0;
    //
    //        }
    //
    //        return new LazyParams(ranges);
    //    }
    //
    //    private static List<LazyParams.Range> getRanges1(Object[][][] cube, int startColumn, int startRow) {
    //
    //        //        int dimj = record.getDim2Labels().length;
    //        //        int dimk = record.getDim3Labels().length;
    //
    //        int dim1Size = cube.length;
    //        int dimj = cube[0].length;
    //        int dimk = cube[0][0].length;
    //
    //        List<LazyParams.Range> ranges = new ArrayList<LazyParams.Range>();
    //
    //        int dx = startRow + NO_OF_ROWS;
    //        int dy = startColumn + NO_OF_CLOUMNS;
    //
    //        int starti = startColumn / dimj;
    //        int endi = dy / dimj;
    //
    //        int startj = startColumn - (starti * dimj);
    //        int endj = dy - (endi * dimj);
    //
    //        int startk = startRow;
    //        int endk = dx > dimk ? dimk : dx;
    //
    //        boolean moreThanOnePage = (endi - starti) > 1;
    //        boolean moreThanTwoPages = (endi - starti) > 2;
    //        boolean endFullPage = (endj == dimj);
    //
    //        ranges.add(new LazyParams.Range(starti, starti + 1, startj, moreThanOnePage ? dimj : endj, startk, endk));
    //
    //        if (moreThanOnePage) {
    //            ranges.add(new LazyParams.Range(starti + 1, endFullPage ? endi : (endi - 1), 0,
    //                    moreThanTwoPages ? dimj : endj, startk, endk));
    //
    //            if (moreThanTwoPages && !endFullPage) {
    //                ranges.add(new LazyParams.Range(endi - 1, endi, 0, endj, startk, endk));
    //            }
    //        }
    //
    //        return ranges;
    //    }
    //
    //    public static void main(String[] args) {
    //
    //        Object[][][] cube = new Object[3][5][1000];
    //
    //        getRanges1(cube, 6, 100);
    //    }
}
