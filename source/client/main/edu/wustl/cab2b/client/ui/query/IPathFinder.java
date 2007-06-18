/**
 * <p>Title: IPathFinder Class>
 * <p>Description:  This interface provides methods for finding the possible paths 
 * between entities.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.cab2b.client.ui.query;

import java.util.List;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.common.querysuite.metadata.associations.IInterModelAssociation;
import edu.wustl.common.querysuite.metadata.path.ICuratedPath;
import edu.wustl.common.querysuite.metadata.path.IPath;

/**
 * This interface provides methods for finding the possible paths 
 * between entities.
 * @author gautam_shetty
 * @author Chandrakant Talele
 */
public interface IPathFinder
{
    /**
     * Finds all possible paths in which the source entity can be connected to a destination entity.
     * Returns list of all possible paths. This method can be used when a new class is added in DAG view.
     * 
     * @param source Starting point of the path.
     * @param destination End of the path 
     * @return Returns the list of IPath present between given source and destination
     */
    public List<IPath> getAllPossiblePaths(EntityInterface source,EntityInterface destination);

    /**
     * Returns all the InterModelAssociations where given entity is present.
     * @param sourceEntityId id of the source Entity
     * @return The list of IInterModelAssociation
     */
    public List<IInterModelAssociation> getInterModelAssociations(Long sourceEntityId);
    
    /**
     * Finds all curated paths defined for given source and desination entity.
     * If no curated path is present, a empty set will be returned.
     * @param source The source entity
     * @param destination The destination entity
     * @return Set of curated paths
     */
    public Set<ICuratedPath> getCuratedPaths(EntityInterface source, EntityInterface destination);
    /**
     * Finds all curated paths defined for given set of entities.
     * @param entitySet Set of entities
     * @return Set of curated paths
     */
    public Set<ICuratedPath> autoConnect(Set<EntityInterface> entitySet);
}
