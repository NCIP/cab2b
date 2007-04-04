/**
 * <p>Title: AdvancedSearchSessionBean class>
 * <p>Description:	AdvancedSearchSessionBean is a stateless session bean for the advanced search.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.cab2b.server.ejb;
  
import java.io.Serializable;
import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import edu.wustl.cab2b.common.advancedSearch.AdvancedSearchBusinessInterface;
import edu.wustl.cab2b.common.beans.MatchedClass;
import edu.wustl.cab2b.common.entityCache.EntityCacheGetterInterface;
import edu.wustl.cab2b.common.entityCache.EntityCacheHome;
import edu.wustl.cab2b.common.exception.CheckedException;
import edu.wustl.cab2b.server.advancedsearch.AdvancedSearch;
import edu.wustl.cab2b.server.advancedsearch.IAdvancedSearch;
import edu.wustl.common.util.global.ApplicationProperties;

/**
 * AdvancedSearchSessionBean is a stateless session bean for the advanced search.
 * @author gautam_shetty
 */
public class AdvancedSearchSessionBean extends AbstractStatelessSessionBean
        implements
            AdvancedSearchBusinessInterface,
            Serializable
{
    
    private static final long serialVersionUID = 1234567890L;
    
    private SessionContext sessionContext;
    
    private IAdvancedSearch advancedSearch;
    /**
     * Searches the array of search strings on the search targets specified.
     * @param searchTarget The search targets to search on.
     * @param searchString The search strings.
     * @param basedOn The based on fields.
     * @return The Matched class object containing all the entities found in the search.
     * @throws RemoteException
     */
    public MatchedClass search(int[] searchTarget, String[] searchString,
            int basedOn) throws RemoteException
    {
        MatchedClass result = null;
        
        try
        {
            result = advancedSearch.search(searchTarget, searchString, basedOn);
        }
        catch (CheckedException checkExp)
        {
            throw new RemoteException(checkExp.getMessage(), checkExp);
        }
        
        return result;
    }
    
    /**
     * (non-Javadoc)
     * @see javax.ejb.SessionBean#setSessionContext(javax.ejb.SessionContext)
     */
    public void setSessionContext(SessionContext sessionContext)
            throws EJBException, RemoteException
    {
        this.sessionContext = sessionContext;
        try
        {
            InitialContext ctx = new InitialContext();
            Object objref = ctx.lookup("local/EntityCache");
            EntityCacheHome entityCacheHome = (EntityCacheHome) PortableRemoteObject
                    .narrow(objref, EntityCacheHome.class);
            EntityCacheGetterInterface entityCacheBean = (EntityCacheGetterInterface)entityCacheHome.create();
            advancedSearch = new AdvancedSearch(entityCacheBean.getEntityCache());
        }
        catch (NamingException namingException)
        {
            namingException.printStackTrace();
            throw new RemoteException(namingException.getMessage(),
                    namingException);
        }
        catch (CreateException createException)
        {
            createException.printStackTrace();
            throw new RemoteException(createException.getMessage(),
                    createException);
        }
    }
}