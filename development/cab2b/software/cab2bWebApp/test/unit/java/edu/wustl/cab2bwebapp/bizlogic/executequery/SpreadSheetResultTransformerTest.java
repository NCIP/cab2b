/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 *
 */

package edu.wustl.cab2bwebapp.bizlogic.executequery;

import junit.framework.TestCase;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.server.queryengine.QueryExecutor;

/**
 * JUnitTest cases for  <code>SpreadSheetResultTransformer</code> class.
 * @author deepak_shingan
 *
 */
public class SpreadSheetResultTransformerTest extends TestCase {

	/**
	 * Test method for transforming query results in <code>IQueryResult</code> format
	 * into spreadsheet data-model format when passed query parameter is null.
	 */
	public void testTransResultToSpreadSheetView_QueryParameterIsNull() {
		SpreadSheetResultTransformer trnsformer = new SpreadSheetResultTransformer();
		ICab2bQuery query = new QueryOperationsTest().createQuery();
		QueryExecutor exe = new QueryExecutor(query, null);
		IQueryResult<? extends IRecord> result = exe.executeQuery();
		TransformedResultObjectWithContactInfo transformedResult = trnsformer.transResultToSpreadSheetView(null,
				result);
		assertNull("Query object is null, should return null", transformedResult);
	}

	/**
	 * Test method for transforming query results in <code>IQueryResult</code> format
	 * into spreadsheet data-model format when passed query result parameter is null.
	 */
	public void testTransResultToSpreadSheetView_QueryResultParameterIsNull() {
		SpreadSheetResultTransformer trnsformer = new SpreadSheetResultTransformer();
		ICab2bQuery query = new QueryOperationsTest().createQuery();

		TransformedResultObjectWithContactInfo transformedResult = trnsformer.transResultToSpreadSheetView(query,
				null);
		assertNull("Query result object is null, method should return null", transformedResult);
	}

	/**
	 * Test method for transforming query results in <code>IQueryResult</code> format
	 * into spreadsheet data-model format.
	 */
	public void testTransResultToSpreadSheetView() {
		SpreadSheetResultTransformer trnsformer = new SpreadSheetResultTransformer();
		ICab2bQuery query = new QueryOperationsTest().createQuery();
		QueryExecutor exe = new QueryExecutor(query, null);
		IQueryResult<? extends IRecord> result = exe.executeQuery();
		TransformedResultObjectWithContactInfo transformedResult = trnsformer.transResultToSpreadSheetView(query,
				result);
		assertNotNull("Valid input, method should not return null", transformedResult);
	}
}
