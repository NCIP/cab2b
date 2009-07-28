package edu.wustl.cab2b.admin.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;

/**
 * @author Chandrakant Talele
 */
public class IndexServiceInputProviderTest extends TestCase {
    static List<String> names = new ArrayList<String>();
    static {
        names.add("Test1");
        names.add("Test2");
    }

    public void testServiceNamesByEntityGroups() throws DynamicExtensionsSystemException,
            DynamicExtensionsApplicationException {
        IndexServiceInputProvider p = new MockIndexServiceInputProvider();
        assertEquals(names, p.serviceNamesByEntityGroups());
    }
}

class MockIndexServiceInputProvider extends IndexServiceInputProvider {
    Collection<EntityGroupInterface> getEntityGroups() {
        int size = IndexServiceInputProviderTest.names.size();
        List<EntityGroupInterface> list = new ArrayList<EntityGroupInterface>(size);
        DomainObjectFactory f = DomainObjectFactory.getInstance();

        for (int i = 0; i < size; i++) {
            EntityGroupInterface eg = f.createEntityGroup();
            eg.setName(IndexServiceInputProviderTest.names.get(i));
            list.add(eg);
        }
        return list;
    }
}