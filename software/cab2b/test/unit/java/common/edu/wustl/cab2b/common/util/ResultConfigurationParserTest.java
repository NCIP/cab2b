/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.util;

import junit.framework.TestCase;

/**
 * @author chandrakant_talele
 */
public class ResultConfigurationParserTest extends TestCase {
    /**
     * Test case to verify the getters for ResultTransformer, DataListSaver, DataListRetriever, ResultRenderer
     */
    public void testGetters() {
        ResultConfigurationParser parser = ResultConfigurationParser.getInstance();

        String transformer = parser.getResultTransformer("", "");
        assertEquals("edu.wustl.cab2b.server.queryengine.resulttransformers.DefaultQueryResultTransformer",
                     transformer);

        String saver = parser.getDataListSaver("", "");
        assertEquals("edu.wustl.cab2b.server.datalist.DefaultDataListSaver", saver);

        String retriever = parser.getDataListRetriever("", "");
        assertEquals("edu.wustl.cab2b.server.datalist.DefaultDataListRetriever", retriever);

        String renderer = parser.getResultRenderer("", "");
        assertEquals("edu.wustl.cab2b.client.ui.viewresults.DefaultDetailedPanel", renderer);
    }
}
