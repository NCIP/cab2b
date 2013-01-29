/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.controls.LazyTable;

/**
 * This is interface that is implemeted by the classses that will be used 
 * by {@link LazyTableModelInterface} to fetch the data.
 * 
 * @author Rahul Ner.
 */
public interface LazyDataSourceInterface {    

    /**
     * method required by table model
     * @return
     */
    int getRowCount();
    
    /**
     * method required by table model
     * @return
     */
    int getColumnCount();

    /**
     * method required by table model
     * @param columnNo
     * @return
     */
    String getColumnName(int columnNo);

    /**
     * method required by table model
     * @param rowNo
     * @param columnNo
     * @return
     */
    Object getData(int rowNo, int columnNo); 
    
    
    /**
     * @return
     */
    Page getCurrentPage();


    /**
     * @return
     */
    CacheInterface getCache();
    
    /**
     * @param cache
     */
    void setCache(CacheInterface cache);
    

}
