package edu.wustl.cab2b.client.ui.mainframe;

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
import edu.wustl.cab2b.common.util.Utility;
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

    private static String gridType = null;

    private static String serializedDCR = null;

    public UserValidator(final String userName, final String gridType) {
        this.userName = userName;
        UserValidator.gridType = gridType;
    }

    /**
     * @return
     */
    public static String getGridType() {
        return gridType;
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
     * @param gridType
     * @throws RemoteException
     */
    public void validateUser(final String password) {
        logger.debug("Validating the user on grid...");
        Utility.generateGlobusCertificate(gridType);

        String authenticationURL = ClientPropertyLoader.getAuthenticationURL(gridType);
        Credential credential = createCredentials(userName, password);
        SAMLAssertion saml = autheticateUser(authenticationURL, credential);

        String dorianUrl = ClientPropertyLoader.getIdP(gridType);
        GlobusCredential proxy = getGlobusCredentials(dorianUrl, saml);

        DelegatedCredentialReference dcr = getDelegatedCredentialReference(proxy, gridType);
        serializeDelegatedCredentialReference(dcr);

        logger.debug("Credential delegated sucessfully");
    }

    private SAMLAssertion autheticateUser(String authenticationUrl, Credential credential) {
        AuthenticationClient authenticationClient = null;
        try {
            logger.debug("Getting authentication client...");
            authenticationClient = new AuthenticationClient(authenticationUrl, credential);
        } catch (MalformedURIException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Please check the authentication service URL", ErrorCodeConstants.UR_0006);
        } catch (RemoteException e) {
            logger.error("Unable to create the authentication client: " + e.getMessage(), e);
            throw new RuntimeException("Unable to create the authentication client: " + e.getMessage(),
                    ErrorCodeConstants.CDS_020);
        }

        SAMLAssertion saml = null;
        try {
            logger.debug("Authenticating the user...");
            saml = authenticationClient.authenticate();
        } catch (InvalidCredentialFault e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Invalid user name or password", ErrorCodeConstants.UR_0007);
        } catch (InsufficientAttributeFault e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("User name or password is missing", ErrorCodeConstants.UR_0008);
        } catch (AuthenticationProviderFault e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Error occurred at Authentication Provider service",
                    ErrorCodeConstants.UR_0009);
        } catch (RemoteException e) {
            logger.error("Unable to authenticate the user:" + e.getMessage(), e);
            throw new RuntimeException("Unable to authenticate the user:" + e.getMessage(),
                    ErrorCodeConstants.CDS_019);
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
            logger.error("Unable to generate GlobusCredential: " + e.getMessage(), e);
            throw new RuntimeException("Unable to generate GlobusCredential: " + e.getMessage(),
                    ErrorCodeConstants.CDS_018);
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

        //Specifies the path length of the credential being delegate the minimum is 1.
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
            throw new RuntimeException("Client does not have permission to delegate the credential to CDS.",
                    ErrorCodeConstants.CDS_008);
        } catch (MalformedURIException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Incorrect CDS URL. Please check the CDS URL in conf/client.properties",
                    ErrorCodeConstants.CDS_009);
        } catch (RemoteException e) {
            logger.error("Unable to delegate the credential to CDS" + e.getMessage(), e);
            throw new RuntimeException("Unable to delegate the credential to CDS" + e.getMessage(),
                    ErrorCodeConstants.CDS_017);
        } catch (Exception e) {
            logger.error("Unable to delegate the credential to CDS" + e.getMessage(), e);
            throw new RuntimeException("Unable to delegate the credential to CDS" + e.getMessage(),
                    ErrorCodeConstants.CDS_017);
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
}
