
package edu.wustl.cab2bwebapp.bizlogic.executequery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.globus.gsi.GlobusCredential;

import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.server.queryengine.QueryExecutor;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionOperand;
import edu.wustl.common.querysuite.queryobject.IRule;

/**
 * This class executes the queries in groups and returns the consolidated result.
 *
 * @author chetan_patil
 */
public class SearchQueryExecutor {

	/**
	 * This method retrieves all keyword queries corresponding to the given entity groups and created/owned by the given
	 * user. Before execution, the provided keyword is replaced with the values of the conditions in all the queries.
	 *
	 * @param keyword value to be searched
	 * @param globusCredential prxoy certificate of the user
	 * @return
	 */
	public Map<ICab2bQuery, TransformedResultObjectWithContactInfo> execute(Collection<ICab2bQuery> regularQueries,
			GlobusCredential globusCredential) {
		for (ICab2bQuery query : regularQueries) {
			if (query.isKeywordSearch()) {
				throw new IllegalStateException(query.getName() + " is not a regular query.");
			}
		}

		Map<ICab2bQuery, IQueryResult<? extends IRecord>> result = new HashMap<ICab2bQuery, IQueryResult<? extends IRecord>>();
		for (ICab2bQuery query : regularQueries) {
			IQueryResult<? extends IRecord> queryResult = new QueryExecutor(query, globusCredential).executeQuery();
			result.put(query, queryResult);
		}

		return transformResult(result);
	}

	/**
	 * This method retrieves all keyword queries corresponding to the given entity groups and created/owned by the given
	 * user. Before execution, the provided keyword is replaced with the values of the conditions in all the queries.
	 *
	 * @param keyword value to be searched
	 * @param globusCredential prxoy certificate of the user
	 * @return
	 */
	public Map<ICab2bQuery, TransformedResultObjectWithContactInfo> execute(Collection<ICab2bQuery> keywordQueries,
			String keyword, GlobusCredential globusCredential) {
		for (ICab2bQuery query : keywordQueries) {
			if (!query.isKeywordSearch()) {
				throw new IllegalStateException(query.getName() + " is not a keyword search query.");
			}
		}

		Map<ICab2bQuery, IQueryResult<? extends IRecord>> result = new HashMap<ICab2bQuery, IQueryResult<? extends IRecord>>();
		for (ICab2bQuery query : keywordQueries) {
			insertKeywordInQueries(query, keyword);

			IQueryResult<? extends IRecord> queryResult = new QueryExecutor(query, globusCredential).executeQuery();
			result.put(query, queryResult);
		}

		return transformResult(result);
	}

	/**
	 * This method replaces the values of the conditions of given query with the provided keyword.
	 * @param query
	 * @param keyword
	 */
	void insertKeywordInQueries(ICab2bQuery query, final String keyword) {
		for (IExpression expression : query.getConstraints()) {
			for (IExpressionOperand operand : expression) {
				if (operand instanceof IRule) {
					IRule rule = (IRule) operand;
					for (ICondition condition : rule) {
						List<String> values = new ArrayList<String>();
						values.add(keyword);

						condition.setValues(values);
					}
				}
			}
		}
	}

	/**
	 * Returns results for given query name in spreadsheet data model format.
	 * If result is already transformed into spreadsheet date-model and available in
	 * <code>queryToTransformedResultMap</code> format returns it directly.
	 * If result is not transformed into spreadsheet data-model format then iterate,
	 * find result for "queryName", transform to data-model format and add to queryToTransformedResultMap.
	 * @param queryName
	 * @return List<Map<AttributeInterface, Object>>
	 */
	Map<ICab2bQuery, TransformedResultObjectWithContactInfo> transformResult(
			Map<ICab2bQuery, IQueryResult<? extends IRecord>> rawResults) {
		Map<ICab2bQuery, TransformedResultObjectWithContactInfo> transformedResult = new HashMap<ICab2bQuery, TransformedResultObjectWithContactInfo>();

		SpreadSheetResultTransformer transformer = new SpreadSheetResultTransformer();
		Set<ICab2bQuery> queries = rawResults.keySet();
		for (ICab2bQuery query : queries) {
			IQueryResult<? extends IRecord> originalResult = rawResults.get(query);
			TransformedResultObjectWithContactInfo resultObj = transformer.transResultToSpreadSheetView(query,
					originalResult);
			transformedResult.put(query, resultObj);
		}
		return transformedResult;
	}
}
