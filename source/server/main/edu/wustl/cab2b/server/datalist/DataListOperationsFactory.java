package edu.wustl.cab2b.server.datalist;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.TaggedValueInterface;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.cab2b.server.cache.EntityCache;

public class DataListOperationsFactory {
    public static DataListSaver<?> createDataListSaver(EntityInterface entity) {
        return new DefaultDataListSaver(entity);
    }

    public static DataListRetriever<?> createDataListRetriever(EntityInterface entity) {
        TaggedValueInterface taggedValue = Utility.getTaggedValue(entity.getTaggedValueCollection(),
                                                                  AbstractDataListSaver.OLD_ENTITY_ID_TAG_NAME);
        Long originEntityId = Long.parseLong(taggedValue.getValue());
        EntityInterface originEntity = EntityCache.getInstance().getEntityById(originEntityId);

        return new DefaultDataListRetriever(entity);
    }
}
