/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.ejb.experiment;

import java.rmi.RemoteException;

import edu.wustl.cab2b.common.domain.ExperimentGroup;
import edu.wustl.cab2b.common.experiment.ExperimentGroupBusinessInterface;
import edu.wustl.cab2b.server.ejb.AbstractStatelessSessionBean;
import edu.wustl.cab2b.server.experiment.ExperimentGroupOperations;
import edu.wustl.cab2b.server.util.UtilityOperations;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * A class containing experiment group related business logic.
 * @author chetan_bh
 *
 */
public class ExperimentGroupSessionBean extends AbstractStatelessSessionBean implements
        ExperimentGroupBusinessInterface {

    private static final long serialVersionUID = -5216059908579963838L;

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
    * @see edu.wustl.cab2b.common.experiment.ExperimentGroupBusinessInterface#addExperimentGroup(java.lang.Long, edu.wustl.cab2b.common.domain.ExperimentGroup)
    */
    public ExperimentGroup addExperimentGroup(Long parentExperimentGroupId, ExperimentGroup experimentGroup,
                                              String serializedDCR) throws BizLogicException,
            UserNotAuthorizedException, RemoteException, DAOException {
        Long userId = UtilityOperations.getLocalUserId(serializedDCR);
        experimentGroup.setUserId(userId);

        return (new ExperimentGroupOperations()).addExperimentGroup(parentExperimentGroupId, experimentGroup);
    }

    /**
     * This method returns false if ExperimentGroup with given name is not present in the database.
     * It returns false otherwise.
     * @param name
     * @return
     */
    public boolean isExperimentGroupByNamePresent(String name) {
        return new ExperimentGroupOperations().isExperimentGroupByNamePresent(name);
    }

}
