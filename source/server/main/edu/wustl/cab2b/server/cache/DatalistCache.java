package edu.wustl.cab2b.server.cache;

import java.util.HashMap;
import java.util.Map;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.server.datalist.DataListUtil;

public class DatalistCache {
    private Map<Long, EntityInterface> entityForId;

    private static DatalistCache datalistCache;

    private DatalistCache() {

    }

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

    public EntityInterface getEntityWithId(Long id) {
        if (!entityForId.containsKey(id)) {
            throw new RuntimeException("Entity with given id not found.");
        }
        return entityForId.get(id);
    }

    public void addEntity(EntityInterface entity) {
        entityForId.put(entity.getId(), entity);
    }
}
