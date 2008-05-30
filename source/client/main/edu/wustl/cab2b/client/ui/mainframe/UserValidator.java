package edu.wustl.cab2b.client.ui.mainframe;

import java.rmi.RemoteException;

import org.globus.gsi.GlobusCredential;

import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.ejb.EjbNamesConstants;
import edu.wustl.cab2b.common.ejb.user.UserBusinessInterface;
import edu.wustl.cab2b.common.ejb.user.UserHomeInterface;
import edu.wustl.cab2b.common.util.PropertyLoader;

/**
 * This class passes user info to serveer to get user validated and get proxy
 * informaion
 * 
 * @author hrishikesh_rajpathak
 * 
 */
public class UserValidator {

    private static GlobusCredential proxy;

    private static String userName;

    private static String dorianUrl;

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
        dorianUrl = PropertyLoader.getDorianUrl(idP);

        UserBusinessInterface userBean = (UserBusinessInterface) CommonUtils.getBusinessInterface(
                                                                                                  EjbNamesConstants.USER_BEAN,
                                                                                                  UserHomeInterface.class);
        proxy = userBean.validateUser(userName, password, dorianUrl);

        if (proxy == null) {
            throw new RemoteException("Proxy is null");
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
     * @param userName
     *            The userName to set.
     */
    public static void setUserName(String userName) {
        UserValidator.userName = userName;
    }

}
