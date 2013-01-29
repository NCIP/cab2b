/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2bwebapp.bizlogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import edu.common.dynamicextensions.domain.DateAttributeTypeInformation;
import edu.common.dynamicextensions.domain.PermissibleValue;
import edu.common.dynamicextensions.domain.UserDefinedDE;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.BooleanValueInterface;
import edu.common.dynamicextensions.domaininterface.DateValueInterface;
import edu.common.dynamicextensions.domaininterface.DoubleValueInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.FloatValueInterface;
import edu.common.dynamicextensions.domaininterface.IntegerValueInterface;
import edu.common.dynamicextensions.domaininterface.LongValueInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.domaininterface.ShortValueInterface;
import edu.common.dynamicextensions.domaininterface.StringValueInterface;
import edu.common.dynamicextensions.domaininterface.UserDefinedDEInterface;
import edu.wustl.cab2b.common.exception.CheckedException;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.util.AttributeInterfaceComparator;
import edu.wustl.cab2b.common.util.PermissibleValueComparator;
import edu.wustl.cab2bwebapp.constants.AddLimitConstants;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionOperand;
import edu.wustl.common.querysuite.queryobject.IParameter;
import edu.wustl.common.querysuite.queryobject.IParameterizedQuery;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.querysuite.queryobject.IRule;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;
import edu.wustl.common.querysuite.utils.QueryUtility;
import edu.wustl.common.util.ParseXMLFile;
import edu.wustl.common.util.Utility;

/**
 * This class generates dynamic UI for 'Add Limits' page.
 * @author chetan_pundhir
 */
public class AddLimitHTMLGeneratorBizLogic {

    /**
     * Resource bundle for text resources on dynamic UI.
     */
    private ResourceBundle bundle = ResourceBundle.getBundle("ApplicationResources");

    /**
     * Object which holds data operators from attributes.
     */
    private static ParseXMLFile parseFile = null;

    private int expressionId = -1;

    private String attributesList = "";

    /**
     * Parameterized Constructor
     * @param parseFilePath
     */
    public AddLimitHTMLGeneratorBizLogic(String parseFilePath) {
        try {
            parseFile = ParseXMLFile.getInstance(parseFilePath);
        } catch (CheckedException e) {
            StringBuffer error =
                    new StringBuffer("Error while generating the metadata for HTML generator! Please check file.");
            error.append(parseFilePath);
            error.append(": ");
            error.append(e.getMessage());
            throw new RuntimeException(error.toString());
        }
    }

    /**
     * This method accesses expressionId, entity and conditions from the query object to generate dynamic UI.
     * @param queryObject
     * @return
     */
    public String getHTMLForSavedQuery(IQuery queryObject) {
        List<IParameter<?>> parameterList = null;
        if (queryObject instanceof IParameterizedQuery) {
            IParameterizedQuery pQuery = (IParameterizedQuery) queryObject;
            parameterList = pQuery.getParameters();
            
   // JJJ         
/*
            Collection<ICondition> JJJparamConditions = QueryUtility.getAllParameterizedConditions(pQuery);
            
            for(ICondition p : JJJparamConditions){
            	if(p == null){
            		System.out.println("@@@ NULL PARAMETER");
            	} else {
            		
            		System.out.println("@@@ val="+p.getValue() +" attribute="+
            		p.getAttribute()+" p.valueS="+
            		p.getValues()+" p.getId="+
            		p.getId()+ " p="+p);
            		
//                    ICondition paramCondition = (ICondition) ((IParameter<?>) p).getParameterizedObject();
//            		System.out.println("&&&& p.getName="+
//                    		p.getName()+ " getParamObj="+
//                    		p.getParameterizedObject()+" parmCondition.getId= "+paramCondition.getId());
            	}
            }
*/
  // JJJ

        }
/*
        System.out.println("JJJ # parameterList size="+parameterList.size());
        
        for(IParameter<?> p : parameterList){
        	if(p == null){
        		System.out.println("&&&& NULL PARAMETER");
        	} else {
        		
                ICondition paramCondition = (ICondition) p.getParameterizedObject();
        		System.out.println("&&&& p.getName="+
                		p.getName()+ " getParamObj="+
                		p.getParameterizedObject()+" parmCondition.getId= "+paramCondition.getId()+"p cond="+paramCondition);
        	}
        }
*/
        
        



        final StringBuffer htmlString = new StringBuffer();
        if (!parameterList.isEmpty()) {
            Map<Integer, EntityConditions> expIdEntityConditionsMap = new HashMap<Integer, EntityConditions>();
            for (IExpression expression : queryObject.getConstraints()) {
                for (int index = 0; index < expression.numberOfOperands(); index++) {
                    IExpressionOperand operand = expression.getOperand(index);
                    if (operand instanceof IRule) {
                        IRule rule = (IRule) operand;

                        List<ICondition> conditions = edu.wustl.common.util.Collections.list(rule);
                        EntityInterface entity = expression.getQueryEntity().getDynamicExtensionsEntity();


                        EntityConditions entityConditions = new EntityConditions(entity, conditions);
                        expIdEntityConditionsMap.put(expression.getExpressionId(), entityConditions);

                    } 
                }
            }

            htmlString.append(createTableForSavedQuery());
            htmlString.append(generateHTMLForSavedQuery(expIdEntityConditionsMap, parameterList))
                .append("</table>");
        }


        return htmlString.toString();
    }

