/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.controls.LazyTable;

import org.apache.log4j.Logger;

import edu.wustl.cab2b.client.cache.UserCache;

/**
 * This cache implementation is extension to BucketCache with 2 dimensional bucket.
 * 
 * @author rahul_ner
 *
 */
public class MatrixCache<D> implements CacheInterface<D> {

    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(UserCache.class);
    
    private Page<D>[][] pageCache;

    private int cacheSize;

    /**
     * @param cacheSize
     */
    public MatrixCache(int cacheSize) {
        this.cacheSize = cacheSize;
        pageCache = new Page[cacheSize][cacheSize];
    }

    /**
     * 
     */
    public MatrixCache() {
        this(3);
    }

    /**
     * @see edu.wustl.cab2b.client.CacheInterface#isCached(edu.wustl.cab2b.client.PageInfo)
     */
    public boolean isCached(PageInfo pageInfo) {
        if (get(pageInfo) == null) {
            return false;
        }
        return true;
    }

    /**
     * @see edu.wustl.cab2b.client.CacheInterface#put(edu.wustl.cab2b.client.Page)
     */
    public void put(Page page) {
        int cacheXLocation = getCacheXLocation(page.getPageInfo());
        int cacheYLocation = getCacheYLocation(page.getPageInfo());
        pageCache[cacheXLocation][cacheYLocation] = page;
        logger.debug("Page Cached: " + page.getPageInfo());

    }

    /**
     * @see edu.wustl.cab2b.client.CacheInterface#get(edu.wustl.cab2b.client.PageInfo)
     */
    public Page get(PageInfo pageInfo) {
        int cacheXLocation = getCacheXLocation(pageInfo);
        int cacheYLocation = getCacheYLocation(pageInfo);
        Page cachedPage = pageCache[cacheXLocation][cacheYLocation];
        if (cachedPage != null && pageInfo.equals(cachedPage.getPageInfo())) {
            logger.debug("Cache Hit: " + pageInfo);
            return cachedPage;
        } else {
            logger.debug("**Cache Miss: " + pageInfo);
            return null;
        }
    }

    private int getCacheXLocation(PageInfo pageInfo) {
        return pageInfo.getStartX() % cacheSize;
    }

    private int getCacheYLocation(PageInfo pageInfo) {
        return pageInfo.getStartY() % cacheSize;
    }

}
