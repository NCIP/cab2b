
package edu.common.dynamicextensions.ui.webui.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.common.dynamicextensions.util.global.Constants;

/**
 * @author deepti_shelar
 * Class that handles the complete logic of storing and retrieving objects from the cache/session.
 */
public class CacheManager
{
	/**
	 * Create a new CacheManager instance
	 *
	 */
	public CacheManager()
	{

	}

	static Map cacheMap;

	/**
	 * Add object to cache 
	 * @param request : HTTP Request object
	 * @param key : Key to be used while storing object in cache
	 * @param formDetailsObject : Object to be stored in cache
	 */
	
	public static void addObjectToCache(HttpServletRequest request, String key, Object formDetailsObject)
	{
		HttpSession session = request.getSession();
		cacheMap = (Map) session.getAttribute(Constants.CACHE_MAP);

		if (cacheMap == null)
		{
			cacheMap = new HashMap();
			session.setAttribute(Constants.CACHE_MAP, cacheMap);
		}
		cacheMap.put(key, formDetailsObject);

	}

	/**
	 * Get object from cache for specified key
	 * @param request : Request object
	 * @param key : Key to search
	 * @return : Object for specified key
	 */
	public static Object getObjectFromCache(HttpServletRequest request, String key)
	{
		HttpSession session = request.getSession();
		Object result = null;

		if (session.getAttribute(Constants.CACHE_MAP) != null)
		{
			cacheMap = (Map) session.getAttribute(Constants.CACHE_MAP);
			result = cacheMap.get(key);
		}
		return result;
	}
	/**
	 * Remove object from cache
	 * @param request : HTTP request object
	 * @param key : Key for which object should be removed from cache
	 */
	public static void removeObjectFromCache(HttpServletRequest request, String key)
	{
		HttpSession session = request.getSession();
		if (session.getAttribute(Constants.CACHE_MAP) != null)
		{
			cacheMap = (Map) session.getAttribute(Constants.CACHE_MAP);
			cacheMap.remove(key);
		}
	}
	/**
	 * Clear cache object
	 * @param request HTTP Request object
	 */
	public static void clearCache(HttpServletRequest request)
	{
		HttpSession session = request.getSession();
		if (session.getAttribute(Constants.CACHE_MAP) != null)
		{
			cacheMap = (Map) session.getAttribute(Constants.CACHE_MAP);
			cacheMap.clear();
		}
	}
}
