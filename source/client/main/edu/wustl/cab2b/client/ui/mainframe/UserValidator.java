package edu.wustl.cab2b.client.ui.mainframe;

import java.io.File;
import java.io.FileInputStream;
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
import org.globus.gsi.GlobusCredential;

import edu.wustl.cab2b.client.ui.util.ClientPropertyLoader;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.RuntimeException;
import gov.nih.nci.cagrid.authentication.bean.BasicAuthenticationCredential;
import gov.nih.nci.cagrid.authentication.bean.Credential;
import gov.nih.nci.cagrid.authentication.client.AuthenticationClient;
import gov.nih.nci.cagrid.authentication.stubs.types.AuthenticationProviderFault;
import gov.nih.nci.cagrid.dorian.client.IFSUserClient;
import gov.nih.nci.cagrid.dorian.ifs.bean.ProxyLifetime;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;
import gov.nih.nci.cagrid.syncgts.bean.SyncDescription;
import gov.nih.nci.cagrid.syncgts.core.SyncGTS;

/**
 * This class validates user and gets GlobusCredential from Dorian.
 * It delegates credential to CDS and gets a Delegated reference which is passed to Server .  
 * information
 * 
 * @author hrishikesh_rajpathak
 * @author lalit_chand
 */
public class UserValidator {

    private static String userName;

    private static String idP;

    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(UserValidator.class);

    private static DelegatedCredentialReference delegatedCredentialReference;

    /**
     * Validates user on the basis of username, password and the idP that it
     * points to.
     * 
     * @param userName
     * @param password
     * @param idP
     * @throws RemoteException
     */
    public static void validateUser(String userName, String password, String idP) throws RemoteException {
        setUserName(userName);

        generateGlobusCertificate(idP);

        String dorianUrl = ClientPropertyLoader.getDorianUrl(idP);

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
        } catch (AuthenticationProviderFault apf) {
            logger.error(apf.getStackTrace());
            throw new RuntimeException(apf.getMessage());
        } catch (RemoteException e) {
            logger.error(e.getStackTrace());
            throw new RuntimeException("Invalid user name or password", ErrorCodeConstants.UR_0007);
        }
        GlobusCredential proxy = null;
        try {
            proxy = getGlobusCredentials(dorianUrl, saml);
            logger.debug("getting globus credential");

        } catch (IOException e) {
            logger.error(e.getStackTrace());
            throw new RuntimeException("Please recheck dorian url", ErrorCodeConstants.UR_0008);
        } catch (Exception e) {
            logger.error(e.getStackTrace());
        }
        try {
            // When the user gets authenticated successfully Then only its credential is delegated 
            UserValidator.delegatedCredentialReference = getDelegatedCredential(proxy, idP);
            logger.debug("Credential delegated sucessfully");
            UserValidator.setIdP(idP);
        } catch (Exception e) {
            logger.error(e.getStackTrace());
            throw new RuntimeException("Could Not Delegate Client Credtial to CDS", ErrorCodeConstants.CDS_001);
        }
        setDelegatedCredentialReference(delegatedCredentialReference);

