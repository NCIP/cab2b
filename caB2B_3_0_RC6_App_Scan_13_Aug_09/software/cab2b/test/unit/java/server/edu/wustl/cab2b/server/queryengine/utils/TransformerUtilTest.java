package edu.wustl.cab2b.server.queryengine.utils;

import edu.wustl.common.querysuite.queryobject.LogicalOperator;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import junit.framework.TestCase;

/**
 * @author chandrakant_talele
 */
public class TransformerUtilTest extends TestCase {

    public void testGetCqlLogicalOperator() {
        gov.nih.nci.cagrid.cqlquery.LogicalOperator opr = TransformerUtil.getCqlLogicalOperator(LogicalOperator.And);
        assertEquals(gov.nih.nci.cagrid.cqlquery.LogicalOperator.AND, opr);
        opr = TransformerUtil.getCqlLogicalOperator(LogicalOperator.Or);
        assertEquals(gov.nih.nci.cagrid.cqlquery.LogicalOperator.OR, opr);
    }

    public void testGetCqlPredicate() {
        Predicate predicate = TransformerUtil.getCqlPredicate(RelationalOperator.IsNotNull);
        assertEquals(Predicate.IS_NOT_NULL, predicate);
        try {
            predicate = TransformerUtil.getCqlPredicate(RelationalOperator.Between);
            assertTrue("Expected an UnsupportedOperationException but it was not thrown", false);
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }
}
