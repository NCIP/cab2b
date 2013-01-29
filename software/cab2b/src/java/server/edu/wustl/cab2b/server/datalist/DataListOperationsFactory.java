/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.datalist;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.util.DataListUtil;
import edu.wustl.cab2b.common.util.ResultConfigurationParser;
import edu.wustl.cab2b.common.util.Utility;

/**
 * 
 * @author srinath_k, juberahamad_patel
 *
 */
public class DataListOperationsFactory {
	
	/**
	 * Creates datalist saver.
	 * @param entity
	 * @return Reference to DatalistSaver.
	 */
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

    /**
     * Creates datalist retriever.
     * @param entity
     * @return Reference to DataListRetriever
     */
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
