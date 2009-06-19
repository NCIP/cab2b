/**
 *
 */
package edu.wustl.cab2bwebapp.util;

import java.util.Set;

import junit.framework.TestCase;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;

/**
 * @author chetan_patil
 *
 */
public class UtilityTest extends TestCase {

    public void testGetEntityGroupsForModelGroups() {
        String[] modelGroupNames = new String[] { "Microarray Data" };
        Set<EntityGroupInterface> entityGroups = Utility.getEntityGroupsForModelGroups(modelGroupNames);
        if (!entityGroups.isEmpty()) {
            assertEquals("caArray_v2", entityGroups.iterator().next().getName());
        }
    }

}
