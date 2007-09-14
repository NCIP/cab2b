package edu.wustl.cab2b.common.util;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.TaggedValueInterface;
import edu.wustl.cab2b.common.cache.AbstractEntityCache;

public class DataListUtil {

    public static final String ORIGIN_ENTITY_ID_KEY = "originEntityId";

    public static final String SOURCE_ENTITY_ID_KEY = "sourceEntityId";

    private DataListUtil() {

    }

    public static EntityInterface getOriginEntity(EntityInterface entity) {
        TaggedValueInterface taggedValue = Utility.getTaggedValue(entity, ORIGIN_ENTITY_ID_KEY);
        if (taggedValue == null) {
            return entity;
        }
        return getEntityWithId(taggedValue.getValue());
    }

    public static EntityInterface getSourceEntity(EntityInterface entity) {
        TaggedValueInterface taggedValue = Utility.getTaggedValue(entity, SOURCE_ENTITY_ID_KEY);
        if (taggedValue == null) {
            return entity;
        }
        return getEntityWithId(taggedValue.getValue());
    }

    private static EntityInterface getEntityWithId(String id) {
        return AbstractEntityCache.getCache().getEntityById(Long.parseLong(id));
    }
    
 
    
    protected final DomainObjectFactory getDomainObjectFactory() {
        return DomainObjectFactory.getInstance();
    }
}
