/**
 * 
 */

package edu.wustl.common.querysuite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import edu.common.dynamicextensions.domain.Attribute;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domain.databaseproperties.TableProperties;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.databaseproperties.ColumnPropertiesInterface;
import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IQueryEntity;
import edu.wustl.common.querysuite.queryobject.IRule;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;
import edu.wustl.common.querysuite.queryobject.impl.Expression;

/**
 * @author prafull_kadam
 * To create Queries on Dummy entity data.
 * It does not use Entity Manager, Test queries on dummy Entity. 
 * Specifically designed to create entities & queries objects of each data type with possible operators. 

 */
public class GenericQueryGeneratorMock
{

	private static DomainObjectFactory factory = DomainObjectFactory.getInstance();

	/**
	 * To create one dummy entity.
	 * @param name Name of the Entity.
	 * @return The entity.
	 */
	public static EntityInterface createEntity(String name)
	{
		EntityInterface e = factory.createEntity();
		e.setName(name);
		e.setCreatedDate(new Date());
		e.setDescription("This is a Dummy entity");
		e.setId(1L);
		e.setLastUpdated(new Date());

		((Entity) e).setAbstractAttributeCollection(getAttributes(e));

		TableProperties tableProperties = new TableProperties();
		tableProperties.setName("catissue_temp");
		tableProperties.setId(1L);
		((Entity) e).setTableProperties(tableProperties);
		return e;
	}

	/**
	 * TO create attribute list, which contains all types of attributes.
	 * @param entity the entity to which all attributes belongs.
	 * @return list of attributes.
	 */
	public static ArrayList<AbstractAttributeInterface> getAttributes(EntityInterface entity)
	{
		ArrayList<AbstractAttributeInterface> attributes = new ArrayList<AbstractAttributeInterface>();

		AttributeInterface att1 = factory.createIntegerAttribute();
		att1.setName("long");
		ColumnPropertiesInterface c1 = factory.createColumnProperties();
		c1.setName("LONG_ATTRIBUTE");
		((Attribute) att1).setColumnProperties(c1);
		att1.setIsPrimaryKey(true);
		att1.setEntity(entity);
		
		AttributeInterface att2 = factory.createDateAttribute();
		att2.setName("date");
		ColumnPropertiesInterface c2 = factory.createColumnProperties();
		c2.setName("DATE_ATTRIBUTE");
		((Attribute) att2).setColumnProperties(c2);
		att2.setEntity(entity);

		AttributeInterface att3 = factory.createLongAttribute();
		att3.setName("int");
		ColumnPropertiesInterface c3 = factory.createColumnProperties();
		c3.setName("INT_ATTRIBUTE");
		((Attribute) att3).setColumnProperties(c3);
		att3.setEntity(entity);

		AttributeInterface att4 = factory.createStringAttribute();
		att4.setName("string");
		ColumnPropertiesInterface c4 = factory.createColumnProperties();
		c4.setName("STRING_ATTRIBUTE");
		((Attribute) att4).setColumnProperties(c4);
		att4.setEntity(entity);

		AttributeInterface att5 = factory.createBooleanAttribute();
		att5.setName("boolean");
		ColumnPropertiesInterface c5 = factory.createColumnProperties();
		c5.setName("BOOLEAN_ATTRIBUTE");
		((Attribute) att5).setColumnProperties(c5);
		att5.setEntity(entity);

		AttributeInterface att6 = factory.createDoubleAttribute();
		att6.setName("double");
		ColumnPropertiesInterface c6 = factory.createColumnProperties();
		c6.setName("DOUBLE_ATTRIBUTE");
		((Attribute) att6).setColumnProperties(c6);
		att6.setEntity(entity);

		AttributeInterface att7 = factory.createFloatAttribute();
		att7.setName("float");
		att7.setEntity(entity);

		ColumnPropertiesInterface c7 = factory.createColumnProperties();
		c7.setName("FLOAT_ATTRIBUTE");
		((Attribute) att7).setColumnProperties(c7);
		(att7).setIsPrimaryKey(new Boolean(true));

		attributes.add(0, att1);
		attributes.add(1, att2);
		attributes.add(2, att3);
		attributes.add(3, att4);
		attributes.add(4, att5);
		attributes.add(5, att6);
		attributes.add(6, att7);

		return attributes;
	}

	/**
	 * To create expression for Dummy entity. with rule as [name in (1,2,3,4)]
	 * @return the Expression.
	 */
	public static IExpression createExpression(EntityInterface entity)
	{
		IQueryEntity queryEntity = QueryObjectFactory.createQueryEntity(entity);

		IExpression expression = new Expression(queryEntity, 1);
		expression.addOperand(createRule(queryEntity.getDynamicExtensionsEntity(), "int"));
		return expression;
	}

	/**
	 * Create Rule for given Participant as : name in (1,2,3,4)
	 * @param entity The Dynamic Extension Entity Participant
	 * @return The Rule Object.
	 */
	public static IRule createRule(EntityInterface entity, String name)
	{
		List<ICondition> conditions = new ArrayList<ICondition>();
		conditions.add(createInCondition(entity, name));
		IRule rule = QueryObjectFactory.createRule(conditions);
		return rule;
	}

	/**
	 * Cretate Condition for given entity & attributeName : name in (1,2,3,4)
	 * @param entity The Dynamic Extension Entity
	 * @return The Condition object.
	 */
	public static ICondition createInCondition(EntityInterface entity, String name)
	{
		List<String> values = new ArrayList<String>();
		values.add("1");
		values.add("2");
		values.add("3");
		values.add("4");
		AttributeInterface attribute = findAttribute(entity, name);
		ICondition condition = QueryObjectFactory.createCondition(attribute, RelationalOperator.In,
				values);
		return condition;
	}

	/**
	 * To search attribute in the Entity.
	 * @param entity The Dynamic Extension Entity Participant.
	 * @param attributeName The name of the attribute to search. 
	 * @return The corresponding attribute.
	 */
	public static AttributeInterface findAttribute(EntityInterface entity, String attributeName)
	{
		Collection<AbstractAttributeInterface> attributes = entity.getAbstractAttributeCollection();
		for (AbstractAttributeInterface attribute: attributes)
		{
			if (attribute.getName().equals(attributeName))
				return (AttributeInterface)attribute;
		}
		return null;
	}
}
