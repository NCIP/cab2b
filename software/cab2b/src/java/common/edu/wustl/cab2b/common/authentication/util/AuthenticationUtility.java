/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 *
 */
package edu.wustl.cab2b.common.authentication.util;

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
import org.cagrid.gaards.pki.KeyUtil;
import org.cagrid.websso.client.filter.CaGridWebSSODelegationLookupFilter;
import org.globus.gsi.CertUtil;
import org.globus.gsi.GlobusCredential;

import edu.wustl.cab2b.common.authentication.exception.AuthenticationException;
import gov.nih.nci.cagrid.common.Utils;

/**
 * @author chetan_patil
 *
 */
public class AuthenticationUtility {
    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(AuthenticationUtility.class);

    private static GlobusCredential globusCredential;

    /**
     *
     * @return
     */
    private static GlobusCredential getServersGlobusCredential() {
        if (globusCredential == null || globusCredential.getTimeLeft() < 3600000) {
            globusCredential = createGlobusCredential();
        }
        return globusCredential;
    }

    /**
     *
     * @return GlobusCredential for server
     */
    private static synchronized GlobusCredential createGlobusCredential() {
        logger.debug("Generating GlobusCredential for server");
        GlobusCredential credential = null;
        try {
            X509Certificate cert = CertUtil.loadCertificate(CagridPropertyLoader.getGridCert());
            PrivateKey key = KeyUtil.loadPrivateKey(new File(CagridPropertyLoader.getGridKey()), null);
            credential = new GlobusCredential(key, new X509Certificate[] { cert });
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new AuthenticationException("Certificate or key file not found. " + e.getMessage(), e);
        } catch (GeneralSecurityException e) {
            logger.error(e.getMessage(), e);
            throw new AuthenticationException("Incorrect certificates found. " + e.getMessage(), e);
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
            throw new AuthenticationException("Unable to deserialize the Delegation Reference. " + e.getMessage(), e);
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
     * @return
     */
    public static GlobusCredential getGlobusCredential(String serializedDCR) {
        GlobusCredential delegatedCredential = null;
        if (serializedDCR != null) {
            GlobusCredential credential = getServersGlobusCredential();

            logger.debug("Coverting serialized DelegatedCredentialReference...");
            DelegatedCredentialReference dReference = getDelegatedCredentialReference(serializedDCR);

            logger.debug("Creating DelegatedCredentialUser client...");
            DelegatedCredentialUserClient client = null;
            try {
                client = new DelegatedCredentialUserClient(dReference, credential);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw new AuthenticationException("Could not create DelegatedCredentialUserClient. " + e.getMessage(), e);
            }

            logger.debug("Getting client's GlobusCredential...");
            try {
                delegatedCredential = client.getDelegatedCredential();
            } catch (CDSInternalFault e) {
                logger.error(e.getMessage(), e);
                throw new AuthenticationException(
                        "An unknown internal error ocurred at CDS while getting the delegated credential. "
                                + e.getMessage(), e);
            } catch (DelegationFault e) {
                logger.error(e.getMessage(), e);
                throw new AuthenticationException("Error ocurred while getting the delegated credential. "
                        + e.getMessage(), e);
            } catch (PermissionDeniedFault e) {
                logger.error(e.getMessage(), e);
                throw new AuthenticationException(
                        "The server doesn't have permission to acess the client's credentials. " + e.getMessage(),
                        e);
            } catch (RemoteException e) {
                logger.error(e.getMessage(), e);
                throw new AuthenticationException("Unable to retreive the delegated credential from CDS. "
                        + e.getMessage(), e);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw new AuthenticationException(e.getMessage(), e);
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
     * @return User's grid identifier
     */
    public static String getUsersGridId(String serializedDCR) {
        //TODO This is very wrong. Just to get the user gridId, CDS is called to get the user's credential.
        GlobusCredential credential = getGlobusCredential(serializedDCR);

        String userName = null;
        if (credential == null) {
            userName = "Anonymous";
        } else {
            userName = credential.getIdentity();
        }

        return userName;
    }

}
