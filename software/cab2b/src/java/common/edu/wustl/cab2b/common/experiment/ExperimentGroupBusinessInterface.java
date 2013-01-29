/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.experiment;

import java.rmi.RemoteException;

import edu.wustl.cab2b.common.BusinessInterface;
import edu.wustl.cab2b.common.domain.ExperimentGroup;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;

public interface ExperimentGroupBusinessInterface extends BusinessInterface {

    /**
     * Adds a experiment group to a given parent
     * @param parentExperimentGroupId
     * @param experimentGroup
     * @param dref
     * @param idP
     * @return Reference to ExperimentGroup
     * @throws BizLogicException
     * @throws UserNotAuthorizedException
     * @throws RemoteException
     * @throws DAOException
     */
    ExperimentGroup addExperimentGroup(Long parentExperimentGroupId, ExperimentGroup experimentGroup, String dref)
            throws BizLogicException, UserNotAuthorizedException, RemoteException, DAOException;

    /**
     * This method returns false if ExperimentGroup with given name is not present in the database.
     * It returns false otherwise.
     * @param name
     * @return
     */
    boolean isExperimentGroupByNamePresent(String name) throws RemoteException;
}
