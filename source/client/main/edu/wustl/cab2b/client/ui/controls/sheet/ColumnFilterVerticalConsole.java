package edu.wustl.cab2b.client.ui.controls.sheet;

/*
 * ColumnFilterVerticalConsole.java
 *
 * Created on November 27, 2007, 10:13 AM
 */
import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.AbstractListModel;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.wustl.cab2b.client.ui.controls.slider.BiSlider;

/**
 *
 * 1) This component is tightly bounded to its associated Model 
 *      - i.e. all the state should be written in Model, NOT in UI/Veiwer.
 * 2) Support for dynamic change of Model is required, i.e. setModel( ...).
 * 3) Components canges by uses, should be reflected in model.
 * 
 * @author  jasbir_sachdeva
 */
public class ColumnFilterVerticalConsole extends javax.swing.JPanel {

    ColumnFilterModel model;

    ModelListener modelListener = new ModelListener();

    private ListItemSelectionListener listSelectionListener = new ListItemSelectionListener();

    private PatternPropertyChangeListener patternChangeListener = new PatternPropertyChangeListener();

    private RangeChangeListener rangePropertyChangeListener = new RangeChangeListener();

    ColumnFilterModel emptyModel = new ColumnFilterModel();

    //    protected Comparable currMaxBounds;
    //    protected Comparable currMinBounds;
    //    private RangeSliderListener rangeListener = new RangeSliderListener();
    /** Creates new form ColumnFilterVerticalConsole */
    public ColumnFilterVerticalConsole() {
        initComponents();
        model = emptyModel;
        showNullModelNotification(true);
        uninstallFilter();
        Common.setBackgroundWhite(pnlVerticalLayout);
        Common.setBackgroundWhite(pnlPattern);
        Common.setBackgroundWhite(scpList);
        Common.setBackgroundWhite(lblNullModel);
        Common.setBackgroundWhite(pnlFilterControlContainer);
    }

    public void changeOrientationToHorizontal() {
        removeAll();
        initComponentsHoriLayout(pnlHorizontalLayout);
        add(pnlHorizontalLayout, BorderLayout.CENTER);
        Common.setBackgroundWhite(pnlHorizontalLayout);
        revalidate();
        repaint();
    }

    public void uninstallControlListeners(ColumnFilterModel oldFilterModel) {
        lstValues.removeListSelectionListener(listSelectionListener);
        tfPattern.getDocument().removeDocumentListener(patternChangeListener);
        oldFilterModel.getRangerComponent().removePropertyChangeListener(rangePropertyChangeListener);
    }

    public void installControlListeners() {
        lstValues.addListSelectionListener(listSelectionListener);
        tfPattern.getDocument().addDocumentListener(patternChangeListener);
        model.getRangerComponent().addPropertyChangeListener(rangePropertyChangeListener);
    }

    void showNullModelNotification(boolean showNullNotification) {
        removeAll();
        if (showNullNotification) {
            add(lblNullModel, BorderLayout.CENTER);
        } else {
            add(pnlVerticalLayout, BorderLayout.CENTER);
        }
    }

    public void setModel(ColumnFilterModel filterModel) {
        showNullModelNotification(null == filterModel);
        if (filterModel == null) {
            filterModel = emptyModel;
        }
        uninstallControlListeners(filterModel);

        //Settings listeners...
        model.removePropertyChangeListener(modelListener);
        model = filterModel;
        model.addPropertyChangeListener(modelListener);
        //        setHeader(filterModel.toString());

        //  Set radio...
        if (model.getActiveFilterType().equals(ColumnFilterModel.FILTER_TYPE_NONE)) {
            rbNoFilter.setSelected(true);
            installCentralComponent(lblNoFilter);
            butApplyFilter.setEnabled(true);
        } else {

            if (model.getActiveFilterType().equals(ColumnFilterModel.FILTER_TYPE_ENUMERATION)) {
                rbList.setSelected(true);
                installCentralComponent(scpList);
            }

            if (model.getActiveFilterType().equals(ColumnFilterModel.FILTER_TYPE_PATTERN)) {
                rbPattern.setSelected(true);
                installCentralComponent(pnlPattern);
            }

            //  Make BiSilder pick range selection from existing model values, so we need a new instance of it...
            model.getNewRangerComponent();
            if (model.getActiveFilterType().equals(ColumnFilterModel.FILTER_TYPE_RANGE)) {
                rbRange.setSelected(true);
                installCentralComponent(model.getRangerComponent());
            }

            butApplyFilter.setEnabled(false);
        }

        //  set individual filiter control...
        syncListModel();
        tfPattern.setText(model.getPatternFilter());
        syncBiSlider();
        revalidate();
        repaint();
        installControlListeners();
    }

