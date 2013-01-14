/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.experiment;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.BusinessInterface;
import edu.wustl.cab2b.common.CustomDataCategoryModel;
import edu.wustl.cab2b.common.domain.Experiment;
import edu.wustl.cab2b.common.exception.CheckedException;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;

public interface ExperimentBusinessInterface extends BusinessInterface {

    /**
     * Adds Experiment for expression.
     * @param exp
     * @throws BizLogicException
     * @throws UserNotAuthorizedException
     * @throws RemoteException
     */
    //  public void addExperiment(Object exp) throws BizLogicException, UserNotAuthorizedException, RemoteException;
    /**
     * Adds a given experiment to specific Experiment Group.
     * @param experimentGroupId
     * @param experiment
     * @throws BizLogicException
     * @throws UserNotAuthorizedException
     * @throws DAOException
     * @throws RemoteException
     */
    void addExperiment(Long experimentGroupId, Experiment experiment, String dref, String idP)
            throws BizLogicException, UserNotAuthorizedException, DAOException, RemoteException;

    /**
     * Moves a experiment from given Experiment group to a target Experiment group
     * @param exp
     * @param srcExpGrp
     * @param tarExpGrp
     * @throws DAOException
     * @throws BizLogicException
     * @throws UserNotAuthorizedException
     * @throws RemoteException
     */
    void move(Object exp, Object srcExpGrp, Object tarExpGrp) throws DAOException, BizLogicException,
            UserNotAuthorizedException, RemoteException;

    /**
     * Copy given Experiment to target Experiment group.
     * @param exp
     * @param tarExpGrp
     * @throws BizLogicException
     * @throws UserNotAuthorizedException
     * @throws RemoteException
     */
    void copy(Object exp, Object tarExpGrp) throws BizLogicException, UserNotAuthorizedException, RemoteException;

    /**
     * Returns list of experiment hierarchy.
     * @return
     * @throws ClassNotFoundException
     * @throws DAOException
     * @throws RemoteException
     */
    Vector getExperimentHierarchy(String dref, String idP) throws ClassNotFoundException, DAOException,
            RemoteException;

    /**
     * Returns list of all experiments associated with specified user 
     * @param user
     * @return
     * @throws RemoteException
     */
    List<Experiment> getExperimentsForUser(UserInterface user, String dref, String idP) throws RemoteException;

    /**
     * Returns experiment belongs to given id.
     * @param id experiment id
     * @return Experiment belongs to given id.
     * @throws RemoteException
     * @throws DAOException
     */
    Experiment getExperiment(Long id) throws RemoteException, DAOException;

    /**
     * get a set of root entities for an experiment where each root entity
     * represents a datalist
     * 
     * @param exp the experiment
     * @return set of root entities for an experiment where each root entity
     *         represents a datalist
     */
    Set<Set<EntityInterface>> getDataListEntitySet(Experiment exp) throws RemoteException;

    /**
     * save the given data as a data category
     * 
     * @param title the title for the category
     * @param attributes list of attributes needed for the new entity
     * @param data the data to be saved
     * @return the newly saved entity
     */
    EntityInterface saveDataCategory(String title, List<AttributeInterface> attributes, Object[][] data)
            throws RemoteException;

    /**
     * Adds a data list to given experiment.
     * @param experimentId data list to be added in this experiment
     * @param dataListMetaDataId
     * @throws RemoteException
     */
    void addDataListToExperiment(Long experimentId, Long dataListMetaDataId) throws RemoteException;

    /**
     * Returns data category data model
     * @param experiment
     * @return CustomDataCategoryModel
     * @throws RemoteException
     * @throws CheckedException
     */
    CustomDataCategoryModel getDataCategoryModel(Experiment experiment) throws RemoteException, CheckedException;

    /**
     * Returns collection of all attributes for specified Experiment ID
     * @param Id
     * @return
     * @throws RemoteException
     * @throws CheckedException
     */
    Collection<AttributeInterface> getAllAttributes(Long experimentID) throws RemoteException, CheckedException;

    /**
     * This method returns false if Experiment with given name is not present in the database.
     * It returns false otherwise.
     * @param name
     * @return
     * @throws DAOException
     */
    boolean isExperimentByNamePresent(String name) throws RemoteException;

}
