package edu.wustl.cab2b.server.ejb.user;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.globus.gsi.GlobusCredential;

import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.cab2b.common.ejb.user.UserBusinessInterface;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.cache.DatalistCache;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2b.server.path.PathFinder;
import edu.wustl.cab2b.server.user.UserOperations;
import edu.wustl.cab2b.server.util.ConnectionUtil;
import edu.wustl.common.util.logger.Logger;

/**
 * This bean handles user based operations
 * 
 * @author hrishikesh_rajpathak
 * 
 */
@Stateless
@Remote(UserBusinessInterface.class)
@TransactionManagement(TransactionManagementType.BEAN)
public class UserBean implements UserBusinessInterface {

    private static final long serialVersionUID = 1L;
    private static final org.apache.log4j.Logger logger = Logger.getLogger(UserBean.class);
    public UserBean() {
        Logger.configure("caB2B.logger");
        EntityCache.getInstance();
        Connection connection = ConnectionUtil.getConnection();
        try {
            PathFinder.getInstance(connection);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectionUtil.close(connection);
        }
        DatalistCache.getInstance();

    }

    public void insertUser(UserInterface user) throws RemoteException {
        new UserOperations().insertUser(user);
    }

    public void updateUser(UserInterface user) throws RemoteException {
        new UserOperations().updateUser(user);
    }

    public Map<String, List<String>> getServiceUrlsForUser(UserInterface user)
            throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException, RemoteException {
        return new UserOperations().getServiceURLsForUser(user);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.wustl.cab2b.common.ejb.user.UserBusinessInterface#getUserByName(java.lang.String)
     *      If user not found, it returns null
     */
    public UserInterface getUserByName(String user) throws RemoteException {
        return new UserOperations().getUserByName(user);
    }

    public GlobusCredential validateUser(String userName, String password, String dorianUrl)
            throws RemoteException {

        return new UserOperations().validateUser(userName, password, dorianUrl);
    }

}
