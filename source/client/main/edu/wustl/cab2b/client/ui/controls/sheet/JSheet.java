/*
 * JSheet.java
 *
 * Created on October 4, 2007, 2:00 PM
 */
package edu.wustl.cab2b.client.ui.controls.sheet;

import static edu.wustl.cab2b.client.ui.controls.sheet.Common.setBackgroundWhite;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import edu.wustl.cab2b.client.ui.controls.Cab2bFileFilter;

/**
 *
 * @author  jasbir_sachdeva
 */
public class JSheet extends javax.swing.JPanel {
    Logger lgr = Logger.getLogger(getClass().getName());

    /** Event name that notifies that User has pressed Magnifying-Glass button...      */
    public static final String EVENT_HEADER_ROW_DOUBLE_CLICKED = "EVENT_HEADER_ROW_DOUBLE_CLICKED";

    /** Event name that notifies that User has pressed Magnifying-Glass button...      */
    public static final String EVENT_DATA_ROW_DOUBLE_CLICKED = "EVENT_DATA_ROW_DOUBLE_CLICKED";

    /** Added  by Deepak : Event name that notifies that User has selected some data...      */
    public static final String EVENT_DATA_SINGLE_CLICKED = "EVENT_DATA_SINGLE_CLICKED";

    /** Event name that notifies that User is interested in details of some Row...      */
    public static final String REQUESTED_SHOW_ROW_DETAILS = "REQUESTED_SHOW_ROW_DETAILS";

    public static final String MENU_BUTTON_COPY = "MENU_BUTTON_COPY";

    public static final String MENU_BUTTON_PASTE = "MENU_BUTTON_PASTE";

    public static final String MENU_BUTTON_ADD_COLUMN = "MENU_BUTTON_ADD_COLUMN";

    public static final String MENU_BUTTON_PROPERTIES = "MENU_BUTTON_PROPERTIES";

    public static final String MENU_BUTTON_RESET = "MENU_BUTTON_RESET";

    public static final String MENU_BUTTON_CLEAR = "MENU_BUTTON_CLEAR";

    /**     Should I allow user to create new columns and allow cut/paste on them?      */
    boolean allowWrite = true;

    /** The Visual components that accepts User settings: which Columns to view: */
    SheetCustomizationConsole consSheetCust = new SheetCustomizationConsole();

    //    SheetCustomizationConsole conSheetCustomization = new SheetCustomizationConsole();
    /**   This panel presents context sensitive Filter Control - for single column. 
     *          The last selected Column in Data View is picked up.
     This Visual component that shows applicable filter as GUI to user, providing chance for correction*/
    ColumnFilterVerticalConsole consColFilter = new ColumnFilterVerticalConsole();

    /***/
    FiltersViewConsole consFiltersView = new FiltersViewConsole();

    /***/
    JDialog colVisibilitySettingsDialog;

    /**     Sheet Customization (Visibility & Filters) are kept here...    */
    SheetCustomizationModel scm;

    /**     Sheet Configuration Monitoring   */
    InternalPCListener lsnSheetConf;

    /**    Data  View COnsole Montoring. */
    InternalPCListener lsnVeiwData = new InternalPCListener();

    //    ArrayList<Set<Object>> sampleValuesAL;
    /**     Indicator flag if sample values from filter should be creatred from Table Model, or 
     filter should be diasbled if NOT explicitly provided.   **/
    private boolean createSampleValuesFromModel = false;

    /**     Column Selection Listener */
    private ColSelectionListener lnsColSelection = new ColSelectionListener();

    ///////////////////////////////////////////////////////////
    /**  The actual Visual Component that renders table on the scren for the user.*/
    ViewDataConsole consData = new ViewDataConsole();

    /** Special Model to show a Button, and Row selection chk box on LHS.*/
    //    FixedLeftColsTblModel tmFixedLeft;
    /**     If this is true, SelectionHanger is allowed to paint itself as selected,
     * iff table cell selected is true as per model.  */
    private boolean selectionRowMode = false;

    private boolean mousePressed = false;

    /** If true, magnifying glass is shown. Pressing on it will fire "Common.EVENT_MAGNIFYING_GLASS_CLICKED" event. */
    private boolean showMG;

    SheetColumn colMG;

    /** refernce to oginal params setModel(...) , for RESET    */
    private TableModel tmROData;

    /** refernce to oginal params setModel(...) , for RESET    */
    private ArrayList<TreeSet<Comparable<?>>> sampleValuesAL;

