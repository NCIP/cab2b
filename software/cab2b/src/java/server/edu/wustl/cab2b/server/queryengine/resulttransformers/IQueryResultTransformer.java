/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.queryengine.resulttransformers;

import java.util.List;
import java.util.Map;

import org.globus.gsi.GlobusCredential;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.querystatus.QueryStatus;
import edu.wustl.cab2b.common.queryengine.result.ICategorialClassRecord;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.common.querysuite.metadata.category.CategorialClass;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.dcql.DCQLQuery;

/**
 * Specifies the operations to be supported by a results transformer. A query
 * result transformer has to be registered in the file
 * <code>ResultConfiguration.xml</xml>.
 * @author srinath_k
 * @see AbstractQueryResultTransformer
 * @param <R>
 *            the type of records created when executing a query for a class.
 * @param <C>
 *            the type of records created when executing a query for a category.
 */
public interface IQueryResultTransformer<R extends IRecord, C extends ICategorialClassRecord> {
    /**
     * Executes the DCQL and transforms the results obtained to an appropriate
     * {@link IQueryResult}.
     * 
     * @param query the DCQL.
     * @param targetEntity the target entity (corresponds to the target object
     *            of the dcql).
     * @param cred security credentials
     * @return the results.
     */
    IQueryResult<R> getResults(DCQLQuery query, EntityInterface targetEntity, GlobusCredential cred);

    /**
     * Executes the DCQL and transforms the results obtained to an appropriate
     * {@link IQueryResult}.
     * 
     * @param query the DCQL whose target object corresponds to the actual UML
     *            class represented by the categorial class.
     * @param categorialClass the categorial class.
     * @param cred security credentials
     * @return the results.
     */
    IQueryResult<C> getCategoryResults(DCQLQuery query, CategorialClass categorialClass, GlobusCredential cred);

	IQueryResult<? extends IRecord> getResultsNoUpdate(DCQLQuery dcqlQuery,
			EntityInterface outputEntity, GlobusCredential gc);

	public void setStatus(IQueryResult<? extends IRecord> result);

	IQueryResult<? extends IRecord> mergeResults(
			List<IQueryResult<? extends IRecord>> results,
			EntityInterface outputEntity);

	void mergeStatus(QueryStatus qStatus,
			List<IQueryResult<? extends IRecord>> results);

}
