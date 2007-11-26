/*
 * CommonRangeFilterPane.java
 *
 * Created on October 23, 2007, 11:27 AM
 */
package edu.wustl.cab2b.client.ui.controls.sheet;

import edu.wustl.cab2b.client.ui.controls.slider.BiSlider;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author  jasbir_sachdeva
 */
class ColumnFilterPane extends javax.swing.JPanel
    implements PropertiesNames, PropertyChangeListener, RowSorterListener {

    /** Repository for Visible Columns  */
    Set<TableColumn> visibleCols = new TreeSet<TableColumn>(new TableColumnNamesComparator());
    /**     Quick Reference for columns Extra State that may affect Filtering.   */
    //    Set<ColumnExtraState> visibleColsES = new HashSet<ColumnExtraState>();

    ArrayList<ColumnExtraState> tblModelIdx2CES = new ArrayList<ColumnExtraState>();
    /**     Model of Combo box , that user use to select active colun to apply filter on.    */
    ColNamesComboBoxModel namesModel = new ColNamesComboBoxModel();
    /**    Eliminates useless values. */
    //    RangeFilter rangeFilter = new RangeFilter();

    UniversalFilter universalFilter = new UniversalFilter();
    /** the associated Table */
    JTable tblData;
    /** listener to Range Slider Componet   */
    RangeSliderListener rangeListener = new RangeSliderListener();

    /** Reference to the Active ColumnExtraState, that is effect by BiSlider, 
     * required by combo lister to set bounds the relevant ces.   */
    private ColumnExtraState activeCES;

    /**     Temporary Refrences to Combo box listerns, while disabling dilery of events...  */
    private ActionListener[] cboListeners;
    /** Action that controls Appy Filter button...  */
    ApplyFilterAction applyFilerAction = new ApplyFilterAction();


    /** Creates new form CommonRangeFilterPane */
    public ColumnFilterPane() {
        initComponents();
        cboColumnsList.setModel(namesModel);
        pnlRangeSlider.removeAll();
        butApplyPatternFilter.setAction(applyFilerAction);
        tfSrcStr.getDocument().addDocumentListener( new UserInputStingListener());
    }

    void reset() {
        visibleCols.clear();
        tblModelIdx2CES.clear();
    }

    /**     disable Combo-Box listners events */
    void disableComboBoxListeners() {
        cboListeners = cboColumnsList.getActionListeners();
        for (ActionListener al : cboListeners) {
            cboColumnsList.removeActionListener(al);
        }
    }

    /**     Enable Action Listners  */
    void enableComboBoxListeners() {
        for (ActionListener al : cboListeners) {
            cboColumnsList.addActionListener(al);
        }
    }

    /** Make Combo Model sync with the Visible Columns, that too insorted order.    */
    void prepareComboModel() {
        if (null == tblData) {
            throw new IllegalStateException(getClass() + ": prepareComboModel(): tblData =" + tblData + ". Please call setRelatedTable(...) before continuing. ");
        }

        disableComboBoxListeners();

        //  Populate combo with active columns...
        namesModel.removeAllElements();
        for (TableColumn vCol : visibleCols) {
            ColumnExtraState ces = (ColumnExtraState) vCol.getIdentifier();
            if (ces.getSampleSortedValues().size() > 1) {
                namesModel.addElement(vCol);
            }

        }
        if (namesModel.getSize() > 0) {
            cboColumnsList.setSelectedIndex(0);
        }

        enableComboBoxListeners();

        //  Make default Selection...
        if (cboColumnsList.getModel().getSize() > 0) {
            cboColumnsList.setSelectedIndex(0);
        }
    }

    /**     Genrates Odered List of Table columns as actually being displayed  in Table,
     * from present list of Table Columns in "visibleCols".      **/
    private void adjustTblMdlIdx2CES(TableColumn newTColumn) {
        //        System.out.println("Appending new entry in new tblModelIdx2CES, TblModelColIdx ="+newTColumn.getModelIndex());
        tblModelIdx2CES.add((ColumnExtraState) newTColumn.getIdentifier());
    }

    /**     Genrates Odered List of Table columns as actually being displayed  in Table,
     * from present list of Table Columns in "visibleCols".      **/
    private void prepareTblViewIdx2CES() {
    //        tblModelIdx2CES.clear();
//
//        //  Do not relie on "TableColumnModel", as it may update itself after return from this cal...
//        TableModel tm = tblData.getModel();
//        System.out.println("Creating new tblModelIdx2CES:");
////        for( int colIdx=0; colIdx< tcm.getColumnCount(); colIdx++)
////            System.out.println("tcm.getColumn( "+colIdx+").getIdentifier()) ="+tcm.getColumn( colIdx).getIdentifier());
//
//        //  Who has all the information regarding all avilable table memger, even if they are invisible...
//        for( int colIdx=0; colIdx< tm.getColumnCount(); colIdx++)
//            tblModelIdx2CES.add( (ColumnExtraState) tm.getColumn( colIdx).getIdentifier());
    }

    //
//      GETTERs & SETTERs
//

    RowFilter getRowFilter() {
        return universalFilter;
    }

    /** Range Filter needs to know on which table filters needs be applied.
     *          On demand active filters will be applied on this Table and appropriate events will be fired.
     */
    void setRelatedTable(JTable tblData) {
        this.tblData = tblData;
        //        tblData.getColumnModel().addColumnModelListener(new ColumnsListener());
        tblData.getRowSorter().addRowSorterListener(this);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        bgIncludeExclude = new javax.swing.ButtonGroup();
        bgAndOr = new javax.swing.ButtonGroup();
        bgSearchType = new javax.swing.ButtonGroup();
        pnlCommonFilter = new javax.swing.JPanel();
        pnlSelectColumn = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cboColumnsList = new javax.swing.JComboBox();
        jPanel4 = new javax.swing.JPanel();
        pnlStringInput = new javax.swing.JPanel();
        pnlSearchType = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        rbSearchString = new javax.swing.JRadioButton();
        rbSearchRegEx = new javax.swing.JRadioButton();
        pnlStrRegExExplaination = new javax.swing.JPanel();
        pnlStrSrchDetails = new javax.swing.JPanel();
        rbInclude = new javax.swing.JRadioButton();
        rbExclude = new javax.swing.JRadioButton();
        lblStringInput = new javax.swing.JLabel();
        pnlRegExDetails = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        pnlStrAndApply = new javax.swing.JPanel();
        tfSrcStr = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        butApplyPatternFilter = new javax.swing.JButton();
        pnlAndOr = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        rbAnd = new javax.swing.JRadioButton();
        rbOr = new javax.swing.JRadioButton();
        pnlRightSide = new javax.swing.JPanel();
        pnlRangeSlider = new javax.swing.JPanel();
        sldDummy = new javax.swing.JSlider();
        jPanel1 = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        pnlCommonFilter.setLayout(new java.awt.GridBagLayout());

        pnlSelectColumn.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Adjust Filter For Column: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
        pnlSelectColumn.add(jLabel1, gridBagConstraints);

        cboColumnsList.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboColumnsList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboColumnsListActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
        pnlSelectColumn.add(cboColumnsList, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.3;
        pnlSelectColumn.add(jPanel4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(5, 20, 0, 20);
        pnlCommonFilter.add(pnlSelectColumn, gridBagConstraints);

        pnlStringInput.setLayout(new java.awt.GridBagLayout());

        pnlStringInput.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(51, 0, 51)), "Condition-1: Specify Pattern Filter", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Narrow", 0, 16)));
        pnlSearchType.setLayout(new java.awt.GridBagLayout());

        jLabel3.setText("Use: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlSearchType.add(jLabel3, gridBagConstraints);

        bgSearchType.add(rbSearchString);
        rbSearchString.setSelected(true);
        rbSearchString.setText("Simple String Filter");
        rbSearchString.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbSearchStringActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.4;
        pnlSearchType.add(rbSearchString, gridBagConstraints);

        bgSearchType.add(rbSearchRegEx);
        rbSearchRegEx.setText("Regular Exp Filter");
        rbSearchRegEx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbSearchRegExActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.4;
        pnlSearchType.add(rbSearchRegEx, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        pnlStringInput.add(pnlSearchType, gridBagConstraints);

        pnlStrRegExExplaination.setLayout(new java.awt.CardLayout());

        pnlStrSrchDetails.setLayout(new java.awt.GridBagLayout());

        bgIncludeExclude.add(rbInclude);
        rbInclude.setSelected(true);
        rbInclude.setText("Include");
        rbInclude.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbIncludeActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        pnlStrSrchDetails.add(rbInclude, gridBagConstraints);

        bgIncludeExclude.add(rbExclude);
        rbExclude.setText("Exclude");
        rbExclude.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbExcludeActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        pnlStrSrchDetails.add(rbExclude, gridBagConstraints);

        lblStringInput.setText("<html>values that contains following <u>string</u>:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.8;
        pnlStrSrchDetails.add(lblStringInput, gridBagConstraints);

        pnlStrRegExExplaination.add(pnlStrSrchDetails, "StrSrch");

        pnlRegExDetails.setLayout(new java.awt.BorderLayout());

        jLabel2.setText("Select Values that qualify following Regular Expression:");
        pnlRegExDetails.add(jLabel2, java.awt.BorderLayout.CENTER);

        pnlStrRegExExplaination.add(pnlRegExDetails, "RegEx");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        pnlStringInput.add(pnlStrRegExExplaination, gridBagConstraints);

        pnlStrAndApply.setLayout(new java.awt.BorderLayout());

        tfSrcStr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfSrcStrActionPerformed(evt);
            }
        });

        pnlStrAndApply.add(tfSrcStr, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        butApplyPatternFilter.setText("Apply");
        butApplyPatternFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butApplyPatternFilterActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel2.add(butApplyPatternFilter, gridBagConstraints);

        pnlStrAndApply.add(jPanel2, java.awt.BorderLayout.EAST);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        pnlStringInput.add(pnlStrAndApply, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.4;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        pnlCommonFilter.add(pnlStringInput, gridBagConstraints);

        pnlAndOr.setLayout(new java.awt.BorderLayout());

        pnlAndOr.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 0, 51), 1, true), "Conditions", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial Narrow", 0, 16)));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0), "Join", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Arial Narrow", 0, 16)));
        bgAndOr.add(rbAnd);
        rbAnd.setSelected(true);
        rbAnd.setText("And");
        rbAnd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbAndActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(rbAnd, gridBagConstraints);

        bgAndOr.add(rbOr);
        rbOr.setText("Or");
        rbOr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbOrActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(rbOr, gridBagConstraints);

        pnlAndOr.add(jPanel3, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.4;
        pnlCommonFilter.add(pnlAndOr, gridBagConstraints);

        pnlRightSide.setLayout(new java.awt.GridBagLayout());

        pnlRangeSlider.setLayout(new java.awt.BorderLayout());

        pnlRangeSlider.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(51, 0, 51)), "Condition-2: Specify Range Filter", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Narrow", 0, 16)));
        pnlRangeSlider.add(sldDummy, java.awt.BorderLayout.CENTER);

        pnlRangeSlider.add(jPanel1, java.awt.BorderLayout.SOUTH);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.6;
        gridBagConstraints.weighty = 0.4;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        pnlRightSide.add(pnlRangeSlider, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.weighty = 0.4;
        pnlCommonFilter.add(pnlRightSide, gridBagConstraints);

        add(pnlCommonFilter, java.awt.BorderLayout.CENTER);

    }// </editor-fold>//GEN-END:initComponents

    private void cboColumnsListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboColumnsListActionPerformed
