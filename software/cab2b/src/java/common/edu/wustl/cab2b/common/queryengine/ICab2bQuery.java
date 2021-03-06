/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 *
 */
package edu.wustl.cab2b.common.queryengine;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.common.querysuite.queryobject.IParameterizedQuery;

/**
 * @author chetan_patil
 *
 */
public interface ICab2bQuery extends IParameterizedQuery, Serializable {
    
    /**
     * TODO need to be generalized for multiple outputs urls for intermodel categories
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
     * This method returns true if the query is marked for keyword search; false otherwise.
     * @return
     */
    Boolean isKeywordSearch();

    /**
     * This method marks whether query is for keyword search or not.
     * @param isKeywordSearch true if marked for keyword search; false if not.
     */
    void setIsKeywordSearch(Boolean isKeywordSearch);
    
	public Collection<ServiceGroup> getServiceGroups();

	public void setServiceGroups(Collection<ServiceGroup> serviceGroups);
    
}
