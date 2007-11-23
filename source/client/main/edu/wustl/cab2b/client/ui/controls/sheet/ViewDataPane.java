/*
 * ViewDataPane.java
 *
 * Created on October 4, 2007, 3:19 PM
 */

package edu.wustl.cab2b.client.ui.controls.sheet;



import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.*;
import java.lang.reflect.Constructor;
import java.text.DateFormat;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

/**
 *
 * @author  jasbir_sachdeva
 */
class ViewDataPane extends javax.swing.JPanel 
        implements PropertyChangeListener, PropertiesNames{
    
    /** The Tables data Model, that pickes data from both Read-Only and Extensible Data Models. */
    private CompositeTableModel compositeDataModel;
    
//    /**  */
//    private Map<TableColumn, ColumnExtraState> colExtraStateMap;
    
    /** Collection of Hidden and Visible Table COlumns.
     * Columns that are deleted are kept in removedTCols. */
    private Set<TableColumn> activeTableCols = new HashSet<TableColumn>( 200);
    
    /** References to Removed Column are kept here (in sets), for undo.*/
    private List<List<TableColumn>> removedTCols = new ArrayList<List<TableColumn>>();
    
    /** Each new Column is given name as: "New Col-1", "New Col-2", and so on. */
    private int columnAppendCount = 0;
    
    /***/
    public static final String EXT_COLUMNS_NAME_PREFIX = "N.Col-";
    
    /** To be used by Table while laying out Columns...*/
    int defaultColWidth = Toolkit.getDefaultToolkit().getScreenSize().width/20;
    
    /** Events name, fired when some button is pressed. */
    public static final String BUTTON_SELECT_ALL = "BUTTON_SELECT_ALL";
    public static final String BUTTON_SELECTION_RESET = "BUTTON_SELECTION_RESET";
    
    TableRowSorter tblRowSorter = new TableRowSorter();
    
    /**     Keeps references to Anyone who wishes to listens to column Selections. */
    ArrayList<ListSelectionListener> colSelectionListeners = new ArrayList<ListSelectionListener>();
    
    /***/
    ArrayList<TableModelListener> tblModelListener = new ArrayList<TableModelListener>();
    ArrayList<RowSorterListener> tblRowSorterListener = new ArrayList<RowSorterListener>();
    
    /**     This action listener is informed if user wants to export selection  .*/
    private ActionListener exportCellsActionListener;
    
    /**     The Fixed side of the Data View...  */
    private JTable tblFixed;
    
    private JComponent tblFixedHeader;
    
    
    //  Actions...
    RemoveAllSelectedExtColumnsAction actRemoveCols = new RemoveAllSelectedExtColumnsAction();
    UndeleteColumnsAction actUndoColDel = new UndeleteColumnsAction();
    PasteFromClipAction actPaste = new PasteFromClipAction();
    ExportSelectionAction actExportSelecion = new ExportSelectionAction();
    ClearSelectionAction actClearSelection = new ClearSelectionAction();
    
    
    
    /** Creates new form ViewDataPane */
    ViewDataPane() {
        initComponents();
        addTableColSelectionListener( new ColSelectionsListener());
        reset();
        setupAcions();
    }

    void extendSelectionsToAllColumns() {
        int endCol = tblData.getColumnModel().getColumnCount();
        tblData.getColumnModel().getSelectionModel().setSelectionInterval( 0, 
                endCol-1);
    }
    
    private void setupAcions() {
        butRemoveCol.setAction( actRemoveCols);
        miRemoveCols.setAction( actRemoveCols);
        
        butUndoColDelete.setAction( actUndoColDel);
        miUndoRemoveCol.setAction( actUndoColDel);
        
        butPaste.setAction( actPaste);
        miPaste.setAction( actPaste);
        
        actExportSelecion.addAssociatedTable( tblData);
        butExportSelection.setAction( actExportSelecion);
        miExportCells.setAction( actExportSelecion);
        
        butErazeContents.setAction( actClearSelection);
        miClearSelection.setAction( actClearSelection);
    }
    
    /** Reset to defaults, as if these are being used for first time.
     */
    private void reset() {
        activeTableCols.clear();
        
        if( null != removedTCols)
            removedTCols.clear();
        columnAppendCount = 0;
        
        //  Clear table with all columns..
        Enumeration<TableColumn> enTC = tblData.getColumnModel().getColumns();
        while( enTC.hasMoreElements() ) {
            TableColumn tcRemoved = enTC.nextElement();
            tblData.getColumnModel().removeColumn( tcRemoved);
        }
        //  Notify all mutables columns are removed...
        firePropertyChange( COLUMNS_REMOVED_ALL, false, true);
        
        //  Apply Selection Schema...
        tblData.getSelectionModel().clearSelection();
        tblData.getSelectionModel().setSelectionMode( 
                ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        ListSelectionModel lsm = new DefaultListSelectionModel();
        lsm.setSelectionMode( ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        
        //  set up listeners...
        for( ListSelectionListener colSL: colSelectionListeners)
            lsm.addListSelectionListener( colSL);
        tblData.getColumnModel().setSelectionModel( lsm);
        
        //  set default rederers: we do NOT want chk boxes for boolean type columns...
        tblData.setDefaultRenderer( Boolean.class, null);
        
        tblData.revalidate();
    }
    
    void setupExtraStateForReadOnlyColumns() {
        //  setup Column Extra information...
        Enumeration<TableColumn> tCols = tblData.getColumnModel().getColumns();
        while( tCols.hasMoreElements()) {
            ColumnExtraState ces = new ColumnExtraState();
            
            //  When this is executed, all the Columns are required to be visible...
            //  therefore, it will contain list of all the columns...
            ces.setColVisible( true);
            
            //  For each Read-Only Table Column, associate Extra State, and keep reference to it...
            TableColumn tc = tCols.nextElement();
            tc.setIdentifier( ces);
            ces.setSampleSortedValues( tblData.getModel(), tc.getModelIndex());
            ces.setColumnNickName( tc.getHeaderValue().toString());
            ces.setReadOnlyColumn( true);
            activeTableCols.add( tc);
        }
    }
    
//
//    Listeners...
//
    public void propertyChange(PropertyChangeEvent evt) {
        if( evt.getPropertyName().compareTo( PropertiesNames.COLUMN_VISIBLITY_CHANGING) ==0) {
            //  get reference of Column and Visibility Status...
            TableColumn tc = (TableColumn)evt.getNewValue();
            ColumnExtraState ces = (ColumnExtraState)tc.getIdentifier();
            
            //  Act as per visibility requirement...
            if( ces.isColVisible()) {
                //  make sure two instances are NOT added in table model...
                tblData.getColumnModel().removeColumn( tc);
                tblData.getColumnModel().addColumn( tc);
            } else
                tblData.getColumnModel().removeColumn( tc);
        }
    }
    
    
//
//    Getters & Setters...
//
    public void setReadOnlyDataModel( TableModel fixedDataTableModel) {
        //  Chk Non-null Data Model
        if( null == fixedDataTableModel)
            throw new IllegalArgumentException( getClass().getName()+".setPresetDataModel(...) does NOT excepts null Data Model.");
        
        
        //setup table column width preferences...
        setupCompositeDataModel( fixedDataTableModel);
        
        //  Set up lister for the sorter...
        for(RowSorterListener rsl: tblRowSorterListener)
            tblData.getRowSorter().removeRowSorterListener( rsl);
        for(RowSorterListener rsl: tblRowSorterListener)
            tblData.getRowSorter().addRowSorterListener( rsl);
    }
    
    public TableModel getReadOnlyDataModel() {
        return compositeDataModel.getPrimaryModel();
    }
    
//    public TableColumnModel getTableColumnModel() {
//        return tblData.getColumnModel();
//    }
    
    /** @returns refrence to Jtable that is used to show contents.  */
    JTable getDataViewTable() {
        return tblData;
    }
    
    ListSelectionModel getSelectionModel(){
        return tblData.getSelectionModel();
    }
    
    
    /**
     *  Setups the Fixed columns to shown on the left hand side of the Data Table.
     *  Note they do not scroll with the selection...
     */
    public void setLeftColumns( JTable tblFixed, JComponent tblFixedHeader) {
        this.tblFixed = tblFixed;
        this.tblFixedHeader = tblFixedHeader;
    }
    
    
    /**
     *  Assume a Column is Extended one, if its name starts with our namig convention,
     *  i.e. with EXT_COLUMNS_NAME_PREFIX.
     */
    boolean isExtendedColumn( TableColumn tc) {
        return ( tc.getIdentifier() instanceof String &&
                ((String)tc.getIdentifier()).startsWith(EXT_COLUMNS_NAME_PREFIX));
    }
    
    /**     @return TRUE, if all the column in selection are extendable, i.e. editable and removable.
     */
    boolean isOnlyExtColumnInSelection() {
        int sCols[] = tblData.getSelectedColumns();
        if( sCols.length <=0 )
            return false;
        
        for( int viewColIdx: sCols) {
            int modelColIdx = tblData.convertColumnIndexToModel( viewColIdx);
            if( modelColIdx < compositeDataModel.roDataModel.getColumnCount())
                //  At least one column, from Read-Only model is selected...
                return false;
        }
        //  no-read-only column found...
        return true;
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        butRemoveExtendedCols = new javax.swing.JButton();
        popTblContextMnu = new javax.swing.JPopupMenu();
        miAddCol = new javax.swing.JMenuItem();
        miRemoveCols = new javax.swing.JMenuItem();
        miUndoRemoveCol = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        miCut = new javax.swing.JMenuItem();
        miCopy = new javax.swing.JMenuItem();
        miPaste = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        miClearSelection = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        miExportCells = new javax.swing.JMenuItem();
        miExportRows = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        spaData = new javax.swing.JScrollPane();
        tblData = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        butSelectAll = new javax.swing.JButton();
        butCopy = new javax.swing.JButton();
        butPaste = new javax.swing.JButton();
        butExportSelection = new javax.swing.JButton();
        butErazeContents = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        addCol = new javax.swing.JButton();
        butRemoveCol = new javax.swing.JButton();
        butUndoColDelete = new javax.swing.JButton();

        butRemoveExtendedCols.setText("Remove All");
        butRemoveExtendedCols.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butRemoveExtendedColsActionPerformed(evt);
            }
        });

        miAddCol.setText("Add Column");
        miAddCol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miAddColActionPerformed(evt);
            }
        });
        popTblContextMnu.add(miAddCol);

        miRemoveCols.setText("Remove Columns");
        miRemoveCols.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miRemoveColsActionPerformed(evt);
            }
        });
        popTblContextMnu.add(miRemoveCols);

        miUndoRemoveCol.setText("Undo Remove Column");
        popTblContextMnu.add(miUndoRemoveCol);
        popTblContextMnu.add(jSeparator1);

        miCut.setText("Cut");
        popTblContextMnu.add(miCut);

        miCopy.setText("Copy");
        miCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miCopyActionPerformed(evt);
            }
        });
        popTblContextMnu.add(miCopy);

        miPaste.setText("Paste");
        popTblContextMnu.add(miPaste);
        popTblContextMnu.add(jSeparator3);

        miClearSelection.setText("Item");
        popTblContextMnu.add(miClearSelection);
        popTblContextMnu.add(jSeparator2);

        miExportCells.setText("Export Selection");
        miExportCells.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miExportCellsActionPerformed(evt);
            }
        });
        popTblContextMnu.add(miExportCells);

        miExportRows.setText("Export Rows ...");

        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        spaData.setAutoscrolls(true);
        spaData.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                spaDataAncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        tblData.setAutoCreateRowSorter(true);
        tblData.setComponentPopupMenu(popTblContextMnu);
        tblData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblData.setAutoCreateColumnsFromModel(false);
        tblData.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblData.setCellSelectionEnabled(true);
        spaData.setViewportView(tblData);

        jPanel1.add(spaData, java.awt.BorderLayout.CENTER);

        add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 0, 51), 1, true), "Selection", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 16)));

        butSelectAll.setText("Select All");
        butSelectAll.setVisible( false);
        butSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butSelectAllActionPerformed(evt);
            }
        });
        jPanel4.add(butSelectAll);

        butCopy.setText("Copy");
        butCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butCopyActionPerformed(evt);
            }
        });
        jPanel4.add(butCopy);

        butPaste.setText("Paste");
        jPanel4.add(butPaste);

        butExportSelection.setText("Export");
        jPanel4.add(butExportSelection);

        butErazeContents.setText("Clear Selection");
        butErazeContents.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butErazeContentsActionPerformed(evt);
            }
        });
        jPanel4.add(butErazeContents);

        jPanel3.add(jPanel4, new java.awt.GridBagConstraints());

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 0, 51), 1, true), " For New Columns Only ", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 16)));

        addCol.setText("Add Col");
        addCol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addColActionPerformed(evt);
            }
        });
        jPanel2.add(addCol);

        butRemoveCol.setText("Remove Col");
        butRemoveCol.setEnabled(false);
        jPanel2.add(butRemoveCol);

        butUndoColDelete.setText("Undo Remove-Col");
        butUndoColDelete.setEnabled(false);
        jPanel2.add(butUndoColDelete);

        jPanel3.add(jPanel2, new java.awt.GridBagConstraints());

        add(jPanel3, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents
    
    private void miCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miCopyActionPerformed
// TODO add your handling code here:
        copySelectionIntoClipboard();
    }//GEN-LAST:event_miCopyActionPerformed
    
    private void miAddColActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miAddColActionPerformed
