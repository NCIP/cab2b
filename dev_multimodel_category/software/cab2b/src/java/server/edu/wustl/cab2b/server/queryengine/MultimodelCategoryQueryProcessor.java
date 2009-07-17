package edu.wustl.cab2b.server.queryengine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.multimodelcategory.MultiModelAttribute;
import edu.wustl.cab2b.common.multimodelcategory.MultiModelCategory;
import edu.wustl.cab2b.common.queryengine.Cab2bQueryObjectFactory;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.MultiModelCategoryQuery;
import edu.wustl.cab2b.common.queryengine.MultiModelCategoryQueryImpl;
import edu.wustl.cab2b.server.category.multimodelcategory.MultiModelCategoryOperations;
import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.metadata.category.CategorialAttribute;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionOperand;
import edu.wustl.common.querysuite.queryobject.IParameter;
import edu.wustl.common.querysuite.queryobject.IRule;
import edu.wustl.common.querysuite.utils.ConstraintsObjectBuilder;

public class MultimodelCategoryQueryProcessor {

    private Map<EntityGroupInterface, List<AttributeInterface>> segregatedAttributes =
            new HashMap<EntityGroupInterface, List<AttributeInterface>>();

    private Map<EntityGroupInterface, List<String>> segregatedOperators =
            new HashMap<EntityGroupInterface, List<String>>();

    private Map<EntityGroupInterface, List<List<String>>> segregatedValues =
            new HashMap<EntityGroupInterface, List<List<String>>>();

    private Map<EntityGroupInterface, List<AttributeInterface>> segregatedParameterAttributes =
            new HashMap<EntityGroupInterface, List<AttributeInterface>>();

    /**
     * This method processes the given MultiModelCategoryQuery to split it to form and populate the sub-queries
     * @param mmcQuery
     * @return
     */
    public MultiModelCategoryQuery process(ICab2bQuery query) {
        MultiModelCategoryQuery mmcQuery = new MultiModelCategoryQueryImpl(query);

        EntityInterface deEntity = mmcQuery.getOutputEntity();
        MultiModelCategory multiModelCategory = getMultiModelCategory(deEntity);

        // Segregate the metadata
        segregatQueryMetadata(mmcQuery, multiModelCategory);

        for (EntityGroupInterface entityGroup : segregatedAttributes.keySet()) {
            List<AttributeInterface> attributes = segregatedAttributes.get(entityGroup);
            List<String> operators = segregatedOperators.get(entityGroup);
            List<List<String>> values = segregatedValues.get(entityGroup);

            EntityInterface categoryEntity = attributes.get(0).getEntity();
            ConstraintsObjectBuilder queryBuilder =
                    new ConstraintsObjectBuilder(Cab2bQueryObjectFactory.createCab2bQuery());
            queryBuilder.addRule(attributes, operators, values, categoryEntity);

            StringBuffer subQueryName =
                    new StringBuffer(mmcQuery.getName()).append('_').append(entityGroup.getLongName());
            StringBuffer subQueryDesc = new StringBuffer("This is a sub-query of ").append(mmcQuery.getName());

            ICab2bQuery subQuery = (ICab2bQuery) queryBuilder.getQuery();
            subQuery.setOutputEntity(categoryEntity);
            subQuery.setName(subQueryName.toString());
            subQuery.setDescription(subQueryDesc.toString());
            subQuery.setCreatedDate(mmcQuery.getCreatedDate());
            subQuery.setType(mmcQuery.getType());
            subQuery.setCreatedBy(mmcQuery.getCreatedBy());
            subQuery.setOutputUrls(new ArrayList<String>());

            addParameters(subQuery, entityGroup);

            mmcQuery.addSubQuery(subQuery);
        }

        return mmcQuery;
    }

    private void addParameters(ICab2bQuery subQuery, EntityGroupInterface entityGroup) {
        List<AttributeInterface> parameterAttributes = segregatedParameterAttributes.get(entityGroup);
        Collection<ICondition> allQueryConditions = getAllConditions(subQuery);

        Collection<IParameter<?>> parameters = new HashSet<IParameter<?>>();
        for (AttributeInterface parameterAttribute : parameterAttributes) {
            for (ICondition condition : allQueryConditions) {
                if (parameterAttribute.equals(condition.getAttribute())) {
                    IParameter<ICondition> parameter = QueryObjectFactory.createParameter(condition, parameterAttribute.getName());
                    parameters.add(parameter);
                }

            }
        }
        subQuery.getParameters().addAll(parameters);
    }

