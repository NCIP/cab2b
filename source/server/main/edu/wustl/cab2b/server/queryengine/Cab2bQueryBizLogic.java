/**
 * 
 */
package edu.wustl.cab2b.server.queryengine;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.cache.AbstractEntityCache;
import edu.wustl.cab2b.common.queryengine.Cab2bQuery;
import edu.wustl.cab2b.common.queryengine.ICab2bParameterizedQuery;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.common.querysuite.bizLogic.QueryBizLogic;

/**
 * This class processes the Cab2bQuery object before persisting and after retreival.
 * @author chetan_patil
 */
public class Cab2bQueryBizLogic extends QueryBizLogic<ICab2bParameterizedQuery> {

    protected void preProcessQuery(ICab2bParameterizedQuery caB2BQuery) {
        super.preProcessQuery(caB2BQuery);

        EntityInterface entity = caB2BQuery.getOutputEntity();
        ((Cab2bQuery) caB2BQuery).setEntityId(entity.getId());
    }

    /**
     * @see edu.wustl.cab2b.server.queryengine.QueryProcessor#postProcessQuery(edu.wustl.common.querysuite.queryobject.IQuery)
     */
    public void postProcessQuery(ICab2bParameterizedQuery caB2BQuery) {
        super.postProcessQuery(caB2BQuery);

        Long entityId = ((Cab2bQuery) caB2BQuery).getEntityId();
        AbstractEntityCache abstractEntityCache = EntityCache.getCache();
        EntityInterface entity = abstractEntityCache.getEntityById(entityId);
        caB2BQuery.setOutputEntity(entity);
    }

    /** 
     * @see edu.wustl.common.querysuite.queryobject.util.QueryProcessor#getQueryClassName()
     */
    protected String getQueryClassName() {
        return Cab2bQuery.class.getName();
    }

}
