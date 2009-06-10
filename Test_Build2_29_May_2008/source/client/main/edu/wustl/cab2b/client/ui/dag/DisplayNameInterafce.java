package edu.wustl.cab2b.client.ui.dag;

import edu.common.dynamicextensions.domaininterface.EntityInterface;

/**
 * This interface provices the ddisplay names for the a particular object. 
 * 
 * @author Rahul Ner
 */
public interface DisplayNameInterafce {

    /**
     * Returns the display name for the given entity
     * @param entity  entity 
     * @return display name
     */
    String getEntityDisplayName(EntityInterface entity);

}
