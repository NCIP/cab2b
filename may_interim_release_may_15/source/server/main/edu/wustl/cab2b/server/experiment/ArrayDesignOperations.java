package edu.wustl.cab2b.server.experiment;

import java.util.List;

import edu.wustl.cab2b.common.domain.ArrayDesign;
import edu.wustl.cab2b.common.domain.MicroarrayChip;
import edu.wustl.cab2b.server.ejb.experiment.ArrayDesignSessionBean;
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


public class ArrayDesignOperations extends DefaultBizLogic
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
		List results = null;
		try
		{
			((HibernateDAO)dao).openSession(null);
			ad = (ArrayDesign) dao.retrieve(ArrayDesign.class.getName(), new Long(1));
			//results = adBizLogic.getAllChipsFor(ad);
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
	}
}