    /**
     * This method creates a table skeleton
     * @return
     */
    private String createTableForSavedQuery() {
        StringBuffer generatedHTML =
                new StringBuffer(
                        "<table cellpadding='3' cellspacing='0' border='0' width='100%' id='definelimitspaneltable'>");
        generatedHTML.append("<tr><td class='labelpanel' valign='top'>" + bundle.getString("label.variables")
                + "</td><td class='labelpanel' valign='top'>" + bundle.getString("label.condition")
                + "</td><td class='labelpanel' colspan='4' valign='top'>" + bundle.getString("label.value")
                + "</td></tr>");
        return generatedHTML.toString();
    }

    /*
     * This method generates the html for Save Query section. This internally
     * calls methods to generate other UI components like text, Calendar,
     * Combobox etc. This method is same as the generateHTML except that this
     * will generate html for selected conditions and will display only those
     * conditions with their values set by user. @param entity entity to be
     * presented on UI. @param conditions List of conditions , will contains
     * atleast one element always. @return String html generated for Save Query
     * section.
     */
    private String generateHTMLForSavedQuery(Map<Integer, EntityConditions> expIdEntityConditionsMap,
                                             List<IParameter<?>> parameterList) {
        String htmlGenerated = "No record found.";

        if (!expIdEntityConditionsMap.isEmpty()) {
            StringBuffer generatedHTML = new StringBuffer();
            StringBuffer expressionEntityString = new StringBuffer();

            Collection<Integer> expressionIds = expIdEntityConditionsMap.keySet();
            for (Integer expressionId : expressionIds) {
                EntityConditions entityConditions = expIdEntityConditionsMap.get(expressionId);
                final EntityInterface entity = entityConditions.getEntity();
                

                generatedHTML.append(generateSaveQueryForEntity(expressionId, entityConditions, parameterList));

                expressionEntityString.append(expressionId.intValue()).append(':');
                expressionEntityString.append(Utility.parseClassName(entity.getName())).append(';');
            }

            generatedHTML.append("<input type='hidden' id='totalentities' value='"
                    + expressionEntityString.toString() + "' />");
            generatedHTML.append("<input type='hidden' id='attributesList' value='" + attributesList + "' />");
            generatedHTML.append("<input type='hidden' id='conditionList' name='conditionList' value='' />");

            htmlGenerated = generatedHTML.toString();
        }
        return htmlGenerated;
    }

