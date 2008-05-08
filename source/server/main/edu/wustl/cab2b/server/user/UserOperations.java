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
 * @author hrishikesh_rajpathak
 *
 */
public class UserOperations extends DefaultBizLogic {

    /**
     * This method returns user from database with given user name
     * 
     * @param name
     * @return
     * @throws RemoteException
     */
    public User getUserByName(String name) throws RemoteException {
        return getUser("userName", name);
    }

    /**
     * Thsi method returns the user with administrative rights
     * 
     * @return
     * @throws RemoteException
     */
    public User getAdmin() throws RemoteException {
        // return getUser("isAdmin", null);
        return getUser("userName", "Admin");
    }

    /**
     * Method to fetch user. Called by getUserByName and getAdmin
     * 
     * @param column
     * @param value
     * @return
     * @throws RemoteException
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
            throw new RemoteException(e.getMessage());
        }

        User user = null;
        if (userList != null && !userList.isEmpty()) {
            user = userList.get(0);
            try {
                postProcessUser(user);
            } catch (DynamicExtensionsSystemException e) {
                throw new RemoteException(e.getMessage());
            } catch (DynamicExtensionsApplicationException e) {
                throw new RemoteException(e.getMessage());
            }
        } else {
            throw new RemoteException("Couldn't find user " + value);
        }
        return user;
    }

    /**
     * Post processing user to get real EntityGroup object from the names present in ServiceURL object
     * 
     * @param user
     * @throws DynamicExtensionsSystemException
     * @throws DynamicExtensionsApplicationException
     */
    private void postProcessUser(User user) throws DynamicExtensionsSystemException,
            DynamicExtensionsApplicationException {
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
     * Saves the given user as a new user in databse. Before saving it checks for user with duplicate name
     * 
     * @param user
     * @throws RemoteException
     */
    public void insertUser(User user) throws RemoteException {
        if (getUserByName(user.getUserName()) != null) {
            throw new RemoteException("Duplicate user. Please enter different user name");
        } else {
            try {
                insert(user, Constants.HIBERNATE_DAO);
            } catch (UserNotAuthorizedException e) {
                throw new RemoteException(e.getMessage());
            } catch (BizLogicException e) {
                throw new RemoteException(e.getMessage());
            }
        }
    }

    /**
     * Updates information about user in database. This does not create new user.
     * 
     * @param user
     * @throws RemoteException
     */
    public void updateUser(User user) throws RemoteException {
        try {
            update(user, Constants.HIBERNATE_DAO);
        } catch (UserNotAuthorizedException e) {
            throw new RemoteException(e.getMessage());
        } catch (BizLogicException e) {
            throw new RemoteException(e.getMessage());
        }
    }

    /**
     * Returns a map of EntityGroup name vs. its set of uls when for a specific user. 
     * If user hdoes not have any specific url configured, then it takes it from admin 
     * 
     * @param user
     * @return
     * @throws DynamicExtensionsSystemException
     * @throws DynamicExtensionsApplicationException
     * @throws RemoteException
     */
    public Map<String, List<String>> getServiceURLsForUser(UserInterface user)
            throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException, RemoteException {

        Collection<EntityGroupInterface> allEntityGroups = EntityManager.getInstance().getAllEntitiyGroups();

        Map<String, List<String>> entityGroupByUrls = new HashMap<String, List<String>>();
        Collection<ServiceURLInterface> userServiceCollection = user.getServiceURLCollection();

        for (ServiceURLInterface url : userServiceCollection) {
            String longName = url.getEntityGroupInterface().getLongName();
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
     * @param finalMap
     * @param absentEntityGroups
     * @return
     * @throws RemoteException
     */
    private Map<String, List<String>> getAdminURLs(Map<String, List<String>> finalMap,
                                                   Collection<String> absentEntityGroups) throws RemoteException {

        User admin = getAdmin();
        Collection<ServiceURLInterface> adminServices = admin.getServiceURLCollection();

        for (ServiceURLInterface url : adminServices) {
            String egName = url.getEntityGroupInterface().getLongName();
            if (absentEntityGroups.contains(egName)) {
                if (finalMap.containsKey(egName)) {
                    (finalMap.get(egName)).add(url.getUrlLocation());
                } else {
                    ArrayList<String> list = new ArrayList<String>();
                    list.add(url.getUrlLocation());
                    finalMap.put(egName, list);
                }
            } else {
                continue;
            }

        }
        return finalMap;
    }

    /**
     * Validates user on the basis of username, password and the idP that it points to.
     * 
     * @param userName
     * @param password
     * @param idP
     * @return
     * @throws RemoteException 
     */
    public GlobusCredential validateUser(String userName, String password, String dorianUrl)
            throws RemoteException {
        AuthenticationClient authClient = null;
        SAMLAssertion saml = null;
        Credential cred = createCredentials(userName, password);
        try {
            authClient = new AuthenticationClient(dorianUrl, cred);
        } catch (MalformedURIException e) {
            e.printStackTrace();
            return null;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
        try {
            saml = authClient.authenticate();
        } catch (InvalidCredentialFault e) {
            e.printStackTrace();
            return null;
        } catch (InsufficientAttributeFault e) {
            e.printStackTrace();
            return null;
        } catch (AuthenticationProviderFault e) {
            e.printStackTrace();
            return null;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
        GlobusCredential proxy = null;
        try {
            proxy = getGlobusCredentials(dorianUrl, saml);
        } catch (MalformedURIException e) {
            e.printStackTrace();
            return null;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
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
     * Sets globus credentials with proxy certificate of 12 hours (maximum possible) lifetime
     * 
     * @param idP
     * @param saml
     * @throws RemoteException 
     * @throws MalformedURIException 
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
