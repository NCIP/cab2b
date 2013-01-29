/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * <p>Title: caB2BPathFinder Class>
 * <p>Description:  This class locates the PathFinder bean and provides methods
 * for finding the possible paths between the entities.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.cab2b.client.ui.query;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.ejb.EjbNamesConstants;
import edu.wustl.cab2b.common.ejb.path.PathFinderBusinessInterface;
import edu.wustl.cab2b.common.ejb.path.PathFinderHomeInterface;
import edu.wustl.cab2b.common.locator.Locator;
import edu.wustl.cab2b.common.locator.LocatorException;
import edu.wustl.common.querysuite.metadata.associations.IInterModelAssociation;
import edu.wustl.common.querysuite.metadata.associations.IIntraModelAssociation;
import edu.wustl.common.querysuite.metadata.path.ICuratedPath;
import edu.wustl.common.querysuite.metadata.path.IPath;

/**
 * This class locates the PathFinder bean and provides methods for finding the 
 * possible paths between the entities.
 * @author gautam_shetty
 * @author Chandrakant Talele
 */
public class ClientPathFinder implements IPathFinder {
    private PathFinderBusinessInterface pathFinder = null;

    public ClientPathFinder() {
        //  Locate the PathFinder bean.
        pathFinder = (PathFinderBusinessInterface) Locator.getInstance().locate(
                                                   EjbNamesConstants.PATH_FINDER_BEAN,
                                                   PathFinderHomeInterface.class);
    }

    
    private class IPathComparator implements Comparator<IPath> {
    	  public int compare(IPath p1, IPath p2) {
    		  	if (p1.toString().length() < p2.toString().length()) {
    		        return -1;
    		     } else if (p1.toString().length() > p2.toString().length()) {
    		        return 1;
    		     } else {
    		       return 0;
    		     }
    		  }
    		}


    /**
     * Finds all possible paths in which the source entity can be connected to a destination entity.
     * Returns list of all possible paths. This method can be used when a new class is added in DAG view.
     * 
     * @param source Starting point of the path.
     * @param destination End of the path 
     * @return Returns the list of IPath present between given source and destination
     */
    public List<IPath> getAllPossiblePaths(EntityInterface source, EntityInterface destination) {
        try {
        	List<IPath> myIPathList=pathFinder.getAllPossiblePaths(source, destination);
        	Collections.sort(myIPathList,new IPathComparator());
        	return myIPathList;
        	//return pathFinder.getAllPossiblePaths(source, destination);
        } catch (LocatorException locExp) {
            locExp.printStackTrace();
        } catch (RemoteException remExp) {
            remExp.printStackTrace();
        }
        return null;
    }

    /**
     * Returns all the InterModelAssociations where given entity is present.
     * @param sourceEntityId id of the source Entity
     * @return The list of IInterModelAssociation
     */
    public List<IInterModelAssociation> getInterModelAssociations(Long sourceEntityId) {
        try {
            return pathFinder.getInterModelAssociations(sourceEntityId);
        } catch (LocatorException locExp) {
            locExp.printStackTrace();
        } catch (RemoteException remExp) {
            remExp.printStackTrace();
        }
        return null;
    }

    /**
     * Finds all curated paths defined for given source and desination entity.
     * If no curated path is present, a empty set will be returned.
     * @param source The source entity
     * @param destination The destination entity
     * @return Set of curated paths
     */
    public Set<ICuratedPath> getCuratedPaths(EntityInterface source, EntityInterface destination) {
        try {
            return pathFinder.getCuratedPaths(source, destination);
        } catch (LocatorException locExp) {
            locExp.printStackTrace();
        } catch (RemoteException remExp) {
            remExp.printStackTrace();
        }
        return null;
    }

    /**
     * Finds all curated paths defined for given set of entities.
     * @param entitySet Set of entities
     * @return Set of curated paths
     */
    public Set<ICuratedPath> autoConnect(Set<EntityInterface> entitySet) {
        try {
            return pathFinder.autoConnect(entitySet);
        } catch (LocatorException locExp) {
            locExp.printStackTrace();
        } catch (RemoteException remExp) {
            remExp.printStackTrace();
        }
        return null;
    }
    
    public IPath getPathForAssociations(List<IIntraModelAssociation> associations) {
        //TODO Needs to implement
        return null;
    }
}