// TODO add your handling code here:
        addTableCol();
    }//GEN-LAST:event_miAddColActionPerformed
    
    private void miRemoveColsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miRemoveColsActionPerformed
// TODO add your handling code here:
    }//GEN-LAST:event_miRemoveColsActionPerformed
    
    private void miExportCellsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miExportCellsActionPerformed
// TODO add your handling code here:
    }//GEN-LAST:event_miExportCellsActionPerformed
    
    private void addColActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addColActionPerformed
// TODO add your handling code here:
        addTableCol();
    }//GEN-LAST:event_addColActionPerformed
    
    private void butCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butCopyActionPerformed
// TODO add your handling code here:
        copySelectionIntoClipboard();
    }//GEN-LAST:event_butCopyActionPerformed
    
    private void butSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butSelectAllActionPerformed
// TODO add your handling code here:
        
    }//GEN-LAST:event_butSelectAllActionPerformed
    
    private void spaDataAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_spaDataAncestorAdded
// TODO add your handling code here:
        setupFixedCols();
    }//GEN-LAST:event_spaDataAncestorAdded
    
    private void butRemoveExtendedColsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butRemoveExtendedColsActionPerformed
// TODO add your handling code here:
        removeAllExtendedColumns();
    }//GEN-LAST:event_butRemoveExtendedColsActionPerformed
    
    private void butErazeContentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butErazeContentsActionPerformed
