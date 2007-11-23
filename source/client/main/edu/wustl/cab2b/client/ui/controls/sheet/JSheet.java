/*
 * JSheet.java
 *
 * Created on October 4, 2007, 2:00 PM
 */
package edu.wustl.cab2b.client.ui.controls.sheet;

import edu.wustl.cab2b.client.ui.controls.*;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.JDialog;
import javax.swing.event.*;
import javax.swing.table.*;

/**
 *
 * @author  jasbir_sachdeva
 */
public class JSheet extends javax.swing.JPanel {

    /** Event name that notifies that User has pressed Magnifying-Glass button...      */
    public static final String EVENT_MAGNIFYING_GLASS_CLICKED = "EVENT_MAGNIFYING_GLASS_CLICKED";
    /**
     *  Should I try to write on ReadOnly Data Model? */
    boolean allowWritesOnReadOnlyPart = false;
    /**     Should I allow user to create new columns and allow cut/paste on them?      */
    boolean allowWrite = true;
    /**  The actual Visual Component that renders table on the scren for the user.*/
    ViewDataPane pnlDataView = new ViewDataPane();
    /** The Visual components that accepts User settings: which Columns to view: */
    ColHideSettingsPane pnlColHide = new ColHideSettingsPane();
    /** the Visual component that shows Combo to ask user to select a Column, and
     * alllows user to select a Filter. The Filter bounds are set on Column Extended State.    */
    ColumnFilterPane pnlCommonRangeFilter = new ColumnFilterPane();
    /** Special Model to show a Button, and Row selection chk box on LHS.*/
    FixedLeftColsTblModel tblMdlFixedLeft;
    /***/
    JDialog colVisibilitySettingsDialog;
    /**     If this is true, SelectionHanger is allowed to paint itself as selected,
     * iff table cell selected is true as per model.  */
    private boolean selectionRowMode = false;
    private boolean mousePressed = false;
    /** If true, magnifying glass is shown. Pressing on it will fire "PropertiesNames.EVENT_MAGNIFYING_GLASS_CLICKED" event. */
    private boolean showMG;
    TableColumn colMG;

    /**
     * Creates new form JSheet
     */
    public JSheet() {
        initComponents();

        //  Set up Main Date Viewer and Column Manager...
        pnlDataViewTab.add(pnlDataView);
        pnlColumnsTab.add(pnlColHide);
        pnlDataView.addPropertyChangeListener(pnlColHide);
        pnlColHide.addPropertyChangeListener(pnlDataView);
        pnlDataView.addExportCellsActionlistener(new ExportCellsActionListener());

        //  Setup Filter UI Component...
        pnlCommonFilter.removeAll();
        pnlCommonFilter.add(pnlCommonRangeFilter);
        pnlDataView.addPropertyChangeListener(pnlCommonRangeFilter);
        pnlColHide.addPropertyChangeListener(pnlCommonRangeFilter);
        pnlCommonRangeFilter.setRelatedTable(pnlDataView.getDataViewTable());

        //  For column removal Button...
        TableChangesSynchronizer tcs = new TableChangesSynchronizer();
        pnlDataView.addTableListener(tcs);
        pnlDataView.addTableRowsorterListener(tcs);

        //  For Row Selections...
        tblFixed.setSelectionModel(pnlDataView.getSelectionModel());
        tblFixed.getSelectionModel().addListSelectionListener(tcs);
        DataViewCellsSelectionListener dvcsl = new DataViewCellsSelectionListener();
        pnlDataView.addTableColSelectionListener(dvcsl);
        pnlDataView.addTableRowSelectionListener(dvcsl);

        setBackgroundWhite(this);
    }

    void showColVisibilitySettingsDialog() {
        if (null == colVisibilitySettingsDialog) {
            colVisibilitySettingsDialog = new JDialog(getHWRoot(this),
                "Data View - Columns Visibility Settings");
            setBackgroundWhite(pnlColumnsTab);
            colVisibilitySettingsDialog.getContentPane().add(pnlColumnsTab);
            Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
            colVisibilitySettingsDialog.setBounds(ss.width * 2 / 8, ss.height / 7,
                ss.width * 4 / 8, ss.height * 5 / 7);
            colVisibilitySettingsDialog.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        }
        colVisibilitySettingsDialog.setVisible(true);
    }

