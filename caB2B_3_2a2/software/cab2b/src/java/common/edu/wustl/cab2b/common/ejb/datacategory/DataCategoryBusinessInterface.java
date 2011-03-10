package edu.wustl.cab2b.common.ejb.datacategory;

import java.rmi.RemoteException;
import java.util.List;

import edu.wustl.cab2b.common.BusinessInterface;
import edu.wustl.cab2b.common.category.DataCategory;
import edu.wustl.common.querysuite.metadata.category.Category;

public interface DataCategoryBusinessInterface extends BusinessInterface {

    /**
     * Saves the input  datacategory  to database.
     * @param dataCategory to  save.
     * @throws RemoteException EBJ specific Exception
     */
    void saveDataCategory(DataCategory dataCategory) throws RemoteException;

    /**   
     * Returns all the datacategories present in system.
     * @return List of datacategories.
     * @throws RemoteException EBJ specific Exception
     */
    List<DataCategory> getAllDataCategories() throws RemoteException;
    
}
