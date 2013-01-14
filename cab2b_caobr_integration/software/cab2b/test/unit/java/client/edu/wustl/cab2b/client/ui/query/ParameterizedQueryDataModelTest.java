/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */
package edu.wustl.cab2b.client.ui.query;

import junit.framework.TestCase;
import edu.wustl.cab2b.client.ui.parameterizedQuery.ParameterizedQueryDataModel;
import edu.wustl.cab2b.common.queryengine.Cab2bQuery;

/**
 * @author deepak_shingan
 *
 */
public class ParameterizedQueryDataModelTest extends TestCase {
    ParameterizedQueryDataModel modelObj = new ParameterizedQueryDataModel();

    /**
     * Test method for {@link edu.wustl.cab2b.client.ui.parameterizedQuery.ParameterizedQueryDataModel#ParameterizedQueryDataModel(edu.wustl.cab2b.common.queryengine.ICab2bQuery)}.
     */
    public final void testParameterizedQueryDataModelICab2bQuery() {
        assertNotNull(new ParameterizedQueryDataModel(null));
    }

    /**
     * Test method for {@link edu.wustl.cab2b.client.ui.parameterizedQuery.ParameterizedQueryDataModel#ParameterizedQueryDataModel(edu.wustl.cab2b.common.queryengine.ICab2bParameterizedQuery)}.
     */
    public final void testParameterizedQueryDataModelICab2bParameterizedQuery() {
        assertNotNull(new ParameterizedQueryDataModel(null));
    }

    /**
     * Test method for {@link edu.wustl.cab2b.client.ui.parameterizedQuery.ParameterizedQueryDataModel#setQuery(edu.wustl.cab2b.common.queryengine.ICab2bParameterizedQuery)}.
     */
    public final void testSetQuery() {
        //null query check
        modelObj.setQuery(null);
        assertEquals(null, modelObj.getQuery());
    }

    /**
     * Test method for {@link edu.wustl.cab2b.client.ui.parameterizedQuery.ParameterizedQueryDataModel#getQuery()}.
     */
    public final void testGetQuery() {
        Cab2bQuery testQuery = new Cab2bQuery();
        modelObj.setQuery(testQuery);
        assertEquals(testQuery, modelObj.getQuery());

        //null query check
        modelObj.setQuery(null);
        assertEquals(null, modelObj.getQuery());
    }

    /**
     * Test method for {@link edu.wustl.cab2b.client.ui.parameterizedQuery.ParameterizedQueryDataModel#getQueryName()}.
     */
    public final void testGetQueryName() {
        //null query get name check
        modelObj.setQuery(null);
        assertEquals(null, modelObj.getQueryName());
    }

    /**
     * Test method for {@link edu.wustl.cab2b.client.ui.parameterizedQuery.ParameterizedQueryDataModel#setQueryName(java.lang.String)}.
     */
    public final void testSetQueryName() {
        //null query get name check
        modelObj.setQuery(null);
        modelObj.setQueryName("TestQuery");
        assertNotSame(modelObj.getQueryName(), "TestQuery");

        //testing normal scenario
        modelObj.setQuery(new Cab2bQuery());
        modelObj.setQueryName("TestQuery");
        assertSame(modelObj.getQueryName(), "TestQuery");
    }

    /**
     * Test method for {@link edu.wustl.cab2b.client.ui.parameterizedQuery.ParameterizedQueryDataModel#getQueryDescription()}.
     */
    public final void testGetQueryDescription() {
        //null query get name check
        modelObj.setQuery(null);
        modelObj.setQueryDescription("TestQueryDescription");
        assertNotSame(modelObj.getQueryDescription(), "TestQueryDescription");

        //testing normal scenario
        modelObj.setQuery(new Cab2bQuery());
        modelObj.setQueryDescription("TestQuery");
        assertSame(modelObj.getQueryDescription(), "TestQuery");
    }

    /**
     * Test method for {@link edu.wustl.cab2b.client.ui.parameterizedQuery.ParameterizedQueryDataModel#setQueryDescription(java.lang.String)}.
     */
    public final void testSetQueryDescription() {
        //null query get name check
        modelObj.setQuery(null);
        modelObj.setQueryDescription("TestQueryDescription");
        assertNotSame(modelObj.getQueryDescription(), "TestQueryDescription");

        //testing normal scenario
        modelObj.setQuery(new Cab2bQuery());
        modelObj.setQueryDescription("TestQuery");
        assertSame(modelObj.getQueryDescription(), "TestQuery");
    }

    /**
     * Test method for {@link edu.wustl.cab2b.client.ui.parameterizedQuery.ParameterizedQueryDataModel#getQueryEntities()}.
     */
    public final void testGetQueryEntities() {
        //null query get entities check
        modelObj.setQuery(null);
        assertEquals(null, modelObj.getQueryEntities());

        modelObj.setQuery(new Cab2bQuery());
        assertNotNull(modelObj.getQueryEntities());
    }

    /**
     * Test method for {@link edu.wustl.cab2b.client.ui.parameterizedQuery.ParameterizedQueryDataModel#getConditions()}.
     */
    public final void testGetConditions() {
        modelObj.setQuery(null);
        assertEquals(null, modelObj.getConditions());

        modelObj.setQuery(new Cab2bQuery());
        assertNotNull(modelObj.getConditions());

    }

    /**
     * Test method for {@link edu.wustl.cab2b.client.ui.parameterizedQuery.ParameterizedQueryDataModel#getAllAttributes()}.
     */
    public final void testGetAllAttributes() {
        modelObj.setQuery(null);
        assertEquals(null, modelObj.getAllAttributes());

        modelObj.setQuery(new Cab2bQuery());
        assertNotNull(modelObj.getAllAttributes());
    }
}
