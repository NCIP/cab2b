/**
 * 
 */

package edu.wustl.common.querysuite.queryobject.impl;

import junit.framework.TestCase;

/**
 * @author prafull_kadam
 * Test case for JoinGraph class.
 */
public class JoinGraphTestCase extends TestCase
{

//	IJoinGraph joinGraph;
//	IExpressionId expId1 = QueryObjectFactory.createExpressionId(1);
//	IExpressionId expId2 = QueryObjectFactory.createExpressionId(2);
//	IExpressionId expId3 = QueryObjectFactory.createExpressionId(3);
//	IExpressionId expId4 = QueryObjectFactory.createExpressionId(4);
//	IExpressionId expId5 = QueryObjectFactory.createExpressionId(5);
//
//	IAssociation association12 = QueryObjectFactory.createIntraModelAssociation(null);
//	IAssociation association13 = QueryObjectFactory.createIntraModelAssociation(null);
//	IAssociation association14 = QueryObjectFactory.createIntraModelAssociation(null);
//	IAssociation association25 = QueryObjectFactory.createIntraModelAssociation(null);
//
//	/**
//	 * @see junit.framework.TestCase#setUp()
//	 * Creating Graph as:
//	 * 
//	 * 	exp1 -->Exp2-->Exp5
//	 * 	  \ \-->Exp3
//	 * 	   \-->Exp4	
//	 */
//	@Override
//	protected void setUp() throws Exception
//	{
//		joinGraph = new JoinGraph();
//
//		joinGraph.putAssociation(expId1, expId2, association12);
//		joinGraph.putAssociation(expId1, expId3, association13);
//		joinGraph.putAssociation(expId1, expId4, association14);
//		joinGraph.putAssociation(expId2, expId5, association25);
//
//		super.setUp();
//	}
//
//	/**
//	 * To check simple case of putAssociation method. After adding Association between ExpressionIds which do not have previously any association.
//	 * The return value by method should be null when there is no association existing previously.
//	 */
//	public void testputAssociation1()
//	{
//		IExpressionId expId6 = QueryObjectFactory.createExpressionId(6);
//		IAssociation association16 = QueryObjectFactory.createIntraModelAssociation(null);
//		try
//		{
//			IAssociation newAssociation = joinGraph.putAssociation(expId1, expId6, association16);
//			assertNull("Expected null value of Association!!!", newAssociation);
//		}
//		catch (CyclicException e)
//		{
//			assertTrue("UnExpected CyclicException, while adding Association!!!", false);
//		}
//	}
//
//	/**
//	 * To check the Override association functionality.
//	 * If user tries to override the Association, then putAssociation() method should override the old assocation & return old association.
//	 */
//	public void testputAssociation2()
//	{
//		IAssociation newAssociation12 = QueryObjectFactory.createIntraModelAssociation(null);
//		try
//		{
//			IAssociation oldAssociation = joinGraph
//					.putAssociation(expId1, expId2, newAssociation12);
//
//			assertEquals(
//					"While overridding association, putAssociation method did not returned the correct Old association!!!",
//					oldAssociation, association12);
//			assertEquals(
//					"While overridding association, putAssociation method did not overrided the association properly!!!",
//					newAssociation12, joinGraph.getAssociation(expId1, expId2));
//		}
//		catch (CyclicException e)
//		{
//			assertTrue("UnExpected CyclicException, while adding Association!!!", false);
//		}
//	}
//
//	/**
//	 * To check CyclicException condition of putAssociation method. 
//	 * If user tries to add an Assiciation which is causing Cycle, the it should throw Exception.
//	 *     <------------
//	 *    |             |
//	 * 	exp1 -->Exp2-->Exp5
//	 * 	  \ \-->Exp3
//	 * 	   \--->Exp4	
//	 */
//	public void testputAssociation3()
//	{
//		IAssociation association51 = QueryObjectFactory.createIntraModelAssociation(null);
//		try
//		{
//			IAssociation newAssociation = joinGraph.putAssociation(expId5, expId1, association51);
//			assertTrue("Expected CyclicException!!!", false);
//			assertNull("Expected null value of Association!!!", newAssociation);
//		}
//		catch (CyclicException e)
//		{
//		}
//	}
//
//	/**
//	 * To check CyclicException condition of putAssociation method. 
//	 * Adding edge in Exp3 to Exp5 should not throw CyclicException.
//	 * 
//	 * 	exp1-->Exp2-->Exp5
//	 * 	  \ \-->Exp3-->|
//	 * 	   \-->Exp4	
//	 */
//	public void testputAssociation4()
//	{
//		IAssociation association35 = QueryObjectFactory.createIntraModelAssociation(null);
//		try
//		{
//			IAssociation newAssociation = joinGraph.putAssociation(expId3, expId5, association35);
//			assertNull("Expected null value of Association!!!", newAssociation);
//		}
//		catch (CyclicException e)
//		{
//			assertTrue("UnExpected CyclicException, while adding Association!!!", false);
//		}
//	}
//
//	/**
//	 * To check CyclicException condition of putAssociation method. 
//	 * If user tries to add an Assiciation after doing Add/Remove associations it should not get Exception for following graph.
//	 *  
//	 * 	exp1<--Exp2-->Exp5
//	 * 	  \ \-->Exp3-->|
//	 * 	   \-->Exp4
//	 * 
//	 * Here From the original graph removed Edge from Exp1 to Exp2 & Add Edge from Exp2 to Exp1.	
//	 */
//	public void testputAssociation5()
//	{
//		IAssociation association21 = QueryObjectFactory.createIntraModelAssociation(null);
//		assertTrue("Expected true value from removeAssociation method, for existing edge!!!",
//				joinGraph.removeAssociation(expId1, expId2));
//
//		try
//		{
//			IAssociation newAssociation = joinGraph.putAssociation(expId2, expId1, association21);
//			assertNull("Expected null value of Association!!!", newAssociation);
//		}
//		catch (CyclicException e)
//		{
//			assertTrue("UnExpected CyclicException, while adding Association!!!", false);
//		}
//	}
//
//	/**
//	 *  To check the getRoot() method with positive test case. for the created graph it will return exp1 node.
//	 *
//	 * 	exp1-->Exp2-->Exp5
//	 * 	  \ \-->Exp3
//	 * 	   \-->Exp4	
//	 */
//	public void testGetRoot1()
//	{
//		try
//		{
//			IExpressionId expressionId = joinGraph.getRoot();
//			assertNotNull("Expected Not null value of ExpressionId from getRoot Method!!!",
//					expressionId);
//			assertEquals(
//					"ExpressionId returned from getRoot Method is not equal to the actual Root node!!!",
//					expressionId, expId1);
//		}
//		catch (MultipleRootsException e)
//		{
//			assertTrue("UnExpected MultipleRootsException, from getRoot method!!!", false);
//		}
//	}
//
//	/**
//	 *  To check the getRoot() method.
//	 *  
//	 *  After removing Edge between exp1 & exp2, the graph will become disjoint graph, hence exp1 & exp2 will have no incomming edges, hence both will considered as root node & method getRoot will throw MultipleRootsException. 
//	 *  
//	 * 	exp1   Exp2-->Exp5
//	 * 	  \ \-->Exp3
//	 * 	   \-->Exp4	
//	 */
//	public void testGetRoot2()
//	{
//		try
//		{
//			joinGraph.removeAssociation(expId1, expId2);
//			joinGraph.getRoot();
//			assertTrue("Expected MultipleRootsException, from getRoot method!!!", false);
//		}
//		catch (MultipleRootsException e)
//		{
//		}
//	}
//
//	/**
//	 *  To check the positive test case for removeAssociation() method.
//	 *  
//	 * 	exp1-->Exp2-->Exp5
//	 * 	  \ \-->Exp3
//	 * 	   \--->Exp4	
//	 */
//	public void testRemoveAssociation1()
//	{
//		boolean flag = joinGraph.removeAssociation(expId1, expId2);
//		assertTrue("Expected true value from removeAssociation method, for existing edge!!!", flag);
//		IAssociation association = joinGraph.getAssociation(expId1, expId2);
//		assertNull("Expected null value for the removed association", association);
//	}
//
//	/**
//	 *  To check the removeAssociation() method. while removing not existing edge by calling removeAssociation method should return false value.
//	 *  
//	 * 	exp1-->Exp2-->Exp5
//	 * 	  \ \-->Exp3
//	 * 	   \-->Exp4	
//	 */
//	public void testRemoveAssociation2()
//	{
//		boolean flag = joinGraph.removeAssociation(expId2, expId3);
//		assertFalse("Expected false value from removeAssociation method, for non-existing edge!!!",
//				flag);
//	}
//
//	/**
//	 *  To check the isConnected() method.
//	 *  Following graph is connected graph.
//	 * 	exp1-->Exp2-->Exp5
//	 * 	  \ \-->Exp3
//	 * 	   \-->Exp4	
//	 */
//	public void testIsConnected1()
//	{
//		boolean flag = joinGraph.isConnected();
//		assertTrue("Expected true value from isConnected method, for Connected graph!!!", flag);
//
//	}
//
//	/**
//	 *  To check the isConnected() method.
//	 *  
//	 *  After removing association between exp1 & exp2, following graph will become disconnected graph.
//	 *  
//	 * 	exp1-->Exp2-->Exp5
//	 * 	  \ \-->Exp3
//	 * 	   \-->Exp4	
//	 */
//	public void testIsConnected2()
//	{
//		joinGraph.removeAssociation(expId1, expId2);
//		boolean flag = joinGraph.isConnected();
//		assertFalse("Expected false value from isConnected method, for disconnected graph!!!", flag);
//	}
//
//	/**
//	 *  To check the isConnected() method.
//	 *  
//	 *  Following graph is connected graph. This Graph contains only one association.
//	 *  
//	 * 	exp1-->Exp2
//	 * 	
//	 */
//	public void testIsConnected3()
//	{
//		IJoinGraph theJoinGraph = new JoinGraph();
//		try
//		{
//			theJoinGraph.putAssociation(expId1, expId2, association12);
//			boolean flag = theJoinGraph.isConnected();
//			assertTrue("Expected true value from isConnected method, for Connected graph!!!", flag);
//		}
//		catch (CyclicException e)
//		{
//			assertTrue(
//					"UnExpected CyclicException, while adding Association for Testing isConnected method!!!",
//					false);
//		}
//	}
//
//	/**
//	 * To check containsAssociation method.
//	 *
//	 * 	exp1-->Exp2-->Exp5
//	 * 	  \ \-->Exp3
//	 * 	   \-->Exp4	
//	 */
//	public void testContainsAssociation1()
//	{
//		assertTrue("Expected true value from containsAssociation method!!!", joinGraph
//				.containsAssociation(expId1, expId2));
//		assertTrue("Expected true value from containsAssociation method!!!", joinGraph
//				.containsAssociation(expId2, expId5));
//		assertFalse("Expected false value from containsAssociation method!!!", joinGraph
//				.containsAssociation(expId1, expId5));
//	}
//
//	/**
//	 *  To check getAssociation method.
//	 */
//	public void testGetAssociation()
//	{
//		IAssociation association = joinGraph.getAssociation(expId1, expId2);
//		assertEquals(association, association12);
//
//		association = joinGraph.getAssociation(expId1, expId5);
//		assertEquals(association, null);
//
//	}
}
