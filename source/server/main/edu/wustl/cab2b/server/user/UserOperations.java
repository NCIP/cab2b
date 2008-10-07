package edu.wustl.cab2b.server.user;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.axis.types.URI.MalformedURIException;
import org.apache.log4j.Logger;
import org.globus.gsi.GlobusCredential;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.RuntimeException;
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
import gov.nih.nci.cagrid.dorian.client.IFSUserClient;
import gov.nih.nci.cagrid.dorian.ifs.bean.ProxyLifetime;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

/**
 * User operations like saving user, retrieving user, validating user are
 * handled by this class
 * 
 * @author hrishikesh_rajpathak
 * 
 */
public class UserOperations extends DefaultBizLogic {
    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(UserOperations.class);

    /**
     * This method returns user from database with given user name
     * @param name user name
     * @return User
     */
    public User getUserByName(String name) {
        return getUser("userName", name);
    }

    /**
     * This method returns the user with administrative rights
     * @return Administrator 
     */
    public User getAdmin() {
        return getUser("userName", "Admin");
    }

    /**
     * Method to fetch user. Called by getUserByName and getAdmin. If user not
     * found, it returns null
     * 
     * @param column column name in database
     * @param value value for the column
     * @return User 
     */
    @SuppressWarnings("unchecked")
    private User getUser(String column, String value) {
        List<User> userList = null;
        try {
            /*
             * if (value == null) { userList = (List<User>)
             * retrieve(User.class.getName(), column, "true"); } else { userList =
             * (List<User>) retrieve(User.class.getName(), column, value); }
             */
            userList = (List<User>) retrieve(User.class.getName(), column, value);
        } catch (DAOException e) {
            logger.error(e.getStackTrace());
            return null;
        }

        User user = null;
        if (userList != null && !userList.isEmpty()) {
            user = userList.get(0);
            // TODO This is a temporary check added to solve case insensitive
            // hibernate query problem.
            // This should be removed and and proper case sensitive query should
            // be implemented
            if (!user.getUserName().equals(value)) {
                return null;
            }
            try {
                postProcessUser(user);
            } catch (Exception e) {
                logger.error(e.getStackTrace());
                return null;
            }
        }
        return user;
    }

    /**
     * Post processing user to get real EntityGroup object from the names
     * present in ServiceURL object
     * @param user User
     */
    private void postProcessUser(User user) {
//        Collection<EntityGroupInterface> entityGroups = EntityCache.getInstance().getEntityGroups();

        Collection<ServiceURLInterface> serviceCollection = user.getServiceURLCollection();
//        for (ServiceURLInterface serviceURL : serviceCollection) {
//            String entityGroupName = ((ServiceURL) serviceURL).getEntityGroupName();
//            for (EntityGroupInterface entityGroup : entityGroups) {
//               // if (entityGroupName.equals(entityGroup.getLongName())) {
//               //     serviceURL.setEntityGroupInterface(entityGroup);
//                    break;
//                }
//            }
//        }
    }

    /**
     * Saves the given user as a new user in database. Before saving it checks
     * for user with duplicate name
     * 
     * @param user user to insert
     */
    public void insertUser(UserInterface user) {
        if (getUserByName(user.getUserName()) != null) {
            throw new RuntimeException("User already exists", ErrorCodeConstants.UR_0002);
        } else {
            try {
                insert(user, Constants.HIBERNATE_DAO);
            } catch (UserNotAuthorizedException e) {
                logger.error(e.getStackTrace());
                throw new RuntimeException("Error while inserting user in database", ErrorCodeConstants.UR_0004);
            } catch (BizLogicException e) {
                logger.error(e.getStackTrace());
                throw new RuntimeException("Error while inserting user in database", ErrorCodeConstants.UR_0004);
            }
        }
    }

    /**
     * Updates information about user in database. This does not create new  user.
     * 
     * @param user User to update
     */
    public void updateUser(UserInterface user) {
        try {
            update(user, Constants.HIBERNATE_DAO);
        } catch (UserNotAuthorizedException e) {
            logger.error(e.getStackTrace());
            throw new RuntimeException("Error while updating user information in database ", ErrorCodeConstants.UR_0005);
        } catch (BizLogicException e) {
            logger.error(e.getStackTrace());
            throw new RuntimeException("Error while updating user information in database ", ErrorCodeConstants.UR_0005);
        }
    }

    /**
     * Returns a map of EntityGroup name versus set of links for a specific
     * user. If user does not have any specific link configured, then it takes
     * it from administrator
     * 
     * @param user User
     * @return Returns a map of EntityGroup name versus set of links
     */
    public Map<String, List<String>> getServiceURLsForUser(UserInterface user) {

        Collection<EntityGroupInterface> allEntityGroups = EntityCache.getInstance().getEntityGroups();

        Map<String, List<String>> entityGroupByUrls = new HashMap<String, List<String>>();
        Collection<ServiceURLInterface> userServiceCollection = user.getServiceURLCollection();

        for (ServiceURLInterface url : userServiceCollection) {
            String longName = url.getEntityGroupName();
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
     * Called when current user does not have links for any entity-group.
     * @param egGroupToUrlListMap
     *            is a map of entity group to its links
     * @param absentEntityGroups
     * @return
     */
    private Map<String, List<String>> getAdminURLs(Map<String, List<String>> egGroupToUrlListMap,
                                                   Collection<String> absentEntityGroups) {

        User admin = getAdmin();
        Collection<ServiceURLInterface> adminServices = admin.getServiceURLCollection();

        for (ServiceURLInterface url : adminServices) {
            String egName = url.getEntityGroupName();
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
     * Validates user on the basis of user name, password and the idP that it
     * points to.
     * 
     * @param userName USer name
     * @param password Password
     * @param idP identity provider
     */
    public GlobusCredential validateUser(String userName, String password, String dorianUrl) {
        AuthenticationClient authClient = null;
        SAMLAssertion saml = null;
        Credential cred = createCredentials(userName, password);
        try {
            authClient = new AuthenticationClient(dorianUrl, cred);
        } catch (IOException e) {
            logger.error(e.getStackTrace());
            throw new RuntimeException("Please recheck identity provider url", ErrorCodeConstants.UR_0006);
        }
        try {
            saml = authClient.authenticate();
        } catch (RemoteException e) {
            logger.error(e.getStackTrace());
            throw new RuntimeException("Invalid user name or password", ErrorCodeConstants.UR_0007);
        }
        GlobusCredential proxy = null;
        try {
            proxy = getGlobusCredentials(dorianUrl, saml);
        } catch (IOException e) {
            logger.error(e.getStackTrace());
            throw new RuntimeException("Please recheck dorian url", ErrorCodeConstants.UR_0008);
        }
        return proxy;
    }

    /**
     * Generates credential object from given user name and password.
     * 
     * @param userName user name
     * @param password password
     * @return Credential
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
    private GlobusCredential getGlobusCredentials(String dorianUrl, SAMLAssertion saml) throws MalformedURIException,
            RemoteException {
        ProxyLifetime lifetime = new ProxyLifetime();
        lifetime.setHours(12);
        lifetime.setMinutes(0);
        lifetime.setSeconds(0);
        int delegationLifetime = 0;
        IFSUserClient dorian = new IFSUserClient(dorianUrl);
        return dorian.createProxy(saml, lifetime, delegationLifetime);
    }
}
