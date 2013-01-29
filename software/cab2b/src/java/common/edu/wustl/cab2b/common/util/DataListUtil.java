/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.util;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.TaggedValueInterface;
import edu.wustl.cab2b.common.cache.AbstractEntityCache;

public class DataListUtil {

    /**
     * When a new entity is created for the datalist, this tag on it is used to
     * point to the original entity (i.e. entity from UML model of the
     * application).
     */
    public static final String ORIGIN_ENTITY_ID_KEY = "originEntityId";

    /**
     * When a new entity is created for the datalist, this tag on it is used to
     * point to the entity from which this new entity was created. <br>
     * Note : In this case, both, the old and new entity, will have the same
     * "origin entity".
     */
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
