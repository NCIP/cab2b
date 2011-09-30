package edu.wustl.common.querysuite;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import org.hibernate.HibernateException;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.common.querysuite.exceptions.CyclicException;
import edu.wustl.common.querysuite.exceptions.MultipleRootsException;
import edu.wustl.common.querysuite.exceptions.SqlException;
import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.factory.SqlGeneratorFactory;
import edu.wustl.common.querysuite.metadata.associations.IIntraModelAssociation;
import edu.wustl.common.querysuite.queryengine.ISqlGenerator;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IConstraints;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.querysuite.queryobject.IQueryEntity;
import edu.wustl.common.querysuite.queryobject.IRule;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;
import edu.wustl.common.util.dbManager.DBUtil;
import edu.wustl.common.util.logger.Logger;

/**
 * @author prafull_kadam
 *  * To generate the Metadata test Report:
 *    - report of Entities whose all attribute have proper attribute metadata.
 *    - report of Entities whose some attribute have not proper attribute metadata.
 *    - report of Entities & their associations along with success/failure status. 
 */
public class MetadataTestReportGenerator
{
	private static Connection connection=null;
	private static Statement stmt = null;
	private static int succesCount, failureCount;
	private static int associationSuccesCount, associationFailureCount;
	static private BufferedWriter successWriter = null;
	static private BufferedWriter failureWriter = null;
	static private BufferedWriter associationFailureWriter = null;

	public static void main(String[] args) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException, MultipleRootsException, SqlException, HibernateException, SQLException, IOException
	{
		Logger.configure();
		
		EntityManagerInterface entityManager = EntityManager.getInstance();
		Collection<EntityInterface> entities = entityManager.getAllEntities();
		int totalNoOfEntities = entities.size();
		System.out.println("No Of entities:"+totalNoOfEntities);
		
		connection = DBUtil.getConnection();
		stmt = connection.createStatement();
		
		String path = "";
		if (args.length>0)
		{
			path=args[0];
		}
		successWriter = new BufferedWriter(new FileWriter(path + "/succesfulQueries_Report.txt"));
		failureWriter = new BufferedWriter(new FileWriter(path + "/failedQueries_Report.txt"));
		associationFailureWriter = new BufferedWriter(new FileWriter(path + "/AssociationQueries_Report.txt"));

		int cnt = 1;
		
		for(EntityInterface entity: entities)
		{
			System.out.println("Processing Entity: "+ cnt++ +"/"+totalNoOfEntities);
//			if (entity.getParentEntity()==null)
			{
				IQuery query = createQuery(entity);
				ISqlGenerator sqlGenerator = SqlGeneratorFactory.getInstance();
				String sql = sqlGenerator.generateSQL(query);
				executeSQL(sql, entity);
				testAllAssociations(entity);
			}
		}
		successWriter.close();
		failureWriter.close();
		
		System.out.println("Done.......");
		System.out.println("SuccessFul Queries: "+succesCount);
		System.out.println("Failed Queries: "+failureCount);
		System.out.println("SuccessFul Assocation Queries: "+associationSuccesCount);
		System.out.println("Failed Association Queries: "+associationFailureCount);
	}
	/**
	 * To test All association of an Entity.
	 * @param entity the reference to entity
	 * @throws IOException If an I/O error occurs
	 */
	private static void testAllAssociations(EntityInterface entity) throws IOException
	{
		Collection<AssociationInterface> associationCollection = entity.getAssociationCollection();
		if (!associationCollection.isEmpty())
		{
			associationFailureWriter.write("\n----------------------------------------------------------------------------");
			associationFailureWriter.write("\nEntityName:"+entity.getName());
			for (AssociationInterface association: associationCollection)
			{
				IQuery query = getQuery(entity, association);
				try
				{
					associationFailureWriter.write("\n\nAssociation: "+ association.getEntity().getName()+"--->"+ association.getTargetEntity().getName()+"("+association.getSourceRole().getName()+":"+association.getTargetRole().getName()+")");
					String sql = SqlGeneratorFactory.getInstance().generateSQL(query);
					executeSQL(sql, association);
				}
				catch (Exception e)
				{
					associationFailureWriter.write("\nAssocation Id: "+ association.getId());
					associationFailureWriter.write("\nSQLGenerator Exception: "+e.getMessage());
					e.printStackTrace();
					associationFailureCount++;
				}
			}
		}
	}
	/**
	 * To get the query object for given entity & association.
	 * @param entity the reference of entity
	 * @param association the reference to association
	 * @return The dummy query for given entity & association.
	 */
	private static IQuery getQuery(EntityInterface entity, AssociationInterface association)
	{
		EntityInterface associatedEntity = association.getTargetEntity();
		IQuery query = QueryObjectFactory.createQuery();
		
		IConstraints constraints = query.getConstraints();
		
		IQueryEntity queryEntity = QueryObjectFactory.createQueryEntity(entity);
		IExpression expression = constraints.addExpression(queryEntity);
		expression.setInView(true);
		IQueryEntity childConstraintEntity = QueryObjectFactory.createQueryEntity(associatedEntity);
		IExpression childExpression = constraints.addExpression(childConstraintEntity);
		expression.addOperand(childExpression);
		childExpression.setInView(true);
		
		IIntraModelAssociation Iassociation = QueryObjectFactory.createIntraModelAssociation(association);
		try
		{
			constraints.getJoinGraph().putAssociation(expression,childExpression, Iassociation);
		}
		catch (CyclicException e)
		{
			throw new RuntimeException("This Exception should not occur!!!!", e);
		}
		IRule rule = QueryObjectFactory.createRule();
		childExpression.addOperand(rule);
		ICondition condition = rule.addCondition();
		condition.setAttribute(getPrimaryKey(associatedEntity));
		condition.setRelationalOperator(RelationalOperator.IsNotNull);
		condition.setValues(new ArrayList<String>());
		return query;
	}
	
