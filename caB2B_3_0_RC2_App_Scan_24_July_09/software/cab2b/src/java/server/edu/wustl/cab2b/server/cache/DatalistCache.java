package edu.wustl.cab2b.server.cache;

import java.util.HashMap;
import java.util.Map;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.server.datalist.DataListUtil;

/**
 * 
 * @author srinath_k
 * @author rahul_ner
 *
 */
public class DatalistCache {
    private Map<Long, EntityInterface> entityForId;

    private static DatalistCache datalistCache;

    private DatalistCache() {

    }

    /**
     * Returns reference to DatalistCache
     * @return DatalistCache
     */
    public static synchronized DatalistCache getInstance() {
        if (datalistCache == null) {
            datalistCache = new DatalistCache();
            datalistCache.init();
        }
        return datalistCache;
    }

    private void init() {
        entityForId = new HashMap<Long, EntityInterface>();
        EntityGroupInterface datalistEntityGroup = DataListUtil.getDatalistEntityGroup();
        for (EntityInterface entity : datalistEntityGroup.getEntityCollection()) {
            addEntity(entity);
        }
    }

    /**
     * Returns reference to EntityInterface
     * @param id
     * @return Reference to EntityInterface
     */
    public EntityInterface getEntityWithId(Long id) {
        if (!isEntityPresent(id)) {
            throw new RuntimeException("Entity with given id not found.");
        }
        return entityForId.get(id);
    }

    /**
     * Adds entity.
     * @param entity
     */
    public void addEntity(EntityInterface entity) {
        entityForId.put(entity.getId(), entity);
    }

    /**
     * Checks whether entity corresponding to given id present or not. 
     * @param id
     * @return true if entity is present otherwise false
     */
    public boolean isEntityPresent(Long id) {
        return entityForId.containsKey(id);
    }
}
