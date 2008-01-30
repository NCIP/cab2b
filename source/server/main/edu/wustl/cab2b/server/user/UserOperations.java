package edu.wustl.cab2b.server.user;

import java.rmi.RemoteException;
import java.util.List;

import edu.wustl.cab2b.common.user.User;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.Constants;

public class UserOperations extends DefaultBizLogic {

	public User getUserById(Long id) throws RemoteException {
		List<User> userList = null;
		try {
			userList = (List<User>) retrieve(User.class.getName(), "userId", id);
		} catch (DAOException e) {
			throw new RemoteException(e.getMessage());
		}

		User user = null;
		if (userList != null && !userList.isEmpty()) {
			user = userList.get(0);
		} else {
			throw new RemoteException("Couldn't find user with userId = " + id);
		}

		return user;
	}

	public void insertUser(User user) throws RemoteException {
		try {
			insert(user, Constants.HIBERNATE_DAO);
		} catch (UserNotAuthorizedException e) {
			throw new RemoteException(e.getMessage());
		} catch (BizLogicException e) {
			throw new RemoteException(e.getMessage());
		}
	}

	public void updateUser(User user) throws RemoteException {
		try {
			update(user, Constants.HIBERNATE_DAO);
		} catch (UserNotAuthorizedException e) {
			throw new RemoteException(e.getMessage());
		} catch (BizLogicException e) {
			throw new RemoteException(e.getMessage());
		}
	}

}
