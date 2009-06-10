package edu.wustl.cab2b.server.experiment;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import edu.wustl.cab2b.common.domain.ArrayDesign;
import edu.wustl.cab2b.common.domain.MicroarrayChip;
import edu.wustl.cab2b.common.experiment.ArrayDesignBusinessInterface;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.HibernateDAO;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.global.Validator;

/**
 * A class containing array design related business logic.
 * @author chetan_bh
 *
 */
public class ArrayDesignSessionBean extends DefaultBizLogic implements SessionBean, Serializable, ArrayDesignBusinessInterface
{
	/**
	 * Hibernate DAO Type to use.
	 */
	int daoType = Constants.HIBERNATE_DAO;
	
	/**
	 * TODO Rollback not done properly.
	 * 
	 * Returns a list of microarray chips of a particular design type.
	 * @param arrayDesign object.
	 * @return a list of microarray chips.
	 * @throws ClassNotFoundException 
	 */
	public List getAllChipsFor(Object arrayDesign) throws DAOException, ClassNotFoundException
	{
		List returner = null;
		
		Long aDesignId = ((ArrayDesign)arrayDesign).getId();
		String hql = "from MicroarrayChip as mChip where mChip.arrayDesign.id = "+aDesignId;
		
		DAO dao = DAOFactory.getInstance().getDAO(daoType);
		
		((AbstractDAO)dao).openSession(null);
		returner = dao.executeQuery(hql, null, false, null);
		((AbstractDAO)dao).closeSession();
		
		return returner;
	}
	
	/**
	 * A function to persist array design object.
	 * @param aDesign array design object to persist.
	 * @param daoType
	 * @throws DAOException
	 * @throws BizLogicException
	 * @throws UserNotAuthorizedException
	 */
	public void addArrayDesign(ArrayDesign aDesign) throws DAOException, BizLogicException, UserNotAuthorizedException
	{
		insert( aDesign, daoType);
	}
	
	/**
	 * A callback validation function.
	 */
	protected boolean validate(Object obj, DAO dao, String operation) throws DAOException
    {
		ArrayDesign aDesign = ((ArrayDesign)obj);
		Validator validator = new Validator();
		
		if(aDesign == null)
		{
			throw new DAOException("ArrayDesign object null");
		}
		if(validator.isEmpty(aDesign.getName()))
		{
			throw new DAOException("ArrayDesign name empty");
		}
		return true;
    }
	
	
	public static void main(String[] args)
	{
		ArrayDesignSessionBean adBizLogic = new ArrayDesignSessionBean();
		
		DAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		
		ArrayDesign ad = null;
		List results;
		try
		{
			((HibernateDAO)dao).openSession(null);
			ad = (ArrayDesign) dao.retrieve(ArrayDesign.class.getName(), new Long(1));
			results = adBizLogic.getAllChipsFor(ad);
			for (int i = 0; i < results.size(); i++)
			{
				MicroarrayChip mChip = (MicroarrayChip)results.get(i);
				//Logger.out.info("MicroarrayChip (id,name) "+mChip.getId()+","+mChip.getLabel());
			}
		}
		catch (DAOException e)
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}		
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
