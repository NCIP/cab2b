/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.common.dynamicextensions;

/**
 * 
 */

import junit.framework.Test;
import junit.framework.TestSuite;
import edu.common.dynamicextensions.entitymanager.TestEntityManager;
import edu.common.dynamicextensions.entitymanager.TestEntityManagerForAssociations;
import edu.common.dynamicextensions.entitymanager.TestEntityManagerForInheritance;

/**
 * Test Suite for testing all DE  related classes.
 */
public class TestAll
{

	/**
	 * @param args arg
	 */
	public static void main(String[] args)
	{
		junit.swingui.TestRunner.run(TestAll.class);
	}

	/**
	 * @return etst
	 */
	public static Test suite()
	{
		TestSuite suite = new TestSuite("Test suite for Query Interface Classes");
		suite.addTestSuite(TestEntityManager.class);
		suite.addTestSuite(TestEntityManagerForAssociations.class);
		suite.addTestSuite(TestEntityManagerForInheritance.class);
		return suite;
	}
}