        if (proxy == null) {
            throw new RemoteException("Proxy is null");
        }

    }

    /**
     * @return Returns the userName.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName
     * The userName to set.
     */
    public static void setUserName(String userName) {
        UserValidator.userName = userName;
    }

    /**
     * Generates credential object from given user name and password.
     * 
     * @param userName user name
     * @param password password
     * @return Credential
     */
    public static Credential createCredentials(String userName, String password) {
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
     *             any other reason for failing in fetching credentials from
     *             dorian
     * @throws MalformedURIException
     *             if dorian url not properly defined
     */
    private static GlobusCredential getGlobusCredentials(String dorianUrl, SAMLAssertion saml)
            throws MalformedURIException, RemoteException {
        ProxyLifetime lifetime = new ProxyLifetime();
        lifetime.setHours(12);
        lifetime.setMinutes(0);
        lifetime.setSeconds(0);
        int delegationLifetime = 4;
        IFSUserClient dorian = new IFSUserClient(dorianUrl);
        return dorian.createProxy(saml, lifetime, delegationLifetime);
    }

    /**
     * 
     * It returns the DelegatedCredentialReference from CDS .
     * @param credential
     * @return DelegatedCredentialReference
     * @throws RemoteException
     * @throws Exception
     */
    private static DelegatedCredentialReference getDelegatedCredential(GlobusCredential credential, String idP)
            throws RemoteException, Exception {
        String cdsURL = ClientPropertyLoader.getCDSUrl(idP);

        //Specifies the path length of the credential being delegate the minumum is 1.
        logger.debug("Delegating Credential to " + cdsURL);
        int delegationPathLength = 1;

        org.cagrid.gaards.cds.common.ProxyLifetime delegationLifetime = new org.cagrid.gaards.cds.common.ProxyLifetime();
        delegationLifetime.setHours(4);
        delegationLifetime.setMinutes(0);
        delegationLifetime.setSeconds(0);

        org.cagrid.gaards.cds.common.ProxyLifetime issuedCredentialLifetime = new org.cagrid.gaards.cds.common.ProxyLifetime();
        issuedCredentialLifetime.setHours(1);
        issuedCredentialLifetime.setMinutes(0);
        issuedCredentialLifetime.setSeconds(0);

        //Specifies the path length of the credentials issued to allowed parties. A path length of 0 means that 
        //the requesting party cannot further delegate the credential.

        int issuedCredentialPathLength = 0;

        //Specifies the key length of the delegated credential

        int keySize = ClientConstants.DEFAULT_KEY_SIZE;

        //The policy stating which parties will be allowed to obtain a delegated credential. The CDS will only 
        //issue credentials to parties listed in this policy.

        List<String> parties = new ArrayList<String>(1);

        String delegateeName = ClientPropertyLoader.getDelegetee(idP);
        logger.debug("Delegatee Name :" + delegateeName);
        parties.add(delegateeName);
        DelegationPolicy policy = Utils.createIdentityDelegationPolicy(parties);

        //Create an instance of the delegation client, specifies the CDS Service URL and the credential 
        //to be delegated.

        DelegationUserClient client = new DelegationUserClient(cdsURL, credential);

        //Delegates the credential and returns a reference which can later be used by allowed parties to 
        //obtain a credential.

        DelegatedCredentialReference ref = client.delegateCredential(delegationLifetime, delegationPathLength,
                                                                     policy, issuedCredentialLifetime,
                                                                     issuedCredentialPathLength, keySize);
        return ref;

    }

    /**
     * It converts the delegatedCredentialReference to String. 
     * @return String
     */
    public static String getSerializedDelegatedCredReference() {

        String serializedDelegatedCredentialReference = null;

        try {
            StringWriter stringWriter = new StringWriter();

            gov.nih.nci.cagrid.common.Utils.serializeObject(
                                                            delegatedCredentialReference,
                                                            new QName(ClientPropertyLoader.getCDSDelegatedUrl(),
                                                                    "DelegatedCredentialReference"),
                                                            stringWriter,
                                                            UserValidator.class.getClassLoader().getResourceAsStream(
                                                                                                                     "cdsclient-config.wsdd"));
            serializedDelegatedCredentialReference = stringWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException("Unable to serialize the  Delegated Credentials : ",
                    ErrorCodeConstants.CDS_002);
        }
        return serializedDelegatedCredentialReference;

    }

    /**
     * It sets delegatedCredentialReference
     * @param delegatedCredentialReference
     */
    public static void setDelegatedCredentialReference(DelegatedCredentialReference delegatedCredentialReference) {
        UserValidator.delegatedCredentialReference = delegatedCredentialReference;
    }

    /**
     * @return
     */
    public static String getIdP() {
        return idP;
    }

    /**
     * @param idP
     */
    public static void setIdP(String idP) {
        UserValidator.idP = idP;
    }

    private static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    /**
     * It generates the .globus Certificate  in User Home
     */
    public static void generateGlobusCertificate(String idP) {
        try {

            String gridCertDir = ClientPropertyLoader.getGridCertForGlobus(idP);
            File dir = new File(gridCertDir);
            if (dir.exists()) {
                File[] list = dir.listFiles();
                if (list != null) {
                    for (int i = 0; i < list.length; i++) {
                        if (list[i].isFile()) {
                            File dest = new File(gov.nih.nci.cagrid.common.Utils.getTrustedCerificatesDirectory()
                                    + File.separator + list[i].getName());
                            copy(list[i], dest);
                        }
                    }
                }
            } else {
                logger.error("The source directory, " + dir.getAbsolutePath() + " does not exist.");

            }
            logger.debug("getting sync-descriptor file for " + idP);

            String pathToSyncDescription = ClientPropertyLoader.getSyncDesFile(idP);
            logger.debug("Trying to sync with GTS service ");

            SyncDescription description = (SyncDescription) gov.nih.nci.cagrid.common.Utils.deserializeDocument(
                                                                                                                pathToSyncDescription,
                                                                                                                SyncDescription.class);
            SyncGTS.getInstance().syncOnce(description);

            logger.debug("Successfully sync with GTS service ");
            logger.debug(".Globus credential generated successfully");
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

}
