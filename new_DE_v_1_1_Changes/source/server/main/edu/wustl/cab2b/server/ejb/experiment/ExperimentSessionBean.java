/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.ejb.experiment;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.hibernate.HibernateException;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.CustomDataCategoryModel;
import edu.wustl.cab2b.common.domain.DataListMetadata;
import edu.wustl.cab2b.common.domain.Experiment;
import edu.wustl.cab2b.common.exception.CheckedException;
import edu.wustl.cab2b.common.experiment.ExperimentBusinessInterface;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.ejb.AbstractStatelessSessionBean;
import edu.wustl.cab2b.server.experiment.ExperimentOperations;
import edu.wustl.cab2b.server.util.UserUtility;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * A class containing experiment related business logic.
 * 
 * @author chetan_bh
 * 
 */
public class ExperimentSessionBean extends AbstractStatelessSessionBean implements ExperimentBusinessInterface {

    private static final long serialVersionUID = 782660710949035029L;

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.common.experiment.ExperimentBusinessInterface#copy(java.lang.Object, java.lang.Object)
     */
    /**
     * Copy given Experiment to target Experiment group.
     * @param exp
     * @param tarExpGrp
     * @throws BizLogicException
     * @throws UserNotAuthorizedException
     * @throws RemoteException
     */
    public void copy(Object exp, Object tarExpGrp) throws BizLogicException, UserNotAuthorizedException,
            RemoteException {
        (new ExperimentOperations()).copy(exp, tarExpGrp);
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.common.experiment.ExperimentBusinessInterface#getExperimentHierarchy()
     */
    /**
     * Returns list of experiment hierarchy.
     * @param dref
     * @param idP
     * @return Experiment hierarchy
     * @throws ClassNotFoundException
     * @throws DAOException
     * @throws RemoteException
     */
    public Vector getExperimentHierarchy(String serializedDCR, String gridType) throws ClassNotFoundException,
            DAOException, RemoteException {
        String userName = UserUtility.getUsersGridId(serializedDCR, gridType);
        return (new ExperimentOperations()).getExperimentHierarchy(userName);
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.common.experiment.ExperimentBusinessInterface#move(java.lang.Object, java.lang.Object, java.lang.Object)
     */
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
    public void move(Object exp, Object srcExpGrp, Object tarExpGrp) throws DAOException, BizLogicException,
            UserNotAuthorizedException, RemoteException {
        (new ExperimentOperations()).move(exp, srcExpGrp, tarExpGrp);
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.common.experiment.ExperimentBusinessInterface#getExperiment(java.lang.Long)
     */
    /**
     * Returns experiment belongs to given id.
     * @param id experiment id
     * @return Experiment belongs to given id.
     * @throws RemoteException
     * @throws DAOException
     */
    public Experiment getExperiment(Long id) throws RemoteException, DAOException {
        return (new ExperimentOperations()).getExperiment(id);
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.common.experiment.ExperimentBusinessInterface#getDataListEntitySet(edu.wustl.cab2b.common.domain.Experiment)
     */
    /**
     * get a set of root entities for an experiment where each root entity
     * represents a datalist
     * 
     * @param exp the experiment
     * @return set of root entities for an experiment where each root entity
     *         represents a datalist
     * @throws RemoteException
     */
    public Set<Set<EntityInterface>> getDataListEntitySet(Experiment exp) throws RemoteException {
        return (new ExperimentOperations()).getDataListEntitySet(exp);
    }

    /**
     * Adds a given experiment to specific Experiment Group.
     * @param experimentGroupId
     * @param experiment
     * @throws BizLogicException
     * @throws UserNotAuthorizedException
     * @throws DAOException
     * @throws RemoteException
     * @see edu.wustl.cab2b.common.experiment.ExperimentBusinessInterface#addExperiment(java.lang.Long,
     *      edu.wustl.cab2b.common.domain.Experiment)
     */
    public void addExperiment(Long experimentGroupId, Experiment experiment, String serializedDCR, String gridType)
            throws RemoteException, BizLogicException, UserNotAuthorizedException, DAOException {
        Long userId = UserUtility.getLocalUserId(serializedDCR, gridType);
        experiment.setUserId(userId);

        (new ExperimentOperations()).addExperiment(experimentGroupId, experiment);
    }

    /**
     * save the given data as a data category
     * 
     * @param title the title for the category
     * @param attributes list of attributes needed for the new entity
     * @param data the data to be saved
     * @return the newly saved entity
     * @throws RemoteException
     */
    public EntityInterface saveDataCategory(String title, List<AttributeInterface> attributes, Object[][] data)
            throws RemoteException {
        return new ExperimentOperations().saveDataCategory(title, attributes, data);

    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.common.experiment.ExperimentBusinessInterface#addDataListToExperiment(java.lang.Long, java.lang.Long)
     */
    /**
     * Adds a data list to given experiment.
     * @param experimentId data list to be added in this experiment
     * @param dataListMataDataId
     * @throws RemoteException
     */
    public void addDataListToExperiment(Long experimentId, Long dataListMataDataId) throws RemoteException {
        new ExperimentOperations().addDataListToExperiment(experimentId, dataListMataDataId);
    }

    /**
     * Returns root Id of specified datalist 
     * @param dl
     * @return long 
     * @throws HibernateException
     */
    public long getRootId(DataListMetadata dl) throws HibernateException {
        return new ExperimentOperations().getRootId(dl);
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.common.experiment.ExperimentBusinessInterface#getDataCategoryModel(edu.wustl.cab2b.common.domain.Experiment)
     */
    /**
     * Returns data category data model
     * @param experiment
     * @return CustomDataCategoryModel
     * @throws CheckedException
     */
    public CustomDataCategoryModel getDataCategoryModel(Experiment experiment) throws CheckedException {
        return new ExperimentOperations().getDataCategoryModel(experiment);
    }

    /**
     * Returns entity by its Id
     * @param id
     * @return EntityInterface
     * @throws CheckedException
     * @throws RemoteException
     */
    public EntityInterface getEnityById(Long id) throws CheckedException, RemoteException {
        return new ExperimentOperations().getEnityById(id);
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.common.experiment.ExperimentBusinessInterface#getAllAttributes(java.lang.Long)
     */
    /**
     * Returns collection of all attributes for specified Experiment ID
     * @param id
     * @return Collection of all attributes
     * @throws RemoteException
     * @throws CheckedException
     */
    public Collection<AttributeInterface> getAllAttributes(Long id) throws RemoteException, CheckedException {
        return new ExperimentOperations().getAllAttributes(id);
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.common.experiment.ExperimentBusinessInterface#getExperimentsForUser(edu.wustl.cab2b.common.user.UserInterface)
     */
    /**
     * Returns list of all experiments for <code>user</code>
     * @param user
     * @param dref
     * @param idP
     * @return List of experiments for <code>user</code>
     * @throws RemoteException
     */
    public List<Experiment> getExperimentsForUser(UserInterface user, String serializedDCR, String gridType)
            throws RemoteException {
        String userName = UserUtility.getUsersGridId(serializedDCR, gridType);
        return new ExperimentOperations().getLatestExperimentForUser(userName);
    }

    /**
     * This method returns false if Experiment with given name is not present in the database.
     * It returns false otherwise.
     * @param name
     * @return
     * @throws DAOException
     */
    public boolean isExperimentByNamePresent(String name) {
        return new ExperimentOperations().isExperimentByNamePresent(name);
    }
}
