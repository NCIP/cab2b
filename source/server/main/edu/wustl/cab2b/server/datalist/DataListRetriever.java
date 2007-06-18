package edu.wustl.cab2b.server.datalist;

import java.util.List;

import edu.wustl.cab2b.common.queryengine.result.IRecord;

public interface DataListRetriever<R extends IRecord> {
    List<R> getEntityRecords(List<Long> recordIds);
}