    /** It is possible to hide the Magnifying Glass Column (the left most Column)., by passing false to this method.  */
    public void setMagnifyingGlassVisible(boolean showMG) {
        this.showMG = showMG;
        try {
            if (showMG) //  Chk if magnifying glass column is visible...
            {
                tblFixed.getColumn("MG");
            } else //  Hide Column...
            {
                tblFixed.removeColumn(colMG);
            }

        } catch (IllegalArgumentException ex) {
            //  column NOT found so add now...
            tblFixed.getColumnModel().addColumn(colMG);
            int presentIdx = tblFixed.getColumnModel().getColumnIndex("MG");
            tblFixed.getColumnModel().moveColumn(presentIdx, 0);
        }

    }

    public boolean isMagnifyingGlassVisible() {
        return showMG;
    }

    public void setConsoleVisible(boolean isConsoleVisible) {
        pnlConsole.setVisible(isConsoleVisible);
    }

    public boolean isConsoleVisible() {
        return pnlConsole.isVisible();
    }

    /**     Returns the top most compoennt in the containment hierarchy that is heavy weight.   */
    static Window getHWRoot(Component comp) {
        while (null != comp && !(comp instanceof Window)) {
            comp = comp.getParent();
        }

        //  Either comp is null, or it is some heavy weight component, as top most container...
        return (Window) comp;
    }

    /** Recursively apply WHITE background to all child components...    */
    void setBackgroundWhite(JComponent comp) {
        comp.setBackground(java.awt.Color.WHITE);
        java.awt.Component[] childComp = comp.getComponents();
        for (int idx = 0; idx < childComp.length; idx++) {
            if (childComp[idx] instanceof JComponent) {
                setBackgroundWhite((JComponent) childComp[idx]);
            }
        }
    }

    class TableChangesSynchronizer implements TableModelListener,
        RowSorterListener, ListSelectionListener {

        public void tableChanged(TableModelEvent e) {
        //            System.out.println("tableChanged =" + e);
        }

        public void sorterChanged(RowSorterEvent e) {
            //            System.out.println("sorterChanged: "+e+",  e.getPreviousRowCount()="+e.getPreviousRowCount());
//            System.out.println(""+ccc+"> e.getType(): "+e.getType());
            tblMdlFixedLeft.setRowCount(pnlDataView.getRowCount());
        }

        /** Selection is Row-Selection_hanger should match full row selection is data-view.     */
        public void valueChanged(ListSelectionEvent e) {
            if (selectionRowMode) {
                pnlDataView.extendSelectionsToAllColumns();
            }
        }
    }

    //
//      PUBLIC setters: configeration of ths is instance.
//
    /**
     *  Set the Model model that will be shown.
     * Default View is without filtering and Sorting.
     */

    public void setReadOnlytDataModel(TableModel fixedDataTableModel) {
        //  Forward the Model to the component that handles all the data...
        pnlDataView.setReadOnlyDataModel(fixedDataTableModel);
        setupRowSelectionWidgets(fixedDataTableModel);
    }

