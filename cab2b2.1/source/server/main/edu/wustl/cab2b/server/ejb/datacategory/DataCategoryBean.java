/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.ejb.datacategory;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.util.List;

import edu.wustl.cab2b.common.category.DataCategory;
import edu.wustl.cab2b.common.ejb.datacategory.DataCategoryBusinessInterface;
import edu.wustl.cab2b.server.datacategory.DataCategoryOperations;
import edu.wustl.cab2b.server.ejb.AbstractStatelessSessionBean;
import edu.wustl.cab2b.server.util.ConnectionUtil;

/**
 * 
 * @author lalit_chand, atul_jawale
 *
 */
public class DataCategoryBean extends AbstractStatelessSessionBean implements DataCategoryBusinessInterface {

    /**
     * Saves the input  datacategory  to database.
     * @param dataCategory to  save.
     * @throws RemoteException EBJ specific Exception
     */
    public void saveDataCategory(DataCategory dataCategory) throws RemoteException {
        new DataCategoryOperations().saveDataCategory(dataCategory);

    }

    /**   
     * Returns all the datacategories present in system.
     * @return List of datacategories.
     * @throws RemoteException EBJ specific Exception
     */
    public List<DataCategory> getAllDataCategories() throws RemoteException {
        Connection con = ConnectionUtil.getConnection();
        try {
            return new DataCategoryOperations().getAllDataCategories(con);
        } finally {
            ConnectionUtil.close(con);
        }
    }

}
