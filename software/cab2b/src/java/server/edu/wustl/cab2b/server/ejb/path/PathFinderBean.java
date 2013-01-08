/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.ejb.path;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.ejb.path.PathFinderBusinessInterface;
import edu.wustl.cab2b.server.ejb.AbstractStatelessSessionBean;
import edu.wustl.cab2b.server.path.PathFinder;
import edu.wustl.common.querysuite.metadata.associations.IInterModelAssociation;
import edu.wustl.common.querysuite.metadata.path.ICuratedPath;
import edu.wustl.common.querysuite.metadata.path.IPath;

/**
 * This EJB is used to find all possible paths in which a collection of classes can be connected to a destination.
 * It finds inter model as well as intra model paths. A path is defined as ordered list of associations.
 * This class internally refers to tables PATH,INTER_MODEL_ASSOCIATION,INTRA_MODEL_ASSOCIATION,ASSOCIATION.
 * 
 * @author Chandrakant Talele
 */
public class PathFinderBean extends AbstractStatelessSessionBean implements PathFinderBusinessInterface {
    private static final long serialVersionUID = 6029219492441048448L;

    /**
     * Finds all possible paths in which a source entity can be connected to a destination entity.
     * This method can be used when a new class is added in DAG view.
     * 
     * @param source collection of classes each of which will be treated as source.
     * @param destination End of the path 
     * @return Returns List of IPath.
     * @throws RemoteException EJB specific exception.
     * @see edu.wustl.cab2b.common.ejb.path.PathFinderBusinessInterface#getAllPossiblePaths(EntityInterface, EntityInterface)
     */
    public List<IPath> getAllPossiblePaths(EntityInterface source, EntityInterface destination)
            throws RemoteException {
        return PathFinder.getInstance().getAllPossiblePaths(source, destination);
    }

    /**
     * Returns all the InterModelAssociations where given entity is present.
     * @param sourceEntityId id of the source Entity
     * @return The list of IInterModelAssociation
     * @throws RemoteException EJB specific exception.
     * @see edu.wustl.cab2b.common.ejb.path.PathFinderBusinessInterface#getInterModelAssociations(Long)
     */
    public List<IInterModelAssociation> getInterModelAssociations(Long sourceEntityId) throws RemoteException {
        return PathFinder.getInstance().getInterModelAssociations(sourceEntityId);
    }

    /**
     * Finds all curated paths defined for given source and desination entity.
     * If no curated path is present, a empty set will be returned.
     * @param source The source entity
     * @param destination The destination entity
     * @return Set of curated paths
     * @throws RemoteException EJB specific exception.
     * @see edu.wustl.cab2b.common.ejb.path.PathFinderBusinessInterface#findPath(edu.common.dynamicextensions.domaininterface.EntityInterface, edu.common.dynamicextensions.domaininterface.EntityInterface)
     */
    public Set<ICuratedPath> getCuratedPaths(EntityInterface source, EntityInterface destination) throws RemoteException {
        return PathFinder.getInstance().getCuratedPaths(source, destination);
    }

    /**
     * Finds all curated paths defined for given set of entities.
     * @param entitySet Set of entities
     * @return Set of curated paths
     * @throws RemoteException EJB specific exception.
     * @see edu.wustl.cab2b.common.ejb.path.PathFinderBusinessInterface#autoConnect(Set)
     */
    public Set<ICuratedPath> autoConnect(Set<EntityInterface> entitySet) throws RemoteException {
        return PathFinder.getInstance().autoConnect(entitySet);
    }
}