	/**
	 * To execute the given SQL on db.
	 * @param sql the SQL to be executed.
	 * @param association the reference to association
	 * @throws IOException If an I/O error occurs
	 */
	private static void executeSQL(String sql, AssociationInterface association) throws IOException
	{
		try
		{
			stmt.executeQuery(sql);
			associationFailureWriter.write("\nSQL: "+ sql);
			associationFailureWriter.write("\nSuccesfully Executed !!!!!!");
			associationSuccesCount++;
		}
		catch (SQLException e)
		{
			associationFailureWriter.write("\nAssocation Id: "+ association.getId());
			associationFailureWriter.write("\nSQL: "+ sql);
			associationFailureWriter.write("\nException: "+e.getMessage());
			associationFailureCount++;
		}
	}
	
	/**
	 * TO create dummy query for entity. "select * from entity where entity.id is not null"
	 * @param entity the referenec to entity.
	 * @return The reference to query object.
	 */
	private static IQuery createQuery(EntityInterface entity)
	{
		IQuery query = QueryObjectFactory.createQuery();
		
		IConstraints constraints = query.getConstraints();
		IQueryEntity queryEntity = QueryObjectFactory.createQueryEntity(entity);
		IExpression expression = constraints.addExpression(queryEntity);
		expression.setInView(true);
		IRule rule = QueryObjectFactory.createRule();
		expression.addOperand(rule);
		
		ICondition condition = rule.addCondition();
		condition.setAttribute(getPrimaryKey(entity));
		condition.setRelationalOperator(RelationalOperator.IsNotNull);
		condition.setValues(new ArrayList<String>());
		
		return query;
	}
	
	/**
	 * To get the primary key attribute of the given entity.
	 * @param entity the DE entity.
	 * @return The Primary key attribute of the given entity.
	 */
	private static AttributeInterface getPrimaryKey(EntityInterface entity)
	{
		Collection<AttributeInterface> attributes = entity.getAttributeCollection();
		for (AttributeInterface attribute : attributes)
		{
			if (attribute.getIsPrimaryKey() || attribute.getName().equals("id"))
			{
				return attribute;
			}
		}
		throw new RuntimeException("No Primary key attribute found for Entity:" + entity.getName());
	}
	
	/**
	 * To execute the given sql on db for the given entity.
	 * @param sql the sql to be executed.
	 * @param entity the reference to entity.
	 * @throws IOException If an I/O error occurs
	 */
	private static void executeSQL(String sql, EntityInterface entity) throws IOException
	{
		try
		{
			stmt.executeQuery(sql);
			successWriter.write("\n-----------------------");
			successWriter.write("\nEntityName: "+ entity.getName());
			successWriter.write("\nSQL: "+ sql);
			successWriter.write("\nSuccesfully Executed !!!!!!");
			succesCount++;
		}
		catch (SQLException e)
		{
			failureWriter.write("\n-----------------------");
			failureWriter.write("\nEntityName: "+entity.getName());
			failureWriter.write("\nSQL: "+ sql);
			failureWriter.write("\nException: "+e.getMessage()+"\n");
			
			failureWriter.write("\nTable:"+entity.getName()+":"+entity.getTableProperties().getName());
			Collection<AttributeInterface> attributes = entity.getAttributeCollection();
			for (AttributeInterface attribute : attributes)
			{
				failureWriter.write("\n"+attribute.getName()+":"+ attribute.getColumnProperties().getName());
			}
			
			failureCount++;
		}
	}
}