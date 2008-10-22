package edu.wustl.cab2b.server.ejb.experiment;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.globus.gsi.GlobusCredential;
import org.hibernate.HibernateException;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.CustomDataCategoryModel;
import edu.wustl.cab2b.common.domain.DataListMetadata;
import edu.wustl.cab2b.common.domain.Experiment;
import edu.wustl.cab2b.common.exception.CheckedException;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.experiment.ExperimentBusinessInterface;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.ejb.AbstractStatelessSessionBean;
import edu.wustl.cab2b.server.experiment.ExperimentOperations;
import edu.wustl.cab2b.server.user.UserOperations;
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
     * @see edu.wustl.cab2b.common.experiment.ExperimentBusinessInterface#addExperiment(java.lang.Object)
     */
    public void addExperiment(Object exp) throws BizLogicException, UserNotAuthorizedException, RemoteException {
        (new ExperimentOperations()).addExperiment(exp);
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.common.experiment.ExperimentBusinessInterface#copy(java.lang.Object, java.lang.Object)
     */
    public void copy(Object exp, Object tarExpGrp) throws BizLogicException, UserNotAuthorizedException,
            RemoteException {
        (new ExperimentOperations()).copy(exp, tarExpGrp);
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.common.experiment.ExperimentBusinessInterface#getExperimentHierarchy()
     */
    public Vector getExperimentHierarchy(String dref) throws ClassNotFoundException, DAOException, RemoteException {

        String userId = null;
        try {
            userId = UserOperations.getGlobusCredential(dref).getIdentity();

        } catch (Exception e) {
            throw new RuntimeException("Unable to deserialize client delegated ref", e.getMessage());
        }

        return (new ExperimentOperations()).getExperimentHierarchy(userId);
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.common.experiment.ExperimentBusinessInterface#move(java.lang.Object, java.lang.Object, java.lang.Object)
     */
    public void move(Object exp, Object srcExpGrp, Object tarExpGrp) throws DAOException, BizLogicException,
            UserNotAuthorizedException, RemoteException {
        (new ExperimentOperations()).move(exp, srcExpGrp, tarExpGrp);
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.common.experiment.ExperimentBusinessInterface#getExperiment(java.lang.Long)
     */
    public Experiment getExperiment(Long id) throws RemoteException, DAOException {
        return (new ExperimentOperations()).getExperiment(id);
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.common.experiment.ExperimentBusinessInterface#getDataListEntitySet(edu.wustl.cab2b.common.domain.Experiment)
     */
    public Set<Set<EntityInterface>> getDataListEntitySet(Experiment exp) throws RemoteException {
        return (new ExperimentOperations()).getDataListEntitySet(exp);
    }

    /**
     * @see edu.wustl.cab2b.common.experiment.ExperimentBusinessInterface#addExperiment(java.lang.Long,
     *      edu.wustl.cab2b.common.domain.Experiment)
     */
    public void addExperiment(Long experimentGroupId, Experiment experiment, String serializedCredRef)
            throws RemoteException, BizLogicException, UserNotAuthorizedException, DAOException {
        try {
            String userId = UserOperations.getGlobusCredential(serializedCredRef).getIdentity();
            experiment.setUserId(userId);
        } catch (Exception e) {
            throw new RuntimeException("Unable to deserialize client delegated ref", e.getMessage());

        }

        (new ExperimentOperations()).addExperiment(experimentGroupId, experiment);
    }

    /**
     * save the given data as a data category
     * 
     * @param title
     *            the title for the category
     * @param attributes
     *            list of attributes needed for the new entity
     * @param data
     *            the data to be saved
     * @return the newly created entity
     */
    public EntityInterface saveDataCategory(String title, List<AttributeInterface> attributes, Object[][] data)
            throws RemoteException {
        return new ExperimentOperations().saveDataCategory(title, attributes, data);

    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.common.experiment.ExperimentBusinessInterface#addDataListToExperiment(java.lang.Long, java.lang.Long)
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
    public Collection<AttributeInterface> getAllAttributes(Long id) throws RemoteException, CheckedException {
        return new ExperimentOperations().getAllAttributes(id);
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.common.experiment.ExperimentBusinessInterface#getExperimentsForUser(edu.wustl.cab2b.common.user.UserInterface)
     */
    public List<Experiment> getExperimentsForUser(UserInterface user, String dref) throws RemoteException {
        try {
            GlobusCredential cred = UserOperations.getGlobusCredential(dref);

            return new ExperimentOperations().getLatestExperimentForUser(user, cred.getIdentity());
        } catch (Exception e) {
            throw new RuntimeException("IO error occured ", e.getMessage());
        }
    }
}
