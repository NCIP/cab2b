package edu.wustl.cab2b.server.ejb.user;

import java.rmi.RemoteException;

import edu.wustl.cab2b.common.ejb.user.UserBusinessInterface;
import edu.wustl.cab2b.common.user.User;
import edu.wustl.cab2b.server.ejb.AbstractStatelessSessionBean;
import edu.wustl.cab2b.server.user.UserOperations;

public class UserBean extends AbstractStatelessSessionBean implements UserBusinessInterface{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public User getUserById(Long id) throws RemoteException {
		return new UserOperations().getUserById(id);
	}

	public void insertUser(User user) throws RemoteException {
		new UserOperations().insertUser(user);
	}

	public void updateUser(User user) throws RemoteException {
		new UserOperations().updateUser(user);
	}

	public String[] getServiceURLs(String appName) throws RemoteException {
		return new UserOperations().getServiceURLs(appName);
	}

}
