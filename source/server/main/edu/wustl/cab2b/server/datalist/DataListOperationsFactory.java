package edu.wustl.cab2b.server.datalist;

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

            DataListSaver<?> saver = (DataListSaver<?>) createObject(transformerName);
            saver.initialize(entity);
            return saver;
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
            DataListRetriever retriever = (DataListRetriever<?>) createObject(transformerName);
            retriever.initialize(entity);
            return retriever;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Object createObject(String className) {
        try {
            return Class.forName(className).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