    /**     Notifyies listeners that Magnifing glass button has clicked.
     * Model Row index, wher click was detected is passed as New-Value.    */
    void fireMagnifyingGlassButtonClicked(int viewRowIndex, int viewColumnIndex) {
        int row = pnlDataView.convertRowIndexToModel(viewRowIndex);
//        System.out.println("MGGlass: Click detected aT Model IDX: row=" + row + ", viewRowIndex=" + viewRowIndex);
        firePropertyChange(EVENT_MAGNIFYING_GLASS_CLICKED, -1, row);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlActions = new javax.swing.JPanel();
        butExportTo = new javax.swing.JButton();
        pnlFiltersTab = new javax.swing.JPanel();
        tblFixed = new javax.swing.JTable();
        pnlFixedTblHeader = new javax.swing.JPanel();
        chkbFixedTblHeader = new javax.swing.JCheckBox();
        pnlFixedTblMGCellRenderer = new javax.swing.JPanel();
        pnlFixedTblMGCellEditor = new javax.swing.JPanel();
        pnlFixedTblHangerRenderer = new javax.swing.JPanel();
        tabSheetAspects = new javax.swing.JTabbedPane();
        pnlColumnsTab = new javax.swing.JPanel();
        pnlConsole = new javax.swing.JPanel();
        pnlColumnViews = new javax.swing.JPanel();
        lbLnShowAll = new javax.swing.JLabel();
        lbLnShowMutableOnly = new javax.swing.JLabel();
        lbLnShowROOnly = new javax.swing.JLabel();
        lbLnShowColSettDialog = new javax.swing.JLabel();
        pnlCommonFilter = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        pnlDataViewTab = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();

        pnlActions.setBorder(javax.swing.BorderFactory.createTitledBorder("Actions"));
        butExportTo.setText("Export Rows ...");
        butExportTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butExportToActionPerformed(evt);
            }
        });

        pnlActions.add(butExportTo);

        pnlFiltersTab.setLayout(new java.awt.BorderLayout());

        pnlFiltersTab.setName("All Filters");
        pnlFiltersTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlFiltersTabMouseClicked(evt);
            }
        });

        tblFixed.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblFixed.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblFixedMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tblFixedMouseEntered(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblFixedMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblFixedMouseReleased(evt);
            }
        });

        pnlFixedTblHeader.setLayout(new java.awt.GridBagLayout());

        chkbFixedTblHeader.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chkbFixedTblHeader.setMargin(new java.awt.Insets(0, 0, 0, 0));
        chkbFixedTblHeader.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkbFixedTblHeaderActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        pnlFixedTblHeader.add(chkbFixedTblHeader, gridBagConstraints);

        pnlFixedTblMGCellRenderer.setLayout(new java.awt.BorderLayout());

        pnlFixedTblMGCellRenderer.setPreferredSize(new java.awt.Dimension(20, 20));
        pnlFixedTblMGCellEditor.setLayout(new java.awt.BorderLayout());

        pnlFixedTblMGCellEditor.setToolTipText("Click Me to View This Row in More Details");
        pnlFixedTblMGCellEditor.setPreferredSize(new java.awt.Dimension(20, 20));
        tabSheetAspects.setName("View Data");
        pnlColumnsTab.setLayout(new java.awt.BorderLayout());

        pnlColumnsTab.setName("Hide Columns");
        pnlColumnsTab.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                pnlColumnsTabComponentShown(evt);
            }
        });

        tabSheetAspects.addTab("Set Columns Visibility", pnlColumnsTab);

        setLayout(new java.awt.BorderLayout());

        pnlConsole.setLayout(new java.awt.BorderLayout());

        pnlColumnViews.setLayout(new java.awt.GridBagLayout());

        pnlColumnViews.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 0, 51), 1, true), "Column Visibility"));
        lbLnShowAll.setText("<html><u>Show All");
        lbLnShowAll.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbLnShowAllMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lbLnShowAllMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lbLnShowAllMouseExited(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        pnlColumnViews.add(lbLnShowAll, gridBagConstraints);

        lbLnShowMutableOnly.setText("<html><u>Show Editable Only");
        lbLnShowMutableOnly.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbLnShowMutableOnlyMouseClicked(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 5, 0, 5);
        pnlColumnViews.add(lbLnShowMutableOnly, gridBagConstraints);

        lbLnShowROOnly.setText("<html><u>Show Originals");
        lbLnShowROOnly.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbLnShowROOnlyMouseClicked(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 5, 0, 5);
        pnlColumnViews.add(lbLnShowROOnly, gridBagConstraints);

        lbLnShowColSettDialog.setText("<html><u>Select Columns</u> ...");
        lbLnShowColSettDialog.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbLnShowColSettDialogMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lbLnShowColSettDialogMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lbLnShowColSettDialogMouseExited(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 5, 0, 5);
        pnlColumnViews.add(lbLnShowColSettDialog, gridBagConstraints);

        pnlConsole.add(pnlColumnViews, java.awt.BorderLayout.WEST);

        pnlCommonFilter.setLayout(new java.awt.BorderLayout());

        pnlConsole.add(pnlCommonFilter, java.awt.BorderLayout.CENTER);

        add(pnlConsole, java.awt.BorderLayout.NORTH);

        jPanel2.setLayout(new java.awt.BorderLayout());

        pnlDataViewTab.setLayout(new java.awt.BorderLayout());

        pnlDataViewTab.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(2, 0, 0, 0, new java.awt.Color(51, 0, 51)), "Data View"));
        pnlDataViewTab.setName("View Data");
        pnlDataViewTab.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                pnlDataViewTabComponentShown(evt);
            }
        });

        jPanel2.add(pnlDataViewTab, java.awt.BorderLayout.CENTER);

        add(jPanel2, java.awt.BorderLayout.CENTER);

        add(jPanel9, java.awt.BorderLayout.SOUTH);

    }// </editor-fold>//GEN-END:initComponents

    private void lbLnShowROOnlyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbLnShowROOnlyMouseClicked
