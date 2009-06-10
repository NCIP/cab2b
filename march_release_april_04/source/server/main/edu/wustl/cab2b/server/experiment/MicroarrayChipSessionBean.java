package edu.wustl.cab2b.server.experiment;

import java.io.Serializable;
import java.rmi.RemoteException;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import edu.wustl.cab2b.common.domain.MicroarrayChip;
import edu.wustl.cab2b.common.experiment.MicroarrayChipBusinessInterface;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * A class containing microarray chip related business logic.
 * @author chetan_bh
 *
 */
public class MicroarrayChipSessionBean extends DefaultBizLogic implements SessionBean, Serializable, MicroarrayChipBusinessInterface
{
	
	/**
	 * Hibernate DAO Type to use.
	 */
	int daoType = Constants.HIBERNATE_DAO;
	
	/**
	 * A function to save a microarray chip.
	 * @param micChip microarray chip to persist.
	 * @param daoType
	 * @throws UserNotAuthorizedException 
	 * @throws Exception
	 */
	public void addChip(Object micChip) throws BizLogicException, UserNotAuthorizedException
	{
		insert(micChip, daoType);
	}
	
	/**
	 * A callback validation function.
	 * @throws BizLogicException 
	 */
	protected boolean validate(Object obj, DAO dao, String operation) throws DAOException
    {
		Logger.out.info("MicroarrayChipSessionBean : validate ");
		MicroarrayChip mChip = (MicroarrayChip) obj;
		//Validator validator = new Validator();
		if(mChip == null)
		{
			throw new DAOException("Null object passed instead of Microarray chip.");
		}
		if(mChip.getArrayDesign() == null)
		{
			throw new DAOException("Chip does't have an array design type.");
		}
		//Logger.out.info("MicroarrayChipSessionBean : validate : returning true");
		return true;
    }
	
	
	public static void main(String[] args)
	{
		
	}

	public void ejbActivate() throws EJBException, RemoteException {
		// TODO Auto-generated method stub
		
	}

	public void ejbPassivate() throws EJBException, RemoteException {
		// TODO Auto-generated method stub
		
	}

	public void ejbRemove() throws EJBException, RemoteException {
		// TODO Auto-generated method stub
		
	}

	public void setSessionContext(SessionContext arg0) throws EJBException, RemoteException {
		// TODO Auto-generated method stub
		
	}
	
	public void ejbCreate()
    {
        
    }
}
