package edu.wustl.cab2b.client.ui.controls.temp;

import javax.swing.table.TableModel;

/**
 * 
 * @author rahul_ner
 *
 * @param <C>
 */
public abstract class LazyTableModel<C extends CachedTableDataSource> implements TableModel {
    
    protected C dataSource;
    
    public LazyTableModel(C tableDataSource) {
        this.dataSource = tableDataSource;
    }

}
