/**
 * 
 */
package edu.wustl.cab2b.server.queryengine;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.cache.AbstractEntityCache;
import edu.wustl.cab2b.common.queryengine.Cab2bQuery;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.common.querysuite.queryobject.util.QueryProcessor;

/**
 * This class processes the Cab2bQuery object before persisting and after retreival.
 * @author chetan_patil
 */
public class Cab2bQueryProcessor extends QueryProcessor<ICab2bQuery> {

    /**
     * @see edu.wustl.common.querysuite.queryobject.util.QueryProcessor#preProcessQuery(edu.wustl.common.querysuite.queryobject.IQuery)
     */
    protected void preProcessQuery(ICab2bQuery caB2Bquery) {
        super.preProcessQuery(caB2Bquery);

        EntityInterface entity = caB2Bquery.getOutputEntity();
        ((Cab2bQuery) caB2Bquery).setEntityId(entity.getId());
    }

    /**
     * @see edu.wustl.cab2b.server.queryengine.QueryProcessor#postProcessQuery(edu.wustl.common.querysuite.queryobject.IQuery)
     */
    protected void postProcessQuery(ICab2bQuery caB2Bquery) {
        super.postProcessQuery(caB2Bquery);

        Long entityId = ((Cab2bQuery) caB2Bquery).getEntityId();
        AbstractEntityCache abstractEntityCache = EntityCache.getCache();
        EntityInterface entity = abstractEntityCache.getEntityById(entityId);
        caB2Bquery.setOutputEntity(entity);
    }

    /** 
     * @see edu.wustl.common.querysuite.queryobject.util.QueryProcessor#getQueryClassName()
     */
    protected String getQueryClassName() {
        return ICab2bQuery.class.getName();
    }

}
