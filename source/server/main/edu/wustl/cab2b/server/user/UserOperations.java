package edu.wustl.cab2b.server.user;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.axis.types.URI.MalformedURIException;
import org.globus.gsi.GlobusCredential;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.user.ServiceURL;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.common.user.User;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.cagrid.authentication.bean.BasicAuthenticationCredential;
import gov.nih.nci.cagrid.authentication.bean.Credential;
import gov.nih.nci.cagrid.authentication.client.AuthenticationClient;
import gov.nih.nci.cagrid.authentication.stubs.types.AuthenticationProviderFault;
import gov.nih.nci.cagrid.authentication.stubs.types.InsufficientAttributeFault;
import gov.nih.nci.cagrid.authentication.stubs.types.InvalidCredentialFault;
import gov.nih.nci.cagrid.dorian.client.IFSUserClient;
import gov.nih.nci.cagrid.dorian.ifs.bean.ProxyLifetime;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

/**
 * User operations like saving user, retrieveing user, validating user are
 * handled by this class
 * 
 * @author hrishikesh_rajpathak
 * 
 */
public class UserOperations extends DefaultBizLogic {

    /**
     * This method returns user from database with given user name
     * 
     * @param name
     *            user name
     * @return
     * @throws RemoteException
     *             in case of database retrieval
     */
    public User getUserByName(String name) throws RemoteException {
        return getUser("userName", name);
    }

    /**
     * Thsi method returns the user with administrative rights
     * 
     * @return
     * @throws RemoteException
     *             in case of database retrieval
     */
    public User getAdmin() throws RemoteException {
        // return getUser("isAdmin", null);
        return getUser("userName", "Admin");
    }

    /**
     * Method to fetch user. Called by getUserByName and getAdmin. If user not
     * found, it returns null
     * 
     * @param column-
     *            column name in database
     * @param value-
     *            value for the column
     * @return
     * @throws RemoteException-
     *             in case of database retrieval
     */
    private User getUser(String column, String value) throws RemoteException {
        List<User> userList = null;
        try {
            /*
             * if (value == null) { userList = (List<User>)
             * retrieve(User.class.getName(), column, "true"); } else { userList =
             * (List<User>) retrieve(User.class.getName(), column, value); }
             */
            userList = (List<User>) retrieve(User.class.getName(), column, value);
        } catch (DAOException e) {
            Logger.out.error(e.getStackTrace());
            throw new RuntimeException("Error while fetching user from database", ErrorCodeConstants.UR_0003);
        }

        User user = null;
        if (userList != null && !userList.isEmpty()) {
            user = userList.get(0);
            // TODO This is a temporary check added to solve case insensitive
            // hibernate query problem.
            // This shoul be removedand and proper case sensitive query should
            // be implemented
            if (!user.getUserName().equals(value)) {
                throw new RuntimeException("Please check the credentials again (User name is case sensitive)",
                        ErrorCodeConstants.UR_0009);
            }
            try {
                postProcessUser(user);
            } catch (Exception e) {
                Logger.out.error(e.getStackTrace());
                throw new RuntimeException("User fetched with incomplete data.", ErrorCodeConstants.UR_0001);
            }
        }
        return user;
    }

    /**
     * Post processing user to get real EntityGroup object from the names
     * present in ServiceURL object
     * 
     * @param user
     */
    private void postProcessUser(User user) {
        Collection<EntityGroupInterface> entityGroups = EntityCache.getInstance().getEntityGroups();

        Collection<ServiceURLInterface> serviceCollection = user.getServiceURLCollection();
        for (ServiceURLInterface serviceURL : serviceCollection) {
            String entityGroupName = ((ServiceURL) serviceURL).getEntityGroupName();
            for (EntityGroupInterface entityGroup : entityGroups) {
                if (entityGroupName.compareTo(entityGroup.getLongName()) == 0) {
                    serviceURL.setEntityGroupInterface(entityGroup);
                    break;
                }
            }
        }
    }

