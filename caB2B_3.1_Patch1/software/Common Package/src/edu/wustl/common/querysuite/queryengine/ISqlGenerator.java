/**
 * 
 */

package edu.wustl.common.querysuite.queryengine;

import java.util.List;
import java.util.Map;

import edu.wustl.common.querysuite.exceptions.MultipleRootsException;
import edu.wustl.common.querysuite.exceptions.SqlException;
import edu.wustl.common.querysuite.queryobject.IOutputTerm;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.querysuite.queryobject.impl.OutputTreeDataNode;

/**
 * To Generate SQL for the given Query Object.
 * 
 * @author prafull_kadam
 * 
 */
public interface ISqlGenerator {

    /**
     * Generates SQL for the given Query Object.
     * 
     * @param query The Reference to Query Object.
     * @return the String representing SQL for the given Query object.
     * @throws MultipleRootsException When there are multpile roots present in a
     *             graph.
     * @throws SqlException When there is error in the passed IQuery object.
     */
    String generateSQL(IQuery query) throws MultipleRootsException, SqlException;

    List<OutputTreeDataNode> getRootOutputTreeNodeList();

    /**
     * @return map with key as sql's column name and value as the term which
     *         that column represents.
     */
    Map<String, IOutputTerm> getOutputTermsColumns();
}
