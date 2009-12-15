package edu.wustl.common.querysuite.metadata.path;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.HashCodeBuilder;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;

/**
 * @author Chandrakant Talele
 * @version 1.0
 * @created 20-Mar-2008 2:08:13 PM
 * @hibernate.class table="PATH"
 * @hibernate.cache usage="read-write"
 */
public class Path implements IPath {

    /**
     * 
     */
    private static final long serialVersionUID = 2375912190167946239L;

    private long pathId;

    private EntityInterface sourceEntity;

    private EntityInterface targetEntity;

    private List<IAssociation> intermediateAssociations;

    private long sourceEntityId;

    private long targetEntityId;

    private String intermediatePaths;

    /**
     * 
     */
    public Path() {

    }

    public Path(EntityInterface sourceEntity, EntityInterface targetEntity,
            List<IAssociation> intermediateAssociations) {
        this(-1, sourceEntity, targetEntity, intermediateAssociations);
    }

    /**
     * @param pathId
     * @param sourceEntity
     * @param targetEntity
     * @param intermediateAssociations
     */
    public Path(long pathId, EntityInterface sourceEntity, EntityInterface targetEntity,
            List<IAssociation> intermediateAssociations) {
        this.pathId = pathId;
        this.sourceEntity = sourceEntity;
        this.targetEntity = targetEntity;
        this.intermediateAssociations = intermediateAssociations;
    }

    /**
     * @return the sourceEntityId
     */
    public long getSourceEntityId() {
        return sourceEntityId;
    }

    /**
     * @hibernate.property name="sourceEntityId" column="FIRST_ENTITY_ID"
     *                     type="long" length="30" unsaved-value="null"
     *                     update="true" insert="true" length="30"
     * 
     */
    public void setSourceEntityId(long sourceEntityId) {
        this.sourceEntityId = sourceEntityId;
    }

    /**
     * @return the targetEntityId
     * @hibernate.property name="targetEntityId" column="LAST_ENTITY_ID"
     *                     type="long" length="30" unsaved-value="null"
     *                     update="true" insert="true" length="30"
     * 
     */

    public long getTargetEntityId() {
        return targetEntityId;
    }

    /**
     * @param targetEntityId the targetEntityId to set
     */
    public void setTargetEntityId(long targetEntityId) {
        this.targetEntityId = targetEntityId;
    }

    /**
     * @return the intermediatePaths
     * @hibernate.property name="intermediatePaths" column="INTERMEDIATE_PATH"
     *                     update="true" insert="true" length="150"
     * 
     */

    public String getIntermediatePaths() {
        return intermediatePaths;
    }

    /**
     * @param intermediatePaths the intermediatePaths to set
     */
    public void setIntermediatePaths(String intermediatePaths) {
        this.intermediatePaths = intermediatePaths;
    }

    public EntityInterface getSourceEntity() {
        return sourceEntity;
    }

    public EntityInterface getTargetEntity() {
        return targetEntity;
    }

    public List<IAssociation> getIntermediateAssociations() {
        return intermediateAssociations;
    }

    /**
     * @see edu.wustl.common.querysuite.metadata.path.IPath#isBidirectional()
     */
    public boolean isBidirectional() {
        for (IAssociation association : intermediateAssociations) {
            if (!association.isBidirectional()) {
                return false;
            }
        }
        return true;
    }

    /**
     * @see edu.wustl.common.querysuite.metadata.path.IPath#reverse()
     */

    // BEGIN REVERSING
    public IPath reverse() {
        List<IAssociation> origAssociations = getIntermediateAssociations();
        List<IAssociation> newAssociations = new ArrayList<IAssociation>(origAssociations.size());
        for (IAssociation association : origAssociations) {
            if (!association.isBidirectional()) {
                throw new IllegalArgumentException("Path ain't bidirectional... cannot reverse.");
            }
            newAssociations.add(association.reverse());
        }
        return new Path(getTargetEntity(), getSourceEntity(), newAssociations);
    }

    /**
     * @param intermediateAssociations The intermediateAssociations to set.
     */
    public void setIntermediateAssociations(List<IAssociation> intermediateAssociations) {
        this.intermediateAssociations = intermediateAssociations;
    }

    /**
     * @param sourceEntity The sourceEntity to set.
     */
    public void setSourceEntity(EntityInterface sourceEntity) {
        this.sourceEntity = sourceEntity;
    }

    /**
     * @param targetEntity The targetEntity to set.
     */
    public void setTargetEntity(EntityInterface targetEntity) {
        this.targetEntity = targetEntity;
    }

    /**
     * 
     * @return Returns the curated_path_Id.
     * 
     * @hibernate.id name="pathId" column="PATH_ID" type="long" length="30"
     *               unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="PATH_SEQ"
     */

    public long getPathId() {
        return pathId;
    }

    /**
     * @param pathId the pathId to set.
     */
    public void setPathId(long pathId) {
        this.pathId = pathId;
    }

    @Override
    public String toString() {
        StringBuffer buff = new StringBuffer();
        buff.append("Start Entity : " + sourceEntity.getName());
        buff.append("End Entity : " + targetEntity.getName());
        for (IAssociation association : intermediateAssociations) {
            buff.append("\t" + association.toString());
        }
        return buff.toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(sourceEntity).append(targetEntity).append(intermediatePaths).append(
                intermediateAssociations).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        boolean equals = false;
        if (this == obj) {
            equals = true;
        }

        if (!equals && obj instanceof Path) {
            Path path = (Path) obj;
            if (pathId == path.getPathId())
                equals = true;
        }
        return equals;
    }
}
