/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.controls.sheet;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class TableColumnWidthSynchronizer {

//    protected JViewport vpSPnlMainTable;
    JViewport vpTblColOwner;
    JViewport vpTblExtraOb;
    protected TableColumn tcolObservable;
    protected JTable tblColumnOwner;
    protected JTable tblExtraObservable;

    /**
    Specify the column and owner table, whose width should be dynamically managed.
    Note we need to have JTable.AUTO_RESIZE_OFF for the <code>columnOwnerTable</code>.
    @param observableTableColumn 
    The column, whose width needs to be adjusted to its visbile contents. 
    @param columnOwnerTable
    The Table to which this column is attached.
     */
    public void manageColumn(TableColumn observableTableColumn, JTable columnOwnerTable) {
        manageColumn(observableTableColumn, columnOwnerTable, null);
    }

    /**
    Specify the column and owner table, whose width should be dynamically managed.
    Note we need to have JTable.AUTO_RESIZE_OFF for the <code>columnOwnerTable</code>.
    @param observableTableColumn 
    The column, whose width needs to be adjusted to its visbile contents. 
    @param columnOwnerTable
    The Table to which this column is attached.
    @param extraObservableTable 
    If changes in some other table effects the contents of <code>tcolObservable</code>, 
    then please specify reference to that table here. Changes are: Contents changed, Filter Applied, Row Sorter changed, etc.
     */
    public void manageColumn(TableColumn observableTableColumn, JTable columnOwnerTable, JTable extraObservableTable) {

        tcolObservable = observableTableColumn;
        tblColumnOwner = columnOwnerTable;
        tblExtraObservable = extraObservableTable;

        try {
            vpTblColOwner = (JViewport) tblColumnOwner.getParent();
            if (null != tblExtraObservable) {
                vpTblExtraOb = (JViewport) tblExtraObservable.getParent();
            }
        } catch (Exception ex) {
            throw new IllegalArgumentException("Only those tables that are installed inside JViewport or JScrollPane are supported by this component.", ex);
        }

        //get viewport of tblTarget and add a listener to it.
        TableViewListener tvl = new TableViewListener();
        vpTblColOwner.addChangeListener(tvl);
        if (null != vpTblExtraOb) {
            vpTblExtraOb.addChangeListener(tvl);
        }

        //  Other changes that may change contents of the column...
        tblColumnOwner.getModel().addTableModelListener(tvl);
//        RowSorter rs = tblColumnOwner.getRowSorter();
//        if (null != rs) {
//            rs.addRowSorterListener(tvl);
//        }
//
//        //  Other changes on Optional Observable Table that may change contents of the column...
//        if (null != tblExtraObservable) {
//            tblExtraObservable.getModel().addTableModelListener(tvl);
//            rs = tblExtraObservable.getRowSorter();
//            if (null != rs) {
//                rs.addRowSorterListener(tvl);
//            }
//        }


    }

    protected class TableViewListener implements ChangeListener, TableModelListener {

        protected void updateColumnWidth() {
            int maxWidth = 0;
            int colNoModel = tcolObservable.getModelIndex();

            if (colNoModel < 0 || !tblColumnOwner.isVisible()) //  Desired Column NOT visible so return...
            {
//                System.out.println("Table/Column NOT prepared for dynamic width computations (1).");
                return;
            }

            //Get visible rectangle of tblTarget. it's "rect.y" is points to first visible row y position. 
            Rectangle rect = vpTblColOwner.getViewRect();
            if (rect.height <= 0) {
//                System.out.println("Table/Column NOT prepared for dynamic width computations (2).");
                return;
            }

            //  Compute max width for displayed rows...
            int colNoView = tblColumnOwner.convertColumnIndexToView(colNoModel);
            int yMax = rect.y + rect.height;
//            System.out.println("colNo=" + colNoView + ",  for rect:" + rect + ";   scanUpto=" + yMax+",  rowHeight="+tblColumnOwner.getRowHeight());
            for (int yLoc = rect.y; yLoc < yMax; yLoc += tblColumnOwner.getRowHeight()) {
                Point visibleRowPoint = new Point(0, yLoc);
                int row = tblColumnOwner.rowAtPoint(visibleRowPoint);
                if( row < 0){
                    //  Early termination...
                    break;
                }
                    
//                System.out.print(", " + row);
                TableCellRenderer tcr = tblColumnOwner.getCellRenderer(row, colNoModel);
                Object value = tblColumnOwner.getValueAt(row, colNoView);
                Component rendComp = tcr.getTableCellRendererComponent(tblColumnOwner, value, false, false, row, colNoView);
                int contentWidth = rendComp.getMaximumSize().width;
                maxWidth = (maxWidth < contentWidth) ? contentWidth : maxWidth;
            }
//            System.out.println("");

            //  Debug...
            if (null != tblExtraObservable) {
                JViewport vpX = (JViewport) tblExtraObservable.getParent();
//                System.out.println("Found maxWidth=" + maxWidth + ", for rect:" + rect + ";   ExtraTblView=" + vpX.getViewRect());
            }

            //  Apply Max Width...
            tcolObservable.setMinWidth(maxWidth + 1);
            tcolObservable.setPreferredWidth(maxWidth + 1);
            int totalWidth = tblColumnOwner.getColumnModel().getColumnMargin() * tblColumnOwner.getColumnCount() + tblColumnOwner.getColumnModel().getTotalColumnWidth();
            tblColumnOwner.setPreferredScrollableViewportSize(new Dimension(
                    totalWidth, 0));
            tblColumnOwner.invalidate();
            if (null != tblExtraObservable) {
                tblExtraObservable.invalidate();
            }
            vpTblColOwner.invalidate();
            if (null != vpTblExtraOb) {
                vpTblExtraOb.invalidate();
            }

            EventQueue.invokeLater(new Runnable() {

                public void run() {
                    vpTblColOwner.repaint();
                    if (null != vpTblExtraOb) {
                        vpTblExtraOb.repaint();
                    }
//                    //  Debug...
//                    if (null != tblExtraObservable) {
//                        JViewport vpX2 = (JViewport) tblExtraObservable.getParent();
//                        System.out.println("tblExtraObservable.getVisibleRect() = " + tblExtraObservable.getVisibleRect());
//                        System.out.println("Repainting with viewPortRect=" + vpTblColOwner.getViewRect() + ";   ExtraTblView=" + vpX2.getViewRect());
//                    }
                }
            });
        }

        public void stateChanged(ChangeEvent e) {
            updateColumnWidth();
        }

//        public void sorterChanged(RowSorterEvent e) {
//            updateColumnWidth();
//        }

        public void tableChanged(TableModelEvent e) {
            updateColumnWidth();
        }
    }

    class UnsupportedParentEcxeption extends RuntimeException {

        public UnsupportedParentEcxeption() {
            super();
        }

        public UnsupportedParentEcxeption(String msg) {
            super(msg);
        }
    }
}

