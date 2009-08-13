package edu.wustl.cab2b.server.queryengine.querystatus;

import java.util.Collection;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import edu.wustl.cab2b.common.queryengine.querystatus.QueryStatusImpl;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.common.hibernate.HibernateDatabaseOperations;
import edu.wustl.common.hibernate.HibernateUtil;
import edu.wustl.common.util.dbManager.HibernateUtility;

public class QueryURLStatusOperations {

    public void insertQueryStatus(QueryStatusImpl qs) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            HibernateDatabaseOperations<QueryStatusImpl> handler =
                    new HibernateDatabaseOperations<QueryStatusImpl>(session);
            handler.insertOrUpdate(qs);
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
    }

    public Collection<QueryStatusImpl> getQueryStatus(UserInterface user) {
        Collection<QueryStatusImpl> queryStatus = null;
        try {
            queryStatus = HibernateUtility.executeHQL("getQueryStatus");
        } catch (HibernateException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return queryStatus;
    }
}