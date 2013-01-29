/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.common.dao;

import java.util.Collection;

/** This interface defines methods which are specific to Hibernate operations .*/

public interface HibernateDAO extends AbstractDAO
{
	public Object loadCleanObj(String sourceObjectName, Long id) throws Exception;
	
	public void addAuditEventLogs(Collection auditEventDetailsCollection);

}
