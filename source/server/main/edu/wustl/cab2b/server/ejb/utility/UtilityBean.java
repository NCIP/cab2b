package edu.wustl.cab2b.server.ejb.utility;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.ejb.SessionBean;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.wustl.cab2b.common.dynamicextensionsstubs.AssociationWrapper;
import edu.wustl.cab2b.common.ejb.utility.UtilityBusinessInterface;
import edu.wustl.cab2b.common.queryengine.result.ILazyParams;
import edu.wustl.cab2b.common.queryengine.result.IPartiallyInitializedRecord;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2b.server.datalist.DataListOperationsController;
import edu.wustl.cab2b.server.ejb.AbstractStatelessSessionBean;
import edu.wustl.cab2b.server.queryengine.LazyInitializer;
import edu.wustl.cab2b.server.util.DynamicExtensionUtility;
import edu.wustl.cab2b.server.util.UtilityOperations;

/**
 * EJB to provide utility methods which are needed by client side, but which needs to aceess server.
 * @author Chandrakant Talele
 */
public class UtilityBean extends AbstractStatelessSessionBean implements SessionBean, UtilityBusinessInterface {

    private static final long serialVersionUID = 1L;

    /**
     * @see edu.wustl.cab2b.common.ejb.utility.UtilityBusinessInterface#getCab2bEntityGroups()
     */
    public Collection<EntityGroupInterface> getCab2bEntityGroups() throws RemoteException {
        return EntityCache.getInstance().getEntityGroups();
    }

    /**
     * @return associations with given entity as the target entity.
     * @throws RemoteException EJB specific exception.
     * @see edu.wustl.cab2b.common.ejb.path.PathFinderBusinessInterface#getIncomingIntramodelAssociations(Long)
     */
    public Collection<AssociationInterface> getIncomingIntramodelAssociations(Long entityId)
            throws RemoteException {
        Collection<AssociationInterface> associations = DynamicExtensionUtility.getIncomingIntramodelAssociations(entityId);
        return postProcess(associations);
    }

    /**
     * @param associations Association to process
     * @return Collection of Associations
     */
    private Collection<AssociationInterface> postProcess(Collection<AssociationInterface> associations) {
        Collection<AssociationInterface> res = new ArrayList<AssociationInterface>();

        for (AssociationInterface association : associations) {
            res.add(new AssociationWrapper(association));
        }
        return res;
    }

    /**
     * @see edu.wustl.cab2b.common.ejb.utility.UtilityBusinessInterface#insert(java.lang.Object)
     */
    public void insert(Object cab2bObject) throws RemoteException {
        new UtilityOperations().insert(cab2bObject);
    }

    /**
     * @see edu.wustl.cab2b.common.ejb.utility.UtilityBusinessInterface#update(java.lang.Object)
     */
    public void update(Object cab2bObject) throws RemoteException {
        new UtilityOperations().update(cab2bObject);
    }

    /**
     * @see edu.wustl.cab2b.common.ejb.utility.UtilityBusinessInterface#getView(int, edu.wustl.cab2b.common.queryengine.result.ILazyParams)
     */
    public IPartiallyInitializedRecord<?, ?> getView(int handle, ILazyParams params) throws RemoteException {
        return LazyInitializer.getView(handle, params);
    }

    /**
     * @see edu.wustl.cab2b.common.ejb.utility.UtilityBusinessInterface#unregister(int)
     */
    public void unregister(int handle) throws RemoteException {
        LazyInitializer.unregister(handle);
    }

    /**
     * This method returns the list of tree set containing the unique record values for a given entity identifier.
     * Tree set stores the values in sorted order. 
     * @param entityId
     * @return
     * @throws RemoteException
     */
    public List<TreeSet<Comparable<?>>> getUniqueRecordValues(Long entityId) throws RemoteException {
        List<IRecord> entityRecordList = DataListOperationsController.getEntityRecords(entityId);
        ArrayList<TreeSet<Comparable<?>>> entityRecordValues = new ArrayList<TreeSet<Comparable<?>>>();

        int index = 0;
        for (IRecord entityRecord : entityRecordList) {
            Set<AttributeInterface> attributeSet = entityRecord.getAttributes();
            List<AttributeInterface> attributeList = Utility.getAttributeList(attributeSet);
            for (AttributeInterface attribute : attributeList) {
                TreeSet<Comparable<?>> columnValues = null;
                try {
                    columnValues = entityRecordValues.get(index);
                } catch (IndexOutOfBoundsException e) {
                    columnValues = new TreeSet<Comparable<?>>();
                    entityRecordValues.add(index, columnValues);
                }
                
                Comparable<?> attributeValue = (Comparable<?>) entityRecord.getValueForAttribute(attribute);
                if (attributeValue != null) {
                    columnValues.add(attributeValue);
                }
                
                index++;
            }
            index = 0;
        }

        return entityRecordValues;
    }
}
