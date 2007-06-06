package edu.wustl.cab2b.client.cache;

import java.awt.Rectangle;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.client.ui.mainframe.MainFrame;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.beans.MatchedClass;
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
     * The EntityCache object. Needed for singleton
     */
    protected static ClientSideCache entityCache = null;

    private Map<Category, Set<EntityInterface>> categoryVsClasseSet = new HashMap<Category, Set<EntityInterface>>();

    private Map<Category, Set<AttributeInterface>> categoryVsAttributeSet = new HashMap<Category, Set<AttributeInterface>>();

    /**
     * @return the singleton instance of the EntityCache class.
     */
    public static synchronized ClientSideCache getInstance() {
        if (entityCache == null) {
            entityCache = new ClientSideCache();
        }
        return entityCache;
    }

    protected ClientSideCache() {
        super();
        CategoryBusinessInterface categoryOperations = (CategoryBusinessInterface) CommonUtils.getBusinessInterface(EjbNamesConstants.CATEGORY_BEAN,CategoryHomeInterface.class);
        showProgress("Getting all categories...", 60);
        try {
            categories = categoryOperations.getAllCategories();
            for (Category category : categories) {
                categoryVsClasseSet.put(category, categoryOperations.getAllSourceClasses(category));
                categoryVsAttributeSet.put(category, categoryOperations.getAllSourceAttributes(category));
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        showProgress("Loading cache completed...", 80);
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.common.cache.AbstractEntityCache#getCab2bEntityGroups()
     */
    @Override
    protected Collection<EntityGroupInterface> getCab2bEntityGroups() {
        try {
            UtilityBusinessInterface util = (UtilityBusinessInterface) CommonUtils.getBusinessInterface(
                                                                                                        EjbNamesConstants.UTILITY_BEAN,
                                                                                                        UtilityHomeInterface.class);
            showProgress("Getting all entities...", 10);
            Collection<EntityGroupInterface> collection = util.getCab2bEntityGroups();
            showProgress("Populating internal data structures...", 40);
            return collection;
        } catch (RemoteException dynSysExp) {
            throw new RuntimeException(dynSysExp.getMessage(), dynSysExp);
        }
    }

    /**
     * @param text Text to show
     * @param completedvalue Percentage completed
     */
    private void showProgress(String text, int completedvalue) {

        JLabel label = MainFrame.getProgressBarLabel();
        label.setText(text);
        Rectangle labelRect = label.getBounds();
        labelRect.x = 0;
        labelRect.y = 0;
        label.paintImmediately(labelRect);

        JProgressBar progress = MainFrame.getProgressBar();
        progress.setValue(completedvalue);
        Rectangle progressRect = progress.getBounds();
        progressRect.x = 0;
        progressRect.y = 0;
        progress.paintImmediately(progressRect);
    }

    /**
     * Returns the Entity objects whose source classes fields match with the respective not null 
     * fields in the passed entity object.
     * @param entity The entity object.
     * @return the Entity objects whose source classes fields match with the respective not null 
     * fields in the passed entity object.
     */
    public MatchedClass getCategories(Collection<EntityInterface> patternEntityCollection) {
        MatchedClass matchedClass = new MatchedClass();
        for (Entry<Category, Set<EntityInterface>> entry : categoryVsClasseSet.entrySet()) {
            Category category = entry.getKey();
            Set<EntityInterface> classesInCategory = entry.getValue();

            for (EntityInterface classInCategory : classesInCategory) {
                for (EntityInterface patternEntity : patternEntityCollection) {
                    if (CompareUtil.compare(classInCategory, patternEntity)) {
                        long deEntityID = category.getDeEntityId();
                        EntityInterface entityInterface = getEntityById(deEntityID);
                        matchedClass.getEntityCollection().add(entityInterface);
                    }
                }
            }
        }
        return matchedClass;
    }

    /**
     * Returns the Entity objects whose attributes's source classes fields match with the respective not null 
     * fields in the passed entity object.
     * @param entity The entity object.
     * @return the Entity objects whose attributes's source classes fields match with the respective not null 
     * fields in the passed entity object.
     */
    public MatchedClass getCategoriesAttributes(Collection<AttributeInterface> patternAttributeCollection) {
        MatchedClass matchedClass = new MatchedClass();
        for (Entry<Category, Set<AttributeInterface>> entry : categoryVsAttributeSet.entrySet()) {
            Category category = entry.getKey();
            Set<AttributeInterface> attributesInCategory = entry.getValue();

            for (AttributeInterface attributeInCategory : attributesInCategory) {
                for (AttributeInterface patternAttribute : patternAttributeCollection) {
                    if (CompareUtil.compare(attributeInCategory, patternAttribute)) {
                        long deEntityID = category.getDeEntityId();
                        EntityInterface entityInterface = getEntityById(deEntityID);
                        matchedClass.getEntityCollection().add(entityInterface);
                    }
                }
            }
        }
        return matchedClass;
    }
}