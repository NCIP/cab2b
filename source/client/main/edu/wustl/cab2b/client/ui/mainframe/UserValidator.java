package edu.wustl.cab2b.client.ui.mainframe;

import edu.wustl.cab2b.common.util.PropertyLoader;
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
import gov.nih.nci.cagrid.dorian.stubs.types.PermissionDeniedFault;
import gov.nih.nci.cagrid.dorian.stubs.types.UserPolicyFault;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

import java.rmi.RemoteException;

import org.apache.axis.types.URI.MalformedURIException;
import org.globus.gsi.GlobusCredential;

/**
 * @author hrishikesh_rajpathak
 *
 */
public class UserValidator {

    private static GlobusCredential proxy;
    
    private static String userName;

    private static String dorianUrl;

    /**
     * Validates user on the basis of username, password and the idP that it points to.
     * 
     * @param userName
     * @param password
     * @param idP
     * @return
     */
    public static boolean validateUser(String userName, String password, String idP) {
        AuthenticationClient authClient = null;
        SAMLAssertion saml = null;
        
        setUserName(userName);
        dorianUrl = PropertyLoader.getDorianUrl(idP);
        Credential cred = createCredentials(userName, password);
       
        try {
            authClient = new AuthenticationClient(dorianUrl, cred);
        } catch (MalformedURIException e) {
            e.printStackTrace();
            return false;
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
        try {
            saml = authClient.authenticate();
        } catch (InvalidCredentialFault e) {
            e.printStackTrace();
            return false;
        } catch (InsufficientAttributeFault e) {
            e.printStackTrace();
            return false;
        } catch (AuthenticationProviderFault e) {
            e.printStackTrace();
            return false;
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
        setProxy(idP, saml);
        return true;
    }

    /**
     * Sets globus credentials with proxy certificate of 12 hours (maximum possible) lifetime
     * 
     * @param idP
     * @param saml
     */
    private static void setProxy(String idP, SAMLAssertion saml) {
        ProxyLifetime lifetime = new ProxyLifetime();
        lifetime.setHours(12);
        lifetime.setMinutes(0);
        lifetime.setSeconds(0);
        int delegationLifetime = 0;
        IFSUserClient dorian = null;
        try {
            dorian = new IFSUserClient(dorianUrl);
        } catch (MalformedURIException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        try {
            proxy = dorian.createProxy(saml, lifetime, delegationLifetime);
        } catch (DorianFault e) {
            e.printStackTrace();
        } catch (DorianInternalFault e) {
            e.printStackTrace();
        } catch (InvalidAssertionFault e) {
            e.printStackTrace();
        } catch (InvalidProxyFault e) {
            e.printStackTrace();
        } catch (UserPolicyFault e) {
            e.printStackTrace();
        } catch (PermissionDeniedFault e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates Credential object from given username and password.
     * 
     * @param userName
     * @param password
     * @return
     */
    private static Credential createCredentials(String userName, String password) {
        Credential credential = new Credential();
        BasicAuthenticationCredential basicCredentials = new BasicAuthenticationCredential();
        basicCredentials.setUserId(userName);
        basicCredentials.setPassword(password);
        credential.setBasicAuthenticationCredential(basicCredentials);
        return credential;
    }

    /**
     * @return GlobusCredential
     */
    public static GlobusCredential getProxy() {
        return proxy;
    }

    /**
     * @return Returns the userName.
     */
    public static String getUserName() {
        return userName;
    }

    /**
     * @param userName The userName to set.
     */
    public static void setUserName(String userName) {
        UserValidator.userName = userName;
    }

}
