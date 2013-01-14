/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

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
     * @param pageInfo
     * @param data
     */
    public Page(PageInfo pageInfo,D data) {
        this.pageInfo = pageInfo;
        this.data = data;
    }
    
    /**
     * @return PageInfo
     */
    public PageInfo getPageInfo() {
        return pageInfo;
    }

    /**
     * @return Data
     * 
     */
    public D getData() {
        return data;
    }
}


