package edu.wustl.cab2b.client.ui.mainframe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.axis.types.URI.MalformedURIException;
import org.apache.log4j.Logger;
import org.cagrid.gaards.cds.client.ClientConstants;
import org.cagrid.gaards.cds.client.DelegationUserClient;
import org.cagrid.gaards.cds.common.DelegationPolicy;
import org.cagrid.gaards.cds.common.Utils;
import org.cagrid.gaards.cds.delegated.stubs.types.DelegatedCredentialReference;
import org.cagrid.gaards.cds.stubs.types.CDSInternalFault;
import org.cagrid.gaards.cds.stubs.types.DelegationFault;
import org.cagrid.gaards.cds.stubs.types.PermissionDeniedFault;
import org.globus.gsi.GlobusCredential;

import edu.wustl.cab2b.client.ui.util.ClientPropertyLoader;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.RuntimeException;
import gov.nih.nci.cagrid.authentication.bean.BasicAuthenticationCredential;
import gov.nih.nci.cagrid.authentication.bean.Credential;
import gov.nih.nci.cagrid.authentication.client.AuthenticationClient;
import gov.nih.nci.cagrid.authentication.stubs.types.AuthenticationProviderFault;
import gov.nih.nci.cagrid.authentication.stubs.types.InsufficientAttributeFault;
import gov.nih.nci.cagrid.authentication.stubs.types.InvalidCredentialFault;
import gov.nih.nci.cagrid.dorian.client.IFSUserClient;
import gov.nih.nci.cagrid.dorian.common.DorianFault;
import gov.nih.nci.cagrid.dorian.ifs.bean.ProxyLifetime;
import gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault;
import gov.nih.nci.cagrid.dorian.stubs.types.InvalidAssertionFault;
import gov.nih.nci.cagrid.dorian.stubs.types.InvalidProxyFault;
import gov.nih.nci.cagrid.dorian.stubs.types.UserPolicyFault;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;
import gov.nih.nci.cagrid.syncgts.bean.SyncDescription;

/**
 * This class validates user and gets GlobusCredential from Dorian.
 * It delegates credential to CDS and gets a Delegated reference which is passed to Server .  
 * information
 * 
 * @author hrishikesh_rajpathak
 * @author lalit_chand
 */
public class UserValidator {
    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(UserValidator.class);

    private String userName;

    private static String idP;

    private static String serializedDCR;

    public UserValidator(final String userName, final String idP) {
        this.userName = userName;
        UserValidator.idP = idP;
    }

    /**
     * @return
     */
    public static String getIdP() {
        return idP;
    }

    /**
     * This method converts the delegatedCredentialReference in string form. 
     * @return String
     */
    public static String getSerializedDCR() {
        return UserValidator.serializedDCR;
    }

    /**
     * Validates user on the basis of user name, password and the idP that it
     * points to.
     * 
     * @param userName
     * @param password
     * @param idP
     * @throws RemoteException
     */
    public void validateUser(final String password) {
        logger.debug("Validating the user on grid...");
        generateGlobusCertificate(idP);

        String dorianUrl = ClientPropertyLoader.getDorianUrl(idP);
        Credential credential = createCredentials(userName, password);

        AuthenticationClient authenticationClient = getAuthenticationClient(dorianUrl, credential);
        SAMLAssertion saml = getSAMLAssertion(authenticationClient);

        GlobusCredential proxy = getGlobusCredentials(dorianUrl, saml);

        DelegatedCredentialReference dcr = getDelegatedCredentialReference(proxy, idP);
        serializeDelegatedCredentialReference(dcr);

        logger.debug("Credential delegated sucessfully");
    }

    private AuthenticationClient getAuthenticationClient(String dorianUrl, Credential credential) {
        AuthenticationClient authenticationClient = null;
        try {
            logger.debug("Getting authentication client...");
            authenticationClient = new AuthenticationClient(dorianUrl, credential);
        } catch (MalformedURIException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Please check the authentication service URL", ErrorCodeConstants.UR_0006);
        } catch (RemoteException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }

        return authenticationClient;
    }

