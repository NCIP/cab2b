
package edu.wustl.common.querysuite;

/**
 * 
 */

import junit.framework.Test;
import junit.framework.TestSuite;
import edu.wustl.common.querysuite.queryengine.impl.SqlGeneratorGenericTestCase;
import edu.wustl.common.querysuite.queryengine.impl.SqlGeneratorTestCase;
import edu.wustl.common.querysuite.queryobject.impl.ExpressionTestCase;
import edu.wustl.common.querysuite.queryobject.impl.JoinGraphTestCase;

/**
 * @author prafull_kadam
 * Test Suite for testing all Query Interface related classes.
 */
public class TestAll
{

	public static void main(String[] args)
	{
		junit.swingui.TestRunner.run(TestAll.class);
	}

	public static Test suite()
	{
		TestSuite suite = new TestSuite("Test suite for Query Interface Classes");
		suite.addTestSuite(ExpressionTestCase.class);
		suite.addTestSuite(JoinGraphTestCase.class);
		suite.addTestSuite(SqlGeneratorTestCase.class);
		suite.addTestSuite(SqlGeneratorGenericTestCase.class);
		return suite;
	}
}
