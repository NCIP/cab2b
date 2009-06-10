/**
 * 
 */
package edu.wustl.cab2b.common.queryengine;

import java.io.Serializable;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.common.querysuite.queryobject.IParameterizedQuery;

/**
 * @author chetan_patil
 *
 */
public interface ICab2bQuery extends IParameterizedQuery, Serializable {
    // TODO need to be generalized for multiple outputs
    // TODO urls for intermodel categories
    /**
     * @return list of output URLs
     */
    List<String> getOutputUrls();

    /**
     * @param url
     */
    void setOutputUrls(List<String> url);

    /**
     * @return output entity
     */
    EntityInterface getOutputEntity();

    /**
     * @param outputEntity
     */
    void setOutputEntity(EntityInterface outputEntity);

    /**
     * @param userId
     */
    void setUserId(Long userId);

    /**
     * @return user id
     */
    Long getUserId();
}