    /**
     *
     * @param expressionID
     * @param entity
     * @param conditions
     * @param isShowAll
     * @param isTopButton
     * @return
     */
    private StringBuffer generateSaveQueryForEntity(Integer expressionID, EntityConditions entityConditions,
                                                    List<IParameter<?>> parameterList) {
        StringBuffer generatedHTML = new StringBuffer();
        Collection<AttributeInterface> attributeCollection = new ArrayList<AttributeInterface>();

        Map<String, ICondition> attribNameConditionMap =
                getAttributeNameConditionMap(entityConditions.getConditions());

        final EntityInterface entity = entityConditions.getEntity();
        List<AttributeInterface> entityAttributes =
                new ArrayList<AttributeInterface>(entity.getEntityAttributesForQuery());
        Collections.sort(entityAttributes, new AttributeInterfaceComparator());

        boolean isBGColor = false;
        for (AttributeInterface attribute : entityAttributes) {
            final String attributeName = attribute.getName();
            if (!attributeName.equalsIgnoreCase("id") && attribNameConditionMap.containsKey(attributeName)) {

                attributeCollection.add(attribute);
                
                String styleSheetClass = "formfieldspanel";
                if (!isBGColor) {
                    isBGColor = !isBGColor;
                    styleSheetClass = "formfieldspanelalternate";
                }
                generatedHTML.append("\n<tr class='" + styleSheetClass + "'" + " id='componentId'>\n");

                IParameter<?> paramater =
                        getParameterForCondition(attribNameConditionMap.get(attributeName), parameterList);
                if (paramater != null) {
                    attributesList += generateComponentName(expressionID, attribute) + ";";

                    generatedHTML
                        .append("<td valign='top' align='left' class='querylimittext' nowrap='nowrap' width=\"15%\">"
                                + paramater.getName());


                    if (attribute.getDataType().equalsIgnoreCase(AddLimitConstants.DATE)) {
                        generatedHTML.append("\n(" + AddLimitConstants.DATE_FORMAT + ")");
                    }

                    generatedHTML.append("</b></td>\n");
                } 
                generatedHTML.append(generateHTMLForConditions(attribute, attribNameConditionMap, parameterList))
                    .append("\n</tr>");
            }
        }
        generatedHTML.append("<input type='hidden' id='" + expressionID + ":"
                + Utility.parseClassName(entity.getName()) + "_attributeList'" + "value="
                + getAttributesString(attributeCollection) + " />");
        return generatedHTML;
    }

    private IParameter<?> getParameterForCondition(ICondition condition, List<IParameter<?>> parameterList) {
        IParameter<?> parameterized = null;
        
        if (condition != null) {
            for (IParameter<?> parameter : parameterList) {
                if (parameter.getParameterizedObject() instanceof ICondition) {
                    ICondition paramCondition = (ICondition) parameter.getParameterizedObject();

                    if (paramCondition.getId() == condition.getId()) {
                        parameterized = parameter;
                        
                        break;
                    }
                } 
            }
        } 
        
        if(parameterized==null){
        	System.out.println("$$gp4c NO MATCH cond.id="+condition.getId()+" cond="+condition);
        	
        }

        return parameterized;
    }

    private String generateComponentName(Integer expressionId, AttributeInterface attribute) {
        this.expressionId = expressionId.intValue();
        return generateComponentName(attribute);
    }

    private String generateComponentName(AttributeInterface attribute) {
        StringBuffer componentId = new StringBuffer();
        String attributeName = "";
        if (expressionId > -1) {
            componentId.append(expressionId).append('_');
        } else {
            attributeName = attribute.getName();
        }
        componentId.append(attributeName + attribute.getId().longValue());

        return componentId.toString();
    }

