package edu.wustl.cab2b.server.path;

import junit.framework.TestCase;

/**
 * @author Chandrakant Talele
 */
public class PathRecordTest extends TestCase {
    PathRecord pathRecord = null;

    @Override
    protected void setUp() throws Exception {
        pathRecord = new PathRecord(111L, 222L, "333_444_555", 666L);
    }

    public void testGetAssociationSequence() {
        Long[] result = pathRecord.getAssociationSequence();
        assertEquals(3, result.length);
        assertEquals(333L, result[0].longValue());
        assertEquals(444L, result[1].longValue());
        assertEquals(555L, result[2].longValue());
    }
    public void testGetters() {
        assertEquals(111L,pathRecord.getPathId());
        assertEquals(222L,pathRecord.getFirstEntityId());
        assertEquals(666L,pathRecord.getLastEntityId());
    }
    @Override
    protected void tearDown() throws Exception {

    }
}
