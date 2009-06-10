package edu.wustl.cab2b.server.queryengine.resulttransformers;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.queryengine.result.ICategorialClassRecord;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.common.querysuite.metadata.category.CategorialClass;
import gov.nih.nci.cagrid.dcql.DCQLQuery;

/**
 * Specifies the operations to be supported by a results transformer.
 * @author srinath_k
 * @param <R>
 *            the type of records created when executing a query for a class.
 * @param <C>
 *            the type of records created when executing a query for a category.
 */
public interface IQueryResultTransformer<R extends IRecord, C extends ICategorialClassRecord> {
    /**
     * Executes the DCQL and transforms the results obtained to an appropriate
     * {@link IQueryResult}.
     * @param query
     *            the DCQL.
     * @param targetEntity
     *            the target entity (corresponds to the target object of the
     *            dcql).
     * @return the results.
     */
    IQueryResult<R> getResults(DCQLQuery query, EntityInterface targetEntity);

    /**
     * Executes the DCQL and transforms the results obtained to an appropriate
     * {@link IQueryResult}.
     * @param query
     *            the DCQL whose target object corresponds to the actual UML
     *            class represented by the categorial class.
     * @param categorialClass
     *            the categorial class.
     * @return the results.
     */
    IQueryResult<C> getCategoryResults(DCQLQuery query,
                                       CategorialClass categorialClass);
}