// TODO add your handling code here:
        pnlColHide.setColumnsVisibilty(PropertiesNames.SHOW_READ_ONLY_COLUMNS_ONLY);
}//GEN-LAST:event_lbLnShowROOnlyMouseClicked

    private void lbLnShowMutableOnlyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbLnShowMutableOnlyMouseClicked
// TODO add your handling code here:
        pnlColHide.setColumnsVisibilty(PropertiesNames.SHOW_MUTABLE_COLUMNS_ONLY);
}//GEN-LAST:event_lbLnShowMutableOnlyMouseClicked

    private void lbLnShowAllMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbLnShowAllMouseClicked
// TODO add your handling code here:
        pnlColHide.setColumnsVisibilty(PropertiesNames.SHOW_ALL_COLUMNS);
}//GEN-LAST:event_lbLnShowAllMouseClicked

    private void lbLnShowAllMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbLnShowAllMouseExited
// TODO add your handling code here:
}//GEN-LAST:event_lbLnShowAllMouseExited

    private void lbLnShowAllMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbLnShowAllMouseEntered
// TODO add your handling code here:
}//GEN-LAST:event_lbLnShowAllMouseEntered

    private void chkbFixedTblHeaderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkbFixedTblHeaderActionPerformed
// TODO add your handling code here:
//        tblMdlFixedLeft.setAllMarked(chkbFixedTblHeader.isSelected());
        pnlDataView.extendSelectionsToAllColumns();
        tblFixed.getSelectionModel().setSelectionInterval(0, tblMdlFixedLeft.getRowCount());
        chkbFixedTblHeader.setSelected(true);

    }//GEN-LAST:event_chkbFixedTblHeaderActionPerformed

    private void pnlDataViewTabComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_pnlDataViewTabComponentShown
// TODO add your handling code here:
//        synDataViewWithColHideSettings();
        
    }//GEN-LAST:event_pnlDataViewTabComponentShown

    private void pnlColumnsTabComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_pnlColumnsTabComponentShown
// TODO add your handling code here:
//        setupColHideSettingsPane( evt);
    }//GEN-LAST:event_pnlColumnsTabComponentShown

    private void butExportToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butExportToActionPerformed
// TODO add your handling code here:
//        pnlDataView.printSelection();
        exportMarkedRows();
    }//GEN-LAST:event_butExportToActionPerformed

    private void tblFixedMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFixedMouseEntered
    // TODO add your handling code here:
        
    }//GEN-LAST:event_tblFixedMouseEntered

    private void pnlFiltersTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlFiltersTabMouseClicked
    // TODO add your handling code here:
