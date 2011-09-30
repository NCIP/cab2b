
package edu.wustl.common.audit;

import edu.wustl.common.beans.LoginDetails;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.domain.LoginEvent;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * This class handles the insertion of Login Events details into the database  
 * @author niharika_sharma
 */
public class LoginAuditManager
{

	public static org.apache.log4j.Logger logger = Logger.getLogger(LoginAuditManager.class);

	/**
	 * Login Event instance which contains the event details
	 */
	private LoginEvent loginEvent;

	/**
	 * Public constructor to instantiate LoginAuditManager from SecurityManager.java 
	 */
	public LoginAuditManager()
	{
		loginEvent = new LoginEvent();
	}

	/**
	 * Create an instance of LoginAuditManager and initialize the contained loginEvent object with
	 * the details provided by the LoginDetails object
	 * @param loginDetails
	 */
	public LoginAuditManager(LoginDetails loginDetails)
	{
		loginEvent = new LoginEvent();
		loginEvent.setIpAddress(loginDetails.getIpAddress());
		loginEvent.setSourceId(loginDetails.getSourceId());
		loginEvent.setUserLoginId(loginDetails.getUserLoginId());
	}

	/**
	 * Inserts the loginEvent object in the database using the DAO implementation provided as argument
	 * @param dao
	 * @param loginEvent
	 * @throws DAOException
	 */
	protected void insert(DAO dao, LoginEvent loginEvent) throws DAOException
	{
		try
		{
			dao.insert(loginEvent, null, false, false);
		}
		catch (UserNotAuthorizedException sme)
		{
			Logger.out.debug("Exception:" + sme.getMessage(), sme);
		}
	}

	/**
	 * To be called by SecurityManager.java to audit this login attempt.
	 * Sets the status of LoginAttempt to loginStatus provided as an argument.
	 * @param loginStatus
	 */
	public void audit(boolean loginStatus)
	{
		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		try
		{
			this.loginEvent.setIsLoginSuccessful(loginStatus);
			dao.openSession(null);
			insert(dao, this.loginEvent);
			dao.commit();
		}
		catch (DAOException daoException)
		{
			logger.debug("Exception while Auditing Login Attempt. " + daoException.getMessage(),
					daoException);
		}
		finally
		{
			try
			{
				dao.closeSession();
			}
			catch (DAOException daoException)
			{
				logger.debug(
						"Exception while Auditing Login Attempt. " + daoException.getMessage(),
						daoException);
			}
		}
	}
}
