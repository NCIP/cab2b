/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.actionForm;

import edu.wustl.common.domain.AbstractDomainObject;

/**
 * This provides the interface / API to set values of domain object to form object 
 * @author sachin_lale
 *
 */
public interface IValueObject 
{
	
	public void setAllValues(AbstractDomainObject abstractDomain);

}
