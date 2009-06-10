package edu.wustl.cab2b.common.ejb.path;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.BusinessInterface;
import edu.wustl.common.querysuite.metadata.associations.IInterModelAssociation;
import edu.wustl.common.querysuite.metadata.path.ICuratedPath;
import edu.wustl.common.querysuite.metadata.path.IPath;

/**
 * This BusinessInterface is used to find all possible paths in which a collection of classes can be connected to a destination.
 * It finds inter model as well as intra model paths. A path is defined as ordered list of associations.
 * This class internally refers to tables PATH,INTER_MODEL_ASSOCIATION,INTRA_MODEL_ASSOCIATION,ASSOCIATION.
 * 
 * @author Chandrakant Talele
 */
public interface PathFinderBusinessInterface extends BusinessInterface {
    /**
     * Finds all possible paths in which the source entity can be connected to a destination entity.
     * Returns list of all possible paths. This method can be used when a new class is added in DAG view.
     * 
     * @param source Starting point of the path.
     * @param destination End of the path 
     * @return Returns the list of IPath present between given source and destination
     * @throws RemoteException EJB specific exception.
     */
    public List<IPath> getAllPossiblePaths(EntityInterface source,EntityInterface destination) throws RemoteException;

    /**
     * Returns all the InterModelAssociations where given entity is present.
     * @param sourceEntityId id of the source Entity
     * @return The list of IInterModelAssociation
     * @throws RemoteException EJB specific exception.
     */
    public List<IInterModelAssociation> getInterModelAssociations(Long sourceEntityId)
            throws RemoteException;
    
    /**
     * Finds all curated paths defined for given source and desination entity.
     * If no curated path is present, a empty set will be returned.
     * @param source The source entity
     * @param destination The destination entity
     * @return Set of curated paths
     * @throws RemoteException EJB specific exception.
     */
    public Set<ICuratedPath> getCuratedPaths(EntityInterface source, EntityInterface destination) throws  RemoteException;
    /**
     * Finds all curated paths defined for given set of entities.
     * @param entitySet Set of entities
     * @return Set of curated paths
     * @throws RemoteException EJB specific exception.
     */
    public Set<ICuratedPath> autoConnect(Set<EntityInterface> entitySet) throws  RemoteException;
}