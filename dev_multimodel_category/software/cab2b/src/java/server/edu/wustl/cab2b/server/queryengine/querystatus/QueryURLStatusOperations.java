package edu.wustl.cab2b.server.queryengine.querystatus;

import java.util.Collection;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import edu.wustl.cab2b.common.queryengine.querystatus.QueryStatus;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.common.hibernate.HibernateDatabaseOperations;
import edu.wustl.common.hibernate.HibernateUtil;
import edu.wustl.common.util.dbManager.HibernateUtility;

/**
 * Class for saving/updating query status properties.  
 * @author  Chetan_pundhir
 *
 */
public class QueryURLStatusOperations {

    public void insertQueryStatus(QueryStatus qs) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            HibernateDatabaseOperations<QueryStatus> handler =
                    new HibernateDatabaseOperations<QueryStatus>(session);
            handler.insertOrUpdate(qs);
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
    }

    public Collection<QueryStatus> getQueryStatus(UserInterface user) {
        Collection<QueryStatus> queryStatus = null;
        try {
            queryStatus = HibernateUtility.executeHQL("getQueryStatus");
        } catch (HibernateException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return queryStatus;
    }
}