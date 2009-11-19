package edu.wustl.cab2bwebapp.bizlogic.executequery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2bwebapp.constants.AddLimitConstants;
import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IConstraints;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionOperand;
import edu.wustl.common.querysuite.queryobject.IParameter;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.querysuite.queryobject.IRule;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;
import edu.wustl.common.querysuite.queryobject.impl.ParameterizedQuery;
import edu.wustl.common.util.Collections;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;

/**
 * @author chetan_pundhir   
 *
 */
public class QueryUpdateBizLogic {
    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(QueryUpdateBizLogic.class);

    /**
     * @param queryInputString
     * @param constraints
     * @param displayNamesMap
     * @param query
     * @return String
     */
    public String setInputDataToQuery(String queryInputString, IConstraints constraints,
                                      Map<String, String> displayNamesMap, IQuery query) {
        String errorMessage = "";
        Map<String, String[]> newConditions = null;
        if (queryInputString != null) {
            newConditions = createConditionsMap(queryInputString);
        }
        for (IExpression expression : constraints) {
            int no_of_oprds = expression.numberOfOperands();
            IExpressionOperand operand;
            for (int i = 0; i < no_of_oprds; i++) {
                operand = expression.getOperand(i);
                if (operand instanceof IRule) {
                    int expId = expression.getExpressionId();
                    IRule rule = (IRule) operand;
                    errorMessage =
                            componentValues(displayNamesMap, errorMessage, newConditions, expId, rule, query);
                    if (displayNamesMap == null && (Collections.list((IRule) operand)).size() == 0) {
                        IExpression expression1 = rule.getContainingExpression();
                        AttributeInterface attributeObj =
                                getIdNotNullAttribute(expression1.getQueryEntity().getDynamicExtensionsEntity());

                        if (attributeObj != null) {
                            ICondition condition = createIdNotNullCondition(attributeObj);
                            rule.addCondition(condition);
                        }
                    }
                }
            }
        }
        return errorMessage;
    }

    /**
     * @param queryString
     * @return Map<String, String[]>
     */
    private Map<String, String[]> createConditionsMap(String queryString) {
        Map<String, String[]> conditionsMap = new HashMap<String, String[]>();
        String[] conditions = queryString.split(AddLimitConstants.QUERY_CONDITION_DELIMITER);
        String[] attrParams;
        String condition;
        int len = conditions.length;
        for (int i = 0; i < len; i++) {
            attrParams = new String[AddLimitConstants.INDEX_LENGTH];
            condition = conditions[i];
            if (!condition.equals("")) {
                condition =
                        condition.substring(AddLimitConstants.ARGUMENT_ZERO, condition
                            .indexOf(AddLimitConstants.ENTITY_SEPARATOR));
                String attrName = null;
                StringTokenizer tokenizer =
                        new StringTokenizer(condition, AddLimitConstants.QUERY_OPERATOR_DELIMITER);
                while (tokenizer.hasMoreTokens()) {
                    attrName = tokenizer.nextToken();
                    if (tokenizer.hasMoreTokens()) {
                        String operator = tokenizer.nextToken();
                        attrParams[AddLimitConstants.INDEX_PARAM_ZERO] = operator;
                        if (tokenizer.hasMoreTokens()) {
                            attrParams[1] = tokenizer.nextToken();
                            if (RelationalOperator.Between.toString().equals(operator)) {
                                attrParams[AddLimitConstants.INDEX_PARAM_TWO] = tokenizer.nextToken();
                            }
                        }
                    }
                }
                conditionsMap.put(attrName, attrParams);
            }
        }
        return conditionsMap;
    }

