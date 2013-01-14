/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.controls.LazyTable;

/**
 * Cache that data source can use.
 * 
 * @author rahul_ner 
 * @param <D>
 */
public interface CacheInterface<D> {

    /**
     * chks if page given by pageInfo is cahced or not.
     * @param pageInfo
     * @return
     */
    boolean isCached(PageInfo pageInfo);

    /**
     * caches the given page 
     * @param page
     */
    void put(Page<D> page);

    /**
     * get a page represented by pageInfo from a cache.
     * If this is not cached returns null.
     * @param pageInfo pageInfo 
     * @return page if presetn otherwise null
     */
    Page<D> get(PageInfo pageInfo);
}