// TODO add your handling code here:
        installFilterUI();
        
    }//GEN-LAST:event_cboColumnsListActionPerformed

    private void tfSrcStrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfSrcStrActionPerformed
        // TODO add your handling code here:
        applyFilerAction.setEnabled(true);
}//GEN-LAST:event_tfSrcStrActionPerformed

    private void rbSearchStringActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbSearchStringActionPerformed
        // TODO add your handling code here:
        CardLayout cl = (CardLayout) pnlStrRegExExplaination.getLayout();
        cl.show(pnlStrRegExExplaination, "StrSrch");
        applyFilerAction.setEnabled(true);
    }//GEN-LAST:event_rbSearchStringActionPerformed

    private void rbSearchRegExActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbSearchRegExActionPerformed
        // TODO add your handling code here:
        CardLayout cl = (CardLayout) pnlStrRegExExplaination.getLayout();
        cl.show(pnlStrRegExExplaination, "RegEx");
        applyFilerAction.setEnabled(true);
    }//GEN-LAST:event_rbSearchRegExActionPerformed

    private void butApplyPatternFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butApplyPatternFilterActionPerformed
    // TODO add your handling code here:
        
    }//GEN-LAST:event_butApplyPatternFilterActionPerformed

    private void rbAndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbAndActionPerformed
        // TODO add your handling code here:
        applyFilerAction.setEnabled(true);
    }//GEN-LAST:event_rbAndActionPerformed

    private void rbOrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbOrActionPerformed
        // TODO add your handling code here:
        applyFilerAction.setEnabled(true);
    }//GEN-LAST:event_rbOrActionPerformed

    private void rbIncludeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbIncludeActionPerformed
        // TODO add your handling code here:
        applyFilerAction.setEnabled(true);
    }//GEN-LAST:event_rbIncludeActionPerformed

    private void rbExcludeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbExcludeActionPerformed
        // TODO add your handling code here:
        applyFilerAction.setEnabled(true);
    }//GEN-LAST:event_rbExcludeActionPerformed

    /** Listens to the Changes in the Table Columns visibility.
     * Possible change are Addition and Removal of Column.
     *      Filter acts only on the active Visible Columns.
     */
    public void propertyChange(PropertyChangeEvent evt) {
        //        System.out.println("Event Received: "+evt.getPropertyName());
        if (evt.getPropertyName().compareTo(COLUMNS_REMOVED_ALL) == 0) {
            noticeAllColumnsRemoved();
        } else if (evt.getPropertyName().compareTo(COLUMN_MUTABLE_ADDING) == 0 || evt.getPropertyName().compareTo(COLUMN_READ_ONLY_ADDED) == 0) {
            noticeColumnAdded((TableColumn) evt.getNewValue());
        } else if (evt.getPropertyName().compareTo(COLUMN_MUTABLE_REMOVED) == 0) {
            noticeColumnRemoved((TableColumn) evt.getNewValue());
        } else if (evt.getPropertyName().compareTo(COLUMN_VISIBLITY_CHANGED) == 0) {
            noticeColumnVisibilityChanged((TableColumn) evt.getNewValue());
        }

        //  Make combo model reflect the underlying change...
        prepareComboModel();

    //  We need to keep track of all the columns with in the table, at model level...
//        if( evt.getPropertyName().compareTo( NEW_COLUMN_CREATED) ==0)
//            adjustTblMdlIdx2CES( (TableColumn) evt.getNewValue());
    }

    private void noticeAllColumnsRemoved() {
        reset();
    }

    private void noticeColumnAdded(TableColumn tableColumn) {
        visibleCols.add(tableColumn);
        ColumnExtraState ces = (ColumnExtraState) tableColumn.getIdentifier();
        //  if there is no previous instance of Slider, and we have non-null data THEN,
        // create a new slider and keep reference to it in CES.
        if (null == ces.cargoRef_1 && ces.getSampleSortedValues().size() > 1) {
            BiSlider bs = new BiSlider(new Vector(ces.getSampleSortedValues()));
            bs.setOpaque( true);
            bs.setBackground( Color.WHITE);
            ((ColumnExtraState) tableColumn.getIdentifier()).cargoRef_1 = bs;
        }
        //  update for internal reference from filter...
        adjustTblMdlIdx2CES(tableColumn);
    }

    private void noticeColumnRemoved(TableColumn tableColumn) {
        visibleCols.remove(tableColumn);
    }

    private void noticeColumnVisibilityChanged(TableColumn tableColumn) {
        //  get reference of Column and Visibility Status...
        ColumnExtraState ces = (ColumnExtraState) tableColumn.getIdentifier();

        //  Act as per visibility requirement...
        if (ces.isColVisible()) {
            visibleCols.add(tableColumn);
        } else {
            visibleCols.remove(tableColumn);
        }
    }

    private void noticeBoundsChanged(Object minBound, Object maxBound) {
    //  set Appropriate ColumnExtraState...
    }

    /** */
    public void sorterChanged(RowSorterEvent e) {
        System.out.println("Detected RowSorterEvent: " + e.getType());
    //        prepareTblMdlIdx2CES();
    }


    /**     This picks up the required pattern filter settings from the UI, and resets the filter on the data table.    */
    void applyPatternFilter() {
        //  Update associated CES, to reflect new flter settings...
        DefaultPatternFilter pf = activeCES.getPatternFilterModel();
        if (rbSearchRegEx.isSelected()) {
            pf.setRegularExpFilter(tfSrcStr.getText());
        }
        if (rbSearchString.isSelected()) {
            pf.setSearchStringFilter(tfSrcStr.getText(), rbInclude.isSelected());
        }
        activeCES.joinFilterByAnd = rbAnd.isSelected();

        //  update UI...
        reApplyFilter();
    }

    void reApplyFilter() {
        EventQueue.invokeLater(new Runnable() {
                public void run() {
                    System.out.println("Setting Filter: "+universalFilter);
                    ((TableRowSorter) tblData.getRowSorter()).setRowFilter(universalFilter);
                }
                });
    }


    /**     Based on the User's active Selection, shows a relevant Filter.   */
    private void installFilterUI() {
        int selIdx = cboColumnsList.getSelectedIndex();
        TableColumn tc = namesModel.getTableColumnAt(selIdx);
        ColumnExtraState ces = (ColumnExtraState) tc.getIdentifier();

        //  required by combo lister to set bounds the relevant ces...
        activeCES = ces;
        synPaternFilterUIWithModel();
        synRangeFilterUIWithModel();

        //  Reflect...
        pnlStringInput.revalidate();
        pnlStringInput.repaint();
        pnlRangeSlider.revalidate();
        pnlRangeSlider.repaint();
    }

    /**     Render the Pattern-UI-subdisplay, to match with selected Column in Combo, i.e. as per activeCES */
    void synPaternFilterUIWithModel() {
        PatternFilterModel pf = null;
        if (!activeCES.isPatternFilterModelExists()) {
            //  pick default values...
            pf = DefaultPatternFilter.getDefault();
        } else //  pick actuals...
        {
            pf = activeCES.getPatternFilterModel();
        }

        //  update UI...
        rbSearchRegEx.setSelected(pf.isRegExp());
        rbSearchString.setSelected(!pf.isRegExp());
        rbAnd.setSelected(activeCES.joinFilterByAnd);
        rbOr.setSelected(!activeCES.joinFilterByAnd);
        rbInclude.setSelected(pf.isStrIncludeTypeFilter());
        rbExclude.setSelected(!pf.isStrIncludeTypeFilter());
        tfSrcStr.setText(pf.getUserSpecifiedStr());
    }

    /**     Update Visible Rangle Slider, as per column selected.   */
    void synRangeFilterUIWithModel() {
        if (pnlRangeSlider.getComponentCount() > 0) {
            //  Detach listner from other Combos...
            Component c = pnlRangeSlider.getComponent(0);
            if (c instanceof BiSlider) {
                ((BiSlider) c).removePropertyChangeListener(rangeListener);
            }
            pnlRangeSlider.removeAll();
        }

        BiSlider slider = (BiSlider) activeCES.cargoRef_1;
        if (null == slider) {
            //  This column has no associated column with it, so repaint parent to show empty backgroubd...
            pnlRangeSlider.repaint();
            return;
        }

        //  Listnes for changes in this Combo...
        slider.addPropertyChangeListener(rangeListener);
        pnlRangeSlider.add(slider);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgAndOr;
    private javax.swing.ButtonGroup bgIncludeExclude;
    private javax.swing.ButtonGroup bgSearchType;
    private javax.swing.JButton butApplyPatternFilter;
    private javax.swing.JComboBox cboColumnsList;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JLabel lblStringInput;
    private javax.swing.JPanel pnlAndOr;
    private javax.swing.JPanel pnlCommonFilter;
    private javax.swing.JPanel pnlRangeSlider;
    private javax.swing.JPanel pnlRegExDetails;
    private javax.swing.JPanel pnlRightSide;
    private javax.swing.JPanel pnlSearchType;
    private javax.swing.JPanel pnlSelectColumn;
    private javax.swing.JPanel pnlStrAndApply;
    private javax.swing.JPanel pnlStrRegExExplaination;
    private javax.swing.JPanel pnlStrSrchDetails;
    private javax.swing.JPanel pnlStringInput;
    private javax.swing.JRadioButton rbAnd;
    private javax.swing.JRadioButton rbExclude;
    private javax.swing.JRadioButton rbInclude;
    private javax.swing.JRadioButton rbOr;
    private javax.swing.JRadioButton rbSearchRegEx;
    private javax.swing.JRadioButton rbSearchString;
    private javax.swing.JSlider sldDummy;
    private javax.swing.JTextField tfSrcStr;
    // End of variables declaration//GEN-END:variables

    /**     Listens to the Slider. Appropriate bounds are set on the respective column
     *              as per new bounds selected by the user..  */
    class RangeSliderListener implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {

            if (evt.getPropertyName().compareTo(BiSlider.EVENT_RANGE_CHANGED) == 0) {
                //  Slider has changes, so change bonds in associated ColumnExtraState...
                activeCES.setBounds((Comparable) evt.getOldValue(), (Comparable) evt.getNewValue());

                //                System.out.println("While Preparing Sort Handler: ");
//                for( int colIdx=0; colIdx<tblData.getColumnCount(); colIdx++) {
//                    String hdr = tblData.getColumnModel().getColumn( colIdx).getHeaderValue().toString();
//                    ColumnExtraState ces = (ColumnExtraState) tblData.getColumnModel().getColumn( colIdx).getIdentifier();
//                    System.out.println(">"+colIdx+": "+hdr+" ces:"+ces.getColumnNickName()+",  val="+tblData.getValueAt( 0, colIdx));
//                }

                //  Reflect changes...
                EventQueue.invokeLater(new Runnable() {

                        public void run() {
                            //                        System.out.println("BEFORE Invoking Sort: ");
//                        for( int colIdx=0; colIdx<tblData.getColumnCount(); colIdx++) {
//                            String hdr = tblData.getColumnModel().getColumn( colIdx).getHeaderValue().toString();
//                            ColumnExtraState ces = (ColumnExtraState) tblData.getColumnModel().getColumn( colIdx).getIdentifier();
//                            System.out.println(">"+colIdx+": "+hdr+" ces:"+ces.getColumnNickName()+",  val="+tblData.getValueAt( 0, colIdx));
//                        }
                            ((TableRowSorter) tblData.getRowSorter()).setRowFilter(universalFilter);
                        }
                    });
            //                disableComboBoxListeners();
//                enableComboBoxListeners();
            }
        }
    }

    class ColNamesComboBoxModel extends DefaultComboBoxModel {
        /**    @ return:
         *                  Column name and Filter Settings as String;
         *                  instead of Object's class version of toString(). */

        @Override
        public Object getElementAt(int index) {
            TableColumn tc = (TableColumn) super.getElementAt(index);
            ColumnExtraState ces = (ColumnExtraState) tc.getIdentifier();
            if (ces.getFilterDescription().length() == 0) {
                return tc.getHeaderValue();
            } else //  Show filter only if one is in effect...
            {
                return tc.getHeaderValue() + "  (" + ces.getFilterDescription() + ")";
            }
        }

        public TableColumn getTableColumnAt(int index) {
            return (TableColumn) super.getElementAt(index);
        }

        public void fireItemChanged(int idx) {
            if (idx < getSize()) {
                fireContentsChanged(this, idx, idx);
            }
        }
    }

    private class UniversalFilter extends RowFilter<TableModel, Integer> {

        public boolean include(RowFilter.Entry<? extends TableModel, ? extends Integer> entry) {

            //  Check if cells of this rows, pass thier recpective column-filters...
            int rowIdx = entry.getIdentifier().intValue();

            //  For each ColumnChk if filter condition is passed...
            for (int colIdx = 0; colIdx < entry.getValueCount(); colIdx++) {
                //  Chk if there are any visible columns...
                if (tblModelIdx2CES.size() <= 0) {
                    //  unconditionally include everything...
                    return true;
                }

                ColumnExtraState ces = tblModelIdx2CES.get(colIdx);
                if (!ces.isFilterActive()) {
                    //  No Filter ser for this column: Skip this column, assume filter condition passed.
                    continue;
                }

                Object value = entry.getValue(colIdx);

                if (!includeCell(ces, value)) {
                    //  Some cell in this row disqualified, so do NOT include this row...
                    return false;
                }
            }

            //  If reached Here: no cell value has violated the filter condition, so include this row...
            return true;
        }

        private boolean includeCell(ColumnExtraState ces, Object value) {

            //  Perform Range Filter ...
            boolean rangeFilterInclude = true;
            if (value instanceof Comparable) {
                rangeFilterInclude = (null == ces.getMinBound() || ces.getMinBound().compareTo(value) <= 0) &&
                    (null == ces.getMaxBound() || ces.getMaxBound().compareTo(value) >= 0);
            } else {
                //  use some alternative comparision method, Right NOW include any how, with warning...
                rangeFilterInclude = true;
                if( null != value){
                    //  [WARNING]: A Non-Null value that cannot be compared...
                    System.out.println(getClass().getName() + ": [WARNING]: Range Filter Failed on value=" + value + ", because, it is NOT comparable." +
                        "\nFilteration may NOT be correct.");
                }
            }

            //  Perform Pattern Filter...
            boolean patternFilterInclude = true;
            if (ces.isPatternFilterModelExists()) {
                DefaultPatternFilter pf = ces.getPatternFilterModel();
                String strVal = "";
                if( null != value)
                    strVal = value.toString();
                patternFilterInclude = pf.include( strVal);
            }

            if (ces.joinFilterByAnd) {
                return rangeFilterInclude && patternFilterInclude;
            } else {
                return rangeFilterInclude || patternFilterInclude;
            }
        }
    }

    private class RangeFilter2 extends RowFilter<TableModel, Integer> {

        public boolean include(RowFilter.Entry<? extends TableModel, ? extends Integer> entry) {

            //  Check if cells of this rows, pass thier recpective column-filters...
            int rowIdx = entry.getIdentifier().intValue();

            //            System.out.println("tblData.getColumnCount() ="+tblData.getColumnCount()+",  entry.getValueCount() ="+entry.getValueCount());
//            for( int colIdx=0; colIdx<tblData.getColumnCount(); colIdx++) {
//                String hdr = tblData.getColumnModel().getColumn( colIdx).getHeaderValue().toString();
//                ColumnExtraState ces = (ColumnExtraState) tblData.getColumnModel().getColumn( colIdx).getIdentifier();
//                System.out.println("include():TM> "+colIdx+": "+hdr+" / "+ces.getColumnNickName()+",  val="+tblData.getValueAt( rowIdx, colIdx));
//            }
//
//            for( int colIdx=0; colIdx<entry.getValueCount(); colIdx++) {
//                ColumnExtraState ces = tblModelIdx2CES.get( colIdx);
//                System.out.println("include():RF> "+colIdx+": RF.CES:"+ces.getColumnNickName()+",  val="+entry.getValue( colIdx));
//            }

            for (int colIdx = 0; colIdx < entry.getValueCount(); colIdx++) {
                if (tblModelIdx2CES.size() <= 0) //  unconditionally include everything...
                {
                    return true;
                }

                ColumnExtraState ces = tblModelIdx2CES.get(colIdx);
                if (!ces.isFilterActive()) //  Skip this column, assume filter condition passed.
                {
                    continue;
                }

                Object value = entry.getValue(colIdx);
                //                Object value = tblData.getValueAt( rowIdx, colIdx);
                if (value instanceof Comparable) {
                    if (null != ces.getMinBound() && ces.getMinBound().compareTo(value) > 0) //  condition failed on this cell/col, value less than specified lower bound, no need to chk further...
                    {
                        return false;
                    }
                    if (null != ces.getMaxBound() && ces.getMaxBound().compareTo(value) < 0) //  condition failed on this cell/col, value greater than specified upper bound, no need to chk further...
                    {
                        return false;
                    }

                    //  if reached here, this cell has passed condition laid on this column...
                    continue;
                } else //  use some alternative comparision method...
                {
                    System.out.println(getClass().getName() + ": [WARNING]: Range Filter Failed on value=" + value + ", because, it is NOT comparable." +
                        "\nFilteration may NOT be correct.");
                }
            }

            //  If reached Here: no cell value has violated the filter condition, so include this row...
            return true;
        }
    }


    /**     Actions that contros the Appy Buttons. 
     * It also decides, when it is NOT appropriate for Apply button to be in enabled state, 
     * by rejecting setEnabled( true) requests.     */
    class ApplyFilterAction extends AbstractAction {

        ApplyFilterAction() {
            super("Apply");
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            applyPatternFilter();
            setEnabled(false);
        }

        @Override
        public void setEnabled(boolean isEnabled) {
//            System.out.println("Enabled Request: "+isEnabled+"tfSrcStr ="+tfSrcStr+", tfSrcStr.getText()="+(null==tfSrcStr?"NPEx":tfSrcStr.getText()));
            
            if (!isEnabled) {
                //  Allow disable, without any issues...
                super.setEnabled(false);
                return;
            }

            String usrInput = tfSrcStr.getText();
            /*
            // requesting enabled, chk if it is possible...
            if (null == usrInput || usrInput.length() <= 0) {
                //  No user input...
                super.setEnabled(false);
                return;
            }
             */

            if ( rbSearchString.isSelected()) {
                //  allow...
                super.setEnabled(true);
                return;
            }

            try {
                //  if here, we are performing reg-ex search...
                Pattern.compile(usrInput);
                super.setEnabled(true);
            } catch (PatternSyntaxException ex) {
                //  Bad RegEx...
                super.setEnabled(false);
                System.out.println("Bad Reg Ex: "+ex);
                firePropertyChange( USER_WARNING, null, "Bad Reg Ex: "+ex);
            }
        }
    }
    
    /**     If user has changes his/her input, we need to chk if we can enable Apply button.    */
    class UserInputStingListener implements DocumentListener{

        public void insertUpdate(DocumentEvent e) {
            applyFilerAction.setEnabled(true);
        }

        public void removeUpdate(DocumentEvent e) {
            applyFilerAction.setEnabled(true);
        }

        public void changedUpdate(DocumentEvent e) {
            applyFilerAction.setEnabled(true);
        }
        
    }
            
}