    /**
     * Method for generating HTML depending on condition
     * @param generatedHTML
     * @param attribute
     * @param operatorsList
     * @param isBetween
     * @param conditions
     * @param attributeNameConditionMap
     * @param attrName
     */
    private StringBuffer generateHTMLForConditions(AttributeInterface attribute,
                                                   Map<String, ICondition> attributeNameConditionMap,
                                                   List<IParameter<?>> parameterList) {
        boolean isBetween = false;
        List<String> operatorsList = getOperatorsList(attribute);
        if (!operatorsList.isEmpty()) {
            isBetween = operatorsList.get(0).equalsIgnoreCase(RelationalOperator.Between.toString());
        }

        StringBuffer generatedHTML = new StringBuffer();

        List<PermissibleValueInterface> permissibleValues = getPermissibleValuesList(attribute);
        if (attributeNameConditionMap.containsKey(attribute.getName())) {
            ICondition condition = attributeNameConditionMap.get(attribute.getName());

            IParameter<?> parameter = getParameterForCondition(condition, parameterList);
            if (parameter != null) {
            	//JJJ try forcing anyway
            	//System.out.println("JJJ *******************************WARNING, gonna try forcing HTML generation in generateHTMLForConditions");
                //JJJ
                String operator = condition.getRelationalOperator().getStringRepresentation();
                generatedHTML.append('\n').append(
                                                  generateHTMLForOperators(attribute, operatorsList, operator,
                                                                           "permissiblevaluesquery"));

                isBetween = operator.equalsIgnoreCase(RelationalOperator.Between.toString());
                List<String> values = condition.getValues();
                if (!permissibleValues.isEmpty() && permissibleValues.size() < 500) {
                    generatedHTML.append('\n').append(
                                                      generateHTMLForEnumeratedValues(attribute,
                                                                                      permissibleValues, values,
                                                                                      "permissiblevaluesquery"));
                } else if (attribute.getDataType().equalsIgnoreCase("boolean")) {
                    generatedHTML.append('\n').append(
                                                      generateHTMLForRadioButton(attribute, values,
                                                                                 "querylimittext"));
                } else {
                    generatedHTML.append('\n').append(
                                                      generateHTMLForTextBox(attribute, isBetween, values,
                                                                             operator, "querylimittext"));
                }
            }
        } else {
            generatedHTML.append(generateHTMLForConditionNull(attribute, operatorsList, permissibleValues,
                                                              isBetween));
        }

        return generatedHTML;
    }

    /**
     * returns PermissibleValuesList' list for attribute
     *
     * @param attribute
     *            AttributeInterface
     * @return List of permissible values for the passed attribute
     */
    private List<PermissibleValueInterface> getPermissibleValuesList(AttributeInterface attribute) {
        UserDefinedDE userDefineDE = (UserDefinedDE) attribute.getAttributeTypeInformation().getDataElement();
        List<PermissibleValueInterface> permissibleValues = new ArrayList<PermissibleValueInterface>();
        if (userDefineDE != null && userDefineDE.getPermissibleValueCollection() != null) {
            Iterator<PermissibleValueInterface> permissibleValueInterface =
                    userDefineDE.getPermissibleValueCollection().iterator();
            while (permissibleValueInterface.hasNext()) {
                PermissibleValue permValue = (PermissibleValue) permissibleValueInterface.next();
                if (permValue instanceof StringValueInterface) {
                    permissibleValues.add(((StringValueInterface) permValue));
                } else if (permValue instanceof ShortValueInterface) {
                    permissibleValues.add(((ShortValueInterface) permValue));
                } else if (permValue instanceof LongValueInterface) {
                    permissibleValues.add(((LongValueInterface) permValue));
                } else if (permValue instanceof DateValueInterface) {
                    permissibleValues.add(((DateValueInterface) permValue));
                } else if (permValue instanceof BooleanValueInterface) {
                    permissibleValues.add(((BooleanValueInterface) permValue));
                } else if (permValue instanceof IntegerValueInterface) {
                    permissibleValues.add(((IntegerValueInterface) permValue));
                } else if (permValue instanceof DoubleValueInterface) {
                    permissibleValues.add((DoubleValueInterface) permValue);
                } else if (permValue instanceof FloatValueInterface) {
                    permissibleValues.add(((FloatValueInterface) permValue));
                }
            }
        }
        return permissibleValues;
    }

    /**
     * Returns the map of name of the attribute and condition obj as its value.
     *
     * @param conditions
     *            list of conditions user had applied in case of edit limits
     * @return Map name of the attribute and condition
     */
    private Map<String, ICondition> getAttributeNameConditionMap(Collection<ICondition> conditions) {
        Map<String, ICondition> attributeNameConditionMap = new HashMap<String, ICondition>();
        if (conditions != null && !conditions.isEmpty()) {
            for (ICondition condition : conditions) {

                attributeNameConditionMap.put(condition.getAttribute().getName(), condition);
            }
        } 
        return attributeNameConditionMap;
    }

