package edu.wustl.cab2b.server.analyticalservice;

import java.util.List;

import edu.common.dynamicextensions.entitymanager.EntityRecordResultInterface;

/**
 * @author Chandrakant Talele
 */
public class CMSServiceInvoker implements ServiceInvokerInterface {

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.server.analyticalservice.ServiceInvokerInterface#invokeService(java.util.List, java.util.List)
     */
    public EntityRecordResultInterface invokeService(List<EntityRecordResultInterface> data,
                                                     List<EntityRecordResultInterface> serviceParamSet) {
        //TODO Real code to call service will go here 
        return data.get(0);
    }
}
