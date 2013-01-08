/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

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

    public void testConvertToKeywordQuery() {
        ICab2bQuery query = new MockQueryObjects().createQuery_GenemRNAProtein();
        query.setIsKeywordSearch(Boolean.TRUE);

        QueryConverter queryConverter = new QueryConverter();
        ICab2bQuery oredQuery = queryConverter.convertToKeywordQuery(query);

        assertEquals(Boolean.TRUE, queryConverter.isKeywordQuery(oredQuery));
    }

    public void testIsFeasibleToConvert() {
        ICab2bQuery query = new MockQueryObjects().createQuery_GenemRNAProtein();
        assertEquals(Boolean.TRUE, new QueryConverter().isFeasibleToConvert(query));
    }

}
