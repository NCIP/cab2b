/**
 * 
 */
package edu.wustl.cab2bwebapp.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2b.common.modelgroup.ModelGroup;
import edu.wustl.cab2b.common.modelgroup.ModelGroupInterface;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2bwebapp.constants.Constants;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
/**
 * @author gaurav_mehta
 *
 */
public class SaveModelGroupAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) {
        String[] entityGroupName = request.getParameterValues("EntityGroupCollection");
        String modelGroupName = request.getParameter("modelGroupName");
        String isSecured = request.getParameter("secured");

        List<EntityGroupInterface> entityList = new ArrayList<EntityGroupInterface>();
        EntityCache entityCache = EntityCache.getInstance();

        ModelGroupInterface modelGroup = new ModelGroup();

        if (entityGroupName.length != 0) {
            for (int i = 0; i < entityGroupName.length; i++) {
                entityList.add(entityCache.getEntityGroupByName(entityGroupName[i]));
            }
            modelGroup.setEntityGroupList(entityList);
        } else {
            modelGroup.setEntityGroupList(entityList);
        }

        if ("True".equals(isSecured)) {
            modelGroup.setSecured(true);
        } else {
            modelGroup.setSecured(false);
        }
        modelGroup.setModelGroupName(modelGroupName);
        
        try {
            new DefaultBizLogic().insert(modelGroup, edu.wustl.common.util.global.Constants.HIBERNATE_DAO);
        } catch (UserNotAuthorizedException e) {
            e.printStackTrace();
        } catch (BizLogicException e) {
            e.printStackTrace();
        }

        return mapping.findForward(Constants.FORWARD_HOME);
    }
}
