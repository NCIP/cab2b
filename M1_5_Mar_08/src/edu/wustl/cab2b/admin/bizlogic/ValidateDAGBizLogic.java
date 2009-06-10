package edu.wustl.cab2b.admin.bizlogic;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.querysuite.QueryCSMUtil;
import edu.wustl.catissuecore.util.querysuite.QueryModuleUtil;
import edu.wustl.common.querysuite.exceptions.MultipleRootsException;
import edu.wustl.common.querysuite.exceptions.SqlException;
import edu.wustl.common.querysuite.factory.SqlGeneratorFactory;
import edu.wustl.common.querysuite.queryengine.impl.SqlGenerator;
import edu.wustl.common.querysuite.queryobject.IConstraints;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionId;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.querysuite.queryobject.impl.OutputTreeDataNode;
import edu.wustl.common.querysuite.queryobject.util.QueryObjectProcessor;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;

/**
 * 
 * @author lalit_chand
 *
 */

public class ValidateDAGBizLogic {
    
   public  ValidateDAGBizLogic(){
        
     ApplicationProperties.initBundle("ApplicationResources");   
        
    }
    
    

    public String getValidateMessage(IQuery query, HttpServletRequest request) {

        String validationMessage = "noexception";
        boolean isRulePresentInDag = QueryModuleUtil.checkIfRulePresentInDag(query);
        if (!isRulePresentInDag) {
            validationMessage = "Please  Add some Rules";

            return validationMessage;
        }
        IConstraints constraints = query.getConstraints();
        Enumeration<IExpressionId> expressionIds = constraints.getExpressionIds();
        boolean noExpressionInView = true;
        while (expressionIds.hasMoreElements()) {
            IExpressionId nextElement = expressionIds.nextElement();
            IExpression expression = constraints.getExpression(nextElement);
            if (expression.isInView()) {
                noExpressionInView = false;
                break;
            }
        }
        
        if (noExpressionInView) {
            validationMessage = ApplicationProperties.getValue("query.defineView.noExpression.message");
            return validationMessage;
        }
        try {
            SqlGenerator sqlGenerator = (SqlGenerator) SqlGeneratorFactory.getInstance();
            String selectSql = sqlGenerator.generateSQL(query);
            HttpSession session = request.getSession();
            session.setAttribute(Constants.SAVE_GENERATED_SQL, selectSql);
            List<OutputTreeDataNode> rootOutputTreeNodeList = sqlGenerator.getRootOutputTreeNodeList();
            session.setAttribute(Constants.SAVE_TREE_NODE_LIST, rootOutputTreeNodeList);
            Map<String, OutputTreeDataNode> uniqueIdNodesMap = QueryObjectProcessor.getAllChildrenNodes(rootOutputTreeNodeList);
            //This method will check if main objects for all the dependant objects are present in query or not.
            Map<EntityInterface, List<EntityInterface>> mainEntityMap = QueryCSMUtil.setMainObjectErrorMessage(
                                                                                                               query,
                                                                                                               session,
                                                                                                               uniqueIdNodesMap);
            session.setAttribute(Constants.ID_NODES_MAP, uniqueIdNodesMap);
            // if no main object is present in the map show the error message set in the session.
            if (mainEntityMap == null) {
                //return NO_MAIN_OBJECT_IN_QUERY;
                validationMessage = (String) session.getAttribute(Constants.NO_MAIN_OBJECT_IN_QUERY);
                validationMessage = "<li><font color='blue' family='arial,helvetica,verdana,sans-serif'>"
                        + validationMessage + "</font></li>";
            }
        } catch (MultipleRootsException e) {
            Logger.out.error(e);
            validationMessage = ApplicationProperties.getValue("errors.executeQuery.multipleRoots");
        } catch (SqlException e) {
            Logger.out.error(e);
            validationMessage = ApplicationProperties.getValue("errors.executeQuery.genericmessage");
        }

        return validationMessage;

    }

}
