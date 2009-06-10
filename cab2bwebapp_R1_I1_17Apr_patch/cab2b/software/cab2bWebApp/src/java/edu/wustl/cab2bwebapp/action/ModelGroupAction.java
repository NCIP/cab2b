/**
 * 
 */
package edu.wustl.cab2bwebapp.action;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2bwebapp.bizlogic.ApplicationBizLogic;
import edu.wustl.cab2bwebapp.constants.Constants;

/**
 * @author gaurav_mehta
 *
 */
public class ModelGroupAction extends Action{

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) {
        Collection<EntityGroupInterface> entityCollection = new ApplicationBizLogic().getEntityGroups();
        request.setAttribute(Constants.ENTITY_GROUP_COLLECTION, entityCollection);
        return mapping.findForward(Constants.FORWARD_MODEL_GROUP);
    }
}
