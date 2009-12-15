package edu.wustl.common.querysuite.metadata.path;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.HashCodeBuilder;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.cache.AbstractEntityCache;

/**
 * @author Chandrakant Talele
 * @version 1.0
 * @created 20-Mar-2008 2:08:13 PM
 * @hibernate.class table="CURATED_PATH"
 * @hibernate.cache usage="read-write"
 */
public class CuratedPath implements ICuratedPath {

    private static final long serialVersionUID = -4723486424420197283L;

    private static final String CONNECTOR = "_";
    
    private Long curatedPathId;

    private Set<IPath> paths = new HashSet<IPath>();

    private Set<EntityInterface> entitySet = new HashSet<EntityInterface>();

    private boolean selected;

    public void addPath(IPath path) {
        paths.add(path);
    }

    /**
     * @return Returns the curated_path_Id.
     * 
     * @hibernate.id name="curatedPathId" column="CURATED_PATH_ID" type="long"
     *               length="30" unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="CURATED_PATH_SEQ"
     */
    public Long getCuratedPathId() {
        return curatedPathId;
    }

    /**
     * @return the entityIds
     * 
     * @hibernate.property name="entityIds" column="ENTITY_IDS" update="true"
     *                     insert="true" length="30"
     * 
     */
    @SuppressWarnings("unused")
    private String getEntityIds() {
        return getStringRepresentation(entitySet);
    }

    /**
     * @param entityIds the entityIds to set
     */
    @SuppressWarnings("unused")
    private void setEntityIds(String entityIds) {
        AbstractEntityCache cache = AbstractEntityCache.getCache();
        String[] entityIdentifiers = entityIds.split(CONNECTOR);
        for (String entityId : entityIdentifiers) {
            EntityInterface entity = cache.getEntityById(Long.parseLong(entityId));
            entitySet.add(entity);
        }
    }

    public Set<EntityInterface> getEntitySet() {
        return entitySet; 
    }

    /**
     * @return Returns the isSelected.
     * @hibernate.property name="selected" column="SELECTED" type="boolean"
     *                     unsaved-value="false" update="true" insert="true"
     * 
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * @hibernate.set name="paths" cascade="none" lazy="false" inverse="false"
     *                table="CURATED_PATH_TO_PATH"
     * @hibernate.collection-key column="CURATED_PATH_ID"
     * @hibernate.collection-many-to-many class="edu.wustl.common.querysuite.metadata.path.Path"
     *                                    column="PATH_ID"
     * @hibernate.cache usage="read-write"
     */
    public Set<IPath> getPaths() {
        return paths;
    }

    /**
     * @param curatedPathId The curatedPathId to set.
     */
    public void setCuratedPathId(Long curatedPathId) {
        this.curatedPathId = curatedPathId;
    }

    /**
     * @param entitySet The entitySet to set.
     */
    public void setEntitySet(Set<EntityInterface> entitySet) {
        this.entitySet = entitySet;
    }

    /**
     * @param isSelected The isSelected to set.
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * @param paths The paths to set.
     */
    public void setPaths(Set<IPath> paths) {
        this.paths = paths;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(paths).append(entitySet).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        boolean equals = false;
        if (this == obj) {
            equals = true;
        }

        if (!equals && obj instanceof CuratedPath) {
            CuratedPath curatedPath = (CuratedPath) obj;
            if (!paths.isEmpty() && paths.equals(curatedPath.getPaths()) && !entitySet.isEmpty()
                    && entitySet.equals(curatedPath.getEntitySet())) {
                equals = true;
            }
        }
        return equals;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for (IPath path : paths) {
            string.append(path.getPathId() + " ");
        }
        for (EntityInterface entity : entitySet) {
            string.append(entity.getId() + " ");
        }
        return "[" + string.toString() + "]";
    }
    
    /**
     * Generates string representation of given entity set after sorting it based on id.
     * String representation is IDs of entities concatenated by {@link PathConstants#ID_CONNECTOR}
     * @param entitySet Set of entities
     * @return String representation of the given entity set. 
     */
    public static String getStringRepresentation(Set<EntityInterface> entitySet) {
        if (entitySet.isEmpty()) {
            return "";
        }
        ArrayList<Long> ids = new ArrayList<Long>(entitySet.size());
        for (EntityInterface en : entitySet) {
            ids.add(en.getId());

        }
        Collections.sort(ids);
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(ids.get(0));
        for (int i = 1; i < ids.size(); i++) {
            strBuilder.append(CONNECTOR);
            strBuilder.append(ids.get(i));
        }
        return strBuilder.toString();
    }
}