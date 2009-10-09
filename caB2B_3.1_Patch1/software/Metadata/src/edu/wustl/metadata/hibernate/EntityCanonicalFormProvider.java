package edu.wustl.metadata.hibernate;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.cache.AbstractEntityCache;

public class EntityCanonicalFormProvider extends AbstractMetadataCanonicalFormProvider<EntityInterface> {

    public Class<EntityInterface> objectClass() {
        return EntityInterface.class;
    }

    @Override
    protected EntityInterface getObjectFromEntityCache(Long id) {
        return AbstractEntityCache.getCache().getEntityById(id);
    }
}
