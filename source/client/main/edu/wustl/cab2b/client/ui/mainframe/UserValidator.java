package edu.wustl.cab2b.client.ui.mainframe;

import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.ejb.EjbNamesConstants;
import edu.wustl.cab2b.common.ejb.user.UserBusinessInterface;
import edu.wustl.cab2b.common.ejb.user.UserHomeInterface;
import edu.wustl.cab2b.common.util.PropertyLoader;
import gov.nih.nci.cagrid.authentication.stubs.types.AuthenticationProviderFault;
import gov.nih.nci.cagrid.authentication.stubs.types.InsufficientAttributeFault;
import gov.nih.nci.cagrid.authentication.stubs.types.InvalidCredentialFault;

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
     * @throws RemoteException 
     * @throws AuthenticationProviderFault 
     * @throws InsufficientAttributeFault 
     * @throws InvalidCredentialFault 
     * @throws MalformedURIException 
     */
    public static boolean validateUser(String userName, String password, String idP) {
        setUserName(userName);
        dorianUrl = PropertyLoader.getDorianUrl(idP);

        UserBusinessInterface userBusinessInterface = (UserBusinessInterface) CommonUtils.getBusinessInterface(
                                                                                                               EjbNamesConstants.USER_BEAN,
                                                                                                               UserHomeInterface.class);
        try {
            proxy = userBusinessInterface.validateUser(userName, password, dorianUrl);
        } catch (RemoteException e) {
            return false;
        }
        if (proxy == null) {
            return false;
        } else {
            return true;
        }
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
