/**
 * 
 */
package edu.wustl.cab2b.admin.searchdata.action;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

import edu.wustl.cab2b.admin.action.BaseAction;
import edu.wustl.cab2b.admin.bizlogic.QueryToCateroryBizLogic;
import edu.wustl.cab2b.admin.util.Cab2bConstants;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.queryengine.ICab2bParameterizedQuery;
import edu.wustl.cab2b.server.category.CategoryOperations;
import edu.wustl.cab2b.server.category.InputCategory;
import edu.wustl.cab2b.server.category.PersistCategory;
import edu.wustl.catissuecore.flex.dag.DAGConstant;
import edu.wustl.common.querysuite.metadata.category.Category;
import edu.wustl.common.querysuite.metadata.path.IPath;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionId;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.querysuite.queryobject.IRule;

/**
 * @author atul_jawale
 * @author lalit_chand
 */
public class CreateCategoryAttributeOrder extends BaseAction implements ServletRequestAware, SessionAware {

    /**
     * 
     */

    private static final long serialVersionUID = 5127133829185857760L;

    private Map session;

    private String action = null;

    private String title;

    private String description;

    private HttpServletRequest servletRequest = null;

    public void setServletRequest(HttpServletRequest req) {
        this.servletRequest = req;
    }

    /**
     * @return the request
     */
    public HttpServletRequest getRequest() {
        return servletRequest;
    }

    public void setSession(final Map session) {
        this.session = session;
    }

    /**
     * @return the session
     */
    public Map getSession() {
        return session;
    }

    /**
     * @return the action
     */
    public String getAction() {
        return action;
    }

    /**
     * @param action the action to set
     */
    public void setAction(String action) {
        this.action = action;
    }

    public String execute() throws RemoteException {
        IQuery query = (IQuery) getSession().get(DAGConstant.QUERY_OBJECT);
        Map<Integer, List<String>> allAttributes = null;

        if (getAction() != null) {
            Map<String, IPath> pathMap = (Map<String, IPath>) getSession().get("pathMap");
            Map<String, String> unFormattedAttributeMap = (Map<String, String>) session.get("unFormattedAttributeMap");

            allAttributes = (Map<Integer, List<String>>) getSession().get("attributeList");
            Map<String, String> allAttributeDisplayNameMap = new HashMap<String, String>();
            for (Integer expressionId : allAttributes.keySet()) {
                List<String> expIDAttributeList = allAttributes.get(expressionId);
                for (String expIdAttributeName : expIDAttributeList) {
                    String[] temp = expIdAttributeName.split("\\.");
                    String attributeDisplayName = getRequest().getParameter(expIdAttributeName);
                    attributeDisplayName = attributeDisplayName == null ? expIdAttributeName
                            : attributeDisplayName;
                    allAttributeDisplayNameMap.put(temp[0] + "." + unFormattedAttributeMap.get(temp[1]),
                                                   attributeDisplayName);
                }
            }

            QueryToCateroryBizLogic bizLogic = new QueryToCateroryBizLogic((ICab2bParameterizedQuery) query,
                    pathMap, allAttributeDisplayNameMap);
            InputCategory inputCategory = bizLogic.convertQueryToCategory(getTitle(), getDescription());
            try {
                Category category = new PersistCategory().persistCategory(inputCategory, null);
                new CategoryOperations().saveCategory(category);
            } catch (Exception e) {
                getRequest().setAttribute("exceptionMessage", e.getMessage());
                return Cab2bConstants.FAILURE;

            } finally {
                getSession().remove("attributeList");
                getSession().remove("pathMap");
            }
           
            return Cab2bConstants.PASS;
        } else {
            allAttributes = new HashMap<Integer, List<String>>();

            Enumeration<IExpressionId> enumeration = query.getConstraints().getExpressionIds();
            Map<String, String> unFormattedAttributeMap = new HashMap<String, String>();
            while (enumeration.hasMoreElements()) {
                List<String> attributes = new ArrayList<String>();
                IExpression expression = query.getConstraints().getExpression(enumeration.nextElement());
                if (expression.isVisible()) {
                    int nodeId = expression.getExpressionId().getInt();
                    IRule rule = (IRule) expression.getOperand(0);
                    for (int i1 = 0; i1 < rule.size(); i1++) {
                        ICondition condition = rule.getCondition(i1);
                        attributes.add(nodeId + "."
                                + CommonUtils.getFormattedString(condition.getAttribute().getName()));
                        unFormattedAttributeMap.put(
                                                    CommonUtils.getFormattedString(condition.getAttribute().getName()),
                                                    condition.getAttribute().getName());
                    }

                    allAttributes.put(nodeId, attributes);
                }
            }
            getSession().put("unFormattedAttributeMap", unFormattedAttributeMap);
            getSession().put("attributeList", allAttributes);
        }
        return Cab2bConstants.SUCCESS;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

}