    /**
     * Returns list of possible numerated/enumerated operators for attribute.
     *
     * @param attribute
     *            attributeInterface
     * @return List listOf operators.
     */
    private List<String> getOperatorsList(AttributeInterface attribute) {
        List<String> operatorsList = new ArrayList<String>();

        String dataType = attribute.getDataType();
        UserDefinedDEInterface userDefineDE =
                (UserDefinedDEInterface) attribute.getAttributeTypeInformation().getDataElement();
        if (userDefineDE != null && userDefineDE.getPermissibleValueCollection().size() < 500) {
            if (dataType.equalsIgnoreCase("long") || dataType.equalsIgnoreCase("double")
                    || dataType.equalsIgnoreCase("short") || dataType.equalsIgnoreCase("integer")
                    || dataType.equalsIgnoreCase("float")) {
                operatorsList = parseFile.getEnumConditionList("number");
            } else if (dataType.equalsIgnoreCase("string")) {
                operatorsList = parseFile.getEnumConditionList("string");
            } else if (dataType.equalsIgnoreCase("boolean")) {
                operatorsList = parseFile.getEnumConditionList("boolean");
            } else if (dataType.equalsIgnoreCase("date")) {
                operatorsList = parseFile.getEnumConditionList("date");
            }
        } else {
            if (dataType.equalsIgnoreCase("long") || dataType.equalsIgnoreCase("double")
                    || dataType.equalsIgnoreCase("short") || dataType.equalsIgnoreCase("integer")
                    || dataType.equalsIgnoreCase("float")) {
                operatorsList = parseFile.getNonEnumConditionList("number");
            } else if (dataType.equalsIgnoreCase("string")) {
                operatorsList = parseFile.getNonEnumConditionList("string");
            } else if (dataType.equalsIgnoreCase("boolean")) {
                operatorsList = parseFile.getNonEnumConditionList("boolean");
            } else if (dataType.equalsIgnoreCase("date")) {
                operatorsList = parseFile.getNonEnumConditionList("date");
            } else if (dataType.equalsIgnoreCase(AddLimitConstants.FILE_TYPE)) {
                operatorsList = parseFile.getNonEnumConditionList(AddLimitConstants.FILE_TYPE);
            }
        }

        Collections.sort(operatorsList);
        return operatorsList;
    }

    /**
     * This method generates the combobox's html to show the operators valid for
     * the attribute passed to it.
     *
     * @param attribute
     *            AttributeInterface
     * @param operatorsList
     *            list of operators for each attribute
     * @return String HTMLForOperators
     */
    private String generateHTMLForOperators(AttributeInterface attribute, List<String> operatorsList, String op,
                                            String cssClass) {
        StringBuffer html = new StringBuffer();
        if (operatorsList.size() != 0) {
            html.append("\n<td width='15%' class=" + cssClass + " valign='top'>");

            String componentId = generateComponentName(attribute);
            AttributeTypeInformationInterface attrTypeInfo = attribute.getAttributeTypeInformation();
            boolean operaratorChanged = attrTypeInfo instanceof DateAttributeTypeInformation;
            html.append("\n<select class=" + cssClass + " style=\"width:150px; display:block;\" name=\""
                    + componentId + "_combobox\" onChange=\"operatorChanged('" + componentId + "','"
                    + operaratorChanged + "')\">");

            String selected = "";
            for (String operator : operatorsList) {
                if (operator.equalsIgnoreCase(op)) {
                    selected = "SELECTED";
                } else {
                    selected = "";
                }
                html.append("\n<option  class=" + cssClass + " value=\"" + operator + "\"" + selected + ">"
                        + operator + "</option>");

            }
            html.append("\n</select>\n</td>");
        }
        return html.toString();
    }

