/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright:TODO</p>
 *@author Vishvesh Mulay
 *@version 1.0
 */

package edu.common.dynamicextensions.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import junit.framework.TestCase;
import org.hibernate.HibernateException;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.RoleInterface;
import edu.common.dynamicextensions.entitymanager.EntityManagerExceptionConstantsInterface;
import edu.common.dynamicextensions.util.global.Constants;
import edu.common.dynamicextensions.util.global.Variables;
import edu.common.dynamicextensions.util.global.Constants.AssociationType;
import edu.common.dynamicextensions.util.global.Constants.Cardinality;
import edu.wustl.common.util.dbManager.DBUtil;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;

public class DynamicExtensionsBaseTestCase extends TestCase implements EntityManagerExceptionConstantsInterface
{

	protected int noOfDefaultColumns = 2;
	
	//1:ACTIVITY_STATUS 2:IDENTIFIER 3:FILE NAME 4:CONTENTE_TYPE 5:ACTUAL_CONTENTS
	protected int noOfDefaultColumnsForfile = 5;
	/**
	 * 
	 */
	public DynamicExtensionsBaseTestCase()
	{
		super();
		
	}

	/**
	 * @param arg0 name
	 */
	public DynamicExtensionsBaseTestCase(String arg0)
	{
		super(arg0);
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp()
	{
		
		Logger.out = org.apache.log4j.Logger.getLogger("dynamicExtensions.logger");
        ApplicationProperties.initBundle("ApplicationResources");
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown()
	{
		Variables.containerFlag = true;
	}

	/**
	 * @param query query to be executed
	 * @return 
	 */
	protected ResultSet executeQuery(String query)
	{
		//      Checking whether the data table is created properly or not.
		Connection conn = null;
		try
		{
			conn = DBUtil.getConnection();
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
		}
		java.sql.PreparedStatement statement = null;
		try
		{
			statement = conn.prepareStatement(query);
			return statement.executeQuery();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			fail();
		}

		return null;
	}
	
	/**
	 * @param query query to be executed
	 * @return  ResultSetMetaData
	 */
	protected ResultSetMetaData executeQueryForMetadata(String query)
	{
		//      Checking whether the data table is created properly or not.
		Connection conn = null;
		try
		{
			conn = DBUtil.getConnection();
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
		}
		java.sql.PreparedStatement statement = null;
		try
		{
			statement = conn.prepareStatement(query);
			return statement.executeQuery().getMetaData();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			fail();
		}

		return null;
	}

	/**
	 * @param query query to be executed
	 * @return  ResultSetMetaData
	 */
	protected ResultSetMetaData executeQueryDDL(String query)
	{
		//      Checking whether the data table is created properly or not.
		Connection conn = null;
		try
		{
			conn = DBUtil.getConnection();
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
		}
		java.sql.PreparedStatement statement = null;
		try
		{
			statement = conn.prepareStatement(query);
			statement.execute(query);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			fail();
		}

		return null;
	}
	
	/**
	 * @param tableName
	 * @return
	 */
	protected boolean  isTablePresent(String tableName) {
		Connection conn = null;
		String query = "select * from " + tableName;
		try
		{
			conn = DBUtil.getConnection();
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
		}
		java.sql.PreparedStatement statement = null;
		try
		{
			statement = conn.prepareStatement(query);
			statement.executeQuery();
		}
		catch (SQLException e)
		{
			return false;
		}
		return true;
	}
	
	protected String getActivityStatus(EntityInterface entity , Long recordId) throws Exception {
		ResultSet resultSet = executeQuery("select " + Constants.ACTIVITY_STATUS_COLUMN +  " from "
				+ entity.getTableProperties().getName()  + " where identifier = " + recordId);
		resultSet.next();
		return resultSet.getString(1);
	}
	
	/**
	 * @param associationType associationType
	 * @param name name
	 * @param minCard  minCard
	 * @param maxCard maxCard
	 * @return  RoleInterface
	 */
	protected RoleInterface getRole(AssociationType associationType, String name,
			Cardinality minCard, Cardinality maxCard)
	{
		RoleInterface role = DomainObjectFactory.getInstance().createRole();
		role.setAssociationsType(associationType);
		role.setName(name);
		role.setMinimumCardinality(minCard);
		role.setMaximumCardinality(maxCard);
		return role;
	}

}
