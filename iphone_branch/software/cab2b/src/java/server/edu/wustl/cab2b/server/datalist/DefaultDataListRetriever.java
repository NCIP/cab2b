/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.datalist;

import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.queryengine.result.QueryResultFactory;
import edu.wustl.cab2b.common.queryengine.result.RecordId;

/**
 * The default implementation of the data list retriever that creates the basic
 * {@link IRecord} using {@link QueryResultFactory#createRecord(Set, RecordId)}.
 * 
 * @author srinath_k
 * 
 */
public class DefaultDataListRetriever extends AbstractDataListRetriever<IRecord> {

    /**
     * Creates the basic {@link IRecord} using
     * {@link QueryResultFactory#createRecord(Set, RecordId)}.
     * 
     * @param entity 
     * @param attributes 
     * @param id 
     * @return Reference to IRecord
     * @see edu.wustl.cab2b.server.datalist.AbstractDataListRetriever#createRecord(edu.common.dynamicextensions.domaininterface.EntityInterface,
     *      java.util.Set, edu.wustl.cab2b.common.queryengine.result.RecordId)
     */
    @Override
    protected IRecord createRecord(EntityInterface entity, Set<AttributeInterface> attributes, RecordId id) {
        return QueryResultFactory.createRecord(attributes, id);
    }
}