    /**
     * Saves the given user as a new user in databse. Before saving it checks
     * for user with duplicate name
     * 
     * @param user
     * @throws RemoteException -
     *             in case of failed database operation
     */
    public void insertUser(UserInterface user) throws RemoteException {
        if (getUserByName(user.getUserName()) != null) {
            throw new RuntimeException("User already exists", ErrorCodeConstants.UR_0002);
        } else {
            try {
                insert(user, Constants.HIBERNATE_DAO);
            } catch (UserNotAuthorizedException e) {
                Logger.out.error(e.getStackTrace());
                throw new RuntimeException("Error while inserting user in database", ErrorCodeConstants.UR_0004);
            } catch (BizLogicException e) {
                Logger.out.error(e.getStackTrace());
                throw new RuntimeException("Error while inserting user in database", ErrorCodeConstants.UR_0004);
            }
        }
    }

    /**
     * Updates information about user in database. This does not create new
     * user.
     * 
     * @param user
     * @throws RemoteException -
     *             in case of failed database operation
     */
    public void updateUser(UserInterface user) throws RemoteException {
        try {
            update(user, Constants.HIBERNATE_DAO);
        } catch (UserNotAuthorizedException e) {
            Logger.out.error(e.getStackTrace());
            throw new RuntimeException("Error while updating user information in database ",
                    ErrorCodeConstants.UR_0005);
        } catch (BizLogicException e) {
            Logger.out.error(e.getStackTrace());
            throw new RuntimeException("Error while updating user information in database ",
                    ErrorCodeConstants.UR_0005);
        }
    }

    /**
     * Returns a map of EntityGroup name vs. its set of uls when for a specific
     * user. If user hdoes not have any specific url configured, then it takes
     * it from admin
     * 
     * @param user
     * @return
     * @throws DynamicExtensionsSystemException
     * @throws DynamicExtensionsApplicationException
     * @throws RemoteException -
     *             for all exceptions other than ones thrown by DE
     */
    public Map<String, List<String>> getServiceURLsForUser(UserInterface user)
            throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException, RemoteException {

        Collection<EntityGroupInterface> allEntityGroups = EntityManager.getInstance().getAllEntitiyGroups();

        Map<String, List<String>> entityGroupByUrls = new HashMap<String, List<String>>();
        Collection<ServiceURLInterface> userServiceCollection = user.getServiceURLCollection();

        for (ServiceURLInterface url : userServiceCollection) {
            String longName = url.getEntityGroups().getLongName();
            if (entityGroupByUrls.containsKey(longName)) {
                (entityGroupByUrls.get(longName)).add(url.getUrlLocation());
            } else {
                ArrayList<String> list = new ArrayList<String>();
                list.add(url.getUrlLocation());
                entityGroupByUrls.put(longName, list);
            }
        }

        Collection<String> absentEntityGroups = new ArrayList<String>();
        for (EntityGroupInterface entityGroupInterface : allEntityGroups) {
            String name = entityGroupInterface.getLongName();
            if (!entityGroupByUrls.containsKey(name)) {
                absentEntityGroups.add(name);
            }
        }
        if (absentEntityGroups.isEmpty()) {
            return entityGroupByUrls;
        } else {
            return getAdminURLs(entityGroupByUrls, absentEntityGroups);
        }
    }

    /**
     * Called when current user does not have urls for any entitygroup.
     * 
     * @param egGroupToUrlListMap
     *            is a map of entity group to its urls
     * @param absentEntityGroups
     * @return
     * @throws RemoteException
     *             in case of failed database operation
     */
    private Map<String, List<String>> getAdminURLs(Map<String, List<String>> egGroupToUrlListMap,
                                                   Collection<String> absentEntityGroups) throws RemoteException {

        User admin = getAdmin();
        Collection<ServiceURLInterface> adminServices = admin.getServiceURLCollection();

        for (ServiceURLInterface url : adminServices) {
            String egName = url.getEntityGroups().getLongName();
            if (absentEntityGroups.contains(egName)) {
                if (egGroupToUrlListMap.containsKey(egName)) {
                    (egGroupToUrlListMap.get(egName)).add(url.getUrlLocation());
                } else {
                    ArrayList<String> list = new ArrayList<String>();
                    list.add(url.getUrlLocation());
                    egGroupToUrlListMap.put(egName, list);
                }
            } else {
                continue;
            }
        }
        return egGroupToUrlListMap;
    }

