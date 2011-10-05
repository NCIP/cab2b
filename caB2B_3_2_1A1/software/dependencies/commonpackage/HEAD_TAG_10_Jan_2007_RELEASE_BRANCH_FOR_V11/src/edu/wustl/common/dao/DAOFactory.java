/**
 * <p>Title: DAOFactory Class>
 * <p>Description:	DAOFactory is a factory for JDBC DAO instances of various domain objects.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.common.dao;

import edu.wustl.common.util.global.Constants;




/**
 * DAOFactory is a factory for JDBC DAO instances of various domain objects.
 * @author gautam_shetty
 */
public class DAOFactory
{
//	Singleton instace
	private static DAOFactory factory = null;
	
	
	static
	{
		factory = new DAOFactory();
	}
	
	protected DAOFactory()
	{
	}
	
	/**
	 * Setter method in singleton class is to setup mock unit testing.
	 * */
	public static void setDAOFactory(DAOFactory externalFactory)
	{
		factory = externalFactory;
	}
	
	public static DAOFactory getInstance()
	{
		return factory;
	}
    /**
     * Returns DAO instance according to the type.
     * @param type The DAO type.
     * @return An AbstractDAO object.
     */
    public AbstractDAO getDAO(int daoType)
    {
        AbstractDAO dao = null;
        switch (daoType)
        {
            case Constants.HIBERNATE_DAO :
                dao = new HibernateDAOImpl();
                break;

            case Constants.JDBC_DAO :
                dao = new JDBCDAOImpl();
            default :
                break;
        }
        return dao;
    }
    
    

}
