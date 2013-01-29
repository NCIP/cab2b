/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.util;

import junit.framework.TestCase;

/**
 * @author Chandrakant Talele
 */
public class IdGeneratorTest extends TestCase {
    /**
     * Tests getNext id method 
     */
    public void testInitialValue() {
        long id = 6778L;
        long val = new IdGenerator(id).getNextId();
        assertEquals(id, val);
    }

    /**
     * Tests getNext id method 
     */
    public void testGetNextIdDifferentInstances() {
        long id = 6778L;
        long val = new IdGenerator(id).getNextId();
        assertEquals(id, val);

        //different instances should not share value
        val = new IdGenerator(id).getNextId();
        assertEquals(id, val);
    }

    /**
     * Tests getNext id method 
     */
    public void testGetNextId() {
        long id = 6778L;
        IdGenerator gen = new IdGenerator(id);
        long val = gen.getNextId();
        assertEquals(id, val);

        val = gen.getNextId();
        assertEquals(id + 1, val);
    }
}
