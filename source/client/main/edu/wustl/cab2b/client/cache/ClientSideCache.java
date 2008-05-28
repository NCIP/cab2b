package edu.wustl.cab2b.client.cache;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.client.ui.mainframe.ClientLauncher;
import edu.wustl.cab2b.client.ui.mainframe.GlobalNavigationGlassPane;
import edu.wustl.cab2b.client.ui.mainframe.GlobalNavigationPanel;
import edu.wustl.cab2b.client.ui.mainframe.MainFrame;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.beans.MatchedClass;
import edu.wustl.cab2b.common.beans.MatchedClassEntry;
import edu.wustl.cab2b.common.cache.AbstractEntityCache;
import edu.wustl.cab2b.common.cache.CompareUtil;
import edu.wustl.cab2b.common.ejb.EjbNamesConstants;
import edu.wustl.cab2b.common.ejb.category.CategoryBusinessInterface;
import edu.wustl.cab2b.common.ejb.category.CategoryHomeInterface;
import edu.wustl.cab2b.common.ejb.utility.UtilityBusinessInterface;
import edu.wustl.cab2b.common.ejb.utility.UtilityHomeInterface;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.common.querysuite.metadata.category.Category;

/**
 * @author Chandrakant Talele
 */
public class ClientSideCache extends AbstractEntityCache {

    private static final long serialVersionUID = 7145001746845163416L;

    /**
     * This is Map with KEY: category and VALUE: set of classes used in forming the category 
     */
    private Map<Category, Set<EntityInterface>> categoryVsClasseSet = new HashMap<Category, Set<EntityInterface>>();
    /**
     * This is Map with KEY: category and VALUE: set of attributes used in forming the category 
     */
    private Map<Category, Set<AttributeInterface>> categoryVsAttributeSet = new HashMap<Category, Set<AttributeInterface>>();

    /**
     * @return the singleton instance of the EntityCache class.
     */
    public static synchronized ClientSideCache getInstance() {
        if (entityCache == null) {
            entityCache = new ClientSideCache();
        }
        return (ClientSideCache) entityCache;
    }

    /**
     * Initializes the cache and also shows the progress information
     */
    protected ClientSideCache() {
        super();
        CategoryBusinessInterface categoryOperations = (CategoryBusinessInterface) CommonUtils.getBusinessInterface(EjbNamesConstants.CATEGORY_BEAN,
                                                                     CategoryHomeInterface.class);
        int length = 50;
        ClientLauncher clientLauncher = ClientLauncher.getInstance();
        clientLauncher.showProgress(" Fetching data from caB2B Server....", length);
        try {
            categories = categoryOperations.getAllCategories();
            int offset = 40;
            if(!categories.isEmpty()) {
                offset = offset / categories.size();
            } 
            for (Category category : categories) {
                categoryVsClasseSet.put(category, categoryOperations.getAllSourceClasses(category));
                categoryVsAttributeSet.put(category, categoryOperations.getAllSourceAttributes(category));
                length = length + offset;
                clientLauncher.showProgress(" Populating internal data structures....", length);
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * @see edu.wustl.cab2b.common.cache.AbstractEntityCache#getCab2bEntityGroups()
     */
    @Override
    protected Collection<EntityGroupInterface> getCab2bEntityGroups() {
        Collection<EntityGroupInterface> collection = new ArrayList<EntityGroupInterface>(0);
        ClientLauncher clientLauncher = ClientLauncher.getInstance();
        clientLauncher.showProgress(" Contacting caB2B Server....", 1);
        try {
            UtilityBusinessInterface util = (UtilityBusinessInterface) CommonUtils.getBusinessInterface(
                                                                                                        EjbNamesConstants.UTILITY_BEAN,
                                                                                                        UtilityHomeInterface.class);
            clientLauncher.setDeterminate();
            clientLauncher.showProgress(" Fetching data from caB2B Server....", 10);
            collection = util.getCab2bEntityGroups();

            clientLauncher.showProgress(" Populating internal data structures....", 30);
        } catch (Exception dynSysExp) {
            CommonUtils.handleException(dynSysExp, null, true, true, false, true);
        }
        return collection;
    }

    /**
     * Returns the Entity objects whose source classes fields match with the
     * respective not null fields in the passed entity object.
     * 
     * @param entity The entity object.
     * @return the Entity objects whose source classes fields match with the
     *         respective not null fields in the passed entity object.
     */
    public MatchedClass getCategories(Collection<EntityInterface> patternEntityCollection) {
        MatchedClass matchedClass = new MatchedClass();
        for (Entry<Category, Set<EntityInterface>> entry : categoryVsClasseSet.entrySet()) {
            Category category = entry.getKey();
            Set<EntityInterface> classesInCategory = entry.getValue();

            for (EntityInterface classInCategory : classesInCategory) {
                for (EntityInterface patternEntity : patternEntityCollection) {
                    MatchedClassEntry matchedClassEntry = CompareUtil.compare(classInCategory, patternEntity);
                    if (matchedClassEntry != null) {
                        long deEntityID = category.getDeEntityId();
                        EntityInterface entityInterface = getEntityById(deEntityID);
                        matchedClass.getEntityCollection().add(entityInterface);
                        matchedClass.addMatchedClassEntry(matchedClassEntry);
                    }
                }
            }
        }
        return matchedClass;
    }

    /**
     * Returns the Entity objects whose attributes's source classes fields match
     * with the respective not null fields in the passed entity object.
     * 
     * @param entity The entity object.
     * @return the Entity objects whose attributes's source classes fields match
     *         with the respective not null fields in the passed entity object.
     */
    public MatchedClass getCategoriesAttributes(Collection<AttributeInterface> patternAttributeCollection) {
        MatchedClass matchedClass = new MatchedClass();
        for (Entry<Category, Set<AttributeInterface>> entry : categoryVsAttributeSet.entrySet()) {
            Category category = entry.getKey();
            Set<AttributeInterface> attributesInCategory = entry.getValue();

            for (AttributeInterface attributeInCategory : attributesInCategory) {
                for (AttributeInterface patternAttribute : patternAttributeCollection) {
                    MatchedClassEntry matchedClassEntry = CompareUtil.compare(attributeInCategory, patternAttribute);
                    if (matchedClassEntry != null) {
                        long deEntityID = category.getDeEntityId();
                        EntityInterface entityInterface = getEntityById(deEntityID);
                        matchedClass.getEntityCollection().add(entityInterface);
                        matchedClass.addMatchedClassEntry(matchedClassEntry);
                    }
                }
            }
        }
        return matchedClass;
    }
}