    /**
     * @param displayNamesMap
     * @param errorMessage
     * @param newConditions
     * @param expId
     * @param rule
     * @param query
     * @return String
     */
    private String componentValues(Map<String, String> displayNamesMap, String errorMessage,
                                   Map<String, String[]> newConditions, int expId, IRule rule, IQuery query) {
        ICondition condition;
        String componentName;
        ArrayList<ICondition> removalList = new ArrayList<ICondition>();
        List<ICondition> deafultConditions = new ArrayList<ICondition>();
        int size = rule.size();
        ParameterizedQuery pQuery = null;
        if (query instanceof ParameterizedQuery) {
            pQuery = (ParameterizedQuery) query;
        }
        for (int j = 0; j < size; j++) {
            condition = rule.getCondition(j);
            componentName = generateComponentName(expId, condition.getAttribute());
            if (newConditions != null && newConditions.containsKey(componentName)) {
                String[] params = newConditions.get(componentName);
                ArrayList<String> attributeValues = getConditionValuesList(params);
                errorMessage =
                        errorMessage
                                + validateAttributeValues(condition.getAttribute().getDataType().toString(),
                                                          attributeValues);

                if (displayNamesMap != null && !(displayNamesMap.containsKey(componentName))) {
                } else {
                    condition.setValues(attributeValues);
                    condition.setRelationalOperator(RelationalOperator
                        .getOperatorForStringRepresentation(params[AddLimitConstants.INDEX_PARAM_ZERO]));
                }
            }
            if ((!newConditions.containsKey(componentName)) && (displayNamesMap == null)) {
                removalList.add(condition);
                if (query instanceof ParameterizedQuery) {
                    pQuery = (ParameterizedQuery) query;
                    List<IParameter<?>> parameterList = pQuery.getParameters();
                    boolean isparameter = false;
                    if (parameterList != null) {
                        for (IParameter<?> parameter : parameterList) {
                            if (parameter.getParameterizedObject() instanceof ICondition) {
                                ICondition paramCondition = (ICondition) parameter.getParameterizedObject();
                                if (paramCondition.getId().equals(condition.getId())) {
                                    isparameter = true;
                                }
                            }
                        }
                    }
                    if (!isparameter) {
                        deafultConditions.add(condition);
                    }
                }
            }
            if (displayNamesMap != null && displayNamesMap.containsKey(componentName)) {

                IParameter<ICondition> parameter =
                        QueryObjectFactory.createParameter(condition, displayNamesMap.get(componentName));
                pQuery.getParameters().add(parameter);
            }
        }
        for (ICondition removalEntity : removalList) {
            if (!deafultConditions.contains(removalEntity)) {
                rule.removeCondition(removalEntity);
            }
        }
        return errorMessage;
    }

    /**
     * @param entityInterfaceObj
     * @return AttributeInterface
     */
    private AttributeInterface getIdNotNullAttribute(EntityInterface entityInterfaceObj) {
        Collection<AttributeInterface> attributes = entityInterfaceObj.getEntityAttributesForQuery();
        for (AttributeInterface attribute : attributes) {
            if (attribute.getName().equals(AddLimitConstants.ID)) {
                return attribute;
            }
        }
        return null;
    }

    /**
     * @param attributeObj
     * @return ICondition
     */
    private ICondition createIdNotNullCondition(AttributeInterface attributeObj) {
        ICondition condition =
                QueryObjectFactory.createCondition(attributeObj, RelationalOperator.IsNotNull, null);
        return condition;
    }

    /**
     * @param expressionId
     * @param attribute
     * @return String
     */
    private String generateComponentName(int expressionId, AttributeInterface attribute) {
        String componentId = expressionId + AddLimitConstants.UNDERSCORE + attribute.getId().toString();
        return componentId;
    }

    /**
     * @param params
     * @return ArrayList<String>
     */
    private ArrayList<String> getConditionValuesList(String[] params) {
        ArrayList<String> attributeValues = new ArrayList<String>();
        if (params[1] != null) {
            String[] values = params[1].split(AddLimitConstants.QUERY_VALUES_DELIMITER);
            int len = values.length;
            for (int i = 0; i < len; i++) {
                if (!"".equals(values[i])) {
                    attributeValues.add(values[i].trim());
                }
            }
        }
        if (params[2] != null) {
            attributeValues.add(params[2].trim());
        }
        return attributeValues;
    }

