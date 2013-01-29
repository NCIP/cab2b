/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.datalist;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.queryengine.result.IRecord;

/**
 * The default implementation of the data list saver that creates the new entity
 * as a copy of the original entity by copying the attributes.
 * 
 * @author srinath_k
 * @see DataListUtil#copyNonVirtualAttributes(EntityInterface, EntityInterface)
 * 
 */
public class DefaultDataListSaver extends AbstractDataListSaver<IRecord> {

    /**
     * Copies the attributes from the old entity to the new entity by calling
     * {@link DataListUtil#copyNonVirtualAttributes(EntityInterface, EntityInterface)}.
     * 
     * @param oldEntity Used for information, copied to a new Entity.
     * 
     * @see edu.wustl.cab2b.server.datalist.AbstractDataListSaver#populateNewEntity(edu.common.dynamicextensions.domaininterface.EntityInterface)
     */
    @Override
    protected void populateNewEntity(EntityInterface oldEntity) {
        DataListUtil.copyNonVirtualAttributes(newEntity, oldEntity);
    }

}
