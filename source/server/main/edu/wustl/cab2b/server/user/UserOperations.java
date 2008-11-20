package edu.wustl.cab2b.server.user;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.cagrid.gaards.cds.client.DelegatedCredentialUserClient;
import org.cagrid.gaards.cds.delegated.stubs.types.DelegatedCredentialReference;
import org.cagrid.gaards.websso.client.filter.CaGridWebSSODelegationLookupFilter;
import org.globus.gsi.CertUtil;
import org.globus.gsi.GlobusCredential;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2b.server.util.ServerProperties;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.Constants;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.security.ProxyUtil;
import gov.nih.nci.cagrid.gridca.common.KeyUtil;

/**
 * User operations like saving user, retrieving user, validating user are
 * handled by this class
 * 
 * @author hrishikesh_rajpathak
 * 
 */
public class UserOperations extends DefaultBizLogic {
    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(UserOperations.class);

    private static GlobusCredential productionCredential;

    private static GlobusCredential trainingCredential;

    /**
     * This method returns user from database with given user name
     * @param name user name
     * @return User
     */
    public UserInterface getUserByName(String value) {
        UserInterface user = getUser("userName", value);

        // TODO This is a temporary check added to solve case insensitive hibernate query problem.
        // This should be removed and and proper case sensitive query should be implemented
        if (user != null && (!user.getUserName().equals(value))) {
            user = null;
        }

        return user;
    }

    /**
     * This method returns user from database with given user name
     * @param name user name
     * @return User
     */
    public UserInterface getUserById(String value) {
        return getUser("userId", value);
    }

    /**
     * This method returns the user with administrative rights
     * @return Administrator 
     */
    public UserInterface getAdmin() {
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
    private UserInterface getUser(String column, String value) {
        List<UserInterface> userList = null;
        try {
            /*
             * if (value == null) { userList = (List<User>)
             * retrieve(User.class.getName(), column, "true"); } else { userList =
             * (List<User>) retrieve(User.class.getName(), column, value); }
             */
            userList = (List<UserInterface>) retrieve(UserInterface.class.getName(), column, value);
        } catch (DAOException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Error occurred while fetching User", ErrorCodeConstants.UR_0003);
        }

        UserInterface user = null;
        if (userList != null && !userList.isEmpty()) {
            user = userList.get(0);
        }
        return user;
    }

    /**
     * Saves the given user as a new user in database. Before saving it checks
     * for user with duplicate name
     * 
     * @param user user to insert
     */
    public UserInterface insertUser(UserInterface user) {
        if (getUserByName(user.getUserName()) != null) {
            throw new RuntimeException("User already exists", ErrorCodeConstants.UR_0002);
        } else {
            try {
                insert(user, Constants.HIBERNATE_DAO);
            } catch (UserNotAuthorizedException e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException("Error while inserting user in database", ErrorCodeConstants.UR_0004);
            } catch (BizLogicException e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException("Error while inserting user in database", ErrorCodeConstants.UR_0004);
            }
        }
        return user;
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
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Error while updating user information in database ",
                    ErrorCodeConstants.UR_0005);
        } catch (BizLogicException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Error while updating user information in database ",
                    ErrorCodeConstants.UR_0005);
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

        Collection<EntityGroupInterface> allEntityGroups = EntityCache.getInstance().getEntityGroups();
        Collection<String> absentEntityGroups = new ArrayList<String>();
        for (EntityGroupInterface entityGroupInterface : allEntityGroups) {
            String name = entityGroupInterface.getLongName();
            if (!entityGroupByUrls.containsKey(name)) {
                absentEntityGroups.add(name);
            }
        }

        if (!absentEntityGroups.isEmpty()) {
            entityGroupByUrls = getAdminURLs(entityGroupByUrls, absentEntityGroups);
        }
        return entityGroupByUrls;
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
        UserInterface admin = getAdmin();
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
     * 
     * @param idP
     * @return
     */
    private static GlobusCredential getGlobusCredentialCreated(String idP) {
        GlobusCredential credential = null;
        if ("Production".compareTo(idP) == 0) {
            if (productionCredential == null || productionCredential.getTimeLeft() < 3600000) {
                productionCredential = createGlobusCredential(idP);
            } else {
                credential = productionCredential;
            }
        } else if ("Training".compareTo(idP) == 0) {
            if (trainingCredential == null || trainingCredential.getTimeLeft() < 3600000) {
                trainingCredential = createGlobusCredential(idP);
            } else {
                credential = trainingCredential;
            }
        }
        return credential;
    }

    /**
     * It retrieves the client GlobusCredential from the delegated reference 
     * 
     * @param dref
     * @param serializedCredRef
     * @param idP
     * @return GlobusCredential 
     * @throws GeneralSecurityException
     * @throws IOException
     * @throws Exception
     */
    public static GlobusCredential getGlobusCredential(String serializedCredRef, String idP)
            throws GeneralSecurityException, IOException, Exception {
        GlobusCredential credential = getGlobusCredentialCreated(idP);

        /*Create and Instance of the delegate credential client, specifying the DelegatedCredentialReference and the credential of the delegatee.
          The DelegatedCredentialReference specifies which credential to obtain. The delegatee's credential is required to authenticate with 
          the CDS such that the CDS may determining if the the delegatee has been granted access to the credential in which they wish to obtain.*/
        DelegatedCredentialReference dref = getDelegatedCredentialReference(serializedCredRef);
        DelegatedCredentialUserClient client = new DelegatedCredentialUserClient(dref, credential);

        //The get credential method obtains a signed delegated credential from the CDS.
        GlobusCredential delegatedCredential = client.getDelegatedCredential();

        //Set the delegated credential as the default, the delegatee is now logged in as the delegator.
        ProxyUtil.saveProxyAsDefault(delegatedCredential);
        logger.debug("Retrieved Client credential");

        return delegatedCredential;
    }

    /**
     * 
     * @param idP
     * @return
     */
    private static synchronized GlobusCredential createGlobusCredential(String idP) {
        logger.debug("Generating GlobusCredential for server");

        String userHome = System.getProperty("user.home");
        String certFileName = userHome + ServerProperties.getGridCert(idP);
        String keyFileName = userHome + ServerProperties.getGridKey(idP);

        GlobusCredential credential = null;
        X509Certificate cert = null;
        PrivateKey key = null;
        try {
            cert = CertUtil.loadCertificate(certFileName);
            key = KeyUtil.loadPrivateKey(new File(keyFileName), null);
            credential = new GlobusCredential(key, new X509Certificate[] { cert });
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Certificate or key file not found", e);
        } catch (GeneralSecurityException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Incorrect certificates found", e);
        }

        return credential;
    }

    /**
     * It deserialized the client serialized credential back to DelegatedCredentialReference 
     * @param serializedCredRef
     * @return DelegatedCredentialReference
     */
    private static DelegatedCredentialReference getDelegatedCredentialReference(String serializedCredRef) {
        DelegatedCredentialReference delegatedCredentialReference = null;
        try {
            delegatedCredentialReference = (DelegatedCredentialReference) Utils.deserializeObject(
                                                                                                  new StringReader(
                                                                                                          serializedCredRef),
                                                                                                  DelegatedCredentialReference.class,
                                                                                                  CaGridWebSSODelegationLookupFilter.class.getClassLoader().getResourceAsStream(
                                                                                                                                                                                "cdsclient-config.wsdd"));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Unable to deserialize the Delegation Reference", e);
        }

        return delegatedCredentialReference;
    }

}
