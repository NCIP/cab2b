package edu.wustl.common.hibernate;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;

/**
 * Provides methods for insert/retrieve/delete/update of objects using
 * hibernate. <br>
 * <b>Transaction details: </b>A new transaction is begun when an instance of
 * this class is created; all operations on this instance use this transaction.
 * If <tt>autoCommit</tt> is <tt>true</tt> (the default), each
 * insert/delete/update operation is instantly committed. If <tt>autoCommit</tt>
 * is initialized as <tt>false</tt>, the caller has to invoke
 * {@link #commit()} explicitly for the commit to occur.
 * 
 * @author srinath_k
 * 
 * @param <T> The type of object to be inserted/deleted/retrieved/updated.
 */
public class HibernateDatabaseOperations<T> {
    private Session session;

    private Transaction transaction;

    private final boolean autoCommit;

    public HibernateDatabaseOperations(Session session, boolean autoCommit) {
        this.session = session;
        transaction = session.beginTransaction();
        this.autoCommit = autoCommit;
    }

    /**
     * autoCommit is set to <tt>true</tt>.
     */
    public HibernateDatabaseOperations(Session session) {
        this(session, true);
    }

    public void insert(T t) {
        getSession().save(t);
        checkCommit();
    }

    public void insertOrUpdate(T t) {
        getSession().saveOrUpdate(t);
        checkCommit();
    }
    public void update(T t) {
        getSession().update(t);
        checkCommit();
    }

    public void delete(T t) {
        getSession().delete(t);
        checkCommit();
    }

    public List<T> retrieve(String className) {
        return cast(newCriteria(className).list());
    }

    public List<T> retrieve(String className, String attributeName, Object value) {
        return cast(newCriteria(className).add(Expression.eq(attributeName, value)).list());
    }

    public T retrieveById(String className, Serializable id) {
        return cast(getSession().get(className, id));
    }

    public void commit() {
        getTransaction().commit();
        transaction = session.beginTransaction();
    }

    private Transaction getTransaction() {
        return transaction;
    }

    public Session getSession() {
        return session;
    }

    private void checkCommit() {
        if (autoCommit) {
            commit();
        }
    }

    private Criteria newCriteria(String className) {
        return getSession().createCriteria(className);
    }

    @SuppressWarnings("unchecked")
    private <E> List<E> cast(List list) {
        return (List<E>) list;
    }

    @SuppressWarnings("unchecked")
    private T cast(Object o) {
        return (T) o;
    }
}
