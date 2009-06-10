package edu.wustl.cab2b.server.ejb.datacategory;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.util.List;

import edu.wustl.cab2b.common.category.DataCategory;
import edu.wustl.cab2b.common.ejb.datacategory.DataCategoryBusinessInterface;
import edu.wustl.cab2b.server.category.CategoryOperations;
import edu.wustl.cab2b.server.datacategory.DataCategoryOperations;
import edu.wustl.cab2b.server.ejb.AbstractStatelessSessionBean;
import edu.wustl.cab2b.server.util.ConnectionUtil;

public class DataCategoryBean extends AbstractStatelessSessionBean implements DataCategoryBusinessInterface {

    /**
     * This method Saves the datacategory to database.
     */
    public void saveDataCategory(DataCategory dataCategory) throws RemoteException {
        new DataCategoryOperations().saveDataCategory(dataCategory);

    }
    
    
    public List<DataCategory> getAllDataCategories() throws RemoteException 
    {
     
        Connection con = ConnectionUtil.getConnection();
        try {
            return new DataCategoryOperations().getAllDataCategories(con);
        } finally {
            ConnectionUtil.close(con);
        }
    }

}