    /**
     * Generates html for textBox to hold the input for operator selected.
     *
     * @param attributeInterface
     *            attribute
     * @param isBetween
     *            boolean
     * @return String HTMLForTextBox
     */
    private String generateHTMLForTextBox(AttributeInterface attributeInterface, boolean isBetween,
                                          List<String> values, String op, String cssClass) {
        String componentId = generateComponentName(attributeInterface);
        String textBoxId = componentId + "_textBox";
        String textBoxId1 = componentId + "_textBox1";

        StringBuffer html = new StringBuffer("<td valign='top' width='15%' class='querylimittext'>\n");
        if (values == null || values.isEmpty()) {
            if (op != null
                    && (op.equalsIgnoreCase(AddLimitConstants.IS_NOT_NULL) || op
                        .equalsIgnoreCase(AddLimitConstants.IS_NULL))) {
                html.append("<input style=\"width:150px; display:block;\" type=\"text\" disabled='true' name=\""
                        + textBoxId + "\" id=\"" + textBoxId + "\">");
            } else {
                html.append("<input style=\"width:150px; display:block;\" type=\"text\" name=\"" + textBoxId
                        + "\" id=\"" + textBoxId + "\">");
            }
        } else {
            String valueStr = "";
            if (op.equalsIgnoreCase(AddLimitConstants.IN) || op.equalsIgnoreCase(AddLimitConstants.NOT_IN)) {
                valueStr = values.toString();
                valueStr = valueStr.replace("[", "");
                valueStr = valueStr.replace("]", "");
                if (values.get(0) == null) {
                    valueStr = "";
                }
                html.append("<input style=\"width:150px; display:block;\" type=\"text\" name=\"" + textBoxId
                        + "\" id=\"" + textBoxId + "\" value=\"" + valueStr + "\">");
            } else {
                if (values.get(0) == null) {
                    html.append("<input style=\"width:150px; display:block;\" type=\"text\" name=\"" + textBoxId
                            + "\" id=\"" + textBoxId + "\" value=\"" + "" + "\">");
                } else {
                    html.append("<input style=\"width:150px; display:block;\" type=\"text\" name=\"" + textBoxId
                            + "\" id=\"" + textBoxId + "\" value=\"" + values.get(0) + "\">");
                }
            }
        }
        html.append("\n</td>");

        String dataType = attributeInterface.getDataType();
        if (dataType.equalsIgnoreCase(AddLimitConstants.DATE)) {
            html.append("\n" + generateHTMLForCalendar(attributeInterface, true, false, cssClass));
        } else {
            html.append("\n<td valign='top' width='1%'>&nbsp;</td>");
        }
        html.append("<td valign='top' width='15%' class='querylimittext'>\n");
        if (isBetween) {
            if (values == null || values.isEmpty()) {
                html.append("<input type=\"text\" name=\"" + textBoxId1 + "\" id=\"" + textBoxId1
                        + "\" style=\"display:block\">");
            } else {
                if (values.get(1) == null) {
                    html.append("<input type=\"text\" name=\"" + textBoxId1 + "\" id=\"" + textBoxId1
                            + "\" value=\"" + "" + "\" style=\"display:block\">");
                } else {
                    html.append("<input type=\"text\" name=\"" + textBoxId1 + "\" id=\"" + textBoxId1
                            + "\" value=\"" + values.get(1) + "\" style=\"display:block\">");
                }
            }
        } else {
            html.append("<input type=\"text\" name=\"" + textBoxId1 + "\" id=\"" + textBoxId1
                    + "\" style=\"display:none\">");
        }
        html.append("\n</td>");
        if (dataType.equalsIgnoreCase(AddLimitConstants.DATE)) {
            html.append("\n" + generateHTMLForCalendar(attributeInterface, false, isBetween, cssClass));
        } else {
            html.append("\n<td valign='top' />");
        }
        return html.toString();
    }

    /**
     * Generators html for Calendar.Depending upon the value of operator the
     * calendar is displayed(hidden/visible).
     *
     * @param attributeName
     *            attributeName
     * @param isFirst
     *            boolean
     * @param isBetween
     *            boolean
     * @return String HTMLForCalendar
     */
    private String generateHTMLForCalendar(AttributeInterface attribute, boolean isFirst, boolean isBetween,
                                           String cssClass) {
        String componentId = generateComponentName(attribute);
        StringBuffer innerStr = new StringBuffer();

        if (isFirst) {
            String textBoxId = componentId + "_textBox";
            String imgStr =
                    "\n<img id=\"calendarImg\" src=\"images/calendar.gif\" width=\"24\" height=\"22\""
                            + " border=\"0\" onclick='scwShow(" + textBoxId + ",event);'>";

            String calendarId = componentId + "_calendar";
            innerStr =
                    innerStr.append(
                                    "\n<td width='3%' class='" + cssClass + "' valign='top' id=\"" + calendarId
                                            + "\">" + "\n" + imgStr).append("\n</td>");
        } else {
            String textBoxId1 = componentId + "_textBox1";
            String imgStr =
                    "\n<img id=\"calendarImg\" src=\"images/calendar.gif\" width=\"24\" height=\"22\" border='0'"
                            + " onclick='scwShow(" + textBoxId1 + ",event);'>";

            String style = "display:none";
            if (isBetween) {
                style = "display:block";
            }
            String calendarId1 = componentId + "_calendar1";
            innerStr =
                    innerStr.append(
                                    "\n<td width='3%' class='" + cssClass + "' valign='top' id=\"" + calendarId1
                                            + "\" style=\"" + style + "\">" + "\n" + imgStr).append("\n</td>");
        }
        return innerStr.toString();
    }

