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
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2b.server.util.ServerProperties;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.dbManager.DBUtil;
import edu.wustl.common.util.global.Constants;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.gridca.common.KeyUtil;

/**
 * User operations like saving user, retrieving user, validating user are
 * handled by this class
 * It also sync the globus certificate of server .
 * 
 * @author hrishikesh_rajpathak
 * @author lalit_chand
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
        List<UserInterface> userList = null;
        String query = new StringBuilder().append(
                                                  "Select {User.*} from cab2b_user User where name COLLATE latin1_bin='").append(
                                                                                                                                 value).append(
                                                                                                                                               "'").toString();
        try {
            SQLQuery sqlQuery = DBUtil.currentSession().createSQLQuery(query);
            userList = sqlQuery.addEntity("User", edu.wustl.cab2b.common.user.User.class).list();
            DBUtil.closeSession();
        } catch (HibernateException hbe) {
            logger.error(hbe.getMessage(), hbe);
            throw new RuntimeException("Error occurred while fetching User", ErrorCodeConstants.UR_0003);
        }
        UserInterface user = null;
        if (userList != null && !userList.isEmpty()) {
            user = userList.get(0);
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
        return getUserByName("Admin");
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
        if (userServiceCollection == null) {
            userServiceCollection = new ArrayList<ServiceURLInterface>();
        }
        for (ServiceURLInterface url : userServiceCollection) {
            String entityName = url.getEntityGroupName();
            if (entityGroupByUrls.containsKey(entityName)) {
                (entityGroupByUrls.get(entityName)).add(url.getUrlLocation());
            } else {
                ArrayList<String> list = new ArrayList<String>();
                list.add(url.getUrlLocation());
                entityGroupByUrls.put(entityName, list);
            }
        }

        Collection<EntityGroupInterface> allEntityGroups = EntityCache.getInstance().getEntityGroups();
        Collection<String> absentEntityGroups = new ArrayList<String>();
        for (EntityGroupInterface entityGroupInterface : allEntityGroups) {
            String name = Utility.createModelName(entityGroupInterface.getLongName(),
                                                  entityGroupInterface.getVersion());
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
     * @param gridType
     * @return
     */
    private static GlobusCredential getGlobusCredentialCreated(String gridType) {
        GlobusCredential credential = null;
        if ("Production".compareTo(gridType) == 0) {
            if (productionCredential == null || productionCredential.getTimeLeft() < 3600000) {
                productionCredential = createGlobusCredential(gridType);
            } else {
                credential = productionCredential;
            }
        } else if ("Training".compareTo(gridType) == 0) {
            if (trainingCredential == null || trainingCredential.getTimeLeft() < 3600000) {
                trainingCredential = createGlobusCredential(gridType);
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
     * @param gridType
     * @return GlobusCredential 
     * @throws GeneralSecurityException
     * @throws IOException
     * @throws Exception
     */
    public static GlobusCredential getGlobusCredential(String serializedCredRef, String gridType)
            throws GeneralSecurityException, IOException, Exception {
        GlobusCredential delegatedCredential = null;
        if (serializedCredRef != null && gridType != null) {
            GlobusCredential credential = getGlobusCredentialCreated(gridType);

            /*
             * Create and Instance of the delegate credential client, specifying the DelegatedCredentialReference and 
             * the credential of the delegatee. The DelegatedCredentialReference specifies which credential to obtain.
             * The delegatee's credential is required to authenticate with the CDS such that the CDS may determining 
             * if the the delegatee has been granted access to the credential in which they wish to obtain.
             */
            logger.debug("getting serialized ref ...");

            DelegatedCredentialReference dref = getDelegatedCredentialReference(serializedCredRef);

            logger.debug("getting deserialized client ");

            DelegatedCredentialUserClient client = new DelegatedCredentialUserClient(dref, credential);

            //The get credential method obtains a signed delegated credential from the CDS.
            logger.debug("getting client reference ...");

            delegatedCredential = client.getDelegatedCredential();

        }

        //Set the delegated credential as the default, the delegatee is now logged in as the delegator.
        logger.debug("Retrieved Client credential");

        return delegatedCredential;
    }

    /**
     * 
     * @param gridType
     * @return GlobusCredential for server 
     */
    private static synchronized GlobusCredential createGlobusCredential(String gridType) {
        logger.debug("Generating GlobusCredential for server");

        String userHome = System.getProperty("user.home");
        String certFileName = userHome + ServerProperties.getGridCert(gridType);
        String keyFileName = userHome + ServerProperties.getGridKey(gridType);

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

    /**
     * 
     * @param dref
     * @param gridType
     * @return It returns grid user name 
     * @throws GeneralSecurityException
     * @throws IOException
     * @throws Exception
     */
    public String getCredentialUserName(String dref, String gridType) throws GeneralSecurityException, IOException,
            Exception {
        //TODO This is very wrong. Just to get the user gridId, CDS is called to get the user's credential.
        GlobusCredential credential = getGlobusCredential(dref, gridType);

        String userName = null;
        if (credential == null) {
            userName = "Anonymous";
        } else {
            userName = credential.getIdentity();
        }
        return userName;
    }

}
