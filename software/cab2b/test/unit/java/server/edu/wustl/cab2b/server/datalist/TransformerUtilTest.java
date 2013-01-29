/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.datalist;

import junit.framework.TestCase;
import edu.wustl.cab2b.server.queryengine.utils.TransformerUtil;
import edu.wustl.common.querysuite.queryobject.LogicalOperator;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;
import gov.nih.nci.cagrid.cqlquery.Predicate;

/**
 * @author chandrakant_talele
 */
public class TransformerUtilTest extends TestCase {
    public void testGetCqlLogicalOperator() {
        assertEquals(gov.nih.nci.cagrid.cqlquery.LogicalOperator.AND, TransformerUtil.getCqlLogicalOperator(LogicalOperator.And));
        assertEquals(gov.nih.nci.cagrid.cqlquery.LogicalOperator.OR, TransformerUtil.getCqlLogicalOperator(LogicalOperator.Or));
    }

    public void testGetCqlPredicate() {
        assertEquals(Predicate.EQUAL_TO, TransformerUtil.getCqlPredicate(RelationalOperator.Equals));
        assertEquals(Predicate.NOT_EQUAL_TO, TransformerUtil.getCqlPredicate(RelationalOperator.NotEquals));
        assertEquals(Predicate.LESS_THAN, TransformerUtil.getCqlPredicate(RelationalOperator.LessThan));
        assertEquals(Predicate.LESS_THAN_EQUAL_TO,
                     TransformerUtil.getCqlPredicate(RelationalOperator.LessThanOrEquals));
        assertEquals(Predicate.GREATER_THAN, TransformerUtil.getCqlPredicate(RelationalOperator.GreaterThan));
        assertEquals(Predicate.GREATER_THAN_EQUAL_TO,
                     TransformerUtil.getCqlPredicate(RelationalOperator.GreaterThanOrEquals));
        assertEquals(Predicate.LIKE, TransformerUtil.getCqlPredicate(RelationalOperator.Contains));
        assertEquals(Predicate.LIKE, TransformerUtil.getCqlPredicate(RelationalOperator.StartsWith));
        assertEquals(Predicate.LIKE, TransformerUtil.getCqlPredicate(RelationalOperator.EndsWith));
        assertEquals(Predicate.IS_NULL, TransformerUtil.getCqlPredicate(RelationalOperator.IsNull));
        assertEquals(Predicate.IS_NOT_NULL, TransformerUtil.getCqlPredicate(RelationalOperator.IsNotNull));

        //assertEquals(Predicate.,TransformerUtil.getCqlPredicate(RelationalOperator.Between));
        //assertEquals(Predicate.,TransformerUtil.getCqlPredicate(RelationalOperator.In));
        //assertEquals(Predicate.,TransformerUtil.getCqlPredicate(RelationalOperator.NotIn));
    }
}
