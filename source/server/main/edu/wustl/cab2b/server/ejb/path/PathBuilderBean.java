package edu.wustl.cab2b.server.ejb.path;

import java.rmi.RemoteException;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import edu.wustl.cab2b.common.ejb.path.PathBuilderBusinessInterface;

/**
 * This class Builds all paths for a Domain Model of given application.<br>
 * Essentially this class acts as a Controller that calls different utility
 * classes to build all possible non-redundent paths for a given model.
 * It also loads the generated paths to database.
 * <b> NOTE : </n> It does not creates PATH table. 
 * It assumes that the table is already been present in database 
 * @author Chandrakant Talele
 */

@Remote(PathBuilderBusinessInterface.class)
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class PathBuilderBean  implements PathBuilderBusinessInterface {
    private static final long serialVersionUID = -8177912823613782951L;

    /**
     * This method is to be called at server startup.
     * It builds all non-redundent paths for traversal between classes of all configured models.
     * It writes all paths to a datafile first for all the supported models then stores all paths to database.
     * @throws RemoteException EJB specific exception
     */
    public void init() throws RemoteException {
        //        Logger.out.debug("Entering method init");
        //        //PathConstants.CREATE_TABLE_FOR_ENTITY = true;
        //        Connection connection = ConnectionUtil.getConnection();
        //        try {
        //            PathBuilder.buildAndLoadAllModels(connection);
        //        } finally {
        //            ConnectionUtil.close(connection);
        //        }
        //        Logger.out.debug("Leaving method main");
        // TODO check if any of this is needed.
    }
}