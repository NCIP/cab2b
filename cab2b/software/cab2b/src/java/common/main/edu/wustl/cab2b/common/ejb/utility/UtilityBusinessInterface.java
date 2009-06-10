package edu.wustl.cab2b.common.ejb.utility;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2b.common.BusinessInterface;
import edu.wustl.cab2b.common.queryengine.result.ILazyParams;
import edu.wustl.cab2b.common.queryengine.result.IPartiallyInitializedRecord;
import edu.wustl.common.querysuite.metadata.path.CuratedPath;

/**
 * @author Chandrakant Talele
 */
public interface UtilityBusinessInterface extends BusinessInterface {
	
    /**
     * Returns caB2B entity groups
     * @return
     * @throws RemoteException
     */
    Collection<EntityGroupInterface> getCab2bEntityGroups() throws RemoteException;

    /**
     * Retunrs incoming intramodel associations
     * @return associations with given entity as the target entity.
     * @throws RemoteException EJB specific exception.
     */
    Collection<AssociationInterface> getIncomingIntramodelAssociations(Long entityId) throws RemoteException;

    /**
     * Inserts cab2bObject
     * @param cab2bObject
     * @throws RemoteException
     */
    void insert(Object cab2bObject) throws RemoteException;

    /**
     * Updates a cab2bObject
     * @param cab2bObject
     * @throws RemoteException
     */
    void update(Object cab2bObject) throws RemoteException;

    /**
     * unregister for a given handle
     * @param handle
     * @throws RemoteException
     */
    void unregister(int handle) throws RemoteException;

    /**
     * Returns a view for a given handle
     * @param handle
     * @param params
     * @return
     * @throws RemoteException
     */
    IPartiallyInitializedRecord<?, ?> getView(int handle, ILazyParams params) throws RemoteException;

    /**
     * This method returns the list of tree set containing the unique record values for a given entity identifier.
     * Tree set stores the values in sorted order. 
     * @param entityId
     * @return
     * @throws RemoteException
     */
    List<TreeSet<Comparable<?>>> getUniqueRecordValues(Long entityId) throws RemoteException;

//    /**
//     * Resreshes Path cache and EntityCache if boolean passed is true
//     * @param resheshEntityCache If to refresh entity cache
//     * @throws RemoteException
//     */
//    void refreshPathAndEntityCache(boolean resheshEntityCache) throws RemoteException;
//
//    /**
//     * Resreshes Category cache and EntityCache.
//     * @throws RemoteException
//     */
//    void refreshCategoryAndEntityCache() throws RemoteException;

    /**
     * Adds curated path to cache.
     * @param curatedPath path to be added to cache.
     * @throws RemoteException
     */
    void addCuretedPathToCache(CuratedPath curatedPath) throws RemoteException;
    
    void refreshCache() throws RemoteException;
}