    /**
     * Creates new form JSheet
     */
    public JSheet() {
        initComponents();

        //  Set up Main Date Viewer and Column Manager...
        pnlDataView.add(consData);
        consData.addPropertyChangeListener(lsnVeiwData);
        consData.addTableColSelectionListener(lnsColSelection);

        pnlSheetCust.add(consSheetCust, BorderLayout.CENTER);
        applySizeOn(diaConsSheetCust);

        //  Debug...
        //        showColFilterConsoleInDialog();
        //         showFIlterViewConsoleInDialog();

        //        pnlSheetCust.add(colVisibilityConsole);
        //        consData.addPropertyChangeListener(colVisibilityConsole);
        //        colVisibilityConsole.addPropertyChangeListener(consData);
        consData.addExportCellsActionlistener(new ExportCellsActionListener());
        consData.addShowPropertyDialogActionListener(new ShowPropDialogActionListener());

        //  Setup Filter UI Component...
        //        pnlCommonFilter.removeAll();
        //        pnlCommonFilter.add(pnlCommonRangeFilter);
        //        consData.addPropertyChangeListener(pnlCommonRangeFilter);
        //        colVisibilityConsole.addPropertyChangeListener(pnlCommonRangeFilter);
        //        pnlCommonRangeFilter.setRelatedTable(consData.getDataViewTable());

        //  For column removal Button...
        //        TableChangesSynchronizer tcs = new TableChangesSynchronizer();
        //        consData.addTableListener(tcs);
        //        consData.addTableRowsorterListener(tcs);

        //  For Row Selections...
        //        tblFixed.setSelectionModel(consData.getSelectionModel());
        //        tblFixed.getSelectionModel().addListSelectionListener(tcs);
        //        DataViewCellsSelectionListener dvcsl = new DataViewCellsSelectionListener();
        //        consData.addTableColSelectionListener(dvcsl);
        //        consData.addTableRowSelectionListener(dvcsl); 

        setBackgroundWhite(this);
    }

    /**     Append new JButtons in the Toolbar, with the specified actions. 
     All the old specified Actions will be removed.  
     However, the JSheet default Actions cannot be changed.
     
     @param actions Arralist specifiing Actions of one or more JButton(s).
     */
    public void setAdditionalToolbarActions(List<Action> actions) {
        consData.setAdditionalToolbarActions(actions);
    }

    public void removeComponentFromToolBar(String menuName) {
        consData.removeComponentFromToolBar(menuName);
    }

    public TableModel getJSheetTableModel() {
        return consData.getCompositeDataModel();
    }

    public JSheetViewDataModel getViewTableModel() {
        return consData.getViewTableModel();
    }

    public void applySizeOn(JDialog dia) {
        setBackgroundWhite((JComponent) dia.getContentPane());
        Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
        dia.setBounds(ss.width * 2 / 8, ss.height / 7, ss.width * 4 / 8, ss.height * 5 / 7);

        //Added code to fix bug 6460 - Deepak 
        dia.setModal(true);
    }

    /**     Returns the top most compoennt in the containment hierarchy that is heavy weight.   */
    static Window getHWRoot(Component comp) {
        while (null != comp && !(comp instanceof Window)) {
            comp = comp.getParent();
        }

        //  Either comp is null, or it is some heavy weight component, as top most container...
        return (Window) comp;
    }

    public int[] getSelectedRows() {
        return consData.getSelectedRows();
    }

    public int[] getSelectedColumns() {
        return consData.getSelectedColumns();
    }

    public int getSelectedRow() {
        return consData.getSelectedRow();
    }

    public int getSelectedColumn() {
        return consData.getSelectedColumn();
    }

    public Object getValueAt(int row, int column) {
        return consData.getValueAt(row, column);
    }

    public JComponent getContextFilterConsole() {
        diaFilterConsole.getContentPane().removeAll();
        diaFilterConsole.setVisible(false);
        return consColFilter;
    }

    public JComponent getFiltersViewConsole() {
        diaFiltersViewConsole.getContentPane().removeAll();
        diaFiltersViewConsole.setVisible(false);
        return consFiltersView;
    }

    public void addRowSelectionListener(ListSelectionListener lsl) {
        consData.addTableRowSelectionListener(lsl);
    }

    public void removeRowSelectionListener(ListSelectionListener lsl) {
        consData.removeTableRowSelectionListener(lsl);
    }