    /**
     * @param dataType
     * @param attrvalues
     * @return String
     */
    private String validateAttributeValues(String dataType, List<String> attrvalues) {
        Validator validator = new Validator();
        String errorMessages = "";
        for (String enteredValue : attrvalues) {
            if (AddLimitConstants.MISSING_TWO_VALUES.equalsIgnoreCase(enteredValue)) {
                errorMessages = getErrorMessageForBetweenOperator(errorMessages, enteredValue);
            } else if ((AddLimitConstants.BIG_INT.equalsIgnoreCase(dataType) || AddLimitConstants.INTEGER
                .equalsIgnoreCase(dataType))
                    || AddLimitConstants.LONG.equalsIgnoreCase(dataType)) {
                logger.debug(" Check for integer");

                if (validator.convertToLong(enteredValue) == null) {
                    errorMessages =
                            errorMessages + ApplicationProperties.getValue("simpleQuery.intvalue.required");
                    logger.debug(enteredValue + " is not a valid integer");
                } else if (!validator.isPositiveNumeric(enteredValue, AddLimitConstants.ARGUMENT_ZERO)) {
                    errorMessages = getErrorMessageForPositiveNum(errorMessages, enteredValue);
                }

            }// integer
            else if ((AddLimitConstants.DOUBLE.equalsIgnoreCase(dataType))
                    && !validator.isDouble(enteredValue, false)) {
                errorMessages = errorMessages + ApplicationProperties.getValue("simpleQuery.decvalue.required");
            } // double
            else if (AddLimitConstants.TINY_INT.equalsIgnoreCase(dataType)) {
                if (!AddLimitConstants.BOOLEAN_YES.equalsIgnoreCase(enteredValue.trim())
                        && !AddLimitConstants.BOOLEAN_NO.equalsIgnoreCase(enteredValue.trim())) {
                    errorMessages = errorMessages + ApplicationProperties.getValue("simpleQuery.tinyint.format");
                }
            } else if (edu.wustl.common.util.global.Constants.FIELD_TYPE_TIMESTAMP_TIME.equalsIgnoreCase(dataType)) {
                errorMessages = getErrorMessageForTimeFormat(validator, errorMessages, enteredValue);
            } else if (edu.wustl.common.util.global.Constants.FIELD_TYPE_DATE.equalsIgnoreCase(dataType)
                    || edu.wustl.common.util.global.Constants.FIELD_TYPE_TIMESTAMP_DATE.equalsIgnoreCase(dataType)) {
                errorMessages = getErrorMessageForDateFormat(validator, errorMessages, enteredValue);
            }
        }
        return errorMessages;
    }

    /**
     * @param errorMessages
     * @param enteredValue
     * @return String
     */
    private String getErrorMessageForBetweenOperator(String errorMessages, String enteredValue) {
        errorMessages =
                errorMessages + "<li><font color\\='red'>"
                        + ApplicationProperties.getValue("simpleQuery.twovalues.required") + "</font></li>";
        logger.debug(enteredValue + " two values required for 'Between' operator ");
        return errorMessages;
    }

    /**
     * @param errorMessages
     * @param enteredValue
     * @return String
     */
    private String getErrorMessageForPositiveNum(String errorMessages, String enteredValue) {
        errorMessages =
                errorMessages + "<li><font color\\='red'>"
                        + ApplicationProperties.getValue("simpleQuery.intvalue.poisitive.required")
                        + "</font></li>";
        logger.debug(enteredValue + " is not a positive integer");
        return errorMessages;
    }

    /**
     * @param validator
     * @param errorMessages
     * @param enteredValue
     * @return String
     */
    private String getErrorMessageForTimeFormat(Validator validator, String errorMessages, String enteredValue) {
        if (!validator.isValidTime(enteredValue, edu.wustl.common.util.global.Constants.TIME_PATTERN_HH_MM_SS)) {
            errorMessages =
                    errorMessages + "<li><font color\\='red'>"
                            + ApplicationProperties.getValue("simpleQuery.time.format") + "</font></li>";
        }
        return errorMessages;
    }

    /**
     * @param validator
     * @param errorMessages
     * @param enteredValue
     * @return String
     */
    private String getErrorMessageForDateFormat(Validator validator, String errorMessages, String enteredValue) {
        if (!validator.checkDate(enteredValue)) {
            errorMessages =
                    errorMessages + "<li><font color\\='red'>"
                            + ApplicationProperties.getValue("simpleQuery.date.format") + "</font></li>";
        }
        return errorMessages;
    }
}