/*
 * Created on Oct 4, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.common.security.dao;

import gov.nih.nci.logging.api.logger.hibernate.HibernateSessionFactoryHelper;
import gov.nih.nci.security.authorization.ObjectPrivilegeMap;
import gov.nih.nci.security.authorization.domainobjects.Privilege;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroup;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.dao.hibernate.ProtectionGroupProtectionElement;
import gov.nih.nci.security.exceptions.CSConfigurationException;
import gov.nih.nci.security.exceptions.CSException;
import gov.nih.nci.security.exceptions.CSObjectNotFoundException;
import gov.nih.nci.security.exceptions.CSTransactionException;
import gov.nih.nci.security.util.StringUtilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import org.apache.log4j.Logger;

/**
 * @author aarti_sharma
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class AuthorizationDAOImpl extends
		gov.nih.nci.security.dao.AuthorizationDAOImpl {

	static final Logger log = edu.wustl.common.util.logger.Logger.out;

	private SessionFactory sf = null;


	private String typeOfAccess = "MIXED";
	/**
	 * @param sf
	 * @param applicationContextName
	 * @throws CSConfigurationException 
	 */
	public AuthorizationDAOImpl(SessionFactory sf, String applicationContextName) throws CSConfigurationException {
		super(sf, applicationContextName);
		this.sf = sf;
		
	}

	public Collection getPrivilegeMap(String userName, Collection pEs)
			throws CSException {
		ArrayList result = new ArrayList();
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		boolean test = false;
		Session s = null;

		Connection cn = null;

		if (StringUtilities.isBlank(userName)) {
			throw new CSException("userName can't be null!");
		}
		if (pEs == null) {
			throw new CSException(
					"protection elements collection can't be null!");
		}
		if (pEs.size() == 0) {
			return result;
		}

		try {

			s = sf.openSession();

			cn = s.connection();

			StringBuffer stbr = new StringBuffer();
			stbr.append("select distinct(p.privilege_name)");
			stbr.append(" from csm_protection_group pg,");
			stbr.append(" csm_protection_element pe,");
			stbr.append(" csm_pg_pe pgpe,");
			stbr.append(" csm_user_group_role_pg ugrpg,");
			stbr.append(" csm_user u,");
			stbr.append(" csm_group g,");
			stbr.append(" csm_user_group ug,");
			stbr.append(" csm_role_privilege rp,");
			stbr.append(" csm_privilege p ");
			stbr
					.append(" where pgpe.protection_group_id = pg.protection_group_id");
			stbr
					.append(" and pgpe.protection_element_id = pe.protection_element_id");
			stbr.append(" and pe.object_id= ?");
			
			stbr.append(" and pe.attribute=?");
			stbr
					.append(" and pg.protection_group_id = ugrpg.protection_group_id ");
			stbr.append(" and (( ugrpg.group_id = g.group_id");
			stbr.append(" and ug.group_id= g.group_id");
			stbr.append("       and ug.user_id = u.user_id)");
			stbr.append("       or ");
			stbr.append("     (ugrpg.user_id = u.user_id))");
			stbr.append(" and u.login_name=?");
			stbr.append(" and ugrpg.role_id = rp.role_id ");
			stbr.append(" and rp.privilege_id = p.privilege_id");
			
			StringBuffer stbr2 = new StringBuffer();
			stbr2.append("select distinct(p.privilege_name)");
			stbr2.append(" from csm_protection_group pg,");
			stbr2.append(" csm_protection_element pe,");
			stbr2.append(" csm_pg_pe pgpe,");
			stbr2.append(" csm_user_group_role_pg ugrpg,");
			stbr2.append(" csm_user u,");
			stbr2.append(" csm_group g,");
			stbr2.append(" csm_user_group ug,");
			stbr2.append(" csm_role_privilege rp,");
			stbr2.append(" csm_privilege p ");
			stbr2
					.append(" where pgpe.protection_group_id = pg.protection_group_id");
			stbr2
					.append(" and pgpe.protection_element_id = pe.protection_element_id");
			stbr2.append(" and pe.object_id= ?");
			
			stbr2.append(" and pe.attribute IS NULL");
			stbr2
					.append(" and pg.protection_group_id = ugrpg.protection_group_id ");
			stbr2.append(" and (( ugrpg.group_id = g.group_id");
			stbr2.append(" and ug.group_id= g.group_id");
			stbr2.append("       and ug.user_id = u.user_id)");
			stbr2.append("       or ");
			stbr2.append("     (ugrpg.user_id = u.user_id))");
			stbr2.append(" and u.login_name=?");
			stbr2.append(" and ugrpg.role_id = rp.role_id ");
			stbr2.append(" and rp.privilege_id = p.privilege_id");

			String sql = stbr.toString();
			log.debug("SQL:"+sql);
			pstmt = cn.prepareStatement(sql);
			
			String sql2 = stbr2.toString();
			pstmt2 = cn.prepareStatement(sql2);

			Iterator it = pEs.iterator();
			while (it.hasNext()) {
				ProtectionElement pe = (ProtectionElement) it.next();
				ArrayList privs = new ArrayList();
				if (pe.getObjectId() != null) {
					
					if (pe.getAttribute() != null) {
						pstmt.setString(1, pe.getObjectId());
						pstmt.setString(2, pe.getAttribute());
						pstmt.setString(3, userName);
						rs = pstmt.executeQuery();
					} else {
						pstmt2.setString(1, pe.getObjectId());
						pstmt2.setString(2, userName);
						rs = pstmt2.executeQuery();
					}
					
				}

				

				while (rs.next()) {
					String priv = rs.getString(1);
					Privilege p = new Privilege();
					p.setName(priv);
					privs.add(p);
				}
				rs.close();
				ObjectPrivilegeMap opm = new ObjectPrivilegeMap(pe, privs);
				result.add(opm);
			}

			pstmt.close();

		} catch (Exception ex) {
			if (log.isDebugEnabled())
				log.debug("Failed to get privileges for " + userName + "|"
						+ ex.getMessage());
			throw new CSException("Failed to get privileges for " + userName
					+ "|" + ex.getMessage(), ex);
		} finally {
			try {

				s.close();
				rs.close();
				pstmt.close();
			} catch (Exception ex2) {
				if (log.isDebugEnabled())
					log
							.debug("Authorization|||getPrivilegeMap|Failure|Error in Closing Session |"
									+ ex2.getMessage());
			}
		}

		return result;
	}
	
	

	//changes to load the object and then delete it
	//else it throws exception
	public void removeProtectionElementsFromProtectionGroup(
			String protectionGroupId, String[] protectionElementIds)
			throws CSTransactionException {
		Session s = null;
		Transaction t = null;

		try {
			s = sf.openSession();
			t = s.beginTransaction();

			ProtectionGroup protectionGroup = (ProtectionGroup) this
					.getObjectByPrimaryKey(s, ProtectionGroup.class, new Long(
							protectionGroupId));

			for (int i = 0; i < protectionElementIds.length; i++) {
				ProtectionGroupProtectionElement intersection = new ProtectionGroupProtectionElement();
				String query = "from gov.nih.nci.security.dao.hibernate.ProtectionGroupProtectionElement protectionGroupProtectionElement" +
				" where protectionGroupProtectionElement.protectionElement.protectionElementId="+protectionElementIds[i]+
				" and protectionGroupProtectionElement.protectionGroup.protectionGroupId="+protectionGroupId ;	
				Query queryObj =  s.createQuery(query);
				List list = queryObj.list();
				if(list!=null && list.size()>0)
					this.removeObject(list.get(0));
				
			}
			
			
			
			t.commit();
			

		} catch (Exception ex) {
			log.error(ex);
			try {
				t.rollback();
			} catch (Exception ex3) {
				if (log.isDebugEnabled())
					log
							.debug("Authorization|||removeProtectionElementsFromProtectionGroup|Failure|Error in Rolling Back Transaction|"
									+ ex3.getMessage());
			}
			log
					.debug("Authorization|||removeProtectionElementsFromProtectionGroup|Failure|Error Occured in deassigning Protection Elements "
							+ StringUtilities
									.stringArrayToString(protectionElementIds)
							+ " to Protection Group"
							+ protectionGroupId
							+ "|"
							+ ex.getMessage());
			throw new CSTransactionException(
					"An error occured in deassigning Protection Elements from Protection Group\n"
							+ ex.getMessage(), ex);
		} finally {
			try {
				s.close();
			} catch (Exception ex2) {
				if (log.isDebugEnabled())
					log
							.debug("Authorization|||removeProtectionElementsFromProtectionGroup|Failure|Error in Closing Session |"
									+ ex2.getMessage());
			}
		}
		log
				.debug("Authorization|||removeProtectionElementsFromProtectionGroup|Success|Success in deassigning Protection Elements "
						+ StringUtilities
								.stringArrayToString(protectionElementIds)
						+ " to Protection Group" + protectionGroupId + "|");
	}
	
	
    public Set getGroups(String userId) throws CSObjectNotFoundException {
        Session s = null;
        Set groups = new HashSet();
        try {
            s = HibernateSessionFactoryHelper.getAuditSession(sf);

            User user = (User) this.getObjectByPrimaryKey(s, User.class,
                    new Long(userId));
            groups = user.getGroups();
            List list = new ArrayList();
            Iterator toSortIterator = groups.iterator();
            while(toSortIterator.hasNext()){ list.add(toSortIterator.next()); }
            Collections.sort(list);
            groups.clear();
            groups.addAll(list);
            
            log.debug("The result size:" + groups.size());

        } catch (Exception ex) {
            log.error(ex);
            if (log.isDebugEnabled())
                log
                        .debug("Authorization|||getGroups|Failure|Error in obtaining Groups for User Id "
                                + userId + "|" + ex.getMessage());
            throw new CSObjectNotFoundException(
                    "An error occurred while obtaining Associated Groups for the User\n"
                            + ex.getMessage(), ex);
        } finally {
            try {
                s.close();
            } catch (Exception ex2) {
                if (log.isDebugEnabled())
                    log
                            .debug("Authorization|||getGroups|Failure|Error in Closing Session |"
                                    + ex2.getMessage());
            }
        }
        if (log.isDebugEnabled())
            log
                    .debug("Authorization|||getGroups|Success|Successful in obtaining Groups for User Id "
                            + userId + "|");
        return groups;

    }
	
	
	private Object getObjectByPrimaryKey(Session s, Class objectType,
			Long primaryKey) throws HibernateException,
			CSObjectNotFoundException {

		if (primaryKey == null) {
			throw new CSObjectNotFoundException("The primary key can't be null");
		}
		Object obj = s.load(objectType, primaryKey);

		if (obj == null) {
			log
					.debug("Authorization|||getObjectByPrimaryKey|Failure|Not found object of type "
							+ objectType.getName() + "|");
			throw new CSObjectNotFoundException(objectType.getName()
					+ " not found");
		}
		log
				.debug("Authorization|||getObjectByPrimaryKey|Success|Success in retrieving object of type "
						+ objectType.getName() + "|");
		return obj;
	}

}