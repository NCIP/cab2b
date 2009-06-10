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
    
    public AbstractLazyDataSource(PageDimension pageDimension,CacheInterface cache) {
        this.pageDimension = pageDimension;
        this.cache = cache;
        
    }

    public int getRowCount() {
        return 0;
    }

    public int getColumnCount() {
        return 0;
    }

    public String getColumnName(int columnNo) {
        return "column_" + columnNo;
    }

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

    public abstract Object extractDataFromPage(int rowNo, int columnNo);

    /** 
     * fetches data for a given page info from the server.
     */
    public abstract Page<D> fetchPageData(PageInfo pageInfo);
    
    
    

    /**
     * @param rowNo
     * @param columnNo
     * @return
     */
    protected PageInfo getPageInfo(int rowNo, int columnNo) {
        int pageStartX = pageDimension.getNoOfRows()* (rowNo / pageDimension.getNoOfRows());
        int pageStartY = pageDimension.getNoOfColumns()* (columnNo / pageDimension.getNoOfColumns());
        return new PageInfo(pageStartX ,pageStartY);
    }


    
    /**
     * @see edu.wustl.cab2b.client.ui.controls.LazyTable.LazyDataSourceInterface#getCache()
     */
    public CacheInterface getCache() {
        return cache;
    };
    
    
    /**
     * @see edu.wustl.cab2b.client.ui.controls.LazyTable.LazyDataSourceInterface#setCache(edu.wustl.cab2b.client.ui.controls.LazyTable.CacheInterface)
     */
    public void setCache(CacheInterface cache) {
        this.cache = cache;
    }

    public Page<D> getCurrentPage() {
        return currentPage;
    }
}