    private SAMLAssertion getSAMLAssertion(AuthenticationClient authenticationClient) {
        SAMLAssertion saml = null;
        try {
            logger.debug("Getting SAMLAssertion...");
            saml = authenticationClient.authenticate();
        } catch (InvalidCredentialFault e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Invalid user name or password", ErrorCodeConstants.UR_0007);
        } catch (InsufficientAttributeFault e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("User name or password is missing", ErrorCodeConstants.UR_0008);
        } catch (AuthenticationProviderFault e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Error occurred during authenticating the user", ErrorCodeConstants.UR_0009);
        } catch (RemoteException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }

        return saml;
    }

    /**
     * Generates credential object from given user name and password.
     * 
     * @param userName user name
     * @param password password
     * @return Credential
     */
    private Credential createCredentials(String userName, String password) {
        logger.debug("Creating credentials...");

        BasicAuthenticationCredential basicCredentials = new BasicAuthenticationCredential();
        basicCredentials.setUserId(userName);
        basicCredentials.setPassword(password);

        Credential credential = new Credential();
        credential.setBasicAuthenticationCredential(basicCredentials);

        return credential;
    }

    /**
     * This method sets globus credentials with proxy certificate of 12 hours (maximum possible) lifetime
     * 
     * @param dorianUrl
     * @param saml
     * @return
     * @throws RemoteException
     */
    private GlobusCredential getGlobusCredentials(String dorianUrl, SAMLAssertion saml) {
        ProxyLifetime lifetime = new ProxyLifetime();
        lifetime.setHours(12);
        lifetime.setMinutes(0);
        lifetime.setSeconds(0);

        final int delegationLifetime = 4;
        GlobusCredential proxy = null;
        try {
            logger.debug("Getting globus credential...");
            IFSUserClient dorian = new IFSUserClient(dorianUrl);
            proxy = dorian.createProxy(saml, lifetime, delegationLifetime);
        } catch (MalformedURIException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Please check the dorian URL", ErrorCodeConstants.CDS_010);
        } catch (DorianFault e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Error occurred at Dorian while obtaining GlobusCredential",
                    ErrorCodeConstants.CDS_011);
        } catch (DorianInternalFault e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Error occurred at Dorian while obtaining GlobusCredential",
                    ErrorCodeConstants.CDS_011);
        } catch (InvalidAssertionFault e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(
                    "Invalid SAMLAssertion. Please check the Dorian URL and user's credentials.",
                    ErrorCodeConstants.CDS_012);
        } catch (InvalidProxyFault e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Error occurred due to invalid proxy.", ErrorCodeConstants.CDS_013);
        } catch (UserPolicyFault e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Incorrect user policy set for the proxy.", ErrorCodeConstants.CDS_014);
        } catch (gov.nih.nci.cagrid.dorian.stubs.types.PermissionDeniedFault e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("You have insufficient permissions. Please contact Dorian Administrator.",
                    ErrorCodeConstants.CDS_015);
        } catch (RemoteException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }

        return proxy;
    }

    /**
     * 
     * It returns the DelegatedCredentialReference from CDS .
     * @param credential
     * @return DelegatedCredentialReference
     * @throws RemoteException
     * @throws Exception
     */
    private DelegatedCredentialReference getDelegatedCredentialReference(GlobusCredential credential, String idP) {
        String cdsURL = ClientPropertyLoader.getCDSUrl(idP);

        org.cagrid.gaards.cds.common.ProxyLifetime delegationLifetime = new org.cagrid.gaards.cds.common.ProxyLifetime();
        delegationLifetime.setHours(4);
        delegationLifetime.setMinutes(0);
        delegationLifetime.setSeconds(0);

        org.cagrid.gaards.cds.common.ProxyLifetime issuedCredentialLifetime = new org.cagrid.gaards.cds.common.ProxyLifetime();
        issuedCredentialLifetime.setHours(1);
        issuedCredentialLifetime.setMinutes(0);
        issuedCredentialLifetime.setSeconds(0);

        //Specifies the path length of the credential being delegate the minumum is 1.
        final int delegationPathLength = 1;

        /*
         * Specifies the path length of the credentials issued to allowed parties. 
         * A path length of 0 means that the requesting party cannot further delegate the credential.
         */
        final int issuedCredentialPathLength = 0;

        //Specifies the key length of the delegated credential
        final int keySize = ClientConstants.DEFAULT_KEY_SIZE;

        /*
         * The policy stating which parties will be allowed to obtain a delegated credential.
         * The CDS will only issue credentials to parties listed in this policy.
         */
        final String delegateeName = ClientPropertyLoader.getDelegetee(idP);
        logger.debug("Delegatee Name :" + delegateeName);

        List<String> parties = new ArrayList<String>(1);
        parties.add(delegateeName);
        DelegationPolicy policy = Utils.createIdentityDelegationPolicy(parties);

        //Delegates the credential and returns a reference which can later be used by allowed parties to obtain a credential.
        DelegatedCredentialReference reference = null;
        try {
            logger.debug("Delegating Credential to " + cdsURL);

            DelegationUserClient client = new DelegationUserClient(cdsURL, credential);
            reference = client.delegateCredential(delegationLifetime, delegationPathLength, policy,
                                                  issuedCredentialLifetime, issuedCredentialPathLength, keySize);

        } catch (CDSInternalFault e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(
                    "An unknown internal error ocurred at CDS while delegating the credentials",
                    ErrorCodeConstants.CDS_006);
        } catch (DelegationFault e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Error ocurred while delegating the credentials",
                    ErrorCodeConstants.CDS_007);
        } catch (PermissionDeniedFault e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("The server doesn't have permission to acess the client's credentials.",
                    ErrorCodeConstants.CDS_008);
        } catch (MalformedURIException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Incorrect CDS URL. Please check the CDS URL in conf/client.properties",
                    ErrorCodeConstants.CDS_009);
        } catch (RemoteException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
        return reference;
    }

    private void serializeDelegatedCredentialReference(DelegatedCredentialReference dcr) {
        StringWriter stringWriter = new StringWriter();
        try {
            logger.debug("Serializing the delegated credential reference...");
            gov.nih.nci.cagrid.common.Utils.serializeObject(
                                                            dcr,
                                                            new QName(ClientPropertyLoader.getCDSDelegatedUrl(),
                                                                    "DelegatedCredentialReference"),
                                                            stringWriter,
                                                            UserValidator.class.getClassLoader().getResourceAsStream(
                                                                                                                     "cdsclient-config.wsdd"));
            serializedDCR = stringWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException("Unable to serialize the delegated credentials.",
                    ErrorCodeConstants.CDS_005);
        }
    }

    private void copyCACertificates(File src, File dst) {
        byte[] buf = new byte[1024];
        int len = 0;

        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(src);
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(src.getAbsolutePath() + " file not found", ErrorCodeConstants.CDS_016);
        }

        try {
            out = new FileOutputStream(dst);
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(dst.getAbsolutePath() + " file not found", ErrorCodeConstants.CDS_016);
        }

        try {
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }

            in.close();
            out.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Unable to copy CA certificates to [user.home]/.globus",
                    ErrorCodeConstants.CDS_003);
        }

    }

    /**
     * This method generates the globus certificate in user.home folder
     * @param idP
     */
    private void generateGlobusCertificate(String idP) {
        String gridCertDir = ClientPropertyLoader.getGridCertForGlobus(idP);
        File dir = new File(gridCertDir);
        if (dir.exists()) {
            File[] list = dir.listFiles();
            if (list != null || list.length != 0) {
                for (int i = 0; i < list.length; i++) {
                    if (list[i].isFile() && !(list[i].getName().contains(".xml"))) {
                        File dest = new File(gov.nih.nci.cagrid.common.Utils.getTrustedCerificatesDirectory()
                                + File.separator + list[i].getName());
                        copyCACertificates(list[i], dest);
                    }
                }
            }
        } else {
            logger.error("The source directory, " + dir.getAbsolutePath() + " does not exist.");
        }

        logger.debug("Getting sync-descriptor.xml file for " + idP);
        String pathToSyncDescription = ClientPropertyLoader.getSyncDesFile(idP);

        try {
            logger.debug("Synchronizing with GTS service");
            SyncDescription description = (SyncDescription) gov.nih.nci.cagrid.common.Utils.deserializeDocument(
                                                                                                                pathToSyncDescription,
                                                                                                                SyncDescription.class);
            logger.debug("Successfully syncronized with GTS service. Globus certificates generated.");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Error occurred while generating globus certificates",
                    ErrorCodeConstants.CDS_004);
        }
    }

}
