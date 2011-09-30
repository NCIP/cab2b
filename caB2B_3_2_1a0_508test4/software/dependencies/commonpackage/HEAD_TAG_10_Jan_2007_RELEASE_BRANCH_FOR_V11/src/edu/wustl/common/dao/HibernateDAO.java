package edu.wustl.common.dao;

import java.util.Collection;

/** This interface defines methods which are specific to Hibernate operations .*/

public interface HibernateDAO extends AbstractDAO
{
	public Object loadCleanObj(String sourceObjectName, Long id) throws Exception;
	
	public void addAuditEventLogs(Collection auditEventDetailsCollection);

}
