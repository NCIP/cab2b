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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2b.admin.action.BaseAction;
import edu.wustl.cab2b.admin.bizlogic.ModelGroupBizLogic;
import edu.wustl.cab2b.admin.util.AdminConstants;
import edu.wustl.cab2b.common.modelgroup.ModelGroup;
import edu.wustl.cab2b.common.modelgroup.ModelGroupInterface;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;

/**
 * @author gaurav_mehta
 *
 */
public class SaveModelGroup extends BaseAction {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String modelGroupName = null;

    private String modelGroupDescription = null;

    private String secured = null;

    private String[] selectedEntityGroup = null;

    /**This method returns the ModelGroupName. It is used by Struts to set the value of Model Group Name.
     * @return
     */
    public String getModelGroupName() {
        return modelGroupName;
    }

    /**
     * This method sets the modelGroupName from session into the local variable. 
     * This is done by Struts 2.0 and we don't have to interfere 
     * @param modelGroupName
     */
    public void setModelGroupName(String modelGroupName) {
        this.modelGroupName = modelGroupName;
    }

    /**
     * This method gets the modelGroupname. This methods are used by Struts 2.0 itself.
     * @return
     */
    public String getModelGroupDescription() {
        return modelGroupDescription;
    }

    /**
     * This method sets the modelGroup description from session into the local variable. 
     * This is done by Struts 2.0 and we don't have to interfere
     * @param modelGroupDescription
     */
    public void setModelGroupDescription(String modelGroupDescription) {
        this.modelGroupDescription = modelGroupDescription;
    }

    /**
     * This method gets whether the modelGroup is secured or not. 
     * This methods are used by Struts 2.0 itself.
     * @return
     */
    public String getSecured() {
        return secured;
    }

    /**
     * This method sets the whether modelGroup is secured or not from session into the local variable.
     * This is done by Struts 2.0 and we don't have to interfere
     * @param modelGroupIsSecured
     */
    public void setSecured(String secured) {
        this.secured = secured;
    }

    /**
     * This method gets the list of modelGroup selected by user. This methods are used by Struts 2.0 itself.
     * @return
     */
    public String[] getSelectedEntityGroup() {
        return selectedEntityGroup;
    }

    /**
     * This method sets the list of model Group selected by user from session into the local variable. 
     * This is done by Struts 2.0 and we don't have to interfere
     * @param checkedModels
     */
    public void setSelectedEntityGroup(String[] selectedEntityGroup) {
        this.selectedEntityGroup = selectedEntityGroup;
    }

    /**
     * @return 
     * @see com.opensymphony.xwork2.ActionSupport#execute()
     */
    public String execute() {

        List<EntityGroupInterface> entityList = new ArrayList<EntityGroupInterface>();
        EntityCache entityCache = EntityCache.getInstance();

        ModelGroupInterface modelGroup = new ModelGroup();

        Collection<String> modelgroupName = new ModelGroupBizLogic().getAllModelGroupsName();
        if (modelgroupName.contains(modelGroupName)) {
            request.setAttribute("error", "Model Group name already exists");
            return AdminConstants.ERROR;
        }

        for (int i = 0; i < selectedEntityGroup.length; i++) {
            entityList.add(entityCache.getEntityGroupByName(selectedEntityGroup[i]));
        }
        modelGroup.setEntityGroupList(entityList);
        if ("True".equals(secured)) {
            modelGroup.setSecured(true);
        } else {
            modelGroup.setSecured(false);
        }
        if (modelGroupDescription != null && modelGroupDescription.length() != 0) {
            modelGroup.setModelDescription(modelGroupDescription);
        }
        modelGroup.setModelGroupName(modelGroupName);

        try {
            new ModelGroupBizLogic().saveModelGroup(modelGroup);
        } catch (UserNotAuthorizedException e) {
            new RuntimeException(e.getMessage(), e);
            return AdminConstants.FAILURE;
        } catch (BizLogicException e) {
            new RuntimeException(e.getMessage(), e);
        }
        return AdminConstants.SUCCESS;
    }

}
