/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */
package edu.wustl.cab2bwebapp.bizlogic.executequery;

import junit.framework.TestCase;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;

/**
 * JUnitTest cases for AttributeFilter class.
 * @author deepak_shingan
 *
 */
public class AttributeFilterTest extends TestCase {

    /**
     * Test method for <code>isVisible()</code> method,
     * When attribute name does not present in properties file for given entity.
     */
    public void testIsVisible_ifAttributeNameDoesNotPresent() {
        EntityInterface entity = DomainObjectFactory.getInstance().createEntity();
        entity.setName("edu.wustl.catissuecore.domain.Address");
        AttributeInterface attribute = DomainObjectFactory.getInstance().createStringAttribute();
        attribute.setName("abc");
        attribute.setId(1L);
        entity.addAttribute(attribute);

        AttributeFilter filter = new AttributeFilter();
        assertFalse("Invalid attribute is passed. Method shouold return false.", filter.isVisible(attribute));
    }

    /**
     * Test method for <code>isVisible()</code> method,
     * When entity name does not present in properties file for given attribute.
     */
    public void testIsVisible_ifEntityNameDoesNotPresent() {
        EntityInterface entity = DomainObjectFactory.getInstance().createEntity();
        entity.setName("XYZ");
        AttributeInterface attribute = DomainObjectFactory.getInstance().createStringAttribute();
        attribute.setName("id");
        attribute.setId(1L);
        entity.addAttribute(attribute);

        AttributeFilter filter = new AttributeFilter();
        assertTrue("Entity name does not present in file, make all attributes visible and return true.",
                   filter.isVisible(attribute));
    }

    /**
     * Test method for <code>isVisible()</code> method.
     * 
     */
    public void testIsVisible() {
        EntityInterface entity = DomainObjectFactory.getInstance().createEntity();
        entity.setName("edu.wustl.catissuecore.domain.Address");
        AttributeInterface attribute = DomainObjectFactory.getInstance().createStringAttribute();
        attribute.setName("country");
        attribute.setId(1L);
        entity.addAttribute(attribute);

        AttributeFilter filter = new AttributeFilter();
        assertTrue("Enitity name and attribute is present in file. Method shouold return true.",
                   filter.isVisible(attribute));
    }
}
