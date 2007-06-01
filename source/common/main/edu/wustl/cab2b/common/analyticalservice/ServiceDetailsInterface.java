package edu.wustl.cab2b.common.analyticalservice;

import java.io.Serializable;
import java.util.List;
import edu.common.dynamicextensions.domaininterface.EntityInterface;

/**
 * It provides details about one method of an analytical service.
 * For each method of analytical service, one implementation of this interface is needed.
 * @author Chandrakant Talele
 */
public interface ServiceDetailsInterface extends Serializable {
    /**
     * @return The display name to be shown on UI
     */
    String getDisplayName();

    /**
     * @return All the entities required for the service
     */
    List<EntityInterface> getRequiredEntities();

    /**
     * @return URL of the location where the service is running
     */
    String getServiceURL();

    /**
     * @return The method name which will be invoked on the service  
     */
    String getMethodName();
}