    public void addColumnSelectionListener(ListSelectionListener lsl) {
        consData.addTableColSelectionListener(lsl);
    }

    public void removeColumnSelectionListener(ListSelectionListener lsl) {
        consData.removeTableColSelectionListener(lsl);
    }

    private void showColFilterConsoleInDialog() {
        diaFilterConsole.getContentPane().removeAll();
        diaFilterConsole.getContentPane().add(consColFilter, BorderLayout.CENTER);
        diaFilterConsole.setVisible(true);
    }

    private void showFIlterViewConsoleInDialog() {
        diaFiltersViewConsole.getContentPane().removeAll();
        diaFiltersViewConsole.getContentPane().add(consFiltersView, BorderLayout.CENTER);
        diaFiltersViewConsole.setVisible(true);
    }

    class TableChangesSynchronizer implements TableModelListener, RowSorterListener, ListSelectionListener {

        public void tableChanged(TableModelEvent e) {
        }

        public void sorterChanged(RowSorterEvent e) {
            //             
            //             
            //            tmFixedLeft.setRowCount(consData.getRowCount());
        }

        /** Selection is Row-Selection_hanger should match full row selection is data-view.     */
        public void valueChanged(ListSelectionEvent e) {
            if (selectionRowMode) {
                consData.extendSelectionsToAllColumns();
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////                                       PUBLIC setters: configeration of ths is instance.
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     *  Senario - Type C, No 2: public method call SetReadOnlyDataModel on JSheet.
     * 
     * Set the Model model that will be shown.
     * Default View is without filtering and Sorting.
     * Sample values are computed by scanning over entire Table Model. Lazy Table Model, if any will in this case.
     * @param tmROData -    The basic data to be shown in JSheet.
     */
    public void setReadOnlyDataModel(TableModel tmROData) {
        setReadOnlyDataModel(tmROData, null);
    }

    /**
     *  Senario - Type C, No 2: public method call SetReadOnlyDataModel on JSheet.
     * 
     * Set the Model model that will be shown.
     * Default View is without filtering and Sorting.
     * @param tmROData -    The basic data to be shown in JSheet.
     * @param sampleValuesAL  Each column can take some possible values which are expected in a Set.
     * Since there are more than 1 column, we have to supply more than 1 set. Club all those Sets in an 
     * ArrayList <code>sampleValues </code>. 
     */
    public void setReadOnlyDataModel(TableModel tmROData, ArrayList<TreeSet<Comparable<?>>> sampleValuesAL) {

        if (null == sampleValuesAL) {
            sampleValuesAL = new ArrayList();
            createSampleValuesFromModel = true;
        } else {
            this.sampleValuesAL = sampleValuesAL;
            createSampleValuesFromModel = false;
        }

        this.tmROData = tmROData;
        if (null == tmROData) {
            throw new IllegalArgumentException("JSheet.setReadOnlyDataModel() does NOT accepts null model. ");
        }

        //  A:  Property Event Propagation Chain setup...
        setupModels(tmROData.getColumnCount());

        //  B:  Table & TableColumn Model settings and setup infra for Relay of Table Model Events...
        //  Table filter setting...
        consData.setTableFilter(scm.getTableFilter());
        consData.setReadOnlyDataModel(tmROData, scm.getRowInfoAL());

        //  C:  Setting up sample values in ColFilterModel (Used by Range & List Filters)...
        setupSampleValues(sampleValuesAL);

        consData.revalidate();
        consData.repaint();
    }

    /**     Senario - C.2-A: Property Event Propagation Chain setup.    */
    private void setupModels(int colCount) {
        //  reference to old network of objects maintained by SheetCustomizationModel is now lost - for garbage collection...
        scm = new SheetCustomizationModel();
        lsnSheetConf = new InternalPCListener();

        //  Create set of Sheet Column for Column in data view to hold configuration information,
        //  Also chain dependencies and Event Propogation chain...
        ArrayList<SheetColumn> alCols = new ArrayList<SheetColumn>();
        for (int idx = 0; idx < colCount; idx++) {
            SheetColumn newSheetCol = createNewColumnModel(idx);
            alCols.add(newSheetCol);
        }
        scm.setRowInfos(alCols);
        scm.addPropertyChangeListener(lsnSheetConf);

        //setting button looks on property sheet 
        Common.setButtonsLooks(consSheetCust);
        Common.setButtonsLooks(consColFilter);

        //  Set Model to Sheet Customization Console...
        consSheetCust.setModel(scm);
        consFiltersView.setModel(scm);
        consColFilter.setModel(null);
    }

    private SheetColumn createNewColumnModel(int idx) {
        SheetColumn col = new SheetColumn(idx);
        ColumnFilterModel cfm = new ColumnFilterModel();
        col.setFilterCondition(cfm);
        return col;
    }

    /**    Senario - C.2-C:  Setting up sample values in ColFilterModel (Used by Range & List Filters)   */
    @SuppressWarnings("empty-statement")
    private void setupSampleValues(ArrayList<TreeSet<Comparable<?>>> sampleValuesAL) {
        if (null == sampleValuesAL) {
            sampleValuesAL = new ArrayList();
        }

        ArrayList<SheetColumn> colSheetAL = scm.getRowInfoAL();
        TableModel roModel = consData.getReadOnlyDataModel();
        assert (colSheetAL.size() == roModel.getColumnCount());

        for (int idxCol = 0; idxCol < roModel.getColumnCount(); idxCol++) {
            ColumnFilterModel cfm = colSheetAL.get(idxCol).getFilterCondition();
            TreeSet sampleValues = getSetForIdx(sampleValuesAL, idxCol);
            if (null == sampleValues) {
                if (createSampleValuesFromModel) {
                    //  we should create sample values from table...
                    cfm.setSampleValues(roModel, idxCol);
                } else //  ignore, Filter will be ineffective, due to want of Sample Values...
                {
                    ;
                }
            } else {
                //  Setting up sample values...
                cfm.setSampleValues(sampleValues);
            }
        }
    }

    static private TreeSet getSetForIdx(ArrayList<TreeSet<Comparable<?>>> sampleValues, int idx) {
        if (idx < sampleValues.size()) //  Value exists at given index...
        {
            return sampleValues.get(idx);
        }

        return null;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////                                                                             BEHAVIOUR SPECIFIC    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void addUserColumn() {
        //  model index is -1, for unknown.
        //  Create a Column...
        SheetColumn sCol = createNewColumnModel(-1);
        sCol.setUserColumn(true);
        //  Add it to configuration model...
        scm.appendRowInfo(sCol);
        //  Create space to accomodate new values in data mode...
        consData.addUserColumn(sCol);
        //  Announce...
        firePropertyChange(Common.USER_COLUMN_ADDED, null, sCol);
    }

    void reapplyFilter() {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                consData.reapplyFilter();
            }
        });
    }

