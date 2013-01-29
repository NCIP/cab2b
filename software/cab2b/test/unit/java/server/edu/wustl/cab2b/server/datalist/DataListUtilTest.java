/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.datalist;

import junit.framework.TestCase;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.TaggedValueInterface;
import edu.wustl.cab2b.common.util.Constants;
import edu.wustl.cab2b.server.util.DynamicExtensionUtility;
import edu.wustl.cab2b.server.util.TestUtil;
import static edu.wustl.cab2b.common.util.DataListUtil.ORIGIN_ENTITY_ID_KEY;
import static edu.wustl.cab2b.common.util.DataListUtil.SOURCE_ENTITY_ID_KEY;
import static edu.wustl.cab2b.common.util.Constants.ENTITY_DISPLAY_NAME;

/**
 * @author Chandrakant Talele
 */
public class DataListUtilTest extends TestCase {
    public void testCreateNewEntity() {
        long id1 = 10;
        long id2 = 11;

        EntityGroupInterface datalistEntityGroup = TestUtil.getEntityGroup("datalistEntityGroup", id1);
        DataListUtil.setDataListEntityGroup(datalistEntityGroup);

        EntityGroupInterface eg = TestUtil.getEntityGroup("EntityGroup", id2);
        DynamicExtensionUtility.addTaggedValue(eg, Constants.CAB2B_ENTITY_GROUP, Constants.CAB2B_ENTITY_GROUP);

        EntityInterface oldEntity = TestUtil.getEntity("entityname", id1);
        oldEntity.addEntityGroupInterface(eg);

        EntityInterface newEntity = DataListUtil.createNewEntity(oldEntity);
        
        assertEquals(datalistEntityGroup, newEntity.getEntityGroupCollection().iterator().next());
        assertEquals(oldEntity.getName(), newEntity.getName());
        assertEquals(3, newEntity.getTaggedValueCollection().size());
        
        boolean[] tags = new boolean[3];
        for (TaggedValueInterface tag : newEntity.getTaggedValueCollection()) {
            if (tag.getKey().equals(ORIGIN_ENTITY_ID_KEY)) {
                tags[0] = true;
            }
            if (tag.getKey().equals(SOURCE_ENTITY_ID_KEY)) {
                tags[1] = true;
            }
            if (tag.getKey().equals(ENTITY_DISPLAY_NAME)) {
                tags[2] = true;
            }
        }
        assertTrue(tags[0]);
        assertTrue(tags[1]);
        assertTrue(tags[2]);
    }
}
