
package edu.wustl.common.querysuite.queryengine;

import edu.wustl.common.querysuite.queryobject.IQuery;

/**
 * @version 1.0
 * @created 03-Oct-2006 11:50:05 AM
 */
public interface IQueryExecutor
{

	/**
	 * TODO
	 * @throws Throwable TODO
	 */
	void finalize() throws Throwable;

	/**
	 * @param query The reference to the query to be executed.
	 */
	void executeQuery(IQuery query);
}