/**
 * 
 */
package edu.wustl.cab2b.server.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.rmi.RemoteException;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import org.apache.log4j.Logger;
import org.cagrid.gaards.cds.client.DelegatedCredentialUserClient;
import org.cagrid.gaards.cds.delegated.stubs.types.DelegatedCredentialReference;
import org.cagrid.gaards.cds.stubs.types.CDSInternalFault;
import org.cagrid.gaards.cds.stubs.types.DelegationFault;
import org.cagrid.gaards.cds.stubs.types.PermissionDeniedFault;
import org.cagrid.gaards.websso.client.filter.CaGridWebSSODelegationLookupFilter;
import org.globus.gsi.CertUtil;
import org.globus.gsi.GlobusCredential;

import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.user.UserOperations;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.gridca.common.KeyUtil;

/**
 * @author chetan_patil
 *
 */
public class UserUtility {
    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(UserUtility.class);

    private static GlobusCredential productionCredential;

    private static GlobusCredential trainingCredential;

    /**
     * 
     * @param gridType
     * @return
     */
    private static GlobusCredential getServersGlobusCredential(String gridType) {
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
            throw new RuntimeException("Certificate or key file not found. " + e.getMessage(), e);
        } catch (GeneralSecurityException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Incorrect certificates found. " + e.getMessage(), e);
        }

        return credential;
    }

    /**
     * This method de-serializes the client serialized DelegatedCredentialReference 
     * 
     * @param serializedDCR
     * @return DelegatedCredentialReference
     */
    private static DelegatedCredentialReference getDelegatedCredentialReference(String serializedDCR) {
        StringReader serialDCR = new StringReader(serializedDCR);
        ClassLoader classLoader = CaGridWebSSODelegationLookupFilter.class.getClassLoader();

        DelegatedCredentialReference dcr = null;
        try {
            InputStream wsddFile = classLoader.getResourceAsStream("cdsclient-config.wsdd");
            dcr = (DelegatedCredentialReference) Utils.deserializeObject(serialDCR,
                                                                         DelegatedCredentialReference.class,
                                                                         wsddFile);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Unable to deserialize the Delegation Reference. " + e.getMessage(), e);
        }

        return dcr;
    }

    /**
     * This method retrieves the client's GlobusCredential from CDS. 
     * 
     * Create and Instance of the delegate credential client, specifying the DelegatedCredentialReference and 
     * the credential of the delegatee. The DelegatedCredentialReference specifies which credential to obtain.
     * The delegatee's credential is required to authenticate with the CDS such that the CDS may determining 
     * if the the delegatee has been granted access to the credential in which they wish to obtain.
     * 
     * @param serializedDCR
     * @param gridType
     * @return
     */
    public static GlobusCredential getGlobusCredential(String serializedDCR, String gridType) {
        GlobusCredential delegatedCredential = null;
        if (serializedDCR != null && gridType != null) {
            GlobusCredential credential = getServersGlobusCredential(gridType);

            logger.debug("Coverting serialized DelegatedCredentialReference...");
            DelegatedCredentialReference dReference = getDelegatedCredentialReference(serializedDCR);

            logger.debug("Creating DelegatedCredentialUser client...");
            DelegatedCredentialUserClient client = null;
            try {
                client = new DelegatedCredentialUserClient(dReference, credential);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException("Could not create DelegatedCredentialUserClient. " + e.getMessage(), e);
            }

            logger.debug("Getting client's GlobusCredential...");
            try {
                delegatedCredential = client.getDelegatedCredential();
            } catch (CDSInternalFault e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException(
                        "An unknown internal error ocurred at CDS while getting the delegated credential. "
                                + e.getMessage(), e);
            } catch (DelegationFault e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException("Error ocurred while getting the delegated credential. "
                        + e.getMessage(), e);
            } catch (PermissionDeniedFault e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException(
                        "The server doesn't have permission to acess the client's credentials. " + e.getMessage(),
                        e);
            } catch (RemoteException e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException("Unable to retreive the delegated credential from CDS. "
                        + e.getMessage(), e);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException(e.getMessage(), e);
            }
        }

        logger.debug("Retrieved Client credential");
        return delegatedCredential;
    }

    /**
     * This method returns the User's grid identifier, given the serialized DelegatedCredentialReference and 
     * the grid type 
     * 
     * @param serializedDCR
     * @param gridType
     * @return User's grid identifier
     */
    public static String getUsersGridId(String serializedDCR, String gridType) {
        //TODO This is very wrong. Just to get the user gridId, CDS is called to get the user's credential.
        GlobusCredential credential = getGlobusCredential(serializedDCR, gridType);

        String userName = null;
        if (credential == null) {
            userName = "Anonymous";
        } else {
            userName = credential.getIdentity();
        }

        return userName;
    }

    /**
     * This method returns the database identifier of the user, given the serialized DelegatedCredentialReference and 
     * the grid type 
     * 
     * @param serializedDCR
     * @param gridType
     * @return user's database identifier
     */
    public static Long getLocalUserId(String serializedDCR, String gridType) {
        String usersGridId = getUsersGridId(serializedDCR, gridType);
        UserInterface user = new UserOperations().getUserByName(usersGridId);

        Long userId = null;
        if (user != null) {
            userId = user.getUserId();
        }
        return userId;
    }

}
