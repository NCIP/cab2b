/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import edu.wustl.cab2b.server.initializer.ApplicationInitializer;

/**
 * Abstract class which represents a Statless Session Bean.
 * Each Statless Session Bean must extend this class.
 * It avoids need of each bean to implement methods from {@link javax.ejb.SessionBean} class.
 * @author Chandrakant Talele
 */
public abstract class AbstractStatelessSessionBean implements SessionBean {
    SessionContext sessionContext;

    /**
     * @throws CreateException
     */
    public void ejbCreate() throws CreateException {

    }

    /**
     * @see javax.ejb.SessionBean#ejbActivate()
     */
    public void ejbActivate() throws EJBException, RemoteException {
        throw new IllegalStateException("ejbActivate() must not be called on Stateless Bean");
    }

    /**
     * @see javax.ejb.SessionBean#ejbPassivate()
     */
    public void ejbPassivate() throws EJBException, RemoteException {
        throw new IllegalStateException("ejbPassivate() must not be called on Stateless Bean");
    }

    /**
     * @see javax.ejb.SessionBean#ejbRemove()
     */
    public void ejbRemove() throws EJBException, RemoteException {
    }

    /**
     * (non-Javadoc)
     * @see javax.ejb.SessionBean#setSessionContext(javax.ejb.SessionContext)
     */
    public void setSessionContext(SessionContext sessionContext) throws EJBException, RemoteException {
        this.sessionContext = sessionContext;
        ApplicationInitializer.getInstance();
    }

}