// TODO add your handling code here:
        eraseAllExtendedCols();
    }//GEN-LAST:event_butErazeContentsActionPerformed
    
//
//  BEHAVIOUR
//
    /**
     *  Setups Table Model for the data-Table, from scratch.
     */
    void setupCompositeDataModel( TableModel fixedDataTableModel) {
        compositeDataModel = new CompositeTableModel( fixedDataTableModel);
        
        reset();
        
        //  Attach model to table, along with old listeners...
        tblData.setModel( compositeDataModel);
        tblData.createDefaultColumnsFromModel();
        for( TableModelListener tml: tblModelListener)
            tblData.getModel().addTableModelListener( tml);
        
        //  reset Default Table Column Width...
        for( int i=0; i<tblData.getColumnCount(); i++) {
//            tblData.getColumnModel().getColumn( i).setPreferredWidth( defaultColWidth);
            tblData.getColumnModel().getColumn( i).setMinWidth( defaultColWidth);
        }
        
        setupExtraStateForReadOnlyColumns();
        
        //  Now all active columns are ready to be announced...
        Enumeration<TableColumn> tCols = tblData.getColumnModel().getColumns();
        while( tCols.hasMoreElements())
            firePropertyChange( COLUMN_READ_ONLY_ADDED, null, tCols.nextElement());
    }
    
    
    public void addTableRowSelectionListener( ListSelectionListener l) {
        tblData.getSelectionModel().addListSelectionListener( l);
    }
    
    public void addTableColSelectionListener( ListSelectionListener l) {
        colSelectionListeners.add( l);
    }
    
    public void addTableListener( TableModelListener tml){
//        tblModelListener .add( tml);
        tblData.getModel().addTableModelListener( tml);
    }
    
    public void addTableRowsorterListener( RowSorterListener rsl){
        tblRowSorterListener.add( rsl);
    }
    
    
    
    /***
     *  Adds a new Column in the Table, on extream right.
     */
    void addTableCol() {
        TableColumnModel tcm = tblData.getColumnModel();
        TableColumn tc = compositeDataModel.createNewColumn();
        ColumnExtraState ces = new ColumnExtraState();
        
        //  Keep reference of newly created Column, and its newly created extended state...
        ces.setColumnNickName( tc.getHeaderValue().toString());
        tc.setIdentifier( ces);
        activeTableCols.add( tc);
        tcm.addColumn( tc);
        
//        Rectangle viewTbl = spaData.getViewport().getViewRect();
////        System.out.println("viewTbl ="+viewTbl);
////        Rectangle cellBounds = new Rectangle( tblData.getSize().width-1, extTbl.height, 1, 0);
//        Rectangle cellBounds = tblData.getCellRect( 0, tblData.getColumnCount()-1, false);
////        System.out.println("cellBounds ="+cellBounds);
////        System.out.println("tblData.getSize() ="+tblData.getSize());
//        int newWidth = tcm.getTotalColumnWidth() +
//                tcm.getColumnMargin() * (tcm.getColumnCount()-1);
//        tblData.setSize( newWidth, tblData.getSize().height);
////        System.out.println("Table New Size ="+tblData.getSize()+",  after Width: "+newWidth);
//        tblData.scrollRectToVisible( cellBounds);
//        tblData.revalidate();
        ces.setSampleSortedValues( new ArrayList());
        
        
//        System.out.println("tblData.PreferredScrollableViewportSize ="+tblData.getPreferredScrollableViewportSize());
//        System.out.println("tblData.PreferredSize ="+tblData.getPreferredSize());
//        System.out.println("tblData.getMinimumSize() ="+tblData.getMinimumSize());
//        System.out.println("tblData.getMaximumSize() ="+tblData.getMaximumSize());
        
        //  First announce to the listeners, to prepare themselves...
        firePropertyChange( COLUMN_MUTABLE_ADDING, null, tc);
        
        //  Now add it, note suddenly after this filter will be activated...
        compositeDataModel.addNewColumn( tc);
        firePropertyChange( COLUMN_MUTABLE_ADDED, null, tc);
    }
    
    
    /** Remove Specified Extended (Modifiable) Columns.
     *  All Column Numbers/Indices are in View Space.
     */
    void removeExtColumns( int colIdx[]) {
        int removedColCount = 0;
        Arrays.sort( colIdx);
        
        ArrayList<TableColumn> removedTCSet = new ArrayList<TableColumn>();
        
        //  Remove these Columns from Visibility by removing references from the colExtraStateMap...
        for( int idx=colIdx.length-1; idx>=0; idx--) {
            int viewColIdx = colIdx[idx];
            int modelColIdx = tblData.convertColumnIndexToModel( viewColIdx);
            int extColModelIdx = modelColIdx - compositeDataModel.roDataModel.getColumnCount();
            if( extColModelIdx >= 0) {
                //  this is from extendable model, so it safe to remove...
                //  Now remove from View, and also in Visibility records...
                removedColCount++;
                TableColumn colToRemove = tblData.getColumnModel().getColumn( viewColIdx);
                tblData.getColumnModel().removeColumn( colToRemove);
                activeTableCols.remove( colToRemove);
                
                //  for undo...
                removedTCSet.add( colToRemove);
                firePropertyChange( COLUMN_MUTABLE_REMOVED, null, colToRemove);
            }
        }
        
        //  Keep Reference, and update GUI...
        removedTCols.add( removedTCSet);
        actUndoColDel.setEnabled( removedTCols.size() > 0);
        compositeDataModel.fireTableStructureChanged();
    }
    
    
    
    /**
     *  Revoke the Last Known Deleted Columns...
     */
    void undoLastcolDelete() {
        //  get Last Removed Set...
        List<TableColumn> colsToRevoke = removedTCols.get( removedTCols.size()-1);
        removedTCols.remove( colsToRevoke);
        
        //  Restore...
        for( TableColumn revokableCol: colsToRevoke) {
            tblData.getColumnModel().addColumn( revokableCol);
            activeTableCols.add( revokableCol);
            firePropertyChange( COLUMN_MUTABLE_ADDING, null, revokableCol);
            firePropertyChange( COLUMN_MUTABLE_ADDED, null, revokableCol);
        }
        
        //  reCheck visibility...
        actUndoColDel.setEnabled( removedTCols.size() > 0);
    }
    
    
    /** Clears al data in Extended Data Model...*/
    private void eraseAllExtendedCols() {
//        compositeDataModel.clearExtendedDataModel();
    }
    
    public void removeAllExtendedColumns() {
        //  Confirm User:
        String message = "Remove All Data New Data from this Sheet. " +
                "Columns NOT added by you will remain intact. \n\n" +
                "CAUTION: You CANNOT UnDo This operation.\nDo you still want to continue?\n";
        String header = "Warning: Remove All - No UnDo";
        int reponse = JOptionPane.showConfirmDialog( this, message, header,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if( 0 == reponse)
            setupCompositeDataModel( compositeDataModel.roDataModel);
        
        actUndoColDel.setEnabled( removedTCols.size() > 0);
    }
    
    private void setupFixedCols() {
        //  Setup dispplay width, of each column...
        int totalWidth = 0;
        for( int colIdx=0; colIdx<tblFixed.getColumnCount(); colIdx++) {
            //  Setup with of the fixed left columns...
            int width = tblFixed.getCellRenderer( 0, colIdx).getTableCellRendererComponent(
                    tblFixed, null, false, false, 0, colIdx).getMinimumSize().width;
            tblFixed.getColumnModel().getColumn( colIdx).setMaxWidth( width+10);
            
            if( (tblFixed.getColumnCount()-1) == colIdx )
                spaData.setRowHeaderView( tblFixed);
        }
        
        totalWidth = tblFixed.getColumnModel().getColumnMargin() * tblFixed.
                getColumnCount() + tblFixed.getColumnModel().getTotalColumnWidth();
        tblFixed.setPreferredScrollableViewportSize( new Dimension( totalWidth, 0));
        spaData.setCorner( ScrollPaneConstants.UPPER_LEFT_CORNER, tblFixedHeader);
//        spaData.setCorner( ScrollPaneConstants.UPPER_LEFT_CORNER, tblFixed.getTableHeader());
    }
    
    
    /**
     *  Author: Unknown  (But NOT Jassi)
     */
    public void pasteFromClipboard_old() {
        Clipboard clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
        
        try {
            String str = (String) clipBoard.getData(DataFlavor.stringFlavor);
            if (str == null && str.length() == 0)
                return;
            int col = tblData.getSelectedColumn();
            int row = tblData.getSelectedRow();
            
            if (col == -1 || row == -1)
                return;
            String[] values = str.split("\n");
            if (values == null || values.length == 0)
                return;
            String firstRows[] = values[0].split("\t");
            boolean isValidData = true;
            int modelIndex = tblData.convertColumnIndexToModel(col);
            for (int j = 0; j < firstRows.length; j++) {
                
                Object oldValue = tblData.getValueAt(row, j);
                
                try {
                    
                    Class columnClass = tblData.getColumnClass(modelIndex);
                    if (columnClass.getSimpleName().equalsIgnoreCase("date")) {
                        DateFormat formater = DateFormat.getDateInstance(DateFormat.MEDIUM);
                        formater.parseObject(firstRows[j]);
                    } else {
                        Constructor constructor = columnClass.getConstructor(new Class[] { String.class });
                        Object[] parameters = new Object[] { firstRows[j] };
                        Object obj = constructor.newInstance(parameters);
                    }
                    
                } catch (Exception e) {
                    System.out.println("Exception catched ....." + e);
                    // e.printStackTrace();
                    isValidData = false;
                    // table.setValueAt(oldValue,0,j);
                    break;
                }
                // model.setValueAt(oldValue,row,table.convertColumnIndexToModel(col+j));
            }
            
            if (!isValidData) {
                JOptionPane.showMessageDialog( tblData, "Invalid data format for the specified column",
                        "Copy Column Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            for (int i = 0; i < values.length; i++) {
                String row_str = values[i];
                String column[] = row_str.split("\t");
                
                for (int j = 0; j < column.length; j++) {
                    
                    if ((row + i) < tblData.getRowCount() && (col + j) < tblData.getColumnCount())
                        compositeDataModel.setValueAt(column[j], row + i, tblData.convertColumnIndexToModel(col + j));
                    else
                        continue;
                }
                
            }
            
        } catch (Exception e) {
            System.out.println("In outer catch...");
            e.printStackTrace();
            
        }
        
    }
    
    
    public void pasteFromClipboard(){
        Clipboard clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
        
        String clipData;
        try{
            clipData = (String) clipBoard.getData(DataFlavor.stringFlavor);
        }catch( Exception ex) {
            ex.printStackTrace();
            return;
        }
        
        if (null == clipData || clipData.length() == 0)
            // nothing to use...
            return;
        
        int targetCol = tblData.getSelectedColumn();
        int targetRow = tblData.getSelectedRow();
        
        if (targetRow == -1 || targetCol == -1)
            // we do NOT know where to paste...
            return;
        
        String[] rowsData = clipData.split("\n");
        if (rowsData == null || rowsData.length == 0)
            //  each row data should be terminated by <EOLN>
            return;
        
        //  Import each Row...
        for( int vRow = 0; vRow < rowsData.length; vRow++) {
            if( vRow+targetRow >= tblData.getRowCount())
                //  ignore rest of the cells, as we have nowhere to paste these cells, as they pass table boundary...
                break;
            
            //  Import all the cells, separated by tab...
            String[] cellsVal = rowsData[ vRow].split("\t");
            for( int vCol = 0; vCol < cellsVal.length; vCol++) {
                if( vCol+targetCol >= tblData.getColumnCount())
                    //  ignore rest of the cells, as we have nowhere to paste these cells, as they pass table boundary...
                    break;
                int mRow = tblData.convertRowIndexToModel( vRow+targetRow);
                int mCol = tblData.convertColumnIndexToModel( vCol+targetCol);
                compositeDataModel.setValueAt( cellsVal[ vCol], mRow, mCol);
            }
        }
    }
    
    
    /**     Clears Selected cells in the table.     */
    void doClearSelection() {
        doClearSelection( tblData.getSelectedRows(), tblData.getSelectedColumns());
    }
    
    /**     Clear contents of Cells, whose location is provided in parameters   */
    void doClearSelection( int[] rowsIdx, int[] colsIdx) {
        for (int rIdx = 0; rIdx < rowsIdx.length; rIdx++) {
            for (int cIdx = 0; cIdx < colsIdx.length; cIdx++) {
                tblData.setValueAt( "", rowsIdx[ rIdx], colsIdx[ cIdx]);
            }
        }
    }
    
    
    /** Selected Contents of table are copied into Clipboard. */
    public void copySelectionIntoClipboard() {
        StringBuffer selText = getTblSelectionDataWithTabs();
        if (null==selText || selText.length() == 0)
            //  Nothing to Select..
            return;
        
        StringSelection sSel = new StringSelection( selText.toString());
        Clipboard clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipBoard.setContents(sSel, sSel);
        
    }
    
    
    /**     ALL Selected rows and columns data would be picked and returned.
     * Columns will be separated with tabs.    */
    public StringBuffer getTblSelectionDataWithTabs() {
        return getTblSelectionData( false, false);
    }
    
    /**     ALL Selected rows and columns data would be picked and returned.
     * Columns will be separated with comma.    */
    public StringBuffer getTblSelectionDataWithCommas() {
        return getTblSelectionData( true, true);
    }
    
    /**     ALL columns for rows "selRows" would be picked, irrespective of the visiblility.
     *              Rows are in table viewcoordinated. */
    public StringBuffer getTblRowDataWithTabs( int[] selRows) {
        return getTblRowData( selRows, false, true);
    }
    
    /**     Returned data from those cells that are part of rows which are both Marked and Visible.
     * Data from only will be returned. ALL columns for rows in ''rowMarkedInModel" would be picked, subjected
     * to the condition that these are also visible.
     * rowMarkedInModel are in Table's Model coordinated.
     */
    public StringBuffer getTblRowDataWithComma( ArrayList<Boolean> rowMarkedInModel) {
        ArrayList<Integer> selIRows = new ArrayList<Integer>();
        
        //  Scan overall visible rows, and pick rows that are marked for export...
        for( int viewRowIdx=0; viewRowIdx<tblData.getRowCount(); viewRowIdx++) {
            int modelRowIndex = tblData.convertRowIndexToModel( viewRowIdx);
            if( rowMarkedInModel.get( modelRowIndex))
                // this row is marked for export...
                selIRows.add( viewRowIdx);
        }
        
        //  Convert to array...
        int[] selRows = new int[ selIRows.size()];
        for( int idx=0; idx<selIRows.size(); idx++)
            selRows[ idx] = selIRows.get( idx);
        
        //  These are the actual rows (both Marked and Visible - data from only these should be returned...
        return getTblRowData( selRows, true, true);
    }
    
    /**     ALL columns for rows "selRows" would be picked, irrespective of the visiblility.
     *              Rows are in table viewcoordinated.
     */
    public StringBuffer getTblRowData( int[] selRows, boolean forceEscapeComma, boolean isCommaSepatrated) {
        
        //  Make a selection that selects all the columns...
        int colCount = tblData.getColumnCount();
        int[] colsIdx = new int[ colCount];
        for( int i=0; i<colCount; i++)
            colsIdx[ i] = i;
        return getTblSelectionData( selRows, colsIdx, forceEscapeComma, isCommaSepatrated);
    }
    
    /**     ALL Selected rows and columns data would be picked and returned.
     * forceEscapeComma: true => Encapsulate all cell values within double quotes.
     * false => Encapsulate those cell values within double quotes, that contains comma
     *
     * isCommaSepatrated: true => Separate all cell values with Comma
     * false => Separate all cell values with Tabs         */
    public StringBuffer getTblSelectionData( boolean forceEscapeComma, boolean isCommaSepatrated) {
        StringBuffer sBuff = new StringBuffer();
        int[] rowsIdx = tblData.getSelectedRows();
        int[] colsIdx = tblData.getSelectedColumns();
        return getTblSelectionData( rowsIdx, colsIdx, forceEscapeComma, isCommaSepatrated);
    }
    
    /**     ALL specified rows and columns data would be picked and returned.
     * rows and columns are specified with rowsIdx[] and colsIdx[] arrays.
     * Both values are in Table view coordinates.
     *
     * forceEscapeComma: true => Encapsulate all cell values within double quotes.
     * false => Encapsulate those cell values within double quotes, that contains comma
     *
     * isCommaSepatrated: true => Separate all cell values with Comma
     * false => Separate all cell values with Tabs         */
    public StringBuffer getTblSelectionData( int[] rowsIdx, int[] colsIdx, boolean forceEscapeComma, boolean isCommaSepatrated) {
        StringBuffer sBuff = new StringBuffer();
        
        if (rowsIdx.length > 0) {
            for (int rIdx = 0; rIdx < rowsIdx.length; rIdx++) {
                for (int cIdx = 0; cIdx < colsIdx.length; cIdx++) {
                    Object cellValue = tblData.getValueAt( rowsIdx[ rIdx], colsIdx[ cIdx]);
                    
                    if ( cIdx >0 && cIdx <colsIdx.length)
                        if( isCommaSepatrated)
                            sBuff.append(',');
                        else
                            sBuff.append("\t");
                    
                    if (null == cellValue) {
                        sBuff.append("");
                    } else {
                        // If special character in the column name put it into double quotes
                        String strVal = cellValue.toString();
                        sBuff.append( forceEscapeComma || (strVal.indexOf( ',') >= 0)
                        ? '"'+strVal+'"' : strVal);
                    }
                }
                sBuff.append("\n");
            }
        }
        return sBuff;
    }
    
    
    
//
//    DEBUG Help...
//
    void printSelection() {
        int[] scols = tblData.getSelectedColumns();
        int[] srows = tblData.getSelectedRows();
        
        for( int idx:srows)
            System.out.println("Selected Rows:  View="+idx+",  Model="+tblData.convertRowIndexToModel( idx));
        for( int idx:scols)
            System.out.println("Selected Cols:  View="+idx+",  Model="+tblData.convertColumnIndexToModel( idx));
        System.out.println("");
    }
    
    void printColList() {
        int idx = 0;
        TableColumnModel colModel = tblData.getColumnModel();
        Enumeration<TableColumn> eTC = colModel.getColumns();
        while( eTC.hasMoreElements()){
            TableColumn tc = eTC.nextElement();
            System.out.println("["+idx+++"]: "+tc.getHeaderValue());
        }
    }
    
    
//
//      EVENT Mgmt
//
    
    
    /**     Given a view-row-index of the data table, it returned the actual row number,
     * the one that was in effect when there was no sort/filter.   */
    int convertRowIndexToModel(int viewRowIndex) {
        return tblData.convertRowIndexToModel( viewRowIndex);
    }
    
    /** Returned underlying table row count...  */
    int getRowCount() {
        return tblData.getRowCount();
    }
    
    void addExportCellsActionlistener(ActionListener exportCellsActionListener) {
        this.exportCellsActionListener = exportCellsActionListener;
    }
    
    
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addCol;
    private javax.swing.JButton butCopy;
    private javax.swing.JButton butErazeContents;
    private javax.swing.JButton butExportSelection;
    private javax.swing.JButton butPaste;
    private javax.swing.JButton butRemoveCol;
    private javax.swing.JButton butRemoveExtendedCols;
    private javax.swing.JButton butSelectAll;
    private javax.swing.JButton butUndoColDelete;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JMenuItem miAddCol;
    private javax.swing.JMenuItem miClearSelection;
    private javax.swing.JMenuItem miCopy;
    private javax.swing.JMenuItem miCut;
    private javax.swing.JMenuItem miExportCells;
    private javax.swing.JMenuItem miExportRows;
    private javax.swing.JMenuItem miPaste;
    private javax.swing.JMenuItem miRemoveCols;
    private javax.swing.JMenuItem miUndoRemoveCol;
    private javax.swing.JPopupMenu popTblContextMnu;
    private javax.swing.JScrollPane spaData;
    private javax.swing.JTable tblData;
    // End of variables declaration//GEN-END:variables
    
//
//  Helpers Classes...
//
/// Actions...
    
    /** Remove all Selected Columns. */
    class RemoveAllSelectedExtColumnsAction extends AbstractAction {
        RemoveAllSelectedExtColumnsAction() {
            super("Remove Columns");
            setEnabled( false);
        }
        
        public void actionPerformed(ActionEvent e) {
            removeExtColumns( tblData.getSelectedColumns());
        }
        
//        public boolean isEnabled(){
//            return isOnlyExtColumnInSelection();
//        }
    }
    
    
    /**     Undo deleted columns */
    class UndeleteColumnsAction extends AbstractAction {
        UndeleteColumnsAction() {
            super("Undo Remove-Col");
            setEnabled( false);
        }
        
        public void actionPerformed(ActionEvent e) {
            undoLastcolDelete();
        }
        
//        public boolean isEnabled(){
//            return removedTCols.size() > 0;
//        }
    }
    
    /**     Paste from Clipboard.       */
    class PasteFromClipAction extends AbstractAction{
        PasteFromClipAction() {
            super("Paste");
            setEnabled( false);
        }
        
        public void actionPerformed(ActionEvent e) {
            pasteFromClipboard();
        }
    }
    
    
    /**     Paste from Clipboard.       */
    class ClearSelectionAction extends AbstractAction{
        ClearSelectionAction() {
            super("Clear Selection");
            setEnabled( false);
        }
        
        public void actionPerformed(ActionEvent e) {
            doClearSelection();
        }
    }
    
    
    /**     Export Selection to File.       */
    class ExportSelectionAction extends AbstractAction
            implements ListSelectionListener{
        /**  Listen to following table for selection events, and keep enabled status synchronised to it.  */
        JTable tblAssociated;
        
        ExportSelectionAction() {
            super("Export Selection");
            setEnabled( false);
        }
        
        public void actionPerformed(ActionEvent e) {
            exportCellsActionListener.actionPerformed( e);
        }
        
        /** This instance will attach itself to the Table, for listening table selections   */
        private void addAssociatedTable(JTable tbl) {
            tblAssociated = tbl;
            tbl.getSelectionModel().addListSelectionListener( this);
            tbl.getColumnModel().getSelectionModel().addListSelectionListener( this);
        }
        
        public void valueChanged(ListSelectionEvent e) {
            setEnabled(
                    tblAssociated.getSelectedRowCount()
                    + tblAssociated.getSelectedColumnCount() > 0);
        }
        
    }
    
    
    /**     Listen to Ciolumnm Selections, so that dependent  button could be enabled/disabled.     */
    class ColSelectionsListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            if( !e.getValueIsAdjusting()) {
                boolean onlyExtColumnSelected = isOnlyExtColumnInSelection();
                actRemoveCols.setEnabled( onlyExtColumnSelected);
                actPaste.setEnabled( onlyExtColumnSelected);
                actClearSelection.setEnabled( onlyExtColumnSelected);
            }
        }
    }
    
    
    class CompositeTableModel extends AbstractTableModel implements TableModelListener{
//
//         Enclosed State and Workers......
//
        /** The Read Only component of the Table Data is stored here. */
        private TableModel roDataModel;
        
        /** The New Columns of the Table Data are stored and maintained here. */
        private DefaultTableModel extDataModel;
        
        //  Fixed state, once read-only models is connected to this...
        int roModelRowCount = -1;
        int roModelColCount = -1;
//
//         Constructor...
//
        /**
         *  It is asumed that Read Only Model does NOT change.
         *  Therefore,  changes performed in read-only model will not be automatically be shown in the JTable.
         */
        private CompositeTableModel( TableModel roDataModel) {
            setPrimaryDataModel( roDataModel);
            
            if( null == extDataModel)
                //  the extentions to the Table are written to this model,
                //  so that the Read-Only Model remains intact...
                extDataModel = new DefaultTableModel( roDataModel.getRowCount(), 0);
            
            connectModels();
        }
        
//
//         Property Accessors...
//
        /** Getter method for underlying Read-Only Table Model */
        TableModel getPrimaryModel() {
            return roDataModel;
        }
        
        /** Setter method for underlying Read-Only Table Model */
        private void setPrimaryDataModel(TableModel roDataModel) {
            if( null == roDataModel)
                throw new IllegalStateException("Primary data Model NOT set. " +
                        "setupCompositeDataModel() should be called after " +
                        "setPrimaryDataModel(). NOTE: roDataModel="+roDataModel);
            
            this.roDataModel = roDataModel;
            roModelColCount = roDataModel.getColumnCount();
            roModelRowCount = roDataModel.getRowCount();
        }
        
        /** Getter for Underlying Extended table Model */
        TableModel getExtendedTableModel() {
            return extDataModel;
        }
        
//
//         Behaviour management...
//
        private void clearExtendedDataModel() {
            extDataModel.getDataVector().clear();
            extDataModel.setColumnCount( 0);
            extDataModel.setRowCount( 0);
        }
        
        private void connectModels() {
            extDataModel.addTableModelListener( CompositeTableModel.this);
            //  Following NOT required...
            //  roDataModel.addTableModelListener( ... );
        }
        
//
//         Listeners management...
//
        public void tableChanged(TableModelEvent oe) {
            //  Performing Co-ordinates Transformation (Ext-Model space to Composite-Model space)...
            int fr = oe.getFirstRow();
            int lr = oe.getLastRow();
            int col = oe.getColumn() + roDataModel.getColumnCount();
            TableModelEvent ne = new TableModelEvent(
                    (TableModel)oe.getSource(), fr, lr, col, oe.getType());
            // Relay to Parent...
            fireTableChanged( ne);
        }
        
//
//         Table Model's Abstract Method now Implemented...
//
        public int getRowCount() {
            assert roDataModel.getRowCount() == roModelRowCount;
            return roDataModel.getRowCount();
        }
        
        public int getColumnCount() {
            assert roDataModel.getColumnCount() == roModelColCount;
            return roDataModel.getColumnCount() + extDataModel.getColumnCount();
        }
        
        @Override
        public String getColumnName(int columnIndex) {
            assert roDataModel.getColumnCount() == roModelColCount;
            if( columnIndex < roDataModel.getColumnCount())
                //  ReadOnly Model has this information...
                return roDataModel.getColumnName( columnIndex);
            
            //  column number index in Extensible model is required, to get Column Name...
            int idxCol2 = columnIndex - roDataModel.getColumnCount();
            return extDataModel.getColumnName( idxCol2);
        }
        
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if( columnIndex < roDataModel.getColumnCount())
                //  ReadOnly Model has this information...
                return roDataModel.getColumnClass( columnIndex);
            
            //  column number index in Extensible model is required, to get Column Name...
//            int idx2 = columnIndex - roDataModel.getColumnCount();
//            return extDataModel.getColumnClass( idx2);
            //  Assume all to be strings...
            return String.class;
        }
        
        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if( columnIndex < roDataModel.getColumnCount())
                //  ReadOnly Model has this information...
                return false;
