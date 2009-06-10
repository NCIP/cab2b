package edu.wustl.cab2b.common.util;

import java.util.ArrayList;
import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2b.common.cache.AbstractEntityCache;

/**
 * @author gaurav_mehta
 *
 */
public class ServiceURLUtility {

    /**
     * This method fetches all the entity groups created as part of metadata
     * creation and returns them.
     * TODO:For efficiency, we can chop off entity collection from entity group
     * object. It is not incorporated currently.
     * @return All the metadata entity groups
     */
    public Collection<EntityGroupInterface> getMetadataEntityGroups(AbstractEntityCache entityCache) {
        Collection<EntityGroupInterface> entityGroups = entityCache.getEntityGroups();

        Collection<EntityGroupInterface> filteredGroups = new ArrayList<EntityGroupInterface>();
        for (EntityGroupInterface entityGroup : entityGroups) {
            if (Constants.CATEGORY_ENTITY_GROUP_NAME.equalsIgnoreCase(entityGroup.getLongName())
                    || Constants.DATALIST_ENTITY_GROUP_NAME.equalsIgnoreCase(entityGroup.getLongName())) {
                continue;
            }
            filteredGroups.add(entityGroup);
        }
        return filteredGroups;
    }
}
