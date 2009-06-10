package edu.wustl.cab2b.client.ui.controls.LazyTable;

import edu.wustl.common.util.logger.Logger;

/**
 * This cache implemenation based on backets.
 * A page can be cached into a fixed bucket. If it is not present in that bucket, the existing page at that location is replaced
 * by this page.
 * 
 * @author Rahul Ner
 */
public class BucketCache<D> implements CacheInterface {

    /**
     * 
     */
    private Page<D>[] pageCache;

    /**
     * 
     */
    private int cacheSize;

    /**
     * @param cacheSize
     */
    public BucketCache(int cacheSize) {
        this.cacheSize = cacheSize;
        pageCache = new Page[cacheSize];
    }

    /**
     * 
     */
    public BucketCache() {
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
        int cacheLocation = getCacheLocation(page.getPageInfo());
        pageCache[cacheLocation] = page;
        Logger.out.debug("Page Cached: " + page.getPageInfo());
    }

    /**
     * @see edu.wustl.cab2b.client.CacheInterface#get(edu.wustl.cab2b.client.PageInfo)
     */
    public Page get(PageInfo pageInfo) {
        int cacheLocation = getCacheLocation(pageInfo);
        Page cachedPage = pageCache[cacheLocation];
        if (cachedPage != null && pageInfo.equals(cachedPage.getPageInfo())) {
            Logger.out.debug("Cache Hit: " + pageInfo);
            return cachedPage;
        } else {
            Logger.out.debug("**Cache Miss: " + pageInfo);
            return null;
        }
    }

    private int getCacheLocation(PageInfo pageInfo) {
        return pageInfo.getStartX() % cacheSize;
    }

}