//                return roDataModel.isCellEditable( rowIndex, columnIndex);
            
            //  column number index in Extensible model is required, to get Column Name...
            int idxCol2 = columnIndex - roDataModel.getColumnCount();
            return extDataModel.isCellEditable( rowIndex, idxCol2);
        }
        
        public Object getValueAt(int rowIndex, int columnIndex) {
            assert roDataModel.getColumnCount() == roModelColCount;
            if( columnIndex < roDataModel.getColumnCount())
                //  ReadOnly Model has this information...
                return roDataModel.getValueAt( rowIndex, columnIndex);
            
            //  column number index in Extensible model is required, to get Column Name...
            int idxCol2 = columnIndex - roDataModel.getColumnCount();
            return extDataModel.getValueAt( rowIndex, idxCol2);
        }
        
        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if( columnIndex < roDataModel.getColumnCount())
                //  ReadOnly Model has this information...
                throw new IllegalStateException("These table cells are read-Only.");
            
            //  column number index in Extensible model is required, to get Column Name...
            int idxCol2 = columnIndex - roDataModel.getColumnCount();
            extDataModel.setValueAt( aValue, rowIndex, idxCol2);
        }
        
        /**
         *  @returns: The newly created tableColumn. It NOT attached to the table yet.
         */
        private TableColumn createNewColumn() {
            //  Create a new Column, set its header value and return the reference...
            String colName = EXT_COLUMNS_NAME_PREFIX+(++columnAppendCount);
            TableColumn newTC = new TableColumn( getColumnCount());
            newTC.setHeaderValue( colName);
            newTC.setPreferredWidth( defaultColWidth);
            newTC.setMinWidth( defaultColWidth);
            return newTC;
        }
        
        /**     Now add a new column to the underlying storage.
         * it is still NOT safe to announce its presence, so momemtorarily disable listeners     */
        private void addNewColumn( TableColumn tc) {
            extDataModel.addColumn( tc.getHeaderValue());
        }
        
        /**
         *  @returns Array of all the columns that belongs to Extended Column Model
         */
        private int[] getViewIdxForAllExtColumns() {
//            extDataModel
            return new int[0];
        }
    }
    
    
    
//
//     Grave-Yard...
//
    
}
