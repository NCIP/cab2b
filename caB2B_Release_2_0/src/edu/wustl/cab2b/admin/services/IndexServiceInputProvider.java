package edu.wustl.cab2b.admin.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;

public class IndexServiceInputProvider {

    public IndexServiceInputProvider() {

    }

    private Collection<EntityGroupInterface> getEntityGroups() throws DynamicExtensionsSystemException,
            DynamicExtensionsApplicationException {
        final Collection<EntityGroupInterface> entityGroup = EntityManager.getInstance().getAllEntitiyGroups();
        return entityGroup;
    }

    public List<String> serviceNamesByEntityGroups() throws DynamicExtensionsSystemException,
            DynamicExtensionsApplicationException {
        final List<String> serviceNames = new ArrayList<String>();
        final Collection<EntityGroupInterface> entityGroup = getEntityGroups();
        for (EntityGroupInterface entityGroupInterface : entityGroup) {
            serviceNames.add(entityGroupInterface.getName());
        }
        return serviceNames;
    }

}
