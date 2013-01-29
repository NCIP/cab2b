/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.searchDataWizard.dag;

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
