/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */
package edu.wustl.cab2b.admin.searchdata.action;

import static edu.wustl.cab2b.admin.util.AdminConstants.ALL_LOADED_MODELS;

import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2b.admin.action.BaseAction;
import edu.wustl.cab2b.admin.bizlogic.ServiceInstanceBizLogic;

/**
 * @author gaurav_mehta
 *
 */
public class DefineModelGroups extends BaseAction {

    private static final long serialVersionUID = 1854118345769487660L;

    @Override
    public String execute() {

        Collection<EntityGroupInterface> allLoadedModels = (Collection<EntityGroupInterface>) session.get(ALL_LOADED_MODELS);
        if (allLoadedModels == null) {
            allLoadedModels = new ServiceInstanceBizLogic().getMetadataEntityGroups();
            session.put(ALL_LOADED_MODELS, allLoadedModels);
        }
        return SUCCESS;
    }

}
