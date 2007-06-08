package edu.wustl.cab2b.client.ui.viewresults;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.jdesktop.swingx.decorator.AlternateRowHighlighter;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterPipeline;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.wustl.cab2b.client.ui.controls.Cab2bTable;
import edu.wustl.cab2b.common.datalist.IDataRow;
import edu.wustl.cab2b.common.queryengine.result.I3DDataRecord;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.common.querysuite.metadata.associations.IInterModelAssociation;

/**
 * @author rahul_ner
 *
 */
public class ThreeDResultObjectDetailsPanel extends DefaultDetailedPanel<I3DDataRecord> {

    private Vector<Vector> threeDTableData = new Vector<Vector>();

    private Vector<String> threeDTableHeader = new Vector<String>();

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
     */
    public ThreeDResultObjectDetailsPanel(IDataRow dataRow, I3DDataRecord record) {
        super(dataRow,record);
        isVFill = false;
    }

    /**
     * @see edu.wustl.cab2b.client.ui.viewresults.ResultObjectDetailsPanel#initData()
     */
    protected void initData() {
        super.initData();

        Object[][][] inputData = record.getCube();

        for (int i = 0; i < inputData.length; i++) {
            for (int j = 0; j < inputData[i].length; j++) {
                String columnHeader = record.getDim2Labels()[j] + "_" + record.getDim1Labels()[i];
                threeDTableHeader.add(columnHeader);
                for (int k = 0; k < inputData[i][j].length; k++) {
                    Object value = inputData[i][j][k];

                    if (threeDTableData.size() == k || threeDTableData.get(k) == null) {
                        threeDTableData.add(new Vector());
                    }

                    Vector row = threeDTableData.get(k);
                    row.add(value);
                }
            }
        }
    }

    Cab2bTable threeDTable;

    /**
     * @see edu.wustl.cab2b.client.ui.viewresults.ResultObjectDetailsPanel#initTableGUI()
     */
    protected void initGUI() {
        super.initGUI();

        adjustRows();

        threeDTable = new Cab2bTable(false, threeDTableData, threeDTableHeader);
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
}
