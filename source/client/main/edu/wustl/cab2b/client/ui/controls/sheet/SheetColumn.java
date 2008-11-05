/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wustl.cab2b.client.ui.controls.sheet;

import java.beans.*;
import javax.swing.table.*;
import javax.swing.table.TableColumn;

/**
 *
 * @author jasbir_sachdeva
 */
public class SheetColumn extends TableColumn {

    /**     When ever associated Column Filter Model's bound peroperty changes, This event is fired.    
     * It wraps the original even from ColumnFilterModel is newValue of the passed event-object.    */
    public static final String FILTER_CHANGED = "FILTER_CHANGED";
    
    private Boolean isVisible = true;
    private boolean isUserColumn = false;
    private ColumnFilterModel cfmFilterCondition;
    ColumnFilterChangesListener lsnColFilterModel = new ColumnFilterChangesListener();
    PropertyChangeSupport pcs;


    SheetColumn() {
        super();
        initComponent();
    }

    SheetColumn(int modelIndex) {
        super(modelIndex);
        initComponent();
    }

    SheetColumn(int modelIndex, int width) {
        super(modelIndex, width);
        initComponent();
    }

    SheetColumn(int modelIndex, int width, TableCellRenderer cellRenderer, TableCellEditor cellEditor) {
        super(modelIndex, width, cellRenderer, cellEditor);
        initComponent();
    }
    
    private void initComponent() {
        pcs = new PropertyChangeSupport( this);
    }
    

    /**
     * @return Column Filter Condition
     */
    public ColumnFilterModel getFilterCondition() {
        return cfmFilterCondition;
    }

    /**
     * Set Column Filter Condition
     * @param cfmFilterCondition
     */
    public void setFilterCondition(ColumnFilterModel cfmFilterCondition) {
        if( this.cfmFilterCondition != null)
            this.cfmFilterCondition.removePropertyChangeListener( lsnColFilterModel);
        this.cfmFilterCondition = cfmFilterCondition;
        cfmFilterCondition.addPropertyChangeListener( lsnColFilterModel);
    }

//    @Override
//    public void setModelIndex(int modelIndex) {
//        super.setModelIndex(modelIndex);
//        firePropertyChange
//    }
    
    

    private void accomodateAssociatedColumnFilterModelChanges( PropertyChangeEvent evt) {
        //  Relay Changes, a catch all proeprty, wrap detailed event in newValue...
        pcs.firePropertyChange( FILTER_CHANGED, null, evt);
    }

    /**
     * @return Boolean Value
     */
    public boolean isVisible() {
        return isVisible.booleanValue();
    }
    
    /**
     * @return Boolean Value
     */
    public Boolean getVisible() {
        return isVisible;
    }

    /**
     * Sets Visibility
     * @param isVisible
     */
    public void setVisible(Boolean isVisible) {
        Boolean oldVal = this.isVisible;
        this.isVisible = isVisible;
        pcs.firePropertyChange( Common.COLUMN_VISIBLITY_CHANGE_REQUESTED, 
                null, this);
    }

    /**
     * Checks for User Column
     * @return Boolean Value
     */
    public boolean isUserColumn() {
        return isUserColumn;
    }

    /**
     * Sets User Column
     * @param isUserColumn
     */
    public void setUserColumn(boolean isUserColumn) {
        this.isUserColumn = isUserColumn;
    }


    class ColumnFilterChangesListener implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            accomodateAssociatedColumnFilterModelChanges( evt);
        }
    }
    
    /** Property Change Listener for Adding Property
     * @see javax.swing.table.TableColumn#addPropertyChangeListener(java.beans.PropertyChangeListener)
     * @param pcl
     */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        super.addPropertyChangeListener(pcl);
        pcs.addPropertyChangeListener(pcl);
    }

    /** Property Change Listener for remove Property
     * @see javax.swing.table.TableColumn#removePropertyChangeListener(java.beans.PropertyChangeListener)
     * @param pcl
     */
    @Override
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        super.removePropertyChangeListener(pcl);
        pcs.removePropertyChangeListener(pcl);
    }

    /** override method toString 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString(){
        StringBuffer vapus = new StringBuffer();
        vapus.append( "[ ");
        vapus.append( getClass().getName());
        vapus.append( ": ");
        vapus.append( "Visible="+isVisible);
        vapus.append( ", ModelIdx=");
        vapus.append( getModelIndex() );
        vapus.append( "]" );
//        vapus.append( "[ ");
        return vapus.toString();
    }
    
}
