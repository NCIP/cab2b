package edu.wustl.cab2b.server.datalist;

import java.lang.reflect.Constructor;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.TaggedValueInterface;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.cab2b.server.analyticalservice.ResultConfigurationParser;
import edu.wustl.cab2b.server.cache.EntityCache;

public class DataListOperationsFactory {
    public static DataListSaver<?> createDataListSaver(EntityInterface entity) {
//        try {
//            String transformerName = ResultConfigurationParser.getInstance().getDataListSaver(
//                                                                                              Utility.getApplicationName(entity),
//                                                                                              entity.getName());
//            Constructor<?> constructor = Class.forName(transformerName).getDeclaredConstructor(
//                                                                                               EntityInterface.class);
//            Object o = constructor.newInstance(entity);
//            return (DataListSaver<?>) o;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
        return new CategoryDataListSaver(entity);
    }

    public static DataListRetriever<?> createDataListRetriever(EntityInterface entity) {
        TaggedValueInterface taggedValue = Utility.getTaggedValue(entity.getTaggedValueCollection(),
                                                                  AbstractDataListSaver.OLD_ENTITY_ID_TAG_NAME);
        Long originEntityId = Long.parseLong(taggedValue.getValue());
        EntityInterface originEntity = EntityCache.getInstance().getEntityById(originEntityId);

        try {
            String transformerName = ResultConfigurationParser.getInstance().getDataListRetriever(
                                                                                                  Utility.getApplicationName(originEntity),
                                                                                                  originEntity.getName());
            Constructor<?> constructor = Class.forName(transformerName).getDeclaredConstructor(
                                                                                               EntityInterface.class);
            Object o = constructor.newInstance(entity);
            return (DataListRetriever<?>) o;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