    private MultiModelCategory getMultiModelCategory(EntityInterface deEntity) {
        return new MultiModelCategoryOperations().getMultiModelCategoryByEntityId(deEntity.getId());
    }

    private void segregatQueryMetadata(MultiModelCategoryQuery mmcQuery, MultiModelCategory multiModelCategory) {
        // Create Attribute -> CategorialAttributes map
        Map<AttributeInterface, Collection<CategorialAttribute>> categorialAttributeMMQAttributesMap =
                new HashMap<AttributeInterface, Collection<CategorialAttribute>>();
        for (MultiModelAttribute multiModelAttribute : multiModelCategory.getMultiModelAttributes()) {
            categorialAttributeMMQAttributesMap.put(multiModelAttribute.getAttribute(), multiModelAttribute
                .getCategorialAttributes());
        }

        Collection<EntityGroupInterface> entityGroups = new HashSet<EntityGroupInterface>();
        Collection<ICondition> mmqConditions = getAllConditions(mmcQuery);
        for (ICondition mmqCondition : mmqConditions) {
            List<String> values = mmqCondition.getValues();
            String operator = mmqCondition.getRelationalOperator().getStringRepresentation();
            AttributeInterface mmqAttribute = mmqCondition.getAttribute();

            Collection<CategorialAttribute> categorialAttributes =
                    categorialAttributeMMQAttributesMap.get(mmqAttribute);
            for (CategorialAttribute categorialAttribute : categorialAttributes) {
                AttributeInterface sourceAttribute = categorialAttribute.getSourceClassAttribute();
                EntityGroupInterface entityGroup =
                        sourceAttribute.getEntity().getEntityGroupCollection().iterator().next();
                entityGroups.add(entityGroup);

                // Create attribute lists
                List<AttributeInterface> attributeList = segregatedAttributes.get(entityGroup);
                if (attributeList == null) {
                    attributeList = new ArrayList<AttributeInterface>();
                    segregatedAttributes.put(entityGroup, attributeList);
                }
                AttributeInterface categoryAttribute = categorialAttribute.getCategoryAttribute();
                attributeList.add(categoryAttribute);

                // Create operator lists
                List<String> operatorList = segregatedOperators.get(entityGroup);
                if (operatorList == null) {
                    operatorList = new ArrayList<String>();
                    segregatedOperators.put(entityGroup, operatorList);
                }
                operatorList.add(operator);

                // Create values lists
                List<List<String>> valueList = segregatedValues.get(entityGroup);
                if (valueList == null) {
                    valueList = new ArrayList<List<String>>();
                    segregatedValues.put(entityGroup, valueList);
                }
                valueList.add(values);

                //Segregate parameters
                segregateParameters(mmcQuery.getParameters(), mmqAttribute, categoryAttribute, entityGroup);
            }
        }
    }

    private void segregateParameters(List<IParameter<?>> parameters, AttributeInterface mmqAttribute,
                                     AttributeInterface categoryAttribute, EntityGroupInterface entityGroup) {
        for (IParameter<?> parameter : parameters) {
            List<AttributeInterface> parameterAttributes = segregatedParameterAttributes.get(entityGroup);
            if (parameterAttributes == null) {
                parameterAttributes = new ArrayList<AttributeInterface>();
                segregatedParameterAttributes.put(entityGroup, parameterAttributes);
            }

            Object object = parameter.getParameterizedObject();
            if (object instanceof ICondition) {
                AttributeInterface attribute = ((ICondition) object).getAttribute();
                if (mmqAttribute.equals(attribute)) {
                    parameterAttributes.add(categoryAttribute);
                }
            }
        }
    }

    private Collection<ICondition> getAllConditions(ICab2bQuery mmcQuery) {
        Collection<ICondition> conditions = new ArrayList<ICondition>();

        IExpression expression = mmcQuery.getConstraints().getExpression(1);
        IExpressionOperand operand = expression.getOperand(0);
        if (operand instanceof IRule) {
            IRule rule = (IRule) operand;
            for (ICondition condition : rule) {
                conditions.add(condition);
            }
        }

        return conditions;
    }

}
