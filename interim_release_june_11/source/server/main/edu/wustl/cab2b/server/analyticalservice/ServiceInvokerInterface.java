package edu.wustl.cab2b.server.analyticalservice;

import java.util.List;

import edu.common.dynamicextensions.entitymanager.EntityRecordResultInterface;

/**
 * @author Chandrakant Talele
 */
public interface ServiceInvokerInterface {
    /**
     * @param data
     * @param serviceParamSet
     * @return
     */
    EntityRecordResultInterface invokeService(List<EntityRecordResultInterface> data,
                                              List<EntityRecordResultInterface> serviceParamSet);

}
