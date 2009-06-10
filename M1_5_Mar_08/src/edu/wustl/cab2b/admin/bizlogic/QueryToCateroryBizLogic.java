package edu.wustl.cab2b.admin.bizlogic;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.common.queryengine.ICab2bParameterizedQuery;
import edu.wustl.cab2b.server.category.InputCategorialAttribute;
import edu.wustl.cab2b.server.category.InputCategorialClass;
import edu.wustl.cab2b.server.category.InputCategory;
import edu.wustl.common.querysuite.exceptions.MultipleRootsException;
import edu.wustl.common.querysuite.metadata.path.IPath;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IConstraints;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionId;
import edu.wustl.common.querysuite.queryobject.IExpressionOperand;
import edu.wustl.common.querysuite.queryobject.IRule;

/**
 * This class converts the Query object generated at the back end of the DAG into Category.
 *  
 * @author chetan_patil
 */
public class QueryToCateroryBizLogic {

    /** Map of ExpressionIds and IPath between them */
    private Map<Map<IExpressionId, IExpressionId>, IPath> expressionIDPathMap = new HashMap<Map<IExpressionId, IExpressionId>, IPath>();

    /** Map of category attribute's display name against an unique identifier */
    private Map<String, String> attributeNameMap = null;

    /** Query to be converted into category */
    private ICab2bParameterizedQuery query = null;

    /**
     * Parameterized constructor
     * @param query the query to be converted into category.
     * @param pathMap the map of ExpressionIds (source and target) and path between them from session.
     */
    public QueryToCateroryBizLogic(
            ICab2bParameterizedQuery query,
            Map<String, IPath> pathMap,
            Map<String, String> attributeNameMap) {
        super();
        this.query = query;
        this.attributeNameMap = attributeNameMap;
        populateExpressionIDPathMap(query.getConstraints(), pathMap);
    }

    /**
     * This method populates the expressionIDPathMap.
     * @param constraints
     * @param pathMap
     */
    private void populateExpressionIDPathMap(IConstraints constraints, Map<String, IPath> pathMap) {
        Set<String> keySet = pathMap.keySet();
        for (String key : keySet) {
            String[] expressionIntValue = key.split("_");

            IExpressionId sourceExpressionId = null;
            IExpressionId destinationExpressionId = null;
            Enumeration<IExpressionId> expressionIds = constraints.getExpressionIds();
            while (expressionIds.hasMoreElements()
                    && (sourceExpressionId == null || destinationExpressionId == null)) {
                IExpressionId tempExpressionId = expressionIds.nextElement();
                int value = tempExpressionId.getInt();

                String strValue = String.valueOf(value);
                if (expressionIntValue[1].compareTo(strValue) == 0) {
                    sourceExpressionId = tempExpressionId;
                } else if (expressionIntValue[2].compareTo(strValue) == 0) {
                    destinationExpressionId = tempExpressionId;
                }
            }

            Map<IExpressionId, IExpressionId> expressionIdMap = new HashMap<IExpressionId, IExpressionId>();
            expressionIdMap.put(sourceExpressionId, destinationExpressionId);

            IPath pathObject = pathMap.get(key);
            expressionIDPathMap.put(expressionIdMap, pathObject);
        }
    }

    /**
     * This method converts the query into InputCategory and returns the same.
     * @param categoryName name of the category
     * @param categoryDescription description of the category
     * @return
     * @throws RemoteException
     */
    public InputCategory convertQueryToCategory(String categoryName, String categoryDescription)
            throws RemoteException {
        IConstraints constraints = query.getConstraints();

        IExpressionId rootExpressionId = null;
        try {
            rootExpressionId = constraints.getRootExpressionId();
        } catch (MultipleRootsException e) {
            throw new RemoteException(e.getMessage());
        }
        IExpression rootExpression = constraints.getExpression(rootExpressionId);
        InputCategorialClass rootCategorialClass = getInputCategorialClass(constraints, rootExpression, null, null);
        rootCategorialClass.setPathFromParent(-1L);

        InputCategory inputCategory = new InputCategory();
        inputCategory.setSubCategories(new ArrayList<InputCategory>());
        inputCategory.setName(categoryName);
        inputCategory.setDescription(categoryDescription);
        inputCategory.setRootCategorialClass(rootCategorialClass);

        return inputCategory;
    }

