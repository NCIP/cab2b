package edu.wustl.cab2b.client.ui.mainframe;

import java.io.IOException;
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

import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.util.PropertyLoader;
import gov.nih.nci.cagrid.authentication.bean.BasicAuthenticationCredential;
import gov.nih.nci.cagrid.authentication.bean.Credential;
import gov.nih.nci.cagrid.authentication.client.AuthenticationClient;
import gov.nih.nci.cagrid.dorian.client.IFSUserClient;
import gov.nih.nci.cagrid.dorian.ifs.bean.ProxyLifetime;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

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

    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(UserValidator.class);

    private static DelegatedCredentialReference delegatedCredentialReference;

    /**
     * Validates user on the basis of username, password and the idP that it
     * points to.
     * 
     * @param userName
     * @param password
     * @param idP
     * @return boolean stating is valid user or not
     */
    public static void validateUser(String userName, String password, String idP) throws RemoteException {
        setUserName(userName);
        String dorianUrl = PropertyLoader.getDorianUrl(idP);

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
        } catch (Exception e) {
            logger.error(e.getStackTrace());
        }
        try {
            // When the user gets authenticated successfully Then only its credential is delegated 
            UserValidator.delegatedCredentialReference = getDelegatedCredential(proxy);

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
    public static DelegatedCredentialReference getDelegatedCredential(GlobusCredential credential)
            throws RemoteException, Exception {
        String cdsURL = PropertyLoader.getCDSTrainingUrl();
        //  String cdsURL = "https://localhost:8443/wsrf/services/cagrid/CredentialDelegationService";
        //Specifies the path length of the credential being delegate the minumum is 1.

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

        String delegateeName = PropertyLoader.getDelegetee();
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
                                                            new QName(PropertyLoader.getCDSDelegatedUrl(),
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

}
