/**
 *
 */
package edu.wustl.cab2b.server.queryengine;

import junit.framework.TestCase;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;

/**
 * @author chetan_patil
 *
 */
public class QueryConverterTest extends TestCase {

    public void testIsORedQuery() {
        ICab2bQuery query = new MockQueryObjects().createCaFEGeneQuery();
        assertEquals(Boolean.FALSE, new QueryConverter().isKeywordQuery(query));
    }

    public void testChangeConditionOperatorPositive() {
        ICab2bQuery query = new MockQueryObjects().createQuery_GenemRNAProtein();

        QueryConverter queryConverter = new QueryConverter();
        ICab2bQuery oredQuery = queryConverter.convertToKeywordQuery(query);

        assertEquals(Boolean.TRUE, queryConverter.isKeywordQuery(oredQuery));
    }

    public void testExecuteORedQuery() {
        ICab2bQuery oredQuery = new MockQueryObjects().createQuery_Gene_mRNA_Protein();
        assertEquals(Boolean.TRUE, new QueryConverter().isKeywordQuery(oredQuery));

        QueryExecutor executor = new QueryExecutor(oredQuery, null);
        try {
            executor.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            assertFalse(e.getMessage(), true);
        }
    }

    public void testIsFeasibleToConvert() {
        ICab2bQuery query = new MockQueryObjects().createQuery_GenemRNAProtein();
        assertEquals(Boolean.TRUE, new QueryConverter().isFeasibleToConvert(query));
    }

}
