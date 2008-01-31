package edu.wustl.cab2b.server.user;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.cab2b.common.user.ServiceURL;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
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
			try {
				postProcessUser(user);
			} catch (DynamicExtensionsSystemException e) {
				throw new RemoteException(e.getMessage());
			} catch (DynamicExtensionsApplicationException e) {
				throw new RemoteException(e.getMessage());
			}
		} else {
			throw new RemoteException("Couldn't find user with userId = " + id);
		}

		return user;
	}

	private void postProcessUser(User user) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException {
		Collection<ServiceURLInterface> serviceCollection = user.getServiceURLCollection();
		for (ServiceURLInterface serviceURL : serviceCollection) {
			String entityGroupName = ((ServiceURL) serviceURL).getEntityGroupName();
			EntityGroupInterface entityGroup = EntityManager.getInstance().getEntityGroupByName(
					entityGroupName);
			serviceURL.setEntityGroupInterface(entityGroup);
		}
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

	/**
	 * Returns all the URLs of the data services which are confirming model of
	 * given application
	 * 
	 * @param appName
	 *            Aplication name
	 * @return Returns the List of URLs
	 * @throws RemoteException
	 */
	public String[] getServiceURLs(String appName) throws RemoteException {
		EntityGroupInterface inputEntityGroup = null;
		Collection<String> returnUrls = new HashSet<String>();
		try {
			inputEntityGroup = EntityManager.getInstance().getEntityGroupByName(appName);
		} catch (DynamicExtensionsSystemException e) {
			throw new RemoteException(e.getMessage());
		} catch (DynamicExtensionsApplicationException e) {
			throw new RemoteException(e.getMessage());
		}

		Long inputEntityGroupId = inputEntityGroup.getId();
		// TODO currently hardcoded. Later this id is to be taken from session
		User user = getUserById(new Long(2L));
		Collection<ServiceURLInterface> serviceURLCollection = user.getServiceURLCollection();
		if (serviceURLCollection != null && !serviceURLCollection.isEmpty()) {
			returnUrls = populateStringURLs(serviceURLCollection, inputEntityGroupId);
		} else {
			returnUrls = getAdminServiceUrls(inputEntityGroupId);
		}

		return returnUrls.toArray(new String[0]);
	}

	/**
	 * @param inputEntityGroupId
	 * @return
	 * @throws RemoteException
	 */
	private Collection<String> getAdminServiceUrls(Long inputEntityGroupId) throws RemoteException {
		// TODO get user not by id but where isAdmin==true;
		User user = getUserById(new Long(1L));
		Collection<ServiceURLInterface> serviceURLCollection = user.getServiceURLCollection();

		Collection<String> serviceURLStrings = new HashSet<String>();
		if (serviceURLCollection != null && !serviceURLCollection.isEmpty()) {
			serviceURLStrings = populateStringURLs(serviceURLCollection, inputEntityGroupId);
		}
		return serviceURLStrings;
	}

	private Collection<String> populateStringURLs(
			Collection<ServiceURLInterface> serviceURLCollection, Long inputEntityGroupId) {
		Collection<String> serviceURLStrings = new HashSet<String>();

		for (ServiceURLInterface serviceURL : serviceURLCollection) {
			EntityGroupInterface entityGroup = serviceURL.getEntityGroupInterface();
			if (inputEntityGroupId.compareTo(entityGroup.getId()) == 0) {
				serviceURLStrings.add(serviceURL.getUrlLocation());
			}
		}

		return serviceURLStrings;
	}

}
