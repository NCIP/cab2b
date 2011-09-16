/**
 * 
 */
package edu.wustl.common.util.dbManager;

import java.util.Collection;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * @author chetan_patil
 * @created Sep 14, 2007, 3:35:35 PM
 */
public class HibernateUtility {
    public static final String GET_PARAMETERIZED_QUERIES_DETAILS = "getParameterizedQueriesDetails";

    public static Collection executeHQL(String queryName, List<Object> values) throws HibernateException{
    	try
    	{
    		Session session = DBUtil.currentSession();
            Query query = session.getNamedQuery(queryName);

            if (values != null) {
                for (int counter = 0; counter < values.size(); counter++) {
                    Object value = values.get(counter);
                    String objectType = value.getClass().getName();
                    String onlyClassName = objectType.substring(objectType.lastIndexOf(".") + 1, objectType.length());
                    if (onlyClassName.equals("String")) {
                        query.setString(counter, (String) value);
                    } else if (onlyClassName.equals("Integer")) {
                        query.setInteger(counter, Integer.parseInt(value.toString()));
                    } else if (onlyClassName.equals("Long")) {
                        query.setLong(counter, Long.parseLong(value.toString()));
                    }
                    
                }
            }
            return query.list();
	    }
		finally
		{
			DBUtil.closeSession();
		}
    }

    public static Collection executeHQL(String queryName) throws HibernateException {
        return executeHQL(queryName, null);
    }

}
