package edu.wustl.cab2b.server.datalist;

import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.queryengine.result.QueryResultFactory;
import edu.wustl.cab2b.common.queryengine.result.RecordId;

public class DefaultDataListRetriever extends AbstractDataListRetriever<IRecord> {
    public DefaultDataListRetriever(EntityInterface newEntity) {
        super(newEntity);
    }

    @Override
    protected IRecord createRecord(EntityInterface entity, Set<AttributeInterface> attributes, RecordId id) {
        return QueryResultFactory.createRecord(attributes, id);
    }
}
