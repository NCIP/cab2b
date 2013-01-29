/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.controls.LazyTable;

/**
 * This is abstract data source. Actual impelmetation should provide implmentation  
 * @author rahul_ner
 *
 * @param <D>
 */
public abstract class AbstractLazyDataSource<D> implements LazyDataSourceInterface {

    /**
     * 
     */
    protected Page<D> currentPage;
    
    /**
     * 
     */
    protected PageDimension pageDimension;
    
    /**
     * 
     */
    private CacheInterface cache;
    
    /**
     * @param pageDimension
     * @param cache
     */
    public AbstractLazyDataSource(PageDimension pageDimension,CacheInterface cache) {
        this.pageDimension = pageDimension;
        this.cache = cache;
        
    }

    /**  Gets RowCount
     * @return 0
     * @see edu.wustl.cab2b.client.ui.controls.LazyTable.LazyDataSourceInterface#getRowCount()
     */
    public int getRowCount() {
        return 0;
    }

    /** Returns ColumnCount
     * @return 0
     * @see edu.wustl.cab2b.client.ui.controls.LazyTable.LazyDataSourceInterface#getColumnCount()
     */
    public int getColumnCount() {
        return 0;
    }

    /** Gets the Column name for given Column Index
     * @param columnNo
     * @return ColumnName
     * @see edu.wustl.cab2b.client.ui.controls.LazyTable.LazyDataSourceInterface#getColumnName(int)
     */
    public String getColumnName(int columnNo) {
        return "column_" + columnNo;
    }

    /**
     * Gets the value for a particular Cell in Object form
     * @param rowNo
     * @param columnNo
     * @return value
     * @see edu.wustl.cab2b.client.ui.controls.LazyTable.LazyDataSourceInterface#getData(int, int)
     */
    public Object getData(int rowNo, int columnNo) {
        PageInfo pageInfo = getPageInfo(rowNo, columnNo);
        
        if (currentPage == null || !currentPage.getPageInfo().equals(pageInfo)) {
            Page<D> cachedPage = cache.get(pageInfo);
            
            if (cachedPage == null) {
                cachedPage = fetchPageData(pageInfo);
                cache.put(cachedPage);
            } 
            currentPage = cachedPage;
        }
        return extractDataFromPage(rowNo,columnNo);
    }

    /**
     * @param rowNo
     * @param columnNo
     * @return
     */
    public abstract Object extractDataFromPage(int rowNo, int columnNo);

    
    /**
     * fetches data for a given page info from the server.
     * @param pageInfo
     * @return
     */
    public abstract Page<D> fetchPageData(PageInfo pageInfo);
    
    /**
     * @param rowNo
     * @param columnNo
     * @return Page Info
     */
    protected PageInfo getPageInfo(int rowNo, int columnNo) {
        int pageStartX = pageDimension.getNoOfRows()* (rowNo / pageDimension.getNoOfRows());
        int pageStartY = pageDimension.getNoOfColumns()* (columnNo / pageDimension.getNoOfColumns());
        return new PageInfo(pageStartX ,pageStartY);
    }


    
    /** Getter Method for Cache
     * @return Cache
     * @see edu.wustl.cab2b.client.ui.controls.LazyTable.LazyDataSourceInterface#getCache()
     */
    public CacheInterface getCache() {
        return cache;
    };
    
    
    /** Setter Method for Cache
     * @param cache
     * @see edu.wustl.cab2b.client.ui.controls.LazyTable.LazyDataSourceInterface#setCache(edu.wustl.cab2b.client.ui.controls.LazyTable.CacheInterface)
     */
    public void setCache(CacheInterface cache) {
        this.cache = cache;
    }

    /** Gets the CurrentPage
     * @return
     * @see edu.wustl.cab2b.client.ui.controls.LazyTable.LazyDataSourceInterface#getCurrentPage() 
     */
    public Page<D> getCurrentPage() {
        return currentPage;
    }
}
