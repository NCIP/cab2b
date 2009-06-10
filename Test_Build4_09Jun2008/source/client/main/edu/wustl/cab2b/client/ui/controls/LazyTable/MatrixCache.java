package edu.wustl.cab2b.client.ui.controls.LazyTable;

import edu.wustl.common.util.logger.Logger;

/**
 * This cache implemenation is extension to BucketCache with 2 dimensional bucket.
 * 
 * @author rahul_ner
 *
 */
public class MatrixCache<D> implements CacheInterface {

    /**
     * 
     */
    private Page<D>[][] pageCache;

    /**
     * 
     */
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
        Logger.out.debug("Page Cached: " + page.getPageInfo());

    }

    /**
     * @see edu.wustl.cab2b.client.CacheInterface#get(edu.wustl.cab2b.client.PageInfo)
     */
    public Page get(PageInfo pageInfo) {
        int cacheXLocation = getCacheXLocation(pageInfo);
        int cacheYLocation = getCacheYLocation(pageInfo);
        Page cachedPage = pageCache[cacheXLocation][cacheYLocation];
        if (cachedPage != null && pageInfo.equals(cachedPage.getPageInfo())) {
            Logger.out.debug("Cache Hit: " + pageInfo);
            return cachedPage;
        } else {
            Logger.out.debug("**Cache Miss: " + pageInfo);
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
