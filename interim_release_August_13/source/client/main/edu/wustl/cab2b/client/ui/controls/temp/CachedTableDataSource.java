package edu.wustl.cab2b.client.ui.controls.temp;

/**
 * @author rahul_ner
 *
 * @param <D>
 */
public interface CachedTableDataSource<D> {

    D getData(int rowIndex,int columnIndex);
    
    D getCurrentData();

}
