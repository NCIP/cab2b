package edu.wustl.cab2b.server.ejb.experiment;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import net.sf.hibernate.HibernateException;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.cab2b.common.CustomDataCategoryModel;
import edu.wustl.cab2b.common.IdName;
import edu.wustl.cab2b.common.domain.DataListMetadata;
import edu.wustl.cab2b.common.domain.Experiment;
import edu.wustl.cab2b.common.exception.CheckedException;
import edu.wustl.cab2b.common.experiment.ExperimentBusinessInterface;
import edu.wustl.cab2b.server.ejb.AbstractStatelessSessionBean;
import edu.wustl.cab2b.server.experiment.ExperimentOperations;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.querysuite.metadata.category.CategorialAttribute;
import edu.wustl.common.querysuite.metadata.category.CategorialClass;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * A class containing experiment related business logic.
 * 
 * @author chetan_bh
 * 
 */
public class ExperimentSessionBean extends AbstractStatelessSessionBean implements
		ExperimentBusinessInterface {

	private static final long serialVersionUID = 782660710949035029L;

	public void addExperiment(Object exp) throws BizLogicException, UserNotAuthorizedException,
			RemoteException {
		(new ExperimentOperations()).addExperiment(exp);
	}

	public void copy(Object exp, Object tarExpGrp) throws BizLogicException,
			UserNotAuthorizedException, RemoteException {
		(new ExperimentOperations()).copy(exp, tarExpGrp);
	}

	public Vector getExperimentHierarchy() throws ClassNotFoundException, DAOException,
			RemoteException {
		return (new ExperimentOperations()).getExperimentHierarchy();
	}

	public void move(Object exp, Object srcExpGrp, Object tarExpGrp) throws DAOException,
			BizLogicException, UserNotAuthorizedException, RemoteException {
		(new ExperimentOperations()).move(exp, srcExpGrp, tarExpGrp);
	}

	public Experiment getExperiment(Long id) throws RemoteException, DAOException {

		return (new ExperimentOperations()).getExperiment(id);
	}

	public Set<Set<EntityInterface>> getDataListEntitySet(Experiment exp) throws RemoteException {
		return (new ExperimentOperations()).getDataListEntitySet(exp);
	}

	/**
	 * @see edu.wustl.cab2b.common.experiment.ExperimentBusinessInterface#addExperiment(java.lang.Long,
	 *      edu.wustl.cab2b.common.domain.Experiment)
	 */
	public void addExperiment(Long experimentGroupId, Experiment experiment)
			throws RemoteException, BizLogicException, UserNotAuthorizedException, DAOException {
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
	public EntityInterface saveDataCategory(String title, List<AttributeInterface> attributes,
			Object[][] data) throws RemoteException {
		return new ExperimentOperations().saveDataCategory(title, attributes, data);

	}

	public void addDataListToExperiment(Long experimentId, Long dataListMataDataId)
			throws RemoteException {
		new ExperimentOperations().addDataListToExperiment(experimentId, dataListMataDataId);
	}

	public long getRootId(DataListMetadata dl) throws HibernateException {
		return new ExperimentOperations().getRootId(dl);
	}

	public CustomDataCategoryModel getDataCategoryModel() throws CheckedException {
		return new ExperimentOperations().getDataCategoryModel();
	}


	public EntityInterface getEnityById(Long id) throws CheckedException, RemoteException {
		return new ExperimentOperations().getEnityById(id);
	}

	public Collection<AttributeInterface> getAllAttributes(Long id) throws RemoteException,
			CheckedException {
		return new ExperimentOperations().getAllAttributes(id);
	}
}
