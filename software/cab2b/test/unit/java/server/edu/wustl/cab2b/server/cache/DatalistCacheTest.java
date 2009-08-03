package edu.wustl.cab2b.server.cache;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.server.util.TestUtil;
import junit.framework.TestCase;

/**
 * @author chandrakant_talele
 */
public class DatalistCacheTest extends TestCase {
    public void testGetInstance() {
        try {
            DatalistCache.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception in DatalistCache.getInstance()");
        }
    }

    public void testGetEntityWithId() {
        DatalistCache cache = DatalistCache.getInstance();
        EntityInterface entity = TestUtil.getEntity("SomeEntity", 201L);
        cache.addEntity(entity);
        EntityInterface actual = cache.getEntityWithId(entity.getId());
        assertEquals(entity.getId(), actual.getId());
    }
}
