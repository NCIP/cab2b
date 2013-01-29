/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.admin.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.cab2b.server.cache.EntityCache;

/**
 * This class provides data needed to call index service
 * 
 * @author Atul Jawale
 */
public class IndexServiceInputProvider {

	Collection<EntityGroupInterface> getEntityGroups()
			throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException {
		return EntityCache.getInstance().getEntityGroups();
	}

	/**
	 * Returns names of entity groups
	 * 
	 * @return names of entity groups
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public List<String> serviceNamesByEntityGroups()
			throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException {
		final List<String> serviceNames = new ArrayList<String>();
		for (EntityGroupInterface entityGroup : getEntityGroups()) {
			serviceNames.add(entityGroup.getName());
		}
		return serviceNames;
	}
}
