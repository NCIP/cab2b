package edu.wustl.cab2b.server.ejb.experiment;

import java.rmi.RemoteException;
import java.util.Set;
import java.util.Vector;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.domain.Experiment;
import edu.wustl.cab2b.common.experiment.ExperimentBusinessInterface;
import edu.wustl.cab2b.server.ejb.AbstractStatelessSessionBean;
import edu.wustl.cab2b.server.experiment.ExperimentOperations;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * A class containing experiment related business logic.
 * @author chetan_bh
 *
 */
public class ExperimentSessionBean extends AbstractStatelessSessionBean implements ExperimentBusinessInterface {

    private static final long serialVersionUID = 782660710949035029L;

    public void addExperiment(Object exp) throws BizLogicException, UserNotAuthorizedException, RemoteException {
        (new ExperimentOperations()).addExperiment(exp);
    }

    public void copy(Object exp, Object tarExpGrp) throws BizLogicException, UserNotAuthorizedException,
            RemoteException {
        (new ExperimentOperations()).copy(exp, tarExpGrp);
    }

    public Vector getExperimentHierarchy() throws ClassNotFoundException, DAOException, RemoteException {
        return (new ExperimentOperations()).getExperimentHierarchy();
    }

    public void move(Object exp, Object srcExpGrp, Object tarExpGrp) throws DAOException, BizLogicException,
            UserNotAuthorizedException, RemoteException {
        (new ExperimentOperations()).move(exp, srcExpGrp, tarExpGrp);
    }

    public Experiment getExperiment(Long id) throws RemoteException, DAOException {

        return (new ExperimentOperations()).getExperiment(id);
    }

    public Set<EntityInterface> getDataListEntityNames(Experiment exp) throws RemoteException {
        return (new ExperimentOperations()).getDataListEntityNames(exp);
    }

    /**
     * @see edu.wustl.cab2b.common.experiment.ExperimentBusinessInterface#addExperiment(java.lang.Long, edu.wustl.cab2b.common.domain.Experiment)
     */
    public void addExperiment(Long experimentGroupId, Experiment experiment) throws RemoteException,BizLogicException,
            UserNotAuthorizedException, DAOException {
        (new ExperimentOperations()).addExperiment(experimentGroupId, experiment);
    }

}