    /**
     * Validates user on the basis of username, password and the idP that it
     * points to.
     * 
     * @param userName
     * @param password
     * @param idP -
     *            indentity provider
     * @return
     * @throws RemoteException
     *             in case of any reason that leads to invalid authentication.
     */
    public GlobusCredential validateUser(String userName, String password, String dorianUrl) {
        AuthenticationClient authClient = null;
        SAMLAssertion saml = null;
        Credential cred = createCredentials(userName, password);
        try {
            authClient = new AuthenticationClient(dorianUrl, cred);
        } catch (MalformedURIException e) {
            Logger.out.error(e.getStackTrace());
            throw new RuntimeException("Please recheck identity provider url", ErrorCodeConstants.UR_0006);
        } catch (RemoteException e) {
            Logger.out.error(e.getStackTrace());
            throw new RuntimeException("Please recheck identity provider url", ErrorCodeConstants.UR_0006);
        }
        try {
            saml = authClient.authenticate();
        } catch (InvalidCredentialFault e) {
            Logger.out.error(e.getStackTrace());
            throw new RuntimeException("Invalid user name or password", ErrorCodeConstants.UR_0007);
        } catch (InsufficientAttributeFault e) {
            Logger.out.error(e.getStackTrace());
            throw new RuntimeException("Invalid user name or password", ErrorCodeConstants.UR_0007);
        } catch (AuthenticationProviderFault e) {
            Logger.out.error(e.getStackTrace());
            throw new RuntimeException("Invalid user name or password", ErrorCodeConstants.UR_0007);
        } catch (RemoteException e) {
            Logger.out.error(e.getStackTrace());
            throw new RuntimeException("Invalid user name or password", ErrorCodeConstants.UR_0007);
        }
        GlobusCredential proxy = null;
        try {
            proxy = getGlobusCredentials(dorianUrl, saml);
        } catch (MalformedURIException e) {
            Logger.out.error(e.getStackTrace());
            throw new RuntimeException("Please recheck dorian url", ErrorCodeConstants.UR_0008);
        } catch (RemoteException e) {
            Logger.out.error(e.getStackTrace());

            throw new RuntimeException("Please recheck dorian url", ErrorCodeConstants.UR_0008);
        }
        return proxy;
    }

    /**
     * Generates Credential object from given username and password.
     * 
     * @param userName
     * @param password
     * @return
     */
    private Credential createCredentials(String userName, String password) {
        Credential credential = new Credential();
        BasicAuthenticationCredential basicCredentials = new BasicAuthenticationCredential();
        basicCredentials.setUserId(userName);
        basicCredentials.setPassword(password);
        credential.setBasicAuthenticationCredential(basicCredentials);
        return credential;
    }

    /**
     * Sets globus credentials with proxy certificate of 12 hours (maximum
     * possible) lifetime
     * 
     * @param idP
     * @param saml
     * @throws RemoteException -
     *             any other eason for failing in fetching credentials from
     *             dorian
     * @throws MalformedURIException
     *             if dorian url not properly defined
     */
    private GlobusCredential getGlobusCredentials(String dorianUrl, SAMLAssertion saml)
            throws MalformedURIException, RemoteException {
        ProxyLifetime lifetime = new ProxyLifetime();
        lifetime.setHours(12);
        lifetime.setMinutes(0);
        lifetime.setSeconds(0);
        int delegationLifetime = 0;
        IFSUserClient dorian = new IFSUserClient(dorianUrl);
        return dorian.createProxy(saml, lifetime, delegationLifetime);
    }
}
