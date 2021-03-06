/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */
package edu.wustl.common.util;

import java.util.Comparator;

import edu.wustl.common.domain.AbstractDomainObject;

/**
 * This class compares the two domain objects  by their identifiers
 * @author chetan_patil
 *
 */
public class DomainBeanIdentifierComparator implements Comparator
{
	public int compare(Object object1, Object object2) 
	{
		AbstractDomainObject domainObject1 = (AbstractDomainObject)object1;
		AbstractDomainObject domainObject2 = (AbstractDomainObject)object2;
		
		Long value = domainObject1.getId() - domainObject2.getId();
		return value.intValue();
	}
}
