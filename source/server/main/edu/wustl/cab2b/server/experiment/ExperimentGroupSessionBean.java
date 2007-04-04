package edu.wustl.cab2b.server.experiment;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import edu.wustl.cab2b.common.domain.ExperimentGroup;
import edu.wustl.cab2b.common.experiment.ExperimentGroupBusinessInterface;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * A class containing experiment group related business logic.
 * @author chetan_bh
 *
 */
public class ExperimentGroupSessionBean extends DefaultBizLogic implements SessionBean, ExperimentGroupBusinessInterface, Serializable
{
	
	/**
	 * Hibernate DAO Type to use.
	 */
	int daoType = Constants.HIBERNATE_DAO;
	
	public ExperimentGroup addExperimentGroup(Object expGrp) throws BizLogicException, UserNotAuthorizedException, RemoteException
	{
		insert(expGrp, daoType);
		return (ExperimentGroup)expGrp;
	}
	
	public ExperimentGroup getExperimentGroup(String identifier) throws DAOException, RemoteException
	{
		System.out.println("\n\ngetExperimentGroup :: "+identifier+"\n\n");
		List expGrpList = retrieve("ExperimentGroup", "id", identifier);
		ExperimentGroup expGrp = (ExperimentGroup)expGrpList.get(0);
		System.out.println("\n\nexperiment group retrieved "+expGrp.getName()+"\n\n");
		return expGrp;
	}
	
	/**
	 * A callback validation function.
	 */
	protected boolean validate(Object obj, DAO dao, String operation) throws DAOException
    {
		ExperimentGroup exp = ((ExperimentGroup)obj);
		Validator validator = new Validator();
		if(exp == null)
		{
			throw new DAOException("Null parameter passed instead of Experiment Group");
		}
		
		//TODO to removed later
		if(validator.isEmpty(exp.getName()))
		{
			throw new DAOException("Experiment group name empty");
		}
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