    private void syncListModel() {
        EnumeratedValuesListModel mdlValues = new EnumeratedValuesListModel();
        mdlValues.setPossibleValues(model.getSampleSortedValues());

        lstValues.setModel(mdlValues);
        // Select all usrSelected Items for Filter...
        if (model.getFilterValues() != null) {
            lstValues.setSelectedIndices(model.getFilterValues());
        }
        lstValues.invalidate();
    }

    private void syncBiSlider() {

    }

    private void enableAppplyButton() {
        butApplyFilter.setEnabled(true);
    }

    class ListItemSelectionListener implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {
            enableAppplyButton();
        }
    }

    class PatternPropertyChangeListener implements DocumentListener {

        public void changedUpdate(DocumentEvent e) {
            enableAppplyButton();
        }

        public void insertUpdate(DocumentEvent e) {
            enableAppplyButton();
        }

        public void removeUpdate(DocumentEvent e) {
            enableAppplyButton();
        }
    }

    class RangeChangeListener implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            enableAppplyButton();
        }
    }

    /** ColumnFilterModel keeps user selection in some collection or Set, I do NOT really care about that.
     Because one setUserSelection(...) is specified on me, I can convert the collection in to List Model
     to present a LIst to user...    */
    class EnumeratedValuesListModel extends AbstractListModel {

        ArrayList alPossibleValues;

        public void setPossibleValues(ArrayList tsUsrSelection) {
            alPossibleValues = tsUsrSelection;
        }

        public int getSize() {
            return null == alPossibleValues ? 0 : alPossibleValues.size();
        }

        public Object getElementAt(int index) {
            return alPossibleValues.get(index);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblNullModel = new javax.swing.JLabel();
        bgVV = new javax.swing.ButtonGroup();
        lblNoFilter = new javax.swing.JLabel();
        lblPleaseWait = new javax.swing.JLabel();
        scpList = new javax.swing.JScrollPane();
        lstValues = new javax.swing.JList();
        pnlPattern = new javax.swing.JPanel();
        tfPattern = new javax.swing.JTextField();
        pnlHorizontalLayout = new javax.swing.JPanel();
        pnlVerticalLayout = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        pnlVVColumnName = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblColName = new javax.swing.JLabel();
        pnlVVSelectFilter = new javax.swing.JPanel();
        rbPattern = new javax.swing.JRadioButton();
        rbRange = new javax.swing.JRadioButton();
        rbList = new javax.swing.JRadioButton();
        rbNoFilter = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        pnlFilterControlContainer = new javax.swing.JPanel();
        pnlApply = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        butApplyFilter = new javax.swing.JButton();

        lblNullModel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNullModel.setText("Please Select a Column"); // NOI18N
        lblNullModel.setOpaque(true);

        lblNoFilter.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNoFilter.setText("No Filter on Column"); // NOI18N

        lblPleaseWait.setFont(new java.awt.Font("Arial", 0, 14));
        lblPleaseWait.setForeground(new java.awt.Color(0, 255, 204));

        lstValues.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        scpList.setViewportView(lstValues);

        pnlPattern.setLayout(new java.awt.GridBagLayout());

        tfPattern.setText(null);
        tfPattern.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfPatternActionPerformed(evt);
            }
        });
        tfPattern.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfPatternFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 20);
        pnlPattern.add(tfPattern, gridBagConstraints);

        setFocusCycleRoot(true);
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                formFocusLost(evt);
            }
        });
        setLayout(new java.awt.BorderLayout());

        pnlVerticalLayout.setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        pnlVVColumnName.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 5, 0, new java.awt.Color(255, 255, 255)));
        pnlVVColumnName.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("  Filter on Column:"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        pnlVVColumnName.add(jLabel1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.8;
        pnlVVColumnName.add(lblColName, gridBagConstraints);

        jPanel1.add(pnlVVColumnName, java.awt.BorderLayout.NORTH);

        pnlVVSelectFilter.setLayout(new java.awt.GridBagLayout());

        bgVV.add(rbPattern);
        rbPattern.setText("Pattern"); // NOI18N
        rbPattern.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 3, 0, 1));
        rbPattern.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbPatternActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlVVSelectFilter.add(rbPattern, gridBagConstraints);

        bgVV.add(rbRange);
        rbRange.setText("Range"); // NOI18N
        rbRange.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 3, 0, 1));
        rbRange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbRangeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlVVSelectFilter.add(rbRange, gridBagConstraints);

        bgVV.add(rbList);
        rbList.setSelected(true);
        rbList.setText("List"); // NOI18N
        rbList.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 3, 0, 1));
        rbList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbListActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlVVSelectFilter.add(rbList, gridBagConstraints);

        bgVV.add(rbNoFilter);
        rbNoFilter.setSelected(true);
        rbNoFilter.setText("No Filter"); // NOI18N
        rbNoFilter.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 3, 0, 1));
        rbNoFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbNoFilterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        pnlVVSelectFilter.add(rbNoFilter, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        pnlVVSelectFilter.add(jLabel2, gridBagConstraints);

        jPanel1.add(pnlVVSelectFilter, java.awt.BorderLayout.SOUTH);

        pnlVerticalLayout.add(jPanel1, java.awt.BorderLayout.NORTH);

        pnlFilterControlContainer.setBorder(javax.swing.BorderFactory.createMatteBorder(3, 0, 3, 0, new java.awt.Color(255, 255, 255)));
        pnlFilterControlContainer.setLayout(new java.awt.BorderLayout());
        pnlVerticalLayout.add(pnlFilterControlContainer, java.awt.BorderLayout.CENTER);

        pnlApply.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.8;
        pnlApply.add(jLabel3, gridBagConstraints);

        butApplyFilter.setText("Apply"); // NOI18N
        butApplyFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butApplyFilterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 4, 13);
        pnlApply.add(butApplyFilter, gridBagConstraints);

        pnlVerticalLayout.add(pnlApply, java.awt.BorderLayout.SOUTH);

        add(pnlVerticalLayout, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void rbRangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbRangeActionPerformed
        // TODO add your handling code here:
        installSlider();
    }//GEN-LAST:event_rbRangeActionPerformed

    private void rbPatternActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbPatternActionPerformed
        // TODO add your handling code here:
        installPattern();
    }//GEN-LAST:event_rbPatternActionPerformed

    private void rbListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbListActionPerformed
        // TODO add your handling code here:
        installList();
    }//GEN-LAST:event_rbListActionPerformed

    private void rbNoFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbNoFilterActionPerformed
        // TODO add your handling code here:
        uninstallFilter();
    }//GEN-LAST:event_rbNoFilterActionPerformed

    private void formFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusLost
        // TODO add your handling c:

    }//GEN-LAST:event_formFocusLost

    private void tfPatternFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfPatternFocusLost
        // TODO add your handling code here:        

    }//GEN-LAST:event_tfPatternFocusLost

    private void butApplyFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butApplyFilterActionPerformed
        applyFilter();

    }//GEN-LAST:event_butApplyFilterActionPerformed

    private void tfPatternActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfPatternActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfPatternActionPerformed

    private void applyFilter() {
        //firePropertyChange(model.filterType, null, null);
        butApplyFilter.setEnabled(false);
        if (rbPattern.isSelected()) {
            model.setPatternFilter(tfPattern.getText());
        } else if (rbRange.isSelected()) {
            BiSlider bsRanger = (BiSlider) model.getRangerComponent();
            model.setRangeFilterBounds(bsRanger.getRangeMinBound(), bsRanger.getRangeMaxBound());
        } else if (rbList.isSelected()) {
            //  Converting array to collections...    
            model.setFilterValues(lstValues.getSelectedIndices());
        } else if (rbNoFilter.isSelected()) {
            model.setNoFilter();
        }

    }

    private void initComponentsHoriLayout(JPanel pnlHoriLayout) {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlHoriLayout.setFocusCycleRoot(true);
        pnlHoriLayout.removeAll();
        pnlHoriLayout.setLayout(new java.awt.BorderLayout());

        JPanel pnlHHSelectFilter = new JPanel();
        pnlHHSelectFilter.setLayout(new java.awt.GridBagLayout());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlHHSelectFilter.add(rbPattern, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlHHSelectFilter.add(rbRange, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlHHSelectFilter.add(rbList, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        pnlHHSelectFilter.add(jLabel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlHHSelectFilter.add(rbNoFilter, gridBagConstraints);

        pnlHoriLayout.add(pnlHHSelectFilter, java.awt.BorderLayout.WEST);
        pnlHoriLayout.add(pnlFilterControlContainer, java.awt.BorderLayout.CENTER);
    }

    void installCentralComponent(JComponent comp) {
        pnlFilterControlContainer.removeAll();
        pnlFilterControlContainer.add(comp);
        butApplyFilter.setEnabled(false);
        pnlFilterControlContainer.revalidate();
        pnlFilterControlContainer.repaint();
    }

    void installPattern() {
        installCentralComponent(pnlPattern);
    }

    void installSlider() {
        installCentralComponent(model.getRangerComponent());
    }

    void installList() {
        lstValues.invalidate();
        installCentralComponent(scpList);
    }

    void uninstallFilter() {
        installCentralComponent(lblNoFilter);
        butApplyFilter.setEnabled(true);
    }

    public void setHeader(String strColumnName) {
        if (null == strColumnName || strColumnName.length() == 0) {
            pnlVVColumnName.setVisible(false);
            return;
        }

        pnlVVColumnName.setVisible(true);
        lblColName.setText(strColumnName);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgVV;
    private javax.swing.JButton butApplyFilter;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblColName;
    private javax.swing.JLabel lblNoFilter;
    private javax.swing.JLabel lblNullModel;
    private javax.swing.JLabel lblPleaseWait;
    private javax.swing.JList lstValues;
    private javax.swing.JPanel pnlApply;
    private javax.swing.JPanel pnlFilterControlContainer;
    private javax.swing.JPanel pnlHorizontalLayout;
    private javax.swing.JPanel pnlPattern;
    private javax.swing.JPanel pnlVVColumnName;
    private javax.swing.JPanel pnlVVSelectFilter;
    private javax.swing.JPanel pnlVerticalLayout;
    private javax.swing.JRadioButton rbList;
    private javax.swing.JRadioButton rbNoFilter;
    private javax.swing.JRadioButton rbPattern;
    private javax.swing.JRadioButton rbRange;
    private javax.swing.JScrollPane scpList;
    private javax.swing.JTextField tfPattern;
    // End of variables declaration//GEN-END:variables
    class ModelListener implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals(ColumnFilterModel.FILTER_TYPE_ENUMERATION)) {
                //install proper filter
            }
        }
    }
    //    /**     Listens to the Slider. Reference to Appropriate new bounds are kept in container/parent object.  */
    //    class RangeSliderListener implements PropertyChangeListener {
    //        public void propertyChange(PropertyChangeEvent evt) {
    //            if (evt.getPropertyName().compareTo(BiSlider.EVENT_RANGE_CHANGED) == 0) {
    //                //  Slider has changes, so change bonds in associated ColumnFilterModel...
    //                currMinBounds = (Comparable) evt.getOldValue();
    //                currMaxBounds = (Comparable) evt.getNewValue();
    //            }
    //        }
    //    }
}
