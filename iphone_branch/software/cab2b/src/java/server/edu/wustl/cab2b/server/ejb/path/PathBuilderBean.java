/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.ejb.path;

import java.rmi.RemoteException;

import edu.wustl.cab2b.common.ejb.path.PathBuilderBusinessInterface;
import edu.wustl.cab2b.server.ejb.AbstractStatelessSessionBean;

/**
 * This class Builds all paths for a Domain Model of given application.<br>
 * Essentially this class acts as a Controller that calls different utility
 * classes to build all possible non-redundent paths for a given model.
 * It also loads the generated paths to database.
 * <b> NOTE : </n> It does not creates PATH table. 
 * It assumes that the table is already been present in database 
 * @author Chandrakant Talele
 */
public class PathBuilderBean extends AbstractStatelessSessionBean implements PathBuilderBusinessInterface {
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