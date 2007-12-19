/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wustl.cab2b.client.ui.controls.sheet;

import java.beans.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.JComponent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 /** Table Model Impl to accept and keep Columns Visibility Settings.
 * 1:   Column Number.
 * 2:   Column Name
 * 3:   Column Visibility Settings
 * 4:   Column Filter Settings
 *
 * @author jasbir_sachdeva
 */
class SheetCustomizationModel extends AbstractTableModel {

    private ArrayList<SheetColumn> scRowInfoAL = new ArrayList<SheetColumn>();

    private PropertyChangeSupport pcs;

    protected SheetColumnListener lsnSheetColumns = new SheetColumnListener();

    private int activeFiltersCount = 0;

    UniversalFilter filterTable = new UniversalFilter();

    HashMap<Integer, Integer> view2ModelFilterIdxMap = new HashMap<Integer, Integer>(20);

    SheetCustomizationModel() {
        pcs = new PropertyChangeSupport(this);
    }

    /**     Count Number of active filters in associasted SheetColumn */
    int getActiveFiltersCount() {
        return activeFiltersCount;
    }

    void recomputeActiveFiltersCount() {
        activeFiltersCount = 0;
        int modelIdxCount = 0;
        view2ModelFilterIdxMap.clear();
        for (SheetColumn colSheet : scRowInfoAL) {
            if (colSheet.getFilterCondition().isFilterActive()) {
                view2ModelFilterIdxMap.put(activeFiltersCount++, modelIdxCount);
            }
            modelIdxCount++;
        }
    }

    SheetColumn getActiveSheetColumnAt(int index) {
        int idxModel = view2ModelFilterIdxMap.get(index).intValue();
        return scRowInfoAL.get(idxModel);
    }

    ColumnFilterModel getActiveFilterAt(int index) {
        int idxModel = view2ModelFilterIdxMap.get(index).intValue();
        return scRowInfoAL.get(idxModel).getFilterCondition();
    }

    ArrayList<SheetColumn> getRowInfoAL() {
        return scRowInfoAL;
    }

    RowFilter getTableFilter() {
        return filterTable;
    }

    void requestResetAll() {
        pcs.firePropertyChange(PropertiesNames.REQUEST_RESET_ALL, 0, 1);
    }

    /** SheetCustomizationModel listerns to all the associated SheetColuns for any Changes.
     * All such change events are also propogated, to any body interested in listening to me.
     * @param scRowInfoAL   
     *      Participating SheetColumns instances for association between SheetCustomizationModel and SheetColumns
     */
    void setRowInfos(ArrayList<SheetColumn> scRowInfoAL) {
        for (SheetColumn col : this.scRowInfoAL) {
            col.removePropertyChangeListener(lsnSheetColumns);
        }
        //  Reset...
        this.scRowInfoAL = scRowInfoAL;
        activeFiltersCount = 0;
        for (SheetColumn col : this.scRowInfoAL) {
            col.addPropertyChangeListener(lsnSheetColumns);
        }
    }

    /**     Return SheetInfo instance attached to given index.*/
    SheetColumn getSheetInfo(int modelIndex) {
        return scRowInfoAL.get(modelIndex);
    }

    /**     Call to this will accomodate new "SheetColumn" in model.
     It is tentative because the underlying model column index is still NOT finalized on "col". 
     *          Therefore, this model CANNOT fire Model change event to its listeners as of now.
     * @param col   
     *  New SheetColumn, representing a new column in Data View .
     */
    void appendRowInfo(SheetColumn col) {
        //  Just to make sure...
        col.removePropertyChangeListener(lsnSheetColumns);
        scRowInfoAL.add(col);
        col.addPropertyChangeListener(lsnSheetColumns);
    }

    public int getRowCount() {
        return scRowInfoAL.size();
    }

    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "#";
            case 1:
                return "Name";
            case 2:
                return "Visible?";
            case 3:
                return "Filter";
        }
        return null;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Object.class;
            case 1:
                return String.class;
            case 2:
                return Boolean.class;
            case 3:
                return JComponent.class;
        }
        return Object.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return false;
            case 1:
                return false;
            case 2:
                return true;
            case 3:
                return true;
        }
        return false;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        SheetColumn sheetCol = scRowInfoAL.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return sheetCol.getModelIndex();
            case 1:
                return sheetCol.getIdentifier();
            case 2:
                return sheetCol.isVisible();
            case 3:
                return sheetCol.getFilterCondition();
        }
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        SheetColumn sheetCol = scRowInfoAL.get(rowIndex);
        switch (columnIndex) {
            case 2:
                sheetCol.setVisible((Boolean) aValue);
                return;
            case 3:
                //  Do nothing, as Model is directly updated by the editor component.
                return;
        }
    }

    void requestAddUserColumn() {
        pcs.firePropertyChange(PropertiesNames.USER_COLUMN_ADDITION_REQUESTED, 0, 1);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        pcs.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        pcs.removePropertyChangeListener(pcl);
    }

    protected class SheetColumnListener implements PropertyChangeListener {

        /**  Keeps continous Track of Changes in associated RowInfoAL...  */
        public void propertyChange(PropertyChangeEvent evt) {

            //  Relay Changes...
            pcs.firePropertyChange(evt);
            recomputeActiveFiltersCount();
        }
    }

    class UniversalFilter extends RowFilter<TableModel, Integer> {

        public boolean include(RowFilter.Entry<? extends TableModel, ? extends Integer> entry) {

            //  Check if cells of this rows, pass thier recpective column-filters...
            int rowIdx = entry.getIdentifier().intValue();
            //            System.out.println("[UniversalFilter]: Include Check fot Row: "+rowIdx+". ");

            //  For each ColumnChk if filter condition is passed...
            for (int colIdx = 0; colIdx < entry.getValueCount(); colIdx++) {
                //  Chk if there are any visible columns...
                if (scRowInfoAL.size() <= 0) {
                    //  unconditionally include everything...
                    return true;
                }

                SheetColumn colSheet = scRowInfoAL.get(colIdx);
                if (!colSheet.isVisible()) //  Ignore the filter on the columns that are invisible...
                {
                    return true;
                }

                ColumnFilterModel mdlColFilter = colSheet.getFilterCondition();
                Object value = entry.getValue(colIdx);

                if (!mdlColFilter.includeCell((Comparable) value)) {
                    //  Some cell in this row disqualified, so do NOT include this row...
                    return false;
                }
            }

            //  If reached Here: no cell value has violated the filter condition, so include this row...
            return true;
        }
    }
}