    /**
     * 
     * @param constraints
     * @param currentExpression
     * @return
     */
    private InputCategorialClass getInputCategorialClass(IConstraints constraints, IExpression currentExpression,
                                                         IExpressionId visibleParentExpressionId,
                                                         List<InputCategorialClass> childernOfVisibleParent) {
        InputCategorialClass inputCategorialClass = new InputCategorialClass();

        List<InputCategorialClass> children = childernOfVisibleParent;
        if (currentExpression.isVisible()) {
            children = new ArrayList<InputCategorialClass>();
        }

        boolean isLeaf = true;
        IExpressionId currentExpressionId = currentExpression.getExpressionId();
        for (int index = 0; index < currentExpression.numberOfOperands(); index++) {
            IExpressionOperand operand = currentExpression.getOperand(index);
            if (operand instanceof IRule && currentExpression.isVisible()) {
                IRule rule = (IRule) operand;
                List<ICondition> conditions = rule.getConditions();
                List<InputCategorialAttribute> attributeList = getInputCategorialAttributes(currentExpressionId,
                                                                                            conditions);
                inputCategorialClass.setAttributeList(attributeList);
            } else if (operand instanceof IExpressionId) {
                isLeaf = false;
                IExpressionId childExpressionId = (IExpressionId) operand;
                IExpression childExpression = constraints.getExpression(childExpressionId);

                IExpressionId expressionId = visibleParentExpressionId;
                if (currentExpression.isVisible()) {
                    expressionId = currentExpressionId;
                }
                InputCategorialClass childCategorialClass = getInputCategorialClass(constraints, childExpression,
                                                                                    expressionId, children);
                if (childExpression.isVisible()) {
                    IPath path = getIPath(expressionId, childExpressionId);
                    childCategorialClass.setPathFromParent(path.getPathId());
                    children.add(childCategorialClass);
                }
            }
        }

        if (currentExpression.isVisible() && !isLeaf) {
            inputCategorialClass.setChildren(children);
        } else {
            inputCategorialClass.setChildren(new ArrayList<InputCategorialClass>());
        }
        return inputCategorialClass;
    }

    /**
     * 
     * @param conditions
     * @return
     */
    private List<InputCategorialAttribute> getInputCategorialAttributes(IExpressionId expressionId,
                                                                        List<ICondition> conditions) {
        List<InputCategorialAttribute> attributeList = new ArrayList<InputCategorialAttribute>();

        for (ICondition condition : conditions) {
            AttributeInterface sourceAttribute = condition.getAttribute();

            InputCategorialAttribute inputCategorialAttribute = new InputCategorialAttribute();
            inputCategorialAttribute.setDynamicExtAttribute(sourceAttribute);
            setDisplayAttributeNames(inputCategorialAttribute, expressionId.getInt());
            attributeList.add(inputCategorialAttribute);
        }

        return attributeList;
    }

    /**
     * This method sets the display name into InputCategorialAttribute.
     * @param inputCategorialAttribute
     * @param expressionId
     */
    private void setDisplayAttributeNames(InputCategorialAttribute inputCategorialAttribute, int expressionId) {
        String strExpressionId = String.valueOf(expressionId);
        AttributeInterface sourceAttribute = inputCategorialAttribute.getDynamicExtAttribute();
        String attributeName = sourceAttribute.getName();

        Set<String> keySet = attributeNameMap.keySet();
        for (String key : keySet) {
            String[] keyComponents = key.split("\\.");

            if (strExpressionId.compareTo(keyComponents[0]) == 0 && attributeName.compareTo(keyComponents[1]) == 0) {
                String displayAttributeName = attributeNameMap.get(key);
                if (displayAttributeName != null && displayAttributeName.length() > 0) {
                    inputCategorialAttribute.setDisplayName(displayAttributeName);
                }
            }
        }
    }

    /**
     * 
     * @param sourcExpressionId
     * @param destinationExpressionId
     * @return
     */
    private IPath getIPath(IExpressionId sourcExpressionId, IExpressionId destinationExpressionId) {
        IPath path = null;

        Set<Map<IExpressionId, IExpressionId>> expressionIdMapKeys = expressionIDPathMap.keySet();
        for (Map<IExpressionId, IExpressionId> expressionMapKey : expressionIdMapKeys) {
            if (expressionMapKey.containsKey(sourcExpressionId)
                    && expressionMapKey.containsValue(destinationExpressionId)) {
                path = expressionIDPathMap.get(expressionMapKey);
                break;
            }
        }

        return path;
    }

}