//        System.out.println("Mouse CLick Detected At: "+tblFixed.getSelectedRow()+", "+tblFixed.getSelectedColumn());
    }//GEN-LAST:event_pnlFiltersTabMouseClicked

    private void tblFixedMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFixedMouseClicked
    // TODO add your handling code here:
//        System.out.println("FxfTbl: Mouse CLick Detected At: "+tblFixed.getSelectedRow()+", "+tblFixed.getSelectedColumn());
    }//GEN-LAST:event_tblFixedMouseClicked

    private void lbLnShowColSettDialogMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbLnShowColSettDialogMouseClicked
        // TODO add your handling code here:
        showColVisibilitySettingsDialog();
}//GEN-LAST:event_lbLnShowColSettDialogMouseClicked

    private void lbLnShowColSettDialogMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbLnShowColSettDialogMouseEntered
    // TODO add your handling code here:
}//GEN-LAST:event_lbLnShowColSettDialogMouseEntered

    private void lbLnShowColSettDialogMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbLnShowColSettDialogMouseExited
    // TODO add your handling code here:
}//GEN-LAST:event_lbLnShowColSettDialogMouseExited

    private void tblFixedMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFixedMousePressed
        // TODO add your handling code here:
        selectionRowMode = true;
        mousePressed = true;
        
    }//GEN-LAST:event_tblFixedMousePressed

    private void tblFixedMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFixedMouseReleased
        // TODO add your handling code here:
