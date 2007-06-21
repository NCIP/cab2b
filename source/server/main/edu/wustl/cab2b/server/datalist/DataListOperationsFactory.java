package edu.wustl.cab2b.server.datalist;

import java.lang.reflect.Constructor;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.util.DataListUtil;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.cab2b.server.analyticalservice.ResultConfigurationParser;

public class DataListOperationsFactory {
    public static DataListSaver<?> createDataListSaver(EntityInterface entity) {
        EntityInterface originEntity = DataListUtil.getOriginEntity(entity);
        try {
            String transformerName = ResultConfigurationParser.getInstance().getDataListSaver(
                                                                                              Utility.getApplicationName(originEntity),
                                                                                              originEntity.getName());
            Constructor<?> constructor = Class.forName(transformerName).getDeclaredConstructor(
                                                                                               EntityInterface.class);
            Object o = constructor.newInstance(entity);
            return (DataListSaver<?>) o;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static DataListRetriever<?> createDataListRetriever(EntityInterface entity) {
        EntityInterface originEntity = DataListUtil.getOriginEntity(entity);
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