    /**
     * This function generates the HTML for enumerated values.
     *
     * @param attribute
     *            AttributeInterface
     * @param enumeratedValuesList
     *            enumeratedValuesList
     * @param list
     *            values values' list in case of edit limits
     * @return String html for enumerated value dropdown
     */
    private String generateHTMLForEnumeratedValues(AttributeInterface attribute,
                                                   List<PermissibleValueInterface> permissibleValues,
                                                   List<String> editLimitPermissibleValues, String cssClass) {
        StringBuffer html = new StringBuffer();
        String componentId = generateComponentName(attribute);
        if (permissibleValues != null && permissibleValues.size() != 0) {
            html.append("\n<td width='70%' valign='centre' colspan='4' >");
            html.append("\n<select style=\"display:block;\" MULTIPLE styleId='country' size ='5' name=\""
                    + componentId + "_enumeratedvaluescombobox\"\">");
            List<PermissibleValueInterface> values = new ArrayList<PermissibleValueInterface>(permissibleValues);
            Collections.sort(values, new PermissibleValueComparator());
            for (int i = 0; i < values.size(); i++) {
                PermissibleValueInterface perValue = (PermissibleValueInterface) values.get(i);
                String value = perValue.getValueAsObject().toString();
                if (editLimitPermissibleValues != null && editLimitPermissibleValues.contains(value)) {
                    html.append("\n<option class=\"permissiblevaluesquery\" title=\"" + value + "\" value=\""
                            + value + "\" SELECTED>" + value + "</option>");
                } else {
                    html.append("\n<option class=\"permissiblevaluesquery\" title=\"" + value + "\" value=\""
                            + value + "\">" + value + "</option>");
                }
            }
            html.append("\n</select>").append("\n</td>");
        }
        return html.toString();

    }

    /**
     *
     * @param attributeCollection
     * @return
     */
    private String getAttributesString(Collection<AttributeInterface> attributeCollection) {
        StringBuffer attributesList = new StringBuffer();
        for (AttributeInterface attribute : attributeCollection) {
            String componentId = generateComponentName(attribute);
            attributesList.append(componentId).append(';');
        }
        return attributesList.toString();
    }