//        selectionRowMode = false;
        mousePressed = false;
    }//GEN-LAST:event_tblFixedMouseReleased
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton butExportTo;
    private javax.swing.JCheckBox chkbFixedTblHeader;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JLabel lbLnShowAll;
    private javax.swing.JLabel lbLnShowColSettDialog;
    private javax.swing.JLabel lbLnShowMutableOnly;
    private javax.swing.JLabel lbLnShowROOnly;
    private javax.swing.JPanel pnlActions;
    private javax.swing.JPanel pnlColumnViews;
    private javax.swing.JPanel pnlColumnsTab;
    private javax.swing.JPanel pnlCommonFilter;
    private javax.swing.JPanel pnlConsole;
    private javax.swing.JPanel pnlDataViewTab;
    private javax.swing.JPanel pnlFiltersTab;
    private javax.swing.JPanel pnlFixedTblHangerRenderer;
    private javax.swing.JPanel pnlFixedTblHeader;
    private javax.swing.JPanel pnlFixedTblMGCellEditor;
    private javax.swing.JPanel pnlFixedTblMGCellRenderer;
    private javax.swing.JTabbedPane tabSheetAspects;
    private javax.swing.JTable tblFixed;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] args) {
        SheetTestFrame.main(args);
    }

    private void setupRowSelectionWidgets(TableModel fixedDataTableModel) {
        if (null == fixedDataTableModel) //   we do not know, height of the table, so return...
        {
            return;
        }

        //  Define a new table model; this will act as fixed cols at left hand side of the Data View...
        tblMdlFixedLeft = new FixedLeftColsTblModel(fixedDataTableModel.getRowCount());
        tblFixed.setModel(tblMdlFixedLeft);
        tblFixed.getTableHeader().setReorderingAllowed(false);
        tblFixed.getTableHeader().setResizingAllowed(false);
        colMG = tblFixed.getColumnModel().getColumn(0);
        colMG.setCellRenderer(new FixedTblMagnifyingGlassRenderer());
        colMG.setCellEditor(new FixedTblMGCellEditor());
        colMG.setWidth(15);
        tblFixed.getColumnModel().getColumn(1).setCellRenderer(new FixedTblSelHangerRenderer());

        //  setup check box on the header of the table...
//        tblFixed.getColumnModel().getColumn( 1).setHeaderRenderer(
//                new FixedTblHeaderRenderer());

        pnlDataView.setLeftColumns(tblFixed, pnlFixedTblHeader);
    //        Insets i = new Insets( 0, 4, 0, 4);
//        butShowAll.set
    }

    /**     If DataView cells selections has changed, we need to remove selection from Row Selectors... */
    class DataViewCellsSelectionListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            if (!mousePressed) {
                selectionRowMode = false;
                tblFixed.repaint();
            }
        }
    }

    class RowSelectionListener implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {
            System.out.println("FxfTbl: ListSelectionEvent=" + e);
        }
    }

    class FixedTblSelHangerRenderer extends DefaultTableCellRenderer {

        SelectionHanger mg = new SelectionHanger();

        FixedTblSelHangerRenderer() {
            super();
            int rh = tblFixed.getRowHeight();
            //            int cw = tblFixed.get;
//            mg.setSize( getWidth(), rh);
            mg.setSize(14, rh - 1);
            setLayout(new BorderLayout());
            add(mg, BorderLayout.CENTER);
            //            pnlFixedTblHangerRenderer.setSize( 10, rh);
            revalidate();
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {

            mg.setSelected(isSelected && selectionRowMode);

            return super.getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, column);
        }
    }

    class FixedTblMagnifyingGlassRenderer implements TableCellRenderer {

        JComponent mg = new MagnifyingButton();

        FixedTblMagnifyingGlassRenderer() {
            int rh = tblFixed.getRowHeight();
            mg.setSize(15, rh);
            mg.setToolTipText("Click Me to View This Row in More Details");
            pnlFixedTblMGCellRenderer.add(mg);
            pnlFixedTblMGCellRenderer.setSize(15, rh);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {

            return pnlFixedTblMGCellRenderer;
        }
    }

    class FixedTblMGCellEditor extends AbstractCellEditor
        implements TableCellEditor, Runnable {

        MagnifyingButton mgHi = new MagnifyingButton("Hi");
        int row;
        int column;

        FixedTblMGCellEditor() {
            int rh = tblFixed.getRowHeight();
            mgHi.setSize(10, rh);
            mgHi.setToolTipText("Click Me to View This Row in More Details");
            pnlFixedTblMGCellEditor.add(mgHi);
            pnlFixedTblMGCellEditor.setSize(10, rh);

        }

        public Object getCellEditorValue() {
            return null;
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
            int row, int column) {
            //  save row & col, they are required when notifying listeners - to inform where click was recorded...
            this.row = row;
            this.column = column;
            //            System.out.println("Detected: getTableCellEditorComponent(): r="+row+", c="+column);
            //  notify listeners after this component is installed by table...
            EventQueue.invokeLater(this);
            return pnlFixedTblMGCellEditor;

        }

        public void run() {
            fireMagnifyingGlassButtonClicked(row, column);
        }
    }

    //    class FixedTblMagnifyingGlassRenderer implements TableCellRenderer{
//
//        JComponent mg = new MagnifyingButton();
//
//        FixedTblMagnifyingGlassRenderer()
//        {
//            pnlFixedTblCellRenderer.add( mg);
//        }
//
//        public Component getTableCellRendererComponent(
//                JTable table, Object value, boolean isSelected, boolean hasFocus,
//                int row, int column) {
//
//            return pnlFixedTblCellRenderer;
//        }
//    }

    class ExportCellsActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            exportSelectedCells();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  FILE Wrting: EXPORTING  by NOT Jassi...
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void exportSelectedCells() {
        exportToFile(true);
    }

    private void exportMarkedRows() {
        exportToFile(false);
    }

    /**
     * Method to perform export operation on data list It prompt user for
     * specifying file name and then saves selected rows into it in the from of
     * .csv
     */
    private void exportToFile(boolean cellSelectionEnabled) {

        boolean done = false;
        do {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.setFileFilter(new Cab2bFileFilter(new String[]{"csv"}));
            int status = fileChooser.showSaveDialog(this);
            if (JFileChooser.APPROVE_OPTION == status) {
                File selFile = fileChooser.getSelectedFile();
                String fileName = selFile.getAbsolutePath();

                if (true == selFile.exists()) {
                    // Prompt user to confirm if he wants to override the value
                    int confirmationValue = JOptionPane.showConfirmDialog(fileChooser, "The file " + selFile.getName() + " already exists.\nDo you want to replace existing file?",
                        "caB2B Confirmation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                    if (confirmationValue == JOptionPane.NO_OPTION) {
                        continue;
                    }
                } else {
                    if (false == fileName.endsWith(".csv")) {
                        fileName = fileName + ".csv";
                    }
                }
                BufferedWriter out = null;
                try {
                    out = new BufferedWriter(new FileWriter(fileName));

                    String csvString = cellSelectionEnabled ? pnlDataView.getTblSelectionDataWithCommas().toString() : pnlDataView.getTblRowDataWithComma(tblMdlFixedLeft.getRowSelections()).toString();
                    out.write(csvString);

                } catch (IOException e) {
                    handleException(e, this, true, true, true, false);
                } finally {
                    try {
                        out.close();
                    } catch (IOException e) {
                        handleException(e, this, true, true, true, false);
                    }
                }
            }
            done = true;
        } while (!done);

    }

    private void handleException(IOException e, JSheet jSheet, boolean b, boolean b0, boolean b1, boolean b2) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    class FixedLeftColsTblModel extends AbstractTableModel {

        int rowCount;
        int colCount = 2;
        int storageSize;
        ArrayList<Boolean> rowSelections;

        FixedLeftColsTblModel(int rowCount) {
            storageSize = rowCount;
            this.rowCount = rowCount;
        //            initModelStore();
        }

        void initModelStore() {
            rowSelections = new ArrayList<Boolean>(storageSize);
            for (int i = 0; i < rowCount; i++) {
                rowSelections.add(Boolean.FALSE);
            }
        }

        public int getRowCount() {
            return rowCount;
        }

        public ArrayList<Boolean> getRowSelections() {
            return rowSelections;
        }

        public void setRowCount(int newRowCount) {
            //            if (rowCount > rowSelections.size()) {
            if (newRowCount > storageSize) {
                throw new IllegalArgumentException("New Row Count cannot be larger the original " +
                    "count, when this instance was created. I.e. you can shrink row count, " +
                    "but cannot increase it beyound the internal storage. " +
                    "Internal storage is created only in constructor. ");
            }

            this.rowCount = newRowCount;
            fireTableDataChanged();
        }

        public int getColumnCount() {
            return colCount;
        }

        @Override
        public String getColumnName(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return "MG";
                case 1:
                    return "RowSelect";
            }
            return null;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return Object.class;
                case 1:
                    return Object.class;
            }
            return null;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return 0 == columnIndex;
        }

        public Object getValueAt(int viewRowIndex, int columnIndex) {
            return null;
        //            int modelRowIndex = pnlDataView.convertRowIndexToModel( viewRowIndex);
//            if( 1==columnIndex)
//                return rowSelections.get( modelRowIndex );
//            return null;
        }

        @Override
        public void setValueAt(Object aValue, int viewRowIndex, int columnIndex) {
            if (0 == columnIndex) {
                return;
            }

            if (true) //  At present this functionality is disabled...
            {
                return;
            }

            if (null == rowSelections) //  Lazy activation of underlying storage...
            {
                initModelStore();
            }

            int modelRowIndex = pnlDataView.convertRowIndexToModel(viewRowIndex);
            rowSelections.set(modelRowIndex, (Boolean) aValue);
        }

        /** Mark all values as either true or false.    */
        private void setAllMarked(boolean b) {
            if (true) //  At present this functionality is disabled...
            {
                return;
            }

            if (null == rowSelections) //  Lazy activation of underlying storage...
            {
                initModelStore();
            }

            for (int i = 0; i < rowSelections.size(); i++) {
                rowSelections.set(i, b);
            }

            //  Notify table if there are some visible rows...
            if (tblFixed.getRowCount() > 0) {
                fireTableRowsUpdated(0, tblFixed.getRowCount() - 1);
            }
        }
    }
}
