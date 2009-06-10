package edu.wustl.cab2b.client.ui.controls.LazyTable;


/**
 * This class represent a small block of data. Your might have a huge data , which is broken into these 
 * pages. 
 *  
 * @author Rahul Ner
 *
 * @param <D> data type that represent the contained data
 */
public class Page<D> {

    private D data;

    private PageInfo pageInfo;

    /**
     * @param startX
     * @param startY
     */
    public Page(PageInfo pageInfo,D data) {
        this.pageInfo = pageInfo;
        this.data = data;
    }
    
    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public D getData() {
        return data;
    }
}