    private String generateHTMLForRadioButton(AttributeInterface attribute, List<String> values, String cssClass) {
        StringBuffer html = new StringBuffer();
        String componentId = generateComponentName(attribute) + "_radioButton";
        String componentName = componentId + "_booleanAttribute";
        String radioButtonTrueId = componentId + "_true";
        String radioButtonFalseId = componentId + "_false";

        html.append("\n<td valign='top' class='" + cssClass + "' >");
        if (values == null || values.isEmpty()) {
            html.append("\n<input type='radio' id = '" + componentId
                    + "_true' value='true' onclick=\"resetOptionButton('" + radioButtonTrueId + "',this)\" name='"
                    + componentName + "'/><font class='" + cssClass + "'>True</font>");
            html.append("\n<input type='radio' id = '" + componentId
                    + "_false' value='false' onclick=\"resetOptionButton('" + radioButtonFalseId
                    + "',this)\" name='" + componentName + "'/><font class='" + cssClass + "'>False</font>");
        } else {
            if (values.get(0) != null) {
                if (values.get(0).equalsIgnoreCase("true")) {
                    html.append("\n<input type='radio' id = '" + componentId
                            + "_true' value='true' onclick=\"resetOptionButton('" + radioButtonTrueId
                            + "',this)\" name='" + componentName + "' checked><font class='" + cssClass
                            + "'>True</font>");
                    html
                        .append("\n<input type='radio' id = '" + componentId
                                + "_false' value='false' onclick=\"resetOptionButton('" + radioButtonFalseId
                                + "',this)\" name='" + componentName + "'><font class='" + cssClass
                                + "'>False</font>");
                } else if (values.get(0).equalsIgnoreCase("false")) {
                    html
                        .append("\n<input type='radio' id = '" + componentId
                                + "_true' value='true' onclick=\"resetOptionButton('" + radioButtonTrueId
                                + "',this)\" name='" + componentName + "' ><font class='" + cssClass
                                + "'>True</font>");
                    html.append("\n<input type='radio' id = '" + componentId
                            + "_false' value='false' onclick=\"resetOptionButton('" + radioButtonFalseId
                            + "',this)\" name='" + componentName + "'  checked><font class='" + cssClass
                            + "'>False</font>");
                } else {
                    html
                        .append("\n<input type='radio' id = '" + componentId
                                + "_true' value='true' onclick=\"resetOptionButton('" + radioButtonTrueId
                                + "',this)\" name='" + componentName + "' ><font class='" + cssClass
                                + "'>True</font>");
                    html
                        .append("\n<input type='radio' id = '" + componentId
                                + "_false' value='false' onclick=\"resetOptionButton('" + radioButtonFalseId
                                + "',this)\" name='" + componentName + "'><font class='" + cssClass
                                + "'>False</font>");
                }
            } else {
                html.append("\n<input type='radio' id = '" + componentId
                        + "_true' value='true' onclick=\"resetOptionButton('" + radioButtonTrueId
                        + "',this)\" name='" + componentName + "' ><font class='" + cssClass + "'>True</font>");
                html.append("\n<input type='radio' id = '" + componentId
                        + "_false' value='false' onclick=\"resetOptionButton('" + radioButtonFalseId
                        + "',this)\" name='" + componentName + "'><font class='" + cssClass + "'>False</font>");
            }
        }
        html.append("\n</td>");

        html.append("\n<td valign='top' class='" + cssClass + "'>&nbsp;\n</td>\n<td valign='top' class='"
                + cssClass + "'>&nbsp;\n</td>");
        html.append("\n<td valign='top' class='" + cssClass + "'>&nbsp;\n</td>");

        return html.toString();
    }

    /**
     * Method to generate HTML for condition NULL
     * @param generatedHTML
     * @param attribute
     * @param operatorsList
     * @param permissibleValues
     * @param isBetween
     */
    private StringBuffer generateHTMLForConditionNull(AttributeInterface attribute, List<String> operatorsList,
                                                      List<PermissibleValueInterface> permissibleValues,
                                                      boolean isBetween) {
        StringBuffer generatedHTML = new StringBuffer();
        generatedHTML.append('\n').append(
                                          generateHTMLForOperators(attribute, operatorsList, null,
                                                                   "permissiblevaluesquery"));
        if (!permissibleValues.isEmpty() && permissibleValues.size() < 500) {
            generatedHTML.append('\n').append(
                                              generateHTMLForEnumeratedValues(attribute, permissibleValues, null,
                                                                              "permissiblevaluesquery"));
        } else {
            if (attribute.getDataType().equalsIgnoreCase("boolean")) {
                generatedHTML.append('\n').append(generateHTMLForRadioButton(attribute, null, "querylimittext"));
            } else {
                generatedHTML.append('\n').append(
                                                  generateHTMLForTextBox(attribute, isBetween, null, null,
                                                                         "querylimittext"));
            }
        }
        return generatedHTML;
    }

    /**
     * This is a private class meant to wrap entity and the conditions for its corresponding attributes
     */
    private class EntityConditions {
        private EntityInterface entity;

        private Collection<ICondition> conditions;

        EntityConditions(EntityInterface entity, Collection<ICondition> conditions) {
            this.entity = entity;

            this.conditions = conditions;
        }

        /**
         * @return the entity
         */
        public EntityInterface getEntity() {
            return entity;
        }

        /**
         * @return the conditions
         */
        public Collection<ICondition> getConditions() {
            return conditions;
        }
    }
}
