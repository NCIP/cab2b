package edu.wustl.cab2b.server.ejb.user;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import org.globus.gsi.GlobusCredential;

import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.cab2b.common.ejb.user.UserBusinessInterface;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.ejb.AbstractStatelessSessionBean;
import edu.wustl.cab2b.server.user.UserOperations;

/**
 * This bean handles user based operations
 * 
 * @author hrishikesh_rajpathak
 *
 */
public class UserBean extends AbstractStatelessSessionBean implements UserBusinessInterface {

	private static final long serialVersionUID = 1L;

	public void insertUser(UserInterface user) throws RemoteException {
		new UserOperations().insertUser(user);
	}

	public void updateUser(UserInterface user) throws RemoteException {
		new UserOperations().updateUser(user);
	}

	public Map<String, List<String>> getServiceUrlsForUser(UserInterface user) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException, RemoteException {
		return new UserOperations().getServiceURLsForUser(user);
	}

	/* (non-Javadoc)
	 * @see edu.wustl.cab2b.common.ejb.user.UserBusinessInterface#getUserByName(java.lang.String)
	 * If user not found, it returns null
	 */
	public UserInterface getUserByName(String user) throws RemoteException {
		return new UserOperations().getUserByName(user);
	}
    
    public GlobusCredential validateUser(String userName, String password, String dorianUrl) throws RemoteException{
        return new UserOperations().validateUser(userName,password,dorianUrl);
    }

}