    void resetAll() {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                if (createSampleValuesFromModel) {
                    setReadOnlyDataModel(tmROData);
                } else {
                    setReadOnlyDataModel(tmROData, sampleValuesAL);
                }
            }
        });
    }

    void reapplyColumnVisibility(SheetColumn colSheet) {
        consData.setColumnVisibility(colSheet);
    }

    //    /**     Notifyies listeners that Magnifing glass button has clicked.
    //     * Model Row index, wher click was detected is passed as New-Value.    */
    //    void fireRowDoubleClicked(int viewRowIndex, int viewColumnIndex) {
    //        int row = consData.convertRowIndexToModel(viewRowIndex);
    ////         
    //        firePropertyChange(EVENT_ROW_DOUBLE_CLICKED, -1, row);
    //    }

    //    /**     Notifyies listeners that Magnifing glass button has clicked.
    //     * Model Row index, wher click was detected is passed as New-Value.    */
    //    void fireShowDetailsClicked(int viewRowIndex, int viewColumnIndex) {
    //        int row = consData.convertRowIndexToModel(viewRowIndex);
    ////         
    //        firePropertyChange(REQUESTED_SHOW_ROW_DETAILS, -1, row);
    //    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tblFixed = new javax.swing.JTable();
        pnlFixedTblHeader = new javax.swing.JPanel();
        pnlFixedTblMGCellRenderer = new javax.swing.JPanel();
        pnlFixedTblMGCellEditor = new javax.swing.JPanel();
        pnlFixedTblHangerRenderer = new javax.swing.JPanel();
        diaFilterConsole = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        butDiaFilterClose = new javax.swing.JButton();
        diaConsSheetCust = new javax.swing.JDialog();
        pnlSheetCust = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        butDiaSheetCustClose = new javax.swing.JButton();
        diaFiltersViewConsole = new javax.swing.JDialog();
        jPanel3 = new javax.swing.JPanel();
        pnlConsole = new javax.swing.JPanel();
        pnlDataView = new javax.swing.JPanel();

        tblFixed.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] { { null, null, null, null }, { null, null, null, null }, { null, null, null, null }, { null, null, null, null } },
                new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }));
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

        pnlFixedTblMGCellRenderer.setPreferredSize(new java.awt.Dimension(20, 20));
        pnlFixedTblMGCellRenderer.setLayout(new java.awt.BorderLayout());

        pnlFixedTblMGCellEditor.setToolTipText("Click Me to View This Row in More Details");
        pnlFixedTblMGCellEditor.setPreferredSize(new java.awt.Dimension(20, 20));
        pnlFixedTblMGCellEditor.setLayout(new java.awt.BorderLayout());

        diaFilterConsole.setTitle("JSheet - Context Sensitive Column Filter");
        diaFilterConsole.setLocationByPlatform(true);
        diaFilterConsole.setMinimumSize(new java.awt.Dimension(200, 200));

        butDiaFilterClose.setText("Close");
        butDiaFilterClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butDiaFilterCloseActionPerformed(evt);
            }
        });
        jPanel1.add(butDiaFilterClose);

        diaFilterConsole.getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        diaConsSheetCust.setTitle("Data View - Define Columns ");

        pnlSheetCust.setLayout(new java.awt.BorderLayout());
        diaConsSheetCust.getContentPane().add(pnlSheetCust, java.awt.BorderLayout.CENTER);

        butDiaSheetCustClose.setText("Close");
        butDiaSheetCustClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butDiaSheetCustCloseActionPerformed(evt);
            }
        });
        jPanel2.add(butDiaSheetCustClose);

        diaConsSheetCust.getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        diaFiltersViewConsole.setMinimumSize(new java.awt.Dimension(100, 100));

        setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new java.awt.BorderLayout());

        pnlConsole.setLayout(new java.awt.BorderLayout());
        jPanel3.add(pnlConsole, java.awt.BorderLayout.NORTH);

        pnlDataView.setLayout(new java.awt.BorderLayout());
        jPanel3.add(pnlDataView, java.awt.BorderLayout.CENTER);

        add(jPanel3, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void tblFixedMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFixedMouseEntered
        // TODO add your handling code here:

    }//GEN-LAST:event_tblFixedMouseEntered

    private void tblFixedMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFixedMouseClicked
        // TODO add your handling code here:
        //         
    }//GEN-LAST:event_tblFixedMouseClicked

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

    private void butDiaFilterCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butDiaFilterCloseActionPerformed
        // TODO add your handling code here:
        diaFilterConsole.setVisible(false);
    }//GEN-LAST:event_butDiaFilterCloseActionPerformed

    private void butDiaSheetCustCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butDiaSheetCustCloseActionPerformed
        // TODO add your handling code here:
        diaConsSheetCust.setVisible(false);
    }//GEN-LAST:event_butDiaSheetCustCloseActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton butDiaFilterClose;

    private javax.swing.JButton butDiaSheetCustClose;

    private javax.swing.JDialog diaConsSheetCust;

    private javax.swing.JDialog diaFilterConsole;

    private javax.swing.JDialog diaFiltersViewConsole;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel pnlConsole;

    private javax.swing.JPanel pnlDataView;

    private javax.swing.JPanel pnlFixedTblHangerRenderer;

    private javax.swing.JPanel pnlFixedTblHeader;

    private javax.swing.JPanel pnlFixedTblMGCellEditor;

    private javax.swing.JPanel pnlFixedTblMGCellRenderer;

    private javax.swing.JPanel pnlSheetCust;

    private javax.swing.JTable tblFixed;

    // End of variables declaration//GEN-END:variables

    public static void main(String[] args) {
        SheetTestFrame.main(args);
    }

    class InternalPCListener implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {

            if (evt.getPropertyName().equals(Common.USER_COLUMN_ADDITION_REQUESTED)) {
                addUserColumn();
                return;
            }
            if (evt.getPropertyName().equals(Common.COLUMN_VISIBLITY_CHANGE_REQUESTED)) {
                reapplyColumnVisibility((SheetColumn) evt.getNewValue());
                //  The column may have some associated active filter ...
                reapplyFilter();
                return;
            }
            if (evt.getPropertyName().equals(SheetColumn.FILTER_CHANGED)) {
                //  Schedule call to filtering...
                reapplyFilter();
                return;
            }

            if (evt.getPropertyName().equals(Common.REQUEST_RESET_ALL)) {
                //  Schedule call to reset...
                resetAll();
                return;
            }

            if (evt.getPropertyName().equals(Common.REQUESTED_SHOW_ROW_DETAILS)) {
                //  Schedule call to reset...
                firePropertyChange(REQUESTED_SHOW_ROW_DETAILS, -1, evt.getNewValue());
                return;
            }

            if (evt.getPropertyName().equals(Common.EVENT_DATA_ROW_DOUBLE_CLICKED)) {
                //  Schedule call to reset...
                firePropertyChange(EVENT_DATA_ROW_DOUBLE_CLICKED, -1, evt.getNewValue());
                return;
            }

            if (evt.getPropertyName().equals(Common.EVENT_DATA_SINGLE_CLICKED)) {

                //  Schedule call to reset...
                firePropertyChange(EVENT_DATA_SINGLE_CLICKED, -1, evt.getNewValue());
                return;
            }

            if (evt.getPropertyName().equals(Common.EVENT_HEADER_ROW_DOUBLE_CLICKED)) {
                //  Schedule call to reset...
                firePropertyChange(EVENT_HEADER_ROW_DOUBLE_CLICKED, -1, evt.getNewValue());
                return;
            }
        }
    }

    //    /**     If DataView cells selections has changed, we need to remove selection from Row Selectors... */
    //    class DataViewCellsSelectionListener implements ListSelectionListener {
    //
    //        public void valueChanged(ListSelectionEvent e) {
    //            if (!mousePressed) {
    //                selectionRowMode = false;
    //                tblFixed.repaint();
    //            }
    //        }
    //    }
    class ColSelectionListener implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting()) //  Ignore it...
            {
                return;
            }

            SheetColumn sheetInfo = null;
            int idxSelCol = consData.getSelectedColumn();
            if (idxSelCol < 0) //  There is a valid column selection, may be no column is selected...
            {
                return;
            }

            sheetInfo = scm.getSheetInfo(consData.convertColumnIndexToModel(idxSelCol));

            if (sheetInfo.isUserColumn()) {
                consColFilter.setModel(null);
                consColFilter.setHeader("<html><br>Filter NOT supported<br>on User Columns");
                return;
            }

            //  At last we can something useful...
            //  Set appropriate model on the Context dependent FilterConsole...
            consColFilter.setModel(sheetInfo.getFilterCondition());
            if (null == sheetInfo.getHeaderValue()) {
                consColFilter.setHeader("");
            } else {
                consColFilter.setHeader(sheetInfo.getHeaderValue().toString());
            }
        }
    }

    class ExportCellsActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            exportSelectedCells();
        }
    }

    class ShowPropDialogActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            diaConsSheetCust.setVisible(true);
            //showColVisibilitySettingsDialog();
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
            fileChooser.setFileFilter(new Cab2bFileFilter(new String[] { "csv" }));
            int status = fileChooser.showSaveDialog(this);
            if (JFileChooser.APPROVE_OPTION == status) {
                File selFile = fileChooser.getSelectedFile();
                String fileName = selFile.getAbsolutePath();

                if (true == selFile.exists()) {
                    // Prompt user to confirm if he wants to override the value
                    int confirmationValue = JOptionPane.showConfirmDialog(fileChooser, "The file "
                            + selFile.getName() + " already exists.\nDo you want to replace existing file?",
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

                    String csvString = consData.getTblSelectionDataWithCommas(true).toString();
                    //                    String csvString = cellSelectionEnabled ? 
                    //                        consData.getTblSelectionDataWithCommas().toString() 
                    //                        : consData.getTblRowDataWithComma(tmFixedLeft.getRowSelections()).toString();
                    out.write(csvString);
                    out.close();

                } catch (IOException e) {
                    String message = String.format("Exception while exporting data in file: %s. \nReason=%s",
                                                   fileName, e.getMessage());
                    lgr.warning(message);
                    JOptionPane.showMessageDialog(this, message);
                } finally {
                    try {
                        if (null != out)
                            out.close();
                    } catch (Exception e) {
                        lgr.warning(String.format(
                                                  "Exception while closing file. Export may NOT have been successful.\nReason=%s",
                                                  e.getMessage()));
                    }
                }
            }
            done = true;
        } while (!done);

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
