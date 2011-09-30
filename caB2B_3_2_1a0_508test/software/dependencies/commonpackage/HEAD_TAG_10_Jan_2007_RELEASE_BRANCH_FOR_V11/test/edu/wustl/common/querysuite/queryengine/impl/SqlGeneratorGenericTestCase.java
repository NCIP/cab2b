/**
 * 
 */

package edu.wustl.common.querysuite.queryengine.impl;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.common.querysuite.GenericQueryGeneratorMock;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.global.Variables;
import edu.wustl.common.util.logger.Logger;

/**
 * @author prafull_kadam
 * To test SQL generator class with positive & negative testcases. 
 * It does not use Entity Manager, Test queries on dummy Entity. 
 * Specificaly test SQL representation of each data type with correspconding operators. 
 */
public class SqlGeneratorGenericTestCase extends TestCase
{

	SqlGenerator generator;

	static
	{
		Logger.configure();// To avoid null pointer Exception for code calling logger statements.
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception
	{
		generator = new SqlGenerator();
		super.setUp();
		setDataBaseType(Constants.MYSQL_DATABASE);
	}

	/**
	 * To set the Database Type as MySQL or Oracle.
	 * @param databaseType Constants.ORACLE_DATABASE or Constants.MYSQL_DATABASE.
	 */
	private void setDataBaseType(String databaseType)
	{
		Variables.databaseName = databaseType;

		if (Variables.databaseName.equals(Constants.ORACLE_DATABASE))
		{
			//set string/function for oracle
			Variables.datePattern = "mm-dd-yyyy";
			Variables.timePattern = "hh-mi-ss";
			Variables.dateFormatFunction = "TO_CHAR";
			Variables.timeFormatFunction = "TO_CHAR";
			Variables.dateTostrFunction = "TO_CHAR";
			Variables.strTodateFunction = "TO_DATE";
		}
		else
		{
			Variables.datePattern = "%m-%d-%Y";
			Variables.timePattern = "%H:%i:%s";
			Variables.dateFormatFunction = "DATE_FORMAT";
			Variables.timeFormatFunction = "TIME_FORMAT";
			Variables.dateTostrFunction = "TO_CHAR";
			Variables.strTodateFunction = "STR_TO_DATE";
		}
	}

	/**
	 * To test alll Integer Operator Conditions.
	 *
	 */
	public void testIntegerConditions()
	{
		EntityInterface entity = GenericQueryGeneratorMock.createEntity("DummyEntity");
		IExpression expression = GenericQueryGeneratorMock.createExpression(entity);
		ICondition condition = GenericQueryGeneratorMock.createInCondition(entity, "int");

		try
		{
			RelationalOperator operator = RelationalOperator.In;
			assertEquals("Incorrect SQL generated for Integer Attribute for Operator:" + operator,
					"DummyEntity_0.INT_ATTRIBUTE in (1,2,3,4)", generator.getSQL(condition,
							expression));

			operator = RelationalOperator.NotIn;
			condition.setRelationalOperator(operator);
			assertEquals("Incorrect SQL generated for Integer Attribute for Operator:" + operator,
					"DummyEntity_0.INT_ATTRIBUTE NOT in (1,2,3,4)", generator.getSQL(condition,
							expression));

			List<String> values = new ArrayList<String>();
			values.add("1");
			condition.setValues(values);

			operator = RelationalOperator.LessThan;
			condition.setRelationalOperator(operator);
			assertEquals("Incorrect SQL generated for Integer Attribute for Operator:" + operator,
					"DummyEntity_0.INT_ATTRIBUTE<1", generator.getSQL(condition, expression));

			operator = RelationalOperator.LessThanOrEquals;
			condition.setRelationalOperator(operator);
			assertEquals("Incorrect SQL generated for Integer Attribute for Operator:" + operator,
					"DummyEntity_0.INT_ATTRIBUTE<=1", generator.getSQL(condition, expression));

			operator = RelationalOperator.Equals;
			condition.setRelationalOperator(operator);
			assertEquals("Incorrect SQL generated for Integer Attribute for Operator:" + operator,
					"DummyEntity_0.INT_ATTRIBUTE=1", generator.getSQL(condition, expression));

			operator = RelationalOperator.NotEquals;
			condition.setRelationalOperator(operator);
			assertEquals("Incorrect SQL generated for Integer Attribute for Operator:" + operator,
					"DummyEntity_0.INT_ATTRIBUTE!=1", generator.getSQL(condition, expression));

			operator = RelationalOperator.GreaterThan;
			condition.setRelationalOperator(operator);
			assertEquals("Incorrect SQL generated for Integer Attribute for Operator:" + operator,
					"DummyEntity_0.INT_ATTRIBUTE>1", generator.getSQL(condition, expression));

			operator = RelationalOperator.GreaterThanOrEquals;
			condition.setRelationalOperator(operator);
			assertEquals("Incorrect SQL generated for Integer Attribute for Operator:" + operator,
					"DummyEntity_0.INT_ATTRIBUTE>=1", generator.getSQL(condition, expression));

			values.remove(0);
			operator = RelationalOperator.IsNull;
			condition.setRelationalOperator(operator);
			assertEquals("Incorrect SQL generated for Integer Attribute for Operator:" + operator,
					"DummyEntity_0.INT_ATTRIBUTE is NULL", generator.getSQL(condition, expression));

			operator = RelationalOperator.IsNotNull;
			condition.setRelationalOperator(operator);
			assertEquals("Incorrect SQL generated for Integer Attribute for Operator:" + operator,
					"DummyEntity_0.INT_ATTRIBUTE is NOT NULL", generator.getSQL(condition,
							expression));

			values = new ArrayList<String>();
			values.add("1");
			values.add("100");
			condition.setValues(values);
			
			operator = RelationalOperator.Between;
			condition.setRelationalOperator(operator);
			assertEquals("Incorrect SQL generated for Integer Attribute for Operator:" + operator,
					"(DummyEntity_0.INT_ATTRIBUTE>=1 And DummyEntity_0.INT_ATTRIBUTE<=100)", generator.getSQL(condition,
							expression));

		}
		catch (Exception e)
		{
			fail("Unexpected Expection while testing Integer Conditions!!!");
		}
	}

	/**
	 * To test alll Boolean Operator Conditions.
	 *
	 */
	public void testBooleanConditions()
	{
		EntityInterface entity = GenericQueryGeneratorMock.createEntity("DummyEntity");
		IExpression expression = GenericQueryGeneratorMock.createExpression(entity);
		ICondition condition = GenericQueryGeneratorMock.createInCondition(entity, "boolean");

		try
		{
			List<String> values = new ArrayList<String>();
			values.add(Constants.TRUE);
			condition.setValues(values);
			
			RelationalOperator operator = RelationalOperator.Equals;
			condition.setRelationalOperator(operator);
			assertEquals("Incorrect SQL generated for Boolean Attribute for Operator:" + operator,
					"DummyEntity_0.BOOLEAN_ATTRIBUTE=1", generator.getSQL(condition,
							expression));
			
			operator = RelationalOperator.NotEquals;
			condition.setRelationalOperator(operator);
			assertEquals("Incorrect SQL generated for Boolean Attribute for Operator:" + operator,
					"DummyEntity_0.BOOLEAN_ATTRIBUTE!=1", generator.getSQL(condition,
							expression));
			
			values.set(0,Constants.FALSE);
			assertEquals("Incorrect SQL generated for Boolean Attribute for Operator:" + operator,
					"DummyEntity_0.BOOLEAN_ATTRIBUTE!=0", generator.getSQL(condition,
							expression));
			
		}
		catch (Exception e)
		{
			fail("Unexpected Expection while testing Boolean Conditions!!!");
		}
	}
			
	/**
	 * To test alll Date Operator Conditions on Mysql database.
	 *
	 */
	public void testDateConditionsOnMysql()
	{
		EntityInterface entity = GenericQueryGeneratorMock.createEntity("DummyEntity");
		IExpression expression = GenericQueryGeneratorMock.createExpression(entity);
		ICondition condition = GenericQueryGeneratorMock.createInCondition(entity, "date");

		try
		{
			List<String> values = new ArrayList<String>();
			values.add("01-01-2000");
			values.add("01-10-2000");
			values.add("05-10-2000");
			condition.setValues(values);

			RelationalOperator operator = RelationalOperator.In;
			assertEquals(
					"Incorrect SQL generated for Date Attribute on MySQL database for Operator:"
							+ operator,
					"DummyEntity_0.DATE_ATTRIBUTE in (STR_TO_DATE('1-1-2000','%m-%d-%Y'),STR_TO_DATE('1-10-2000','%m-%d-%Y'),STR_TO_DATE('5-10-2000','%m-%d-%Y'))",
					generator.getSQL(condition, expression));

			operator = RelationalOperator.NotIn;
			condition.setRelationalOperator(operator);
			assertEquals(
					"Incorrect SQL generated for Date Attribute on MySQL database for Operator:"
							+ operator,
					"DummyEntity_0.DATE_ATTRIBUTE NOT in (STR_TO_DATE('1-1-2000','%m-%d-%Y'),STR_TO_DATE('1-10-2000','%m-%d-%Y'),STR_TO_DATE('5-10-2000','%m-%d-%Y'))",
					generator.getSQL(condition, expression));

			values = new ArrayList<String>();
			values.add("01-01-2000");
			condition.setValues(values);

			operator = RelationalOperator.LessThan;
			condition.setRelationalOperator(operator);
			assertEquals(
					"Incorrect SQL generated for Date Attribute on MySQL database for Operator:"
							+ operator,
					"DummyEntity_0.DATE_ATTRIBUTE<STR_TO_DATE('1-1-2000','%m-%d-%Y')", generator
							.getSQL(condition, expression));

			operator = RelationalOperator.LessThanOrEquals;
			condition.setRelationalOperator(operator);
			assertEquals(
					"Incorrect SQL generated for Date Attribute on MySQL database for Operator:"
							+ operator,
					"DummyEntity_0.DATE_ATTRIBUTE<=STR_TO_DATE('1-1-2000','%m-%d-%Y')", generator
							.getSQL(condition, expression));

			operator = RelationalOperator.Equals;
			condition.setRelationalOperator(operator);
			assertEquals(
					"Incorrect SQL generated for Date Attribute on MySQL database for Operator:"
							+ operator,
					"DummyEntity_0.DATE_ATTRIBUTE=STR_TO_DATE('1-1-2000','%m-%d-%Y')", generator
							.getSQL(condition, expression));

			operator = RelationalOperator.NotEquals;
			condition.setRelationalOperator(operator);
			assertEquals(
					"Incorrect SQL generated for Date Attribute on MySQL database for Operator:"
							+ operator,
					"DummyEntity_0.DATE_ATTRIBUTE!=STR_TO_DATE('1-1-2000','%m-%d-%Y')", generator
							.getSQL(condition, expression));

			operator = RelationalOperator.GreaterThan;
			condition.setRelationalOperator(operator);
			assertEquals(
					"Incorrect SQL generated for Date Attribute on MySQL database for Operator:"
							+ operator,
					"DummyEntity_0.DATE_ATTRIBUTE>STR_TO_DATE('1-1-2000','%m-%d-%Y')", generator
							.getSQL(condition, expression));

			operator = RelationalOperator.GreaterThanOrEquals;
			condition.setRelationalOperator(operator);
			assertEquals(
					"Incorrect SQL generated for Date Attribute on MySQL database for Operator:"
							+ operator,
					"DummyEntity_0.DATE_ATTRIBUTE>=STR_TO_DATE('1-1-2000','%m-%d-%Y')", generator
							.getSQL(condition, expression));

			values.remove(0);
			operator = RelationalOperator.IsNull;
			condition.setRelationalOperator(operator);
			assertEquals(
					"Incorrect SQL generated for Date Attribute on MySQL database for Operator:"
							+ operator, "DummyEntity_0.DATE_ATTRIBUTE is NULL", generator.getSQL(
							condition, expression));

			operator = RelationalOperator.IsNotNull;
			condition.setRelationalOperator(operator);
			assertEquals(
					"Incorrect SQL generated for Date Attribute on MySQL database for Operator:"
							+ operator, "DummyEntity_0.DATE_ATTRIBUTE is NOT NULL", generator
							.getSQL(condition, expression));

			values = new ArrayList<String>();
			values.add("01-01-2000");
			values.add("02-01-2000");
			condition.setValues(values);

			operator = RelationalOperator.Between;
			condition.setRelationalOperator(operator);
			assertEquals(
					"Incorrect SQL generated for Date Attribute on MySQL database for Operator:"
							+ operator,
					"(DummyEntity_0.DATE_ATTRIBUTE>=STR_TO_DATE('1-1-2000','%m-%d-%Y') And DummyEntity_0.DATE_ATTRIBUTE<=STR_TO_DATE('2-1-2000','%m-%d-%Y'))", generator
							.getSQL(condition, expression));
		}
		catch (Exception e)
		{
			fail("Unexpected Expection while testing Date Conditions on MySQL database!!!");
		}
	}

	/**
	 * To test alll Date Operator Conditions on Oracle database.
	 *
	 */
	public void testDateConditionsOnOracle()
	{
		setDataBaseType(Constants.ORACLE_DATABASE);

		EntityInterface entity = GenericQueryGeneratorMock.createEntity("DummyEntity");
		IExpression expression = GenericQueryGeneratorMock.createExpression(entity);
		ICondition condition = GenericQueryGeneratorMock.createInCondition(entity, "date");
		try
		{
			List<String> values = new ArrayList<String>();
			values.add("01-01-2000");
			values.add("01-10-2000");
			values.add("05-10-2000");
			condition.setValues(values);

			RelationalOperator operator = RelationalOperator.In;
			assertEquals(
					"Incorrect SQL generated for Date Attribute on Oracle database for Operator:"
							+ operator,
					"DummyEntity_0.DATE_ATTRIBUTE in (TO_DATE('1-1-2000','mm-dd-yyyy'),TO_DATE('1-10-2000','mm-dd-yyyy'),TO_DATE('5-10-2000','mm-dd-yyyy'))",
					generator.getSQL(condition, expression));

			operator = RelationalOperator.NotIn;
			condition.setRelationalOperator(operator);
			assertEquals(
					"Incorrect SQL generated for Date Attribute on Oracle database for Operator:"
							+ operator,
					"DummyEntity_0.DATE_ATTRIBUTE NOT in (TO_DATE('1-1-2000','mm-dd-yyyy'),TO_DATE('1-10-2000','mm-dd-yyyy'),TO_DATE('5-10-2000','mm-dd-yyyy'))",
					generator.getSQL(condition, expression));

			values = new ArrayList<String>();
			values.add("01-01-2000");
			condition.setValues(values);

			operator = RelationalOperator.LessThan;
			condition.setRelationalOperator(operator);
			assertEquals(
					"Incorrect SQL generated for Date Attribute on Oracle database for Operator:"
							+ operator,
					"DummyEntity_0.DATE_ATTRIBUTE<TO_DATE('1-1-2000','mm-dd-yyyy')", generator
							.getSQL(condition, expression));

			operator = RelationalOperator.LessThanOrEquals;
			condition.setRelationalOperator(operator);
			assertEquals(
					"Incorrect SQL generated for Date Attribute on Oracle database for Operator:"
							+ operator,
					"DummyEntity_0.DATE_ATTRIBUTE<=TO_DATE('1-1-2000','mm-dd-yyyy')", generator
							.getSQL(condition, expression));

			operator = RelationalOperator.Equals;
			condition.setRelationalOperator(operator);
			assertEquals(
					"Incorrect SQL generated for Date Attribute on Oracle database for Operator:"
							+ operator,
					"DummyEntity_0.DATE_ATTRIBUTE=TO_DATE('1-1-2000','mm-dd-yyyy')", generator
							.getSQL(condition, expression));

			operator = RelationalOperator.NotEquals;
			condition.setRelationalOperator(operator);
			assertEquals(
					"Incorrect SQL generated for Date Attribute on Oracle database for Operator:"
							+ operator,
					"DummyEntity_0.DATE_ATTRIBUTE!=TO_DATE('1-1-2000','mm-dd-yyyy')", generator
							.getSQL(condition, expression));

			operator = RelationalOperator.GreaterThan;
			condition.setRelationalOperator(operator);
			assertEquals(
					"Incorrect SQL generated for Date Attribute on Oracle database for Operator:"
							+ operator,
					"DummyEntity_0.DATE_ATTRIBUTE>TO_DATE('1-1-2000','mm-dd-yyyy')", generator
							.getSQL(condition, expression));

			operator = RelationalOperator.GreaterThanOrEquals;
			condition.setRelationalOperator(operator);
			assertEquals(
					"Incorrect SQL generated for Date Attribute on Oracle database for Operator:"
							+ operator,
					"DummyEntity_0.DATE_ATTRIBUTE>=TO_DATE('1-1-2000','mm-dd-yyyy')", generator
							.getSQL(condition, expression));

			values.remove(0);
			operator = RelationalOperator.IsNull;
			condition.setRelationalOperator(operator);
			assertEquals(
					"Incorrect SQL generated for Date Attribute on Oracle database for Operator:"
							+ operator, "DummyEntity_0.DATE_ATTRIBUTE is NULL", generator.getSQL(
							condition, expression));

			operator = RelationalOperator.IsNotNull;
			condition.setRelationalOperator(operator);
			assertEquals(
					"Incorrect SQL generated for Date Attribute on Oracle database for Operator:"
							+ operator, "DummyEntity_0.DATE_ATTRIBUTE is NOT NULL", generator
							.getSQL(condition, expression));

			values = new ArrayList<String>();
			values.add("01-01-2000");
			values.add("02-01-2000");
			condition.setValues(values);

			operator = RelationalOperator.Between;
			condition.setRelationalOperator(operator);
			assertEquals(
					"Incorrect SQL generated for Date Attribute on MySQL database for Operator:"
							+ operator,
					"(DummyEntity_0.DATE_ATTRIBUTE>=TO_DATE('1-1-2000','mm-dd-yyyy') And DummyEntity_0.DATE_ATTRIBUTE<=TO_DATE('2-1-2000','mm-dd-yyyy'))", generator
							.getSQL(condition, expression));
		}
		catch (Exception e)
		{
			fail("Unexpected Expection while testing Date Conditions on Oracle database!!!");
		}
	}

	/**
	 * To test alll String Operator Conditions.
	 *
	 */
	public void testStringConditions()
	{
		EntityInterface entity = GenericQueryGeneratorMock.createEntity("DummyEntity");
		IExpression expression = GenericQueryGeneratorMock.createExpression(entity);
		ICondition condition = GenericQueryGeneratorMock.createInCondition(entity, "string");

		try
		{
			RelationalOperator operator = RelationalOperator.In;
			assertEquals("Incorrect SQL generated for String Attribute for Operator:" + operator,
					"DummyEntity_0.STRING_ATTRIBUTE in ('1','2','3','4')", generator.getSQL(
							condition, expression));

			operator = RelationalOperator.NotIn;
			condition.setRelationalOperator(operator);
			assertEquals("Incorrect SQL generated for String Attribute for Operator:" + operator,
					"DummyEntity_0.STRING_ATTRIBUTE NOT in ('1','2','3','4')", generator.getSQL(
							condition, expression));

			List<String> values = new ArrayList<String>();
			values.add("1");
			condition.setValues(values);

			operator = RelationalOperator.Equals;
			condition.setRelationalOperator(operator);
			assertEquals("Incorrect SQL generated for String Attribute for Operator:" + operator,
					"DummyEntity_0.STRING_ATTRIBUTE='1'", generator.getSQL(condition, expression));

			operator = RelationalOperator.NotEquals;
			condition.setRelationalOperator(operator);
			assertEquals("Incorrect SQL generated for String Attribute for Operator:" + operator,
					"DummyEntity_0.STRING_ATTRIBUTE!='1'", generator.getSQL(condition, expression));

			operator = RelationalOperator.Contains;
			condition.setRelationalOperator(operator);
			assertEquals("Incorrect SQL generated for String Attribute for Operator:" + operator,
					"DummyEntity_0.STRING_ATTRIBUTE like '%1%'", generator.getSQL(condition,
							expression));

			operator = RelationalOperator.StartsWith;
			condition.setRelationalOperator(operator);
			assertEquals("Incorrect SQL generated for String Attribute for Operator:" + operator,
					"DummyEntity_0.STRING_ATTRIBUTE like '1%'", generator.getSQL(condition,
							expression));

			operator = RelationalOperator.EndsWith;
			condition.setRelationalOperator(operator);
			assertEquals("Incorrect SQL generated for String Attribute for Operator:" + operator,
					"DummyEntity_0.STRING_ATTRIBUTE like '%1'", generator.getSQL(condition,
							expression));

			values.remove(0);
			operator = RelationalOperator.IsNull;
			condition.setRelationalOperator(operator);
			assertEquals("Incorrect SQL generated for String Attribute for Operator:" + operator,
					"DummyEntity_0.STRING_ATTRIBUTE is NULL", generator.getSQL(condition,
							expression));

			operator = RelationalOperator.IsNotNull;
			condition.setRelationalOperator(operator);
			assertEquals("Incorrect SQL generated for String Attribute for Operator:" + operator,
					"DummyEntity_0.STRING_ATTRIBUTE is NOT NULL", generator.getSQL(condition,
							expression));

		}
		catch (Exception e)
		{
			fail("Unexpected Expection while testing String Conditions!!!");
		}
	}
	
	/**
	 * To test the Positive & Negative Test cases for Between Operator.
	 *
	 */
	public void testBetweenOperator()
	{
		EntityInterface entity = GenericQueryGeneratorMock.createEntity("DummyEntity");
		IExpression expression = GenericQueryGeneratorMock.createExpression(entity);
		ICondition condition = GenericQueryGeneratorMock.createInCondition(entity, "string");

		// Negative Testcase, Using Between operator on String.
		try
		{
			List<String> values = new ArrayList<String>();
			values.add("str1");
			values.add("str1");
			condition.setValues(values);
			RelationalOperator operator = RelationalOperator.Between;
			condition.setRelationalOperator(operator);
			generator.getSQL(condition, expression);
			fail("Expected SqlException!!!, String operand can not have Between Operator in condition.");
		}
		catch (Exception e)
		{
		}
		
		// Negative Testcase, Using Between operator on Boolean.
		condition = GenericQueryGeneratorMock.createInCondition(entity, "boolean");
		try
		{
			List<String> values = new ArrayList<String>();
			values.add("true");
			values.add("false");
			condition.setValues(values);
			RelationalOperator operator = RelationalOperator.Between;
			condition.setRelationalOperator(operator);
			generator.getSQL(condition, expression);
			fail("Expected SqlException!!!, Boolean operand can not have Between Operator in condition.");
		}
		catch (Exception e)
		{
		}
		
		// Negative Testcase, Using Between operator with one value & more than 2 values.
		condition = GenericQueryGeneratorMock.createInCondition(entity, "int");
		try
		{
			List<String> values = new ArrayList<String>();
			values.add("1");
			condition.setValues(values);
			RelationalOperator operator = RelationalOperator.Between;
			condition.setRelationalOperator(operator);
			generator.getSQL(condition, expression);
			fail("Expected SqlException!!!, Two values required for Between Operator in condition.");
			
			values.add("2");
			values.add("3");
			generator.getSQL(condition, expression);
			fail("Expected SqlException!!!, Two values required for Between Operator in condition.");
		}
		catch (Exception e)
		{
		}
	}
}
