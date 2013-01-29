/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.path;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.common.querysuite.metadata.path.CuratedPath;

/**
 * @author Chandrakant Talele
 */
public class CuratedPathTest extends TestCase  {
      public void testGetStringRepresentationEmptySet() {
        Set<EntityInterface> set = new HashSet<EntityInterface>();
        String str = CuratedPath.getStringRepresentation(set);
        assertEquals("", str);
    }

    public void testGetStringRepresentation() {
        Set<EntityInterface> set = new HashSet<EntityInterface>();
        DomainObjectFactory fact = DomainObjectFactory.getInstance();
        for (long i = 0; i < 3; i++) {
            EntityInterface e1 = fact.createEntity();
            e1.setId(i);
            set.add(e1);
        }
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(0);
        strBuilder.append("_");
        strBuilder.append(1);
        strBuilder.append("_");
        strBuilder.append(2);
        String str = CuratedPath.getStringRepresentation(set);
        assertEquals(strBuilder.toString(), str);
    }

    public void testGetStringRepresentationUnsorted() {
        Set<EntityInterface> set = new HashSet<EntityInterface>();
        DomainObjectFactory fact = DomainObjectFactory.getInstance();

        EntityInterface e0 = fact.createEntity();
        e0.setId(0L);
        set.add(e0);
        EntityInterface e2 = fact.createEntity();
        e2.setId(2L);
        set.add(e2);
        EntityInterface e1 = fact.createEntity();
        e1.setId(1L);
        set.add(e1);

        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(0);
        strBuilder.append("_");
        strBuilder.append(1);
        strBuilder.append("_");
        strBuilder.append(2);
        String str = CuratedPath.getStringRepresentation(set);
        assertEquals(strBuilder.toString(), str);
    }

}
