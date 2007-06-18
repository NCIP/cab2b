package edu.wustl.cab2b.server.datalist;

import java.util.Map;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.queryengine.result.IRecord;

public interface DataListSaver<R extends IRecord> {
    Map<AbstractAttributeInterface, Object> getRecordAsMap(R record);

    EntityInterface getNewEntity();
}
